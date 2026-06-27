package net.irisshaders.firis.mixin;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.gui.option.FirisVideoSettings;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Objects;

@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugScreenOverlay {
	@Unique
	private static final List<BufferPoolMXBean> firis$pools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);

	@Unique
	private static final BufferPoolMXBean firis$directPool;

	static {
		BufferPoolMXBean found = null;

		for (BufferPoolMXBean pool : firis$pools) {
			if (pool.getName().equals("direct")) {
				found = pool;
				break;
			}
		}

		firis$directPool = Objects.requireNonNull(found);
	}

	// stackoverflow.com/a/3758880
	@Unique
	private static String firis$humanReadableByteCountBin(long bytes) {
		long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
		if (absB < 1024) {
			return bytes + " B";
		}
		long value = absB;
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");
		for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
			value >>= 10;
			ci.next();
		}
		value *= Long.signum(bytes);
		return String.format("%.3f %ciB", value / 1024.0, ci.current());
	}

	// From Sodium
	@Unique
	private static long firis$getNativeMemoryUsage() {
		return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
	}

	@Inject(method = "getSystemInformation", at = @At("RETURN"))
	private void firis$appendShaderPackText(CallbackInfoReturnable<List<String>> cir) {
		List<String> messages = cir.getReturnValue();

		messages.add("");
		messages.add("[" + Firis.MODNAME + "] Version: " + Firis.getFormattedVersion());
		messages.add("");

		if (Firis.getFirisConfig().areShadersEnabled()) {
			messages.add("[" + Firis.MODNAME + "] Shaderpack: " + Firis.getCurrentPackName() + (Firis.isFallback() ? " (fallback)" : ""));
			Firis.getCurrentPack().ifPresent(pack -> {
				messages.add("[" + Firis.MODNAME + "] " + pack.getProfileInfo());
			});
			messages.add("[" + Firis.MODNAME + "] Color space: " + FirisVideoSettings.colorSpace.name());
		} else {
			messages.add("[" + Firis.MODNAME + "] Shaders are disabled");
		}

		messages.add(3, "Direct Buffers: +" + firis$humanReadableByteCountBin(firis$directPool.getMemoryUsed()));

		//if (!Firis.isSodiumInstalled()) {
		//	messages.add(3, "Native Memory: +" + firis$humanReadableByteCountBin(firis$getNativeMemoryUsage()));
		//}
	}

	@Inject(method = "getGameInformation", at = @At("RETURN"))
	private void firis$appendShadowDebugText(CallbackInfoReturnable<List<String>> cir) {
		List<String> messages = cir.getReturnValue();

		//if (!Firis.isSodiumInstalled() && Firis.getCurrentPack().isPresent()) {
		//	messages.add(1, ChatFormatting.YELLOW + "[" + Firis.MODNAME + "] Sodium isn't installed; you will have poor performance.");
		//	messages.add(2, ChatFormatting.YELLOW + "[" + Firis.MODNAME + "] Install Sodium if you want to run benchmarks or get higher FPS!");
		//}

		Firis.getPipelineManager().getPipeline().ifPresent(pipeline -> pipeline.addDebugText(messages));
	}
}

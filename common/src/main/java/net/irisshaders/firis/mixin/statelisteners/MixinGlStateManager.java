package net.irisshaders.firis.mixin.statelisteners;

import com.mojang.blaze3d.platform.GlStateManager;
import net.irisshaders.firis.gl.state.StateUpdateNotifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {
	private static Runnable blendFuncListener;

	static {
		StateUpdateNotifiers.blendFuncNotifier = listener -> blendFuncListener = listener;
	}

	@Inject(method = "_blendFunc", at = @At("RETURN"), remap = false)
	private static void firis$onBlendFunc(int srcRgb, int dstRgb, CallbackInfo ci) {
		if (blendFuncListener != null) {
			blendFuncListener.run();
		}
	}

	@Inject(method = "_blendFuncSeparate", at = @At("RETURN"), remap = false)
	private static void firis$onBlendFuncSeparate(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha, CallbackInfo ci) {
		if (blendFuncListener != null) {
			blendFuncListener.run();
		}
	}
}

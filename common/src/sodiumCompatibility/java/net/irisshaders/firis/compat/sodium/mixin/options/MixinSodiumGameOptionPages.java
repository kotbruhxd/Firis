package net.irisshaders.firis.compat.sodium.mixin.options;

import net.caffeinemc.mods.fodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.fodium.client.gui.options.Option;
import net.caffeinemc.mods.fodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.fodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.compat.sodium.impl.options.FirisSodiumOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * Adds the Firis-specific options / option changes to the Sodium game options pages.
 */
@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGameOptionPages {
	@Shadow(remap = false)
	@Final
	private static MinecraftOptionsStorage vanillaOpts;

	@Redirect(method = "general", remap = false,
		slice = @Slice(
			from = @At(value = "CONSTANT", args = "stringValue=options.renderDistance"),
			to = @At(value = "CONSTANT", args = "stringValue=options.simulationDistance")
		),
		at = @At(value = "INVOKE", remap = false,
			target = "net/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder.add (" +
				"Lnet/caffeinemc/mods/sodium/client/gui/options/Option;" +
				")Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;"),
		allow = 1)
	private static OptionGroup.Builder firis$addMaxShadowDistanceOption(OptionGroup.Builder builder,
																	   Option<?> candidate) {
		builder.add(candidate);
		builder.add(FirisSodiumOptions.createMaxShadowDistanceSlider(vanillaOpts));

		return builder;
	}

	@Redirect(method = "quality", remap = false,
		slice = @Slice(
			from = @At(value = "CONSTANT", args = "stringValue=options.graphics"),
			to = @At(value = "CONSTANT", args = "stringValue=options.renderClouds")
		),
		at = @At(value = "INVOKE", remap = false,
			target = "net/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder.add (" +
				"Lnet/caffeinemc/mods/sodium/client/gui/options/Option;" +
				")Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;"),
		allow = 1)
	private static OptionGroup.Builder firis$addColorSpaceOption(OptionGroup.Builder builder,
																Option<?> candidate) {
		builder.add(candidate);
		builder.add(FirisSodiumOptions.createColorSpaceButton(vanillaOpts));

		return builder;
	}

	@ModifyArg(method = "quality", remap = false,
		slice = @Slice(
			from = @At(value = "CONSTANT", args = "stringValue=options.graphics"),
			to = @At(value = "CONSTANT", args = "stringValue=options.renderClouds")
		),
		at = @At(value = "INVOKE", remap = false,
			target = "net/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder.add (" +
				"Lnet/caffeinemc/mods/sodium/client/gui/options/Option;" +
				")Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;"),
		allow = 1)
	private static Option<?> firis$replaceGraphicsQualityButton(Option<?> candidate) {
		if (!Firis.getFirisConfig().areShadersEnabled()) {
			return candidate;
		} else {
			return FirisSodiumOptions.createLimitedVideoSettingsButton(vanillaOpts);
		}
	}
}

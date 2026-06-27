package net.irisshaders.firis.compat.sodium.mixin.options;

import net.caffeinemc.mods.fodium.client.gui.SodiumGameOptions;
import net.irisshaders.firis.Firis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

/**
 * Ensures that the Firis config file is written whenever Sodium options are changed, in case the user changed the
 * Max Shadow Distance setting.
 */
@Mixin(SodiumGameOptions.class)
public class MixinSodiumGameOptions {
	@Inject(method = "writeToDisk", at = @At("RETURN"), remap = false)
	private static void firis$writeIrisConfig(CallbackInfo ci) {
		try {
			if (Firis.getFirisConfig() != null) {
				Firis.getFirisConfig().save();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

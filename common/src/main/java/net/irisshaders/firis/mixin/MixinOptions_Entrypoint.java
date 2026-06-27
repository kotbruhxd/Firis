package net.irisshaders.firis.mixin;

import net.irisshaders.firis.Firis;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Options.class, priority = 990)
public class MixinOptions_Entrypoint {
	@Unique
	private static boolean firis$initialized;

	@Inject(method = "load()V", at = @At("HEAD"))
	private void firis$beforeLoadOptions(CallbackInfo ci) {
		if (firis$initialized) {
			return;
		}

		firis$initialized = true;
		new Firis().onEarlyInitialize();
	}
}

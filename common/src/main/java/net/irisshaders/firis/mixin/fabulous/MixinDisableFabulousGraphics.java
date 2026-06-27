package net.irisshaders.firis.mixin.fabulous;

import net.irisshaders.firis.Firis;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class MixinDisableFabulousGraphics {
	@Inject(method = "onResourceManagerReload", at = @At("HEAD"))
	private void firis$disableFabulousGraphicsOnResourceReload(CallbackInfo ci) {
		firis$disableFabulousGraphics();
	}

	// This method is called whenever the user tries to change the graphics mode.
	// We can still revert / intercept the change at the head of the method.
	@Inject(method = "allChanged", at = @At("HEAD"))
	private void firis$disableFabulousGraphicsOnLevelRendererReload(CallbackInfo ci) {
		firis$disableFabulousGraphics();
	}

	@Unique
	private void firis$disableFabulousGraphics() {
		Options options = Minecraft.getInstance().options;

		if (!Firis.getFirisConfig().areShadersEnabled()) {
			// Nothing to do here, shaders are disabled.
			return;
		}

		if (options.graphicsMode().get() == GraphicsStatus.FABULOUS) {
			// Disable fabulous graphics when shaders are enabled.
			options.graphicsMode().set(GraphicsStatus.FANCY);
		}
	}
}

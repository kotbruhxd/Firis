package net.irisshaders.firis.compat.sodium.mixin.directional_shading;

import net.caffeinemc.mods.fodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.fodium.client.model.light.smooth.SmoothLightPipeline;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmoothLightPipeline.class)
public class MixinSmoothLightPipeline {
	@Inject(method = "applySidedBrightness", at = @At("HEAD"), cancellable = true, remap = false)
	private void firis$disableDirectionalShading(CallbackInfo ci) {
		if (WorldRenderingSettings.INSTANCE.shouldDisableDirectionalShading()) {
			ci.cancel();
		}
	}
}

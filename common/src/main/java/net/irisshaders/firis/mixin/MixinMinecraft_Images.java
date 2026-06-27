package net.irisshaders.firis.mixin;

import net.irisshaders.firis.platform.FirisPlatformHelpers;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.shaderpack.texture.CustomTextureData;
import net.irisshaders.firis.shaderpack.texture.TextureFilteringData;
import net.irisshaders.firis.targets.backed.NativeImageBackedCustomTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

/**
 * This Mixin is responsible for registering the "widgets" texture used in Firis' GUI's.
 * Normally Fabric API would do this automatically, but we don't use it here, so it must be done manually.
 */
@Mixin(Minecraft.class)
public class MixinMinecraft_Images {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void firis$setupImages(GameConfig arg, CallbackInfo ci) {
		if (!FirisPlatformHelpers.getInstance().isModLoaded("fabric-resource-loader-v0")) {
			try {
				Minecraft.getInstance().getTextureManager().register(new ResourceLocation("firis", "textures/gui/widgets.png"), new NativeImageBackedCustomTexture(new CustomTextureData.PngData(new TextureFilteringData(false, false), IOUtils.toByteArray(Firis.class.getResourceAsStream("/assets/iris/textures/gui/widgets.png")))));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

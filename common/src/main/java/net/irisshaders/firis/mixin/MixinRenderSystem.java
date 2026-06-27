package net.irisshaders.firis.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.gl.GLDebug;
import net.irisshaders.firis.gl.FirisRenderSystem;
import net.irisshaders.firis.samplers.FirisSamplers;
import net.irisshaders.firis.texture.TextureTracker;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
	@Inject(method = "initRenderer", at = @At("RETURN"), remap = false)
	private static void firis$onRendererInit(int debugVerbosity, boolean alwaysFalse, CallbackInfo ci) {
		Firis.duringRenderSystemInit();
		GLDebug.reloadDebugState();
		FirisRenderSystem.initRenderer();
		FirisSamplers.initRenderer();
		Firis.onRenderSystemInit();
	}

	@Inject(method = "_setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/AbstractTexture;getId()I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void _setShaderTexture(int unit, ResourceLocation resourceLocation, CallbackInfo ci, TextureManager lv, AbstractTexture tex) {
		TextureTracker.INSTANCE.onSetShaderTexture(unit, tex.getId());
	}

	@Inject(method = "_setShaderTexture(II)V", at = @At("RETURN"), remap = false)
	private static void _setShaderTexture(int unit, int glId, CallbackInfo ci) {
		TextureTracker.INSTANCE.onSetShaderTexture(unit, glId);
	}
}

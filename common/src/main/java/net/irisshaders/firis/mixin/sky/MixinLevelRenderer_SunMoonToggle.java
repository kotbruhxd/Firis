package net.irisshaders.firis.mixin.sky;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.pipeline.WorldRenderingPipeline;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows pipelines to disable the sun, moon, or both.
 */
@Mixin(LevelRenderer.class)
public class MixinLevelRenderer_SunMoonToggle {
	/**
	 * This is a convenient way to disable rendering the sun / moon, since this clears the sun's vertices from
	 * the buffer, then when BufferRenderer is passed the empty buffer it will notice that it's empty and
	 * won't dispatch an unnecessary draw call. Nice!
	 */
	@Unique
	private void firis$emptyBuilder() {
		BufferBuilder builder = Tesselator.getInstance().getBuilder();

		builder.end().release();
		builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
	}

	@Inject(method = "renderSky",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;"),
		slice = @Slice(
			from = @At(value = "FIELD", target = "net/minecraft/client/renderer/LevelRenderer.SUN_LOCATION : Lnet/minecraft/resources/ResourceLocation;"),
			to = @At(value = "FIELD", target = "net/minecraft/client/renderer/LevelRenderer.MOON_LOCATION : Lnet/minecraft/resources/ResourceLocation;")),
		allow = 1)
	private void firis$beforeDrawSun(PoseStack poseStack, Matrix4f matrix4f, float f, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci) {
		if (!Firis.getPipelineManager().getPipeline().map(WorldRenderingPipeline::shouldRenderSun).orElse(true)) {
			firis$emptyBuilder();
		}
	}

	@Inject(method = "renderSky",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;"),
		slice = @Slice(
			from = @At(value = "FIELD", target = "net/minecraft/client/renderer/LevelRenderer.MOON_LOCATION : Lnet/minecraft/resources/ResourceLocation;"),
			to = @At(value = "INVOKE", target = "net/minecraft/client/multiplayer/ClientLevel.getStarBrightness (F)F")),
		allow = 1)
	private void firis$beforeDrawMoon(PoseStack poseStack, Matrix4f matrix4f, float f, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci) {
		if (!Firis.getPipelineManager().getPipeline().map(WorldRenderingPipeline::shouldRenderMoon).orElse(true)) {
			firis$emptyBuilder();
		}
	}
}

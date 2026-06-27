package net.irisshaders.firis.mixin.vertices.immediate;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.firis.vertices.ExtendingBufferBuilder;
import net.irisshaders.firis.vertices.ImmediateState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Quick optimization to disable the extended vertex format outside of level rendering if we're using a BufferSource.
 * This is a heuristic that should hopefully work almost always because of how people use BufferSource.
 */
@Mixin(MultiBufferSource.BufferSource.class)
public class MixinBufferSource {
	@Redirect(method = "getBuffer",
		at = @At(value = "INVOKE",
			target = "com/mojang/blaze3d/vertex/BufferBuilder.begin (Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;)V"))
	private void firis$redirectBegin(BufferBuilder bufferBuilder, VertexFormat.Mode drawMode, VertexFormat vertexFormat) {
		if (firis$notRenderingLevel()) {
			((ExtendingBufferBuilder) bufferBuilder).firis$beginWithoutExtending(drawMode, vertexFormat);
		} else {
			bufferBuilder.begin(drawMode, vertexFormat);
		}
	}

	@Inject(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;)V",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/RenderType;end(Lcom/mojang/blaze3d/vertex/BufferBuilder;Lcom/mojang/blaze3d/vertex/VertexSorting;)V"))
	private void firis$beforeFlushBuffer(RenderType renderType, CallbackInfo ci) {
		if (firis$notRenderingLevel()) {
			ImmediateState.renderWithExtendedVertexFormat = false;
		}
	}

	@Inject(method = "endBatch(Lnet/minecraft/client/renderer/RenderType;)V",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/RenderType;end(Lcom/mojang/blaze3d/vertex/BufferBuilder;Lcom/mojang/blaze3d/vertex/VertexSorting;)V",
			shift = At.Shift.AFTER))
	private void firis$afterFlushBuffer(RenderType renderType, CallbackInfo ci) {
		if (firis$notRenderingLevel()) {
			ImmediateState.renderWithExtendedVertexFormat = true;
		}
	}

	private boolean firis$notRenderingLevel() {
		return !ImmediateState.isRenderingLevel;
	}
}

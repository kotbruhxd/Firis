package net.irisshaders.firis.mixin.vertices;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.firis.vertices.ImmediateState;
import net.irisshaders.firis.vertices.FirisVertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ensures that the correct state for the extended vertex format is set up when needed.
 */
@Mixin(VertexFormat.class)
public class MixinVertexFormat {
	@Inject(method = "setupBufferState", at = @At("HEAD"), cancellable = true)
	private void firis$onSetupBufferState(CallbackInfo ci) {
		if (WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat() && ImmediateState.renderWithExtendedVertexFormat) {
			if ((Object) this == DefaultVertexFormat.BLOCK) {
				FirisVertexFormats.TERRAIN.setupBufferState();

				ci.cancel();
			} else if ((Object) this == DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP) {
				FirisVertexFormats.GLYPH.setupBufferState();

				ci.cancel();
			} else if ((Object) this == DefaultVertexFormat.NEW_ENTITY) {
				FirisVertexFormats.ENTITY.setupBufferState();

				ci.cancel();
			}
		}
	}

	@Inject(method = "clearBufferState", at = @At("HEAD"), cancellable = true)
	private void firis$onClearBufferState(CallbackInfo ci) {
		if (WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat() && ImmediateState.renderWithExtendedVertexFormat) {
			if ((Object) this == DefaultVertexFormat.BLOCK) {
				FirisVertexFormats.TERRAIN.clearBufferState();

				ci.cancel();
			} else if ((Object) this == DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP) {
				FirisVertexFormats.GLYPH.clearBufferState();

				ci.cancel();
			} else if ((Object) this == DefaultVertexFormat.NEW_ENTITY) {
				FirisVertexFormats.ENTITY.clearBufferState();

				ci.cancel();
			}
		}
	}
}

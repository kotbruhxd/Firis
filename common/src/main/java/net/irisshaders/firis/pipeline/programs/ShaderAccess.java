package net.irisshaders.firis.pipeline.programs;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.pipeline.ShaderRenderingPipeline;
import net.irisshaders.firis.pipeline.WorldRenderingPipeline;
import net.irisshaders.firis.shadows.ShadowRenderingState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;

public class ShaderAccess {
	public static final VertexFormat IE_FORMAT = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder().put("Position", DefaultVertexFormat.ELEMENT_POSITION).put("Color", DefaultVertexFormat.ELEMENT_COLOR).put("UV0", DefaultVertexFormat.ELEMENT_UV0).put("Normal", DefaultVertexFormat.ELEMENT_NORMAL).put("Padding", DefaultVertexFormat.ELEMENT_PADDING).build());

	public static ShaderInstance getParticleTranslucentShader() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {
			ShaderInstance override = ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShaderKey.PARTICLES_TRANS);

			if (override != null) {
				return override;
			}
		}

		return GameRenderer.getParticleShader();
	}

	public static ShaderInstance getIEVBOShader() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {

			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.IE_COMPAT_SHADOW : ShaderKey.IE_COMPAT);
		}

		return null;
	}

    public static ShaderInstance getMekanismFlameShader() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {

			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.MEKANISM_FLAME_SHADOW : ShaderKey.MEKANISM_FLAME);
		}

		return GameRenderer.getPositionColorTexShader();
    }

	public static ShaderInstance getMekasuitShader() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline instanceof ShaderRenderingPipeline) {
			return ((ShaderRenderingPipeline) pipeline).getShaderMap().getShader(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? ShaderKey.SHADOW_ENTITIES_CUTOUT : ShaderKey.ENTITIES_TRANSLUCENT);
		}

		return GameRenderer.getRendertypeEntityCutoutShader();
	}
}

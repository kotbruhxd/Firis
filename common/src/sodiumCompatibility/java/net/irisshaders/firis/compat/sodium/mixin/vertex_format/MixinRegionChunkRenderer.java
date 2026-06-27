package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import net.caffeinemc.mods.fodium.client.gl.attribute.GlVertexAttributeBinding;
import net.caffeinemc.mods.fodium.client.gl.buffer.GlBuffer;
import net.caffeinemc.mods.fodium.client.gl.device.RenderDevice;
import net.caffeinemc.mods.fodium.client.gl.tessellation.TessellationBinding;
import net.caffeinemc.mods.fodium.client.render.chunk.DefaultChunkRenderer;
import net.caffeinemc.mods.fodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.fodium.client.render.chunk.shader.ChunkShaderBindingPoints;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkMeshAttribute;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.firis.compat.sodium.impl.FirisChunkShaderBindingPoints;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisChunkMeshAttributes;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultChunkRenderer.class)
public abstract class MixinRegionChunkRenderer extends ShaderChunkRenderer {
	public MixinRegionChunkRenderer(RenderDevice device, ChunkVertexType vertexType) {
		super(device, vertexType);
	}

	@Redirect(remap = false, method = "createRegionTessellation", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/gl/tessellation/TessellationBinding;forVertexBuffer(Lnet/caffeinemc/mods/sodium/client/gl/buffer/GlBuffer;[Lnet/caffeinemc/mods/sodium/client/gl/attribute/GlVertexAttributeBinding;)Lnet/caffeinemc/mods/sodium/client/gl/tessellation/TessellationBinding;"))
	private TessellationBinding firis$onInit(GlBuffer buffer, GlVertexAttributeBinding[] attributes) {
		if (!WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat()) {
			return TessellationBinding.forVertexBuffer(buffer, attributes);
		}

		attributes = new GlVertexAttributeBinding[]{
			new GlVertexAttributeBinding(ChunkShaderBindingPoints.ATTRIBUTE_POSITION_HI,
				vertexFormat.getAttribute(ChunkMeshAttribute.POSITION_HI)),
			new GlVertexAttributeBinding(ChunkShaderBindingPoints.ATTRIBUTE_POSITION_LO,
				vertexFormat.getAttribute(ChunkMeshAttribute.POSITION_LO)),
			new GlVertexAttributeBinding(ChunkShaderBindingPoints.ATTRIBUTE_COLOR,
				vertexFormat.getAttribute(ChunkMeshAttribute.COLOR)),
			new GlVertexAttributeBinding(ChunkShaderBindingPoints.ATTRIBUTE_TEXTURE,
				vertexFormat.getAttribute(ChunkMeshAttribute.TEXTURE)),
			new GlVertexAttributeBinding(ChunkShaderBindingPoints.ATTRIBUTE_LIGHT_MATERIAL_INDEX,
				vertexFormat.getAttribute(ChunkMeshAttribute.LIGHT_MATERIAL_INDEX)),
			new GlVertexAttributeBinding(FirisChunkShaderBindingPoints.MID_BLOCK,
				vertexFormat.getAttribute(FirisChunkMeshAttributes.MID_BLOCK)),
			new GlVertexAttributeBinding(FirisChunkShaderBindingPoints.BLOCK_ID,
				vertexFormat.getAttribute(FirisChunkMeshAttributes.BLOCK_ID)),
			new GlVertexAttributeBinding(FirisChunkShaderBindingPoints.MID_TEX_COORD,
				vertexFormat.getAttribute(FirisChunkMeshAttributes.MID_TEX_COORD)),
			new GlVertexAttributeBinding(FirisChunkShaderBindingPoints.TANGENT,
				vertexFormat.getAttribute(FirisChunkMeshAttributes.TANGENT)),
			new GlVertexAttributeBinding(FirisChunkShaderBindingPoints.NORMAL,
				vertexFormat.getAttribute(FirisChunkMeshAttributes.NORMAL))
		};

		return TessellationBinding.forVertexBuffer(buffer, attributes);
	}
}

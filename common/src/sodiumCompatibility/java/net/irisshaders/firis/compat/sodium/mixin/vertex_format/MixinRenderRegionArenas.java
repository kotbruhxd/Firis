package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import net.caffeinemc.mods.fodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkMeshFormats;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisModelVertexFormats;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderRegion.DeviceResources.class)
public class MixinRenderRegionArenas {
	@Redirect(method = "<init>", remap = false,
		at = @At(value = "FIELD",
			target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkMeshFormats;COMPACT:Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexType;",
			remap = false))
	private ChunkVertexType firis$useExtendedStride() {
		return WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat() ? FirisModelVertexFormats.MODEL_VERTEX_XHFP : ChunkMeshFormats.COMPACT;
	}
}

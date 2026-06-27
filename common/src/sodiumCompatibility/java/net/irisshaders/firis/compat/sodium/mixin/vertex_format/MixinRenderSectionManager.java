package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import net.caffeinemc.mods.fodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisModelVertexFormats;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RenderSectionManager.class)
public class MixinRenderSectionManager {
	@ModifyArg(method = "<init>", remap = false,
		at = @At(value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;<init>(Lnet/caffeinemc/mods/sodium/client/gl/device/RenderDevice;Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexType;)V"))
	private ChunkVertexType firis$useExtendedVertexFormat$1(ChunkVertexType vertexType) {
		return WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat() ? FirisModelVertexFormats.MODEL_VERTEX_XHFP : vertexType;
	}

	@ModifyArg(method = "<init>",
		at = @At(value = "INVOKE",
			target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/executor/ChunkBuilder;<init>(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexType;)V"))
	private ChunkVertexType firis$useExtendedVertexFormat$2(ChunkVertexType vertexType) {
		return WorldRenderingSettings.INSTANCE.shouldUseExtendedVertexFormat() ? FirisModelVertexFormats.MODEL_VERTEX_XHFP : vertexType;
	}
}

package net.irisshaders.firis.compat.sodium.mixin.block_id;

import net.caffeinemc.mods.fodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.irisshaders.firis.compat.sodium.impl.block_context.BlockContextHolder;
import net.irisshaders.firis.compat.sodium.impl.block_context.ContextAwareVertexWriter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMeshBufferBuilder.class)
public class MixinChunkVertexBufferBuilder implements ContextAwareVertexWriter {
	@Shadow
	@Final
	private ChunkVertexEncoder encoder;

	@Override
	public void firis$setContextHolder(BlockContextHolder holder) {
		if (encoder instanceof ContextAwareVertexWriter) {
			((ContextAwareVertexWriter) encoder).firis$setContextHolder(holder);
		}
	}

	@Override
	public void flipUpcomingQuadNormal() {
		if (encoder instanceof ContextAwareVertexWriter) {
			((ContextAwareVertexWriter) encoder).flipUpcomingQuadNormal();
		}
	}
}

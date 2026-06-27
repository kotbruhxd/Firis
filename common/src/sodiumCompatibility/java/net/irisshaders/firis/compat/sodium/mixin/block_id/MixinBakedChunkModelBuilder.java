package net.irisshaders.firis.compat.sodium.mixin.block_id;

import net.caffeinemc.mods.fodium.client.render.chunk.compile.buffers.BakedChunkModelBuilder;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.irisshaders.firis.compat.sodium.impl.block_context.BlockContextHolder;
import net.irisshaders.firis.compat.sodium.impl.block_context.ContextAwareVertexWriter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BakedChunkModelBuilder.class)
public class MixinBakedChunkModelBuilder implements ContextAwareVertexWriter {

	@Shadow
	@Final
	private ChunkMeshBufferBuilder[] vertexBuffers;

	@Override
	public void firis$setContextHolder(BlockContextHolder holder) {
		for (ChunkMeshBufferBuilder builder : this.vertexBuffers) {
			((ContextAwareVertexWriter) builder).firis$setContextHolder(holder);
		}
	}

	@Override
	public void flipUpcomingQuadNormal() {
		for (ChunkMeshBufferBuilder builder : this.vertexBuffers) {
			((ContextAwareVertexWriter) builder).flipUpcomingQuadNormal();
		}
	}
}

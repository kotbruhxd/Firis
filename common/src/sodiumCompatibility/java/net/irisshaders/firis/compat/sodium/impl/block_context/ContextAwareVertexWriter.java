package net.irisshaders.firis.compat.sodium.impl.block_context;

public interface ContextAwareVertexWriter {
	void firis$setContextHolder(BlockContextHolder holder);

	void flipUpcomingQuadNormal();
}

package net.irisshaders.firis.compat.sodium.impl.block_context;

import net.minecraft.world.level.block.state.BlockState;

public interface ChunkBuildBuffersExt {
	void firis$setLocalPos(int localPosX, int localPosY, int localPosZ);

	void firis$setMaterialId(BlockState state, short renderType, byte lightValue);

	void firis$resetBlockContext();

	void firis$ignoreMidBlock(boolean state);
}

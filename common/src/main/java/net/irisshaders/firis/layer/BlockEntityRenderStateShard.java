package net.irisshaders.firis.layer;

import net.minecraft.client.renderer.RenderStateShard;

public final class BlockEntityRenderStateShard extends RenderStateShard {
	public static final BlockEntityRenderStateShard INSTANCE = new BlockEntityRenderStateShard();

	private BlockEntityRenderStateShard() {
		super("firis:is_block_entity", GbufferPrograms::beginBlockEntities, GbufferPrograms::endBlockEntities);
	}
}

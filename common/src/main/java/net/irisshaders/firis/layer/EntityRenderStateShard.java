package net.irisshaders.firis.layer;

import net.minecraft.client.renderer.RenderStateShard;

public final class EntityRenderStateShard extends RenderStateShard {
	public static final EntityRenderStateShard INSTANCE = new EntityRenderStateShard();

	private EntityRenderStateShard() {
		super("firis:is_entity", GbufferPrograms::beginEntities, GbufferPrograms::endEntities);
	}
}

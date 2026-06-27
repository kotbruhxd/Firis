package net.irisshaders.firis.layer;

import net.minecraft.client.renderer.RenderStateShard;

public class IsOutlineRenderStateShard extends RenderStateShard {
	public static final IsOutlineRenderStateShard INSTANCE = new IsOutlineRenderStateShard();

	private IsOutlineRenderStateShard() {
		super("firis:is_outline", GbufferPrograms::beginOutline, GbufferPrograms::endOutline);
	}
}

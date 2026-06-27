package net.irisshaders.firis.compat.sodium.mixin.shadow_map.frustum;

import net.caffeinemc.mods.fodium.client.render.viewport.Viewport;
import net.caffeinemc.mods.fodium.client.render.viewport.ViewportProvider;
import net.caffeinemc.mods.fodium.client.render.viewport.frustum.Frustum;
import net.irisshaders.firis.shadows.frustum.CullEverythingFrustum;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CullEverythingFrustum.class)
public class MixinCullEverythingFrustum implements Frustum, ViewportProvider {
	private static final Vector3d EMPTY = new Vector3d();

	@Override
	public Viewport sodium$createViewport() {
		return new Viewport(this, EMPTY);
	}

	@Override
	public boolean testAab(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		return false;
	}
}

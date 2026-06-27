package net.irisshaders.firis.compat.sodium.impl.shader_overrides;

import net.caffeinemc.mods.fodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.caffeinemc.mods.fodium.client.render.chunk.terrain.TerrainRenderPass;

public enum FirisTerrainPass {
	SHADOW("shadow"),
	SHADOW_CUTOUT("shadow"),
	GBUFFER_SOLID("gbuffers_terrain"),
	GBUFFER_CUTOUT("gbuffers_terrain_cutout"),
	GBUFFER_TRANSLUCENT("gbuffers_water");

	private final String name;

	FirisTerrainPass(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isShadow() {
		return this == SHADOW || this == SHADOW_CUTOUT;
	}

	public TerrainRenderPass toTerrainPass() {
		switch (this) {
			case SHADOW, GBUFFER_SOLID:
				return DefaultTerrainRenderPasses.SOLID;
			case SHADOW_CUTOUT, GBUFFER_CUTOUT:
				return DefaultTerrainRenderPasses.CUTOUT;
			case GBUFFER_TRANSLUCENT:
				return DefaultTerrainRenderPasses.TRANSLUCENT;
			default:
				return null;
		}
	}
}

package net.irisshaders.firis.texture.pbr;

import org.jetbrains.annotations.Nullable;

public interface SpriteContentsExtension {
	@Nullable
	PBRSpriteHolder getPBRHolder();

	PBRSpriteHolder getOrCreatePBRHolder();
}

package net.irisshaders.firis.test.shaderpack;

import net.irisshaders.firis.shaderpack.ShaderPack;
import net.irisshaders.firis.shaderpack.texture.CustomTextureData;
import net.irisshaders.firis.test.FirisTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class LightmapCustomTextureTest {
	@Test
	void testLightmapCustomTexture() {
		ShaderPack shaderPack = FirisTests.loadPackOrFail("lightmap_custom_texture");

		Assertions.assertEquals(Optional.of(new CustomTextureData.LightmapMarker()), shaderPack.getCustomNoiseTexture());
	}
}

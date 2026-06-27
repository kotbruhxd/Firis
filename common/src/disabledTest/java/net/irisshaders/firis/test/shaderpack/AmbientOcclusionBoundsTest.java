package net.irisshaders.firis.test.shaderpack;

import net.irisshaders.firis.shaderpack.DimensionId;
import net.irisshaders.firis.shaderpack.ShaderPack;
import net.irisshaders.firis.test.FirisTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AmbientOcclusionBoundsTest {
	@Test
	void testAmbientOcclusionBounds() {
		ShaderPack shaderPack = FirisTests.loadPackOrFail("ambient_occlusion_out_of_bounds");

		Assertions.assertEquals(1.0f,
			shaderPack.getProgramSet(DimensionId.OVERWORLD).getPackDirectives().getAmbientOcclusionLevel());
	}
}

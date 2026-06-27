package net.irisshaders.firis.pipeline.transform.parameter;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.irisshaders.firis.gl.blending.AlphaTest;
import net.irisshaders.firis.gl.texture.TextureType;
import net.irisshaders.firis.helpers.Tri;
import net.irisshaders.firis.pipeline.transform.Patch;
import net.irisshaders.firis.shaderpack.texture.TextureStage;

public class ComputeParameters extends TextureStageParameters {
	// WARNING: adding new fields requires updating hashCode and equals methods!

	public ComputeParameters(Patch patch, TextureStage stage,
							 Object2ObjectMap<Tri<String, TextureType, TextureStage>, String> textureMap) {
		super(patch, stage, textureMap);
	}

	@Override
	public AlphaTest getAlphaTest() {
		return AlphaTest.ALWAYS;
	}

	// since this class has no fields, hashCode() and equals() are inherited from
	// TextureStageParameters
}

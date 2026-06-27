package net.irisshaders.firis.pipeline;

import net.irisshaders.firis.pipeline.programs.ShaderMap;
import net.irisshaders.firis.uniforms.FrameUpdateNotifier;

public interface ShaderRenderingPipeline extends WorldRenderingPipeline {
	ShaderMap getShaderMap();

	FrameUpdateNotifier getFrameUpdateNotifier();

	boolean shouldOverrideShaders();
}

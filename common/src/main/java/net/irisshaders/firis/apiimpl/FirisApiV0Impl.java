package net.irisshaders.firis.apiimpl;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.api.v0.FirisApi;
import net.irisshaders.firis.api.v0.FirisApiConfig;
import net.irisshaders.firis.api.v0.FirisTextVertexSink;
import net.irisshaders.firis.gui.screen.ShaderPackScreen;
import net.irisshaders.firis.pipeline.VanillaRenderingPipeline;
import net.irisshaders.firis.pipeline.WorldRenderingPipeline;
import net.irisshaders.firis.shadows.ShadowRenderingState;
import net.irisshaders.firis.vertices.FirisTextVertexSinkImpl;
import net.minecraft.client.gui.screens.Screen;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;

public class FirisApiV0Impl implements FirisApi {
	public static final FirisApiV0Impl INSTANCE = new FirisApiV0Impl();
	private static final FirisApiV0ConfigImpl CONFIG = new FirisApiV0ConfigImpl();

	@Override
	public int getMinorApiRevision() {
		return 2;
	}

	@Override
	public boolean isShaderPackInUse() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline == null) {
			return false;
		}

		return !(pipeline instanceof VanillaRenderingPipeline);
	}

	@Override
	public boolean isRenderingShadowPass() {
		return ShadowRenderingState.areShadowsCurrentlyBeingRendered();
	}

	@Override
	public Object openMainIrisScreenObj(Object parent) {
		return new ShaderPackScreen((Screen) parent);
	}

	@Override
	public String getMainScreenLanguageKey() {
		return "options.iris.shaderPackSelection";
	}

	@Override
	public FirisApiConfig getConfig() {
		return CONFIG;
	}

	@Override
	public FirisTextVertexSink createTextVertexSink(int maxQuadCount, IntFunction<ByteBuffer> bufferProvider) {
		return new FirisTextVertexSinkImpl(maxQuadCount, bufferProvider);
	}

	@Override
	public float getSunPathRotation() {
		WorldRenderingPipeline pipeline = Firis.getPipelineManager().getPipelineNullable();

		if (pipeline == null) {
			return 0;
		}

		return pipeline.getSunPathRotation();
	}
}

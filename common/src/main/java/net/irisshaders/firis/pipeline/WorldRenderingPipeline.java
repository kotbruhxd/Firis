package net.irisshaders.firis.pipeline;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.irisshaders.firis.compat.dh.DHCompat;
import net.irisshaders.firis.features.FeatureFlags;
import net.irisshaders.firis.gl.texture.TextureType;
import net.irisshaders.firis.helpers.Tri;
import net.irisshaders.firis.mixin.LevelRendererAccessor;
import net.irisshaders.firis.shaderpack.properties.CloudSetting;
import net.irisshaders.firis.shaderpack.properties.ParticleRenderingSettings;
import net.irisshaders.firis.shaderpack.texture.TextureStage;
import net.irisshaders.firis.targets.RenderTargetStateListener;
import net.irisshaders.firis.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Camera;

import java.util.List;
import java.util.OptionalInt;

public interface WorldRenderingPipeline {
	void beginLevelRendering();

	void renderShadows(LevelRendererAccessor worldRenderer, Camera camera);

	void addDebugText(List<String> messages);

	OptionalInt getForcedShadowRenderDistanceChunksForDisplay();

	Object2ObjectMap<Tri<String, TextureType, TextureStage>, String> getTextureMap();

	WorldRenderingPhase getPhase();

	void setPhase(WorldRenderingPhase phase);

	void setOverridePhase(WorldRenderingPhase phase);

	RenderTargetStateListener getRenderTargetStateListener();

	int getCurrentNormalTexture();

	int getCurrentSpecularTexture();

	void onSetShaderTexture(int id);

	void beginHand();

	void beginTranslucents();

	void finalizeLevelRendering();

	void finalizeGameRendering();

	void destroy();

	SodiumTerrainPipeline getSodiumTerrainPipeline();

	FrameUpdateNotifier getFrameUpdateNotifier();

	boolean shouldDisableVanillaEntityShadows();

	boolean shouldDisableDirectionalShading();

	boolean shouldDisableFrustumCulling();

	boolean shouldDisableOcclusionCulling();

	CloudSetting getCloudSetting();

	boolean shouldRenderUnderwaterOverlay();

	boolean shouldRenderVignette();

	boolean shouldRenderSun();

	boolean shouldRenderMoon();

	boolean shouldWriteRainAndSnowToDepthBuffer();

	ParticleRenderingSettings getParticleRenderingSettings();

	boolean allowConcurrentCompute();

	boolean hasFeature(FeatureFlags flags);

	float getSunPathRotation();

	DHCompat getDHCompat();
}

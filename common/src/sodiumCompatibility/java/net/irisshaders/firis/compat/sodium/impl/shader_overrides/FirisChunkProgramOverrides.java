package net.irisshaders.firis.compat.sodium.impl.shader_overrides;

import net.caffeinemc.mods.fodium.client.gl.GlObject;
import net.caffeinemc.mods.fodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.fodium.client.gl.shader.GlShader;
import net.caffeinemc.mods.fodium.client.gl.shader.ShaderType;
import net.caffeinemc.mods.fodium.client.render.chunk.shader.ChunkFogMode;
import net.caffeinemc.mods.fodium.client.render.chunk.shader.ChunkShaderBindingPoints;
import net.caffeinemc.mods.fodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.fodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.firis.Firis;
import net.irisshaders.firis.compat.sodium.impl.FirisChunkShaderBindingPoints;
import net.irisshaders.firis.gl.GLDebug;
import net.irisshaders.firis.gl.blending.AlphaTest;
import net.irisshaders.firis.gl.blending.AlphaTests;
import net.irisshaders.firis.gl.blending.BlendModeOverride;
import net.irisshaders.firis.gl.blending.BufferBlendOverride;
import net.irisshaders.firis.gl.framebuffer.GlFramebuffer;
import net.irisshaders.firis.pipeline.SodiumTerrainPipeline;
import net.irisshaders.firis.pipeline.WorldRenderingPipeline;
import net.irisshaders.firis.shadows.ShadowRenderingState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL43C;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class FirisChunkProgramOverrides {
	private final EnumMap<FirisTerrainPass, GlProgram<FirisChunkShaderInterface>> programs = new EnumMap<>(FirisTerrainPass.class);
	private boolean shadersCreated = false;
	private int versionCounterForSodiumShaderReload = -1;

	private GlShader createVertexShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		Optional<String> irisVertexShader;

		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			irisVertexShader = pipeline.getShadowVertexShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			irisVertexShader = pipeline.getTerrainSolidVertexShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			irisVertexShader = pipeline.getTerrainCutoutVertexShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			irisVertexShader = pipeline.getTranslucentVertexShaderSource();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}

		String source = irisVertexShader.orElse(null);

		if (source == null) {
			return null;
		}

		return new GlShader(ShaderType.VERTEX, new ResourceLocation("firis",
			"sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT) + ".vsh"), source);
	}

	private GlShader createGeometryShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		Optional<String> irisGeometryShader;

		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			irisGeometryShader = pipeline.getShadowGeometryShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			irisGeometryShader = pipeline.getTerrainSolidGeometryShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			irisGeometryShader = pipeline.getTerrainCutoutGeometryShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			irisGeometryShader = pipeline.getTranslucentGeometryShaderSource();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}

		String source = irisGeometryShader.orElse(null);

		if (source == null) {
			return null;
		}

		return new GlShader(FirisShaderTypes.GEOMETRY, new ResourceLocation("firis",
			"sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT) + ".gsh"), source);
	}

	private GlShader createTessControlShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		Optional<String> irisTessControlShader;

		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			irisTessControlShader = pipeline.getShadowTessControlShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			irisTessControlShader = pipeline.getTerrainSolidTessControlShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			irisTessControlShader = pipeline.getTerrainCutoutTessControlShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			irisTessControlShader = pipeline.getTranslucentTessControlShaderSource();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}

		String source = irisTessControlShader.orElse(null);

		if (source == null) {
			return null;
		}

		return new GlShader(FirisShaderTypes.TESS_CONTROL, new ResourceLocation("firis",
			"sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT) + ".tcs"), source);
	}

	private GlShader createTessEvalShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		Optional<String> irisTessEvalShader;

		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			irisTessEvalShader = pipeline.getShadowTessEvalShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			irisTessEvalShader = pipeline.getTerrainSolidTessEvalShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			irisTessEvalShader = pipeline.getTerrainCutoutTessEvalShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			irisTessEvalShader = pipeline.getTranslucentTessEvalShaderSource();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}

		String source = irisTessEvalShader.orElse(null);

		if (source == null) {
			return null;
		}

		return new GlShader(FirisShaderTypes.TESS_EVAL, new ResourceLocation("firis",
			"sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT) + ".tes"), source);
	}

	private GlShader createFragmentShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		Optional<String> irisFragmentShader;

		if (pass == FirisTerrainPass.SHADOW) {
			irisFragmentShader = pipeline.getShadowFragmentShaderSource();
		} else if (pass == FirisTerrainPass.SHADOW_CUTOUT) {
			irisFragmentShader = pipeline.getShadowCutoutFragmentShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			irisFragmentShader = pipeline.getTerrainSolidFragmentShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			irisFragmentShader = pipeline.getTerrainCutoutFragmentShaderSource();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			irisFragmentShader = pipeline.getTranslucentFragmentShaderSource();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}

		String source = irisFragmentShader.orElse(null);

		if (source == null) {
			return null;
		}

		return new GlShader(ShaderType.FRAGMENT, new ResourceLocation("firis",
			"sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT) + ".fsh"), source);
	}

	private BlendModeOverride getBlendOverride(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			return pipeline.getShadowBlendOverride();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			return pipeline.getTerrainSolidBlendOverride();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			return pipeline.getTerrainCutoutBlendOverride();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			return pipeline.getTranslucentBlendOverride();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}
	}

	private List<BufferBlendOverride> getBufferBlendOverride(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			return pipeline.getShadowBufferOverrides();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			return pipeline.getTerrainSolidBufferOverrides();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			return pipeline.getTerrainCutoutBufferOverrides();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			return pipeline.getTranslucentBufferOverrides();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}
	}

	@Nullable
	private GlProgram<FirisChunkShaderInterface> createShader(FirisTerrainPass pass, SodiumTerrainPipeline pipeline, ChunkVertexType vertexType) {
		GlShader vertShader = createVertexShader(pass, pipeline);
		GlShader geomShader = createGeometryShader(pass, pipeline);
		GlShader tessCShader = createTessControlShader(pass, pipeline);
		GlShader tessEShader = createTessEvalShader(pass, pipeline);
		GlShader fragShader = createFragmentShader(pass, pipeline);
		BlendModeOverride blendOverride = getBlendOverride(pass, pipeline);
		List<BufferBlendOverride> bufferOverrides = getBufferBlendOverride(pass, pipeline);
		float alpha = getAlphaReference(pass, pipeline);

		if (vertShader == null || fragShader == null) {
			if (vertShader != null) {
				vertShader.delete();
			}

			if (geomShader != null) {
				geomShader.delete();
			}

			if (tessCShader != null) {
				tessCShader.delete();
			}

			if (tessEShader != null) {
				tessEShader.delete();
			}

			if (fragShader != null) {
				fragShader.delete();
			}

			// TODO: Partial shader programs?
			return null;
		}

		try {
			GlProgram.Builder builder = GlProgram.builder(new ResourceLocation("fodium", "chunk_shader_for_"
				+ pass.getName()));

			if (geomShader != null) {
				builder.attachShader(geomShader);
			}
			if (tessCShader != null) {
				builder.attachShader(tessCShader);
			}
			if (tessEShader != null) {
				builder.attachShader(tessEShader);
			}

			return builder.attachShader(vertShader)
				.attachShader(fragShader)
				// The following 4 attributes are part of Sodium.
				.bindAttribute("a_PositionHi", ChunkShaderBindingPoints.ATTRIBUTE_POSITION_HI)
				.bindAttribute("a_PositionLo", ChunkShaderBindingPoints.ATTRIBUTE_POSITION_LO)
				.bindAttribute("a_Color", ChunkShaderBindingPoints.ATTRIBUTE_COLOR)
				.bindAttribute("a_TexCoord", ChunkShaderBindingPoints.ATTRIBUTE_TEXTURE)
				.bindAttribute("a_LightAndData", ChunkShaderBindingPoints.ATTRIBUTE_LIGHT_MATERIAL_INDEX)
				.bindAttribute("mc_Entity", FirisChunkShaderBindingPoints.BLOCK_ID)
				.bindAttribute("mc_midTexCoord", FirisChunkShaderBindingPoints.MID_TEX_COORD)
				.bindAttribute("at_tangent", FirisChunkShaderBindingPoints.TANGENT)
				.bindAttribute("iris_Normal", FirisChunkShaderBindingPoints.NORMAL)
				.bindAttribute("at_midBlock", FirisChunkShaderBindingPoints.MID_BLOCK)
				.link((shader) -> {
					int handle = ((GlObject) shader).handle();
					ShaderBindingContextExt contextExt = (ShaderBindingContextExt) shader;
					GLDebug.nameObject(GL43C.GL_PROGRAM, handle, "sodium-terrain-" + pass.toString().toLowerCase(Locale.ROOT));
					return new FirisChunkShaderInterface(handle, contextExt, pipeline, new ChunkShaderOptions(ChunkFogMode.SMOOTH, pass.toTerrainPass(), vertexType),
						tessCShader != null || tessEShader != null, pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT, blendOverride, bufferOverrides, alpha, pipeline.getCustomUniforms());
				});
		} finally {
			vertShader.delete();
			if (geomShader != null) {
				geomShader.delete();
			}
			if (tessCShader != null) {
				tessCShader.delete();
			}
			if (tessEShader != null) {
				tessEShader.delete();
			}
			fragShader.delete();
		}
	}

	private float getAlphaReference(FirisTerrainPass pass, SodiumTerrainPipeline pipeline) {
		if (pass == FirisTerrainPass.SHADOW || pass == FirisTerrainPass.SHADOW_CUTOUT) {
			return pipeline.getShadowAlpha().orElse(AlphaTests.ONE_TENTH_ALPHA).reference();
		} else if (pass == FirisTerrainPass.GBUFFER_SOLID) {
			return AlphaTest.ALWAYS.reference();
		} else if (pass == FirisTerrainPass.GBUFFER_CUTOUT) {
			return pipeline.getTerrainCutoutAlpha().orElse(AlphaTests.ONE_TENTH_ALPHA).reference();
		} else if (pass == FirisTerrainPass.GBUFFER_TRANSLUCENT) {
			return pipeline.getTranslucentAlpha().orElse(AlphaTest.ALWAYS).reference();
		} else {
			throw new IllegalArgumentException("Unknown pass type " + pass);
		}
	}

	private SodiumTerrainPipeline getSodiumTerrainPipeline() {
		WorldRenderingPipeline worldRenderingPipeline = Firis.getPipelineManager().getPipelineNullable();

		if (worldRenderingPipeline != null) {
			return worldRenderingPipeline.getSodiumTerrainPipeline();
		} else {
			return null;
		}
	}

	public void createShaders(SodiumTerrainPipeline pipeline, ChunkVertexType vertexType) {
		if (pipeline != null) {
			pipeline.patchShaders(vertexType);
			for (FirisTerrainPass pass : FirisTerrainPass.values()) {
				if (pass.isShadow() && !pipeline.hasShadowPass()) {
					this.programs.put(pass, null);
					continue;
				}

				this.programs.put(pass, createShader(pass, pipeline, vertexType));
			}
		} else {
			for (GlProgram<?> program : this.programs.values()) {
				if (program != null) {
					program.delete();
				}
			}
			this.programs.clear();
		}

		shadersCreated = true;
	}

	@Nullable
	public GlProgram<FirisChunkShaderInterface> getProgramOverride(TerrainRenderPass pass, ChunkVertexType vertexType) {
		if (versionCounterForSodiumShaderReload != Firis.getPipelineManager().getVersionCounterForSodiumShaderReload()) {
			versionCounterForSodiumShaderReload = Firis.getPipelineManager().getVersionCounterForSodiumShaderReload();
			deleteShaders();
		}

		WorldRenderingPipeline worldRenderingPipeline = Firis.getPipelineManager().getPipelineNullable();
		SodiumTerrainPipeline sodiumTerrainPipeline = null;

		if (worldRenderingPipeline != null) {
			sodiumTerrainPipeline = worldRenderingPipeline.getSodiumTerrainPipeline();
		}

		if (!shadersCreated) {
			createShaders(sodiumTerrainPipeline, vertexType);
		}

		if (ShadowRenderingState.areShadowsCurrentlyBeingRendered()) {
			if (sodiumTerrainPipeline != null && !sodiumTerrainPipeline.hasShadowPass()) {
				throw new IllegalStateException("Shadow program requested, but the pack does not have a shadow pass?");
			}

			if (pass.supportsFragmentDiscard()) {
				return this.programs.get(FirisTerrainPass.SHADOW_CUTOUT);
			} else {
				return this.programs.get(FirisTerrainPass.SHADOW);
			}
		} else {
			if (pass.supportsFragmentDiscard()) {
				return this.programs.get(FirisTerrainPass.GBUFFER_CUTOUT);
			} else if (pass.isTranslucent()) {
				return this.programs.get(FirisTerrainPass.GBUFFER_TRANSLUCENT);
			} else {
				return this.programs.get(FirisTerrainPass.GBUFFER_SOLID);
			}
		}
	}

	public void bindFramebuffer(TerrainRenderPass pass) {
		SodiumTerrainPipeline pipeline = getSodiumTerrainPipeline();
		boolean isShadowPass = ShadowRenderingState.areShadowsCurrentlyBeingRendered();

		if (pipeline != null) {
			GlFramebuffer framebuffer;

			if (isShadowPass) {
				framebuffer = pipeline.getShadowFramebuffer();
			} else if (pass.isTranslucent()) {
				framebuffer = pipeline.getTranslucentFramebuffer();
			} else {
				framebuffer = pipeline.getTerrainSolidFramebuffer();
			}

			if (framebuffer != null) {
				framebuffer.bind();
			}
		}
	}

	public void unbindFramebuffer() {
		SodiumTerrainPipeline pipeline = getSodiumTerrainPipeline();

		if (pipeline != null) {
			Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
		}
	}

	public void deleteShaders() {
		for (GlProgram<?> program : this.programs.values()) {
			if (program != null) {
				program.delete();
			}
		}

		this.programs.clear();
		shadersCreated = false;
	}
}

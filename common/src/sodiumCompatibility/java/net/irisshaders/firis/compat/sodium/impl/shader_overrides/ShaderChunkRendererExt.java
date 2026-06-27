package net.irisshaders.firis.compat.sodium.impl.shader_overrides;

import net.caffeinemc.mods.fodium.client.gl.shader.GlProgram;

public interface ShaderChunkRendererExt {
	GlProgram<FirisChunkShaderInterface> firis$getOverride();
}

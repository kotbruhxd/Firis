package net.irisshaders.firis.compat.sodium.mixin.shader_overrides;

import net.caffeinemc.mods.fodium.client.gl.shader.ShaderType;
import net.irisshaders.firis.compat.sodium.impl.shader_overrides.FirisShaderTypes;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.opengl.GL42C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShaderType.class)
public class MixinShaderType {
	@SuppressWarnings("target")
	@Shadow(remap = false)
	@Final
	@Mutable
	private static ShaderType[] $VALUES;

	static {
		int baseOrdinal = $VALUES.length;

		FirisShaderTypes.GEOMETRY
			= ShaderTypeAccessor.createShaderType("GEOMETRY", baseOrdinal, GL32C.GL_GEOMETRY_SHADER);
		FirisShaderTypes.TESS_CONTROL
			= ShaderTypeAccessor.createShaderType("TESS_CONTROL", baseOrdinal + 1, GL42C.GL_TESS_CONTROL_SHADER);
		FirisShaderTypes.TESS_EVAL
			= ShaderTypeAccessor.createShaderType("TESS_EVAL", baseOrdinal + 2, GL42C.GL_TESS_EVALUATION_SHADER);

		$VALUES = ArrayUtils.addAll($VALUES, FirisShaderTypes.GEOMETRY, FirisShaderTypes.TESS_CONTROL, FirisShaderTypes.TESS_EVAL);
	}
}

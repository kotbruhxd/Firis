package net.irisshaders.firis.mixin;

import com.mojang.blaze3d.shaders.Program;
import net.irisshaders.firis.gl.program.FirisProgramTypes;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.opengl.GL42C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Program.Type.class)
public class MixinProgramType {
	@SuppressWarnings("target")
	@Shadow
	@Final
	@Mutable
	private static Program.Type[] $VALUES;

	static {
		int baseOrdinal = $VALUES.length;

		FirisProgramTypes.GEOMETRY
			= ProgramTypeAccessor.createProgramType("GEOMETRY", baseOrdinal, "geometry", ".gsh", GL32C.GL_GEOMETRY_SHADER);

		FirisProgramTypes.TESS_CONTROL
			= ProgramTypeAccessor.createProgramType("TESS_CONTROL", baseOrdinal + 1, "tess_control", ".tcs", GL42C.GL_TESS_CONTROL_SHADER);

		FirisProgramTypes.TESS_EVAL
			= ProgramTypeAccessor.createProgramType("TESS_EVAL", baseOrdinal + 2, "tess_eval", ".tes", GL42C.GL_TESS_EVALUATION_SHADER);

		$VALUES = ArrayUtils.addAll($VALUES, FirisProgramTypes.GEOMETRY, FirisProgramTypes.TESS_CONTROL, FirisProgramTypes.TESS_EVAL);
	}
}

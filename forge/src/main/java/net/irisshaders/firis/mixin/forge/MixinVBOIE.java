package net.irisshaders.firis.mixin.forge;

import net.irisshaders.firis.api.v0.FirisApi;
import net.irisshaders.firis.pipeline.programs.FallbackShader;
import net.irisshaders.firis.pipeline.programs.ShaderAccess;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

@Pseudo
@Mixin(targets = "blusunrize/immersiveengineering/client/utils/IEGLShaders", remap = false)
public class MixinVBOIE {
	@Shadow
	private static ShaderInstance vboShader;

	@Overwrite
	public static ShaderInstance getVboShader() {
		if (!FirisApi.getInstance().isShaderPackInUse()) {
			return vboShader;
		} else {
			ShaderInstance shader = ShaderAccess.getIEVBOShader();
			if (shader == null || shader instanceof FallbackShader) {
				return vboShader;
			} else {
				return shader;
			}
		}
	}
}

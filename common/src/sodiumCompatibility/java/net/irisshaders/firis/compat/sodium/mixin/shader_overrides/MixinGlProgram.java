package net.irisshaders.firis.compat.sodium.mixin.shader_overrides;

import com.mojang.blaze3d.platform.GlStateManager;
import net.caffeinemc.mods.fodium.client.gl.GlObject;
import net.caffeinemc.mods.fodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.fodium.client.gl.shader.uniform.GlUniform;
import net.caffeinemc.mods.fodium.client.gl.shader.uniform.GlUniformBlock;
import net.irisshaders.firis.compat.sodium.impl.shader_overrides.ShaderBindingContextExt;
import net.irisshaders.firis.gl.FirisRenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.IntFunction;

@Mixin(value = GlProgram.class, remap = false)
public class MixinGlProgram extends GlObject implements ShaderBindingContextExt {
	public <U extends GlUniform<?>> U bindUniformIfPresent(String name, IntFunction<U> factory) {
		int index = GlStateManager._glGetUniformLocation(this.handle(), name);
		if (index < 0) {
			return null;
		} else {
			return factory.apply(index);
		}
	}

	@Redirect(method = "bind", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL20C;glUseProgram(I)V"))
	private void firis$useGlStateManager(int i) {
		GlStateManager._glUseProgram(i);
	}

	@Redirect(method = "unbind", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL20C;glUseProgram(I)V"))
	private void firis$useGlStateManager2(int i) {
		GlStateManager._glUseProgram(i);
	}

	public GlUniformBlock bindUniformBlockIfPresent(String name, int bindingPoint) {
		int index = FirisRenderSystem.getUniformBlockIndex(this.handle(), name);
		if (index < 0) {
			return null;
		} else {
			FirisRenderSystem.uniformBlockBinding(this.handle(), index, bindingPoint);
			return new GlUniformBlock(bindingPoint);
		}
	}
}

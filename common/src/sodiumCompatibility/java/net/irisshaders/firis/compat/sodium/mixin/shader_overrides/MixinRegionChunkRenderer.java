package net.irisshaders.firis.compat.sodium.mixin.shader_overrides;

import net.caffeinemc.mods.fodium.client.gl.shader.GlProgram;
import net.caffeinemc.mods.fodium.client.render.chunk.DefaultChunkRenderer;
import net.irisshaders.firis.compat.sodium.impl.shader_overrides.ShaderChunkRendererExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultChunkRenderer.class)
public abstract class MixinRegionChunkRenderer implements ShaderChunkRendererExt {
	@Redirect(method = "render", remap = false,
		at = @At(value = "INVOKE",
			target = "net/caffeinemc/mods/sodium/client/gl/shader/GlProgram.getInterface ()Ljava/lang/Object;"))
	private Object firis$getInterface(GlProgram<?> program) {
		if (program == null) {
			// Firis sentinel null
			return firis$getOverride().getInterface();
		} else {
			return program.getInterface();
		}
	}

}

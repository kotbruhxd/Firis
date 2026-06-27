package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.caffeinemc.mods.fodium.client.render.vertex.VertexFormatDescriptionImpl;
import net.irisshaders.firis.vertices.FirisVertexFormats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VertexFormatDescriptionImpl.class)
public class MixinVertexFormatDescriptionImpl {
	// A better fix would be to treat FirisVertexFormats.PADDING_SHORT as padding, but this works too.
	@Inject(method = "checkSimple", at = @At("HEAD"), cancellable = true, remap = false)
	private static void firis$forceSimple(VertexFormat format, CallbackInfoReturnable<Boolean> cir) {
		if (format == FirisVertexFormats.TERRAIN || format == FirisVertexFormats.ENTITY || format == FirisVertexFormats.GLYPH) {
			cir.setReturnValue(true);
		}
	}
}

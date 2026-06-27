package net.irisshaders.firis.compat.sodium.impl.vertex_format;

import net.caffeinemc.mods.fodium.client.gl.attribute.GlVertexAttributeFormat;
import net.irisshaders.firis.compat.sodium.mixin.vertex_format.GlVertexAttributeFormatAccessor;
import org.lwjgl.opengl.GL20C;

public class FirisGlVertexAttributeFormat {
	public static final GlVertexAttributeFormat BYTE =
		GlVertexAttributeFormatAccessor.createGlVertexAttributeFormat(GL20C.GL_BYTE, 1);
	public static final GlVertexAttributeFormat SHORT = GlVertexAttributeFormatAccessor.createGlVertexAttributeFormat(GL20C.GL_SHORT, 2);
}

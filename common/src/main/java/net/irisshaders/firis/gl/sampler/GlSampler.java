package net.irisshaders.firis.gl.sampler;

import net.irisshaders.firis.gl.GlResource;
import net.irisshaders.firis.gl.FirisRenderSystem;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL30C;

public class GlSampler extends GlResource {
	public GlSampler(boolean linear, boolean mipmapped, boolean shadow, boolean hardwareShadow) {
		super(FirisRenderSystem.genSampler());

		FirisRenderSystem.samplerParameteri(getId(), GL11C.GL_TEXTURE_MIN_FILTER, linear ? GL11C.GL_LINEAR : GL11C.GL_NEAREST);
		FirisRenderSystem.samplerParameteri(getId(), GL11C.GL_TEXTURE_MAG_FILTER, linear ? GL11C.GL_LINEAR : GL11C.GL_NEAREST);
		FirisRenderSystem.samplerParameteri(getId(), GL11C.GL_TEXTURE_WRAP_S, GL13C.GL_CLAMP_TO_EDGE);
		FirisRenderSystem.samplerParameteri(getId(), GL11C.GL_TEXTURE_WRAP_T, GL13C.GL_CLAMP_TO_EDGE);

		if (mipmapped) {
			FirisRenderSystem.samplerParameteri(getId(), GL11C.GL_TEXTURE_MIN_FILTER, linear ? GL11C.GL_LINEAR_MIPMAP_LINEAR : GL11C.GL_NEAREST_MIPMAP_NEAREST);
		}

		if (hardwareShadow) {
			FirisRenderSystem.samplerParameteri(getId(), GL20C.GL_TEXTURE_COMPARE_MODE, GL30C.GL_COMPARE_REF_TO_TEXTURE);
		}
	}

	@Override
	protected void destroyInternal() {
		FirisRenderSystem.destroySampler(getGlId());
	}

	public int getId() {
		return getGlId();
	}
}

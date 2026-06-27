package net.irisshaders.firis.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.irisshaders.firis.targets.Blaze3dRenderTargetExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allows Firis to detect when the depth texture was re-created, so we can re-attach it
 * to the shader framebuffers. See DeferredWorldRenderingPipeline and RenderTargets.
 */
@Mixin(RenderTarget.class)
public class MixinRenderTarget implements Blaze3dRenderTargetExt {
	@Shadow
	protected int depthBufferId;

	@Unique
	private int firis$depthBufferVersion;
	@Unique
	private int firis$colorBufferVersion;

	@Inject(method = "destroyBuffers()V", at = @At("HEAD"))
	private void firis$onDestroyBuffers(CallbackInfo ci) {
		firis$depthBufferVersion++;
		firis$colorBufferVersion++;
	}

	@Override
	public int firis$getDepthBufferVersion() {
		return firis$depthBufferVersion;
	}

	@Override
	public int firis$getColorBufferVersion() {
		return firis$colorBufferVersion;
	}
}

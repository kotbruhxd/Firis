package net.irisshaders.firis.mixin;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL30C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A simple optimization to avoid redundant glBindFramebuffer calls, works in principle the same as things like
 * glBindTexture in GlStateManager.
 */
@Mixin(GlStateManager.class)
public class MixinGlStateManager_FramebufferBinding {
	private static int firis$drawFramebuffer = 0;
	private static int firis$readFramebuffer = 0;
	private static int firis$program = 0;

	@Inject(method = "_glBindFramebuffer(II)V", at = @At("HEAD"), cancellable = true, remap = false)
	private static void firis$avoidRedundantBind(int target, int framebuffer, CallbackInfo ci) {
		if (target == GlConst.GL_FRAMEBUFFER) {
			if (firis$drawFramebuffer == target && firis$readFramebuffer == target) {
				ci.cancel();
			} else {
				firis$drawFramebuffer = framebuffer;
				firis$readFramebuffer = framebuffer;
			}
		} else if (target == GL30C.GL_DRAW_FRAMEBUFFER) {
			if (firis$drawFramebuffer == target) {
				ci.cancel();
			} else {
				firis$drawFramebuffer = framebuffer;
			}
		} else if (target == GL30C.GL_READ_FRAMEBUFFER) {
			if (firis$readFramebuffer == target) {
				ci.cancel();
			} else {
				firis$readFramebuffer = framebuffer;
			}
		} else {
			throw new IllegalStateException("Invalid framebuffer target: " + target);
		}
	}

	@Inject(method = "_glUseProgram", at = @At("HEAD"), cancellable = true, remap = false)
	private static void firis$avoidRedundantBind2(int pInt0, CallbackInfo ci) {
		if (firis$program == pInt0) {
			ci.cancel();
		} else {
			firis$program = pInt0;
		}
	}

	@Inject(method = "_glDeleteFramebuffers(I)V", at = @At("HEAD"), remap = false)
	private static void firis$trackFramebufferDelete(int framebuffer, CallbackInfo ci) {
		if (firis$drawFramebuffer == framebuffer) {
			firis$drawFramebuffer = 0;
		}

		if (firis$readFramebuffer == framebuffer) {
			firis$readFramebuffer = 0;
		}
	}
}

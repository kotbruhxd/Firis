package net.irisshaders.firis.mixin.texture;

import net.irisshaders.firis.texture.format.TextureFormatLoader;
import net.irisshaders.firis.texture.pbr.PBRTextureManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TextureManager.class)
public class MixinTextureManager {
	@Inject(method = {
		"method_18167",
		"lambda$reload$5",
		"m_244739_"
	}, at = @At("TAIL"), require = 1)
	private void firis$onTailReloadLambda(ResourceManager resourceManager, Executor applyExecutor, CompletableFuture<?> future, Void void1, CallbackInfo ci) {
		TextureFormatLoader.reload(resourceManager);
		PBRTextureManager.INSTANCE.clear();
	}

	@Inject(method = "_dumpAllSheets(Ljava/nio/file/Path;)V", at = @At("RETURN"))
	private void firis$onInnerDumpTextures(Path path, CallbackInfo ci) {
		PBRTextureManager.INSTANCE.dumpTextures(path);
	}

	@Inject(method = "close()V", at = @At("TAIL"), remap = false)
	private void firis$onTailClose(CallbackInfo ci) {
		PBRTextureManager.INSTANCE.close();
	}
}

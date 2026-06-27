package net.irisshaders.firis.mixin.texture.pbr;

import net.irisshaders.firis.texture.pbr.PBRSpriteHolder;
import net.irisshaders.firis.texture.pbr.SpriteContentsExtension;
import net.minecraft.client.renderer.texture.SpriteContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteContents.class)
public class MixinSpriteContents implements SpriteContentsExtension {
	@Unique
	private PBRSpriteHolder pbrHolder;

	@Inject(method = "close()V", at = @At("TAIL"), remap = false)
	private void firis$onTailClose(CallbackInfo ci) {
		if (pbrHolder != null) {
			pbrHolder.close();
		}
	}

	@Override
	public PBRSpriteHolder getPBRHolder() {
		return pbrHolder;
	}

	@Override
	public PBRSpriteHolder getOrCreatePBRHolder() {
		if (pbrHolder == null) {
			pbrHolder = new PBRSpriteHolder();
		}
		return pbrHolder;
	}
}

package net.irisshaders.firis.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.irisshaders.firis.api.v0.FirisApi;
import net.irisshaders.firis.pathways.HandRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
	@Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
	private void firis$skipTranslucentHands(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
		if (FirisApi.getInstance().isShaderPackInUse()) {
			if (HandRenderer.INSTANCE.isRenderingSolid() && HandRenderer.INSTANCE.isHandTranslucent(interactionHand)) {
				ci.cancel();
			} else if (!HandRenderer.INSTANCE.isRenderingSolid() && !HandRenderer.INSTANCE.isHandTranslucent(interactionHand)) {
				ci.cancel();
			}
		}
	}
}

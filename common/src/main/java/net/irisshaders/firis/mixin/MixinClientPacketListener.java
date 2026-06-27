package net.irisshaders.firis.mixin;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.gl.shader.ShaderCompileException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
	@Inject(method = "handleLogin", at = @At("TAIL"))
	private void firis$showUpdateMessage(ClientboundLoginPacket a, CallbackInfo ci) {
		if (Minecraft.getInstance().player == null) {
			return;
		}

		Firis.getUpdateChecker().getUpdateMessage().ifPresent(msg ->
			Minecraft.getInstance().player.displayClientMessage(msg, false));

		Firis.getStoredError().ifPresent(e ->
			Minecraft.getInstance().player.displayClientMessage(Component.translatable(e instanceof ShaderCompileException ? "firis.load.failure.shader" : "firis.load.failure.generic").append(Component.literal("Copy Info").withStyle(arg -> arg.withUnderlined(true).withColor(ChatFormatting.BLUE).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, e.getMessage())))), false));

		if (Firis.loadedIncompatiblePack()) {
			Minecraft.getInstance().gui.setTimes(10, 70, 140);
			Firis.logger.warn("Incompatible pack for DH!");
			Minecraft.getInstance().player.displayClientMessage(Component.literal("This pack doesn't have DH support.").withStyle(ChatFormatting.BOLD, ChatFormatting.RED), false);
			Minecraft.getInstance().player.displayClientMessage(Component.literal("Distant Horizons (DH) chunks won't show up. This isn't a bug, get another shader.").withStyle(ChatFormatting.RED), false);
		}
	}
}

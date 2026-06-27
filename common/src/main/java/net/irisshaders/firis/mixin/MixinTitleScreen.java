package net.irisshaders.firis.mixin;

import net.irisshaders.firis.Firis;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
	private static boolean firis$hasFirstInit;

	protected MixinTitleScreen(Component arg) {
		super(arg);
	}

	@Inject(method = "init", at = @At("RETURN"))
	public void firis$firstInit(CallbackInfo ci) {
		if (!firis$hasFirstInit) {
			Firis.onLoadingComplete();
		}

		firis$hasFirstInit = true;

	}
}

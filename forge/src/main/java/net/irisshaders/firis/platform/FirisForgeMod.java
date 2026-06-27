package net.irisshaders.firis.platform;

import net.irisshaders.firis.api.v0.FirisApi;
import net.irisshaders.firis.gui.screen.ShaderPackScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Mod("firis")
public class FirisForgeMod {
	public static List<KeyMapping> KEYLIST = new ArrayList<>();

	public FirisForgeMod() {
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> {
			return new ShaderPackScreen(screen);
		}));
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeys);
	}

	public void registerKeys(RegisterKeyMappingsEvent event) {
		KEYLIST.forEach(event::register);
		KEYLIST.clear();
	}
}

package net.irisshaders.firis.platform;

import net.minecraft.client.KeyMapping;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.ServiceLoader;

public interface FirisPlatformHelpers {
	FirisPlatformHelpers INSTANCE = ServiceLoader.load(FirisPlatformHelpers.class).findFirst().get();

	static FirisPlatformHelpers getInstance() {
		return INSTANCE;
	}

	boolean isModLoaded(String modId);

	String getVersion();

	boolean isDevelopmentEnvironment();

	Path getGameDir();

	Path getConfigDir();

	int compareVersions(String currentVersion, String semanticVersion) throws Exception;

	KeyMapping registerKeyBinding(KeyMapping keyMapping);
}

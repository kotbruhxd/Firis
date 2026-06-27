package net.irisshaders.firis.platform;

import net.irisshaders.firis.Firis;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.nio.file.Path;

public class FirisForgeHelpers implements FirisPlatformHelpers{
	@Override
	public boolean isModLoaded(String modId) {
		return LoadingModList.get().getModFileById(modId) != null;
	}

	@Override
	public String getVersion() {
		return LoadingModList.get().getModFileById(Firis.MODID).versionString();
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public Path getGameDir() {
		return FMLPaths.GAMEDIR.get();
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public int compareVersions(String currentVersion, String semanticVersion) throws Exception {
		return new DefaultArtifactVersion(currentVersion).compareTo(new DefaultArtifactVersion(semanticVersion));
	}

	@Override
	public KeyMapping registerKeyBinding(KeyMapping keyMapping) {
		FirisForgeMod.KEYLIST.add(keyMapping);
		return keyMapping;
	}
}

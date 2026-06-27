package net.irisshaders.firis.mixinterface;

import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public interface ShaderInstanceInterface {
	void firis$createExtraShaders(ResourceProvider factory, String name) throws IOException;
}

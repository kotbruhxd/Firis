package net.irisshaders.firis.shaderpack;

import net.irisshaders.firis.gl.texture.InternalTextureFormat;
import net.irisshaders.firis.gl.texture.PixelFormat;
import net.irisshaders.firis.gl.texture.PixelType;
import net.irisshaders.firis.gl.texture.TextureType;

public record ImageInformation(String name, String samplerName, TextureType target, PixelFormat format,
							   InternalTextureFormat internalTextureFormat,
							   PixelType type, int width, int height, int depth, boolean clear, boolean isRelative,
							   float relativeWidth, float relativeHeight) {
}

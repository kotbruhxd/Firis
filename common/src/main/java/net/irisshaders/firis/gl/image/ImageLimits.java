package net.irisshaders.firis.gl.image;

import net.irisshaders.firis.gl.FirisRenderSystem;

public class ImageLimits {
	private static ImageLimits instance;
	private final int maxImageUnits;

	private ImageLimits() {
		this.maxImageUnits = FirisRenderSystem.getMaxImageUnits();
	}

	public static ImageLimits get() {
		if (instance == null) {
			instance = new ImageLimits();
		}

		return instance;
	}

	public int getMaxImageUnits() {
		return maxImageUnits;
	}
}

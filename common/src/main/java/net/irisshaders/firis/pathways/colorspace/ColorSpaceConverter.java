package net.irisshaders.firis.pathways.colorspace;

public interface ColorSpaceConverter {
	void rebuildProgram(int width, int height, ColorSpace colorSpace);

	void process(int target);
}

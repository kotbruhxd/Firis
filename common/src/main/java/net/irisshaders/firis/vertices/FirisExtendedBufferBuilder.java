package net.irisshaders.firis.vertices;

import com.mojang.blaze3d.vertex.VertexFormat;

public interface FirisExtendedBufferBuilder {
	VertexFormat firis$format();

	VertexFormat.Mode firis$mode();

	boolean firis$extending();

	boolean firis$isTerrain();

	boolean firis$injectNormalAndUV1();

	int firis$vertexCount();

	void firis$incrementVertexCount();

	void firis$resetVertexCount();

	short firis$currentBlock();

	short firis$currentRenderType();

	int firis$currentLocalPosX();

	int firis$currentLocalPosY();

	int firis$currentLocalPosZ();
}

package net.irisshaders.firis.compat.sodium.impl;

/**
 * Defines Firis-specific chunk shader binding points.
 * <p>
 * NB: Make sure this doesn't collide with anything in {@link net.caffeinemc.mods.fodium.client.render.chunk.shader.ChunkShaderBindingPoints}
 */
public class FirisChunkShaderBindingPoints {
	public static final int NORMAL = 10;
	public static final int BLOCK_ID = 11;
	public static final int MID_TEX_COORD = 12;
	public static final int TANGENT = 13;
	public static final int MID_BLOCK = 14;
}

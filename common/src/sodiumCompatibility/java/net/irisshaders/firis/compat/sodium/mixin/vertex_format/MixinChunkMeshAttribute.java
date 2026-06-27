package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkMeshAttribute;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisChunkMeshAttributes;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Uses some rather hacky shenanigans to add a few new enum values to {@link ChunkMeshAttribute} corresponding to our
 * extended vertex attributes.
 * <p>
 * Credit goes to Nuclearfarts for the trick.
 */
@Mixin(ChunkMeshAttribute.class)
public class MixinChunkMeshAttribute {
	@SuppressWarnings("target")
	@Shadow(remap = false)
	@Final
	@Mutable
	private static ChunkMeshAttribute[] $VALUES;

	static {
		int baseOrdinal = $VALUES.length;

		FirisChunkMeshAttributes.NORMAL
			= ChunkMeshAttributeAccessor.createChunkMeshAttribute("NORMAL", baseOrdinal);
		FirisChunkMeshAttributes.TANGENT
			= ChunkMeshAttributeAccessor.createChunkMeshAttribute("TANGENT", baseOrdinal + 1);
		FirisChunkMeshAttributes.MID_TEX_COORD
			= ChunkMeshAttributeAccessor.createChunkMeshAttribute("MID_TEX_COORD", baseOrdinal + 2);
		FirisChunkMeshAttributes.BLOCK_ID
			= ChunkMeshAttributeAccessor.createChunkMeshAttribute("BLOCK_ID", baseOrdinal + 3);
		FirisChunkMeshAttributes.MID_BLOCK
			= ChunkMeshAttributeAccessor.createChunkMeshAttribute("MID_BLOCK", baseOrdinal + 4);

		$VALUES = ArrayUtils.addAll($VALUES,
			FirisChunkMeshAttributes.NORMAL,
			FirisChunkMeshAttributes.TANGENT,
			FirisChunkMeshAttributes.MID_TEX_COORD,
			FirisChunkMeshAttributes.BLOCK_ID,
			FirisChunkMeshAttributes.MID_BLOCK);
	}
}

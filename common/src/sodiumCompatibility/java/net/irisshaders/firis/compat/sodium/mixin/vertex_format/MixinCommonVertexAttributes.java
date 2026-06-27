package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import net.caffeinemc.mods.fodium.api.vertex.attributes.CommonVertexAttribute;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisCommonVertexAttributes;
import net.irisshaders.firis.vertices.FirisVertexFormats;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Uses some rather hacky shenanigans to add a few new enum values to {@link CommonVertexAttribute} corresponding to our
 * extended vertex attributes.
 * <p>
 * Credit goes to Nuclearfarts for the trick.
 */
@Mixin(CommonVertexAttribute.class)
public class MixinCommonVertexAttributes {
	@Mutable
	@Shadow
	@Final
	public static int COUNT;
	@SuppressWarnings("target")
	@Shadow(remap = false)
	@Final
	@Mutable
	private static CommonVertexAttribute[] $VALUES;

	static {
		int baseOrdinal = $VALUES.length;

		FirisCommonVertexAttributes.TANGENT
			= CommonVertexAttributeAccessor.createCommonVertexElement("TANGENT", baseOrdinal, FirisVertexFormats.TANGENT_ELEMENT);
		FirisCommonVertexAttributes.MID_TEX_COORD
			= CommonVertexAttributeAccessor.createCommonVertexElement("MID_TEX_COORD", baseOrdinal + 1, FirisVertexFormats.MID_TEXTURE_ELEMENT);
		FirisCommonVertexAttributes.BLOCK_ID
			= CommonVertexAttributeAccessor.createCommonVertexElement("BLOCK_ID", baseOrdinal + 2, FirisVertexFormats.ENTITY_ELEMENT);
		FirisCommonVertexAttributes.ENTITY_ID
			= CommonVertexAttributeAccessor.createCommonVertexElement("ENTITY_ID", baseOrdinal + 3, FirisVertexFormats.ENTITY_ID_ELEMENT);
		FirisCommonVertexAttributes.MID_BLOCK
			= CommonVertexAttributeAccessor.createCommonVertexElement("MID_BLOCK", baseOrdinal + 4, FirisVertexFormats.MID_BLOCK_ELEMENT);

		$VALUES = ArrayUtils.addAll($VALUES,
			FirisCommonVertexAttributes.TANGENT,
			FirisCommonVertexAttributes.MID_TEX_COORD,
			FirisCommonVertexAttributes.BLOCK_ID,
			FirisCommonVertexAttributes.ENTITY_ID,
			FirisCommonVertexAttributes.MID_BLOCK);

		COUNT = $VALUES.length;
	}
}

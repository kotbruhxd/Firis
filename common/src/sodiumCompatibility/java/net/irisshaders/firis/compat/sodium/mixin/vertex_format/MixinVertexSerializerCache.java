package net.irisshaders.firis.compat.sodium.mixin.vertex_format;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import net.caffeinemc.mods.fodium.client.render.vertex.serializers.VertexSerializerRegistryImpl;
import net.caffeinemc.mods.fodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.fodium.api.vertex.format.VertexFormatRegistry;
import net.caffeinemc.mods.fodium.api.vertex.serializer.VertexSerializer;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.EntityToTerrainVertexSerializer;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.GlyphExtVertexSerializer;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.FirisEntityToTerrainVertexSerializer;
import net.irisshaders.firis.compat.sodium.impl.vertex_format.ModelToEntityVertexSerializer;
import net.irisshaders.firis.vertices.FirisVertexFormats;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VertexSerializerRegistryImpl.class, remap = false)
public abstract class MixinVertexSerializerCache {
	@Shadow
	@Final
	private Long2ReferenceMap<VertexSerializer> cache;

	@Shadow
	protected static long createKey(VertexFormatDescription a, VertexFormatDescription b) {
		return 0;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void putSerializerIris(CallbackInfo ci) {
		cache.put(createKey(VertexFormatRegistry.instance().get(DefaultVertexFormat.NEW_ENTITY), VertexFormatRegistry.instance().get(FirisVertexFormats.ENTITY)), new ModelToEntityVertexSerializer());
		cache.put(createKey(VertexFormatRegistry.instance().get(FirisVertexFormats.ENTITY), VertexFormatRegistry.instance().get(FirisVertexFormats.TERRAIN)), new FirisEntityToTerrainVertexSerializer());
		cache.put(createKey(VertexFormatRegistry.instance().get(DefaultVertexFormat.NEW_ENTITY), VertexFormatRegistry.instance().get(FirisVertexFormats.TERRAIN)), new EntityToTerrainVertexSerializer());
		cache.put(createKey(VertexFormatRegistry.instance().get(DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), VertexFormatRegistry.instance().get(FirisVertexFormats.GLYPH)), new GlyphExtVertexSerializer());
	}
}

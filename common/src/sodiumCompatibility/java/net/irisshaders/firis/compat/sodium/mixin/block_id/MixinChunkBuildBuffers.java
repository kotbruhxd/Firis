package net.irisshaders.firis.compat.sodium.mixin.block_id;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.caffeinemc.mods.fodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.fodium.client.render.chunk.compile.buffers.BakedChunkModelBuilder;
import net.caffeinemc.mods.fodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.fodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.irisshaders.firis.compat.sodium.impl.block_context.BlockContextHolder;
import net.irisshaders.firis.compat.sodium.impl.block_context.ChunkBuildBuffersExt;
import net.irisshaders.firis.compat.sodium.impl.block_context.ContextAwareVertexWriter;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Associates the block context holder with the chunk build buffers, allowing {@link MixinChunkRenderRebuildTask} to pass
 * data to {@link ContextAwareVertexWriter}.
 */
@Mixin(ChunkBuildBuffers.class)
public class MixinChunkBuildBuffers implements ChunkBuildBuffersExt {
	@Shadow
	@Final
	private Reference2ReferenceOpenHashMap<TerrainRenderPass, BakedChunkModelBuilder> builders;
	@Unique
	private BlockContextHolder contextHolder;

	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	private void firis$onConstruct(ChunkVertexType vertexType, CallbackInfo ci) {
		Object2IntMap<BlockState> blockStateIds = WorldRenderingSettings.INSTANCE.getBlockStateIds();

		if (blockStateIds != null) {
			this.contextHolder = new BlockContextHolder(blockStateIds);
		} else {
			this.contextHolder = new BlockContextHolder();
		}
	}

	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL", remap = false))
	private void firis$redirectWriterCreation(ChunkVertexType vertexType, CallbackInfo ci) {
		for (BakedChunkModelBuilder builder : this.builders.values()) {
			if (builder instanceof ContextAwareVertexWriter) {
				((ContextAwareVertexWriter) builder).firis$setContextHolder(contextHolder);
			}
		}
	}

	@Override
	public void firis$setLocalPos(int localPosX, int localPosY, int localPosZ) {
		this.contextHolder.setLocalPos(localPosX, localPosY, localPosZ);
	}

	@Override
	public void firis$setMaterialId(BlockState state, short renderType, byte lightValue) {
		this.contextHolder.set(state, renderType, lightValue);
	}

	@Override
	public void firis$resetBlockContext() {
		this.contextHolder.reset();
	}

	@Override
	public void firis$ignoreMidBlock(boolean state) {
		this.contextHolder.ignoreMidBlock = state;
	}
}

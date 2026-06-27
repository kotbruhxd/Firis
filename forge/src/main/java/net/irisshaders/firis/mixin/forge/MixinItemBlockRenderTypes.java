package net.irisshaders.firis.mixin.forge;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.shaderpack.materialmap.BlockMaterialMapping;
import net.irisshaders.firis.shaderpack.materialmap.BlockRenderType;
import net.irisshaders.firis.shaderpack.materialmap.WorldRenderingSettings;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemBlockRenderTypes.class)
public class MixinItemBlockRenderTypes {
	@Unique
	private static final ChunkRenderTypeSet[] LAYER_SET;

	static {
		LAYER_SET = new ChunkRenderTypeSet[BlockRenderType.values().length];
		for (int i = 0; i < BlockRenderType.values().length; i++) {
			LAYER_SET[i] = ChunkRenderTypeSet.of(BlockMaterialMapping.convertBlockToRenderType(BlockRenderType.values()[i]));
		}
	}

	@Inject(method = "getRenderLayers", at = @At("HEAD"), cancellable = true, remap = false)
	private static void firis$setCustomRenderType(BlockState arg, CallbackInfoReturnable<ChunkRenderTypeSet> cir) {
		Map<Block, BlockRenderType> idMap = WorldRenderingSettings.INSTANCE.getBlockTypeIds();
		if (idMap != null) {
			BlockRenderType type = idMap.get(arg.getBlock());
			if (type != null) {
				cir.setReturnValue(LAYER_SET[type.ordinal()]);
			}
		}
	}
}

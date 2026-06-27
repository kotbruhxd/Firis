package net.irisshaders.firis.mixin;

import net.irisshaders.firis.api.v0.item.FirisItemLightProvider;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class MixinItem implements FirisItemLightProvider {
}

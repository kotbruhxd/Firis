package net.irisshaders.firis.compat.sodium.mixin.options;

import net.caffeinemc.mods.fodium.client.gui.options.OptionImpl;
import net.irisshaders.firis.compat.sodium.impl.options.OptionImplExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BooleanSupplier;

/**
 * Allows a slider to be dynamically enabled or disabled based on some external condition.
 */
@Mixin(OptionImpl.class)
public class MixinOptionImpl implements OptionImplExtended {
	@Unique
	private BooleanSupplier firis$dynamicallyEnabled;

	@Override
	public void firis$dynamicallyEnable(BooleanSupplier enabled) {
		this.firis$dynamicallyEnabled = enabled;
	}

	@Inject(method = "isAvailable()Z", at = @At("HEAD"), cancellable = true, remap = false)
	private void firis$dynamicallyEnable(CallbackInfoReturnable<Boolean> cir) {
		if (firis$dynamicallyEnabled != null) {
			cir.setReturnValue(firis$dynamicallyEnabled.getAsBoolean());
		}
	}
}

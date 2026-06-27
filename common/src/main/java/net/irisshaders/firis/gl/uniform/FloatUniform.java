package net.irisshaders.firis.gl.uniform;

import net.irisshaders.firis.gl.FirisRenderSystem;
import net.irisshaders.firis.gl.state.ValueUpdateNotifier;

public class FloatUniform extends Uniform {
	private final FloatSupplier value;
	private float cachedValue;

	FloatUniform(int location, FloatSupplier value) {
		this(location, value, null);
	}

	FloatUniform(int location, FloatSupplier value, ValueUpdateNotifier notifier) {
		super(location, notifier);

		this.cachedValue = 0;
		this.value = value;
	}

	@Override
	public void update() {
		updateValue();

		if (notifier != null) {
			notifier.setListener(this::updateValue);
		}
	}

	private void updateValue() {
		float newValue = value.getAsFloat();

		if (cachedValue != newValue) {
			cachedValue = newValue;
			FirisRenderSystem.uniform1f(location, newValue);
		}
	}
}

package net.irisshaders.firis.gl.uniform;

import net.irisshaders.firis.gl.FirisRenderSystem;
import net.irisshaders.firis.gl.state.ValueUpdateNotifier;
import org.joml.Vector4i;

import java.util.function.Supplier;

public class Vector4IntegerJomlUniform extends Uniform {
	private final Supplier<Vector4i> value;
	private Vector4i cachedValue;

	Vector4IntegerJomlUniform(int location, Supplier<Vector4i> value) {
		this(location, value, null);
	}

	Vector4IntegerJomlUniform(int location, Supplier<Vector4i> value, ValueUpdateNotifier notifier) {
		super(location, notifier);

		this.cachedValue = null;
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
		Vector4i newValue = value.get();

		if (!newValue.equals(cachedValue)) {
			cachedValue = newValue;
			FirisRenderSystem.uniform4i(this.location, newValue.x, newValue.y, newValue.z, newValue.w);
		}
	}
}

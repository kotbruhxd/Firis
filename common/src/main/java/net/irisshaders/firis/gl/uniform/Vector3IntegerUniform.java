package net.irisshaders.firis.gl.uniform;

import net.irisshaders.firis.gl.FirisRenderSystem;
import net.irisshaders.firis.gl.state.ValueUpdateNotifier;
import org.joml.Vector3i;

import java.util.function.Supplier;

public class Vector3IntegerUniform extends Uniform {
	private final Vector3i cachedValue;
	private final Supplier<Vector3i> value;

	Vector3IntegerUniform(int location, Supplier<Vector3i> value) {
		super(location);

		this.cachedValue = new Vector3i();
		this.value = value;
	}

	Vector3IntegerUniform(int location, Supplier<Vector3i> value, ValueUpdateNotifier notifier) {
		super(location, notifier);

		this.cachedValue = new Vector3i();
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
		Vector3i newValue = value.get();

		if (!newValue.equals(cachedValue)) {
			cachedValue.set(newValue.x(), newValue.y(), newValue.z());
			FirisRenderSystem.uniform3i(location, cachedValue.x(), cachedValue.y(), cachedValue.z());
		}
	}
}

package net.irisshaders.firis.shaderpack.properties;

import net.irisshaders.firis.Firis;

import java.util.Optional;

public enum ParticleRenderingSettings {
	BEFORE,
	MIXED,
	AFTER;

	public static Optional<ParticleRenderingSettings> fromString(String name) {
		try {
			return Optional.of(ParticleRenderingSettings.valueOf(name));
		} catch (IllegalArgumentException e) {
			Firis.logger.warn("Invalid particle rendering settings! " + name);
			return Optional.empty();
		}
	}
}

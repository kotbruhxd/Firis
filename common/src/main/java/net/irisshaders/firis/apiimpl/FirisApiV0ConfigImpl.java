package net.irisshaders.firis.apiimpl;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.api.v0.FirisApiConfig;
import net.irisshaders.firis.config.FirisConfig;

import java.io.IOException;

public class FirisApiV0ConfigImpl implements FirisApiConfig {
	@Override
	public boolean areShadersEnabled() {
		return Firis.getFirisConfig().areShadersEnabled();
	}

	@Override
	public void setShadersEnabledAndApply(boolean enabled) {
		FirisConfig config = Firis.getFirisConfig();

		config.setShadersEnabled(enabled);

		try {
			config.save();
		} catch (IOException e) {
			Firis.logger.error("Error saving configuration file!", e);
		}

		try {
			Firis.reload();
		} catch (IOException e) {
			Firis.logger.error("Error reloading shader pack while applying changes!", e);
		}
	}
}

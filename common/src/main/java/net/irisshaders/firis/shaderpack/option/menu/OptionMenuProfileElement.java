package net.irisshaders.firis.shaderpack.option.menu;

import net.irisshaders.firis.Firis;
import net.irisshaders.firis.shaderpack.option.OptionSet;
import net.irisshaders.firis.shaderpack.option.ProfileSet;
import net.irisshaders.firis.shaderpack.option.values.MutableOptionValues;
import net.irisshaders.firis.shaderpack.option.values.OptionValues;

public class OptionMenuProfileElement extends OptionMenuElement {
	public final ProfileSet profiles;
	public final OptionSet options;

	private final OptionValues packAppliedValues;

	public OptionMenuProfileElement(ProfileSet profiles, OptionSet options, OptionValues packAppliedValues) {
		this.profiles = profiles;
		this.options = options;
		this.packAppliedValues = packAppliedValues;
	}

	/**
	 * @return an {@link OptionValues} that also contains values currently
	 * pending application.
	 */
	public OptionValues getPendingOptionValues() {
		MutableOptionValues values = packAppliedValues.mutableCopy();
		values.addAll(Firis.getShaderPackOptionQueue());

		return values;
	}
}

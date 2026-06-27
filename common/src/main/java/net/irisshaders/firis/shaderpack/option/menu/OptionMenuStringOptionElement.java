package net.irisshaders.firis.shaderpack.option.menu;

import net.irisshaders.firis.shaderpack.option.StringOption;
import net.irisshaders.firis.shaderpack.option.values.OptionValues;
import net.irisshaders.firis.shaderpack.properties.ShaderProperties;

public class OptionMenuStringOptionElement extends OptionMenuOptionElement {
	public final StringOption option;

	public OptionMenuStringOptionElement(String elementString, OptionMenuContainer container, ShaderProperties shaderProperties, OptionValues values, StringOption option) {
		super(elementString, container, shaderProperties, values);
		this.option = option;
	}
}

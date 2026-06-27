package net.irisshaders.firis.shaderpack.option.menu;

import net.irisshaders.firis.shaderpack.option.BooleanOption;
import net.irisshaders.firis.shaderpack.option.values.OptionValues;
import net.irisshaders.firis.shaderpack.properties.ShaderProperties;

public class OptionMenuBooleanOptionElement extends OptionMenuOptionElement {
	public final BooleanOption option;

	public OptionMenuBooleanOptionElement(String elementString, OptionMenuContainer container, ShaderProperties shaderProperties, OptionValues values, BooleanOption option) {
		super(elementString, container, shaderProperties, values);
		this.option = option;
	}
}

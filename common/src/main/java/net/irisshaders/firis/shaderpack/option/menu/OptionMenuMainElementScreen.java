package net.irisshaders.firis.shaderpack.option.menu;

import net.irisshaders.firis.shaderpack.option.ShaderPackOptions;
import net.irisshaders.firis.shaderpack.properties.ShaderProperties;

import java.util.List;
import java.util.Optional;

public class OptionMenuMainElementScreen extends OptionMenuElementScreen {
	public OptionMenuMainElementScreen(OptionMenuContainer container, ShaderProperties shaderProperties, ShaderPackOptions shaderPackOptions, List<String> elementStrings, Optional<Integer> columnCount) {
		super(container, shaderProperties, shaderPackOptions, elementStrings, columnCount);
	}
}

package com.code1024.dependlib;

import org.eclipse.ant.core.IAntPropertyValueProvider;

public class AntProperty implements IAntPropertyValueProvider {

	@Override
	public String getAntPropertyValue(String antPropertyName) {
		return DependLibPlugin.getDefault().getDependLibHome().toFile().getAbsolutePath();
	}

}

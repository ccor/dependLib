package com.code1024.dependlib;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore prefStore = DependLibPlugin.getDefault().getPreferenceStore();
		String home = System.getenv("DEPEND_LIB_HOME");
		prefStore.setDefault(DependLibPlugin.PREF_DEPEND_LIB_HOME, home);
	}

}

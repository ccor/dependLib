package com.code1024.dependlib;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DependLibPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	public DependLibPreferencePage() {
		super(GRID);
		setPreferenceStore(DependLibPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		DirectoryFieldEditor dependLibHome = 
				new DirectoryFieldEditor(DependLibPlugin.PREF_DEPEND_LIB_HOME, "DependLib Home", getFieldEditorParent());
		addField(dependLibHome);
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}

}

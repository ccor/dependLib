package com.code1024.dependlib;


import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class DependLibConfPage extends WizardPage implements
		IClasspathContainerPage, IClasspathContainerPageExtension {

	private IJavaProject proj;
	private StringFieldEditor conf;
	private String confFileName = DependLibPlugin.DEPEND_LIB_CONF;
	
	public DependLibConfPage() {
		super("Depend Lib Conf", "The depend library conf for you project.", null);
		setPageComplete(true);
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        conf = new StringFieldEditor("dependConf", "DependLib Conf", composite);
        conf.setStringValue(confFileName);

        setControl(composite); 
	}

	@Override
	public boolean finish() {
		if(!DependLibPlugin.getDefault().getDependLibHome().toFile().exists()){
			setErrorMessage("DependLib Home not exists.");
			return false;
		}
		File f = proj.getProject().getLocation().toFile();
		String confName = conf.getStringValue();
		f = new File(f, confName);
		if(f.exists()) {
			return true;
		}else{
			setErrorMessage("DependLib conf file not exists.");
			return false;
		}
	}

	@Override
	public IClasspathEntry getSelection() {
		String confName = conf.getStringValue();
        IPath containerPath = DependLibContainer.ID.append( "/" + confName);
        return JavaCore.newContainerEntry(containerPath);
	}

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
		if(containerEntry != null){
			confFileName = containerEntry.getPath().lastSegment();
		}
	}

	@Override
	public void initialize(IJavaProject project,
			IClasspathEntry[] currentEntries) {
		proj = project;
		
	}

}

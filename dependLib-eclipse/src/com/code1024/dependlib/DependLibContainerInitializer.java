package com.code1024.dependlib;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class DependLibContainerInitializer extends
		ClasspathContainerInitializer {

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		DependLibContainer libContainer = new DependLibContainer(containerPath, project);
		if(libContainer.isValid()){
			JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {project}, new IClasspathContainer[] {libContainer}, null);   
		}else{
			DependLibPlugin.getDefault().error("depend lib load failed.");
		}
	}

	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath,
			IJavaProject project) {
		return true;
	}
	
	@Override
	public void requestClasspathContainerUpdate(IPath containerPath,
			IJavaProject project, IClasspathContainer containerSuggestion)
			throws CoreException {
		  JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },   new IClasspathContainer[] { containerSuggestion }, null);
	}
}

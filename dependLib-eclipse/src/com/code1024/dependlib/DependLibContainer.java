package com.code1024.dependlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class DependLibContainer implements IClasspathContainer{

	public final static Path ID = new Path("com.code1024.dependlib.DEPENDLIB_CONF");
    private File _conf;
    private IClasspathEntry[] cacheEntries;
    private long cacheLastM = -1L;
    
	public DependLibContainer(IPath path, IJavaProject project) {
        File projDir = project.getProject().getLocation().makeAbsolute().toFile();
        _conf = new File(projDir, path.lastSegment());
	}
	
	@Override
	public IClasspathEntry[] getClasspathEntries() {
		long lastm = _conf.lastModified();
		if(lastm == cacheLastM){
			return cacheEntries;
		}
		IPath dependLibHome = DependLibPlugin.getDefault().getDependLibHome();
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(_conf));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.length() > 0 && !line.startsWith("#")){
					String[] tmp = line.split(">");
					 entryList.add(
							 JavaCore.newLibraryEntry(dependLibHome.append(tmp[0].trim()), null, null));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			DependLibPlugin.getDefault().error("Read dependLib conf failed.", e);
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					DependLibPlugin.getDefault().error("Try close dependLib conf failed.", e);
				}
			}
		}
		IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
		cacheEntries = entryArray;
		cacheLastM = lastm;
        return (IClasspathEntry[])entryList.toArray(entryArray);
	}

	@Override
	public String getDescription() {
		return DependLibPlugin.DEPEND_LIB;
	}

	@Override
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return ID;
	}
	
   public boolean isValid() {
	   IPath dependLibHome = DependLibPlugin.getDefault().getDependLibHome();
        if(dependLibHome != null && dependLibHome.toFile().exists() 
        		&& _conf.exists() && _conf.isFile()) {
            return true;
        }
        return false;
    }
   
}

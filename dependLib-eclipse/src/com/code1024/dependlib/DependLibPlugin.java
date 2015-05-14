package com.code1024.dependlib;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class DependLibPlugin extends AbstractUIPlugin {
	
	/**
	 * 配置文件默认名称
	 */
	public final static String DEPEND_LIB_CONF = "depend.conf";
	
	/**
	 * 库显示的名称
	 */
	public final static String DEPEND_LIB = "DependLib";
	
	/**
	 * 依赖库的主目录
	 */
	public final static String PREF_DEPEND_LIB_HOME = "dependLibHome";
	
	private static DependLibPlugin plugin;
	private ILog _log;
	
	public DependLibPlugin() {
		plugin = this;
		_log = getLog();
	}
	
	public static DependLibPlugin getDefault(){
		return plugin;
	}
	
	public IPath getDependLibHome(){
		IPreferenceStore pref =	getPreferenceStore();
		String home = pref.getString(PREF_DEPEND_LIB_HOME);
		if("".equals(home)){
			return null;
		}
		return new Path(home);
	}
	
	public void error(String msg) {
		_log.log(new Status(IStatus.ERROR, getBundle().getSymbolicName(), msg));
	}
	
	public void error(String msg, Exception e) {
		_log.log(new Status(IStatus.ERROR, getBundle().getSymbolicName(), IStatus.ERROR, msg, e));
	}
	
}

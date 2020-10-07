/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import java.util.HashMap;
import java.util.Map;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class LoadableObject 
	extends KMObject 
	implements Loadable 
{
	private final static Class _loadableBaseClass = Loadable.class;
	private final static Map _loadableClasses = new HashMap();
	
	private static Class get_loadableBaseClass() {return _loadableBaseClass;}
	private static Map get_loadableClasses() {return _loadableClasses;}
	
	public static Class getLoadableClass(Object name) {
		Class loadableClass = ((Class)(get_loadableClasses().get(name)));
		if (loadableClass == null) {
			String className =  get_loadableBaseClass().toString() + name;
			try {
				loadableClass =  Class.forName(className);
			} catch (ClassNotFoundException ex) {
				// okee dokey
			}
			if (loadableClass != null) {
				get_loadableClasses().put(className, loadableClass);
			}
		}
		return loadableClass;
	}
	
	private Object[] _parameters = null;
	
	private Object[] get_parameters() {return _parameters;}
	private void set_parameters(Object[] parameters) {_parameters = parameters;}
	
	protected LoadableObject() {super();}
	
	public LoadableObject(Object[] parameters) {
		this();
		set_parameters(parameters);
	}
	
	public Object[] getParameters() {
		return get_parameters();
	}

	public Object getParameter(int i)
	{
		Object parameter = get_parameters()[i];
		return parameter;
	}
	
	public Object load(Context context) {
		return null;
	}
	
	public int	getSize () {
		int size = get_parameters().length;
		return size;
	}
	
	public static Object annotatedName (Object nameParameter) {
		Object name = nameParameter;
		if (name instanceof Object[]){
			name = ((Object[])name)[0];
		}
		name = getName(name);
		return name;
	}
	
	public static Object annotatedAnnotation (Object nameParameter) {
		Object annotation = null;
		if (nameParameter instanceof Object[]){
			annotation = ((Object[])nameParameter)[1];
		}
		return annotation;
	}
	
	private static final String _classPrefix = className(Loadable.class);	// safe for renaming!
	
	public String toString() {
		String string = this.className();	
		if (string.startsWith(_classPrefix)) {
			string = string.substring(_classPrefix.length());
		}
		string = string + toString(get_parameters());
		return string;
	}

}

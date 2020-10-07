/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.operations.Proxy;

/**
 * @author MLB
 *
 *
 */
public class LoadableLocal
	extends LoadableExpression 
{
	protected LoadableLocal() {super();}
	public LoadableLocal(Object[] parameters) {super(parameters);}
	
	public Object[] loadPath(Object[] source, Context context) {
		int size = source.length;
		Object[] path = new Object[size];
		for (int i=0; i<size; i++){
			Object element = source[i];
			if (element instanceof Object[]){
				element = loadPath((Object[])element, context);
				element = makeIndexKey(element, true);
			} else if (element instanceof Loadable){
				element = ((Loadable)element).load(context);
				if (element instanceof Entailment){
					element = ((Entailment)element).getOwnIndexKey();
				}
			}		
			path[i] = element;
		}
		return path;
	}
	
	public static String makePathProxyName(Object[] path){
		// String name = "." + toString(path);
		String name = "<PROXY " + toPathString(path) + ">";
		return name;
	}
	
	public Object load(Context context) {
		Object[] path = loadPath(getParameters(), context);
		Object object = context.find(path);
		if (object == null) {
			String name = makePathProxyName(path);
			System.out.println("\nCreating " + name);	// ToDo MLB: do this right
			Proxy proxy = new Proxy(name, context, path);
			context.addMember(proxy);
			proxy.activate();
			object = proxy;
		}
		return object;
	}

}

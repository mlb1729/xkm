/*
 * Created on Oct 8, 2004
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.contexts.Context;

/**
 * @author MLB
 *
 *
 */
public class LoadableGlobal 
	extends LoadableLocal 
{
	protected LoadableGlobal() {super();}
	public LoadableGlobal(Object[] parameters) {super(parameters);}
	
	public Object load(Context context) {
		Object object = super.load(context.getGlobalContext());
		return object;
	}

}

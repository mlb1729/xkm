/*
 * Created on Jul 8, 2005
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Herd;

/**
 * @author MLB
 *
 *
 */
public class LoadableEnum 
	extends LoadableHerd 
{
	private Object[] _roster = null; 
	
	public LoadableEnum() {super();}

	public LoadableEnum(Object[] parameters) {
		super(new Object[]{parameters[0]});
		_roster = parameters;
	}

	public Object load(Context context) {
		Herd herd = ((Herd)super.load(context));
		Context herdContext = herd.getContext();
		int size = _roster.length;
		for (int i=1; i<size; i++){
			LoadableMember loadable = new LoadableMember(new Object[]{_roster[i]});
			Object object = loadable.load(herdContext);
			herd.getBasis().add(object);
		}
		herd.makeConstant();
		return herd;
	}
}

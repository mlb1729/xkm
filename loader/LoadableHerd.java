/*
 * Created on Jul 30, 2005
 *
 
 */
package loader;

import contexts.Context;
import entailments.Herd;
import operators.EntailmentOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableHerd 
	extends LoadableMember 
{
	LoadableEntailment	_loadable = null;
	
	public LoadableHerd() {super();}

	public LoadableHerd(Object[] parameters) {
		super(new Object[]{parameters[0]});
		if (parameters.length > 1){
			_loadable = ((LoadableEntailment)parameters[1]);
		}
	}
	
	public EntailmentOperator newEntailmentOperator(Object name) {
		EntailmentOperator operator = new EntailmentOperator(name, Herd.class);
		return operator;
	}

	public Object load(Context context, Object name) {
		Object object = super.load(context, name);
		Herd herd = ((Herd)object);
		herd.initLoadable(_loadable);
		return object;
	}
	
}

/*
 * Created on Sep 30, 2004
 *
 
 */
package loader;

import java.util.ArrayList;
import java.util.List;

import contexts.Context;
import expressions.SetObject;

/**
 * @author MLB
 *
 *
 */
public class LoadableSet 
	extends LoadableObject 
{
	protected LoadableSet() {super();}
	public LoadableSet(Object[] parameters) {super(parameters);}
	
	public Object load(Context context) {
		Object setObject = load(context, false);
		return setObject;
	}

	protected Object load(Context context, boolean isConstant) {
		int size = getSize();
		List operands = new ArrayList(size);
		for (int i=0; i < size; i++) {
			Object parameter = getParameter(i);
			if (parameter instanceof Loadable) {
				parameter = ((Loadable)(parameter)).load(context);
			}
			operands.add(parameter);
		}
		SetObject setObject = (isConstant ?
								new SetObject(operands) :
								new SetObject(context, operands));
		context.addMember(setObject);
		setObject.activate();
		return setObject;
	}

}

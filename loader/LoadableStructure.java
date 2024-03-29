/*
 * Created on Oct 5, 2004
 *
 
 */
package loader;

import contexts.Context;
import contexts.ContextObject;

/**
 * @author MLB
 *
 *
 */
public class LoadableStructure 
	extends LoadableEntailment 
{
	public LoadableStructure() {super();}
	public LoadableStructure(Object[] parameters) {super(parameters);}
	
	protected Context getParameterContext(Context context, Object name) {
		if (context instanceof ContextObject) {
			context = new ContextObject((ContextObject)context);
			if (getSize() > 0)  {
				// Object name = getParameter(0);
				context.setName(name);
			}
		}
		return context;
	}

}

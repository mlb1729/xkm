/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import com.resonant.xkm.api.ConstraintDescription;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.ConstraintDescriptionObject;

/**
 * @author MLB
 *
 *
 */
public class LoadableConstrainment 
	extends LoadableObject 
{
	protected LoadableConstrainment() {super();}
	public LoadableConstrainment(Object[] parameters) {super(parameters);}
	
	public Loadable processLoadable(Context context, Loadable constraint, ConstraintDescription description) {
		return constraint;
	}

	public Object load(Context context) {
		Loadable constraint = (Loadable)getParameter(0);
		ConstraintDescription description = ConstraintDescriptionObject.coerce(getParameter(1));	
		Loadable loadable = processLoadable(context, constraint, description);
		BoundedExpression expression = (BoundedExpression)(loadable.load(context));	
		context.addToStory(expression, description);
		return expression;
	}

}

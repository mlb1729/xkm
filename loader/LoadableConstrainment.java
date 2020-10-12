/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import api.ConstraintDescription;
import contexts.Context;
import expressions.BoundedExpression;
import kb.ConstraintDescriptionObject;

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

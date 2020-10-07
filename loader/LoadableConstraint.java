/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.api.ConstraintDescription;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedVariable;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableConstraint 
	extends LoadableConstrainment 
{

//	private LoadableBounds _booleanBounds = new LoadableBounds(new Object[]{Type.BOOLEAN});
	private LoadableBounds _booleanBounds = new LoadableBounds(new Object[]{Boolean.class});
	
	protected LoadableConstraint() {super();}
	public LoadableConstraint(Object[] parameters) {super(parameters);}
	
	public Loadable processLoadable(Context context, Loadable constraint, ConstraintDescription description) {
//		Object varName = "Enforce " + description.getName() + " (" + description.getDocumentation() + ")";
		Object varName = "" + description.getName();
		Loadable variable = new LoadableVariable(new Object[]{varName, _booleanBounds});
		BoundedVariable variableExpr = (BoundedVariable)(variable.load(context));
		Loadable enforced = new LoadableExpression(new Object[]{RootOperator.Implies, variable, constraint});

		Object[] varPath = variableExpr.getVariablePath();
		KBObject kb = context.getKB();
		Assertion assertion = kb.newAssertion(varPath);
		kb.addDeferredAssertion(assertion);

		return enforced;
	}

	// TrueConstraint constraint = new TrueConstraint((BoundedOperation)expression, reason);
	// context.addMember(constraint);
	// constraint.activate();
	// return constraint;


}

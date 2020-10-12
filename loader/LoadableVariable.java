/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import java.util.List;

import bounds.Bounds;
import contexts.Context;
import expressions.BoundedVariable;
import expressions.Expression;
import km.Named;
import operators.RootOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableVariable 
	extends LoadableObject
	implements Named
{
	protected LoadableVariable() {super();}
	public LoadableVariable(Object[] parameters) {super(parameters);}
		
	public Object load(Context context) {
		Object result = load(context, getParameter(0));
		return result;
	}
	
	public Object getName(){
		Object name = annotatedName(getParameter(0));
		return name;
	}
	
	public Object load(Context context, Object nameParameter) {
		Object name = annotatedName(nameParameter);
		Expression variable = context.getVariable(name);
		int size = getParameters().length;

		if (variable == null) {
			if (size < 2)
				debugError("Can't define variable - missing parameters for variable - " + this);
			
			Object parameter1 = getParameter(1);			
			LoadableObject loadableObject = (LoadableObject)parameter1;
			Object definition = loadableObject.load(context);
			if (definition instanceof Bounds) {
				BoundedVariable var = new BoundedVariable(name, (Bounds)definition);
				var.setAnnotation(annotatedAnnotation(nameParameter));
				variable = var;
			} else  {
				error("Can't define variable " + name + " with " + definition);
			}
			context.addOwnVariable(variable);
			variable.activate();
		}
		
		Object result = variable;
		
		if (size >= 3) {
			Object declaredValue = getParameter(2);			
			LoadableObject loadableObject = (LoadableObject)declaredValue;
			Context parent = context.getParent();
			if (parent == null) {
				error("No parent context in which to define value for variable " + variable);
			}
			Object value = loadableObject.load(parent);
			List operands = toList(new Object[]{variable, value});
			Expression expression = RootOperator.Equal.makeExpression(operands);
			Object reason = ((size >= 4) ?
								getParameter(3) :
								"Variable " + name + " declared = " + declaredValue);
			context.addToStory(expression, reason);
			expression.activate();
			result = expression;
		}

		return result;
	}

}

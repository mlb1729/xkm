/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import java.util.ArrayList;
import java.util.List;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.operators.Operator;

/**
 * @author MLB
 *
 *
 */
public class LoadableExpression 
	extends LoadableObject 
{
	protected LoadableExpression() {super();}
	public LoadableExpression(Object[] parameters) {super(parameters);}
	
	public Object load(Context context) {
		int size = getSize();
		Object name = getParameter(0);
		Operator operator = context.findOperator(name);
		if (operator == null) {
			debugError("Can't find operator named " + name + " in expression " + this);
		}
		List operands = new ArrayList(size - 1);
		for (int i=1; i < size; i++) {
			Object parameter = getParameter(i);
			if (parameter instanceof Loadable) {
				parameter = ((Loadable)(parameter)).load(context);
			}
			if (parameter == null)
				debugError("Can't load parameter " + i + " of " + this);
			operands.add(parameter);
		}
		Expression expression = operator.makeExpression(operands);
		context.addMember(expression);
		expression.activate();
		return expression;
	}

}

/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import contexts.Context;
import expressions.Expression;
import points.Point;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class LoadableConstant 
	extends LoadableExpression 
{
	protected LoadableConstant() {super();}
	public LoadableConstant(Object[] parameters) {super(Type.classifyTypes(parameters));}
	
	public Object load(Context context) {
		Object parameter0 = getParameter(0);
		Type type = Type.coerceType(parameter0);
		boolean notTyped = (type == null);
		if (notTyped){
			type = Type.coerceType(parameter0.getClass());
		}
		Point point = type.getFixedPoint(notTyped ? parameter0 : getParameter(1));
		Expression expression = point.getFixedExpression();
		return expression;
	}

}

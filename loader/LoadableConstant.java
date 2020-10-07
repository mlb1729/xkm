/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Type;

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

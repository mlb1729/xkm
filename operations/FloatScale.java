/*
 * Created on Mar 31, 2005
 *
 
 */
package com.resonant.xkm.operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type.FLOAT;

/**
 * @author MLB
 *
 *
 */
public class FloatScale 
	extends FloatingOperation 
{
	public FloatScale() {super();}

	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression x = (BoundedExpression)(operands.get(1));
		float aVal = FLOAT.toFloat(a.getMinIndex());
		float yMin = FLOAT.toFloat(getMinIndex());
		float yMax = FLOAT.toFloat(getMaxIndex());
		float xMin = yMin / aVal;
		float xMax = yMax / aVal;
		if (aVal < 0) {
			float temp = xMin;
			xMin = xMax;
			xMax = temp;
		}
		x.changeMinIndex(FLOAT.toIndex(xMin));
		x.changeMaxIndex(FLOAT.toIndex(xMax));
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression x = (BoundedExpression)(operands.get(1));
		float aVal = FLOAT.toFloat(a.getMinIndex());
		float xMin = FLOAT.toFloat(x.getMinIndex());
		float xMax = FLOAT.toFloat(x.getMaxIndex());
		float yMin = aVal * xMin;
		float yMax = aVal * xMax;
		if (aVal < 0) {
			float temp = yMin;
			yMin = yMax;
			yMax = temp;
		}
		changeMinIndex(FLOAT.toIndex(yMin));
		changeMaxIndex(FLOAT.toIndex(yMax));
		return;	
	}

}

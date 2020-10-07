/*
 * Created on Oct 4, 2004
 *
 
 */
package com.resonant.xkm.operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class Minimum 
	extends ArithmeticOperation 
{
	public Minimum(Type type) {super(type);}

	public void constrainOperands () {
		List operands = getOperands();
		int min = getMinIndex();
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			operand.changeMinIndex(min);
		} 
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		int min = getType().maxIndex();
		int max = min;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			int opMin = operand.getMinIndex();
			if (opMin < min) {
				min = opMin;
			}
			int opMax = operand.getMaxIndex();
			if (opMax < max) {
				max = opMax;
			}
		}	
		changeMinIndex(min);
		changeMaxIndex(max);
		return;	
	}

}

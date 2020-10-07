/*
 * Created on Mar 18, 2005
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
public class Negate 
	extends ArithmeticOperation 
{
	public Negate(Type type) {super(type);}
	
	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		operand.changeMinIndex(- getMaxIndex());
		operand.changeMaxIndex(- getMinIndex());
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		changeMinIndex(- operand.getMaxIndex());
		changeMaxIndex(- operand.getMinIndex());
		return;	
	}

}

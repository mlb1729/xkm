/*
 * Created on Mar 18, 2005
 *
 */

package operations;

import java.util.List;

import expressions.BoundedExpression;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class Conditional
	extends ArithmeticOperation 
{
	public Conditional() {super();}
	public Conditional(Type type) {super(type);}
	
	public void constrainOperands () {
		List operands = getOperands();
		int size = operands.size();
		BoundedExpression ifOperand = ((BoundedExpression)operands.get(0));
		BoundedExpression thenOperand = (BoundedExpression)((size>1) ? operands.get(1) : this);
		BoundedExpression elseOperand = (BoundedExpression)((size>2) ? operands.get(2) : this);
		int min = getMinIndex();
		int max = getMaxIndex();
		if (ifOperand.getMaxIndex() > 0) {
			if ((min > thenOperand.getMaxIndex()) || (max < thenOperand.getMinIndex())) {
				ifOperand.changeToBe(false);
			} 
		}
		if (ifOperand.getMinIndex() <= 0) {
			if ((min > elseOperand.getMaxIndex()) || (max < elseOperand.getMinIndex())) {
				ifOperand.changeToBe(true);
			} 
		}
		if (ifOperand.isKnownToBe(true)) {
			thenOperand.changeMinIndex(min);
			thenOperand.changeMaxIndex(max);
		} else if (ifOperand.isKnownToBe(false)) {
			elseOperand.changeMinIndex(min);
			elseOperand.changeMaxIndex(max);
		}
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		int size = operands.size();
		BoundedExpression ifOperand = ((BoundedExpression)operands.get(0));
		BoundedExpression thenOperand = (BoundedExpression)((size>1) ? operands.get(1) : this);
		BoundedExpression elseOperand = (BoundedExpression)((size>2) ? operands.get(2) : this);
		int thenMin = thenOperand.getMinIndex();
		int thenMax = thenOperand.getMaxIndex();
		int elseMin = elseOperand.getMinIndex();
		int elseMax = elseOperand.getMaxIndex();
		if (ifOperand.isKnownToBe(true)) {
			changeMinIndex(thenMin);
			changeMaxIndex(thenMax);
		} else if (ifOperand.isKnownToBe(false)) {
			changeMinIndex(elseMin);
			changeMaxIndex(elseMax);
		} else {
			changeMinIndex((thenMin < elseMin) ? thenMin : elseMin);
			changeMaxIndex((thenMax > elseMax) ? thenMax : elseMax);
		}
		return;	
	}
}

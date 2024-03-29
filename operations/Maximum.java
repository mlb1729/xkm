/*
 * Created on Oct 4, 2004
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
public class Maximum 
	extends ArithmeticOperation 
{
	public Maximum(Type type) {super(type);}
	
	public void constrainOperands () {
		List operands = getOperands();
		int max = getMaxIndex();
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			operand.changeMaxIndex(max);
		} 
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		int min = getType().minIndex();
		int max = min;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			int opMin = operand.getMinIndex();
			if (opMin > min) {
				min = opMin;
			}
			int opMax = operand.getMaxIndex();
			if (opMax > max) {
				max = opMax;
			}
		}	
		changeMinIndex(min);
		changeMaxIndex(max);
		return;	
	}

}

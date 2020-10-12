/*
 * Created on Mar 17, 2005
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
public class IntSum 
	extends ArithmeticOperation 
{
	public IntSum() {super(Type.INTEGER);}

	public void constrainOperands () {
		List operands = getOperands();
		int operandMin = 0;
		int operandMax = 0;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			operandMin += operand.getMinIndex();
			operandMax += operand.getMaxIndex();
		}
		int operationMin = getMinIndex();
		int operationMax = getMaxIndex();
		
		int baseMin = operationMin - operandMax;
		int baseMax = operationMax - operandMin;
		
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
// 			operand.changeMinIndex(operationMin - (operandMax - operand.getMaxIndex()));
// 			operand.changeMaxIndex(operationMax - (operandMin - operand.getMinIndex()));
 			int newMin = baseMin + operand.getMaxIndex();
			int newMax = baseMax + operand.getMinIndex();
			operand.changeMinIndex(newMin);
			operand.changeMaxIndex(newMax);
		} 
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		int min = 0;
		int max = 0;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			min += operand.getMinIndex();
			max += operand.getMaxIndex();
		}	
		changeMinIndex(min);
		changeMaxIndex(max);
		return;	
	}

}

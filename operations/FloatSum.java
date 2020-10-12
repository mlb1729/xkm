/*
 * Created on Mar 30, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type.FLOAT;

/**
 * @author MLB
 *
 *
 */
public class FloatSum 
	extends FloatingOperation 
{
	public FloatSum() {super();}
	
	public void constrainOperands () {
		List operands = getOperands();
		float operandMin = 0;
		float operandMax = 0;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			operandMin += FLOAT.toFloat(operand.getMinIndex());
			operandMax += FLOAT.toFloat(operand.getMaxIndex());
		}
		float operationMin = FLOAT.toFloat(getMinIndex());
		float operationMax = FLOAT.toFloat(getMaxIndex());
		float baseMin = operationMin - operandMax;
		float baseMax = operationMax - operandMin;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
 			operand.changeMinIndex(FLOAT.toIndex(baseMin + FLOAT.toFloat(operand.getMaxIndex())));
			operand.changeMaxIndex(FLOAT.toIndex(baseMax + FLOAT.toFloat(operand.getMinIndex())));
		} 
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		float min = 0;
		float max = 0;
		for (int i=0; i<operands.size(); i++) {
			BoundedExpression operand = ((BoundedExpression)operands.get(i));
			min += FLOAT.toFloat(operand.getMinIndex());
			max += FLOAT.toFloat(operand.getMaxIndex());
		}	
		changeMinIndex(FLOAT.toIndex(min));
		changeMaxIndex(FLOAT.toIndex(max));
		return;	
	}

}

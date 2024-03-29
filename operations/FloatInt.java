/*
 * Created on Mar 30, 2005
 *
 
 */
package operations;

import java.util.List;

import expressions.BoundedExpression;
import types.Type.FLOAT;

/**
 * @author MLB
 *
 *
 */
public class FloatInt 
	extends FloatingOperation 
{
	public FloatInt() {super();}
	
	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		operand.changeMinIndex(FLOAT.toIntIndex(getMaxIndex()));
		operand.changeMaxIndex(FLOAT.toIntIndex(getMinIndex()));
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		changeMinIndex(FLOAT.toFloatIndex(operand.getMaxIndex()));
		changeMaxIndex(FLOAT.toFloatIndex(operand.getMinIndex()));
		return;	
	}

}

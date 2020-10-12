/*
 * Created on Mar 30, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.Type.FLOAT;

/**
 * @author MLB
 *
 *
 */
public class IntFloat 
	extends ArithmeticOperation 
{
	public IntFloat() {super(Type.INTEGER);}
	
	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		operand.changeMinIndex(FLOAT.toFloatIndex(getMaxIndex()));
		operand.changeMaxIndex(FLOAT.toFloatIndex(getMinIndex()));
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		changeMinIndex(FLOAT.toIntIndex(operand.getMaxIndex()));
		changeMaxIndex(FLOAT.toIntIndex(operand.getMinIndex()));
		return;	
	}

}

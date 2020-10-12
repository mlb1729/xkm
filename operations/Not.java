/*
 * Created on Mar 18, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;

/**
 * @author MLB
 *
 *
 */
public class Not 
	extends LogicalOperation 
{
	public Not() {super();}
	public Not(Object name) {super(name);}
	
	public void constrainOperands () {
		if (isPoint()) {
			List operands = getOperands();
			BoundedExpression operand = ((BoundedExpression)operands.get(0));
			operand.changeToBe(isKnownToBe(false));
		}
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression operand = ((BoundedExpression)operands.get(0));
		if (operand.isPoint()) {
			changeToBe(operand.isKnownToBe(false));
		}
		return;	
	}

}
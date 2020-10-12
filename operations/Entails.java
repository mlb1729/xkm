/*
 * Created on Oct 11, 2004
 *
 
 */
package operations;

import java.util.List;

import expressions.BoundedExpression;
import expressions.SetObject;

/**
 * @author MLB
 *
 *
 */
public class Entails 
	// extends Implies 
	extends LogicalOperation
{
	public Entails() {super();}
	public Entails(Object name) {super(name);}
	
	public List coercedOperands (List operands) {
		List coercedOperands = super.coercedOperands(operands);
		if (coercedOperands.size() == 1) {
			Object operand0 = coercedOperands.get(0);
			if (operand0 instanceof SetObject) {
				coercedOperands = ((SetObject)operand0).getOperands();
			}		
		}
		return coercedOperands;
	}
	
	public void constrainOperands () {
//		System.out.println("\nConstraining operands for entailment " + this);
		List operands = getOperands();
		int size = operands.size();
		if (isKnownToBe(true)) {
			for (int i=0; i<size; i++) {
				BoundedExpression operand = (BoundedExpression)(operands.get(i));
//				System.out.println("    " + this + " entails " + operand);
				operand.changeToBe(true);
			}
		} 
		return;
	}

	public void constrainOperation () {
//		System.out.println("\nConstraining entailment value " + this);
		List operands = getOperands();
		int size = operands.size();
		for (int i=0; i<size; i++) {
			BoundedExpression operand = (BoundedExpression)(operands.get(i));
			if (operand.isKnownToBe(false)) {
				changeToBe(false);
				break;
			}
		}
		return;
	}

}

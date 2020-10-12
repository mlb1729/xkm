/*
 * Created on Mar 17, 2005
 *
 
 */
package functions;

import java.util.List;

import domains.Domain;
import entailments.Closure;
import entailments.Entailment;
import entailments.Herd;
import expressions.BasisListener;
import expressions.BoundedExpression;
import expressions.SetObject;
import types.Type;

/**
 * @author MLB
 *
 *
 */

//public class Quantity
//	extends SetReduction
//{
//	public Quantity() {
//		super();
//		initOperatorArg(RootOperator.Sum);
//	}
//	
//}


public class Quantity
	extends ArithmeticFunction
	implements BasisListener
{
	SetObject	_setObject = null;
	
	public Quantity() {super(Type.INTEGER);}

//	public boolean resetsOnBasisChange(){
//		return true;
//	}
	
	public List coercedOperands (List operands) {
		if (operands.size() == 1) {
			Object operand0 = operands.get(0);
			if (operand0 instanceof SetObject) {
				SetObject set = (SetObject)operand0;
				_setObject = set;
				set.addBasisListener(this);
				operands = set.getMemberBooleans();
			} else if (operand0 instanceof Herd) {
				Herd herd = (Herd)operand0;
				herd.addBasisListener(this);
				operands = herd.getBasis();
			}
		}
		List coercedOperands = super.coercedOperands(operands);
		return coercedOperands;
	}
	
	public void newBasisElement(Domain newOperand) {
		if (_setObject != null) {
			if ((newOperand instanceof Entailment) &&
					!(newOperand instanceof Closure)){
				Object name = ((Entailment)newOperand).getName();
				newOperand = _setObject.getMember(name);
				if (newOperand != null)
					addNewOperand(newOperand);
			}
		} else {
			addNewOperand(newOperand);
		}
		return;
	}
	
	// from IntSum
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

/*
 * Created on Mar 17, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.expressions.AttributeOperation;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.expressions.BoundedOperation;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class AttrIntSum 
	extends AttributeOperation 
{
	public AttrIntSum() {super(Type.INTEGER);}
	
	// derived from IntSum

	public void constrainOperands () {
		// List operands = getOperands();
		List operands = getSource().getBasis();
		int operandMin = 0;
		int operandMax = 0;
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){		
					boolean isTrue = booleanMember.isKnownToBe(true);
					int opMin = operand.getMinIndex();
					int opMax = operand.getMaxIndex();
					if (isTrue || (opMin < 0)){
						operandMin += opMin;
					}
					if (isTrue || (opMax > 0)){
						operandMax += opMax;						
					}
				}
			}
		}
		int operationMin = getMinIndex();
		int operationMax = getMaxIndex();
		
		int baseMin = operationMin - operandMax;
		int baseMax = operationMax - operandMin;
		
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){
					int opMin = operand.getMinIndex();
		 			int opMax = operand.getMaxIndex();
					if (booleanMember.isKnownToBe(true)){
						int newMin = baseMin + opMax;
						int newMax = baseMax + opMin;
						operand.changeMinIndex(newMin);
						operand.changeMaxIndex(newMax);
					} else {
						
					}
				}
			}
		} 
		return;	
	}

	public void constrainOperation () {
		// List operands = getOperands();
		List operands = getSource().getBasis();
		int min = 0;
		int max = 0;
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){		
					boolean isTrue = booleanMember.isKnownToBe(true);
					int opMin = operand.getMinIndex();
					int opMax = operand.getMaxIndex();
					if (isTrue || (opMin < 0)){
						min += opMin;
					}
					if (isTrue || (opMax > 0)){
						max += opMax;						
					}
				}	
			}
		}
		changeMinIndex(min);
		changeMaxIndex(max);
		return;	
	}

}

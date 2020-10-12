/*
 * Created on Aug 31, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.expressions.AttributeOperation;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.expressions.BoundedOperation;
import com.resonant.xkm.types.Type;


public class AttributeMaximum 
	extends AttributeOperation 
{
	public AttributeMaximum() {super();}
	public AttributeMaximum(Type type) {super(type);}
	
	// derived from Minimum
	
	public void constrainOperands () {
		// List operands = getOperands();
		List operands = getSource().getBasis();
		int max = getMaxIndex();
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){
					if (booleanMember.isKnownToBe(true)){
						operand.changeMaxIndex(max);						
					} else {
						int opMin = operand.getMinIndex();
						if (max < opMin){
							booleanMember.changeToBe(false);
						}
					}
				}
			}
		} 
		return;	
	}

	public void constrainOperation () {
		// List operands = getOperands();
		List operands = getSource().getBasis();
		int max = getType().minIndex();
		int min = max;
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){
					int opMax = operand.getMaxIndex();
					if (opMax > max) {
						max = opMax;
					}
					if (booleanMember.isKnownToBe(true)){
						int opMin = operand.getMinIndex();
						if (opMin > min) {
							min = opMin;
						}
					}
				}
			}
		}	
		changeMaxIndex(max);
		changeMinIndex(min);
		return;	
	}

}

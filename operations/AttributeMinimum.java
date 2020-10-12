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


public class AttributeMinimum 
	extends AttributeOperation 
{
	public AttributeMinimum() {super();}
	public AttributeMinimum(Type type) {super(type);}
	
	// derived from Minimum
	
	public void constrainOperands () {
		// List operands = getOperands();
		List operands = getSource().getBasis();
		int min = getMinIndex();
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){
					if (booleanMember.isKnownToBe(true)){
						operand.changeMinIndex(min);						
					} else {
						int opMax = operand.getMaxIndex();
						if (min > opMax){
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
		int min = getType().maxIndex();
		int max = min;
		for (int i=0; i<operands.size(); i++) {
			Entailment entailment = ((Entailment)operands.get(i));
			BoundedOperation booleanMember = getBooleanMember(entailment);
			if (!(booleanMember.isKnownToBe(false))){
				BoundedExpression operand = (BoundedExpression)(extractAttribute(entailment));
				if (operand != null){
					int opMin = operand.getMinIndex();
					if (opMin < min) {
						min = opMin;
					}
					if (booleanMember.isKnownToBe(true)){
						int opMax = operand.getMaxIndex();
						if (opMax < max) {
							max = opMax;
						}
					}
				}
			}
		}	
		changeMinIndex(min);
		changeMaxIndex(max);
		return;	
	}

}

/*
 * Created on Sep 29, 2004
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
public class OrderRelation 
	extends LogicalOperation 
{
	private int		_aOperandIndex;
	private int		_bOperandIndex;
	private boolean	_isLoose;
	private boolean	_isTight;
	
	protected OrderRelation () {super();}
	
	protected OrderRelation (boolean isIncreasing, boolean isLoose) {
		this();
		_aOperandIndex = (isIncreasing ? 0 : 1);
		_bOperandIndex = (1 - _aOperandIndex);
		_isLoose = isLoose;
		_isTight = (! _isLoose);
	}

	public void constrainOperands () {
		List operands = getOperands();
		// if (operands.size() > 1) {
			BoundedExpression a = (BoundedExpression)(operands.get(_aOperandIndex));
			BoundedExpression b = (BoundedExpression)(operands.get(_bOperandIndex));		
			if (isKnownToBe(_isLoose)) {
				changeMaxIndex(a, b.getMaxIndex());
				changeMinIndex(b, a.getMinIndex());	
			} else if (isKnownToBe(_isTight)) {
				changeMinIndex(a, b.getMinIndex() + 1);
				changeMaxIndex(b, a.getMaxIndex() - 1);
			}
		// }
		return;
	}

	public void constrainOperation () {
		List operands = getOperands();
		// if (operands.size() > 1) {
			BoundedExpression a = (BoundedExpression)(operands.get(_aOperandIndex));
			BoundedExpression b = (BoundedExpression)(operands.get(_bOperandIndex));		
			if (a.getMaxIndex() <= b.getMinIndex()) {
				changeToBe(_isLoose);
			}		
			if (a.getMinIndex() > b.getMaxIndex()) {
				changeToBe(_isTight);
			}
		// }
		return;
	}

}

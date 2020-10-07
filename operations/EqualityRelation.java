/*
 * Created on Sep 29, 2004
 *
 
 */
package com.resonant.xkm.operations;

import java.util.List;

import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.expressions.BoundedExpression;

public class EqualityRelation 
	extends LogicalOperation 
{
	private static final Contradiction _equalContradiction = new Contradiction("Operands can't be equal");
	private static final Contradiction _unequalContradiction = new Contradiction("Operands must be equal"); 
	
	private boolean _isEqual;
	private boolean _unEqual;
	
	protected EqualityRelation() {super();}

	protected EqualityRelation(boolean isEqual) 
	{
		this();
		_isEqual = isEqual;
		_unEqual = !isEqual;
	}

	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression b = (BoundedExpression)(operands.get(1));
		int aMin = a.getMinIndex();
		int bMin = b.getMinIndex();
		int aMax = a.getMaxIndex();
		int bMax = b.getMaxIndex();
		if (isKnownToBe(_isEqual)) {
			if (aMin > bMin) {
				changeMinIndex(b, aMin);		
			} else  {
				changeMinIndex(a, bMin);		
			}
			if (aMax < bMax) {
				changeMaxIndex(b, aMax);		
			} else  {
				changeMaxIndex(a, bMax);		
			}
		} else if (isKnownToBe(_unEqual)) {
			boolean aIsPoint = (aMin == aMax);
			boolean bIsPoint = (bMin == bMax);
			boolean minEq = (aMin == bMin);
			boolean maxEq = (aMax == bMax);
			boolean isEqual = (aIsPoint && bIsPoint && minEq);
			if (isEqual) {
				signal(_unEqual ? _equalContradiction : _unequalContradiction);
			}
			if (aIsPoint) {
				if(minEq) {
					b.changeMinIndex(aMin + 1);
				} else if (maxEq) {
					b.changeMaxIndex(aMax - 1);
				}
			} else if (bIsPoint) {
				if (minEq) {
					a.changeMinIndex(bMin + 1);
				} else if (maxEq) {
					a.changeMaxIndex(bMax - 1);
				}
			}
		}				
		return;
	}
	
	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression b = (BoundedExpression)(operands.get(1));
		int aMin = a.getMinIndex();
		int bMin = b.getMinIndex();
		int aMax = a.getMaxIndex();
		int bMax = b.getMaxIndex();
		boolean aIsPoint = (aMin == aMax);
		boolean bIsPoint = (bMin == bMax);
		boolean minEq = (aMin == bMin);
		boolean maxEq = (aMax == bMax);
		boolean isEqual = (aIsPoint && bIsPoint && minEq);
		if (isEqual) {
			changeToBe(_isEqual);
		}		
		if ((aMin > bMax) || (bMin > aMax)) {
			changeToBe(_unEqual);
		}
		return;
	}
	
}

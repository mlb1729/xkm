/*
 * Created on Oct 5, 2004
 *
 
 */
package com.resonant.xkm.operations;

import java.util.List;

import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.expressions.SetObject;

/**
 * @author MLB
 *
 *
 */
public class NaryBoolean 
	extends LogicalOperation 
{
	private static final Contradiction _trueContradiction = new Contradiction("All operands can't be true");
	private static final Contradiction _falseContradiction = new Contradiction("All operands can't be false");
	
	private boolean		_isWhenAllOperandsPass;
	private boolean		_isWhenAnyOperandsFail;
	private boolean 	_passWhenAllOperandsAre;
	private boolean 	_failWhenAnyOperandsAre;

	protected NaryBoolean() {super();}
	
	protected NaryBoolean(Object name) {super(name);}
	
	public NaryBoolean (boolean isWhenAllOperandsPass, boolean passWhenAllOperandsAre) {
		this();
		initBooleans(isWhenAllOperandsPass, passWhenAllOperandsAre);
	}
		
	public void initBooleans(boolean isWhenAllOperandsPass, boolean passWhenAllOperandsAre) {
		_isWhenAllOperandsPass	= isWhenAllOperandsPass;
		_isWhenAnyOperandsFail	= !isWhenAllOperandsPass;
		_passWhenAllOperandsAre = passWhenAllOperandsAre;
		_failWhenAnyOperandsAre = !passWhenAllOperandsAre;
		return;
	}
	
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
		List operands = getOperands();
		int size = operands.size();
		if (isKnownToBe(_isWhenAllOperandsPass)) {	// make all operands conform
			for (int i=0; i<size; i++) {
				((BoundedExpression)(operands.get(i))).changeToBe(_passWhenAllOperandsAre);
			}
		} else if (isKnownToBe(_isWhenAnyOperandsFail)) {
			boolean allPass = true;	// true at end iff there's a contradiction
			BoundedExpression lastChildStanding = null;	// setiff there's exactly one unknown operand left 
			for (int i=0; i<size; i++) {
				BoundedExpression operand = ((BoundedExpression)(operands.get(i)));
				if (!operand.isKnownToBe(_passWhenAllOperandsAre)) {
					allPass = false;
					if (operand.isKnownToBe(_failWhenAnyOperandsAre) ||	// already have driving operand, OR...
							(lastChildStanding != null)) {					// ...have more than one unknown operand
						lastChildStanding = null;
						break;
					} else {
						lastChildStanding = operand;	// record unknown operand in case it will have to be forced
					}
				}
			}
			if (allPass) {
				signal(_passWhenAllOperandsAre ? _trueContradiction : _falseContradiction);
			} else if (lastChildStanding != null) {
				// "When you've eliminated the impossible, 
				//   whatever remains, however improbable,
				//     must be the truth!"  --Sherlock Holmes
				lastChildStanding.changeToBe(_failWhenAnyOperandsAre);
			}
		}				
		return;
	}

	public void constrainOperation () {
		List operands = getOperands();
		int size = operands.size();
		boolean allPass = true;
		for (int i=0; i<size; i++) {
			BoundedExpression operand = ((BoundedExpression)(operands.get(i)));
			if (!operand.isKnownToBe(_passWhenAllOperandsAre)) {
				allPass = false;
				if (operand.isKnownToBe(_failWhenAnyOperandsAre)) {
					changeToBe(_isWhenAnyOperandsFail);
					break;
				}
			}
		}
		if (allPass) {
			changeToBe(_isWhenAllOperandsPass);
		}
		return;
	}
}

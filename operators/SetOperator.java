/*
 * Created on Oct 1, 2004
 *
 
 */
package com.resonant.xkm.operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.entailments.Closure;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.expressions.SetObject;


public class SetOperator 
	extends EntailmentOperator
{
	public SetOperator(){this("");}
	
	public SetOperator(Object name) {
		this(name, SetObject.class);
	}
	
	public SetOperator(Object name, Class objectClass) {
		super(name, objectClass);
	}
	
	public List coercedArguments (List operands) {
		List coercedOperands = new ArrayList(new HashSet(operands));
		return coercedOperands;
	}

	public String toStringOperandsOpen() {
		return "{";
	}
	
	public String toStringOperands(List operands) {
		String string = defaultToStringOperands(operands);
		return string;
	}

	public String toStringOperands(Iterator iterator) {
		String string = defaultToStringOperands(iterator);
		return string;
	}

	public String toStringOperand(Expression operand) {
		String string = "";
		if (!(operand instanceof Closure)){
			String operandString = operand.toString();
			string = operandString + ":" + operand.toStringValue();
		} else {
			string += "";
		}
		return string;
	}
	
	public String toStringOperandsClose() {
		return "}";
	}
	
}
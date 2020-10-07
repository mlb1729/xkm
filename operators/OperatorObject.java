/*
 * Created on Aug 23, 2004
 *
 
 */
package com.resonant.xkm.operators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class OperatorObject 
	extends KMObject
	implements Operator
{
	private Object	_name;
	private Class	_expressionClass;
	
	private Object get_name() {return _name;}
	private Class get_expressionClass() {return _expressionClass;}

	public OperatorObject () {super();}
	
	public OperatorObject (Object name, Class expressionClass) {
		this();
		_name = getName(name);
		_expressionClass = expressionClass;
	}
	
	public Object getName() {
		return get_name();
	}
	
	public Class getExpressionClass () {
		return get_expressionClass();
	}
	
	/*
	public Domain coerceOperand (Domain operand) {
		Domain coercedOperand = operand;
		if (!isOperandClass(coercedOperand)) {
			coercedOperand = wrapOperand(operand);
		}
		if (!isOperandClass(coercedOperand)) {
			error("Operator " + this + " was not able to coerce " + operand + 
					" into an instance of " + get_operandClass());
		}
		return coercedOperand;
	}
	*/
	
	public static List makeCoercedArguments (List operands) {
		List coercedArguments = new ArrayList(operands);
		return coercedArguments;
	}
	
	public List coercedArguments (List operands) {
		List coercedArguments = makeCoercedArguments(operands);
		return coercedArguments;
	}
	
	public Expression newExpression() {
		Expression expression = (Expression)newObject(getExpressionClass());
		return expression;
	}
	
	public Expression makeExpression (List operands) {
		Expression expression = newExpression();
		expression.initOperator(this);
		expression.initOperands(operands);		
		return expression;
	}
	
	public Expression makeExpression (List operands, Object reason) {
		Expression expression = makeExpression(operands);
		expression.initReason(reason);
		return expression;
	}
	
	public String toStringOperandsOpen() {
		return "(";
	}
	
	public String toStringOperandsClose() {
		return ")";
	}
	
	public String toStringOperand(Expression operand) {
		String string = operand.toString();
		return string;
	}

	public String defaultToStringOperands(List operands) {
		String string = defaultToStringOperands(iterator(operands));
		return string;
	}

	public String defaultToStringOperands(Iterator iterator) {
		String string = toStringOperandsOpen();
		boolean first = true;
		while (iterator.hasNext()) {
			if (first) {
				first = false;
			} else {
				string = string + ", ";
			}
			Object operand = iterator.next();
			string = string + ((operand instanceof Expression) ? 
								toStringOperand((Expression)operand) : 
								toString(operand));
		}
		string = string + toStringOperandsClose();
		return string;
	}

	public String toStringOperands(List operands) {
		String string = defaultToStringOperands(operands);
		return string;
	}

	public String toStringOperands(Iterator iterator) {
		String string = defaultToStringOperands(iterator);
		return string;
	}

}

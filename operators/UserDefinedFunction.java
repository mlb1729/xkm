/*
 * Created on Jul 20, 2005
 *
 
 */
package com.resonant.xkm.operators;

import com.resonant.xkm.api.UserDefinedFunctionKernel;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.expressions.BoundedOperation;
import com.resonant.xkm.expressions.Expression;

/**
 * @author MLB
 *
 *
 */
public class UserDefinedFunction 
	extends RootOperator 
{
	private UserDefinedFunctionKernel	_kernel;

	private UserDefinedFunctionKernel get_kernel() {return _kernel;}

	public UserDefinedFunction() {super();}
	
	public UserDefinedFunction(String name, UserDefinedFunctionKernel kernel) {
		super(name, UserExpressionClass.class);
		_kernel = kernel;		
	}
	
	public UserDefinedFunctionKernel getKernel() {
		return get_kernel();
	}
	
	public Class getExpressionClass() {
		return UserExpressionClass.class;
	}
	
	public Expression newExpression () {
		Expression newExpression = new UserExpressionClass();
		return newExpression;
	}
	
	class UserExpressionClass 
		extends BoundedOperation
	{
		public UserExpressionClass() {
			this(getKernel().getReturnValueClass());
		}
		
		public UserExpressionClass(Class expressionClass) {
			super(expressionClass);
		}
		
		public void constrainOperation() {
			Object[] operands = getOperands().toArray();
			boolean allPoints = true;
			for (int i=0; i<operands.length; i++) {
				BoundedExpression operand = ((BoundedExpression) operands[i]);
				Object value = operand.getPointObject();
				if (value == null) {
					allPoints = false;
					break;
				} else {
					operands[i] = value;
				}
			}
			if (allPoints) {
				Object result = null;
				try {
					result = getKernel().apply(this.getKB(), operands);
				} catch (Error e) {
					// ToDo MLB: whatever
				}
				if (result != null) {
//					Type.classType(getKernel().getReturnValueClass()) --> getType();
					changeToBe(getType().getIndex(result));
				}
			}
			return;
		}
	}

}

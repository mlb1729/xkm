/*
 * Created on Oct 1, 2004
 *
 
 */
package com.resonant.xkm.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.contexts.Context;


public class VariableOperator 
	extends OperatorObject
{
	private Context 	_context = null;
	
	private Context get_context() {return _context;}
	private void set_context(Context context) {_context = context;}
	
	public VariableOperator() {super();}
	public VariableOperator(Object name, Class expressionClass) {
		super(name, expressionClass);
	}
	
	public List coercedArguments (List operands) {
		List coercedOperands = Collections.EMPTY_LIST;
		return coercedOperands;
	}
	
	public String toStringOperands(List operands) {
		return "";
	}
	
	public String toStringOperands(Iterator iterator) {
		return "";
	}
	
	public Context getContext () {
		Context context = get_context();
		return context;
	}
	
	public void initContext (Context context) {
		Context oldContext = get_context();
		 if (oldContext == null) {
		 	set_context(context);
		 } else {
		 	error("Can't reinitialize " + this + "context from " + oldContext + " to " + context);
		 }
		return;
	}

	public Object[] getPathNames() {
		List trail = new ArrayList();
		Context context = getContext();
		if (context != null) {
			context.getVariablePath(trail);
		}
		trail.add(this);
		Object[] path = coerceNames(trail.toArray());
		return path;
	}
	
}
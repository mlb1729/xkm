/*
 * Created on Sep 1, 2004
 *
 
 */
package com.resonant.xkm.constraints;

import com.resonant.xkm.kb.KBMemberObject;

/**
 * @author MLB
 *
 *
 */
public class ConstraintObject 
	extends KBMemberObject 
	implements Constraint
{
//	public static final String NO_REASON = "(No Reason)";
//
//	private Expression	_expression;
//	private Object		_reason;
//	
//	private Expression get_expression() {return _expression;}
//	private Object get_reason() {return _reason;}
//	
//	public ConstraintObject(Expression expression, Object reason) {
//		super();
//		_expression = expression;
//		_reason = reason;
//		getExpression().addDomainListener(this);
//	}
//	
//	public ConstraintObject(Expression expression) {
//		this(expression, NO_REASON);
//	}
//	
//	public Expression getExpression () {
//		return get_expression();
//	}
//	
//	public Object getReason () {
//		return get_reason();
//	}
//	
//	public boolean activate () {
//		boolean activated = super.activate();
//		domainChanged(null);
//		return activated;
//	}
//	
//	protected void constrain () {
//		return;
//	}
//	
//	public boolean retractThis (Set restoreSet) {
//		boolean suspended = super.retractThis(restoreSet);
//		if (suspended) {
//			restoreSet.add(this);
//			getExpression().retractSupport(this, restoreSet);
//		}
//		return suspended;
//	}
//
//	public void domainChanged (Domain changedDomain) {
//		if (isActive()) {
//			constrain();
//		}
//		return;
//	}
//
//	public String toStringBody () {
//		String string = "|" + getExpression() + ": " + getReason() + "|";
//		return string;
//	}
	
}

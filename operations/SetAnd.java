/*
 * Created on Jun 27, 2005
 *
 
 */
package operations;

import java.util.Iterator;
import java.util.List;

import contexts.Context;
import entailments.Herd;
import expressions.Expression;
import expressions.SetObject;
import expressions.SetOperation;
import kb.KBObject;

/**
 * @author MLB
 *
 *
 */
public abstract class SetAnd 
	extends And 
{
	public SetAnd() {super();}
	public SetAnd(Object name) {super(name);}
	
	protected SetObject coercedSet(Object object) {
		if (object instanceof SetOperation) {
			object = ((SetOperation)object).getSet();
		}
		if (object instanceof Herd) {
			object = ((Herd)object).getSetObject();
		}
		SetObject set = ((SetObject)object);
		return set;
	}
	
	
	protected void initOperands() {
		List operands = getOperands();
		List coercedOperands = coercedOperands(operands);
		operands.clear();
		operands.addAll(coercedOperands);
		return;
	}
	
	 public void setKB(KBObject kb) {
	 	super.setKB(kb);
		if (kb != null) {
			Context context = kb.getContext();
			Iterator it = getOperands().iterator();
			while (it.hasNext()) {
				Expression operand = ((Expression)(it.next()));
				context.addMember(operand);
			}
		}
	 	return;
	 }
	 
	 public boolean activate() {
	 	boolean isActivated = super.activate();
		Iterator it = getOperands().iterator();
		while (it.hasNext()) {
			Expression operand = ((Expression)(it.next()));
			operand.activate();
		}
	 	return isActivated;
	 }

	 public boolean suspend() {
	 	boolean isSuspended = super.suspend();
		Iterator it = getOperands().iterator();
		while (it.hasNext()) {
			Expression operand = ((Expression)(it.next()));
			operand.suspend();
		}
	 	return isSuspended;
	 }
}

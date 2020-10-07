/*
 * Created on Sep 21, 2004
 *
 
 */
package com.resonant.xkm.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.resonant.xkm.api.ConstraintDescription;
import com.resonant.xkm.changes.Change;
import com.resonant.xkm.contexts.ContextObject;
import com.resonant.xkm.domains.Domain;
import com.resonant.xkm.domains.DomainObject;
import com.resonant.xkm.entailments.Closure;
import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.exceptions.UnwindingException;
import com.resonant.xkm.kb.ConstraintDescriptionObject;
import com.resonant.xkm.kb.KBMember;
import com.resonant.xkm.kb.KBMemberObject;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.km.Named;
import com.resonant.xkm.loader.LoadableEntailment;
import com.resonant.xkm.operators.Operator;
import com.resonant.xkm.operators.OperatorObject;
import com.resonant.xkm.operators.VariableOperator;

/**
 * @author MLB
 *
 *
 */
public class Operation 
	extends DomainObject
	implements Expression, Named
{
	private static int _INITIAL_LIST_SIZE = 2;
	
	private Operator	_operator		= null;
	private List		_arguments 		= null;
	private List		_operands 		= null;
	private Object		_reason			= null;
	private List		_basis			= null;
	private List		_basisListeners	= null;
	
	private Operator get_operator() {return _operator;}
	private void set_operator(Operator operator) {_operator = operator;}
	private List get_arguments() {return _arguments;}
	private void set_arguments(List arguments) {_arguments = arguments;}
	private List get_operands() {return _operands;}
	private void set_operands(List operands) {_operands = operands;}
	private Object get_reason() {return _reason;}
	private void set_reason(Object reason) {_reason = reason;}
	private List get_basis() {return _basis;}
	private void set_basis(List basis) {_basis = basis;}
	private List get_basisListeners() {return _basisListeners;}
	private void set_basisListeners(List listeners) {_basisListeners = listeners;}
	
	protected Operation() {super();}
	
	public Operation (Object name) {
		this();
		name = getName(name);
		initOperator(new VariableOperator(name, this.getClass()));		
	}
	
	public Operator getOperator () {
		Operator operator = get_operator();
		return operator;
	}
	
	public void initOperator (Operator operator) {
		set_operator(operator);
		return;
	}
	
	public boolean hasOperands() {
		boolean hasOperands = ((get_operands() != null) &&
								(get_operands().size() > 0));
		return hasOperands;
	}
	
	public List getOperands () {
		List operands = get_operands();
		if (operands == null) {
			operands = new ArrayList(_INITIAL_LIST_SIZE);
			initOperands(operands);
			operands = get_operands();
		}
		return operands;
	}

	protected void addNewOperand(Domain newOperand) {
		boolean wasActive = isActive();
		if (wasActive) {
//		 	retract();
		}
		List operands = getOperands();
		operands.add(newOperand);
		newOperand.addDomainListener(this);
		if (wasActive) {
//		 	activate();
		}
		return;
	}
	
	public List coercedOperands (List operands) {
		List coercedOperands = operands;
		Operator operator = getOperator();
//		if (operator == null) {
//			// error("Expression operator is uninitialized!");
//			coercedOperands = OperatorObject.makeCoercedArguments(operands);
//		} else {
			List arguments = get_arguments();
			if (arguments == null) {
				set_arguments(operands);
				Iterator it = operands.iterator();
				while (it.hasNext()) {
					Object operand = it.next();
					Domain domain = ((Domain)operand);
					domain.addDomainListener(this);
				}
			} else {
				error("Can't reinitialize aruments of " + this + 
						" from " + arguments + " to " + operands);
			}
			coercedOperands = ((operator == null) ?
					OperatorObject.makeCoercedArguments(operands) :
					operator.coercedArguments(operands));
			Iterator it = coercedOperands.iterator();
			while (it.hasNext()) {
				Object operand = it.next();
				Domain domain = ((Domain)operand);
				domain.addDomainListener(this);
			}
//		}
		return coercedOperands;
	}

	public void initOperands (List operands) {
		List oldOperands = get_operands();
		if (oldOperands == null) {
			operands = coercedOperands(operands);
			set_operands(operands);
		} else {
			error("Can't reinitialize operands of " + this + 
					" from " + oldOperands + " to " + operands);
		}
		return;
	}
	
	public Object getReason() {
		return get_reason();
	}
	
	public void initReason (Object reason) {
		ConstraintDescription description = ConstraintDescriptionObject.coerce(reason);
		Object oldReason = get_reason();
		if ((oldReason != null) && (description != oldReason)) {
			error("Can't reinitialize reason of " + this + " from " + oldReason + " to " + description);
		}
		// System.out.println(description.getName() + " : " + description.getDocumentation());
		set_reason(description);	
	}
	
	public List getBasis() {
		List basis = get_basis();
		if (basis == null) {
			basis = new ArrayList(_INITIAL_LIST_SIZE);
			set_basis(basis);
		}
		return basis;
	}

	public boolean addBasisListener(BasisListener listener) {
		List listeners = get_basisListeners();
		if (listeners == null) {
			listeners = new ArrayList(_INITIAL_LIST_SIZE);
			set_basisListeners(listeners);
		}
		boolean isAdded = listeners.add(listener);
		return isAdded;
	}
	
	public void addNewBasisElement(Domain newOperand) {
		Set restoreSet = basisReset(null);
		getBasis().add(newOperand);
		List basisListeners = get_basisListeners();
		if (basisListeners != null) {
			List listeners = new ArrayList(basisListeners);
			Iterator listenerIterator = listeners.iterator();
			while (listenerIterator.hasNext()) {
				BasisListener listener = (BasisListener)(listenerIterator.next());
				listener.newBasisElement(newOperand);
			}
		}
		basisRestore(restoreSet);
	}

	public void constrain (Domain changedDomain) {
		if (changedDomain != this) 
		{
			constrainOperation();
		}
		if (!isActivating()) 
		{
			constrainOperands();
		}
		return;
	}
	
	public void constrainOperation () {
		return;	
	}
	
	public void constrainOperands () {
		return;	
	}
	
	public void domainChanged (Domain changedDomain) {
		super.domainChanged(changedDomain);
		if (isActive()) {
			KBObject kb = getKB();
			Change change = kb.lastChange();
			try {
				constrain(changedDomain);
			} catch (UnwindingException ue) {
				kb.undoTo(change);
				signal(ue);
			}
		}
		return;
	}
		
	public void onChanged () {
		domainChanged(this);
		super.onChanged();
		return;
	}

	public boolean activate () {
		boolean isActivating = super.activate();
		if (isActivating) 
		{	
			constrainOperation();
		}
		return isActivating;
	}
	
	public void onActivation () {
		if (hasOperands()) {
			Iterator iterator = getOperands().iterator();
			while (iterator.hasNext()) {
				KBMember operand = (KBMember) (iterator.next());
				operand.activate();
			}
		}
		domainChanged(null);
//		onChanged()
		return;
	}
	
	public boolean retractThis (Set restoreSet) {
		boolean wasActive = super.retractThis(restoreSet);
		if (wasActive) {
			Iterator iterator = getOperands().iterator();
			while (iterator.hasNext()) {
				Domain operand = (Domain) (iterator.next());
				operand.retractSupport(this, restoreSet);
			}
		}
		return wasActive;
	}
	
	public void retractSupport (Object object, Set restoreSet) {
		super.retractSupport(object, restoreSet);
		if (hasOperands() && getOperands().contains(object)) {
			retractThis(restoreSet);
		}
		return;
	}
	
	public void accumulateSupport(Set support, Set visited) {
		super.accumulateSupport(support, visited);
		if (hasOperands()) {
			Iterator iterator = getOperands().iterator();
			while (iterator.hasNext()) {
				Domain operand = (Domain) (iterator.next());
				operand.gatherSupport(support, visited);
			}
		}
		return;
	}
	
	public void restore(Set restoreSet, boolean includeThis) {
		if (includeThis) {
			activate();
		}
		constrainOperands();
		constrainOperation();
		super.restore(restoreSet, includeThis);
		return;
	}
	
	public Set onBasisReset(Set restoreSet) {
		restoreSet = super.onBasisReset(restoreSet);
		if (restoreSet != null) {
			if (hasOperands()) {
				Iterator iterator = getOperands().iterator();
				while (iterator.hasNext()) {
					KBMemberObject operand = (KBMemberObject) (iterator.next());
					operand.basisReset(restoreSet);
				}
			}
		}
		return restoreSet;
	}
	
//	public Set basisReset(Set restoreSet) {
//		restoreSet = super.basisReset(restoreSet);
//		return restoreSet;
//	}

	public boolean basisRestore(Set restoreSet) {
		boolean contains = super.basisRestore(restoreSet);;
		if (contains) {
			if (hasOperands()) {
				Iterator iterator = getOperands().iterator();
				while (iterator.hasNext()) {
					KBMemberObject operand = (KBMemberObject) (iterator.next());
					operand.basisRestore(restoreSet);
				}
			}
	// 		if (isActive()) {
				constrainOperands();
				constrainOperation();
	//		}
		}
		return contains;
	}
	
	public Object getName () {
		Operator operator = getOperator();
		Object name = ((operator == null) ? 
						((Object)this).toString() :
						getName(operator)); 
		return name;
	}
	
	public String toStringValue () {
		String string = super.toStringBody();
		return string;
	}

	public String toStringBody () {
		String string = toStringBodyLHS(); 
		if (string == "") {
			string = toStringValue();
		}
		return string;
	}
	
	public String toStringOperatorName () {
		String string = "";
		Operator operator = getOperator();
		if (operator != null) {
			string = toString(operator.getName());
		} else {
			string = toStringValue();
		}
		return string;
	}

	public String toStringOperatorOperands () {
		String string = toStringOperatorOperands(get_operands());
		return string;
	}

	public String toStringOperatorOperands (List operands) {
		String string = toStringOperatorOperands(iterator(operands));
		return string;
	}

	public String toStringOperatorOperands (Iterator iterator) {
		String string = "";
		Operator operator = getOperator();
		if (operator != null) {
			string = operator.toStringOperands(iterator);
			// string = "Poop";
		}
		return string;
	}

	public String toStringBodyLHS () {
		String string = toStringOperatorName() + toStringOperatorOperands();
		return string;
	}
	
	public boolean changeMinIndex(BoundedExpression expression, int index) {
		return expression.changeMinIndex(index, this);
	}
	
	public boolean changeMaxIndex(BoundedExpression expression, int index) {
		return expression.changeMaxIndex(index, this);
	}
	
	public boolean changeToBe(BoundedExpression expression, boolean bool) {
		return expression.changeToBe(bool, this);
	}

	public Closure addClosure(ContextObject outerEnvironmentContext,
								Object closureName, 
								Object structureVariableName, 
								Entailment structure, 
								LoadableEntailment loadable) 
	{
		List operands = new ArrayList(_INITIAL_LIST_SIZE);
		Closure closure = addClosure(operands,
										outerEnvironmentContext,
										closureName, 
										structureVariableName, 
										structure, 
										loadable);
		return closure;
	}
	
	public Closure addClosure(List closureOperands,
								ContextObject outerEnvironmentContext,
								Object closureName, 
								Object structureVariableName, 
								Entailment structure, 
								LoadableEntailment loadable) 
	{
		Closure closure = Closure.makeClosure(closureOperands, 
												outerEnvironmentContext, 
												closureName,
												structureVariableName,
												structure,
												loadable);
		addNewOperand(closure);
		closure.activate();
		return closure;
	}

	public Object[] getConstraintPath(){
		Object[] path = null;
		Object reason = getReason();
		if (reason != null){
			ContextObject localContext = getLocalContext();
			List trail = new ArrayList(_INITIAL_LIST_SIZE);
			localContext.getVariablePath(trail);
			trail.add(getName(reason));
			path = coerceNames(trail.toArray());
		}
		return path;
	}
	
}

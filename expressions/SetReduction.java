/*
 * Created on Sep 1, 2005
 *
 
 */
package expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import contexts.Context;
import domains.Domain;
import entailments.Closure;
import entailments.Entailment;
import entailments.Herd;
import kb.KBObject;
import operators.Operator;

/**
 * @author MLB
 *
 *
 */
public class SetReduction 
	extends BoundedOperation 
	implements BasisListener
{
	private Operator			_operatorArg	= null;
	private	BoundedOperation	_operation		= null;
	private Entailment			_source			= null;
	private	boolean				_isSet			= false;
	
	private Operator get_operatorArg() {return _operatorArg;}
	private void set_operatorArg(Operator operator) {_operatorArg = operator;}
	private boolean get_isSet() {return _isSet;}
	private void set_isSet(boolean isSet) {_isSet = isSet;}
	private BoundedOperation get_operation() {return _operation;}
	private void set_operation(BoundedOperation operation) {_operation = operation;}
	private Entailment get_source() {return _source;}
	private void set_source(Entailment source) {_source = source;}
	
	public SetReduction() {super();}
	public SetReduction(Object name) {super(name);}
	
	public void initOperatorArg(Operator operator){
		set_operatorArg(operator);
		return;
	}
	
	public BoundedOperation getOperation(){
		BoundedOperation operation = get_operation();
		if (operation == null){
			Operator operator = get_operatorArg();
			operation = (BoundedOperation)(operator.makeExpression(new ArrayList()));
			set_operation(operation);
			initBounds(operation.getType());
			this.addDomainListener(operation);
			operation.addDomainListener(this);
		}
		return operation;
	}
	
	 public void setKB(KBObject kb) {
	 	super.setKB(kb);
		if (kb != null) {
			Context context = kb.getContext();
			context.addMember(getOperation());
		}
	 	return;
	 }

	 public void initSource(Entailment source){
		set_source(source);
		set_isSet(source instanceof SetObject);
		initSourceBasis(source);
		source.addBasisListener(this);
		return;
	}
	
	public void initSourceBasis(Entailment source){
		List basis = source.getBasis();
		Iterator iterator = basis.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof Domain)
			{
				addBasisElement((Domain)element);
			}
		}
	}
	
	public List coercedOperationOperands(){
		List operands = new ArrayList();
		operands.add(getOperation());
		List coercedOperands = super.coercedOperands(operands);
		return coercedOperands;
	}
	
	public List coercedOperands (List operands) {
		if (operands.size()>0){
			Entailment entailment = (Entailment)(operands.get(0));
			if (entailment instanceof Herd){
				entailment = ((Herd)entailment).getSetObject();
			}
			initSource(entailment);
		}
		if (operands.size()>1){
			initOperatorArg((Operator)(operands.get(1)));
		}
		List coercedOperands = coercedOperationOperands();
		return coercedOperands;
	}
	
	public BoundedOperation getBooleanMember(Object name){
		BoundedOperation member = null;
		if (get_isSet()){
			member = ((SetObject)get_source()).getMember(name);
		}		
		return member;
	}
	
	public BoundedOperation getBooleanMember(Entailment element){
		BoundedOperation member = null;
		if (!(element instanceof Closure)){
			member = getBooleanMember(element.getName());
		}
		return member;
	}
	
	public BoundedOperation getOperationOperand(Entailment element){
		BoundedOperation member = getBooleanMember(element);
		return member;
	}
	
	public void addNewOperationOperand(Domain domain){
		if (domain != null){
			getOperation().addNewOperand(domain);
		}
		return;
	}
	
	public void addBasisElement(Domain domain){
		if (domain instanceof Entailment){
			addNewOperationOperand(getOperationOperand((Entailment)domain));
		}
		return;
	}
	
	public void newBasisElement(Domain domain){
		addBasisElement(domain);
		return;
	}
	
	public void constrainOperands () {
		BoundedExpression operation = getOperation();
		if (operation != null) {
			operation.changeMinIndex(getMinIndex());
			operation.changeMaxIndex(getMaxIndex());
		}
		return;
	}
	
	public void constrainOperation () {
		BoundedExpression operation = getOperation();
		if (operation != null) {
			changeMinIndex(operation.getMinIndex());
			changeMaxIndex(operation.getMaxIndex());
		}
		return;
	}

}

/*
 * Created on Aug 29, 2005
 *
 
 */
package expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domains.Domain;
import entailments.Entailment;
import types.Type;


public class AttributeOperation 
	extends BoundedOperation 
	implements BasisListener
{
	private Entailment	_source		= null;
	private	boolean		_isSet		= false;
	private Object		_attribute	= null;
	
	public AttributeOperation() {super();}
	public AttributeOperation(Type type) {super(type);}
	
	public Entailment getSource() {
		return _source;
	}
	
	public boolean isSet(){
		return _isSet;
	}
	
	public Object getAttribute() {
		return _attribute;
	}
	
	public BoundedOperation getBooleanMember(Object name){
		BoundedOperation member = null;
		if (isSet()){
			member = ((SetObject)getSource()).getMember(name);
		}		
		return member;
	}
	
	public BoundedOperation getBooleanMember(Entailment element){
		BoundedOperation member = getBooleanMember(element.getName());
		return member;
	}
	
	public List coercedOperands (List operands) {
		_source = (Entailment)(operands.get(0));
		_isSet = (_source instanceof SetObject);
		_attribute = operands.get(1);
		getSource().addBasisListener(this);
		operands = new ArrayList();
		List basis = getSource().getBasis();
		Iterator iterator = basis.iterator();
		while (iterator.hasNext()) {
			Object element = iterator.next();
			Domain domain = extractAttribute(element);
			if (domain != null) {
				operands.add(domain);
				BoundedOperation member = getBooleanMember((Entailment)element);
				if (member != null){
					member.addDomainListener(this);
				}
			}
		}
		List coercedOperands = super.coercedOperands(operands);
		return coercedOperands;
	}
	
	public Domain extractAttribute(Object operand){
		Domain domain = null;
		if (operand instanceof Entailment) {
			Object object = ((Entailment)operand).get(getAttribute());
			if (object instanceof Domain) {
				domain = (Domain)object;
			}
		}
		return domain;
	}

	public void newBasisElement(Domain newOperand) {
		Domain domain = extractAttribute(newOperand);
		if (domain != null) {
			addNewOperand(domain);
			BoundedOperation member = getBooleanMember((Entailment)newOperand);
			if (member != null){
				member.addDomainListener(this);
			}
		}
		return;
	}
	
}

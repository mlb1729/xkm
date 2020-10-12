/*
 * Created on Sep 30, 2004
 *
 
 */
package expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import contexts.Context;
import contexts.ContextObject;
import domains.Domain;
import entailments.Closure;
import entailments.Entailment;
import entailments.Herd;
import loader.LoadableConstant;
import loader.LoadableConstraint;
import loader.LoadableEntailment;
import loader.LoadableExpression;
import operators.RootOperator;
import operators.SetOperator;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class SetObject 
	extends Entailment 
	implements SetExpression, BasisListener
{
	private static LoadableExpression _truePredicate = 
		new LoadableConstant(new Object[]{Type.BOOLEAN, new Boolean(true)});
	
	private static SetOperator	_enumSetOperator = new SetOperator();
	
	private		boolean				_isConstant	= false;
	private		boolean				_isEnum		= false;
	
	private		Entailment			_parent = null;
// 	private		Entailment			_ground = null;
	
	private		Object				_structureVariableName = "_";
	private		LoadableExpression	_predicate = _truePredicate;
	
	private		List				_memberBooleans = new ArrayList();
	
	public SetObject() {super();}
	
	public SetObject(List members) 
	{
     	this();
     	initForList(members);
	}
     
	public void initForList(List members){
		_isConstant = true;
		_isEnum = true;
		initForContextList(null, members);
		return;
	}
	
	public SetObject(Context context) {
		this();
		initForContext(context);
	}
	
	public void initForContext(Context context)	{
		initContext(new ContextObject((ContextObject)context));
		return;
	}

	public SetObject(Context context, Iterator iterator) 
	{
		this(context);
		initForIterator(iterator);
	}
	
	public void initForIterator(Iterator iterator)
	{
		while (iterator.hasNext()){
			memberByName(iterator.next());
		}
	}
	
	public SetObject(Context context, List members) 
	{
		this(context);
		initForContextList(context, members);
	}
	
	public void initForContextList(Context context, List members) 
	{
		initForIterator(iterator(members));
		initOperator(_enumSetOperator);
	}
	
	public SetObject(Context context, Herd herd){
		this(context);
		_isConstant = true;
		initForHerd(herd);
	}
	
	public void initForHerd(Herd herd){
		_parent = herd;
		if (herd.isConstant()) {
			_isEnum = true;
			Iterator iterator = herd.getBasis().iterator();
			while(iterator.hasNext()){
				Entailment entailment = (Entailment)(iterator.next());
				memberByName(entailment.getName());
			}
		} else {
			Iterator iterator = herd.getBasis().iterator();
			while (iterator.hasNext()){
				Object object = iterator.next();
				newBasisElement((Domain)object);
		}
		herd.addBasisListener(this);
		}
	}

//	public SetObject(Context context, SetOperation operation){
//		this(context);
//		initForSetOperation(operation);
//	}
	
//	public void initForSetOperation(SetOperation operation){
//		initForIterator(operation.getSet().memberIterator());
//		operation.addBasisListener(this);
//	}

	public SetObject(Context context, 
						Entailment parent,
						Object structureVariableName,
						LoadableExpression predicate) {
		this(context);
		if (!initForEnumSet(parent))
		{
			initForFilter(parent, structureVariableName, predicate);
		}
	}
	
	public boolean initForEnumSet(Entailment parent) {
		_parent = parent;
		boolean isEnum = false;
		if (parent instanceof SetObject) {
			SetObject set = (SetObject)parent;
			isEnum = set.isEnum(); 
			if (isEnum){
				_isEnum = true;
				Iterator iterator = set.getMemberBooleans().iterator();
				while(iterator.hasNext()){
					BoundedVariable parentBoolean = (BoundedVariable)(iterator.next());
					memberByName(parentBoolean.getName());
				}
			}
		}
		return isEnum;
	}
		
	public void initForFilter(Entailment parent,
								Object structureVariableName,
								LoadableExpression predicate) {
		_parent = parent;
		if (parent instanceof Herd){
			Herd herd = (Herd)parent;
			initForHerd(herd);
		} 
		if (structureVariableName != null){
			_structureVariableName = getName(structureVariableName);
		}
		if (predicate != null){
			_predicate = predicate;
		}
		Iterator iterator = parent.getBasis().iterator();
		while (iterator.hasNext()){
			newBasisElement((Domain)iterator.next());
		}
		parent.addBasisListener(this);
	}
	
	public boolean isConstant() {
		return _isConstant;
	}
	
	public boolean isEnum() {
		return _isEnum;
	}
	
	public List getMemberBooleans(){
		List booleans = _memberBooleans;
		return booleans;
	}

	public BoundedOperation getMember (Object name) {
		name = getName(name);
		BoundedOperation member = (BoundedOperation)(getContext().getVariable(name));
		return member;
	}

	public BoundedOperation findMember (Object name) {
		BoundedOperation member = getMember(name);
		if (member == null) {
			error("Object " + name + " is not a member of " + this);
		}
		return member;
	}
	
	public Iterator memberIterator() {
		Iterator iterator = getContext().ownVariablesIterator();
		return iterator;
	}

	public BoundedOperation memberByName(Object name) {
		name = getName(name);
		BoundedOperation operation = getMember(name);
		if (operation == null) {
			operation = 
				(isConstant() ?
					new BoundedVariable(name, Type.BOOLEAN.getFixedPoint(Boolean.TRUE)) :
					new BoundedVariable(name, Type.BOOLEAN));
			getMemberBooleans().add(operation);
			registerNewMember(operation);
		}
		return operation;
	}
	

	public BoundedOperation memberByReference(BoundedOperation operation){
		BoundedOperation member = getMember(operation.getName());
		if (member == null) {
			member = operation;
			registerNewMember(member);
		}
		return member;
	}
	
	protected void registerNewMember(BoundedOperation operation) {
		getContext().addOwnVariable(operation);
		operation.activate();		
		return;
	}
	
	// this is the basic basis event
	public void newBasisElement(Domain domain) {
		if ((domain instanceof Entailment) 
				&& !(domain instanceof Closure)){
			newMemberElement((Entailment)domain);
		}
		return;
	}
	
	public void newMemberElement(Entailment entailment) {
		Object name = entailment.getName();
		BoundedOperation member = getMember(name);
		if (member == null) {
			makeNewElement(name, entailment);
			super.addNewBasisElement(entailment);
		}
		return;
	}

	public LoadableEntailment makeLoadableFilterBody(Object reason, 
														Expression variable, 
														Object value) {
		Object[] eqExprArgs = {RootOperator.Equal, variable, value};
		LoadableExpression eqExpr = new LoadableExpression(eqExprArgs);
		Object[] eqCTArgs = {eqExpr, reason};
		LoadableConstraint eqCT = new LoadableConstraint(eqCTArgs);
		Object[] loadableArgs = {reason + " Entailment", variable, eqCT};
		LoadableEntailment loadable = new LoadableEntailment(loadableArgs);
		return loadable;
	}
	
	public void makeNewElement(Object name, Entailment entailment){
		name = getName(name);
		String reason = "Set filter for " + name;
		// get contexts and variables
		ContextObject thisContext = (ContextObject)getContext();
		ContextObject parentContext = (ContextObject)(_parent.getContext());
		Expression parentVariable = parentContext.getVariable(name);
		Expression thisVariable = memberByName(name);	// will create it
		if (_predicate == null) {	// shouldn't happen any more
			LoadableEntailment loadable = makeLoadableFilterBody(name, thisVariable, parentVariable);
			Object expr = loadable.load(thisContext);
			BoundedExpression expression = (BoundedExpression)expr;		
			thisContext.addMember(expression);
			expression.activate();
			addNewOperand(expression);
		} else {
			// create closure body
			Object[] andExprArgs = {RootOperator.And, parentVariable, _predicate};
			LoadableExpression andExpr = new LoadableExpression(andExprArgs);
			LoadableEntailment body = makeLoadableFilterBody(name, thisVariable, andExpr);
			// create closure
			//		addClosure(List closureOperands,
			//					ContextObject outerEnvironmentContext,
			//					Object closureName, 
			//					Object structureVariableName, 
			//					Entailment structure, 
			//					LoadableEntailment loadable) 
			Closure closure = addClosure(new ArrayList(),
											thisContext,
											reason + " Closure",
											_structureVariableName,
											entailment,
											body);
			closure.activate();
		}
		return;
	}
	
	public String toStringOperatorOperands () {
		String string = super.toStringOperatorOperands(getMemberBooleans().iterator());
		return string;
	}

	public String toStringOperatorOperands (List operands) {
		String string = toStringOperatorOperands();
		return string;
	}
	
	public String toStringOperatorOperands (Iterator iterator) {
		String string = toStringOperatorOperands();
		return string;
	}
	
}

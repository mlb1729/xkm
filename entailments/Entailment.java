/*
 * Created on Oct 5, 2004
 *
 
 */
package com.resonant.xkm.entailments;

import java.util.ArrayList;
import java.util.List;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.contexts.ContextObject;
import com.resonant.xkm.domains.Domain;
import com.resonant.xkm.expressions.Variable;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.km.Fetcher;
import com.resonant.xkm.loader.LoadableEntailment;
import com.resonant.xkm.operations.Entails;
import com.resonant.xkm.operators.EntailmentOperator;
import com.resonant.xkm.operators.Operator;


public class Entailment 
	extends Entails
	implements Fetcher, Variable
{
	private Context	_context	= null;
	private Object	_ownKey		= null;
	private	Object	_annotation	= null;
	
	public Object	getAnnotation(){
		return _annotation;
	}
	
	public void setAnnotation(Object annotation){
		_annotation  = annotation;
		return;
	}

	private Context get_context() {return _context;}
	private void set_context(Context context) {_context = context; return;}

	public Entailment() {super();}
	
	public Entailment(Context context){
		this();
		initContext(context);
	}
	
	public Context getContext () {
		Context context = get_context();
		if (context == null) {
			error("Attempt to access uninitialized context in " + this);
		}
		return context;
	}
	
	public void initContext (Context context) {
		Context oldContext = get_context();
		if (oldContext != null) {
			error("Attempt to re-initialize context in " + this);
		}
		set_context(context);
		return;
	}

	public Object get(Object key) {
		boolean isIndex = (key instanceof Object[]);
		if (isIndex){
			key = makeIndexKey(key, true);
		}
		Object member = (isIndex ?
							get(key, true) :
							getContext().getVariable(key));
//		if (member == null) {
//			if (key instanceof Entailment) {
//				member = getMatchingMember((Entailment)key);
//			}
//		}
		return member;
	}
	
	public Object getBasisElement (Object name, LoadableEntailment loadable) {
		Object member = get(name);
		if ((member == null) && (loadable != null)) {
			member = loadable.load(getContext(), name);
			addNewOperand((Domain)member);
		}
		return member;
	}

	public Object getMatchingMember(Entailment testEntailment) {
		Object object = getContext().getMatchingMember(testEntailment.getContext());
		return object;
	}
	
	public void addNewOperand(Domain newOperand) {
		super.addNewOperand(newOperand);
		addNewBasisElement(newOperand);
		return;
	}

	public Object[] getPath() {
		Object[] path = getPath(false);
		return path;
	}
	
	public Context getPathContext() {
		Context context = getContext();
		return context;
	}
	
	public Object[] getPath(boolean appendName) {
		Object[] path = getPath(appendName, getPathContext());
		return path;
	}
	
	public Object[] getPath(boolean appendName, Context context) {
		Object[] path = null;
		Operator operator = getOperator();
		if (operator instanceof EntailmentOperator) {
			EntailmentOperator entailmentOperator = ((EntailmentOperator)operator);
			List trail;
			Object name = entailmentOperator.getName();
			name = getName(name);
			if (context == null) {
				trail = KBObject.getPathNames(context, name);
				path = null;	// breakpoint just for debugging null contexts
			} else {
				trail = KBObject.getPathNames(context, name);
				if (appendName) {
					trail.add(name);
				}
				path = trail.toArray();
			}
		}
		return path;
	}
	
	public Object[] getOwnPath() {
		Object[] path = getPath(false, getContext());
		return path;
	}
	
	public Object getOwnIndexKey(){
		Object key = _ownKey;
		if (key == null){
			key = makeIndexKey(getOwnPath(), false);
			_ownKey = key;
		}
		return key;
	}
	
	public Entailment makeAliasEntailment(Object name, Context aliasContext) {
		EntailmentOperator operator = new EntailmentOperator(name, Alias.class);
		List operands = new ArrayList();	// just a named structure with no args
		Alias entailment = (Alias)(operator.newExpression(operands, getContext()));
		entailment.initAliasContext(aliasContext);
		return entailment;
	}

	protected static String closurePrefix(Object localName, Entailment structure) {
		String closurePrefix = "(with " + localName + " := " + toPathString(structure.getPath()) + ")"; 
		return closurePrefix;
	}	
	
	public Closure addClosure(Object localName, Entailment structure, LoadableEntailment loadable) {
		// get the enclosing context that the closure will reside in 
		ContextObject outerEnvironmentContext = (ContextObject)getContext();
		String closureName = closurePrefix(localName, structure);
		Closure closure = addClosure(outerEnvironmentContext, closureName, localName, structure, loadable);
		return closure;
	}	
		
	public Closure addClosure(List closureOperands,
								ContextObject outerEnvironmentContext,
								Object closureName, 
								Object structureVariableName, 
								Entailment structure, 
								LoadableEntailment loadable) 
{
			// add the outer structure to the initial operands
			closureOperands.add(this);
			Closure closure = super.addClosure(closureOperands, 
												outerEnvironmentContext, 
												closureName,
												structureVariableName,
												structure,
												loadable);
		return closure;
	}
	
//	public String toString(){
//		String string = super.toString() + "(";
//		if (hasOperands()){
//			boolean first = true;
//			Iterator it = getOperands().iterator();
//			while (it.hasNext()){
//				if (first) {
//					first = false;
//				} else {
//					string = string + ",";
//				}
//				string = string + getName(it.next()); 	
//			}
//		} 
//		string = string + ")";
//		return string;
//	}
	
}

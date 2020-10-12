/*
 * Created on Aug 11, 2005
 *
 
 */
package entailments;

import java.util.List;

import contexts.Context;
import contexts.ContextObject;
import loader.Loadable;
import loader.LoadableEntailment;
import operators.EntailmentOperator;

/**
 * @author MLB
 *
 *
 */
public class Closure 
	extends Entailment 
{
	private Entailment	_structure		= null;
	private Object		_structureName	= null;
	
	private Object get_structureName() {return _structureName;}
	private void set_structureName(Object name) {_structureName = getName(name);}
	private Entailment get_structure() {return _structure;}
	private void set_structure(Entailment structure) {_structure = structure;}

	public Closure() {super();}

	public Object getStructureName() {
		Object name = get_structureName();
		return name;
	}
	
	public Entailment getStructure() {
		Entailment structure = get_structure();
		return structure;
	}
	
	public void setBindingInfo (Object name, Entailment structure) {
		set_structure(structure);
		set_structureName(name);
		return;
	}
	
	public static Closure makeClosure(List closureOperands,
										ContextObject outerEnvironmentContext,
										Object closureName,
										Object structureVariableName, 
										Entailment structure, 
										LoadableEntailment loadable) 
	{
		closureName = getName(closureName);
		structureVariableName = getName(structureVariableName);
		// set up context to be nested within closure for use by its structure members
		Context innerClosureContext = new ContextObject(outerEnvironmentContext);
		// make operator for the closure
		EntailmentOperator closureOperator = new EntailmentOperator(closureName, Closure.class);
		// get the closed-over member, disguised with the desired local name
		Entailment aliasStructure = structure.makeAliasEntailment(structureVariableName, innerClosureContext);
		// put the aliased member into the initial operands for the closure
		closureOperands.add(aliasStructure);
		// create the closure, giving it it's own inner context
		Closure closure = (Closure)(closureOperator.newExpression(closureOperands, innerClosureContext));
		// set the name and structure members
		closure.setBindingInfo(structureVariableName, structure);
		// set the closure's story to be the quantification
		innerClosureContext.initStory(closure);
		// set its name
		innerClosureContext.setName(closureName);
		// add it into the namespace
		innerClosureContext.addVariable(aliasStructure);
		// load each of the loadable's members and register in closure
		int size = loadable.getSize();
		for (int i=1; i < size; i++) {
			Object parameter = loadable.getParameter(i);
			if (parameter instanceof Loadable) {
				parameter = ((Loadable)parameter).load(innerClosureContext);
				loadable.registerLoadedObject(closureOperands, parameter);
			}
		}
		// add the new closure into the outer namespace	
		outerEnvironmentContext.addVariable(closure);
		outerEnvironmentContext.addMember(closure);
		return closure;
	}

}

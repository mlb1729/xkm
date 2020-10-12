/*
 * Created on Aug 17, 2005
 *
 
 */
package loader;

import contexts.Context;
import entailments.Entailment;
import expressions.SetObject;
import operators.EntailmentOperator;
import operators.SetOperator;

/**
 * @author MLB
 *
 *
 */
public class LoadableSetObject 
	extends LoadableMember 
{
	private Loadable			_parent = null;
	private Object 				_structureVariableName = null;
	private LoadableExpression 	_predicate = null;
	
	public LoadableSetObject() {super();}

	public LoadableSetObject(Object[] parameters) {
		super(new Object[]{parameters[0]});
		_parent = (Loadable)(parameters[1]);
		if (parameters.length > 2){
			_structureVariableName = getName(parameters[2]);
			_predicate = (LoadableExpression)parameters[3];
		}
	}
	
	public EntailmentOperator newEntailmentOperator(Object name) {
		name = getName(name);
		EntailmentOperator operator = new SetOperator(name);
		return operator;
	}

	public Object load(Context context, Object name) {
		name = getName(name);
		SetObject set = (SetObject)(super.load(context, name));
		Entailment parent = (Entailment)(_parent.load(context));
		if (!(set.initForEnumSet(parent))){
			set.initForFilter(parent, _structureVariableName, _predicate);
		}
		context.addMember(set);
		return set;
	}

}

/*
 * Created on Oct 1, 2004
 *
 
 */
package expressions;

import java.util.List;

import contexts.Context;
import domains.Domain;
import entailments.Herd;
import km.Fetcher;

/**
 * @author MLB
 *
 *
 */
public class SetOperation 
	extends Operation 
	implements SetExpression, Fetcher, BasisListener
{
	private SetObject	_set	= null;

	private SetObject get_set() {return _set;}
	private void set_set(SetObject set) {_set = set;}

	public SetOperation() {
		super();
	}

	public SetOperation(Object name) {
		super(name);
	}
	
	public SetOperation(SetObject set) {
		this();
		initSet(set);
	}
	
	public SetOperation(Context context, List members) {
		this();
		initSet(context, members);
	}
	
	public SetOperation(Herd herd) {
		this(herd.getSetObject());
	}
	
	protected void initSet (SetObject set) {
		set_set(set);
		set.addBasisListener(this);
		return;
	}
	
	protected void initSet (Context context, List members) {
		initSet(new SetObject(context, members));
		return;
	}
	
	public SetObject getSet() {
		return get_set();
	}

	public BoundedOperation getMember(Object name) {
		BoundedOperation member = getSet().getMember(name);
		return member;
	}
	
	public Object get(Object name){
		BoundedOperation member = getMember(name);
		return member;
	}
	
	public String toStringValue() {
		SetObject set = getSet();
		String toStringBodyLHS = set.toStringBodyLHS();
		return toStringBodyLHS;
	}
	
	public void newBasisElement(Domain domain){
		super.addNewBasisElement(domain);
		return;
	}

}

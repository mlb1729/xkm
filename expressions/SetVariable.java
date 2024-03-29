/*
 * Created on Oct 1, 2004
 *
 
 */
package expressions;

import java.util.List;

import contexts.Context;
import entailments.Herd;
import km.Fetcher;

/**
 * @author MLB
 *
 *
 */
public class SetVariable 
	extends SetOperation
	implements Variable, Fetcher
{
	private	Object	_annotation	= null;

	public Object	getAnnotation(){
		return _annotation;
	}
	
	public void setAnnotation(Object annotation){
		_annotation  = annotation;
		return;
	}
	
	public SetVariable(Object name) {
		super(name);
	}

	public SetVariable(Object name, SetObject set) {
		this(name);
		initSet(set);
	}

	public SetVariable(Object name, Context context, List members) {
		this(name);
		initSet(context, members);
	}
	
	public SetVariable(Object name, Herd herd) {
		this(name, herd.getSetObject());
	}

}

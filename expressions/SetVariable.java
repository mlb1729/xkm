/*
 * Created on Oct 1, 2004
 *
 
 */
package com.resonant.xkm.expressions;

import java.util.List;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Herd;
import com.resonant.xkm.km.Fetcher;

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

/*
 * Created on Oct 10, 2005
 *
 
 */
package com.resonant.xkm.kb;

import com.resonant.xkm.api.ConstraintDescription;
import com.resonant.xkm.km.KMObject;
import com.resonant.xkm.km.Named;

/**
 * @author MLB
 *
 *
 */
public class ConstraintDescriptionObject 
	extends KMObject 
	implements Named, ConstraintDescription 
{
	private static int	_UID	= 0;

	private Object	_name			= null;
	private Object	_documentation	= null;
	private	Object	_object			= null;
	
	public ConstraintDescriptionObject(){
		super();
	};
	
	public ConstraintDescriptionObject(Object object){
		this();
		initObject(object);
	};

	public ConstraintDescriptionObject(Object name, Object object, Object documentation) {
		this(object);
		initName(name);
		initDocumentation(documentation);
	}

	public Object getName() {
		Object name = _name;
		if (name == null){
			name = "Constraint #" + ++_UID;
		}
		return name;
	}

	public Object getDocumentation() {
		Object documentation = _documentation;
		if (documentation == null){
			documentation = toString(getObject());
		}
		return documentation;
	}

	public Object getObject() {
		return _object;
	}

	public void initName(Object name) {
		if (_name != null){
			error("Attempt to reinitialize name of " + this + 
					" to " + name + " from " + _name);
		}
		_name = name;
		return;
	}

	public void initDocumentation(Object documentation) {
		if (_documentation != null){
			error("Attempt to reinitialize documentation of " + this + 
					" to " + documentation + " from " + _documentation);
		}
		_documentation = documentation;
	}

	public void initObject(Object object) {
		if (_object != null){
			error("Attempt to reinitialize object of " + this + 
					" to " + object + " from " + _object);
		}
		_object = object;
		return;
	}
	
	public static ConstraintDescription coerce(Object reason) {
		ConstraintDescription description = 
			((reason instanceof ConstraintDescription) ?
				((ConstraintDescription)reason) :
				new ConstraintDescriptionObject(reason));
		return description;
	}

}

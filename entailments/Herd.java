/*
 * Created on Jul 30, 2005
 *
 
 */
package entailments;

import expressions.SetObject;
import loader.LoadableEntailment;

/**
 * @author MLB
 *
 *
 */
public class Herd 
	extends Entailment 
{	
	private static final String _defaultNamePrefix = "_ ";
	
	private LoadableEntailment	_loadable 	= null;
	private int					_nameCount 	= 0;
	private String				_namePrefix = _defaultNamePrefix;
	private SetObject			_setObject	= null;
	private boolean				_isConstant	= false;

	private LoadableEntailment get_loadable() {return _loadable;}
	private void set_loadable(LoadableEntailment loadable) {_loadable = loadable;}
	private SetObject get_setObject() {return _setObject;}
	private void set_setObject(SetObject setObject) {_setObject = setObject;}

	private String get_namePrefix() {return _namePrefix;}
	private void set_namePrefix(String namePrefix) {_namePrefix = toString(getName(namePrefix));}

	public boolean isConstant() {return _isConstant;}
	public void makeConstant() {_isConstant = true;}

	public Herd() {super();}

	public LoadableEntailment getLoadable() {
		LoadableEntailment loadable = get_loadable();
//		if (loadable == null) {
//			error("Attempt to access uninitialized loadable in " + this);
//		}
		return loadable;
	}
	
	public void initLoadable(LoadableEntailment loadable) {
		LoadableEntailment oldLoadable = get_loadable();
		if (oldLoadable != null) {
			error("Attempt to re-initialize loadable in " + this);
		}
		set_loadable(loadable);
		return;
	}
	
	public String getNamePrefix() {
		String prefix = get_namePrefix();
		return prefix;
	}

	public void setNamePrefix(Object prefix) {
		set_namePrefix(prefix.toString());
		return;
	}

	public String nextName() {
		String name = getNamePrefix() + (++_nameCount);
		return name;
	}
	
	public Object getBasisElement(Object name) {
		if (name == null) {
			name = nextName();
		}
		name = getName(name);
		Object member = getBasisElement(name, getLoadable());
		return member;
	}
	
	public Object get(Object name, boolean mayCreate) {
		Object object = get(getName(name));
		if ((object == null) && mayCreate){
			object = getBasisElement(name);
		}
		return object;
	}
	
	public SetObject getSetObject() {
		SetObject setObject = get_setObject();
		if (setObject == null) {
			setObject = new SetObject(getContext(), this);
			set_setObject(setObject);
		}
		return setObject;
	}
	
	public String toStringOperatorName () {
		String string = super.toStringOperatorName();
		string = string + "(of type ";
		LoadableEntailment loadable = get_loadable();
		String type = "Any";
		if (loadable != null) {
			Object parameter0 = loadable.getParameter(0);
			if (parameter0 != null) {
				type = toString(parameter0);
			}
		}
		string = string + type + ")";
		return string;
	}

}

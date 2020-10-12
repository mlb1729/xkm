/*
 * Created on Dec 14, 2004
 *
 
 */
package reader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MLB
 *
 *
 */
public class Combo 
	extends ArrayList 
{
	private Object	_type	= null;
	
	private Object get_type() {return _type;}
	private void set_type(Object type) {_type = type;}

	public Combo() {
		super();
	}
	
	public Combo(Object type) {
		this();
		setType(type);
	}
	
	public Object getType () {
		return get_type();
	}
	
	public List getList () {
		List list = this;
		return list;
	}
	
	public boolean isTyped () {
		boolean isTyped = (getType() != null);
		return isTyped;
	}

	public void setType (Object type) {
		set_type(type);
		return;
	}

	public String toString () {
		String string = super.toString();
		if (isTyped()) {
			string = getType() + string;
		} else if (size() == 1) {
			string = get(0).toString();
		}
		return string;
	}
	
	public boolean add(Token token) {
		boolean isAdded = super.add(token);
		return isAdded;
	}
}

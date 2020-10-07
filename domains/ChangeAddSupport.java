/*
 * Created on Sep 27, 2004
 *
 
 */
package com.resonant.xkm.domains;


/**
 * @author MLB
 *
 *
 */
public class ChangeAddSupport 
	extends ChangeManagedPoint 
{
	private Object	_object;
	
	private Object get_object() {return _object;}
	private void set_object(Object support) {_object = support;}
	
	public ChangeAddSupport() {super();}
	
	public void initObject(Object object) {
		set_object(object);	
		return;
	}
	
	public void undo() {
		getPoint().removeSupport(get_object()); 
		return;
	}
	
	public void release () {
		super.release();
		set_object(null); 
		return;
	}

}

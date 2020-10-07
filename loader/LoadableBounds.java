/*
 * Created on Sep 28, 2004
 *
 
 */
package com.resonant.xkm.loader;

import com.resonant.xkm.bounds.Bounds;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class LoadableBounds 
	extends LoadableObject 
{
//	private	Type	_type 		= null;
	private	boolean	_untyped	= false;
	
	protected LoadableBounds() {super();}
	
	public LoadableBounds(Object[] parameters){super(Type.classifyTypes(parameters));}
	
//	public Type getType() {
//		if (_type == null) {
//			Object parameter0 = getParameter(0);
//			_type = Type.coerceType(parameter0);
//			_untyped = (_type == null);
//			if (_untyped){
//				_type = Type.coerceType(parameter0.getClass());			
//			}
//		}
//		return _type;
//	}
	
	public Type getType() {
		Object parameter0 = getParameter(0);
		Type type = Type.coerceType(parameter0);
		_untyped = type == null;
		if (_untyped){
			type = Type.coerceType(parameter0.getClass());			
		}
		return type;
	}
	
	public Object load(Context context) {
		getType();
		int hiIndex = (_untyped ? 1 : 2);
		int loIndex = hiIndex-1;
		int size = getSize();
		Bounds bounds = null;
		if (size > hiIndex) {
			bounds = getType().getFixedBounds(getParameter(loIndex), getParameter(hiIndex));			
		} else if (size > 1) {
			bounds = getType().getFixedBounds(getParameter(loIndex), getParameter(loIndex));
		} else {
			bounds = getType().getFixedBounds();
		}
		return bounds;
	}
	
}

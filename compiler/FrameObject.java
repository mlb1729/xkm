/*
 * Created on Mar 29, 2005
 *
 
 */
package compiler;

import java.util.HashMap;
import java.util.Map;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class FrameObject 
	extends KMObject 
	implements Frame
{
	Frame		_parent	= null;
	Map			_map	= null;

	private Map get_map() {return _map;}
	private void set_map(Map map) {_map = map;}
	private Frame get_parent() {return _parent;}
	
	public FrameObject() {
		this(null);
	}
	
	public FrameObject(Frame parent) {
		super();
		_parent = parent;
	}
	
	protected Frame getParent() {
		Frame parent = get_parent();
		return parent;
	}

	protected Map getMap () {
		Map map = get_map();
		if (map == null) {
			map = new HashMap();
			set_map(map);
		}
		return map;
	}
	
	public Object get(Object name) {
		Object object = null;
		Map map = get_map();
		if (map != null) {
			object = map.get(name);
		}
		if (object == null) {
			Frame parent = get_parent();
			if (parent != null) {
				object =  parent.get(name);
			}
		}
		return object;
	}

	public boolean define(Object name, Object value) {
		Object object = null;
		Map map = get_map();
		if (map != null) {
			object = map.get(name);
		}
		boolean isNew = (object == null);
		if (isNew) {
			getMap().put(name, value);
		}
		return isNew;
	}

}

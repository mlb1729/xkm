/*
 * Created on Sep 16, 2004
 *
 
 */
package containers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class NamespaceObject 
	extends KMObject 
	implements Namespace
{
	private static int _INITIAL_MAP_SIZE = 4;
	
	Object		_name	= "";
	Namespace	_parent	= null;
	Map			_map	= null;

	private Map get_map() {return _map;}
	private void set_map(Map map) {_map = map;}
	private Object get_name() {return _name;}
	private void set_name(Object name) {_name = getName(name);}
	private Namespace get_parent() {return _parent;}
	private void set_parent(Namespace parent) {_parent = parent;}
	
	public NamespaceObject() {super();}

	public NamespaceObject(Map map) {
		this();
		set_map(map);
	}
	
	protected Map getMap () {
		Map map = get_map();
		if (map == null) {
			map = new HashMap(_INITIAL_MAP_SIZE);
			set_map(map);
		}
		return map;
	}

	public Object getName() {
		return get_name();
	}

	public void setName(Object key) {
		Object newName = getName(key);
		Object oldName = getName();
		if (oldName != newName) {
			Namespace parent = getParent();
			if (parent != null) {
				parent.remove(oldName);
			}
			set_name(newName);
			if (parent != null) {
				parent.add(this);
			}
		}
		return;
	}
	
	public Namespace getParent() {
		return get_parent();
	}

	public void setParent(Namespace namespace) {
		Namespace parent = getParent();
		if (parent != namespace) {
			Object name = getName();
			if (parent != null) {
				parent.remove(name);
			}
			set_parent(namespace);
			if (namespace != null) {
				namespace.add(this);
			}
		}
		return;
	}

	public Object get(Object key) {
		Object object = null;
		Map map = get_map();
		if (map != null) {
			Object name = getName(key);
			object = map.get(name);			
		}
		return object;
	}
	
	public boolean add(Object object) {
		Object name = getName(object);
		boolean isAdded = (get(name) == null);
		if (isAdded) {
			getMap().put(name, object);			
		}
		return isAdded;
	}
	
	public boolean remove(Object key) {
		boolean isRemoved = false;
		Map map = get_map();
		if (map != null) {
			Object name = getName(key);
			isRemoved = (map.remove(name) != null);
		}
		return isRemoved;
	}
	
	public Object find(Object key) {
		Object name = getName(key);
		Object object = get(name);
		if (object == null) {
			Namespace parent = getParent();
			if (parent != null) {
				object = parent.find(name);
			}
		}
		return object;
	}

	public boolean getPath(Namespace namespace, List list) {
		boolean isRelative = true;
		if (this != namespace) {
			Namespace parent = getParent();
			isRelative = (parent != null);
			if (isRelative) {
				isRelative = parent.getPath(namespace, list);
			}
		}
		list.add(this);
		return isRelative;
	}
	
	public Iterator iterator() {
		Map map = get_map();
//		Iterator iterator = ((map == null) ? EMPTY_ITERATOR : (new TreeSet(map.values())).iterator());
		Iterator iterator = ((map == null) ? EMPTY_ITERATOR : ((map.values())).iterator());
		return iterator;
	}

}

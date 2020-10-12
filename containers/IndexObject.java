/*
 * Created on Sep 4, 2004
 *
 
 */
package containers;

import java.util.*;

import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class IndexObject 
	extends KMObject 
	implements Index 
{
	private Class	_javaClass;
	private int		_size = 0;
	private List	_indexToObjectMap = null;
	private Map  	_objectToIndexMap = null;
	private boolean	_isClosed = false;
	private Object	_undefinedObject = null;
	
	private List get_indexToObjectMap() {return _indexToObjectMap;}
	private void set_indexToObjectMap(List toObjectMap) {_indexToObjectMap = toObjectMap;}
	private boolean get_isClosed() {return _isClosed;}
	private void set_isClosed(boolean fixed) {_isClosed = fixed;}
	private Class get_javaClass() {return _javaClass;}
	private void set_javaClass(Class javaClass) {_javaClass = javaClass;}
	private Map get_objectToIndexMap() {return _objectToIndexMap;}
	private void set_objectToIndexMap(Map toIndexMap) {_objectToIndexMap = toIndexMap;}
	private int get_size() {return _size;}
	private void set_size(int size) {_size = size;}
	private Object get_undefinedObject() {return _undefinedObject;}
	private void set_undefinedObject(Object object) {_undefinedObject = object;}
		
	protected IndexObject() {super();}
	
	public IndexObject(Class javaClass) {
		set_javaClass(javaClass);
		return;
	}
	
	public Class getJavaClass () {
		return get_javaClass();
	}
	
	public int getSize () {
		return get_size();
	}
	
	public boolean isClosed () {
		return (get_isClosed());
	}
	
	public void close() {
		set_isClosed(true);
		return;
	}
	
	private List getIndexToObjectMap () {
		List indexToObjectMap = get_indexToObjectMap();
		if (indexToObjectMap == null) {
			indexToObjectMap = new ArrayList();
			set_indexToObjectMap(indexToObjectMap);
		}
		return indexToObjectMap;
	}
	
	private Map getObjectToIndexMap () {
		Map objectToIndexMap = get_objectToIndexMap();
		if (objectToIndexMap == null) {
			objectToIndexMap = new HashMap();
			set_objectToIndexMap(objectToIndexMap);
		}
		return objectToIndexMap;
	}
	
	public Object getUndefinedObject () {
		Object object = get_undefinedObject();
		if (object == null) {
			object = "<...undefined " + this + "...>";
			set_undefinedObject(object);
		}
		return object;
	}
	
	public Object checkClass(Object object) {
		if (!getJavaClass().isInstance(object)) {
			error("The object {" + object + "} can't be indexed in " + this);
		}
		return object;
	}
	
	public Integer getKey (Object object) {
		Integer key = ((object == getUndefinedObject()) 
						? new Integer(Integer.MAX_VALUE)
						: ((Integer) getObjectToIndexMap().get(object)));
		return key;
	}
	
	public int getIndex (Object object) {
		Integer key = getKey(object);
		int index = ((key == null) ?  0 : key.intValue());
		return index;
	}
	
	public Integer key (Object object) {
		Integer key = getKey(object);
		if (key == null) {
			if (isClosed()) {
				error("Can't add new object " + object + " to closed index " + this);
			} else {
				int index = getSize() + 1;
				set_size(index);
				key = new Integer(index);
				getObjectToIndexMap().put(object,key);
				getIndexToObjectMap().add(object);
			}
		}
		return key;
	}

	public int index (Object object) {
		int index = key(object).intValue();
		return index;
	}
		
	public Object getObject(int index) {
		Object object = null;
		if ((index <= getSize()) && (0 < index)) {
			object = getIndexToObjectMap().get(index - 1);			
		} else {
			object = getUndefinedObject();
		}
		return object;
	}
	
}

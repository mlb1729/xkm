/*
 * Created on Nov 10, 2005
 *
 
 */
package containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class KMSet 
	extends KMObject
{
	private static int _INITIAL_DEFAULT_SET_SIZE = 2;
	
	private ArrayList _list = null;	
	
	public KMSet() {
		this(_INITIAL_DEFAULT_SET_SIZE);
	}
	
	public KMSet(int size) {
		super();
		_list	= new ArrayList(size);
	}
	
	public KMSet(Object[] array){
		this();
		for (int i=0; i<array.length; i++){
			_list.set(i, array[i]);
		}
	}
	
	public KMSet(Collection collection){
		this();
		addAll(collection);
	}
	
	public KMSet(KMSet set){
		this(set.getList());
	}
	
	public List getList(){
		return _list;
	}
	
	public Object get(int i){
		Object result = _list.get(i);
		return result;
	}
	
	public void set(int i, Object object){
		_list.set(i, object);
		return;
	}
	
	public boolean contains(Object object){
		boolean contains = _list.contains(object);
		return contains;
	}
	
	public boolean add(Object object){
		boolean isAdded = !contains(object);
		if (isAdded){
			isAdded = _list.add(object);
		}
		return isAdded;
	}
	
	public void addAll(Collection collection){
		Iterator iterator = collection.iterator();
		while(iterator.hasNext()) {
			add(iterator.next());
		}
		return;
	}
	
	public void addAll(KMSet listSet){
		addAll(listSet.getList());
		return;
	}
	
	public boolean remove(Object object){
		boolean isRemoved = _list.remove(object);
		return isRemoved;
	}
	
	public void clear() {
		_list.clear();
		return;
	}

	public Iterator iterator() {
		Iterator iterator = _list.iterator();
		return iterator;
	}
	
	public boolean isEmpty() {
		boolean isEmpty = _list.isEmpty();
		return isEmpty;
	}
	
	public int size() {
		int size = _list.size();
		return size;
	}
	
	public Object[] toArray(){
		Object[] array = _list.toArray();
		return array;
	}
	
	public String toString(){
		String string = toString(_list);
		return string;
	}
	
}

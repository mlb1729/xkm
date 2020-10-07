/*
 * Created on Sep 17, 2004
 *
 
 */
package com.resonant.xkm.recycling;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.resonant.xkm.km.KMObject;

public class RecyclableFactory 
	extends KMObject 
	implements Recycler 
{
	Map 		_pools 		= new HashMap();
	Class		_superClass;
	
	int			_allocations	= 0;
	int			_reuses			= 0;
	int			_created		= 0;
	int			_pooled			= 0;
	int			_recycles		= 0;
	
	private Map get_pools() {return _pools;}
	private Class get_superClass() {return _superClass;}
	private void set_superClass(Class superClass) {_superClass = superClass;}
	
	public int getAllocations() {return _allocations;}
	public int getReuses() {return _reuses;}
	public int getCreated() {return _created;}
	public int getPooled() {return _pooled;}
	public int getRecycles() {return _recycles;}
	
	public RecyclableFactory() {
		this(Recyclable.class);
	}
	
	public RecyclableFactory(Class superClass) {
		super();
		initSuperClass(superClass);
	}
	
	public Class getSuperClass () {
		return get_superClass();
	}
	
	private void initSuperClass (Class superClass) {
		if (!Recyclable.class.isAssignableFrom(superClass)) {
			error("Can't create recycler for " + superClass +
					" that is not a Recyclable class" );
		}
		set_superClass(superClass);
		return;
	}
	
	protected LinkedList getPool(Class objectClass) {
		Map pools = get_pools();
		LinkedList pool = (LinkedList)(pools.get(objectClass));
		if (pool == null) {
			pool = new LinkedList();
			pools.put(objectClass, pool);
		}
		return pool;
	}

	protected Recyclable makeObject(Class objectClass) {
		Class superClass = get_superClass();
		if (!superClass.isAssignableFrom(objectClass)) {
			error("Can't make recyclable instances of " + objectClass +
					" that's not a sub-class of " + superClass);
		}
		Recyclable object = (Recyclable)newObject(objectClass);
		_created++;
		return object;
	}

	public Recyclable getObject(Class objectClass) {
		_allocations++;
		LinkedList pool = getPool(objectClass);
		Recyclable object = null;
		if (pool.isEmpty())
		{
			object = makeObject(objectClass);
		} else {
			object = ((Recyclable)(pool.removeLast()));
			_pooled--;
			_reuses++;
		}
		object.setRecycler(this);
		return object;
	}
	
	public Recyclable getObject () {
		Recyclable object = getObject(get_superClass());
		return object;
	}

	public void recycle(Recyclable recyclable) {
		Recycler recycler = recyclable.getRecycler();
		if (recycler != this) {
			error("Attempt to recycle " + recyclable + 
					" which belongs to " + recycler + 
					" instead of " + this);
		}
		recyclable.release();
		recyclable.setRecycler(null);
		getPool(recyclable.getClass()).addLast(recyclable);
		_pooled++;
		_recycles++;
		return;
	}
	
	public int getPoolCount(){
		int count = get_pools().size();
		return count;
	}

}

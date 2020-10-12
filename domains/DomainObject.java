/*
 * Created on Sep 27, 2004
 *
 
 */
package domains;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.resonant.xkm.containers.KMSet;
import com.resonant.xkm.kb.KBMemberObject;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.points.PointListener;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.Typed;
import com.resonant.xkm.types.TypedObject;

/**
 * @author MLB
 *
 *
 */
public class DomainObject 
	extends KBMemberObject
	implements Domain, DomainListener, PointListener 
{
	private static int _INITIAL_LISTENER_SET_SIZE	= 2;
	private static int _INITIAL_POINT_LIST_SIZE		= 2;
	
	private Type	_type		= null;
	private List	_points		= null;
//	private Set		_listeners	= null;
	private KMSet	_listeners	= null;

	private Type get_type() {return _type;}
	private void set_type(Type type) {_type = type;}	
	private List get_points() {return _points;}
//	private Set get_listeners() {return _listeners;}
	private KMSet get_listeners() {return _listeners;}

	public DomainObject() {
		super();
	}
	
	public Type getType () {
		return get_type();
	}
	
	protected void setType (Type type) {
		set_type(type);
		return;
	}
	
	public Type checkType (Typed that) {
		Type thisType = get_type();
		Type thatType = that.getType();
		if (thisType != null) {
			TypedObject.checkType(this, that);
		}
		return thatType;
	}

	public void domainChanged(Domain changedDomain) {
//		System.out.println("  Domain " + this + " handling changes from " + changedDomain);
		return;
	}
	
	public boolean addManagedPoint(ManagedPoint point) {
		Type thisType = getType();
		if (thisType == null) {
			setType(point.getType());
		} else {
			checkType(point);
		}
		List points = get_points();
		if (points == null){
			_points = new ArrayList(_INITIAL_POINT_LIST_SIZE);
			points = _points;
		}
		boolean isAdded = !(points.contains(point));
		if (isAdded) {
			points.add(point);
			point.setListener(this);
			point.setManager(getKB());
		}
		return isAdded;
	}

//	public boolean removeManagedPoint(ManagedPoint point) {
//		boolean isRemoved = get_points().remove(point);
//		if (isRemoved) {
//			point.setListener(null);
//		}
//		return isRemoved;
//	}

	public Iterator pointsIterator() {
		Iterator iterator = iterator(get_points());
		return iterator;
	}

	public Iterator listenersIterator() {
		Iterator iterator = iterator(get_listeners());
		return iterator;
	}

	public boolean addDomainListener(DomainListener listener) {
//		Set listeners = get_listeners();
		KMSet listeners = get_listeners();
		if (listeners == null){
//			_listeners = new HashSet(_INITIAL_LISTENER_SET_SIZE);
			_listeners = new KMSet(_INITIAL_LISTENER_SET_SIZE);
			listeners = _listeners;
		}
		boolean isAdded = listeners.add(listener);
		return isAdded;
	}

//	public boolean removeDomainListener(DomainListener listener) {
//		boolean isRemoved = get_listeners().remove(listener);
//		return isRemoved;
//	}

	public void setKB(KBObject kb) {
		super.setKB(kb);
		Iterator it = pointsIterator();
		while (it.hasNext()) {
			((ManagedPoint)(it.next())).setManager(kb);
		}
		return;
	}
	
//	public boolean activate () {
//		boolean isActivating = super.activate();
//		if (isActivating) 
//		{	
//		}
//		Iterator it = listenersIterator();
//		while (it.hasNext()) {
//			DomainListener listener = ((DomainListener)(it.next()));
//			System.out.println("    Activating " + this + ", listener: " + listener);			
//			if (listener instanceof Operation){
//				((Operation)listener).constrainOperands();
//			}
//		}
//		return isActivating;
//	}

//	public void reset() {
//		Iterator it = pointsIterator();
//		while (it.hasNext()) {
//			((ManagedPoint)(it.next())).reset();
//		}
//		return;
//	}
	
	public void reset() {
		{
			Iterator it = pointsIterator();
			while (it.hasNext()) {
				((ManagedPoint)(it.next())).resetIndex();
			}
		}
		{
			Iterator it = pointsIterator();
			while (it.hasNext()) {
				((ManagedPoint)(it.next())).resetSupport();
			}
		}
		return;
	}

	/*
	public Set getAllSupport() {
		Set allSupport = new TreeSet();
		Iterator it = pointsIterator();
		while (it.hasNext()) {
//			allSupport.addAll(((ManagedPoint)(it.next())).getSupport());
			ManagedPoint point = (ManagedPoint)(it.next());
			Set pointSupport = point.getSupport();
			Iterator pit = pointSupport.iterator();
			while (pit.hasNext()) {
				allSupport.add(pit.next());
			}
		}
		return allSupport;
	}
	*/

	public Set getAllSupport() {
//		Set allSupport = new TreeSet();
		Set allSupport = new HashSet();
		Set visited = new HashSet();
		gatherSupport(allSupport, visited);
		return allSupport;
	}
	
	public final void gatherSupport(Set support, Set visited){
		boolean isNew = visited.add(this);
		if (isNew){
			accumulateSupport(support, visited);
		}
		return;
	}
	
	public void accumulateSupport(Set support, Set visited) {
		Iterator it = pointsIterator();
		while (it.hasNext()) {
			ManagedPoint point = (ManagedPoint)(it.next());
//			Set pointSupport = point.getSupport();
			KMSet pointSupport = point.getSupport();
			Iterator pit = pointSupport.iterator();
			while (pit.hasNext()) {
				Object supporter = pit.next();
				boolean isDomain = (supporter instanceof Domain);
				if (isDomain) {
					support.add(supporter);
					((Domain)supporter).gatherSupport(support, visited);
				}
			}
		}
		return;
	}

	public void pointChanged (Point point) {
		onChanged();
		return;
	}
	
	public void onChanged() {
		Iterator it = listenersIterator();
		while (it.hasNext()) {
			((DomainListener)(it.next())).domainChanged(this);
		}
		return;
	}

	public boolean retractThis (Set restoreSet) {
// 		System.out.println("\n" + this + ".retractThis()...");
		boolean suspended = super.retractThis(restoreSet);
		restoreSet.add(this);
		if (suspended) {
			restoreSet.add(this);
			reset();
			Iterator it = listenersIterator();
			while (it.hasNext()) {
				DomainListener listener = (DomainListener)(it.next());
				if (listener instanceof Domain) {
					((Domain) listener).retractSupport(this, restoreSet);
				}
			}
		}
// 		System.out.println("\ndone: " + this + ".retractThis()");
		return suspended;
	}
	
	public void retractSupport (Object object, Set restoreSet) {
// 		System.out.println("\n" + this + ".retractSupport(" + object + ")...");
		boolean retractThis = false;
		Iterator it = pointsIterator();
		while (it.hasNext()) {
			boolean removed = ((ManagedPoint)(it.next())).removeSupport(object);
			if (removed) {
				retractThis = true;
			}
		}
		if (retractThis) {
			retractThis(restoreSet);
		} 
// 		System.out.println("\ndone: " + this + ".retractSupport(" + object + ")...");
		return;
	}
	
//	public boolean basisReset(Set restoreSet) {
//		boolean isAdded = super.basisReset(restoreSet);
//		if (isAdded) {
//			reset();
//			Iterator it = get_listeners().iterator();
//			while (it.hasNext()) {
//				DomainListener listener = (DomainListener)(it.next());
//				if (listener instanceof KBMemberObject) {
//					((KBMemberObject) listener).basisReset(restoreSet);
//				}
//			}			
//		}
//		return isAdded;
//	}

	public Set onBasisReset(Set restoreSet) {
		restoreSet = super.onBasisReset(restoreSet);
		if (restoreSet != null) {
			reset();
			Iterator it = listenersIterator();
			while (it.hasNext()) {
				DomainListener listener = (DomainListener)(it.next());
				if (listener instanceof KBMemberObject) {
					((KBMemberObject) listener).basisReset(restoreSet);
				}
			}			
		}
		return restoreSet;
	}

	public boolean basisRestore(Set restoreSet) {
		boolean contains = super.basisRestore(restoreSet);;
		if (contains) {
			Iterator it = listenersIterator();
			while (it.hasNext()) {
				DomainListener listener = (DomainListener)(it.next());
				if (listener instanceof KBMemberObject) {
					((KBMemberObject) listener).basisRestore(restoreSet);
				}
			}			
		}
		return contains;
	}

	public String toStringBody () {
		List points = get_points();
		if (points == null){
			points = Collections.EMPTY_LIST;
		}
		String string = points.toString();
		return string;
	}
	
//	public String toString(){
//		String string = super.toString() + "{";
//		boolean first = true;
//		Iterator it = listenersIterator();
//		while (it.hasNext()){
//			if (first) {
//				first = false;
//			} else {
//				string = string + ",";
//			}
//			string = string + getName(it.next()); 	
//		}
//		string = string + "}";
//		return string;
//	}

	
}

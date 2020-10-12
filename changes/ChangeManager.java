/*
 * Created on Sep 17, 2004
 *
 
 */
package changes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import km.KMObject;
import points.Point;
import recycling.RecyclableFactory;
import recycling.RecyclableObject;

/**
 * @author MLB
 *
 *
 */
public class ChangeManager 
	extends KMObject 
{	
	LinkedList			_queue			= new LinkedList();
	RecyclableFactory	_factory		= new RecyclableFactory(Change.class);
	Point				_latchPoint		= null;
	Object				_latchObject	= null;
	
	private LinkedList get_queue() {return _queue;}
	private RecyclableFactory get_factory() {return _factory;}
	private Point get_latchPoint() {return _latchPoint;}
	private void set_latchPoint(Point point) {_latchPoint = point;}
	private Object get_latchObject() {return _latchObject;}
	private void set_latchObject(Object object) {_latchObject = object;}
	
	public ChangeManager() {super();}
	
	public Change getChange (Class changeClass) {
		RecyclableFactory factory = get_factory();
		Change change = (Change)(factory.getObject(changeClass));
		addChange(change);
		return change;
	}

	public void addChange(Change change) {
		get_queue().addLast(change);
		return;
	}

	public Change lastChange() {
		Change change = null;
		if (!get_queue().isEmpty()) {
			change = (Change)(get_queue().getLast());
		}
		return change;
	}
	
	public boolean isChanged () {
		boolean isChanged = !(get_queue().isEmpty());
		return isChanged;
	}
	
	public void checkChanged () {
		if (isChanged()) {
			signal("Must commit pending changes before proceeding");
		}
		return;
	}
	
	public void recycleChange(Change change){
		RecyclableObject.recycle(change);
		return;
	}

	/*
	 * "commit: to place officially in confinement or custody, as in a mental health facility."
	 * 	--The American Heritage� Stedman's Medical Dictionary
	 */

	public void commitAll() {
		commitTo(null);
		return;
	}

	public void commitTo(Change change) {
		LinkedList queue = get_queue();
		while (!queue.isEmpty()) {
			Change first = (Change)(queue.getFirst());
			if (first == change) break;
			queue.removeFirst();
			recycleChange(first);
		}
		return;
	}

	public void undoAll() {
		undoTo(null);
		return;
	}

	public void undoTo(Change change) {
		LinkedList queue = get_queue();
		while (!queue.isEmpty()) {
			Change last = ((Change)queue.getLast());
			if (last == change) break;
			queue.removeLast();
			last.undo();
			recycleChange(last);
		}
		return;
	}

	public Object getLatchObject() {
		Object object = get_latchObject();
		return object;
	}
	
	public Point readAndClearLatch() {
		Point point = get_latchPoint();
		set_latchPoint(null);
		set_latchObject(null);
		return point;
	}
		
	public boolean latchPoint(Point point) {
		Point	latchPoint = get_latchPoint();
		if (latchPoint == null) {
			set_latchPoint(point);
			set_latchObject(null);
			latchPoint = point;
		}
		boolean isLatched = (point == latchPoint);
		return isLatched;
	}
	
	public boolean latchPoint(Point point, Object object) {
		boolean isLatched = latchPoint(point);
		if (isLatched) {
			set_latchObject(object);
		}
		return isLatched;
	}

	public Iterator changeIterator () {
		Iterator it = get_queue().iterator();
		return it;
	}
	
	public List getQueue() {
		return get_queue();
	}
	
	public int getChangeAllocations(){
		int count = get_factory().getAllocations();
		return count;
	}
	
	public int getChangeReuses(){
		int count = get_factory().getReuses();
		return count;
	}

	public int getChangesCreated(){
		int count = get_factory().getCreated();
		return count;
	}
	
	public int getChangesPooled(){
		int count = get_factory().getPooled();
		return count;
	}

	public int getChangeRecycles(){
		int count = get_factory().getRecycles();
		return count;
	}

	public int getChangePools(){
		int count = get_factory().getPoolCount();
		return count;
	}

}

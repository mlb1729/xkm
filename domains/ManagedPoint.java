/*
 * Created on Sep 20, 2004
 *
 
 */
package domains;

import com.resonant.xkm.changes.ChangeManager;
import com.resonant.xkm.containers.KMSet;
import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.km.KMObject;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.points.PointListener;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.Typed;

/**
 * @author MLB
 *
 *
 */
public class ManagedPoint 
	extends KMObject 
	implements Point 
{
	private static int _INITIAL_SUPPORT_SET_SIZE	= 2;

	private Point			_point		= null;
	private PointListener	_listener	= null;
	private Point			_origin		= null;
//	private Set				_support	= null;
	private KMSet			_support	= null;
	private ChangeManager	_manager;

	private ChangeManager get_manager() {return _manager;}
	private void set_manager(ChangeManager manager) {_manager = manager;}
	private Point get_point() {return _point;}
	private void set_point(Point point) {_point = point;}
	private PointListener get_listener() {return _listener;}
	private void set_listener(PointListener listener) {_listener = listener;}
	private Point get_origin() {return _origin;}
	private void set_origin(Point origin) {_origin = origin;}
//	private Set get_support() {return _support;}
//	private void set_support(Set support) {_support = support;}
	private KMSet get_support() {return _support;}
	private void set_support(KMSet support) {_support = support;}

	protected ManagedPoint() {super();}
	
	public ManagedPoint(ChangeManager manager) {
		this();
		set_manager(manager);
	}
	
	protected ChangeManager getManager() {
		ChangeManager manager = get_manager();
		if (manager == null) {
			error("Attempt to access null manager of " + this);
		}
		return manager;
	}

	protected void setManager(ChangeManager changeManager) {
//		if ((changeManager == null) && (get_manager() != null)) {
//			error("Attempt to remove manager of " + this);
//		}
		 set_manager(changeManager);
		return;
	}
	
	protected ChangeManagedPoint getChange (Class changeClass) {
		ChangeManagedPoint change = ((ChangeManagedPoint)getManager().getChange(changeClass));
		change.initPoint(this);
		return change;
	}

	public Point getPoint () {
		Point point = get_point();
		if (point == null) {
			error("Attempt to access managed null point " + this);
		}
		return point;
	}
	
	public Point getOrigin () {
		Point origin = get_origin();
		if (origin == null) {
			error("Attempt to access managed null point " + this);
		}
		return origin;
	}
	
	public void setPoint (Point point) {
		set_point(point);
		set_origin(point.getFixedPoint());
		return;
	}
	
	public PointListener getListener () {
		return get_listener();
	}

	public void setListener (PointListener listener) {
		set_listener(listener);
		return;
	}

	public boolean isFixed() {return getPoint().isFixed();}
	public int getIndex() {return getPoint().getIndex();}
	public void setIndex(int index) {getPoint().setIndex(index); return;}
	public boolean checkChange(int index) {return getPoint().checkChange(index);}
	public Point getFixedPoint() {return getPoint().getFixedPoint();}
	public Expression getFixedExpression() {return getPoint().getFixedExpression();}
	public Type getType() {return getPoint().getType();}
	public Type checkType(Typed that) {return getPoint().checkType(that);}
//	public int compareTo(Object object) {return getPoint().compareTo(object);}
	public boolean equals(Object object) {return getPoint().equals(object);	}
	public int hashCode() {return getPoint().hashCode();}
	public String toString() {return getPoint().toString();}

	public Object getValueObject() {
		Object object = getType().getObject(getIndex());
		return object;
	}

//	public Set getSupport() {
	public KMSet getSupport() {
//		Set support = get_support();
		KMSet support = get_support();
		if (support == null) {
//			support = new HashSet(_INITIAL_SUPPORT_SET_SIZE);
//			support = new TreeSet();
			support = new KMSet(_INITIAL_SUPPORT_SET_SIZE);
//			support.add(getOrigin());
			set_support(support);
		}
		return support;
	}

	public boolean inSupport(Object object) {
//		Set support = get_support();
		KMSet support = get_support();
		boolean inSupport = ((support == null) ?
								(object == getOrigin()) :
								getSupport().contains(object));		
		return inSupport;
	}

	public boolean addSupport (Object object) {
		boolean isAdded = !inSupport(object);
		if (isAdded) {		
			ChangeAddSupport change = ((ChangeAddSupport)(getChange(ChangeAddSupport.class)));
			change.initObject(object);
			getSupport().add(object);	
//			System.out.println("Adding " + object + " to " + toStringBody());
		}
		return isAdded;
	}
	
	public boolean removeSupport(Object object) {
		boolean isReset = false;
		Point origin = getOrigin();
		if (object != origin) {
//			Set support = get_support();
			KMSet support = get_support();
			if (support != null) {
				support.remove(object);
				isReset = (support.isEmpty() ||
							((support.size() == 1) && support.contains(origin)));
				if (isReset) {
					reset();
				}
			}
		}
		return isReset;
	}

	protected void resetSupport() {
//		Set support = getSupport();
		KMSet support = getSupport();
		if (support != null)
		{
			support.clear();
//			support.add(getOrigin());
		}
		return;
	}

	public void resetIndex() {
		getPoint().setIndex(getOrigin().getIndex());
		return;
	}

	public void reset() {
		resetIndex();
		resetSupport();
		return;
	}

	public boolean changeIndex (int index, Object support) {
		boolean isChanged = false;
		try {
			isChanged = checkChange(index);
		} catch (Contradiction c) {
			getManager().latchPoint(this, c);
			throw c;
		}
		PointListener listener = getListener();
		try {
			setListener(null);
			if (isChanged) {
				getChange(ChangeIndex.class);
				getPoint().changeIndex(index);
				getChange(ChangeResetSupport.class);
				resetSupport();
			}
			addSupport(support);
		} finally {
			setListener(listener);
		}
		if (isChanged) {
			onChange();
		}
		return isChanged;
	}

	public boolean changeIndex (int index) {
		return changeIndex(index, this);
	}
	
	protected void onChange () {
		PointListener listener = getListener();
		if (listener != null) {
			listener.pointChanged(this);
		}
		return;
	}
	
	public String toStringBody () {
		String string = getPoint().toString();
//		Set support = get_support();
		KMSet support = get_support();
		if (support != null)
		{
			string = string + support;
		}
		return string;
	}

}

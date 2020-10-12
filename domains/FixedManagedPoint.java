/*
 * Created on Sep 24, 2004
 *
 
 */
package domains;

import com.resonant.xkm.changes.ChangeManager;
import com.resonant.xkm.containers.KMSet;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.points.PointListener;

/**
 * @author MLB
 *
 *
 */
public final class FixedManagedPoint 
	extends ManagedPoint 
{
	protected FixedManagedPoint() {
		super(null);
	}

	public FixedManagedPoint (Point point) {
		this();
		super.setPoint(point.getFixedPoint());
	}
	
	protected void setManager(ChangeManager changeManager) {
		 return;
	}

	public void setPoint (Point point) {
		return;
	}
	
	public PointListener getListener () {
		return null;
	}

	public void setListener (PointListener listener) {
		return;
	}
		
	public boolean isFixed() {return true;}
	public Point getFixedPoint() {return getPoint();}

//	public Set getSupport() {
	public KMSet getSupport() {
//		Set support = super.getSupport();
		KMSet support = super.getSupport();
		return support;
	}

	public boolean addSupport (Object object) {
		return false;
	}
	
	public boolean removeSupport(Object object) {
		return false;
	}

	public Object getLabel() {
		return null;
	}

	protected void resetSupport() {
		return;
	}

	public void setLabel(Object label) {
		if (label != null) {
			error("Can't change label of " + this + " to non-null " + label);
		}
		return;
	}
	
	public void reset() {
		return;
	}
	
	public boolean changeLabel(Object label) {
		setLabel(label);
		return false;
	}
	
	protected void onChange () {
		return;
	}
	
}

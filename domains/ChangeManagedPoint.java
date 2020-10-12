/*
 * Created on Sep 27, 2004
 *
 
 */
package domains;

import changes.Change;
import recycling.RecyclableObject;

/**
 * @author MLB
 *
 *
 */
public class ChangeManagedPoint 
	extends RecyclableObject 
	implements Change 
{
	private ManagedPoint _point	= null;
	
	private ManagedPoint get_point() {return _point;}
	private void set_point(ManagedPoint point) {_point = point;}
	
	public ChangeManagedPoint () {super();}
	
	public void initPoint(ManagedPoint point) {
		set_point(point);
		return;
	}
	
	public ManagedPoint getPoint () {
		return get_point();
	}
	
	public void undo() {
		return;
	}

	public void release() {
		super.release();
		set_point(null);
		return;
	}

}

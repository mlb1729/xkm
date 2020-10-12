/*
 * Created on Sep 27, 2004
 *
 
 */
package domains;

import java.util.ArrayList;

import com.resonant.xkm.containers.KMSet;

/**
 * @author MLB
 *
 *
 */
public class ChangeResetSupport 
	extends ChangeManagedPoint 
{
	private final ArrayList	_array = new ArrayList(); 	

	private ArrayList get_array() {return _array;}
	
	public ChangeResetSupport() {super();}
	
	public void initPoint(ManagedPoint point) {
		super.initPoint(point);
		ArrayList array = get_array();
		array.clear();
		array.addAll(point.getSupport().getList());
		return;
	}
	
	public void undo() {
//		Set support = getPoint().getSupport();
		KMSet support = getPoint().getSupport();
		support.clear();
		support.addAll(get_array());
		return;
	}
	
	public void release () {
		super.release();
		ArrayList array = get_array();
		array.clear();
		return;
	}
}

/*
 * Created on Sep 5, 2004
 *
 
 */
package points;

import types.Type;



/**
 * @author MLB
 *
 *
 */
public class DynamicPoint
	extends PointObject 
{
	public DynamicPoint () {super();}
	
	public DynamicPoint(Type type, int initialIndex) {
		super(type, initialIndex);
	}
	
	public DynamicPoint(Type type, Object object) {
		this(type, type.index(object));
	}
		
	public DynamicPoint(Point point) {
		this(point.getType(), point.getIndex());
	}
		
	public DynamicPoint(int index) {
		this(Type.INTEGER, index);
	}
	
	public boolean isFixed () {
		return false;
	}
	
	public boolean checkChange(int index) {
		boolean isChanged = (index != getIndex());
		return isChanged;
	}

}
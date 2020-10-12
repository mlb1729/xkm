/*
 * Created on Sep 5, 2004
 *
 
 */
package points;


import bounds.Bounds;


/**
 * @author MLB
 *
 *
 */
public class IncreasingPoint 
	extends BoundedPoint 
{
	public IncreasingPoint() {super();}
	
	public IncreasingPoint(Bounds bounds) {
		super(bounds.getMinPoint().getIndex(), bounds);
	}
	
	public boolean checkChange (int index) {
		boolean isChanged = (super.checkChange(index) && (index > getIndex()));
		return isChanged;
	}
}

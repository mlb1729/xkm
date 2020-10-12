/*
 * Created on Sep 5, 2004
 *
 
 */
package points;

import com.resonant.xkm.bounds.Bounds;


/**
 * @author MLB
 *
 *
 */
public class DecreasingPoint
	extends BoundedPoint 
{

	public DecreasingPoint() {super();}
	
	public DecreasingPoint(Bounds bounds) {
		super(bounds.getMaxPoint().getIndex(), bounds);
	}
	
	public boolean checkChange (int index) {
		boolean isChanged = (super.checkChange(index) && (index < getIndex()));
		return isChanged;
	}

}

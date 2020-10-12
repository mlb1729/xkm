/*
 * Created on Sep 6, 2004
 *
 
 */
package bounds;

import points.Point;

/**
 * @author MLB
 *
 *
 */
public final class FixedBounds 
	extends BoundsObject 
{
	public FixedBounds() {super();}
	
	public FixedBounds (Point minBound, Point maxBound) {
		super(minBound.getFixedPoint(), maxBound.getFixedPoint());
		getType().addFixedBounds(this);
	}

	public FixedBounds (Bounds bounds) {
		this(bounds.getMinPoint(), bounds.getMaxPoint());
	}

	public boolean isFixed() {
		return true;
	}
	
	public void setMinPoint (Point minPoint) {
		return;
	}

	public void setMaxPoint (Point maxPoint) {
		return;
	}
	
//	public void writeExternal(ObjectOutput stream) 
//	throws IOException {
//		super.writeExternal(stream);
//		return;
//	}
//
//	public void readExternal(ObjectInput stream) 
//	throws ClassNotFoundException, IOException {
//		super.readExternal(stream);
//		return;
//	}
	
}

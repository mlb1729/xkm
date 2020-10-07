/*
 * Created on Sep 7, 2004
 *
 
 */
package com.resonant.xkm.bounds;

import com.resonant.xkm.points.DecreasingPoint;
import com.resonant.xkm.points.IncreasingPoint;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public final class CollidingBounds
	extends BoundsObject 
{
	public CollidingBounds() {super();}
	
	public CollidingBounds (Point minPoint, Point maxPoint) {
		super(minPoint, maxPoint);
		Point minLimit = getMinPoint().getFixedPoint();
		Point maxLimit = getMaxPoint().getFixedPoint();
		BoundsObject increasingBounds = new BoundsObject(minLimit, maxLimit);
		BoundsObject decreasingBounds = new BoundsObject(minLimit, maxLimit);
		IncreasingPoint increasingPoint = new IncreasingPoint (increasingBounds);
		DecreasingPoint decreasingPoint = new DecreasingPoint (decreasingBounds);
		increasingBounds.setMaxPoint(decreasingPoint);
		decreasingBounds.setMinPoint(increasingPoint);
		setMinPoint(increasingPoint);
		setMaxPoint(decreasingPoint);
	}

	public CollidingBounds (Bounds bounds) {
		this(bounds.getMinPoint(), bounds.getMaxPoint());
	}
	
	public CollidingBounds (Type type) {
		this(type.getFixedBounds());
	}

	public boolean isFixed() {
		return false;
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
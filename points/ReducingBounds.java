/*
 * Created on Sep 6, 2004
 *
 
 */
package com.resonant.xkm.points;

import com.resonant.xkm.bounds.Bounds;
import com.resonant.xkm.bounds.BoundsObject;

/**
 * @author MLB
 *
 *
 */
public final class ReducingBounds 
	extends BoundsObject 
{
	public ReducingBounds() {super();}
	
	public ReducingBounds(Point minPoint, Point maxPoint) {
		super(minPoint, maxPoint);
		Point minLimit = getMinPoint().getFixedPoint();
		Point maxLimit = getMaxPoint().getFixedPoint();
		BoundsObject bounds = new BoundsObject(minLimit, maxLimit);
		IncreasingPoint increasingPoint = new IncreasingPoint(bounds);
		DecreasingPoint decreasingPoint = new DecreasingPoint(bounds);
		setMinPoint(increasingPoint);
		setMaxPoint(decreasingPoint);
	}

	public ReducingBounds (Bounds bounds) {
		this(bounds.getMinPoint(), bounds.getMaxPoint());
	}

	public boolean isFixed() {
		return false;
	}

}

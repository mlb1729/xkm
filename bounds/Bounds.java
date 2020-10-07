/*
 * Created on Sep 5, 2004
 *
 
 */
package com.resonant.xkm.bounds;

import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Typed;

/**
 * @author MLB
 *
 *
 */
public interface Bounds 
//	extends Typed, Comparable
	extends Typed
{
	Point 		getMinPoint 	();
	Point 		getMaxPoint 	();
	boolean		contains		(int index);
	boolean		isPoint			();
	boolean		isEmpty			();
	boolean		isFixed			();
	boolean		isKnownToBe 	(boolean bool);
	boolean		isKnownToBe 	(int index);
	Bounds		getFixedBounds	();
	Object		getPointObject	();
}

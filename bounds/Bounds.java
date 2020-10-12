/*
 * Created on Sep 5, 2004
 *
 
 */
package bounds;

import points.Point;
import types.Typed;

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

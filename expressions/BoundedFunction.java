/*
 * Created on Jul 18, 2005
 *
 
 */
package expressions;

import bounds.Bounds;
import domains.ManagedPoint;
import exceptions.Contradiction;
import points.Point;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class BoundedFunction 
	extends BoundedOperation 
{
	private static final Contradiction _contradiction = 
		new Contradiction("Can't change the value of a function.");

	public BoundedFunction() {super();}
	public BoundedFunction(Object name) {super(name);}
	public BoundedFunction(Type type) {super(type);}
	public BoundedFunction(Bounds bounds) {super(bounds);}
	public BoundedFunction(Point point) {super(point);}
	
	protected boolean changePoint(ManagedPoint point, int index, Object support) {
		if (support != this) {
			signal(_contradiction);
		}
		boolean isChanged = super.changePoint(point, index, support); 
		return isChanged;
	}
	
	public final void constrainOperands () {
		return;	
	}

}

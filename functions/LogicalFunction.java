/*
 * Created on Oct 5, 2004
 *
 
 */
package functions;

import expressions.BoundedFunction;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class LogicalFunction 
	extends BoundedFunction 
{
	public LogicalFunction() {super(); initBounds(Type.BOOLEAN);}

	public LogicalFunction(Object name) {super(name); initBounds(Type.BOOLEAN);}
}

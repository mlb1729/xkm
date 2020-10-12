/*
 * Created on Oct 5, 2004
 *
 
 */
package operations;

import expressions.BoundedOperation;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class LogicalOperation 
	extends BoundedOperation 
{
	public LogicalOperation() {super(); initBounds(Type.BOOLEAN);}
	public LogicalOperation(Object name) {super(name); initBounds(Type.BOOLEAN);}
}

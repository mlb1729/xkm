/*
 * Created on Oct 5, 2004
 *
 
 */
package operations;

import com.resonant.xkm.expressions.BoundedOperation;
import com.resonant.xkm.types.Type;

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

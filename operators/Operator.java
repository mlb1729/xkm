/*
 * Created on Sep 22, 2004
 *
 
 */
package operators;

import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.km.Named;

/**
 * @author MLB
 *
 *
 */
public interface Operator 
	extends Named
{
	Class		getExpressionClass 	();
	List		coercedArguments	(List operands);
	Expression	makeExpression		(List operands);
	Expression	makeExpression		(List operands, Object reason);
	String		toStringOperands	(List operands);
	String		toStringOperands	(Iterator iterator);
}

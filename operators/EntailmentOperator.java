/*
 * Created on Oct 5, 2004
 *
 
 */
package operators;

import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.entailments.Entailment;
import com.resonant.xkm.expressions.Expression;

/**
 * @author MLB
 *
 *
 */
public class EntailmentOperator 
	extends OperatorObject 
{
	public EntailmentOperator(){this("");}
	
	public EntailmentOperator(Object name) {
		this(getName(name), Entailment.class);
	}
	
	public EntailmentOperator(Object name, Class objectClass) {
		super(getName(name), objectClass);
	}
	
	public String toStringOperands(List operands) {
		return "";
	}
	
	public String toStringOperands(Iterator iterator) {
		return "";
	}
	
	public Expression newExpression (List operands, Context context) {
		Expression expression = makeExpression(operands);
		((Entailment)expression).initContext(context);
		return expression;
	}

}

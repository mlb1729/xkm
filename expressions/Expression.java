/*
 * Created on Sep 23, 2004
 *
 
 */
package expressions;

import java.util.List;

import domains.Domain;
import operators.Operator;

/**
 * @author MLB
 *
 *
 */
public interface Expression
	extends Domain
{
	Operator	getOperator			();
	List		getOperands			();
	Object 		getReason			();
	void		initOperator		(Operator operator);
	void		initOperands		(List operands);
	void		initReason			(Object reason);
	void 		constrain			(Domain changedDomain);
	String 		toStringValue		();
}
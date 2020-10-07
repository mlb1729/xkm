/*
 * Created on Sep 23, 2004
 *
 
 */
package com.resonant.xkm.expressions;

import java.util.List;

import com.resonant.xkm.domains.Domain;
import com.resonant.xkm.operators.Operator;

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
/*
 * Created on Jun 27, 2005
 *
 
 */
package com.resonant.xkm.operations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.expressions.BoundedOperation;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.expressions.SetObject;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 *
 */
public class SetEqual 
	extends SetAnd 
{
	public SetEqual() {super();}
	public SetEqual(Object name) {super(name);}
	
	public List coercedOperands (List members) {
		SetObject superSet = coercedSet(members.get(0));
		SetObject subSet = coercedSet(members.get(1));
		List coercedOperands = new ArrayList();
		Expression term = null;
		List termOperands = new ArrayList();
		// Iterator subIterator = subSet.getOperands().iterator();
		Iterator subIterator = subSet.getMemberBooleans().iterator();
		while (subIterator.hasNext()) {
			BoundedOperation subElement = ((BoundedOperation)subIterator.next());
			BoundedOperation superElement = superSet.getMember(subElement.getName());
			termOperands.clear();
			termOperands.add(subElement);
			if (superElement == null){
				term = RootOperator.Not.makeExpression(termOperands);				
			} else {
				termOperands.add(superElement);
				term = RootOperator.Equal.makeExpression(termOperands);
			}
			coercedOperands.add(term);
		}
		// Iterator superIterator = superSet.getOperands().iterator();
		Iterator superIterator = subSet.getMemberBooleans().iterator();
		while (superIterator.hasNext()) {
			BoundedOperation superElement = ((BoundedOperation)superIterator.next());
			BoundedOperation subElement = subSet.getMember(superElement.getName());
			if (subElement == null){
				termOperands.clear();
				termOperands.add(superElement);
				term = RootOperator.Not.makeExpression(termOperands);				
				coercedOperands.add(term);
			}
		}
		return coercedOperands;
	}
}

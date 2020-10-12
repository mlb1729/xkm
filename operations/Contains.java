/*
 * Created on Jun 27, 2005
 *
 
 */
package operations;

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
public class Contains 
	extends SetAnd 
{
	public Contains() {super();}
	public Contains(Object name) {super(name);}
	
	public List coercedOperands (List members) {
		SetObject superSet = coercedSet(members.get(0));
		SetObject subSet = coercedSet(members.get(1));
		List coercedOperands = new ArrayList();
// 		Iterator it = subSet.getOperands().iterator();
		Iterator it = subSet.getMemberBooleans().iterator();
		Expression term = null;
		List termOperands = new ArrayList();
		while (it.hasNext()) {
			BoundedOperation subElement = ((BoundedOperation)it.next());
			Object name = subElement.getName();
			BoundedOperation superElement = superSet.getMember(name);
			termOperands.clear();
			if (superElement == null){
				termOperands.add(subElement);
				term = RootOperator.Not.makeExpression(termOperands);				
			} else {
				termOperands.add(subElement);
				termOperands.add(superElement);
				term = RootOperator.Implies.makeExpression(termOperands);
			}
			coercedOperands.add(term);
		}
		return coercedOperands;
	}
}

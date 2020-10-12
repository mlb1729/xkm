/*
 * Created on Sep 5, 2004
 *
 
 */
package points;

import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.types.Typed;

/**
 * @author MLB
 *
 *
 */
public interface Point 
//	extends Typed, Comparable
	extends Typed
{
	boolean		isFixed				();
	int			getIndex			();
	void		setIndex			(int index);
	Object		getValueObject		();
	boolean		checkChange			(int index);
	boolean		changeIndex			(int index);
	Point		getFixedPoint		();
	Expression	getFixedExpression	();
}

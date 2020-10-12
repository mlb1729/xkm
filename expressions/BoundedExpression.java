/*
 * Created on Sep 27, 2004
 *
 
 */
package expressions;

import api.Interval;
import bounds.Bounds;

/**
 * @author MLB
 *
 *
 */
public interface BoundedExpression
	extends Expression, Bounds
{
	int getMaxIndex();
	int getMinIndex();
	boolean changeMinIndex(int index, Object support);
	boolean changeMinIndex(int index);
	boolean changeMaxIndex(int index, Object support);
	boolean changeMaxIndex(int index);
	boolean changeToBe(boolean bool, Object support);
	boolean changeToBe(boolean bool);
	boolean changeToBe(int index, Object support);
	boolean changeToBe(int index);
	
	Interval getInterval();
}

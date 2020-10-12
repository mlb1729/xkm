/*
 * Created on Mar 31, 2005
 *
 
 */
package operations;

import java.util.List;

import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class IntScale 
	extends ArithmeticOperation 
{
	public IntScale() {super(Type.INTEGER);}
	
	public int divToMinf(int n, int d){
		int q = n/d;
		int r = n - q*d;
		if (r<0){
			q--;
		}
		return q;
	}

	public void constrainOperands () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression x = (BoundedExpression)(operands.get(1));
		int aVal = a.getMinIndex();
		int yMin = getMinIndex();
		int yMax = getMaxIndex();
		if (aVal < 0) {
			int temp = yMin;
			yMin = yMax;
			yMax = temp;
		}
		int xMin = -divToMinf(-yMin, aVal);
		int xMax = divToMinf(yMax , aVal);
		x.changeMinIndex(xMin);
		x.changeMaxIndex(xMax);
		return;	
	}

	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression x = (BoundedExpression)(operands.get(1));
		int aVal = a.getMinIndex();
		int xMin = x.getMinIndex();
		int xMax = x.getMaxIndex();
		int yMin = aVal * xMin;
		int yMax = aVal * xMax;
		if (aVal < 0) {
			int temp = yMin;
			yMin = yMax;
			yMax = temp;
		}
		changeMinIndex(yMin);
		changeMaxIndex(yMax);
		return;	
	}

}

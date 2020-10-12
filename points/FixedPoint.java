/*
 * Created on Sep 5, 2004
 *
 
 */
package points;

import expressions.BoundedOperation;
import expressions.Expression;
import types.Type;



/**
 * @author MLB
 *
 *
 */
public final class FixedPoint 
	extends PointObject
{
	private Expression _fixedExpression = null;
	
	private Expression get_fixedExpression() {return _fixedExpression;}
	
	public boolean isFixed () {return true;}
	
	public FixedPoint() {super();}
	
	public FixedPoint(Type type, int index) {
		super(type, index);
	}
		
	public FixedPoint(Type type, Object object) {
		this(type, type.index(object));
	}
		
	public FixedPoint(Point point) {
		this(point.getType(), point.getIndex());
	}
		
	public FixedPoint(int index) {
		this(Type.INTEGER, index);
	}
	
	public Point getFixedPoint () {
		return this;
	}
	
	public Expression getFixedExpression () {
		Expression expression = get_fixedExpression();
		if (expression == null) {
			expression = new BoundedOperation(this);
		}
		return expression;
	}

}

/*
 * Created on Sep 7, 2004
 *
 
 */
package points;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.expressions.Expression;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.TypedObject;

/**
 * @author MLB
 *
 *
 */
public abstract class PointObject 
	extends TypedObject 
	implements Point
{
	private static final Contradiction _changeContradiction = new Contradiction("Contradictory index change");
	
	private int	_index;

	private int get_index() {return _index;}
	private void set_index(int index) {_index = index;}

	public PointObject() {super();}
	
	protected PointObject(Type type, int index) {
		super(type);
		set_index(index); 
		if (isFixed()){
			type.addFixedPoint(this);
		}
	}
		
	protected PointObject(Type type, Object object) {
		this(type, type.index(object));
	}
		
	protected PointObject(Point point) {
		this(point.getType(), point.getIndex());
	}
		
	protected PointObject(int index) {
		this(Type.INTEGER, index);
	}
	
	public abstract boolean isFixed ();
	
	public Point getFixedPoint () {
		Point fixedPoint = (isFixed() ? 
							this :
							getType().getFixedPoint(this));
		return fixedPoint;
	}

	public int getIndex() {
		return get_index();
	}

	public void setIndex(int index) {
		checkChange(index);
		set_index(index);
		return;
	}
	
	public Object getValueObject() {
		Object object = getType().getObject(getIndex());
		return object;
	}

	public void throwChangeContradiction (int index) {
		signal(_changeContradiction);		
		// throw (_changeContradiction);		
	}
	
	public boolean checkChange(int index) {
		if ((index != getIndex()) && isFixed())
		{
			throwChangeContradiction(index);
		} 
		return false;
	}
	
	public boolean changeIndex(int index) {
		boolean isChanged = checkChange(index);
		if (isChanged) {
			setIndex(index);
		}
		return isChanged;
	}

	public Expression getFixedExpression () {
		Expression expression = ((FixedPoint)getFixedPoint()).getFixedExpression(); 
		return expression;
	}
	
//	public boolean equals(Object object) {
//		boolean isEqual = super.equals(object);
//		if (!isEqual && (object instanceof Point)) {
//			Point point = (Point)object;
//			isEqual = ((getIndex() == point.getIndex()) &&
//						(getType() == point.getType()));
//		}
//		return isEqual;
//	}
//	
//	public int hashCode () {
//		int hashCode = getType().hashCode(getIndex()); 
//		return hashCode;
//	}
//	
//	public int compareTo(Object that) {
//		int comparison = 0;
//		if (compareMore(that, Point.class)) {
//			Point point = (Point)that;
//			if  (getType() == point.getType()) {
//				int thisIndex = getIndex();
//				int thatIndex = point.getIndex(); 
//				comparison = ((thisIndex <  thatIndex) ? 
//								-1 : ((thisIndex == thatIndex) ? 0 : 1));
//			}
//		} else {
////			throw new ClassCastException(noComparisonString(that));
//			comparison = ((this == that) ? 0 : 1);
//		}
//		return comparison;
//	}

	public String toString () {
		String string = getType().toString(getIndex());
		return string;
	}

	public void writeExternalInternal(ObjectOutput stream) 
	throws IOException {
		super.writeExternalInternal(stream);
		stream.writeInt(get_index());
		if (isFixed()){
			return;
		}
		return;
	}

	public void readExternalInternal(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
		super.readExternalInternal(stream);
		set_index(stream.readInt());
		return;
	}
	
}

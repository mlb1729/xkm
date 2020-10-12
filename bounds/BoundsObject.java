/*
 * Created on Sep 5, 2004
 *
 
 */
package bounds;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.TypedObject;
import com.resonant.xkm.types.Type.BOOLEAN;

/**
 * @author MLB
 *
 *
 */
public class BoundsObject
	extends TypedObject 
	implements Bounds
{
	private	Point	_minPoint;
	private	Point	_maxPoint;

	private Point get_maxPoint() {return _maxPoint;}
	private void set_maxPoint(Point point) {_maxPoint = point;}
	private Point get_minPoint() {return _minPoint;}
	private void set_minPoint(Point point) {_minPoint = point;}

	public BoundsObject () {super();}
	
	public BoundsObject (Point minPoint, Point maxPoint) {
		super(minPoint.checkType(maxPoint));
		set_minPoint(minPoint);
		set_maxPoint(maxPoint);
	}
	
	public BoundsObject (Bounds bounds) {
		this(bounds.getMinPoint(), bounds.getMaxPoint());
	}
	
	public BoundsObject (Type type) {
		this(type.getFixedBounds());
	}

	public Point getMinPoint() {
		return get_minPoint();
	}

	public void setMinPoint(Point minPoint) {
		getMaxPoint().checkType(minPoint);
		set_minPoint(minPoint);
		return;
	}

	public Point getMaxPoint() {
		return get_maxPoint();
	}

	public void setMaxPoint(Point maxPoint) {
		getMinPoint().checkType(maxPoint);
		set_maxPoint(maxPoint);
		return;
	}
	
	public Object getPointObject() {
		Object object = (isPoint() ? getMinPoint().getValueObject() : null);
		return object;
	}

	public boolean contains(int index) {
		boolean contains = ((getMinPoint().getIndex() <= index) &&
							(getMaxPoint().getIndex() >= index));
		return contains;
	}

	public boolean isPoint() {
		boolean isPoint = (getMinPoint().getIndex() == 
						   getMaxPoint().getIndex());
		return isPoint;
	}

	public boolean isEmpty() {
		boolean isEmpty = (getMinPoint().getIndex() > 
						   getMaxPoint().getIndex());
		return isEmpty;
	}

	public boolean isFixed() {
		boolean isFixed = (getMinPoint().isFixed() && 
			   			   getMaxPoint().isFixed());
		return isFixed;
	}

	public boolean isKnownToBe (boolean bool) {
		boolean isKnownToBe = !contains(BOOLEAN.toIndex(!bool));
		return isKnownToBe;
	}
	
	public boolean isKnownToBe (int index) {
		boolean isKnownToBe = isPoint() && contains(index);
		return isKnownToBe;
	}
	
	public Bounds getFixedBounds () {
		Bounds fixedBounds = this;
		if (!isFixed()) {
			getType().getFixedBounds(this);
		}
		return fixedBounds;
	}

//	public boolean equals(Object object) {
//		boolean isEqual = super.equals(object);
//		if (!isEqual && (object instanceof Bounds)) {
//			Bounds bounds = (Bounds)object;
//			isEqual = ((getType() == bounds.getType()) &&
//					   ((isEmpty() && bounds.isEmpty()) ||
//						((getMaxPoint().equals(bounds.getMaxPoint())) &&
//						 (getMinPoint().equals(bounds.getMinPoint())))));
//		}
//		return isEqual;
//	}
//
//	public int hashCode () {
//		return getType().hashCode(getMinPoint(), getMaxPoint());
//	}
//	
//	public int compareTo(Object that) {
//		int comparison = 0;
//		if (compareMore(that, Bounds.class)) {
//			Bounds bounds = (Bounds)that;
//			comparison = getMaxPoint().compareTo(bounds.getMaxPoint());
//			if (comparison == 0) {
//				comparison = getMinPoint().compareTo(bounds.getMinPoint());				
//			}
//		} else {
//// 			// throw new ClassCastException(noComparisonString(that));
////			// comparison = super.compareTo(that);
//			comparison = ((this == that) ? 0 : 1);
//		}
//		return comparison;
//	}
	
	protected String toStringEmpty () {
		return "[]";
	}
	
	protected String toStringOpen () {
		return (isFixed() ? "" : "[");
	}
	
	protected String toStringClose () {
		return (isFixed() ? "" : "]");
	}
	
	protected String toStringBounds () {
		String string = getMinPoint().toString();
		if (!isPoint()) {
			string = ((getType() == Type.BOOLEAN) ? 
					"unknown" :
					string + ":" + getMaxPoint().toString());
		}
		return string;
	}
	
	public String toStringBody () {
		String s = (isEmpty() ?
					toStringEmpty() :
					toStringOpen() + toStringBounds() + toStringClose());
		return s;
	}

	public void writeExternalInternal(ObjectOutput stream) 
	throws IOException {
		super.writeExternalInternal(stream);
		stream.writeObject(get_minPoint());
		stream.writeObject(get_maxPoint());
		return;
	}

	public void readExternalInternal(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
		super.readExternalInternal(stream);
		set_minPoint((Point)(stream.readObject()));
		set_maxPoint((Point)(stream.readObject()));
		return;
	}
	
}

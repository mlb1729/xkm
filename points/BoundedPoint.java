/*
 * Created on Sep 5, 2004
 *
 
 */
package points;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import bounds.Bounds;



/**
 * @author MLB
 *
 *
 */
public class BoundedPoint 
	extends DynamicPoint 
{
	private Bounds	_bounds;
	
	public BoundedPoint() {super();}
	
	public BoundedPoint(int index, Bounds bounds) {
		super(bounds.getType(), index);
		_bounds = bounds;
	}

	public BoundedPoint(Object object, Bounds bounds) {
		this(bounds.getType().index(object), bounds);
	}
		
	public BoundedPoint(Point point, Bounds bounds) {
		this(point.getIndex(), bounds);
	}
	
	public Bounds getBounds () {
		return _bounds;
	}
	
	public Point getMinBound () {
		return getBounds().getMinPoint();
	}
	
	public Point getMaxBound () {
		return getBounds().getMaxPoint();
	}
	
	public boolean checkChange (int index) {
		if ((index < getMinBound().getIndex()) || (index > getMaxBound().getIndex())) {
			throwChangeContradiction(index);
		}
		boolean isChanged = (index != getIndex());
		return isChanged;
	}

	public void writeExternalInternal(ObjectOutput stream) 
	throws IOException {
		super.writeExternalInternal(stream);
		stream.writeObject(getBounds());
		return;
	}

	public void readExternalInternal(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
		super.readExternalInternal(stream);
		_bounds = (Bounds)(stream.readObject());
		return;
	}

}

/*
 * Created on Sep 21, 2004
 *
 
 */
package com.resonant.xkm.expressions;

import com.resonant.xkm.api.Interval;
import com.resonant.xkm.bounds.Bounds;
import com.resonant.xkm.bounds.CollidingBounds;
import com.resonant.xkm.domains.FixedManagedPoint;
import com.resonant.xkm.domains.ManagedPoint;
import com.resonant.xkm.kb.IntervalObject;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Type;
import com.resonant.xkm.types.Type.BOOLEAN;

/**
 * @author MLB
 *
 *
 */
public class BoundedOperation 
	extends Operation 
	implements BoundedExpression 
{
	private Bounds			_bounds = null;
	private	ManagedPoint	_minManagedPoint;
	private	ManagedPoint	_maxManagedPoint;
	private boolean			_isFixedPoint = false;
	
	private Bounds get_bounds() {return _bounds;}
	private void set_bounds(Bounds bounds) {_bounds = bounds; return;}
	
	private ManagedPoint get_maxManagedPoint() {return _maxManagedPoint;}
	private void set_maxManagedPoint(ManagedPoint point) {_maxManagedPoint = point;}
	
	private ManagedPoint get_minManagedPoint() {return _minManagedPoint;}
	private void set_minManagedPoint(ManagedPoint point) {_minManagedPoint = point;}
	
	private boolean get_isFixedPoint() {return _isFixedPoint;}
		
	public BoundedOperation() {super();}
	
	public BoundedOperation(Object name) {
		super(name);
	}
	
	public BoundedOperation(Type type) {
		this();
		initBounds(type);
	}
	
	public BoundedOperation (Class typeableClass) {
		this(Type.classType(typeableClass));
	}
	
	public BoundedOperation(Bounds bounds) {
		this();
		initBounds(bounds);
	}
	
	public BoundedOperation(Point point) {
		this();
		initBounds(point);
	}
	
	public void initBounds (Bounds bounds) {
		set_bounds(bounds);
		KBObject kb = getKB();
		
		ManagedPoint minManagedPoint = new ManagedPoint(kb);
		minManagedPoint.setPoint(bounds.getMinPoint());
		addManagedPoint(minManagedPoint);
		set_minManagedPoint(minManagedPoint);
		
		ManagedPoint maxManagedPoint = new ManagedPoint(kb);
		maxManagedPoint.setPoint(bounds.getMaxPoint());
		addManagedPoint(maxManagedPoint);
		set_maxManagedPoint(maxManagedPoint);
		onInitBounds();
	}

	public void initBounds(Type type) {
		initBounds(new CollidingBounds(type));
	}
	
	public void initBounds(Point point) {
		Type type = point.getType();
		Bounds bounds = type.getFixedBounds(point, point);
		set_bounds(bounds);
		ManagedPoint managedPoint = new FixedManagedPoint(point);
		addManagedPoint(managedPoint);
		set_minManagedPoint(managedPoint);
		set_maxManagedPoint(managedPoint);
		_isFixedPoint = true;
		onInitBounds();
	}
	
	public void onInitBounds(){
		return;
	}

	public Bounds getBounds() {return get_bounds();}
	public boolean contains(int index) {return getBounds().contains(index);}
	public Bounds getFixedBounds() {return getBounds().getFixedBounds();}
	public Point getMaxPoint() {return getBounds().getMaxPoint();}
	public Point getMinPoint() {return getBounds().getMinPoint();}
	public boolean isEmpty() {return getBounds().isEmpty();}
	public boolean isPoint() {return getBounds().isPoint();}
	public boolean isKnownToBe (boolean bool) {return getBounds().isKnownToBe(bool);}
	public boolean isKnownToBe (int index) {return getBounds().isKnownToBe(index);}
//	public int compareTo(Object that) {return getBounds().compareTo(that);}
	public int getMaxIndex(){return getMaxPoint().getIndex();}
	public int getMinIndex(){return getMinPoint().getIndex();}
	
	public boolean isFixedPoint() {
		return get_isFixedPoint();
	}
	
	public boolean isActive() {
		boolean isActive = (isFixedPoint() || super.isActive());
		return isActive;
	}
	
	public boolean isFixed() {
		boolean isFixed = (isFixedPoint() || getBounds().isFixed());
		return isFixed;
	}

	public Object getPointObject() {
		Object object = (isPoint() ? getMinPoint().getValueObject() : null);
		return object;
	}

	public String toStringValue() {
		String string = "";
		boolean isBoolean = (getType() == Type.BOOLEAN);
		boolean isFixed = isFixed();
		if (isBoolean || isFixed) 
		{
			string += getBounds().toString();
		} else {
			string += super.toStringValue();
		}
		if (!isFixed){
//			string += toStringSupport();
		}
		return string;
	}
	
	public String toStringSupport() {
		String string = 
				"\n  |  " + 
				get_minManagedPoint().getSupport() +
				"\n  |  " +
				get_maxManagedPoint().getSupport() +
				" ";
		return string;
	}
	
	protected boolean changePoint(ManagedPoint point, int index, Object support) {
		boolean isChanged = point.changeIndex(index, support);
		return isChanged;
	}

	public boolean changeMinIndex (int index, Object support) {
		ManagedPoint point = get_minManagedPoint();
		int oldIndex = point.getIndex();
		boolean isChanged = (index > oldIndex);
		if (isChanged) {
//			System.out.println("      Min " + this + " changed to " + index + " by " + support);
			isChanged = changePoint(point, index, support);
		} else if (index == oldIndex){
//			System.out.println("      Min " + this + " also supported by " + support);
//			point.addSupport(support);
		} else {
//			System.out.println("      Min " + this + " NOT changed to " + index + " by " + support);			
		}
		return isChanged;
	}
	
	public boolean changeMinIndex (int index) {
		return changeMinIndex(index, this);
	}

	public boolean changeMaxIndex (int index, Object support) {
		ManagedPoint point = get_maxManagedPoint();
		int oldIndex = point.getIndex();
		boolean isChanged = (index < oldIndex);
		if (isChanged) {
//			System.out.println("      Max " + this + " changed to " + index + " by " + support);
			isChanged = changePoint(point, index, support);
		} else if (index == oldIndex){
//			System.out.println("      Max " + this + " also supported by " + support);
//			point.addSupport(support);
		} else {
//			System.out.println("      Max " + this + " NOT changed to " + index + " by " + support);			
		}
		return isChanged;
	}

	public boolean changeMaxIndex (int index) {
		return changeMaxIndex(index, this);
	}

	public boolean changeToBe (boolean bool, Object support) {
		int index = BOOLEAN.toIndex(bool);
		boolean isChanged = (bool ? 
								changeMinIndex(index, support) :
								changeMaxIndex(index, support));
		return isChanged;
	}
	
	public boolean changeToBe (boolean bool) {
		return changeToBe(bool, this);
	}

	public boolean changeToBe (int index, Object support) {
		boolean	minChanged = changeMinIndex(index, support);
		boolean maxChanged = changeMaxIndex(index, support);
		boolean isChanged = (minChanged || maxChanged);
		return isChanged;
	}
	
	public boolean changeToBe (int index) {
		return changeToBe(index, this);
	}

	public Interval getInterval() {
		Type type =  getType();
		Object min = type.getObject(getMinIndex());
		Object max = type.getObject(getMaxIndex());
		Interval interval = new IntervalObject(min, max);
		return interval;
	}

}

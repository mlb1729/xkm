/*
 * Created on Aug 18, 2004
 *
 
 */
package com.resonant.xkm.types;

// import java.util.Collections;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

import com.resonant.xkm.bounds.Bounds;
import com.resonant.xkm.bounds.FixedBounds;
import com.resonant.xkm.containers.IndexObject;
import com.resonant.xkm.km.Named;
import com.resonant.xkm.points.FixedPoint;
import com.resonant.xkm.points.Point;

/**
 * @author MLB
 *
 *
 */
public abstract class Type 
	extends IndexObject 
	implements Named
{
//	private static final Map	_typeMap = Collections.synchronizedMap(new HashMap()); 
	
//	private static Map get_typeMap() {return _typeMap;}
	
//	public static Type getType (String name) {
//		Type type = (Type)(get_typeMap().get(name));
//		return type;
//	}

	private Object	_name;
	private Map  	_fixedPointMap = null;
	private Map  	_fixedBoundsMap = null;
	
	private Map get_fixedBoundsMap() {return _fixedBoundsMap;}
	private void set_fixedBoundsMap(Map boundsMap) {_fixedBoundsMap = boundsMap;}
	private Map get_fixedPointMap() {return _fixedPointMap;}
	private void set_fixedPointMap(Map pointMap) {_fixedPointMap = pointMap;}
	private Object get_name() {return _name;}
	private void set_name(Object name) {_name = getName(name);}
	
	protected Type() {super();}	
	
	protected Type(Object name, Class javaClass) {
		super(javaClass);
		set_name(name);
//		get_typeMap().put(name, this);
	}
	
	public Object getName() {
		return get_name();
	}
	
	public String toString() {
		String string = "" + getName();
		return string;
	}
	
	public Type	getType () {
		return this;
	}
	
	public Type checkType(Typed that) {
		Type thatType = that.getType();
		if (this != thatType) {
			error(thatType + " of " + that + " isn't " + this);
		}
		return this;
	}
	
	public int minIndex() {return 1;}
	public int maxIndex() {return (getSize());}
	public int limitIndex() {return (isClosed() ? maxIndex() : Integer.MAX_VALUE);}
	
	public boolean contains (int index) {
		boolean contains = ((minIndex() <= index) && (index <= maxIndex()));
		return contains;
	}
	
	public int checkIndex(int index) {
		if (!contains(index)) {
			error("Type " + this + " has no element with index " + index);
		}
		return index;
	}
	
	public Object getObject (int index) {
//		return super.getObject(checkIndex(index));
		return super.getObject(index);
	}
	
	public int hashCode(int n) {
		int hashCode = super.hashCode(n) ^ hashCode();
		return hashCode;
	}

	private Map getFixedPointMap () {
		Map fixedPointMap = get_fixedPointMap();
		if (fixedPointMap == null) {
			fixedPointMap = new HashMap();
			set_fixedPointMap(fixedPointMap);
		}
		return fixedPointMap;
	}
	
	public void addFixedPoint (Point point) {
		if (point.isFixed()) {
			Map map = getFixedPointMap();
			Integer key = new Integer(point.getIndex());
			Point fixedPoint = (Point)map.get(key);
			if (fixedPoint == null)
			{
				map.put(key, point);
			} else {
				error("Can't add fixed point " + this + " that duplicates " + fixedPoint);
			}
		}
		return;
	}

	public Point getFixedPoint (Integer key) {
		Map map = getFixedPointMap();
		Point fixedPoint = (Point)map.get(key);
		if (fixedPoint == null) {
			fixedPoint = new FixedPoint(this, key.intValue());
		}
		return fixedPoint;
	}
	
	public Point getFixedPoint (Point point) {
		Point fixedPoint = point;
		if (!point.isFixed())
		{
			Map map = getFixedPointMap();
//			fixedPoint = (Point)map.get(point);
			Integer key = new Integer(point.getIndex());
			fixedPoint = (Point)map.get(key);
			if (fixedPoint == null)
			{
				fixedPoint = new FixedPoint(point);
			}						
		}
		return fixedPoint;
	}
	
	public Point getFixedPoint (Object object) {
		return getFixedPoint(key(object));
	}
	
	public Point getFixedPoint (int index) {
		return getFixedPoint(getObject(index));
	}
	
	private Map getFixedBoundsMap () {
		Map fixedBoundsMap = get_fixedBoundsMap();
		if (fixedBoundsMap == null) {
			fixedBoundsMap = new HashMap();
			set_fixedBoundsMap(fixedBoundsMap);
		}
		return fixedBoundsMap;
	}
	
//	public void addFixedBounds (Bounds bounds) {
//		if (bounds.isFixed()) {
//			Map map = getFixedBoundsMap();
//			Integer key = new Integer(bounds.hashCode());
//			Bounds fixedBounds = (Bounds)map.get(key);
//			if (fixedBounds == null)
//			{
//				map.put(key, bounds);
//			}			
//		}
//		return;
//	}

	public void addFixedBounds (Bounds bounds) {
		if (bounds.isFixed()) {
			Map map = getFixedBoundsMap();
			Integer maxKey = new Integer(bounds.getMaxPoint().getIndex());
			Map maxMap = (Map)(map.get(maxKey));
			if (maxMap == null){
				maxMap = new HashMap();
				map.put(maxKey, maxMap);
			}
			Integer minKey = new Integer(bounds.getMinPoint().getIndex());
			Bounds fixedBounds = (Bounds)(maxMap.get(minKey));
			if (fixedBounds == null)
			{
				maxMap.put(minKey, bounds);
			}			
		}
		return;
	}

	public int hashCode (Point minPoint, Point maxPoint) {
		int hashCode = ((minPoint.getIndex() > maxPoint.getIndex()) ?
						hashCode() :
						minPoint.hashCode() * maxPoint.hashCode());
		return hashCode;
	}
	
//	public Bounds getFixedBounds (Point minPoint, Point maxPoint) {
//		Map map = getFixedBoundsMap();
//		Integer key = new Integer(hashCode(minPoint, maxPoint));
//		Bounds fixedBounds = (Bounds)map.get(key);
//		if (fixedBounds == null) {
//			fixedBounds = new FixedBounds(minPoint.getFixedPoint(), maxPoint.getFixedPoint());
//		}
//		return fixedBounds;
//	}
	
	public Bounds getFixedBounds (Point minPoint, Point maxPoint) {
		Bounds fixedBounds = null;
		Map map = getFixedBoundsMap();
		Integer maxKey = new Integer(maxPoint.getIndex());
		Map maxMap = (Map)(map.get(maxKey));
		if (maxMap != null){
			Integer minKey = new Integer(minPoint.getIndex());
			fixedBounds = (Bounds)(maxMap.get(minKey));
		}
		if (fixedBounds == null) {
			fixedBounds = new FixedBounds(minPoint.getFixedPoint(), maxPoint.getFixedPoint());
		}
		return fixedBounds;
	}
	
	public Bounds getFixedBounds (Object minPoint, Object maxPoint) {
		return getFixedBounds(getFixedPoint(minPoint), getFixedPoint(maxPoint));
	}
	
	public Bounds getFixedBounds (Bounds bounds) {
		return getFixedBounds(bounds.getMinPoint(), bounds.getMaxPoint());
	}
	
	public Bounds getFixedBounds () {
		return getFixedBounds(minIndex(), limitIndex());
	}
	
	public Bounds getFixedBounds (int minPoint, int maxPoint) {
		return getFixedBounds(getFixedPoint(minPoint), getFixedPoint(maxPoint));
	}
	
	public String toString (int index) {
		Object object = getObject(index);
		String string = object.toString();
		return string;
	}

	public int toIndex (String string) {return 0;}
	
	
	public abstract static class ClosedType extends Type {
		protected ClosedType(String name, Class javaClass) {
			super(name, javaClass); 
			close();
		}
	}

	
	public static class INTEGER extends ClosedType {
		protected INTEGER(String name, Class javaClass){super(name, javaClass);}
		public INTEGER() {this("Integer", Integer.class);}
		public int minIndex() {return Integer.MIN_VALUE;}
		public int maxIndex() {return Integer.MAX_VALUE;}
		public Object getObject (int index) {return new Integer(checkIndex(index));}
		public Integer getKey (Object object) {return (Integer)checkClass(object);}	
		public int getIndex(Object object) {return getKey(object).intValue();}
		public int toIndex (String string) {return Integer.parseInt(string);}
		public static boolean toBoolean (int integer) {return (integer > 0);}
		public String toString (int index) {return "" + checkIndex(index);}
	}; 
	public static final INTEGER INTEGER = new INTEGER();

	/*
	public static class QUANTITY extends INTEGER {
		protected QUANTITY(String name, Class javaClass){super(name, javaClass);}
		public QUANTITY() {this("Quantity", Integer.class);}
		public int minIndex() {return 0;}	
	}; 
	public static final QUANTITY QUANTITY = new QUANTITY();
	*/

	public static class BOOLEAN extends ClosedType {
		protected BOOLEAN(String name, Class javaClass){super(name, javaClass);}
		public BOOLEAN() {this("Boolean", Boolean.class);}
		private static final int FALSE_INT = 0;
		private static final int TRUE_INT = 1; 
		private static final Integer FALSE_INTEGER = new Integer(FALSE_INT);
		private static final Integer TRUE_INTEGER = new Integer(TRUE_INT); 
		public int minIndex() {return FALSE_INT;}
		public int maxIndex() {return TRUE_INT;}
		public Object getObject (int index) {
			Boolean object = (toBoolean(index) ? Boolean.TRUE : Boolean.FALSE);
			return object;
		}
		public Integer getKey (Object object) {
			Integer integer = (((Boolean)checkClass(object)).booleanValue() ? 
									TRUE_INTEGER : FALSE_INTEGER);
			return integer;
		}	
		public int getIndex(Object object) {return getKey(object).intValue();}
		public int toIndex(String string) {return toIndex(Boolean.getBoolean(string));}	
		public static boolean toBoolean (int integer) {return (integer > 0);}
		public static String toString (boolean bool) {
			return (bool ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
		}
		public String toString (int index) {return toString(toBoolean(index));}
		public static int toIndex (boolean bool) {return (bool ? TRUE_INT : FALSE_INT);}
	}; 
	public static final BOOLEAN BOOLEAN = new BOOLEAN();

	public static class FLOAT extends ClosedType {
		protected FLOAT(String name, Class javaClass){super(name, javaClass);}
		public FLOAT(){this("Float", Float.class);}
		public int minIndex() {return toIndex(-Float.MAX_VALUE);}
		public int maxIndex() {return toIndex(Float.MAX_VALUE);}
		public Object getObject (int index) {
			Float flonum = new Float(toFloat(checkIndex(index)));
			return flonum;
		}
		public Integer getKey (Object object) {
			Integer integer = new Integer(toIndex(((Float)checkClass(object)).floatValue()));
			return integer;
		}	
		public int getIndex(Object object) {
			return toIndex(((Float)object).floatValue());
		}
		public int toIndex (String string) {
			return Float.floatToIntBits(Float.parseFloat(string));
		}
		public static int toIndex(float f) {
			int value = ((f<0) ? -toIndex(-f) : Float.floatToIntBits(f));
			return value;
		}
		public static float toFloat(int index) {
			float value = ((index<0) ? -toFloat(-index) : Float.intBitsToFloat(index));
			return value;
		}
		// public static int toFloatIndex(int index) {return toIndex((float)index);};  // flo()
		public static int toFloatIndex(int index) {return toIndex(index);};  // flo()
		public static int toIntIndex(int index) {return (int)(toFloat(index));};	// int()
		public String toString (int index) {return "" + toFloat(checkIndex(index));}
	}; 
	public static final FLOAT FLOAT = new FLOAT();

	
	public abstract static class OpenType extends Type {
		protected OpenType(String name, Class javaClass) {
			super(name, javaClass); 
		}
	}
	
	public static class STRING extends Type {
		protected STRING(String name, Class javaClass){super(name, javaClass); index("");}
		public STRING(){this("String", String.class);}
	}
	public static final STRING STRING = new STRING();
	
//	public class SYMBOL extends STRING {
//		protected SYMBOL(String name, Class javaClass){super(name, javaClass);}
//		public SYMBOL() {this("Symbol", String.class);}
//		public int toIndex(String string) {return super.toIndex(string.toUpperCase());}
//	}
//	public final SYMBOL SYMBOL = new SYMBOL();
	
	// TIME, MONEY, etc etc 
	

	public static Type objectType(Object object) {
		Type type = classType(object.getClass());
		return type;
	}

	public static Type classType(Class objectClass) {
		Type type = null;
		for (Class superClass = objectClass; 
				superClass != null; 
				superClass = superClass.getSuperclass()) {
			if ((Boolean.class == superClass)) {
				type = Type.BOOLEAN;
				break;
			} else if (Integer.class == superClass) {
				type = Type.INTEGER;
				break;
			} else if (String.class == superClass) {
				type = Type.STRING;
				break;
			} else if (Float.class == superClass) {
				type = Type.FLOAT;
				break;
			}
		}
		if (type == null) {
			error("Can't determine the KB type for the class " + objectClass);
		}
		return type;
	}
	
	public static Type coerceType(Object type){
		Type kbType = null;
		if (type instanceof Type) {
			kbType = ((Type)type);
		} else if (type instanceof Class) {
			kbType = classType((Class)type);
		}
		return kbType;
	}
	
	public static Object[] classifyTypes(Object[] parameters){
		for (int i=0;i<parameters.length;i++){
			Object parameter = parameters[i];
			if (parameter instanceof Type){
				parameters[i] = ((Type)parameter).getClass();
			}
		}
		return parameters;
	}
	
	public static void writeExternalType(ObjectOutput stream, Object type) 
		throws IOException
	{
		Class javaClass = coerceType(type).getJavaClass();
		stream.writeObject(javaClass);
		return;
	}

	public static Type readExternalType(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
		Object object = stream.readObject();
		Class javaClass = (Class)object;
		Type type = Type.classType(javaClass);
		return type;
	}

}

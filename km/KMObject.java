/*
 * Created on Aug 18, 2004
 *
 */
package km;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import containers.KMSet;
import exceptions.KMError;
import exceptions.KMSignal;

/**
 * @author MLB
 *
 *	This is the root base class for *all* KM entities
 *
 */
public class KMObject 
	extends Object implements Serializable
{
	public KMObject() {super();}

	// "All throws lead through Rome."  Breakpoint here to catch them in transit.
	protected final static void throwKM (Throwable throwable) {
		if (throwable instanceof KMSignal) {
			throw (KMSignal)throwable;
		} else if (throwable instanceof KMError) {
			throw (KMError)throwable;			
		} else {
			error("Throwing non-KM exception type " + throwable.getClass() + " in KM.", throwable);
		}
	}

	public static void error() {throwKM(new KMError());}
	public static void error(String string) {throwKM(new KMError(string));}
	public static void error(Throwable throwable) {
		if (!(throwable instanceof KMError)) {
			throwable = new KMError(throwable);
		}
		throwKM(throwable);
	}
	public static void error(String string, Throwable throwable) {throwKM(new KMError(string, throwable));}

	public static void signal() {throwKM(new KMSignal());}
	public static void signal(String string) {throwKM(new KMSignal(string));}
	public static void signal(Throwable throwable) {
		if (!(throwable instanceof KMSignal)) {
			throwable = new KMSignal(throwable);
		}
		throwKM(throwable);
	}
	public static void signal(String string, Throwable throwable) {throwKM(new KMSignal(string, throwable));}
	
	public static void debugError() {error();}
	public static void debugError(String string) {error(string);}
	public static void debugError(Throwable throwable) {error(throwable);}
	public static void debugError(String string, Throwable throwable) {error(string, throwable);}

	public static final Iterator EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();
	
	public static Iterator iterator(List collection){
		Iterator iterator = ((collection != null) ? collection.iterator() : EMPTY_ITERATOR);
		return iterator;
	}

	public static Iterator iterator(Set collection){
		Iterator iterator = ((collection != null) ? collection.iterator() : EMPTY_ITERATOR);
		return iterator;
	}

	public static Iterator iterator(KMSet collection){
		Iterator iterator = ((collection != null) ? collection.iterator() : EMPTY_ITERATOR);
		return iterator;
	}

	public static Object getName (Object object) {
		Object name = ((object instanceof Named) ? 
						((Named)object).getName() : 
						object);
		if (!(name instanceof String)){
			name = toString(name);
		}
		return name;
	}
	
	public static Object[] coerceNames(Object[] names){
		for (int i=0; i<names.length; i++){
			names[i] = getName(names[i]);
		}
		return names;
	}

	public static List coerceNames(List names){
		for (int i=0; i<names.size(); i++) {
			names.set(i, getName(names.get(i)));
		}
		return names;
	}

	public static String className(Class classObject) {
		String fullName = classObject.getName();
		String packageName = classObject.getPackage().getName();
		String className = fullName.substring(packageName.length() + 1);
		return className;
	}
	
	public String className() {
		String className = className(this.getClass());
		return className;
	}
	
	protected String noComparisonString (Object that) {
		return "Can't compare " + this + " to " + that;
	}
	
	protected boolean compareMore (Object that, Class thisClass) {
		boolean compareMore = (this != that);
		if (compareMore) {
			if (that == null) {
				throw new NullPointerException(noComparisonString(that));
			/*
			} else if (!thisClass.isInstance(that)) {
 				throw new ClassCastException(noComparisonString(that));
			}
			*/
			} else {
				compareMore = thisClass.isInstance(that);
			}
		}
		return compareMore;
	}

	protected int hashCode(int n) {
		int hashCode = 2*n*n-199;
		return hashCode;
	}
	
	protected String toStringBody () {
		return super.toString();
	}
	
	public String toString () {
		return toStringBody();
	}
	
	public static String toString(Object object) {
		String string = ((object==null) ? 
							"" + null :
							((object instanceof Object[]) ? 
									toString((Object[])object) :
									object.toString()));
		return string;
	}
	
	public static String toStringBody(Object[] objects, String delimiter) {
		String string = "";
		for (int i=0; i<objects.length; i++) {
			if (i>0){
				string += delimiter;
			}
			string += toString(objects[i]);
		}
		return string;
	}
	
	public static String toString(Object[] objects) {
		String string = "[" + toStringBody(objects, ", ") + "]";
		return string;
	}
	
	public static String toPathString(Object[] path) {
		String string = toStringBody(path, ".");
		return string;
	}
	
	public static Object makeIndexKey(Object key, boolean isIndex) {
		if (key instanceof Object[]){
			String string = (isIndex ? "[" : "");
			Object[] arrayKey = (Object[])key;
			int size = arrayKey.length;
			for (int i =  0; i<size; i++){
				Object element = arrayKey[i];
				if (i>0){
					string += (isIndex ? "," : ".");
				}
				string += makeIndexKey(element, !isIndex);
			}
			key = string + (isIndex ? "]" : "");
		} else {
			key = getName(key);
		}
		return key;
	}

	public static Object newObject(Class objectClass) {
		Object object = newObject(objectClass, false);
		return object;
	}
	
	public static Object newObject(Class objectClass, boolean forceLoad) {
		Object object = null;
		try {
			if (forceLoad) {
				objectClass = java.lang.ClassLoader.getSystemClassLoader().loadClass(objectClass.getName());
			}
			object = objectClass.newInstance();
		} catch (InstantiationException ex) {
			if (forceLoad) {
				error(ex);
			} else {
				object = newObject(objectClass, true);
			}
		} catch (Exception ex) {
			error(ex);
		}
		return object;
	}
	
	public static Map toMap(Object[] plist) {
		int length = plist.length;
		if ((length & 1) != 0) {
			error("Attempt to create keyed map with odd number of objects: " + plist);
		}
		Map map = new HashMap();	
		for (int i=0; i < length; i++) {
			map.put(plist[i], plist[++i]);		
		}
		return map;
	}
	
	public static List toList(Object[] objects) {
		int length = objects.length;
		List list = new ArrayList(length);
		for (int i=0; i < length; i++) {
			list.add(objects[i]);
		}
		return list;
	}
	
	public static Set toSet(Object[] objects) {
		Set set = new HashSet(toList(objects));
		return set;
	}
	
	/*
	public static Object fetch(Object object, Iterator iterator) {
		while (iterator.hasNext()) {
			if (object instanceof Fetcher) {
				object = ((Fetcher)object).get(iterator.next());
			} else {
				object = null;
				break;
			}
		}
		return object;
	}
	*/

	public static Object fetch(Object object, Object[] path, int i, boolean mayCreate) {
		int size = path.length;
		while (i<size) {
			if (object instanceof Fetcher) {
				Fetcher fetcher = (Fetcher)object;
				Object key = getName(path[i]);
				object = fetcher.get(key, mayCreate);
				if ((key == null) && (object != null)) {
					key = getName(object);
					path[i] = key;
				}
			} else {
				object = null;
				break;
			}
			i++;
		}
		return object;
	}
	
	public Object fetch(Object[] path, boolean mayCreate) {
		Object object = fetch(this, path, 0, mayCreate);
		return object;
	}
	
	public Object fetch(Object[] path) {
		// Object object = fetch(path, false);
		Object object = fetch(path, true);
		return object;
	}
	
	public Object get(Object key, boolean mayCreate) {
		if (!(this instanceof Fetcher)) {
			error("The " + className() + " " + this + " is not a Fetcher");
		}
		key = getName(key);
		Object object = get(key);
		return object;
	}
	
	public Object get(Object key) {
		key = getName(key);
		Object object = get(key, false);
		return object;
	}

	/*
	public Object fetch(Iterator iterator) {
		Object object = fetch(this, iterator);
		return object;
	}
	*/

	public static List collectClassMembers (Iterator it, List list, Class memberClass) {
		while (it.hasNext()) {
			Object next = it.next();
			if (memberClass.isInstance(next)) {
				list.add(next);
			}
		}		
		return list;
	}
	
}

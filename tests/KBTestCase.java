/*
 * Created on Aug 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tests;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import km.KMObject;

import junit.framework.TestCase;

/**
 * @author Dave
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KBTestCase extends TestCase 
{
	KMObject kmObject = new KMObject();
	

	/**
	 * 
	 */
	public KBTestCase() 
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public KBTestCase(String arg0) 
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{
	}
	
	
	
	
	
	
	
	/***********************************************************************
	 ***********************************************************************
	 * 
	 * The Following are delegate methods to KMObject * 
	 * 
	 ************************************************************************
	 ***********************************************************************/
	
	/**
	 * @param classObject
	 * @return
	 */
	public static String className(Class classObject) {
		return KMObject.className(classObject);
	}
	/**
	 * @param it
	 * @param list
	 * @param memberClass
	 * @return
	 */
	public static List collectClassMembers(Iterator it, List list,
			Class memberClass) {
		return KMObject.collectClassMembers(it, list, memberClass);
	}
	/**
	 * 
	 */
	public static void debugError() {
		KMObject.debugError();
	}
	/**
	 * @param string
	 */
	public static void debugError(String string) {
		KMObject.debugError(string);
	}
	/**
	 * @param string
	 * @param throwable
	 */
	public static void debugError(String string, Throwable throwable) {
		KMObject.debugError(string, throwable);
	}
	/**
	 * @param throwable
	 */
	public static void debugError(Throwable throwable) {
		KMObject.debugError(throwable);
	}
	/**
	 * 
	 */
	public static void error() {
		KMObject.error();
	}
	/**
	 * @param string
	 */
	public static void error(String string) {
		KMObject.error(string);
	}
	/**
	 * @param string
	 * @param throwable
	 */
	public static void error(String string, Throwable throwable) {
		KMObject.error(string, throwable);
	}
	/**
	 * @param throwable
	 */
	public static void error(Throwable throwable) {
		KMObject.error(throwable);
	}
	/**
	 * @param object
	 * @param path
	 * @param i
	 * @param mayCreate
	 * @return
	 */
	public static Object fetch(Object object, Object[] path, int i,
			boolean mayCreate) {
		return KMObject.fetch(object, path, i, mayCreate);
	}
	/**
	 * @param object
	 * @return
	 */
	public static Object getName(Object object) {
		return KMObject.getName(object);
	}
	/**
	 * @param collection
	 * @return
	 */
	public static Iterator iterator(List collection) {
		return KMObject.iterator(collection);
	}
	/**
	 * @param collection
	 * @return
	 */
	public static Iterator iterator(Set collection) {
		return KMObject.iterator(collection);
	}
	/**
	 * @param objectClass
	 * @return
	 */
	public static Object newObject(Class objectClass) {
		return KMObject.newObject(objectClass);
	}
	/**
	 * @param objectClass
	 * @param forceLoad
	 * @return
	 */
	public static Object newObject(Class objectClass, boolean forceLoad) {
		return KMObject.newObject(objectClass, forceLoad);
	}
	/**
	 * 
	 */
	public static void signal() {
		KMObject.signal();
	}
	/**
	 * @param string
	 */
	public static void signal(String string) {
		KMObject.signal(string);
	}
	/**
	 * @param string
	 * @param throwable
	 */
	public static void signal(String string, Throwable throwable) {
		KMObject.signal(string, throwable);
	}
	/**
	 * @param throwable
	 */
	public static void signal(Throwable throwable) {
		KMObject.signal(throwable);
	}
	/**
	 * @param objects
	 * @return
	 */
	public static List toList(Object[] objects) {
		return KMObject.toList(objects);
	}
	/**
	 * @param plist
	 * @return
	 */
	public static Map toMap(Object[] plist) {
		return KMObject.toMap(plist);
	}
	/**
	 * @param path
	 * @return
	 */
	public static String toPathString(Object[] path) {
		return KMObject.toPathString(path);
	}
	/**
	 * @param objects
	 * @return
	 */
	public static Set toSet(Object[] objects) {
		return KMObject.toSet(objects);
	}
	/**
	 * @param object
	 * @return
	 */
	public static String toString(Object object) {
		return KMObject.toString(object);
	}
	/**
	 * @param objects
	 * @return
	 */
	public static String toString(Object[] objects) {
		return KMObject.toString(objects);
	}
	/**
	 * @param objects
	 * @param delimiter
	 * @return
	 */
	public static String toStringBody(Object[] objects, String delimiter) {
		return KMObject.toStringBody(objects, delimiter);
	}
	/**
	 * @param key
	 * @param isIndex
	 * @return
	 */
	public static Object makeIndexKey(Object key, boolean isIndex) {
		return KMObject.makeIndexKey(key, isIndex);
	}
	/**
	 * @return
	 */
	public String className() {
		return kmObject.className();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return kmObject.equals(arg0);
	}
	/**
	 * @param path
	 * @return
	 */
	public Object fetch(Object[] path) {
		return kmObject.fetch(path);
	}
	/**
	 * @param path
	 * @param mayCreate
	 * @return
	 */
	public Object fetch(Object[] path, boolean mayCreate) {
		return kmObject.fetch(path, mayCreate);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		return kmObject.get(key);
	}
	/**
	 * @param key
	 * @param mayCreate
	 * @return
	 */
	public Object get(Object key, boolean mayCreate) {
		return kmObject.get(key, mayCreate);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return kmObject.hashCode();
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#toString()
	 */
	public String toString() {
		return kmObject.toString();
	}
}

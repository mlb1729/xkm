/*
 * Created on Sep 13, 2004
 *
 
 */
package com.resonant.xkm.containers;

/**
 * @author MLB
 *
 *
 */
public interface Index {
	Class	getJavaClass	();
	int		getSize			();
	boolean	isClosed		();
	Object	checkClass		(Object object);
	Integer	getKey			(Object object);
	int		getIndex		(Object object);
	Integer	key				(Object object);
	int		index			(Object object);
	Object	getObject		(int index);
}
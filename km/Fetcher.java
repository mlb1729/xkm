/*
 * Created on Oct 5, 2004
 *
 
 */
package com.resonant.xkm.km;



/**
 * @author MLB
 *
 *
 */
public interface Fetcher 
{
//	Object	fetch	(Iterator iterator);
	Object	get		(Object key);
	Object	get		(Object key, boolean mayCreate);
	Object	fetch	(Object[] path);
	Object	fetch	(Object[] path, boolean mayCreate);
}

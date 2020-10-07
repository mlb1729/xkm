/*
 * Created on Sep 16, 2004
 *
 
 */
package com.resonant.xkm.containers;

import java.util.Iterator;
import java.util.List;

import com.resonant.xkm.km.Fetcher;
import com.resonant.xkm.km.Named;

/**
 * @author MLB
 *
 *
 */
public interface Namespace 
	extends Named, Fetcher
{
	void		setName(Object key);
	Namespace	getParent();
	void		setParent(Namespace namespace);
	boolean		add(Object object);
	boolean		remove(Object key);
	Object		get(Object key);
	Object		find(Object key);
	boolean		getPath(Namespace namespace, List list);
	Iterator	iterator();
}

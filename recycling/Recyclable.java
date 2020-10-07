/*
 * Created on Sep 17, 2004
 *
 
 */
package com.resonant.xkm.recycling;

/**
 * @author MLB
 *
 *
 */
public interface Recyclable 
{
	Recycler	getRecycler();
	void		setRecycler(Recycler recycler);
	void		release();
}

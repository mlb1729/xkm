/*
 * Created on Jul 22, 2005
 *
 
 */
package com.resonant.xkm.api;

/**
 * @author MLB
 *
 *
 */
public interface Interval 
{
	Object	getMin	();
	Object	getMax	();
	boolean	isBound	();
	void	setMin	(Object value);
	void	setMax	(Object value);
}

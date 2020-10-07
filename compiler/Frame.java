/*
 * Created on Mar 29, 2005
 *
 
 */
package com.resonant.xkm.compiler;


/**
 * @author MLB
 *
 *
 */
public interface Frame 
{
	Object	get(Object name);
	boolean define(Object name, Object value);
}

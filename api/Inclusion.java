/*
 * Created on Nov 8, 2005
 *
 
 */
package com.resonant.xkm.api;

/**
 * @author MLB
 *
 *
 */
public interface Inclusion 
	extends Command 
{
	Object []	getPath();
	Object		getDescriptor();
	void		setPath(Object [] path);
	void		setDescriptor(Object descriptor);
}

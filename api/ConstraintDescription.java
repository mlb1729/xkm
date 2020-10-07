/*
 * Created on Oct 10, 2005
 *
 
 */
package com.resonant.xkm.api;

import com.resonant.xkm.km.Named;

public interface ConstraintDescription
	extends Named
{
	Object	getName();
	Object	getDocumentation();
	Object	getObject();
	void	initName(Object name);
	void	initDocumentation(Object documentation);
	void	initObject(Object object);
}

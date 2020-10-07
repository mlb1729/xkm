/*
 * Created on Jul 20, 2005
 *
 
 */
package com.resonant.xkm.api;

import java.io.Serializable;

/**
 * @author MLB
 *
 *
 */
public interface UserDefinedFunctionKernel extends Serializable
{
	Object	apply(KB kb, Object[] argumentList);
	Class	getReturnValueClass();
}

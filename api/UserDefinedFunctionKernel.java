/*
 * Created on Jul 20, 2005
 *
 
 */
package api;

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

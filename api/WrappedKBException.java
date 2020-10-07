/*
 * Created on Dec 1, 2005
 *
 
 */
package com.resonant.xkm.api;

import com.resonant.exception.ResonantBaseException;
import com.resonant.xkm.exceptions.KMException;

/**
 * @author MLB
 *
 *
 */
public class WrappedKBException 
	extends ResonantBaseException
{
	public WrappedKBException(){super();}
	
	public WrappedKBException(KMException exception)
	{
		super("Exception from KB: ", exception);
	}
}

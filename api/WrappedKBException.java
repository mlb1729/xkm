/*
 * Created on Dec 1, 2005
 *
 
 */
package api;

import com.resonant.exception.ResonantBaseException;
import exceptions.KMException;

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

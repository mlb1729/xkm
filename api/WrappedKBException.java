/*
 * Created on Dec 1, 2005
 *
 
 */
package api;

// import com.resonant.exception.ResonantBaseException;	// fixme
import exceptions.KMException;

/**
 * @author MLB
 *
 *
 */
public class WrappedKBException 
//	extends ResonantBaseException	// fixme
	extends Exception
{
	public WrappedKBException(){super();}
	
	public WrappedKBException(KMException exception)
	{
		super("Exception from KB: ", exception);
	}
}

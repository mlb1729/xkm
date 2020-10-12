/*
 * Created on Aug 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package exceptions;

/**
 * @author Dave
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KMException extends RuntimeException 
{
	public KMException() {super();	onNew();}
	public KMException(String string) {super(string); onNew();}
	public KMException(Throwable throwable) {super(throwable); onNew();}
	public KMException(String string, Throwable throwable) {super(string, throwable); onNew();}
	
	private void onNew () {return;}	
}

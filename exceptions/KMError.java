/*
 * Created on Sep 14, 2004
 *
 
 */
package exceptions;


public class KMError 
	extends KMException 
{
	public KMError() {super(); onNew();}
	public KMError(String string) {super(string); onNew();}
	public KMError(Throwable throwable) {super(throwable); onNew();}
	public KMError(String string, Throwable throwable) {super(string, throwable); onNew();}
	
	private void onNew () {return;}
}
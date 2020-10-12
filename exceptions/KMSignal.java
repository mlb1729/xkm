/*
 * Created on Sep 14, 2004
 *
 
 */
package exceptions;


public class KMSignal 
	extends KMException 
{
	public KMSignal() {super();	onNew();}
	public KMSignal(String string) {super(string); onNew();}
	public KMSignal(Throwable throwable) {super(throwable); onNew();}
	public KMSignal(String string, Throwable throwable) {super(string, throwable); onNew();}
	
	private void onNew () {return;}
}
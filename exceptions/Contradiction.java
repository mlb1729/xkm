/*
 * Created on Sep 1, 2004
 *
 
 */
package exceptions;


/**
 * @author MLB
 *
 *
 */
public class Contradiction 
	extends UnwindingException 
{
	public Contradiction() {super();}
	public Contradiction(String string) {super(string);}
	public Contradiction(Throwable throwable) {super(throwable);}
	public Contradiction(String string, Throwable throwable) {super(string, throwable);}

}

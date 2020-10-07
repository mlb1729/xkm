/*
 * Created on Sep 14, 2004
 *
 
 */
package com.resonant.xkm.exceptions;



public class UnwindingException 
	extends KMSignal 
{
	public UnwindingException() {super();}
	public UnwindingException(String string) {super(string);}
	public UnwindingException(Throwable throwable) {super(throwable);}
	public UnwindingException(String string, Throwable throwable) {super(string, throwable);}

}
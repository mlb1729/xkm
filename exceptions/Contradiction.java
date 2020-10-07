/*
 * Created on Sep 1, 2004
 *
 
 */
package com.resonant.xkm.exceptions;


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

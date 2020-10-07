/*
 * Created on Dec 14, 2004
 *
 
 */
package com.resonant.xkm.reader;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class Token 
	extends KMObject 
{
	private StringBuffer _buffer = null;
	
	private StringBuffer get_buffer() {return _buffer;}
	private void set_buffer(StringBuffer buffer) {_buffer = buffer; return;}

	protected StringBuffer getBuffer() {
		StringBuffer buffer = get_buffer();
		if (buffer == null) {
			buffer = new StringBuffer();
			set_buffer(buffer);
		}
		return buffer;
	}
	
	public String toString () {
		String string = "";
		StringBuffer buffer = get_buffer();
		if (buffer != null) {
			string = buffer.toString();
		}		
		return string;
	}
	
	public void reset () {
		StringBuffer buffer = get_buffer();
		if (buffer != null) {
			buffer.setLength(0);
		}
		return;
	}
	
	public boolean hasToken() {
		StringBuffer buffer = get_buffer();
		boolean hasToken = ((buffer != null) && (buffer.length() > 0));
		return hasToken;
	}
	
	public char appendChar (char c) {
		getBuffer().append(c);
		return c;
	}
	
}

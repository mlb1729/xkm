/*
 * Created on Dec 10, 2004
 *
 
 */
package reader;

import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class StringScanner 
	extends KMObject 
{
	private String	_string;
	private int		_position;
	
	private int get_position() {return _position;}
	private void set_position(int position) {_position = position; return;}
	private String get_string() {return _string;}
	private void set_string(String string) {_string = string; return;}
	
	public StringScanner () {
		super();
		setString("");
	}
		
	public StringScanner (String string) {
		this();
		setString(string);
	}
	
	public String getString () {
		return get_string();
	}
	
	public void setString(String string) {
		set_string(string);
		reset();
		return;
	}
		
	public int getPosition () {
		return get_position();
	}

	public void reset () {
		set_position(0);
		return;
	}
	
	public boolean hasChar () {
		boolean hasChar = (getPosition() < getString().length());
		return hasChar;
	}
	
	public void next () {
		if (hasChar()) {
			set_position(getPosition() + 1);		
		}
		return;
	}
	
	public char getChar() {
		char c = getString().charAt(getPosition());
		return c;
	}
	
	public boolean isChar (char c) {
		boolean isChar = (hasChar() && (getChar() == c));
		return isChar;
	}
	
	public int charIndex (String chars) {
		int index = -1;
		if ((chars != null) && hasChar()) {
			index = chars.indexOf(getChar());
		}
		return index;
	}
	
	public boolean isChar (String chars) {
		boolean isChar = (charIndex(chars) >= 0);
		return isChar;
	}
	
	// ToDo MLB: make this extract line around cursor
	public String toString () {
		String string = "\n" + getString() + "\n";
		for (int i=0; i<getPosition(); i++) {
			string += " ";
		}
		string += "^\n";
		return string;
	}

}

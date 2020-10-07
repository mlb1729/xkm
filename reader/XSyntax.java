/*
 * Created on Dec 10, 2004
 *
 
 */
package com.resonant.xkm.reader;

import com.resonant.xkm.km.KMObject;


public class XSyntax extends KMObject {
	
	private String	_openChars;
	private String	_closeChars;
	private String	_spaceChars;
	private String	_delimiterChars;
	private String	_breakChars;
	private char	_escapeChar;
	private String	_literalOpenChars;
	private String	_literalCloseChars;
	private String	_dotChars;
	
	public String getDotChars() {return _dotChars;}
	public String getLiteralCloseChars() {return _literalCloseChars;}
	public String getLiteralOpenChars() {return _literalOpenChars;}
	public char getEscapeChar() {return _escapeChar;}
	public String getBreakChars() {return _breakChars;}
	public String getCloseChars() {return _closeChars;}
	public String getOpenChars() {return _openChars;}
	public String getSpaceChars() {return _spaceChars;}
	public String getDelimiterChars() {return _delimiterChars;}
	
	public XSyntax
		(String openChars, String closeChars, String spaceChars, String delimiterChars, 
				char escapeChar, String literalOpenChars, String literalCloseChars, String dotChars) {
		super();
		_openChars = openChars;
		_closeChars = closeChars;
		_spaceChars = spaceChars;
		_delimiterChars = delimiterChars;
		_breakChars = spaceChars + delimiterChars + closeChars + openChars;
		_escapeChar = escapeChar;
		_literalOpenChars = literalOpenChars;
		_literalCloseChars = literalCloseChars;
		_dotChars = dotChars;
	}
	
	public char closeForOpen (char openChar) {
		char c = getCloseChars().charAt(getOpenChars().indexOf(openChar));
		return c;
	}
	
	public char literalCloseForOpen (char literalOpenChar) {
		char c = getLiteralCloseChars().charAt(getLiteralOpenChars().indexOf(literalOpenChar));
		return c;
	}

}
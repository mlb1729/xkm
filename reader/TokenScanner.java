/*
 * Created on Dec 10, 2004
 *
 
 */
package com.resonant.xkm.reader;

/**
 * @author MLB
 *
 *
 */
public class TokenScanner 
	extends StringScanner 
{
	private Token _token = null;
		
	private Token get_token() {return _token;}
	private void set_token(Token token) {_token = token; return;}
	
	public TokenScanner() {
		super();
	}

	public TokenScanner(String string) {
		super(string);
	}

	public Token getToken() {
		Token token = get_token();
		if (token == null) {
			token = new Token();
			set_token(token);
		}
		return token;
	}
	
	public String getTokenString () {
		String string = getToken().toString();
		return string;
	}
	
	protected void resetToken () {
		getToken().reset();
		return;
	}
	
	public void reset () {
		super.reset();
		resetToken();
		return;
	}
	
	public char copyChar() {
		char c = getChar();
		getToken().appendChar(c);
		return c;
	}

	public boolean scanToken(char closeChar, char escapeChar, String breakChars) {
		boolean isClose = false;
		while (hasChar()) {
			isClose = isChar(closeChar);
			if (isClose || isChar(breakChars)) {
				break;
			} else if (isChar(escapeChar)) {
				next();
			}
			copyChar();
			next();
		}
		return isClose;
	}

}

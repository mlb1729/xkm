/*
 * Created on Dec 10, 2004
 *
 
 */
package reader;


/**
 * @author MLB
 *
 *
 */
public class ExpressionScanner 
	extends TokenScanner 
{

	public ExpressionScanner(String string) {
		super(string);
	}
	
	public Combo scanExpression (char closeChar, XSyntax syntax) {
		Combo combo = new Combo();
		boolean	isDelimited = true;
		while (hasChar()) {
			if (isChar(closeChar)) {
				next();
				break;
			} else if (isChar(syntax.getSpaceChars())) {
				next();
			} else if (isChar(syntax.getDelimiterChars())) {
				next();
				isDelimited = true;
			} else if (isChar(syntax.getOpenChars())) {
				char subCloseChar = syntax.closeForOpen(getChar());
				next();
				Combo subCombo = scanExpression(subCloseChar, syntax);
				if (isDelimited) {
					combo.add(subCombo);
				} else {
					int last = combo.size()-1;
					subCombo.setType(combo.get(last));
					combo.set(last, subCombo);
				}
				isDelimited = false;
			} else if (isChar(syntax.getCloseChars())) {
				error("Unbalanced expression");
			} else if (isChar(syntax.getLiteralOpenChars())) {
				char literalOpenChar = getChar();
				char literalCloseChar = syntax.literalCloseForOpen(literalOpenChar);
				next();
				resetToken();
				scanToken(literalCloseChar, syntax.getEscapeChar(), null);
				String atom = getTokenString();
				Combo subCombo = new Combo();
				subCombo.setType("" + literalOpenChar);
				subCombo.add(atom);
				combo.add(subCombo);
				next();
				isDelimited = false;
			} else {
				// String atom = scanAtom(closeChar, syntax);
				// combo.add(atom);
				Object atom = scanDotted(closeChar, syntax.getEscapeChar(), syntax.getBreakChars(), syntax.getDotChars());
				combo.add(atom);
				isDelimited = false;
			}
		}
		return combo;
	}
	
	public String scanAtom(char closeChar, XSyntax syntax) {
		resetToken();
		scanToken(closeChar, syntax.getEscapeChar(), syntax.getBreakChars());
		String atom = getTokenString();
		return atom;
	}
	
	public Object scanDotted(char closeChar, char escapeChar, String breakChars, String dotChars) {
		Combo combo = new Combo(".");
		boolean isDotted = true;
		while (isDotted) {
			resetToken();
			scanToken(closeChar, escapeChar, breakChars + dotChars);
			String atom = getTokenString();
			combo.add(atom);
			isDotted = isChar(dotChars);
			if (isDotted) {
				next();
			}
		}
		Object object = combo;
		if (combo.size() == 1) {
			object = combo.get(0);
		}
		return object;
	}

}

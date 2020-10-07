/*
 * Created on Apr 27, 2005
 *
 
 */
package com.resonant.xkm.operations;

import java.util.List;

import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class StrCat 
	extends StringOperation 
{
	public StrCat() {super();}
	
	private static final Contradiction _contradiction = 
		new Contradiction("Concatenation relationship is impossible.");

	public void constrainOperands () {
		if (isPoint()) {
			String aString = "";
			String bString = "";
			String cString = ((String)(Type.STRING.getObject(getMinIndex())));
			List operands = getOperands();
			BoundedExpression a = (BoundedExpression)(operands.get(0));
			BoundedExpression b = (BoundedExpression)(operands.get(1));
			if (a.isPoint()) {
				aString = ((String)(Type.STRING.getObject(a.getMinIndex())));
				if (cString.startsWith(aString)) {
					if (b.isPoint()){
						bString = ((String)(Type.STRING.getObject(b.getMinIndex())));
						if ((cString.length() != aString.length() + bString.length()) ||
								!cString.endsWith(bString))
						{
							signal(_contradiction);
						}
					} else {
						bString = cString.substring(aString.length());
						int index = Type.STRING.index(bString);
						b.changeMinIndex(index);
						b.changeMaxIndex(index);
					}
				} else {
					signal(_contradiction);
				}
			} else if (b.isPoint()) {
				bString = ((String)(Type.STRING.getObject(b.getMinIndex())));
				if (cString.endsWith(bString)) {
					aString = cString.substring(0,cString.length()-bString.length());
					int index = Type.STRING.index(aString);
					a.changeMinIndex(index);
					a.changeMaxIndex(index);
				} else {
					signal(_contradiction);
				}
			}
		}
		return;
	}
	
	public void constrainOperation () {
		List operands = getOperands();
		BoundedExpression a = (BoundedExpression)(operands.get(0));
		BoundedExpression b = (BoundedExpression)(operands.get(1));
		boolean aIsPoint = a.isPoint();
		boolean bIsPoint = b.isPoint();
		if (aIsPoint && bIsPoint) {
			String aString = ((String)(Type.STRING.getObject(a.getMinIndex())));
			String bString = ((String)(Type.STRING.getObject(b.getMinIndex())));
			String cString = aString + bString;
			int index = Type.STRING.index(cString);
			changeMinIndex(index);
			changeMaxIndex(index);
		}
		return;
	}

}

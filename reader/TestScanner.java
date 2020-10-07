/*
 * Created on Mar 21, 2005
 *
 
 */
package com.resonant.xkm.reader;

import java.util.List;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestScanner 
	extends KMObject
{
	public static void main(String[] args) {
		
		System.out.println("Testing...");

		XSyntax syntax = new XSyntax(
				"({[", ")}]", " \t", 
				",;", 
				("\\").charAt(0),
				"\"`", "\"\n", ".:"
		);
			
		// String expr = "foo(bar,baz,mumble(123 abc.xyz \"glorp\"))";
		// ExpressionScanner es = new ExpressionScanner(expr);
		// List list = es.scanExpression((";").charAt(0), syntax);
		// System.out.println(list);

		String expr2 = 
			"LoanApplication" +
			"{" +
			"  entails(Process);" +
			"  int(AvailableAmount, 10000, 900000);" +
			"  int(ApprovedAmount, 0, 1000000);" +
			"  <(AvailableAmount, ApprovedAmount); 'happy guideline!" +
			"}";
		
		System.out.println("\nSource string:");
		System.out.println(expr2);
		
		ExpressionScanner es2 = new ExpressionScanner(expr2);
		List list2 = es2.scanExpression((";").charAt(0), syntax);
		
		System.out.println("\nScanner output:");
		System.out.println(list2);

		System.out.println("\n...Test Done");
	}
}

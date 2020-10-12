/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import api.Assertion;
import contexts.Context;
import expressions.BoundedExpression;
import kb.KBObject;
import loader.Loadable;
import loader.LoadableBounds;
import loader.LoadableConstant;
import loader.LoadableConstrainment;
import loader.LoadableExpression;
import loader.LoadableLocal;
import loader.LoadableStructure;
import loader.LoadableVariable;
import operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestContradiction 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestContradiction.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestContradiction.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???	
		
		// define standard bounds for variable
		Object[] boundsBArgs = {Boolean.class};
		Loadable boundsB = new LoadableBounds(boundsBArgs);
		
		// define the variable with those bounds
		String nameB = "B";
		Object[] varBArgs = {nameB, boundsB};
		Loadable varB = new LoadableVariable(varBArgs);

		// define a reference to the variable
		Object[] refBArgs = {nameB};
		Loadable refToB = new LoadableLocal(refBArgs);
		
		// define bounds for variable
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variable with those bounds
		String nameI = "I";
		Object[] varIArgs = {nameI, boundsI};
		Loadable varI = new LoadableVariable(varIArgs);

		// define a reference to the variable
		Object[] refIArgs = {nameI};
		Loadable refToI = new LoadableLocal(refIArgs);
		
		// define a constraint 
		Object[] constantArgs6 = {new Integer(666)};
		Loadable constant6 = new LoadableConstant(constantArgs6);
		
		Object[] aGE6Args = {RootOperator.GreaterEqual, refToI, constant6};
		Loadable aGE6Expr = new LoadableExpression(aGE6Args);
		
		Object[] bGE6Args = {RootOperator.Equal, refToB, aGE6Expr};
		Loadable bGE6Expr = new LoadableExpression(bGE6Args);
		
		Object[] bGE6CTArgs = {bGE6Expr, nameB + " == (" + nameI + " >= 666)"};
		Loadable bGE6CT = new LoadableConstrainment(bGE6CTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameS = "TheCase";
		Object[] structureArgs = {nameS, 
									varB,
									varI,
									bGE6CT,
									};
		Loadable structure = new LoadableStructure(structureArgs);
		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		BoundedExpression x = (BoundedExpression)(structure.load(context));
		
		// now make it exist
		kb.reify(x);
		
		System.out.println("\nInitial KB state...");	
		System.out.println(context);
				
		System.out.println("\nNow make a path to the variables to access them...");
		// note this is just one possible way of doing this
		Object[] pathBArgs = {nameS, nameB};
		BoundedExpression varBExpr = 
			(BoundedExpression)(context.fetch(pathBArgs));

		Object[] pathIArgs = {nameS, nameI};
		BoundedExpression varIExpr = 
			(BoundedExpression)(context.fetch(pathIArgs));

		System.out.println("\nInitially:");
		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
		System.out.println(varIExpr + " = " + varIExpr.toStringValue());

 		Assertion trueAssertion = kb.newAssertion(pathBArgs, "==", Boolean.TRUE);
 		Assertion falseAssertion = kb.newAssertion(pathBArgs, "==", Boolean.FALSE);

 		Assertion assertion555 = kb.newAssertion(pathIArgs, "==", new Integer(555));
 		// Assertion assertion777 = kb.newAssertion(pathIArgs, ">=", new Integer(777));

		kb.assertAssertion(trueAssertion);
 		
		System.out.println("\nAfter asserting true:");
		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
		System.out.println(varIExpr + " = " + varIExpr.toStringValue());
		
		System.out.println("\nTrying to assert 555...");

		// try {
		//	kb.assertAssertion(assertion555);
		// } catch (Contradiction c){
		// 	System.out.println("\nCaught contradiction: " + c);
		// }
		boolean errorThrown;
		errorThrown = false;
		
		try
		{
			kb.assertAssertion(assertion555);
		}
		catch (Throwable e)
		{
			errorThrown = true;
		}
		
		assertFalse(errorThrown);
		errorThrown = false;
 		
		System.out.println("\nAfter asserting 555:");
		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
		System.out.println(varIExpr + " = " + varIExpr.toStringValue());

		System.out.println("\nFinal KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

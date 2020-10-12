/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import contexts.Context;
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
public class TestContradictionSupport 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestContradictionSupport.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestContradictionSupport.class);
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
		
		// define the variables with those bounds
		String nameD = "Declined";
		Object[] varDArgs = {nameD, boundsB};
		Loadable varD = new LoadableVariable(varDArgs);

		// define references to those variables
		Object[] refDArgs = {nameD};
		Loadable refToD = new LoadableLocal(refDArgs);
		
		// define the variables with those bounds
		String nameT = "TV";
		Object[] varTArgs = {nameT, boundsB};
		Loadable varT = new LoadableVariable(varTArgs);

		// define references to those variables
		Object[] refTArgs = {nameT};
		Loadable refToT = new LoadableLocal(refTArgs);
		
		// define bounds for variable
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variable with those bounds
		String nameW = "Weight";
		Object[] varWArgs = {nameW, boundsI};
		Loadable varW = new LoadableVariable(varWArgs);

		// define a reference to the variable
		Object[] refWArgs = {nameW};
		Loadable refToW = new LoadableLocal(refWArgs);
		
		// define a constraint 
		Object[] TimpDArgs = {RootOperator.Implies, refToT, refToD};
		Loadable TimpDExpr = new LoadableExpression(TimpDArgs);
		
		Object[] TimpDCTArgs = {TimpDExpr, nameT + " Implies " + nameD};
		Loadable TimpDCT = new LoadableConstrainment(TimpDCTArgs);
		
		// define a constraint 
		int xlimit = 4500;
		Object[] constantArgsX = {new Integer(xlimit)};
		Loadable constantX = new LoadableConstant(constantArgsX);
		
		Object[] WgtXArgs = {RootOperator.GreaterThan, refToW, constantX};
		Loadable WgtXExpr = new LoadableExpression(WgtXArgs);
		
		Object[] WgtXimpDArgs = {RootOperator.Implies, WgtXExpr, refToD};
		Loadable WgtXimpDExpr = new LoadableExpression(WgtXimpDArgs);
		
		Object[] WgtXimpDCTArgs = {WgtXimpDExpr, "(" + nameW + " > " + xlimit + ")" + " Implies " + nameD};
		Loadable WgtXimpDCT = new LoadableConstrainment(WgtXimpDCTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameS = "Structure";
		Object[] structureArgs = {nameS, 
									varD,
									varT,
									varW,
									TimpDCT,
									WgtXimpDCT,
									};
		LoadableStructure structure = new LoadableStructure(structureArgs);
		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		kb.commitAll();
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		String nameCase= "Case";
		Object[] pathCaseArgs = {nameCase};
		kb.ensureMember(pathCaseArgs, structure);
		
		System.out.println("\nAfter Structure:");
		System.out.println(context);		
				
//		System.out.println("\nNow make a path to the variables to access them...");
//		// note this is just one possible way of doing this
//		Object[] pathBArgs = {nameS, nameD};
//		BoundedExpression varBExpr = 
//			(BoundedExpression)(context.fetch(pathBArgs));
//
//		Object[] pathIArgs = {nameS, nameW};
//		BoundedExpression varIExpr = 
//			(BoundedExpression)(context.fetch(pathIArgs));
//
//		System.out.println("\nInitially:");
//		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
//		System.out.println(varIExpr + " = " + varIExpr.toStringValue());
//
// 		Assertion trueAssertion = kb.newAssertion(pathBArgs, "==", Boolean.TRUE);
// 		Assertion falseAssertion = kb.newAssertion(pathBArgs, "==", Boolean.FALSE);
//
// 		Assertion assertion555 = kb.newAssertion(pathIArgs, "==", new Integer(555));
// 		// Assertion assertion777 = kb.newAssertion(pathIArgs, ">=", new Integer(777));
//
//		kb.assertAssertion(trueAssertion);
// 		
//		System.out.println("\nAfter asserting true:");
//		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
//		System.out.println(varIExpr + " = " + varIExpr.toStringValue());
//		
//		System.out.println("\nTrying to assert 555...");
//
//		// try {
//		//	kb.assertAssertion(assertion555);
//		// } catch (Contradiction c){
//		// 	System.out.println("\nCaught contradiction: " + c);
//		// }
//		boolean errorThrown;
//		errorThrown = false;
//		
//		try
//		{
//			kb.assertAssertion(assertion555);
//		}
//		catch (Throwable e)
//		{
//			errorThrown = true;
//		}
//		
//		assertFalse(errorThrown);
//		errorThrown = false;
// 		
//		System.out.println("\nAfter asserting 555:");
//		System.out.println(varBExpr + " = " + varBExpr.toStringValue());
//		System.out.println(varIExpr + " = " + varIExpr.toStringValue());

		System.out.println("\nFinal KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

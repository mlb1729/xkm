/*
 * Created on Mar 18, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableConstant;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableEnum;
import com.resonant.xkm.loader.LoadableExpression;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableSetObject;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 *
 */
public class TestContains 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestContains.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestContains.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???
		
		Object[] constantTArgs = {new Boolean(true)};
		Loadable constantT = new LoadableConstant(constantTArgs);

		Object[] constantFArgs = {new Boolean(false)};
		Loadable constantF = new LoadableConstant(constantFArgs);

		Object[] enumDomainA = {"Enumeration 1", "foo", "bar", "baz", "mumble",};
		Object[] enumDomainB = {"Enumeration 2", "foo", "bar", "baz", "xyzzy",};
		Loadable enumA = new LoadableEnum(enumDomainA);
		Loadable enumB = new LoadableEnum(enumDomainB);
		String nameA = "A";
		String nameB = "B";
		Object[] setVariableArgsA = {nameA, enumA};
		Object[] setVariableArgsB = {nameB, enumB};
		Loadable setVariableA = new LoadableSetObject(setVariableArgsA);
		Loadable setVariableB = new LoadableSetObject(setVariableArgsB);

		String nameC = "C";
		String nameD = "D";
		Object[] setVariableArgsC = {nameC, setVariableA};
		Object[] setVariableArgsD = {nameD, setVariableB};
		Loadable setVariableC = new LoadableSetObject(setVariableArgsC);
		Loadable setVariableD = new LoadableSetObject(setVariableArgsD);

		Object[] refArgsA = {nameA};
		Loadable refA = new LoadableLocal(refArgsA);
		Object[] refArgsB = {nameB};
		Loadable refB = new LoadableLocal(refArgsB);
		Object[] refArgsC= {nameC};
		Loadable refC = new LoadableLocal(refArgsC);
		Object[] refArgsD = {nameD};
		Loadable refD = new LoadableLocal(refArgsD);

		// now make a constraint
		
		Object[] containsExprArgs = {RootOperator.Contains, refD, refB};
		Loadable containsExpr = new LoadableExpression(containsExprArgs);
		
		Object[] containsExprCTArgs = {containsExpr, "Set " + nameD + " Contains Set " + nameB};
		Loadable containsExprCT = new LoadableConstrainment(containsExprCTArgs);
		
		// and load it all up
		Object[] structureArgs = {"TestCase", 
				setVariableA, 
				setVariableB,
				setVariableC,
				setVariableD,
				containsExprCT,
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
		
		System.out.println("\nCurrent KB state...");	
		System.out.println(context);
		
		System.out.println("\nNote that since the structure doesn't definitely exist");	
		System.out.println(" none of its constraints are enforced (= unknown)");	
		
		System.out.println("\nNow force the top-level model to definitely exist");	
		x.changeToBe(true);
		kb.commitAll();
		
		System.out.println("Current KB state...");	
		System.out.println(context);
		
//		System.out.println("\nNote that since the structure now definitely exists");	
//		System.out.println(" all of its constraints are enforced (= true)");	
//		
//		System.out.println("\nNow make paths to some element variables to access them...");
//		// note this is just one possible way of doing this
//		Object[] pathAArgs = {"TestCase", nameA, "foo"};
//		Object[] pathBArgs = {"TestCase", nameB, "bar"};
//		BoundedExpression elementAVar = (BoundedExpression)(context.fetch(pathAArgs));
//		BoundedExpression elementBVar = (BoundedExpression)(context.fetch(pathBArgs));
//
//		System.out.println(elementAVar);
// 		System.out.println(elementAVar.toStringValue());
//
//		System.out.println(elementBVar);
// 		System.out.println(elementBVar.toStringValue());
// 		
//		System.out.println("\nNext create & enforce runtime constraints on variables");
//		// perhaps this is set as a result of a credit rating activity
//		// note this is just one possible way of doing this
//		Loadable refPathA = new LoadableLocal(pathAArgs);
//		Object[] aFArgs = {"==", refPathA, constantF};
//		Loadable aFExpr = new LoadableExpression(aFArgs);
//		BoundedExpression y = (BoundedExpression)(aFExpr.load(context));
//		y.changeToBe(true);
//		
//		Loadable refPathB = new LoadableLocal(pathBArgs);
//		Object[] bTArgs = {"==", refPathB, constantT};
//		Loadable bTExpr = new LoadableExpression(bTArgs);
//		BoundedExpression z = (BoundedExpression)(bTExpr.load(context));
//		z.changeToBe(true);
		
//		kb.commitAll();
		
		System.out.println("\nFinal KB state...");	
		System.out.println(context);		
				
		System.out.println("\n...Test Done");
	}
	
}

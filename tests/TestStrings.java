/*
 * Created on May 2, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

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

/**
 * @author MLB
 *
 *
 */
public class TestStrings 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestStrings.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestStrings.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???
		
		// define some basic constants
		Object[] constantArgs60 = {"sixty"};
		Loadable constant60 = new LoadableConstant(constantArgs60);

		Object[] constantArgs9 = {"nine"};
		Loadable constant9 = new LoadableConstant(constantArgs9);
		
//		Object[] constantArgs69 = {"sixtynine"};
//		Loadable constant69 = new ModelConstant(constantArgs69);
		
		Object[] constantArgs960 = {"ninesixty"};
		Loadable constant960 = new LoadableConstant(constantArgs960);
		
		Object[] constantArgs999 = {"nineninenine"};
		Loadable constant999 = new LoadableConstant(constantArgs999);
		
		Object[] constantArgsXYZZY = {"XYZZY"};
		Loadable constantXYZZY = new LoadableConstant(constantArgsXYZZY);
		
		
		// define standard bounds for variables
		Object[] boundsArgs = {String.class};
		Loadable bounds = new LoadableBounds(boundsArgs);
		
		// define variables with those basic bounds
		Object[] varArgsA = {"StringA", bounds};
		Loadable intVarA = new LoadableVariable(varArgsA);
		Object[] refArgsA = {"StringA"};
		Loadable refA = new LoadableLocal(refArgsA);
		
		Object[] varArgsB = {"StringB", bounds};
		Loadable intVarB = new LoadableVariable(varArgsB);
		Object[] refArgsB = {"StringB"};
		Loadable refB = new LoadableLocal(refArgsB);
		
		Object[] varArgsC = {"StringC", bounds};
		Loadable intVarC = new LoadableVariable(varArgsC);
		Object[] refArgsC = {"StringC"};
		Loadable refC = new LoadableLocal(refArgsC);

		// define some initial lower and upped bound constraints on a variable
		// perhaps these might be driven by the loan product being modeled

		Object[] aEQ6Args = {"==", refA, constant60};
		Loadable aEQ6Expr = new LoadableExpression(aEQ6Args);
		Object[] aEQ6CTArgs = {aEQ6Expr, "A = 'sixty'"};
		Loadable aEQ6CT = new LoadableConstrainment(aEQ6CTArgs);
		
		Object[] bEQ9Args = {"==", refB, constant9};
		Loadable bEQ9Expr = new LoadableExpression(bEQ9Args);
		Object[] bEQ9CTArgs = {bEQ9Expr, "B = 'nine'"};
		Loadable bEQ9CT = new LoadableConstrainment(bEQ9CTArgs);
		
		// define a basic relationship constraint between variables
		// perhaps this reflects a lending guideline

		Object[] aStrCatbArgs = {"s+", refA, refB};
		Loadable aStrCatbExpr = new LoadableExpression(aStrCatbArgs);
		Object[] cEQStrCatArgs = {"==", refC, aStrCatbExpr};
		Loadable cEQStrCatExpr = new LoadableExpression(cEQStrCatArgs);

		Object[] cStrCatCTArgs = {cEQStrCatExpr, "C = A + B"};
		Loadable cStrCatCT = new LoadableConstrainment(cStrCatCTArgs);
		

		// define & display a model structure with those variables and constraints
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"LoanApplication", 
									intVarA, 
									intVarB, 
									intVarC, 
									aEQ6CT,
									bEQ9CT,
									cStrCatCT, 
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
		
		System.out.println("\nNote that since the structure now definitely exists");	
		System.out.println(" all of its constraints are enforced (= true)");	

		System.out.println("\n...Test Done");
	}

}

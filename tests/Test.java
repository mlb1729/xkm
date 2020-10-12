/*
 * Created on Mar 9, 2005
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
import operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Test 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(Test.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(Test.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???
		
		// define some basic constants
		Object[] constantArgs1 = {new Integer(10000)};
		Loadable constant1 = new LoadableConstant(constantArgs1);

		Object[] constantArgs6 = {new Integer(600000)};
		Loadable constant6 = new LoadableConstant(constantArgs6);
		
		Object[] constantArgs9 = {new Integer(900000)};
		Loadable constant9 = new LoadableConstant(constantArgs9);
		
		// define standard bounds for variables
		Object[] boundsArgs = {new Integer(0), new Integer(1000000)};
		Loadable bounds = new LoadableBounds(boundsArgs);
		
		// define variables with those basic bounds
		Object[] varArgsA = {"AvailableAmount", bounds};
		Loadable intVarA = new LoadableVariable(varArgsA);

		Object[] refArgsA = {"AvailableAmount"};
		Loadable refA = new LoadableLocal(refArgsA);
		
		Object[] varArgsB = {"ApprovedAmount", bounds};
		Loadable intVarB = new LoadableVariable(varArgsB);
		
		Object[] refArgsB = {"ApprovedAmount"};
		Loadable refB = new LoadableLocal(refArgsB);

		// define some initial lower and upped bound constraints on a variable
		// perhaps these might be driven by the loan product being modeled

		Object[] aGE1Args = {RootOperator.GreaterEqual, refA, constant1};
		Loadable aGE1Expr = new LoadableExpression(aGE1Args);
		
		Object[] aGE1CTArgs = {aGE1Expr, "AvailableAmount >= 10000"};
		Loadable aGE1CT = new LoadableConstrainment(aGE1CTArgs);
		
		Object[] aLE9Args = {RootOperator.LessEqual, refA, constant9};
		Loadable aLE9Expr = new LoadableExpression(aLE9Args);
		
		Object[] aLE9CTArgs = {aLE9Expr, "AvailableAmount <= 900000"};
		Loadable aLE9CT = new LoadableConstrainment(aLE9CTArgs);

		// define a basic relationship constraint between variables
		// perhaps this reflects a lending guideline
		// Note strict inequality (just for fun)(gotta have that $1 margin!)
		Object[] aLTbArgs = {RootOperator.GreaterThan, refA, refB};
		Loadable aLTbExpr = new LoadableExpression(aLTbArgs);
		
		Object[] aLTbCTArgs = {aLTbExpr, "AvailableAmount < ApprovedAmount"};
		Loadable aLTbCT = new LoadableConstrainment(aLTbCTArgs);

		// define & display a model structure with those variables and constraints
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"LoanApplication", 
									intVarA, 
									intVarB, 
									aGE1CT, 
									aLE9CT, 
									aLTbCT, 
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
		System.out.println("\n none of its constraints are enforced (= unknown)");	
		
		System.out.println("\nNow force the top-level model to definitely exist");	
		x.changeToBe(true);
		kb.commitAll();
		
		System.out.println("Current KB state...");	
		System.out.println(context);
		
		System.out.println("\nNote that since the structure now definitely exists");	
		System.out.println("\n all of its constraints are enforced (= true)");	
		
		System.out.println("\nNow make a path to a variable and access it...");
		// note this is just one possible way of doing this
		Object[] pathBArgs = {"LoanApplication", "ApprovedAmount"};
		BoundedExpression var = 
			(BoundedExpression)(context.fetch(pathBArgs));
		System.out.println(var);
 		System.out.println(var.toStringValue());
		System.out.println(var.getMinIndex());
		System.out.println(var.getMaxIndex());

		System.out.println("\nNext create & enforce a runtime constraint on that variable");
		System.out.println("\nNote that the request is outside of the structure context");
		// perhaps this is set as a result of a credit rating activity
		// note this is just one possible way of doing this
		Loadable refPathB = new LoadableLocal(pathBArgs);
		Object[] bLE6Args = {"<=", refPathB, constant6};	// Hi Dave!
		Loadable bLE6Expr = new LoadableExpression(bLE6Args);
		Object[] bLE6CTArgs = {bLE6Expr, "ApprovedAmount LE 600000"};
		Loadable bLE6CT = new LoadableConstrainment(bLE6CTArgs);
		BoundedExpression y = (BoundedExpression)(bLE6CT.load(context));
		y.changeToBe(true);
		kb.commitAll();
		
		System.out.println("\nCurrent KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

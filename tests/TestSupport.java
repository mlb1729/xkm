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
public class TestSupport 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestSupport.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestSupport.class);
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
		
		// define standard bounds for variable
		Object[] boundsArgs = {new Integer(0), new Integer(1000000)};
		Loadable bounds = new LoadableBounds(boundsArgs);
		
		// define variable with those basic bounds
		Object[] varArgsA = {"Amount", bounds};
		Loadable intVarA = new LoadableVariable(varArgsA);

		Object[] refArgsA = {"Amount"};
		Loadable refA = new LoadableLocal(refArgsA);
		
		// define multiple bound constraints on  variable

		Object[] aGE1Args = {RootOperator.GreaterEqual, refA, constant1};
		Loadable aGE1Expr = new LoadableExpression(aGE1Args);
		
		Object[] aGE1CTArgs = {aGE1Expr, "Amount >= 10000"};
		Loadable aGE1CT = new LoadableConstrainment(aGE1CTArgs);
		
		Object[] aGE6Args = {RootOperator.GreaterEqual, refA, constant6};
		Loadable aGE6Expr = new LoadableExpression(aGE6Args);
		
		Object[] aGE6CTArgs = {aGE6Expr, "Amount >= 600000"};
		Loadable aGE6CT = new LoadableConstrainment(aGE6CTArgs);
		
		Object[] aLE9Args = {RootOperator.LessEqual, refA, constant9};
		Loadable aLE9Expr = new LoadableExpression(aLE9Args);
		
		Object[] aLE9CTArgs = {aLE9Expr, "Amount <= 900000"};
		Loadable aLE9CT = new LoadableConstrainment(aLE9CTArgs);

		// define & display a model structure with those variables and constraints
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"Test", 
									intVarA, 
									aGE1CT, 
									aGE6CT, 
									aLE9CT, 
									};
		Loadable structure = new LoadableStructure(structureArgs);
		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB and reify
		BoundedExpression x = (BoundedExpression)(structure.load(context));
		x.changeToBe(true);
		kb.commitAll();
		
		System.out.println("Current KB state...");	
		System.out.println(context);
			
		System.out.println("\nNow make a path to a variable and access it...");
		// note this is just one possible way of doing this
		Object[] pathAArgs = {"Test", "Amount"};
		BoundedExpression var = 
			(BoundedExpression)(context.fetch(pathAArgs));
		System.out.println(var);
 		System.out.println(var.toStringValue());
		System.out.println(var.getMinIndex());
		System.out.println(var.getMaxIndex());

		System.out.println("\nSupport for variable...");	
		System.out.println(var + ": " + var.getAllSupport());		
		
		System.out.println("\nCurrent KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

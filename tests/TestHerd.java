/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import api.Assertion;
import contexts.Context;
import kb.KBObject;
import loader.Loadable;
import loader.LoadableBounds;
import loader.LoadableConstant;
import loader.LoadableConstrainment;
import loader.LoadableExpression;
import loader.LoadableHerd;
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
public class TestHerd
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestHerd.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestHerd.class);
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
		Object[] structureArgs = {"Poopydinkle", 
									varB,
									varI,
									bGE6CT,
									};
		LoadableStructure structure = new LoadableStructure(structureArgs);
		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		// now we are ready to simulate runtime in a real process
		Object[] shellArgs = {"EmptyShell"};
		LoadableStructure shell = new LoadableStructure(shellArgs);
		
		Object[] herdArgs = {"Herd", structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);
		
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		// BoundedExpression x = (BoundedExpression)(structure.load(context));
		
		// now make it exist
		// kb.reify(x);
		kb.commitAll();
		
		String nameCase = "Case";
		String nameFoo = "Things";
		String nameBar = "1";
		String nameBaz = "2";
		
		// Object[] pathRootArgs = {};
		Object[] pathCaseArgs = {nameCase};
		Object[] pathFooArgs = {nameCase, nameFoo};
		Object[] pathBarArgs = {nameCase, nameFoo, nameBar};
 		Object[] pathBazArgs = {nameCase, nameFoo, nameBaz};
//		Object[] pathBazArgs = {nameCase, nameFoo, null};
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		kb.ensureMember(pathCaseArgs, shell);
		kb.ensureMember(pathFooArgs, herd);
		
		System.out.println("\nAfter Foo:");
		System.out.println(context);		

		
		kb.ensureMember(pathBarArgs, structure);
		kb.ensureMember(pathBazArgs, structure);

		System.out.println("\nAfter Bar & Baz:");
		System.out.println(context);		

		Object[] pathBarBArgs = {nameCase, nameFoo, nameBar, nameB};
		Object[] pathBazBArgs = {nameCase, nameFoo, nameBaz, nameB};
	
	    Assertion trueAssertion = kb.newAssertion(pathBarBArgs, "==", Boolean.TRUE);
 		Assertion falseAssertion = kb.newAssertion(pathBazBArgs, Boolean.FALSE);

	    Assertion fooAssertion = kb.newAssertion(pathFooArgs);
	    Assertion barAssertion = kb.newAssertion(pathBarArgs);
	    Assertion bazAssertion = kb.newAssertion(pathBazArgs);
	   
//	    System.out.println("\nReifying Foo...:");
//		kb.assertAssertion(fooAssertion);
//		System.out.println(context);		
		 		
	    System.out.println("\nAsserting vars...:");
		kb.assertAssertion(trueAssertion);
		kb.assertAssertion(falseAssertion);
		System.out.println(context);		
		 		
//	    System.out.println("\nReifying Bar & Baz...:");
//		kb.assertAssertion(barAssertion);
//		kb.assertAssertion(bazAssertion);
//		System.out.println(context);		
	 		
		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));
		
		System.out.println("\n...Test Done");
	}
}

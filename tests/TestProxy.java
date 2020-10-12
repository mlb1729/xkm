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
public class TestProxy
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestProxy.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestProxy.class);
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
		// System.out.println("\nModel definition...");
		// System.out.println(structure);

		String nameJ = "J";
		Object[] varJArgs = {nameJ, boundsI};
		Loadable varJ = new LoadableVariable(varJArgs);

		// define a reference to the variable
		Object[] refJArgs = {nameJ};
		Loadable refToJ = new LoadableLocal(refJArgs);
		
		String nameCase = "Case";
		String nameHerd = "Things";

		Object[] herdArgs = {nameHerd, structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);
		
		// pattern stuff
		Object[] trueBoundsArgs = {Boolean.TRUE};
		Loadable trueBounds = new LoadableConstant(trueBoundsArgs);
		
		Object[] varBTrueArgs = {nameB, trueBounds};
		Loadable varBTrue = new LoadableVariable(varBTrueArgs);

		Object[] patternArgs = {"Pattern", varBTrue};
		LoadableStructure pattern = new LoadableStructure(patternArgs);
		
		// proxy stuff
		Object[] refPatternIArgs = {nameHerd, pattern, nameI};
		Loadable refToPatternI = new LoadableLocal(refPatternIArgs);
		
		Object[] jEQiExprArgs = {RootOperator.Equal, refToJ, refToPatternI};
		Loadable jEQiExpr = new LoadableExpression(jEQiExprArgs);
		
		Object[] jEQiCTArgs = {jEQiExpr, "J == matching I"};
		Loadable jEQiCT = new LoadableConstrainment(jEQiCTArgs);
		
		Object[] shellArgs = {nameCase, varJ, herd, jEQiCT};
		LoadableStructure shell = new LoadableStructure(shellArgs);
		
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		// BoundedExpression x = (BoundedExpression)(structure.load(context));
		
		// now make it exist
		// kb.reify(x);
		kb.commitAll();
		
		String nameBar = "1";
		String nameBaz = "2";
		String namePattern = "Pattern";
		
		// Object[] pathRootArgs = {};
		Object[] pathCaseArgs = {nameCase};
		Object[] pathFooArgs = {nameCase, nameHerd};
		Object[] pathBarArgs = {nameCase, nameHerd, nameBar};
 		Object[] pathBazArgs = {nameCase, nameHerd, nameBaz};
//		Object[] pathBazArgs = {nameCase, nameFoo, null};
		Object[] pathPatternArgs = {nameCase, namePattern};
		
		// and now back to our regularly scheduled program
		kb.ensureMember(pathCaseArgs, shell);
		kb.commitAll();
		
		System.out.println("\nInitially:");
		System.out.println(context);		


		kb.ensureMember(pathBarArgs, structure);
		kb.ensureMember(pathBazArgs, structure);

		System.out.println("\nAfter Structures, Before Assertions:");
		System.out.println(context);		

		Object[] pathBarBArgs = {nameCase, nameHerd, nameBar, nameB};
		Object[] pathBazBArgs = {nameCase, nameHerd, nameBaz, nameB};
	
	    Assertion trueAssertion = kb.newAssertion(pathBarBArgs, Boolean.TRUE);
 		Assertion falseAssertion = kb.newAssertion(pathBazBArgs, Boolean.FALSE);

	    Assertion fooAssertion = kb.newAssertion(pathFooArgs);
	    
	    Assertion barAssertion = kb.newAssertion(pathBarArgs);
	    Assertion bazAssertion = kb.newAssertion(pathBazArgs);
		 		
	    System.out.println("\nAsserting vars...:");
		kb.assertAssertion(trueAssertion);
		kb.assertAssertion(falseAssertion);
		System.out.println(context);		
		 			 		
		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));
			
		System.out.println(context);		
		System.out.println("\n...Test Done");
	}
}

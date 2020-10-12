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
public class TestNew
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestNew.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestNew.class);
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
		
		
		String nameHerd = "Nerd";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
		
		Object[] qtyExprArgs = {RootOperator.Quantity, refToHerd};
		Loadable qtyExpr = new LoadableExpression(qtyExprArgs);
		
		String nameQ = "Q";
		Object[] varQArgs = {nameQ, boundsI};
		Loadable varQ = new LoadableVariable(varQArgs);

		Object[] refToQArgs = {nameQ};
		Loadable refToQ = new LoadableLocal(refToQArgs);
		
		Object[] qExprArgs = {RootOperator.Equal, refToQ, qtyExpr};
		Loadable qExpr = new LoadableExpression(qExprArgs);
		
		Object[] qCTArgs = {qExpr, "Q is the quantity of the herd."};
		Loadable qCT = new LoadableConstrainment(qCTArgs);
		
		Object[] herdArgs = {nameHerd, structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);


		Object[] shellArgs = {"Shell", 
				herd, 
				varQ, 
				qCT,
				};
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
		
		System.out.println("\nAfter Shell:");
		System.out.println(context);		
		
		Object[] pathHerdArgs = {nameCase, nameHerd};

		// Assertion herdAssertion = kb.newAssertion(pathHerdArgs);
		// kb.assertAssertion(herdAssertion);
			
		System.out.println("\nAfter Herd:");
		System.out.println(context);			
		
		String nameThing1 = "ABC";
		String nameThing2 = "XYZ";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
	    System.out.println("\nAdding first to herd...:");
		kb.ensureMember(pathHerd1Args, structure);
		System.out.println(context);		
		
	    System.out.println("\nAdding second to herd...:");
		kb.ensureMember(pathHerd2Args, structure);
		System.out.println(context);
		
		Object[] pathBoolean1Args = {nameCase, nameHerd, nameThing1, nameB};
		Object[] pathBoolean2Args = {nameCase, nameHerd, nameThing2, nameB};

		Assertion boolean1Assertion = kb.newAssertion(pathBoolean1Args, Boolean.TRUE);
		Assertion boolean2Assertion = kb.newAssertion(pathBoolean2Args, Boolean.FALSE);
		
		kb.assertAssertion(boolean1Assertion);
		kb.assertAssertion(boolean2Assertion);
		
		System.out.println("\nAfter assertions:");
		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		System.out.println("\n...Test Done");

	}
}

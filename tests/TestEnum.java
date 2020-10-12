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
import loader.LoadableConstrainment;
import loader.LoadableEnum;
import loader.LoadableExpression;
import loader.LoadableLocal;
import loader.LoadableSetObject;
import loader.LoadableStructure;
import operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestEnum 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestEnum.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestEnum.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");
		
		String nameEnum1 = "Enum #1";
		String nameX = "XXX";
		String nameY = "YYY";
		String nameZ = "ZZZ";
		Object[] enum1Domain = {nameEnum1, nameX, nameY, nameZ};
		Loadable enum1 = new LoadableEnum(enum1Domain);
		
		String nameEnum2 = "Enum #2";
		Object[] enum2Domain = {nameEnum2, nameX, };
		Loadable enum2 = new LoadableEnum(enum2Domain);
		
		String nameEnumAVar = "Enum Var A";
		Object[] varEnumAArgs = {nameEnumAVar, enum1,};
		Loadable varEnumA = new LoadableSetObject(varEnumAArgs);
		Object[] pathEnumAArgs = {nameEnumAVar};
		Loadable pathEnumA = new LoadableLocal(pathEnumAArgs);
		
		String nameEnumBVar = "Enum Var B";
		Object[] varEnumBArgs = {nameEnumBVar, enum1,};
		Loadable varEnumB = new LoadableSetObject(varEnumBArgs);
		Object[] pathEnumBArgs = {nameEnumBVar};
		Loadable pathEnumB = new LoadableLocal(pathEnumBArgs);
		
		Object[] setEqExprArgs = {RootOperator.SetEqual, pathEnumA, pathEnumB};
		Loadable setEqExpr = new LoadableExpression(setEqExprArgs);
		
		Object[] setEqCTArgs = {setEqExpr, "Enum var A == Enum var B"};
		Loadable setEqCT = new LoadableConstrainment(setEqCTArgs);
	
		Object[] setGeExprArgs = {RootOperator.Contains, pathEnumA, enum2};
		Loadable setGeExpr = new LoadableExpression(setGeExprArgs);
		
		Object[] setGeCTArgs = {setGeExpr, "Enum var A Contains X"};
		Loadable setGeCT = new LoadableConstrainment(setGeCTArgs);
	
		String nameApplication = "Application";
		Object[] applicationArgs = {nameApplication,
										varEnumA, 
										varEnumB,
										setEqCT,
										setGeCT,
										};
		LoadableStructure application = new LoadableStructure(applicationArgs);

		// *** session ***

		System.out.println("\nModel definition...");
		System.out.println(application);
		
		// now we are ready to simulate runtime in a real process
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		kb.commitAll();
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		String nameCase = "Case";
		Object[] pathCaseArgs = {nameCase};
		kb.ensureMember(pathCaseArgs, application);
		
		System.out.println("\nAfter Case:");
		System.out.println(context);		
		
		Object[] pathAYArgs = {nameCase, nameEnumAVar, nameY};
		Assertion assertAY = kb.newAssertion(pathAYArgs, "==", Boolean.TRUE);
		
		Object[] pathBZArgs = {nameCase, nameEnumBVar, nameZ};
		Assertion assertBZ = kb.newAssertion(pathBZArgs, "==", Boolean.FALSE);
		
		System.out.println("\nAsserting");	
		System.out.println(assertAY);		
		System.out.println(assertBZ);		
		
		kb.assertAssertion(assertAY);	
		kb.assertAssertion(assertBZ);

		System.out.println("\nFinally:");
		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		System.out.println("\n...Test Done");
	}
}

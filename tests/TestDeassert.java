/*
 * Created on Mar 9, 2005
 *
 
 */
package com.resonant.xkm.tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.api.Interval;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableExpression;
import com.resonant.xkm.loader.LoadableHerd;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableMember;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestDeassert
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestRequired.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestDeassert.class);
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
		String nameB = "Boo";
		Object[] varBArgs = {nameB, boundsB};
		Loadable varB = new LoadableVariable(varBArgs);

		// define a reference to the variable
		Object[] refBArgs = {nameB};
		Loadable refToB = new LoadableLocal(refBArgs);
		
		// define the variable with those bounds
		String nameC = "Coo";
		Object[] varCArgs = {nameC, boundsB};
		Loadable varC = new LoadableVariable(varCArgs);

		// define a reference to the variable
		Object[] refCArgs = {nameC};
		Loadable refToC = new LoadableLocal(refCArgs);
			
		// define the variable with those bounds
		String nameD = "Doo";
		Object[] varDArgs = {nameD, boundsB};
		Loadable varD = new LoadableVariable(varDArgs);

		// define a reference to the variable
		Object[] refDArgs = {nameD};
		Loadable refToD = new LoadableLocal(refDArgs);
			
		// define a constraint 	
		Object[] notBArgs = {RootOperator.Not, refToB};
		Loadable notBExpr = new LoadableExpression(notBArgs);
		
		Object[] equalArgs = {RootOperator.Equal, refToC, notBExpr};
		Loadable equalExpr = new LoadableExpression(equalArgs);
		
		Object[] equalCTArgs = {equalExpr, nameC + " == not(" + nameB + ")"};
		Loadable equalCT = new LoadableConstrainment(equalCTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameStruct = "Struct";
		Object[] structureArgs = {nameStruct, 
				varB,
				varC,
				// varD,
				equalCT,
				};
		// LoadableStructure structure = new LoadableStructure(structureArgs);
		LoadableMember structure = new LoadableMember(structureArgs);
		System.out.println("\nStructure definition...");
		System.out.println(structure);
		

		String nameHerd = "Frobs";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
		
		Object[] herdArgs = {nameHerd, structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);
		
		String nameCase = "Case";
		Object[] pathHerdArgs = {nameCase, nameHerd};
	
		// put it all together...
		Object[] shellArgs = {"Shell", 
				herd,
//				structure,
				};
		LoadableMember shell = new LoadableMember(shellArgs);
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		kb.commitAll();
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		Object[] pathCaseArgs = new Object[]{nameCase};
		kb.ensureMember(pathCaseArgs, shell);
		
		System.out.println("\nAfter Shell:");
		System.out.println(context);		
		
//		Assertion herdAssertion = kb.newAssertion(pathHerdArgs);
//		kb.assertAssertion(herdAssertion);
//		
//		System.out.println("\nAfter Herd:");
//		System.out.println(context);			
		
		String nameThing1 = "Bob";
		String nameThing2 = "Ray";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
//	    System.out.println("\nAdding first to herd...:");
		kb.ensureMember(pathHerd1Args, structure);
//		System.out.println(context);		
//		
//		System.out.println("\nAdding second to herd...:");
		kb.ensureMember(pathHerd2Args, structure);
//		System.out.println(context);
		
//		Object[] pathBoolean1Args = {nameCase, nameStruct, nameB};
//		Object[] pathBoolean2Args = {nameCase, nameStruct, nameC};
		
		Object[] pathBoolean1Args = {nameCase, nameHerd, nameThing1, nameB};
		Object[] pathBoolean2Args = {nameCase, nameHerd, nameThing2, nameB};

		Assertion boolean1Assertion = kb.newAssertion(pathBoolean1Args, Boolean.TRUE);
		Assertion boolean2Assertion = kb.newAssertion(pathBoolean2Args, Boolean.FALSE);
//		Assertion boolean2Assertion = kb.newAssertion(pathBoolean1Args, Boolean.FALSE);
		
		kb.commitAll();

		kb.assertAssertion(boolean1Assertion);
		System.out.println("\nAfter 1st assertion:");
		System.out.println(context);		

		kb.assertAssertion(boolean2Assertion);	
		System.out.println("\nAfter 2nd assertion:");
		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		Interval interval = kb.getVariableInterval(new Object[] { 
				nameCase,
				nameHerd,
				nameThing1,
				nameC});
		
		System.out.println("\nInterval: " + interval);
		// System.out.println(context);	
		
		Object[] structureNames = kb.getStructureNames();
		System.out.println("\nTop level structures: " + toString(structureNames));		

		System.out.println("\n...Test Done");
		
//		System.out.println("\n" + nameX + " : " + nameX.hashCode());
//		System.out.println("\n" + nameXX + " : " + nameXX.hashCode());
//		System.out.println("\n" + getName(nameX).equals(getName(nameXX)));
	}
}

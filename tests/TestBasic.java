/*
 * Created on Mar 9, 2005
 *
 
 */
package com.resonant.xkm.tests;

import org.apache.log4j.Logger;

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
public class TestBasic
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestBasic.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestBasic.class);
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
		String nameA = "Aa";
		Object[] varAArgs = {nameA, boundsB};
		Loadable varA = new LoadableVariable(varAArgs);

		String nameB = "Bb";
		Object[] varBArgs = {nameB, boundsB};
		Loadable varB = new LoadableVariable(varBArgs);

		// define a reference to the variables
		Object[] refAArgs = {nameA};
		Loadable refToA = new LoadableLocal(refAArgs);
		
		Object[] refBArgs = {nameB};
		Loadable refToB = new LoadableLocal(refBArgs);
	
		// define a constraint 
		Object[] aLTbArgs = {RootOperator.LessThan, refToA, refToB};
		Loadable aLTbExpr = new LoadableExpression(aLTbArgs);
			
		Object[] aLTbCTArgs = {aLTbExpr, nameA + " < " + nameB};
		Loadable aLTbCT = new LoadableConstrainment(aLTbCTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"Frobozz", 
									varA,
									varB,
									aLTbCT,
									};
		LoadableStructure structure = new LoadableStructure(structureArgs);

		String nameCase = "Case";
		String nameFoo = "Things";
		String nameBar = "1";
		String nameBaz = "2";
		
		// Object[] pathRootArgs = {};
		Object[] pathCaseArgs = {nameCase};
		Object[] pathFooArgs = {nameCase, nameFoo};
		Object[] pathBarArgs = {nameCase, nameFoo, nameBar};
 		Object[] pathBazArgs = {nameCase, nameFoo, nameBaz};

		String nameHerd = "Frobs";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
		
		Object[] herdArgs = {nameHerd, structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);
		
		Object[] pathHerdArgs = {nameCase, nameHerd};

		
		// put it all together...
		
		Object[] shellArgs = {"Shell", 
				herd,
				};
		LoadableMember shell = new LoadableMember(shellArgs);
		
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		kb.commitAll();	
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		kb.ensureMember(pathCaseArgs, shell);
		kb.commitAll();
		
		System.out.println("\nAfter Shell:");
		System.out.println(context);		
		
		String nameThing1 = "Foo";
		String nameThing2 = "Pheaux";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
	    System.out.println("\nAdding first to herd...:");
		kb.ensureMember(pathHerd1Args, structure);
		kb.commitAll();
		System.out.println(context);		
		
		System.out.println("\nAdding second to herd...:");
		kb.ensureMember(pathHerd2Args, structure);
		kb.commitAll();
		System.out.println(context);
		
		Object[] pathBoolean1Args = {nameCase, nameHerd, nameThing1, nameB};
		Object[] pathBoolean2Args = {nameCase, nameHerd, nameThing2, nameB};

//		Assertion boolean1Assertion = kb.newAssertion(pathBoolean1Args, Boolean.TRUE);
//		Assertion boolean2Assertion = kb.newAssertion(pathBoolean2Args, Boolean.FALSE);
//		
//		kb.assertAssertion(boolean1Assertion);
//		kb.assertAssertion(boolean2Assertion);
//		
//		System.out.println("\nAfter assertions:");
//		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));
		
		Object[] structureNames = kb.getStructureNames();
		System.out.println("\nTop level structures: " + toString(structureNames));		

		System.out.println("\n...Test Done");
		
	}
}

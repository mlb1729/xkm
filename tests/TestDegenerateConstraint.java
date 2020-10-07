/*
 * Created on Mar 9, 2005
 *
 
 */
package com.resonant.xkm.tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestDegenerateConstraint 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestDegenerateConstraint.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestDegenerateConstraint.class);
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
			
		Object[] bCTArgs = {refToB, "Let there be B"};
		Loadable bCT = new LoadableConstrainment(bCTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameS = "TheCase";
		Object[] structureArgs = {nameS, 
									varB,
									bCT,
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
		
		// now make it exist
		kb.reify(x);
		
		System.out.println("\nInitial KB state...");	
		System.out.println(context);
				
		System.out.println("\nNow make a path to the variables to access them...");
		// note this is just one possible way of doing this
		Object[] pathBArgs = {nameS, nameB};
		BoundedExpression varBExpr = 
			(BoundedExpression)(context.fetch(pathBArgs));

		System.out.println("\nInitially:");
		System.out.println(varBExpr + " = " + varBExpr.toStringValue());

		System.out.println("\n...Test Done");
	}
}

/*
 * Created on May 4, 2005
 *
 
 */
package com.resonant.xkm.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableExpression;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 *
 */

public class TestTree 
	extends TestCase 
{
   private static final Logger log = Logger.getLogger(TestTree.class);
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(NotTest.class));
		junit.textui.TestRunner.run(TestTree.class);
	}
	
	private Loadable[] getPair(int level, int index, List structureArgs) {
		Loadable[] pair = new Loadable[2];
		index = index*2;
		
		if (level == 0) {
			Object[] andArgs = {RootOperator.And, 
									getLeafRef(index, structureArgs), 
									getLeafRef(index+1, structureArgs)};
			pair[0] = new LoadableExpression(andArgs);
			pair[1] = new LoadableExpression(andArgs);					
		} else {
			Object[] pair0 = getPair(level-1, index, structureArgs);
			Object[] pair1 = getPair(level-1, index+1, structureArgs);
			for (int i=0; i<2; i++) {
				Object[] andArgs = {RootOperator.And, pair0[i], pair1[i]};
				pair[i] = new LoadableExpression(andArgs);
			}
		}
		return pair;
	}
	
	private Loadable getLeafRef(int index, List structureArgs) {
		// make pretty var name
		String varName = "LeafVar" + index;
		
		// define standard bounds for Boolean variables
		Object[] boundsArgs = {Boolean.class};
		Loadable varBounds = new LoadableBounds(boundsArgs);

		// define as Boolean
		Object[] varArgs = {varName, varBounds};
		Loadable varSpec = new LoadableVariable(varArgs);
		
		// add local var to structure spec
		structureArgs.add(varSpec);
		
		// create a local reference to var
		Object[] refArgs = {varName};
		Loadable refSpec = new LoadableLocal(refArgs);
		
		return refSpec;
	}
	
	public void testSample()
	{
		try
		{
			long beforeTime = 0;
			long afterTime = 0;
			
			int L = 13;		// change this to scale test
			int K = (1 << L);
			
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nTesting times for tree with " + (L+1) + " levels, " + 2*K + " total nodes...");
			}
					
			beforeTime = System.currentTimeMillis();
			
			// Strategy: set up 2 binary trees of L levels of ANDs, connected at their leaves by 2^L boolean variables 
			// Set one root to true (time propagation) and check result at other root.
			
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			// define standard bounds for Boolean variables
			Object[] boundsArgs = {Boolean.class};
			Loadable varBounds = new LoadableBounds(boundsArgs);
						
			Object[][] rootPathList = new Object[2][];	// external paths to runtime var
			
			Loadable[] tree = getPair(L, 0, structureArgs);
			
			for (int i=0; i<2; i++) {
				// make pretty var name
				String rootVarName = "RootVar" + i;
				
				// define as Boolean
				Object[] varArgs = {rootVarName, varBounds};
				Loadable rootVarSpec = new LoadableVariable(varArgs);
				
				// add root var to structure spec
				structureArgs.add(rootVarSpec);
				
				// create a local reference to var
				Object[] refArgs = {rootVarName};
				Loadable rootRefSpec = new LoadableLocal(refArgs);
				
				// create a path to be used to access instance in the KB
				Object[] pathArgs = {structureName, rootVarName};
				rootPathList[i] = pathArgs;
				
				// equate with tree i
				Object[] eqlArgs = {RootOperator.Equal, rootRefSpec, tree[i]};
				Loadable eqlExpr = new LoadableExpression(eqlArgs);
				
				// add equation to list of structure's constraints
				Object[] eqlCTArgs = {eqlExpr, rootVarName + " == Tree[" + i + "]"};
				Loadable eqlCT = new LoadableConstrainment(eqlCTArgs);
				
				structureArgs.add(eqlCT);
			}
			
			// make the loadable model 
			Loadable structure = new LoadableStructure(structureArgs.toArray());
			afterTime = System.currentTimeMillis();
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nModel construction: " + (afterTime - beforeTime)/1000.0 + " sec");
			}
			
			// definitions complete, simulation of runtime begins
			
			// create a KB instance and get a handle on its context (aka namespace)
			KBObject kb = new KBObject();
			Context context = kb.getContext();
			
			// load the model of the structure into the KB and make it "exist"

			beforeTime = System.currentTimeMillis();
			BoundedExpression exists = (BoundedExpression)(structure.load(context));
			afterTime = System.currentTimeMillis();
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nLoad: " + (afterTime - beforeTime)/1000.0 + " sec");
			}
		
			beforeTime = System.currentTimeMillis();
			exists.changeToBe(true);
			afterTime = System.currentTimeMillis();
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nInitialization: " + (afterTime - beforeTime)/1000.0 + " sec");
			}
		    
			kb.commitAll();
			
			// collect the actual instantiated root variable objects from the KB
			BoundedExpression[] var = new BoundedExpression[3];		
			for (int i=0; i<2; i++) {
				var[i] = (BoundedExpression)(context.fetch(rootPathList[i]));
			}
			
			// manually munge the "left" root (in real usage CTs would be added instead)
			
			beforeTime = System.currentTimeMillis();
			var[0].changeToBe(true);	// time this to see how long propagation takes
			afterTime = System.currentTimeMillis();
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nPropagation: " + (afterTime - beforeTime)/1000.0 + " sec");
			}

			kb.commitAll();	// don't forget to do this to commit change
			
			// check that "right" root variable has correct value
			assertTrue(var[1].isKnownToBe(true));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

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
public class TestChain 
	extends TestCase 
{
	private static final Logger log = Logger.getLogger(TestChain.class);
		
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(ChainTest.class));
		junit.textui.TestRunner.run(TestChain.class);
	}
	
	public void testSample()	// ?? should this method be named testChain or something?
	{
		try
		{
			long beforeTime = 0;
			long afterTime = 0;
			
			int K = 100;	// change this to scale test
			int KK = K+K;
			int KK1 = KK+1;
			
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nTesting times for chain of length " + KK1 + "...");
			}
					
			beforeTime = System.currentTimeMillis();
			
			// Strategy: set up 2K+1 Boolean variables linked by constraints of the form
			//  var[n] == not(var[n+1])
			// Set the middle variable to true (time propagation) and check results.
			
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			// define standard bounds for Boolean variables
			Object[] boundsArgs = {Boolean.class};
			Loadable varBounds = new LoadableBounds(boundsArgs);
			
			// temps associatd with each var
			String[] varName = new String[KK1];	// var names
			Loadable[] varSpec = new Loadable[KK1];	// specs for var definitions
			Loadable[] refSpec = new Loadable[KK1];	// specs for references to var
			Object[][] pathList = new Object[3][];	// external paths to runtime var
			
			for (int i=0; i<KK1; i++) {
				// make pretty var name
				varName[i] = "Variable" + i;
				
				// define as Boolean
				Object[] varArgs = {varName[i], varBounds};
				varSpec[i] = new LoadableVariable(varArgs);
				
				// add local var to structure spec
				structureArgs.add(varSpec[i]);
				
				// create a local reference to var
				Object[] refArgs = {varName[i]};
				refSpec[i] = new LoadableLocal(refArgs);
				
				if ((i%K)==0) {
					// create a path to be used to access instance in the KB
					Object[] pathArgs = {structureName, varName[i]};
					pathList[i/K] = pathArgs;
				}
			}
			
			for (int i=0; i<KK; i++) {
				// create not(Operand N) expression
				Object[] notArgs = {RootOperator.Not, refSpec[i+1]};
				Loadable notExpr = new LoadableExpression(notArgs);
				
				// equate with Operation N
				Object[] eqlArgs = {RootOperator.Equal, refSpec[i], notExpr};
				Loadable eqlExpr = new LoadableExpression(eqlArgs);
				
				// add equation to list of structure's constraints
				Object[] eqlCTArgs = {eqlExpr, varName[i] + " == not(" + varName[i+1] + ")"};
				Loadable eqlCT = new LoadableConstrainment(eqlCTArgs);
				
				structureArgs.add(eqlCT);
			}
			
			// make the loadable model with 10 vars and 5 constraints
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
			
			// collect the actual instantiated variable objects from the KB
			BoundedExpression[] var = new BoundedExpression[3];		
			for (int i=0; i<3; i++) {
				var[i] = (BoundedExpression)(context.fetch(pathList[i]));
			}
			
			// manually munge the center var (in real usage CTs would be added instead)
			
			beforeTime = System.currentTimeMillis();
			var[1].changeToBe(true);	// time this to see how long propagation takes
			afterTime = System.currentTimeMillis();
		    if (log.isDebugEnabled())
			{
		    	log.debug("\nPropagation: " + (afterTime - beforeTime)/1000.0 + " sec");
			}

			kb.commitAll();	// don't forget to do this to commit change
			
			// check that variables that depend on known quantities have correct values
			boolean endsAreTrue = ((K&1)==0);	// parity of half-chain length
			assertTrue(var[0].isKnownToBe(endsAreTrue));	// check first operation var
			assertTrue(var[2].isKnownToBe(endsAreTrue));	// check last operand var
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

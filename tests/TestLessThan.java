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

public class TestLessThan 
	extends TestCase 
{
   private static final Logger log = Logger.getLogger(TestLessThan.class);
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(NotTest.class));
		junit.textui.TestRunner.run(TestLessThan.class);
	}
	
	public void testSample()	// ?? should this method be named testNot or something?
	{
		try
		{
			// Strategy: set up k Boolean and 2k integer variables with k constraints of the form
			//  Op[k] == ><(aArg[k],bArg[k])
			// where "><" is the order relation under test (ie <, <=, > or >=).
			
			// Then to test "rootward" propagation set two of the operands to true/false
			//   and check if the corresponding operation results in false/true.
			// Next to test "leafward" propagation set two of the operations to true/false
			//   and check if the corresponding operand results in false/true.
			// Finally, make sure that the last pair of variables are both unknown.
			
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			RootOperator relation = RootOperator.LessThan;
			
			int intMin = 691;
			int intMid = intMin + 1;
			int intMax = intMid + 1;
			
			// define standard bounds for Boolean and integer variables
			Object[] booleanBoundsArgs = {Boolean.class};
			Loadable booleanVarBounds = new LoadableBounds(booleanBoundsArgs);
			Object[] integerBoundsArgs = {new Integer(intMin), new Integer(intMax)};
			Loadable integerVarBounds = new LoadableBounds(integerBoundsArgs);
			
			int K = 7;
			
			// temps associated with each var
			String[] opVarName = new String[K];	// Op var names
			String[] aArgVarName = new String[K];	// Op var names
			String[] bArgVarName = new String[K];	// Op var names

			Loadable[] opVarSpec = new Loadable[K];	// specs for var definitions
			Loadable[] aArgVarSpec = new Loadable[K];	// specs for var definitions
			Loadable[] bArgVarSpec = new Loadable[K];	// specs for var definitions

			Loadable[] opRefSpec = new Loadable[K];	// specs for references to var
			Loadable[] aArgRefSpec = new Loadable[K];	// specs for references to var
			Loadable[] bArgRefSpec = new Loadable[K];	// specs for references to var

			Object[][] opPathList = new Object[K][];		// external paths to runtime var
			Object[][] aArgPathList = new Object[K][];		// external paths to runtime var
			Object[][] bArgPathList = new Object[K][];		// external paths to runtime var
			
			for (int i=0; i<K; i++) {
				// make pretty var names
				opVarName[i] = "Op" + i;
				aArgVarName[i] = "aArg" + i;
				bArgVarName[i] = "bArg" + i;		
				
				// define Op as Boolean
				Object[] opVarArgs = {opVarName[i], booleanVarBounds};
				opVarSpec[i] = new LoadableVariable(opVarArgs);
				
				// define Args as integers
				Object[] aArgVarArgs = {aArgVarName[i], integerVarBounds};
				aArgVarSpec[i] = new LoadableVariable(aArgVarArgs);
				Object[] bArgVarArgs = {bArgVarName[i], integerVarBounds};
				bArgVarSpec[i] = new LoadableVariable(bArgVarArgs);
				
				// add local Op, aArg and bArg vars to structure spec
				structureArgs.add(opVarSpec[i]);
				structureArgs.add(aArgVarSpec[i]);
				structureArgs.add(bArgVarSpec[i]);
				
				// create a local reference to Op, aArg and bArg vars
				Object[] opRefArgs = {opVarName[i]};
				opRefSpec[i] = new LoadableLocal(opRefArgs);
				Object[] aArgRefArgs = {aArgVarName[i]};
				aArgRefSpec[i] = new LoadableLocal(aArgRefArgs);
				Object[] bArgRefArgs = {bArgVarName[i]};
				bArgRefSpec[i] = new LoadableLocal(bArgRefArgs);
				
				// create a path to be used to access var instances in the KB
				Object[] opPathArgs = {structureName, opVarName[i]};
				opPathList[i] = opPathArgs;
				Object[] aArgPathArgs = {structureName, aArgVarName[i]};
				aArgPathList[i] = aArgPathArgs;
				Object[] bArgPathArgs = {structureName, bArgVarName[i]};
				bArgPathList[i] = bArgPathArgs;
			}
			
			for (int i=0; i<K; i++) {
				// create ><(aArg,bArg) expression
				Object[] relationArgs = {relation, aArgRefSpec[i], bArgRefSpec[i]};
				Loadable relationExpr = new LoadableExpression(relationArgs);
				
				// equate with Op
				Object[] eqlArgs = {RootOperator.Equal, opRefSpec[i], relationExpr};
				Loadable eqlExpr = new LoadableExpression(eqlArgs);
				
				// add equation to list of structure's constraints
				Object[] eqlCTArgs = {eqlExpr, 
						opVarName[i] + " == ><(" + aArgVarName[i] + "," + bArgVarName[i] + ")"};
				Loadable eqlCT = new LoadableConstrainment(eqlCTArgs);
				
				structureArgs.add(eqlCT);
			}
			
			// make the loadable model with vars and constraints
			Loadable structure = new LoadableStructure(structureArgs.toArray());
			
			// definitions complete, simulation of runtime begins
			
			// create a KB instance and get a handle on its context (aka namespace)
			KBObject kb = new KBObject();
			Context context = kb.getContext();
			
			// load the model of the structure into the KB and make it "exist"
			BoundedExpression exists = (BoundedExpression)(structure.load(context));
			exists.changeToBe(true);
			kb.commitAll();
			
			// collect the actual instantiated variable objects from the KB
			BoundedExpression[] opVar = new BoundedExpression[K];		
			BoundedExpression[] aArgVar = new BoundedExpression[K];		
			BoundedExpression[] bArgVar = new BoundedExpression[K];		
			for (int i=0; i<K; i++) {
				opVar[i] = (BoundedExpression)(context.fetch(opPathList[i]));
				aArgVar[i] = (BoundedExpression)(context.fetch(aArgPathList[i]));
				bArgVar[i] = (BoundedExpression)(context.fetch(bArgPathList[i]));
			}
			
			// manually munge their values (in real usage CTs would be added instead)
			opVar[0].changeToBe(true);	// set operation 0 : true
			
			opVar[1].changeToBe(false);	// set operation 1 : false
			aArgVar[1].changeToBe(intMid);
			opVar[2].changeToBe(false);	// set operation 2 : false
			bArgVar[2].changeToBe(intMid);
			
			aArgVar[3].changeToBe(intMid);
			aArgVar[4].changeToBe(intMid);
			aArgVar[5].changeToBe(intMid);

			bArgVar[3].changeToBe(intMin);
			bArgVar[4].changeToBe(intMid);
			bArgVar[5].changeToBe(intMax);
			
			kb.commitAll();	// don't forget to do this to commit previous changes
			
		    if (log.isDebugEnabled())
			{
				for (int i=0; i<K; i++) {
					log.debug("\n#" + i + " ><(" + 
							aArgVar[i].getFixedBounds() + "," + 
							bArgVar[i].getFixedBounds() + ") : " + 
							opVar[i].getFixedBounds());
				}
			}
			
			// check that arguments have correct values
			assertTrue(aArgVar[0].getMinIndex() == intMin);
			assertTrue(aArgVar[0].getMaxIndex() == intMid);
			assertTrue(bArgVar[0].getMinIndex() == intMid);
			assertTrue(bArgVar[0].getMaxIndex() == intMax);
			
			assertTrue(bArgVar[1].getMinIndex() == intMin);
			assertTrue(bArgVar[1].getMaxIndex() == intMid);
			assertTrue(aArgVar[2].getMinIndex() == intMid);
			assertTrue(aArgVar[2].getMaxIndex() == intMax);		

			// check operations have correct values
			assertTrue(opVar[3].isKnownToBe(false));
			assertTrue(opVar[4].isKnownToBe(false));
			assertTrue(opVar[5].isKnownToBe(true));
			
			// check that unmunged variables are unconstrained
			assertFalse(opVar[6].isPoint());
			assertTrue(aArgVar[6].getMinIndex() == intMin);
			assertTrue(aArgVar[6].getMaxIndex() == intMax);
			assertTrue(bArgVar[6].getMinIndex() == intMin);
			assertTrue(bArgVar[6].getMaxIndex() == intMax);			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

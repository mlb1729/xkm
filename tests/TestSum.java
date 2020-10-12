/*
 * Created on May 4, 2005
 *
 
 */
package tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import contexts.Context;
import expressions.BoundedExpression;
import kb.KBObject;
import loader.Loadable;
import loader.LoadableBounds;
import loader.LoadableConstrainment;
import loader.LoadableExpression;
import loader.LoadableLocal;
import loader.LoadableStructure;
import loader.LoadableVariable;
import operators.RootOperator;

/**
 * @author MLB
 *
 *
 */

public class TestSum 
	extends TestCase 
{
   private static final Logger log = Logger.getLogger(TestSum.class);
	
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(NotTest.class));
		junit.textui.TestRunner.run(TestSum.class);
	}
	
	public void testSample()	// ?? should this method be named testNot or something?
	{
		try
		{
			// Strategy: set up k Boolean and 2k integer variables with k constraints of the form
			//  Op[k] == +(aArg[k],bArg[k])
			
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			RootOperator operation = RootOperator.Sum;
			
			int intMin = 1;
			int intMax = 9;
			
			// define standard bounds for integer variables
			Object[] integerBoundsArgs = {new Integer(intMin), new Integer(intMax)};
			Loadable integerVarBounds = new LoadableBounds(integerBoundsArgs);
			
			int K = 1;
			
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
				Object[] opVarArgs = {opVarName[i], integerVarBounds};
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
				// create +(aArg,bArg) expression
				Object[] relationArgs = {operation, aArgRefSpec[i], bArgRefSpec[i]};
				Loadable relationExpr = new LoadableExpression(relationArgs);
				
				// equate with Op
				Object[] eqlArgs = {RootOperator.Equal, opRefSpec[i], relationExpr};
				Loadable eqlExpr = new LoadableExpression(eqlArgs);
				
				// add equation to list of structure's constraints
				Object[] eqlCTArgs = {eqlExpr, 
						opVarName[i] + " == +(" + aArgVarName[i] + "," + bArgVarName[i] + ")"};
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
			
			assertTrue(opVar[0].getMinIndex() == 2);
			assertTrue(opVar[0].getMaxIndex() == 9);
			assertTrue(aArgVar[0].getMinIndex() == 1);
			assertTrue(aArgVar[0].getMaxIndex() == 8);
			assertTrue(bArgVar[0].getMinIndex() == 1);
			assertTrue(bArgVar[0].getMaxIndex() == 8);
			
			// manually munge their values (in real usage CTs would be added instead)
			aArgVar[0].changeMinIndex(2);
			kb.commitAll();	// don't forget to do this to commit previous changes

			assertTrue(opVar[0].getMinIndex() == 3);
			assertTrue(opVar[0].getMaxIndex() == 9);
			assertTrue(aArgVar[0].getMinIndex() == 2);
			assertTrue(aArgVar[0].getMaxIndex() == 8);
			assertTrue(bArgVar[0].getMinIndex() == 1);
			assertTrue(bArgVar[0].getMaxIndex() == 7);
			
			// manually munge their values (in real usage CTs would be added instead)
			opVar[0].changeMaxIndex(6);
			kb.commitAll();	// don't forget to do this to commit previous changes

			assertTrue(opVar[0].getMinIndex() == 3);
			assertTrue(opVar[0].getMaxIndex() == 6);
			assertTrue(aArgVar[0].getMinIndex() == 2);
			assertTrue(aArgVar[0].getMaxIndex() == 5);
			assertTrue(bArgVar[0].getMinIndex() == 1);
			assertTrue(bArgVar[0].getMaxIndex() == 4);
						
			// manually munge their values (in real usage CTs would be added instead)
			bArgVar[0].changeMinIndex(4);
			kb.commitAll();	// don't forget to do this to commit previous changes

			assertTrue(opVar[0].getMinIndex() == 6);
			assertTrue(opVar[0].getMaxIndex() == 6);
			assertTrue(aArgVar[0].getMinIndex() == 2);
			assertTrue(aArgVar[0].getMaxIndex() == 2);
			assertTrue(bArgVar[0].getMinIndex() == 4);
			assertTrue(bArgVar[0].getMaxIndex() == 4);
						
		    if (log.isDebugEnabled())
			{
				for (int i=0; i<K; i++) {
					log.debug("\n#" + i + " +(" + 
							aArgVar[i].getFixedBounds() + "," + 
							bArgVar[i].getFixedBounds() + ") : " + 
							opVar[i].getFixedBounds());
				}
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

/*
 * Created on May 4, 2005
 *
 
 */
package tests;

import java.util.ArrayList;
import java.util.List;

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

public class TestAnd 
	extends KBTestCase 
{
   private static final Logger log = Logger.getLogger(TestAnd.class);
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(NotTest.class));
		junit.textui.TestRunner.run(TestAnd.class);
	}
	
	public void testSample()	// ?? should this method be named testNot or something?
	{
		try
		{
			// Strategy: set up k Boolean and 2k integer variables with k constraints of the form
			//  Op[k] == ?(aArg[k],bArg[k])
			// where "?" is the Boolean relation under test (ie And, Or, etc).
						
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			RootOperator relation = RootOperator.And;
				
			// define standard bounds for Boolean variables
			Object[] booleanBoundsArgs = {Boolean.class};
			Loadable booleanVarBounds = new LoadableBounds(booleanBoundsArgs);
			
			int K = 16;
			
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
				Object[] aArgVarArgs = {aArgVarName[i], booleanVarBounds};
				aArgVarSpec[i] = new LoadableVariable(aArgVarArgs);
				Object[] bArgVarArgs = {bArgVarName[i], booleanVarBounds};
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

			//  0 ... --> UUU
			
			//  1 T.. --> tTT
			opVar[1].changeToBe(true);
			
			//  2 F.. --> fUU
			opVar[2].changeToBe(false);
			
			//  3 .T. --> UtU
			aArgVar[3].changeToBe(true);
			
			//  4 .F. --> FfU
			aArgVar[4].changeToBe(false);
			
			//  5 ..T --> UUt
			bArgVar[5].changeToBe(true);
			
			//  6 ..F --> FUf
			bArgVar[6].changeToBe(false);
			
			//  7 TT. --> ttT
			opVar[7].changeToBe(true);
			aArgVar[7].changeToBe(true);
			
			//  8 T.T --> tTt
			opVar[8].changeToBe(true);
			bArgVar[8].changeToBe(true);
			
			//  9 FT. --> ftF
			opVar[9].changeToBe(false);
			aArgVar[9].changeToBe(true);
					
			// 10 FF. --> ffU
			opVar[10].changeToBe(false);
			aArgVar[10].changeToBe(false);
			
			// 11 F.T --> fFt
			opVar[11].changeToBe(false);
			bArgVar[11].changeToBe(true);
			
			// 12 F.F --> fUf
			opVar[12].changeToBe(false);
			bArgVar[12].changeToBe(false);
		
			// 13 TT. --> ttT
			opVar[13].changeToBe(true);
			aArgVar[13].changeToBe(true);
			
			// 14 T.T --> tTt
			opVar[14].changeToBe(true);
			bArgVar[14].changeToBe(true);
			
			// 15 TTT --> ttt
			opVar[15].changeToBe(true);
			aArgVar[15].changeToBe(true);
			bArgVar[15].changeToBe(true);
						
			kb.commitAll();	// don't forget to do this to commit previous changes
			
		    if (log.isDebugEnabled())
			{
				for (int i=0; i<K; i++) {
					log.debug("\n#" + i + " ?(" + 
							aArgVar[i].getFixedBounds() + "," + 
							bArgVar[i].getFixedBounds() + ") : " + 
							opVar[i].getFixedBounds());
				}
			}
			
			// check that arguments have correct values
			//  0 ... --> UUU
			assertFalse(opVar[0].isPoint());
			assertFalse(aArgVar[0].isPoint());
			assertFalse(bArgVar[0].isPoint());
			
			//  1 T.. --> tTT
			assertTrue(aArgVar[1].isKnownToBe(true));
			assertTrue(bArgVar[1].isKnownToBe(true));
			
			//  2 F.. --> fUU
			assertFalse(aArgVar[2].isPoint());
			assertFalse(bArgVar[2].isPoint());
			
			//  3 .T. --> UtU
			assertFalse(opVar[3].isPoint());
			assertFalse(bArgVar[3].isPoint());
			
			//  4 .F. --> FfU
			assertTrue(opVar[4].isKnownToBe(false));
			assertFalse(bArgVar[4].isPoint());
			
			//  5 ..T --> UUt
			assertFalse(opVar[5].isPoint());
			assertFalse(aArgVar[5].isPoint());
			
			//  6 ..F --> FUf
			assertTrue(opVar[6].isKnownToBe(false));
			assertFalse(aArgVar[6].isPoint());
			
			//  7 TT. --> ttT
			assertTrue(bArgVar[7].isKnownToBe(true));
			
			//  8 T.T --> tTt
			assertTrue(aArgVar[8].isKnownToBe(true));
			
			//  9 FT. --> ftF
			assertTrue(bArgVar[9].isKnownToBe(false));
					
			// 10 FF. --> ffU
			assertFalse(bArgVar[10].isPoint());
			
			// 11 F.T --> fFt
			assertTrue(aArgVar[11].isKnownToBe(false));
			
			// 12 F.F --> fUf
			assertFalse(aArgVar[12].isPoint());
		
			// 13 TT. --> ttT
			assertTrue(bArgVar[13].isKnownToBe(true));
			
			// 14 T.T --> tTt
			assertTrue(aArgVar[13].isKnownToBe(true));
			
			// 15 TTT --> ttt
						
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

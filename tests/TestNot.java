/*
 * Created on May 4, 2005
 *
 
 */
package tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

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

public class TestNot 
	extends TestCase 
{
	/**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		//TestRunner.run(suite(NotTest.class));
		junit.textui.TestRunner.run(TestNot.class);
	}
	
	public void testSample()	// ?? should this method be named testNot or something?
	{
		try
		{
			// Strategy: set up 10 Boolean variables with 5 constraints of the form
			//  var[2n] == not(var[2n+1])
			// Thus the even numbered variables reflect the "operation" result
			//   which should be the negation of the odd numbered variable "operand".
			// Then to test "branchward" propagation set two of the operands to true/false
			//   and check if the corresponding operation results in false/true.
			// Next to test "leafward" propagation set two of the operations to true/false
			//   and check if the corresponding operand results in false/true.
			// Finally, make sure that the last pair of variables are both unknown.
			
			// accumulate specification of top level "case" container
			String structureName = "TestStructure";
			List structureArgs = new ArrayList();
			structureArgs.add(structureName);
			
			// define standard bounds for Boolean variables
			Object[] boundsArgs = {Boolean.class};
			Loadable varBounds = new LoadableBounds(boundsArgs);
			
			// temps associatd with each var
			String[] varName = new String[10];	// var names
			Loadable[] varSpec = new Loadable[10];	// specs for var definitions
			Loadable[] refSpec = new Loadable[10];	// specs for references to var
			Object[][] pathList = new Object[10][];		// external paths to runtime var
			
			for (int i=0; i<10; i++) {
				// make pretty var name
				varName[i] = (((i&1)==0) ? "Operation" : "Operand") + (i >> 1);
				
				// define as Boolean
				Object[] varArgs = {varName[i], varBounds};
				varSpec[i] = new LoadableVariable(varArgs);
				
				// add local var to structure spec
				structureArgs.add(varSpec[i]);
				
				// create a local reference to var
				Object[] refArgs = {varName[i]};
				refSpec[i] = new LoadableLocal(refArgs);
				
				// create a path to be used to access instance in the KB
				Object[] pathArgs = {structureName, varName[i]};
				pathList[i] = pathArgs;
			}
			
			for (int i=0; i<10; i=i+2) {
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
			
			// definitions complete, simulation of runtime begins
			
			// create a KB instance and get a handle on its context (aka namespace)
			KBObject kb = new KBObject();
			Context context = kb.getContext();
			
			// load the model of the structure into the KB and make it "exist"
			BoundedExpression exists = (BoundedExpression)(structure.load(context));
			exists.changeToBe(true);
			kb.commitAll();
			
			// collect the actual instantiated variable objects from the KB
			BoundedExpression[] var = new BoundedExpression[10];		
			for (int i=0; i<10; i++) {
				var[i] = (BoundedExpression)(context.fetch(pathList[i]));
			}
			
			// manually munge their values (in real usage CTs would be added instead)
			var[1].changeToBe(true);	// set operand 0 : true
			var[3].changeToBe(false);	// set operand 1 : false
			var[4].changeToBe(true);	// set operation 2 : true
			var[6].changeToBe(false);	// set operation 3 : false
			kb.commitAll();	// don't forget to do this to commit previous changes
			
			// check that variables that depend on known quantities have correct values
			assertTrue(var[0].isKnownToBe(false));	// check operation 0 = false
			assertTrue(var[2].isKnownToBe(true));	// check operation 1 = true
			assertTrue(var[5].isKnownToBe(false));	// check operand 2 = false
			assertTrue(var[7].isKnownToBe(true));	// check operand 3 = true

			// check that unmunged variables are UNKNOWN
			assertFalse(var[8].isPoint());	// operation 4 unknown
			assertFalse(var[9].isPoint());	// operand 4 unknown
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
}

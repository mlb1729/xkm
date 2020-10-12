/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableConstant;
import com.resonant.xkm.loader.LoadableConstrainment;
import com.resonant.xkm.loader.LoadableExpression;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;
import com.resonant.xkm.operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestMembers 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestMembers.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestMembers.class);
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
		
		// define bounds for variable
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variable with those bounds
		String nameI = "I";
		Object[] varIArgs = {nameI, boundsI};
		Loadable varI = new LoadableVariable(varIArgs);

		// define a reference to the variable
		Object[] refIArgs = {nameI};
		Loadable refToI = new LoadableLocal(refIArgs);
		
		// define a constraint 
		Object[] constantArgs6 = {new Integer(666)};
		Loadable constant6 = new LoadableConstant(constantArgs6);
		
		Object[] aGE6Args = {RootOperator.GreaterEqual, refToI, constant6};
		Loadable aGE6Expr = new LoadableExpression(aGE6Args);
		
		Object[] bGE6Args = {RootOperator.Equal, refToB, aGE6Expr};
		Loadable bGE6Expr = new LoadableExpression(bGE6Args);
		
		Object[] bGE6CTArgs = {bGE6Expr, nameB + " == (" + nameI + " >= 666)"};
		Loadable bGE6CT = new LoadableConstrainment(bGE6CTArgs);
		
		// define two members
		String nameT1 = "Thing1";
		Object[] memberT1Args = {nameT1, 
									varB, varI,	bGE6CT,};
		Loadable memberT1 = new LoadableStructure(memberT1Args);
		
		String nameT2 = "Thing2";
		Object[] memberT2Args = {nameT2, 
									varB, varI,	bGE6CT,};
		Loadable memberT2 = new LoadableStructure(memberT2Args);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameS = "TheCase";
		Object[] structureArgs = {nameS, 
									memberT1, memberT2,};
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
				
		Object[] pathT1Args = {nameS, nameT1,};	
 		Assertion memberT1Assertion = kb.newAssertion(pathT1Args, "==", Boolean.TRUE);

		kb.assertAssertion(memberT1Assertion);

		Object[] pathT1BArgs = {nameS, nameT1, nameB,};	
 		Assertion varT1BAssertion = kb.newAssertion(pathT1BArgs, "==", Boolean.TRUE);

		kb.assertAssertion(varT1BAssertion);

		System.out.println("\nFinal KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

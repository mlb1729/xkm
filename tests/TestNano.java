/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.expressions.BoundedExpression;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestNano 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestNano.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestNano.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???	
		
		// define standard bounds for the variable
		Object[] boundsArgs = {Boolean.class};
		Loadable bounds = new LoadableBounds(boundsArgs);
		
		// define the variable with those bounds
		Object[] theVarArgs = {"TheVariable", bounds};
		Loadable theVar = new LoadableVariable(theVarArgs);

		// define a reference to the variable
		Object[] refArgs = {"TheVariable"};
		Loadable refToTheVar = new LoadableLocal(refArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"TheImage", 
									theVar, 
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
		
		System.out.println("\nCurrent KB state...");	
		System.out.println(context);
		
		System.out.println("\nNote that since the structure doesn't definitely exist");	
		System.out.println("\n none of its constraints are enforced (= unknown)");	
		
		System.out.println("\nNow force the top-level model to definitely exist");	
		x.changeToBe(true);
		kb.commitAll();
		
		System.out.println("Current KB state...");	
		System.out.println(context);
		
		System.out.println("\nNote that since the structure now definitely exists");	
		System.out.println("\n all of its constraints are enforced (= true)");	
		
		System.out.println("\nNow make a path to the variable and access it...");
		// note this is just one possible way of doing this
		Object[] pathArgs = {"TheImage", "TheVariable"};
		BoundedExpression var = 
			(BoundedExpression)(context.fetch(pathArgs));
		System.out.println(var);
 		System.out.println(var.toStringValue());
		System.out.println(var.getMinIndex());
		System.out.println(var.getMaxIndex());

		System.out.println("\nCurrent KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

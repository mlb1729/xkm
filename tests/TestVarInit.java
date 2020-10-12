/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
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
public class TestVarInit
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestVarInit.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestVarInit.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// first we simulate authoring, by defining a simple model
		// in real use this all these objects would be automatically constructed  
		// via interpreting a declarative user language, such as XML or "GAL" or ???	
			
		// define bounds for independent variable
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variables with those bounds
		String nameI = "I";
		Object[] varIArgs = {nameI, boundsI};
		Loadable varI = new LoadableVariable(varIArgs);

		// define a reference to the variable
		Object[] refIArgs = {nameI};
		Loadable refToI = new LoadableLocal(refIArgs);
		
		String nameJ = "J";
		Object[] varJArgs = {nameJ, boundsI};
		Loadable varJ = new LoadableVariable(varJArgs);

		// define a reference to the variable
		Object[] refJArgs = {nameJ};
		Loadable refToJ = new LoadableLocal(refJArgs);
		
		// define value expressions for dependent variable
		Object[] valDArgs = {RootOperator.Sum, varI, varI};
		Loadable valDExpr = new LoadableExpression(valDArgs);
		
		Object[] valEArgs = {RootOperator.Sum, varJ, varJ};
		Loadable valEExpr = new LoadableExpression(valEArgs);
		
		// define the variables with those declarations
		String nameD = "D";
		Object[] varDArgs = {nameD, boundsI, valDExpr};
		Loadable varD = new LoadableVariable(varDArgs);
			
		String nameE = "E";
		Object[] varEArgs = {nameE, boundsI, valEExpr};
		Loadable varE = new LoadableVariable(varEArgs);
			
    	// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameInner = "Inner";
		Object[] innerArgs = {nameInner, varD, varE};
		LoadableStructure inner = new LoadableStructure(innerArgs);
		
		String nameOuter = "Outer";
		Object[] outerArgs = {nameOuter, varI, varJ, inner};
		LoadableStructure outer = new LoadableStructure(outerArgs);

		System.out.println("\nModel definition...");
		System.out.println(outer);
	
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
			
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		String nameCase = "Case";
				
		// Object[] pathRootArgs = {};
		Object[] pathCaseArgs	= {nameCase};
		Object[] pathIArgs		= {nameCase, nameI};
		Object[] pathInnerArgs	= {nameCase, nameInner};
		Object[] pathDArgs		= {nameCase, nameInner, nameD};
		Object[] pathEArgs		= {nameCase, nameInner, nameE};
		
		kb.ensureMember(pathCaseArgs, outer);

		System.out.println("\nInitially:");
		System.out.println(context);		
		
	    Assertion innerAssertion = kb.newAssertion(pathInnerArgs);
		kb.assertAssertion(innerAssertion);
		kb.commitAll();

	    System.out.println("\nAfter reification:");
		System.out.println(context);		

	    Assertion iAssertion = kb.newAssertion(pathIArgs, new Integer(777));
	    // Assertion eAssertion = kb.newAssertion(pathEArgs, new Integer(888));
	    Assertion eGAssertion = kb.newAssertion(pathEArgs, ">=", new Integer(887));
	    Assertion eLAssertion = kb.newAssertion(pathEArgs, "<=", new Integer(889));
	   		 		
	    System.out.println("\nAsserting vars...:");
		kb.assertAssertion(iAssertion);
		// kb.assertAssertion(eAssertion);
		kb.assertAssertion(eGAssertion);
		kb.assertAssertion(eLAssertion);

		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		System.out.println("\n...Test Done");
	}
}

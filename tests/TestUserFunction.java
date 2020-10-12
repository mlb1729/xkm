/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import org.apache.log4j.Logger;

import com.resonant.xkm.api.Assertion;
import com.resonant.xkm.api.KB;
import com.resonant.xkm.api.UserDefinedFunctionKernel;
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
import com.resonant.xkm.operators.UserDefinedFunction;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestUserFunction 
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestUserFunction.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestUserFunction.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");
		
		// define user function adapter class

		class MyUserFunctionAdapter 
			implements UserDefinedFunctionKernel 
		{
			// basic constructor, don't need to pass in anybthing from outside to encapsulate
			public MyUserFunctionAdapter() {super();}
			
			// arbitrary user function, defined in terms of raw native numeric types 			
			public int myUserFunction(int n) {
				int result = n*n;
				return result;
			}
			
			// called to find out what kind of Object we return (required)
			public Class getReturnValueClass() {
				return Integer.class;
			}
			
			// Object based interface called by KB (required)
			public Object apply(KB kb, Object[] operands) {
				int operand = ((Integer)operands[0]).intValue();	// extract operand
				int result = myUserFunction(operand);				// compute result
				Integer value = new Integer(result);				// wrap value for return
				return value;
			}
		}
	
		// first add user fn to operator namespace
		String nameFn = "foo";
		UserDefinedFunction myFn = 
			new UserDefinedFunction(nameFn, new MyUserFunctionAdapter());
			
		// define bounds for variables
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define variables with those bounds
		String nameI = "I";
		Object[] varIArgs = {nameI, boundsI};
		Loadable varI = new LoadableVariable(varIArgs);

		String nameJ = "J";
		Object[] varJArgs = {nameJ, boundsI};
		Loadable varJ = new LoadableVariable(varJArgs);

		// define references to the variable
		Object[] refIArgs = {nameI};
		Loadable refToI = new LoadableLocal(refIArgs);
		
		Object[] refJArgs = {nameJ};
		Loadable refToJ = new LoadableLocal(refJArgs);
		
		// define a guideline with the user function
		// Object[] fnExprArgs = {myFn, refToI};
		Object[] fnExprArgs = {nameFn, refToI};
		Loadable fnExpr = new LoadableExpression(fnExprArgs);		
		
		Object[] equalExprArgs = {RootOperator.Equal, refToJ, fnExpr,};
		Loadable equalExpr = new LoadableExpression(equalExprArgs);
		
		Object[] equalCTArgs = {equalExpr, "J = f(I)"};
		Loadable equalCT = new LoadableConstrainment(equalCTArgs);	
		
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		String nameS = "TestCase";
		Object[] structureArgs = {nameS, 
									varI, varJ,
									equalCT,
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
				
		Object[] pathIArgs = {nameS, nameI};
		BoundedExpression varIExpr = 
			(BoundedExpression)(context.fetch(pathIArgs));

		Object[] pathJArgs = {nameS, nameJ};
		BoundedExpression varJExpr = 
			(BoundedExpression)(context.fetch(pathJArgs));

		System.out.println("\nInitially:");
		System.out.println(varIExpr + " = " + varIExpr.toStringValue());
		System.out.println(varJExpr + " = " + varJExpr.toStringValue());

 		Assertion assertion7 = kb.newAssertion(pathIArgs, "==", new Integer(7));
			
		kb.assertAssertion(assertion7);
 		
		System.out.println("\nAfter asserting I == 7:");
		System.out.println(varIExpr + " = " + varIExpr.toStringValue());
		System.out.println(varJExpr + " = " + varJExpr.toStringValue());

		System.out.println("\nFinal KB state...");	
		System.out.println(context);		

		System.out.println("\n...Test Done");
	}
}

/*
 * Created on Mar 9, 2005
 *
 */
package tests;

import org.apache.log4j.Logger;

import api.Assertion;
import contexts.Context;
import kb.KBObject;
import loader.Loadable;
import loader.LoadableBounds;
import loader.LoadableConstant;
import loader.LoadableConstrainment;
import loader.LoadableEntailment;
import loader.LoadableExpression;
import loader.LoadableHerd;
import loader.LoadableLocal;
import loader.LoadableMember;
import loader.LoadableQuantification;
import loader.LoadableSetObject;
import loader.LoadableVariable;
import operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestQuantification
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestQuantification.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestQuantification.class);
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
		String nameB = "isPreferred";
		Object[] varBArgs = {nameB, boundsB};
		Loadable varB = new LoadableVariable(varBArgs);

		// define a reference to the variable
		Object[] refBArgs = {nameB};
		Loadable refToB = new LoadableLocal(refBArgs);
		
		// define bounds for variable
		Object[] boundsIArgs = {new Integer(0), new Integer(999999)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variable with those bounds
		String nameI = "insurableAmount";
		Object[] varIArgs = {nameI, boundsI};
		Loadable varI = new LoadableVariable(varIArgs);

		// define a reference to the variable
		Object[] refIArgs = {nameI};
		Loadable refToI = new LoadableLocal(refIArgs);
		
		// define a constraint 
		Object[] constantArgs6 = {new Integer(6000)};
		Loadable constant6 = new LoadableConstant(constantArgs6);
		
		Object[] aGE6Args = {RootOperator.GreaterEqual, refToI, constant6};
		Loadable aGE6Expr = new LoadableExpression(aGE6Args);
		
		Object[] bGE6Args = {RootOperator.Equal, refToB, aGE6Expr};
		Loadable bGE6Expr = new LoadableExpression(bGE6Args);
		
		Object[] bGE6CTArgs = {bGE6Expr, nameB + " == (" + nameI + " >= 666)"};
		Loadable bGE6CT = new LoadableConstrainment(bGE6CTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"Driver", 
									varB,
									varI,
									bGE6CT,
									};
		// LoadableStructure structure = new LoadableStructure(structureArgs);
		LoadableMember structure = new LoadableMember(structureArgs);
		System.out.println("\nModel definition...");
		System.out.println(structure);
		
		String nameCase = "Case";
		String nameFoo = "Things";
		String nameBar = "1";
		String nameBaz = "2";
		
		// Object[] pathRootArgs = {};
		Object[] pathCaseArgs = {nameCase};
		Object[] pathFooArgs = {nameCase, nameFoo};
		Object[] pathBarArgs = {nameCase, nameFoo, nameBar};
 		Object[] pathBazArgs = {nameCase, nameFoo, nameBaz};
//		Object[] pathBazArgs = {nameCase, nameFoo, null};
		
		String nameHerd = "Drivers";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
		
		Object[] qtyExprArgs = {RootOperator.Quantity, refToHerd};
		Loadable qtyExpr = new LoadableExpression(qtyExprArgs);
		
		String nameQ = "numberOfDrivers";
		Object[] varQArgs = {nameQ, boundsI};
		Loadable varQ = new LoadableVariable(varQArgs);

		Object[] refToQArgs = {nameQ};
		Loadable refToQ = new LoadableLocal(refToQArgs);
		
		Object[] qExprArgs = {RootOperator.Equal, refToQ, qtyExpr};
		Loadable qExpr = new LoadableExpression(qExprArgs);
		
		Object[] qCTArgs = {qExpr, "Q is the quantity of the herd."};
		Loadable qCT = new LoadableConstrainment(qCTArgs);
		
		Object[] herdArgs = {nameHerd, structure};
		// LoadableStructure herd = new LoadableHerd(herdArgs);
		LoadableMember herd = new LoadableHerd(herdArgs);
		
		Object[] pathHerdArgs = {nameCase, nameHerd};

		// quantification stuff
		
		//// loadable inner body entailment
		Object[] constantArgs5 = {new Integer(5000)};
		Loadable constant5 = new LoadableConstant(constantArgs5);
		
		Object[] constantArgs7 = {new Integer(7000)};
		Loadable constant7 = new LoadableConstant(constantArgs7);
		
		String nameFree = "d";
		
		// define references to the variables
		Object[] refFreeBArgs = {nameFree, nameB};
		Loadable refToFreeB = new LoadableLocal(refFreeBArgs);
		
		Object[] refFreeIArgs = {nameFree, nameI};
		Loadable refToFreeI = new LoadableLocal(refFreeIArgs);
		
		Object[] qmExprArgs = {RootOperator.IntCond, refToFreeB, constant7, constant5};
		// Object[] qmExprArgs = {RootOperator.IntCond, refToFreeB, constant7};
		// Object[] qmExprArgs = {RootOperator.IntCond, refToFreeB};
		Loadable qmExpr = new LoadableExpression(qmExprArgs);
		
		Object[] iEQqmExprArgs = {RootOperator.Equal, refToFreeI, qmExpr};
		Loadable iEQqmExpr = new LoadableExpression(iEQqmExprArgs);
		
		Object[] iEQqmCTArgs = {iEQqmExpr, "closure CT"};
		Loadable iEQqmCT = new LoadableConstrainment(iEQqmCTArgs);
		
		Object[] iEQqmBodyArgs = {"Body", iEQqmCT};
		Loadable iEQqmBody = new LoadableEntailment(iEQqmBodyArgs);

		System.out.println("\nBody definition...");
		System.out.println(iEQqmBody);
		
		Object[] localPathHerdArgs = {nameHerd};
		LoadableLocal localPathHerd = new LoadableLocal(localPathHerdArgs);
		
		LoadableLocal pathHerd = new LoadableLocal(pathHerdArgs);
		
		Object[] quantificationArgs = {nameFree, localPathHerd, iEQqmBody};
		Loadable quantification = new LoadableQuantification(quantificationArgs);
		
		// herd set and filter set stuff
		
		String nameS = "setO'Drivers";
		Object[] varSArgs = {nameS, refToHerd};
		Loadable varS = new LoadableSetObject(varSArgs);
		
		Object[] refToSArgs = {nameS};
		Loadable refToS = new LoadableLocal(refToSArgs);
		
		String nameX = "x";
		String nameXX = nameX;
		Object[] refToXBArgs = {nameX, nameB};
		Loadable refToXB = new LoadableLocal(refToXBArgs);
		
		Object[] fExprArgs = {RootOperator.Not, refToXB};
		Loadable fExpr = new LoadableExpression(fExprArgs);
		
		String nameZ = "setO'BadDrivers";
		Object[] varZArgs = {nameZ, refToS, nameXX, fExpr};
		Loadable varZ = new LoadableSetObject(varZArgs);
		
		Object[] refToZArgs = {nameZ};
		Loadable refToZ = new LoadableLocal(refToZArgs);
		
		Object[] qtyZExprArgs = {RootOperator.Quantity, refToZ};
		Loadable qtyZExpr = new LoadableExpression(qtyZExprArgs);
		
		String nameQZ = "numberOfBadDrivers";
		Object[] varQZArgs = {nameQZ, boundsI};
		Loadable varQZ = new LoadableVariable(varQZArgs);

		Object[] refToQZArgs = {nameQZ};
		Loadable refToQZ = new LoadableLocal(refToQZArgs);
		
		Object[] qzExprArgs = {RootOperator.Equal, refToQZ, qtyZExpr};
		Loadable qzExpr = new LoadableExpression(qzExprArgs);
		
		Object[] qzCTArgs = {qzExpr, "QZ is the quantity of the set."};
		Loadable qzCT = new LoadableConstrainment(qzCTArgs);

		Loadable refToAttrOp = refToS;
		
		Object[] minIExprArgs = {RootOperator.IntMinAttr, refToAttrOp, nameI};
		Loadable minIExpr = new LoadableExpression(minIExprArgs);

		Object[] maxIExprArgs = {RootOperator.IntMaxAttr, refToAttrOp, nameI};
		Loadable maxIExpr = new LoadableExpression(maxIExprArgs);

		Object[] boundsAllIArgs = {Integer.class};
		Loadable boundsAllI = new LoadableBounds(boundsAllIArgs);

		String nameMinI = "minimumInsurableAmount";
		Object[] varMinIArgs = {nameMinI, boundsAllI};
		Loadable varMinI = new LoadableVariable(varMinIArgs);

		Object[] refToMinIArgs = {nameMinI};
		Loadable refToMinI = new LoadableLocal(refToMinIArgs);
		
		Object[] minIEqExprArgs = {RootOperator.Equal, refToMinI, minIExpr};
		Loadable minIEqExpr = new LoadableExpression(minIEqExprArgs);
		
		Object[] minICTArgs = {minIEqExpr, "minI is the minimum of I"};
		Loadable minICT = new LoadableConstrainment(minICTArgs);
		
		String nameMaxI = "maximumInsurableAmount";
		Object[] varMaxIArgs = {nameMaxI, boundsAllI};
		Loadable varMaxI = new LoadableVariable(varMaxIArgs);

		Object[] refToMaxIArgs = {nameMaxI};
		Loadable refToMaxI = new LoadableLocal(refToMaxIArgs);
		
		Object[] maxIEqExprArgs = {RootOperator.Equal, refToMaxI, maxIExpr};
		Loadable maxIEqExpr = new LoadableExpression(maxIEqExprArgs);
		
		Object[] maxICTArgs = {maxIEqExpr, "maxI is the maximum of I"};
		Loadable maxICT = new LoadableConstrainment(maxICTArgs);
		
		// put it all together...
		
		Object[] shellArgs = {"Shell", 
				herd, 
				varS,
				varZ,
				varQ, 
				qCT,
				varQZ,
				qzCT,
				varMinI,
				minICT,
				varMaxI,
				maxICT,
				quantification,
				};
		LoadableMember shell = new LoadableMember(shellArgs);
		
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
		
		// load the model of the structure into the KB
		// the result is a boolean domain that says whether the structure "exists"
		// BoundedExpression x = (BoundedExpression)(structure.load(context));
		
		// now make it exist
		// kb.reify(x);
		kb.commitAll();
		
		
		System.out.println("\nInitially:");
		System.out.println(context);		

		kb.ensureMember(pathCaseArgs, shell);
		
		System.out.println("\nAfter Shell:");
		System.out.println(context);		
		
		Assertion herdAssertion = kb.newAssertion(pathHerdArgs);
		// kb.assertAssertion(herdAssertion);
		
		System.out.println("\nAfter Herd:");
		System.out.println(context);			
		
		String nameThing1 = "Mary";
		String nameThing2 = "John";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
	    System.out.println("\nAdding first to herd...:");
		kb.ensureMember(pathHerd1Args, structure);
		System.out.println(context);		
		
		kb.commitAll();

		System.out.println("\nAdding second to herd...:");
		kb.ensureMember(pathHerd2Args, structure);
		System.out.println(context);
		
		Object[] pathBoolean1Args = {nameCase, nameHerd, nameThing1, nameB};
		Object[] pathBoolean2Args = {nameCase, nameHerd, nameThing2, nameB};

		Assertion boolean1Assertion = kb.newAssertion(pathBoolean1Args, Boolean.TRUE);
		Assertion boolean2Assertion = kb.newAssertion(pathBoolean2Args, Boolean.FALSE);
		
		kb.assertAssertion(boolean1Assertion);
		kb.assertAssertion(boolean2Assertion);
		
		System.out.println("\nAfter assertions:");
		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		System.out.println("\n...Test Done");
		
//		System.out.println("\n" + nameX + " : " + nameX.hashCode());
//		System.out.println("\n" + nameXX + " : " + nameXX.hashCode());
//		System.out.println("\n" + getName(nameX).equals(getName(nameXX)));
	}
}

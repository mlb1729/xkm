/*
 * Created on Mar 9, 2005
 *
 
 */
package tests;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import api.Assertion;
import contexts.Context;
import kb.KBObject;
import loader.Loadable;
import loader.LoadableBounds;
import loader.LoadableConstant;
import loader.LoadableConstraint;
import loader.LoadableExpression;
import loader.LoadableHerd;
import loader.LoadableLocal;
import loader.LoadableMember;
import loader.LoadableStructure;
import loader.LoadableVariable;
import operators.RootOperator;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestScale
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestScale.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestScale.class);
	}
	
	public void testSample()
	{		
		System.out.println("Testing...");

		// define standard bounds for variables
		Object[] boundsFArgs = {new Float(-1000000.0), new Float(+1000000.0)};
		Loadable boundsF = new LoadableBounds(boundsFArgs);
		
		Object[] boundsIArgs = {new Integer(-1000000), new Integer(+1000000)};
		Loadable boundsI = new LoadableBounds(boundsIArgs);
		
		// define the variables with those bounds
		String nameP = "Pp";
		Object[] varPArgs = {nameP, boundsI};
		Loadable varP = new LoadableVariable(varPArgs);

		String nameQ = "Qq";
		Object[] varQArgs = {nameQ, boundsI};
		Loadable varQ = new LoadableVariable(varQArgs);

		String nameR = "Rr";
		Object[] varRArgs = {nameR, boundsI};
		Loadable varR = new LoadableVariable(varRArgs);

		String nameS = "Ss";
		Object[] varSArgs = {nameS, boundsI};
		Loadable varS = new LoadableVariable(varSArgs);

		String nameW = "Ww";
		Object[] varWArgs = {nameW, boundsF};
		Loadable varW = new LoadableVariable(varWArgs);

		String nameX = "Xx";
		Object[] varXArgs = {nameX, boundsF};
		Loadable varX = new LoadableVariable(varXArgs);

		String nameY = "Yy";
		Object[] varYArgs = {nameY, boundsF};
		Loadable varY = new LoadableVariable(varYArgs);

		String nameZ = "Zz";
		Object[] varZArgs = {nameZ, boundsF};
		Loadable varZ = new LoadableVariable(varZArgs);

		// define a reference to the variables
		Object[] refPArgs = {nameP};
		Loadable refToP = new LoadableLocal(refPArgs);
		
		Object[] refQArgs = {nameQ};
		Loadable refToQ = new LoadableLocal(refQArgs);
		
		Object[] refRArgs = {nameR};
		Loadable refToR = new LoadableLocal(refRArgs);
		
		Object[] refSArgs = {nameS};
		Loadable refToS = new LoadableLocal(refSArgs);
	
		Object[] refWArgs = {nameW};
		Loadable refToW = new LoadableLocal(refWArgs);
		
		Object[] refXArgs = {nameX};
		Loadable refToX = new LoadableLocal(refXArgs);
		
		Object[] refYArgs = {nameY};
		Loadable refToY = new LoadableLocal(refYArgs);
		
		Object[] refZArgs = {nameZ};
		Loadable refToZ = new LoadableLocal(refZArgs);
	
		// define some constants 
		Object[] constantArgs6 = {new Integer(6)};
		Loadable constant6 = new LoadableConstant(constantArgs6);
		
		Object[] constantArgs9 = {new Integer(-9)};
		Loadable constant9 = new LoadableConstant(constantArgs9);
				
		Object[] constantArgs69 = {new Float(6.9)};
		Loadable constant69 = new LoadableConstant(constantArgs69);
		
		Object[] constantArgs96 = {new Float(-0.96)};
		Loadable constant96 = new LoadableConstant(constantArgs96);
		
		// define some relations
		Object[] SqArgs = {RootOperator.Scale, constant6, refToQ};
		Loadable SqExpr = new LoadableExpression(SqArgs);
		
		Object[] pESqArgs = {RootOperator.Equal, refToP, SqExpr};
		Loadable pESqExpr = new LoadableExpression(pESqArgs);
		
		Object[] pESqCTArgs = {pESqExpr, "Pp == 6 Qq"};
		Loadable pESqCT = new LoadableConstraint(pESqCTArgs);
		
		Object[] SsArgs = {RootOperator.Scale, constant9, refToS};
		Loadable SsExpr = new LoadableExpression(SsArgs);
		
		Object[] rESsArgs = {RootOperator.Equal, refToR, SsExpr};
		Loadable rESsExpr = new LoadableExpression(rESsArgs);

		Object[] rESsCTArgs = {rESsExpr, "Rr == -9 Ss"};
		Loadable rESsCT = new LoadableConstraint(rESsCTArgs);
		
		Object[] SxArgs = {RootOperator.FloatScale, constant69, refToX};
		Loadable SxExpr = new LoadableExpression(SxArgs);
		
		Object[] wESxArgs = {RootOperator.Equal, refToW, SxExpr};
		Loadable wESxExpr = new LoadableExpression(wESxArgs);
		
		Object[] wESxCTArgs = {wESxExpr, "Ww == 6.9 Xx"};
		Loadable wESxCT = new LoadableConstraint(wESxCTArgs);
		
		Object[] SzArgs = {RootOperator.FloatScale, constant96, refToZ};
		Loadable SzExpr = new LoadableExpression(SzArgs);
		
		Object[] yESzArgs = {RootOperator.Equal, refToY, SzExpr};
		Loadable yESzExpr = new LoadableExpression(yESzArgs);

		Object[] yESzCTArgs = {yESzExpr, "Yy == -0.96 Zz"};
		Loadable yESzCT = new LoadableConstraint(yESzCTArgs);
		
		// define & display a model structure with the variable
		// this is the object that can be loaded and instantiated in KBs
		Object[] structureArgs = {"FlakeyFoont", 
				varP,
				varQ,
				varR,
				varS,
				varW,
				varX,
				varY,
				varZ,
				pESqCT,
				rESsCT,
				wESxCT,
				yESzCT,
		};
		
		LoadableStructure structure = new LoadableStructure(structureArgs);
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
 		
		String nameHerd = "Flakes";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
			
		Object[] herdArgs = {nameHerd, structure};
		LoadableStructure herd = new LoadableHerd(herdArgs);
		
		Object[] pathHerdArgs = {nameCase, nameHerd};
		
		
		// put it all together...
		
		Object[] shellArgs = {"Shell", 
				herd,
				};
		LoadableMember shell = new LoadableMember(shellArgs);
		
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		Context context = kb.getContext();
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
		
		String nameThing1 = "Spa";
		String nameThing2 = "Fon";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
	    System.out.println("\nAdding first to herd...:");
		kb.ensureMember(pathHerd1Args, structure);
		System.out.println(context);		
		kb.commitAll();

		System.out.println("\nAdding second to herd...:");
		kb.ensureMember(pathHerd2Args, structure);
		System.out.println(context);
		kb.commitAll();
		
		Object[] pathP1 = {nameCase, nameHerd, nameThing1, nameP};
		Object[] pathQ1 = {nameCase, nameHerd, nameThing1, nameQ};
		Object[] pathR1 = {nameCase, nameHerd, nameThing1, nameR};
		Object[] pathS1 = {nameCase, nameHerd, nameThing1, nameS};

		Object[] pathP2 = {nameCase, nameHerd, nameThing2, nameP};
		Object[] pathQ2 = {nameCase, nameHerd, nameThing2, nameQ};
		Object[] pathR2 = {nameCase, nameHerd, nameThing2, nameR};
		Object[] pathS2 = {nameCase, nameHerd, nameThing2, nameS};

		Assertion Q1Assertion = kb.newAssertion(pathQ1, new Integer(10));
		Assertion S1Assertion = kb.newAssertion(pathS1, new Integer(100));
		
		Assertion P2Assertion = kb.newAssertion(pathP2, new Integer(666));
		Assertion R2Assertion = kb.newAssertion(pathR2, new Integer(99));
		
		kb.assertAssertion(Q1Assertion);
		kb.assertAssertion(S1Assertion);
		kb.assertAssertion(P2Assertion);
		kb.assertAssertion(R2Assertion);
		
		Object[] pathW1 = {nameCase, nameHerd, nameThing1, nameW};
		Object[] pathX1 = {nameCase, nameHerd, nameThing1, nameX};
		Object[] pathY1 = {nameCase, nameHerd, nameThing1, nameY};
		Object[] pathZ1 = {nameCase, nameHerd, nameThing1, nameZ};

		Object[] pathW2 = {nameCase, nameHerd, nameThing2, nameW};
		Object[] pathX2 = {nameCase, nameHerd, nameThing2, nameX};
		Object[] pathY2 = {nameCase, nameHerd, nameThing2, nameY};
		Object[] pathZ2 = {nameCase, nameHerd, nameThing2, nameZ};

		Assertion X1Assertion = kb.newAssertion(pathX1, new Float(10.0));
		Assertion Z1Assertion = kb.newAssertion(pathZ1, new Float(100.0));
		
		Assertion W2Assertion = kb.newAssertion(pathW2, new Float(100.0));
		Assertion Y2Assertion = kb.newAssertion(pathY2, new Float(10.0));
		
		kb.assertAssertion(X1Assertion);
		kb.assertAssertion(Z1Assertion);
		kb.assertAssertion(W2Assertion);
		kb.assertAssertion(Y2Assertion);
		
		System.out.println("\nAfter assertions:");
		System.out.println(context);		

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));
		
		Object[] structureNames = kb.getStructureNames();
		System.out.println("\nTop level structures: " + toString(structureNames));		

		System.out.println("\nSerializing... ");
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bas);
			oos.writeObject(kb);
//			oos.flush();
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.toString());
		}

		System.out.println("\n...Test Done");
		
//		System.out.println("\n" + nameX + " : " + nameX.hashCode());
//		System.out.println("\n" + nameXX + " : " + nameXX.hashCode());
//		System.out.println("\n" + getName(nameX).equals(getName(nameXX)));
	}
}

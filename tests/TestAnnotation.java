/*
 * Created on Mar 9, 2005
 *
 
 */
package com.resonant.xkm.tests;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.kb.KBObject;
import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.loader.LoadableBounds;
import com.resonant.xkm.loader.LoadableHerd;
import com.resonant.xkm.loader.LoadableLocal;
import com.resonant.xkm.loader.LoadableMember;
import com.resonant.xkm.loader.LoadableStructure;
import com.resonant.xkm.loader.LoadableVariable;

/**
 * @author MLB
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestAnnotation
extends KBTestCase
{
   private static final Logger log = Logger.getLogger(TestAnnotation.class);

   /**
	 * Main method to run this testcase standalone
	 */
	public static void main(String args[])
	{
		junit.textui.TestRunner.run(TestAnnotation.class);
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
		Object[] annotatedNameB = {nameB, "Save this!"};
		Object[] varBArgs = {annotatedNameB, boundsB};
		Loadable varB = new LoadableVariable(varBArgs);

		// define a reference to the variable
		Object[] refBArgs = {nameB};
		Loadable refToB = new LoadableLocal(refBArgs);
		
		Object[] structureArgs = {"Driver", 
									varB,
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
//		Object[] pathBazArgs = {nameCase, nameFoo, null};
 		

		String nameHerd = "Drivers";
		Object[] refToHerdArgs = {nameHerd};
		Loadable refToHerd = new LoadableLocal(refToHerdArgs);
		
		Object[] herdArgs = {nameHerd, structure};
		// LoadableStructure herd = new LoadableHerd(herdArgs);
		LoadableMember herd = new LoadableHerd(herdArgs);
		
		Object[] pathHerdArgs = {nameCase, nameHerd};		
		
		// put it all together...
		
		Object[] shellArgs = {"Shell", 
				herd,
				};
		LoadableMember shell = new LoadableMember(shellArgs);
		
		
		// now we are ready to simulate runtime in a real process
		
		// create a KB and get a handle on its context (aka namespace)
		KBObject kb = new KBObject();
		kb.commitAll();
		
		Context context = kb.getContext();
		
		kb.ensureMember(pathCaseArgs, shell);
	
		String nameThing1 = "Mary";
		String nameThing2 = "John";
		Object[] pathHerd1Args = {nameCase, nameHerd, nameThing1};
		Object[] pathHerd2Args = {nameCase, nameHerd, nameThing2};
		
		kb.ensureMember(pathHerd1Args, structure);
		kb.ensureMember(pathHerd2Args, structure);

		System.out.println(context);

		System.out.println("\nChanged variables:");
		System.out.println(toString(kb.getChangedVariables()));

		System.out.println("\nChanged entailments:");
		System.out.println(toString(kb.getChangedEntailments()));

		Object annotation = kb.getAnnotation(new Object[] { 
						"Case",
						"Drivers",
						"Mary",
						"isPreferred",
						});
		
		System.out.println("\nAnnotation: " + annotation);
		
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

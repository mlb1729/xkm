/*
 * Created on Aug 25, 2005
 *
 
 */
package com.resonant.xkm.kb;

import java.io.Serializable;
import java.util.List;

import com.resonant.xkm.api.ConflictHandler;
import com.resonant.xkm.api.KB;
import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class DefaultConflictHandler
	extends Object
	implements ConflictHandler, Serializable
{
	private static final Object[] _globalPath = new Object[]{};
	
	public DefaultConflictHandler() {super();}

	public void handleConflict(Object info) {
		// see handleAssertionConflict() for info structure
		// get KB
		KB kb = (KB)(((List)info).get(0));
		// create a conflict message
		String message = "\nConflict with " + KMObject.toString(info);
		// save the conflict message as the top level "user object"
		kb.setUserObject(_globalPath, message);
		// also output the message
		System.out.println(message);
		return;
	}

}
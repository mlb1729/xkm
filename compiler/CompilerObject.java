/*
 * Created on Mar 29, 2005
 *
 
 */
package compiler;

import java.lang.reflect.Method;

import loader.Loadable;
import reader.Combo;

/**
 * @author MLB
 *
 *
 */
public class CompilerObject 
	extends FrameObject
	implements Compiler 
{
	public CompilerObject () {
		super();
	}
	
	public Loadable compile(Combo combo) {
		Loadable model = compile(combo, this);
		return model;
	}
	
	public Loadable compile(Combo combo, Frame frame) {
		return null;
	}

	public Loadable compileNewFrame(Combo combo, Frame frame) {
		Frame newFrame = new FrameObject(frame);
		Loadable model = compile(combo, newFrame);
		return model;
	}
	
	public boolean definePrimitive(Object name, String primitive) {
		boolean isNew = false;
		{
			Method method = null;
			Class [] signature = {Combo.class,Frame.class};
			try {
				method = this.getClass().getMethod(primitive,signature);
			} catch (Exception ex) {
				error("Bad method name " + primitive + " to define primitive " + name, ex);
			}
			if (method != null) {
				isNew = define(name, method);
			}
		} 
		return isNew;
	}
	
	public Loadable applyMethod (Method method, Combo combo, Frame frame) {
		Object [] args = {combo, frame};
		Loadable model = null;
		try {
			model = ((Loadable)(method.invoke(this, args)));
		} catch (Exception ex) {
			error("applyMethod failed", ex);
		}
		return model;
	}

}

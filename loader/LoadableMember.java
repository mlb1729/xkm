/*
 * Created on Aug 19, 2005
 *
 
 */
package loader;

import contexts.Context;
import expressions.Expression;

/**
 * @author MLB
 *
 *
 */
public class LoadableMember 
	extends LoadableStructure 
{
	public LoadableMember() {super();}
	public LoadableMember(Object[] parameters) {super(parameters);}
	
	public static void addMemberToStory(Object object, Context context){
		// context.addToStory((Expression)object, "Member of " + toString(context.getName()));
		context.addToStory((Expression)object);
		return;
	}
	
	public Object load(Context context) {
		Object object = super.load(context);
		addMemberToStory(object, context);
		return object;
	}
	
}

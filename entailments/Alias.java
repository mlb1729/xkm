/*
 * Created on Aug 11, 2005
 *
 
 */
package entailments;

import contexts.Context;

public class Alias 
	extends Entailment 
{
	Context	_aliasContext = null;
	
	private Context get_aliasContext() {return _aliasContext;}
	private void set_aliasContext(Context context) {_aliasContext = context;}
	
	public Alias() {super();}
	
	public Context getAliasContext () {
		Context context = get_aliasContext();
		if (context == null) {
			error("Attempt to access uninitialized alias context in " + this);
		}
		return context;
	}
	
	public void initAliasContext (Context context) {
		Context oldContext = get_aliasContext();
		if (oldContext != null) {
			error("Attempt to re-initialize alias context in " + this);
		}
		set_aliasContext(context);
		return;
	}

	public Object[] getPath() {
		Object[] path = getPath(true);
		return path;
	}
	
	public Context getPathContext() {
		Context context = getAliasContext();
		return context;
	}

}

/*
 * Created on Sep 28, 2004
 *
 
 */
package loader;

import com.resonant.xkm.contexts.Context;

/**
 * @author MLB
 *
 *
 */
public interface Loadable {
	Object		getParameter	(int i);
	Object[]	getParameters	();
	int			getSize			();
	Object 		load			(Context context);
}
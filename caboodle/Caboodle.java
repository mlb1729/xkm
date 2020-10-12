/*
 * Created on Jul 25, 2005
 *
 
 */
package caboodle;

import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.operators.Operator;
import com.resonant.xkm.reader.Combo;

/**
 * @author MLB
 *
 *
 */
public interface Caboodle {
	Loadable getLoadable(Object name);

	void setLoadable(Object name, Loadable loadable);

	Operator getOperator(Object name);

	Combo getDefinition(Object name);

	void setDefinition(Object name, Combo definition);
}
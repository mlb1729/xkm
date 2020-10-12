/*
 * Created on Jun 16, 2005
 *
 
 */
package caboodle;

import java.util.HashMap;
import java.util.Map;

import compiler.CompilerObject;
import loader.Loadable;
import loader.LoadableExpression;
import loader.LoadableObject;
import operators.Operator;
import operators.RootOperator;
import reader.Combo;

/**
 * @author MLB
 *
 *
 */
public class CaboodleObject 
	extends CompilerObject 
	implements Caboodle
{
	private Map _loadables = new HashMap();
	private Map	_operators = new HashMap();
	private Map	_definitions = new HashMap();
	
	private Map get_loadables() {return _loadables;}
	private Map get_operators() {return _operators;}
	private Map get_definitions() {return _definitions;}
	

	public CaboodleObject() {
		super();
		initOperators(RootOperator.rootOperators());
	}
	
	public Loadable getLoadable(Object name) {
		name = getName(name);
		Loadable loadable = ((Loadable)(get_loadables().get(name)));
		if (loadable == null) {
			Combo definition = getDefinition(name);
			if (definition != null) {
				loadable = compileDefinition(definition);
				if (loadable != null) {
					setLoadable(name, loadable);
				}
			}
		}
		return loadable;
	}

	public void setLoadable(Object name, Loadable loadable) {
		name = getName(name);
		get_loadables().put(name, loadable);
		return;
	}
	
	public Operator getOperator(Object name) {
		name = getName(name);
		Operator operator = ((Operator)(get_operators().get(name)));
		return operator;
	}
	
	protected void initOperators(Operator[] operators) {
		for (int i=0; i < operators.length; i++){
			Operator operator = operators[i];
			Object name = getName(operator);
			get_operators().put(name, operator);
		}
		return;
	}

	public Combo getDefinition(Object name) {
		name = getName(name);
		Combo definition = ((Combo)(get_definitions().get(name)));
		return definition;
	}

	public void setDefinition(Object name, Combo definition) {
		name = getName(name);
		get_definitions().put(name, definition);
		setLoadable(name, null);
		return;
	}

	public Loadable compileDefinition(Combo definition) {
		Loadable loadable = null;
		Object loadableType = definition.getType();
		if (loadableType != null) {
			Class loadableClass = LoadableObject.getLoadableClass(loadableType);
			if (loadableClass == null) {
				Operator operator = getOperator(loadableType);
				if (operator != null) {
						loadableType = LoadableExpression.class;
						definition.add(0,operator);
				}
			}
			if (loadableType == null) {
				error("Unresolvable operator " + loadableType + " (mispelled?)");
			} else {
				
			}
		}
		return loadable;
	}
}

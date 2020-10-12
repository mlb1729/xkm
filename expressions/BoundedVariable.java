/*
 * Created on Oct 1, 2004
 *
 
 */
package expressions;

import com.resonant.xkm.api.Binding;
import com.resonant.xkm.api.Interval;
import com.resonant.xkm.bounds.Bounds;
import com.resonant.xkm.bounds.CollidingBounds;
import com.resonant.xkm.kb.BindingObject;
import com.resonant.xkm.operators.Operator;
import com.resonant.xkm.operators.VariableOperator;
import com.resonant.xkm.points.Point;
import com.resonant.xkm.types.Type;

/**
 * @author MLB
 *
 *
 */
public class BoundedVariable 
	extends BoundedOperation
	implements Variable
{
	private	Object	_annotation	= null;
	private int		_oldMin		= +1;
	private int		_oldMax		= -1;
	
	public Object	getAnnotation(){
		return _annotation;
	}
	
	public void setAnnotation(Object annotation){
		_annotation  = annotation;
		return;
	}
	
	public int	getOldMinIndex(){
		return _oldMin;
	}
	
	public int	getOldMaxIndex(){
		return _oldMax;
	}
		
	public BoundedVariable(Object name) {
		super(name);
	}

	public BoundedVariable(Object name, Type type) {
		this(name);
		initBounds(type);
	}

	public BoundedVariable(Object name, Bounds bounds) {
		this(name);
		initBounds(new CollidingBounds(bounds));
	}

	// actually, a named constant
	public BoundedVariable(Object name, Point point) {
		this(name);
		initBounds(point);
	}
	
	public void onInitBounds(){
		updateOldBounds();
		return;
	}
	
	public void updateOldBounds(){
		_oldMin = getMinIndex();
		_oldMax = getMaxIndex();
		return;
	}

	public Object[] getVariablePath() {
		Object[] path = null;
		Operator operator = getOperator();
		if (operator instanceof VariableOperator) {
			VariableOperator variableOperator = ((VariableOperator)operator);
			if (variableOperator.getContext() == null) {
				path = variableOperator.getPathNames();
				path = null;
			} else {
				path = variableOperator.getPathNames();
			}
		}
		return path;
	}

	public Binding getBinding(){
		Object[] path = getVariablePath();
		Interval interval = getInterval();
		Binding binding = new BindingObject(path, interval);
		return binding;
	}
	
	public Binding getChangedBinding(){
		Binding binding = null;
		if ((getMinIndex() != getOldMinIndex()) || 
				(getMaxIndex() != getOldMaxIndex()))
		{
			binding = getBinding();
			updateOldBounds();
		}
		return binding;
	}

}

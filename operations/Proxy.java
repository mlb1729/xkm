/*
 * Created on Aug 6, 2005
 *
 
 */
package operations;

import contexts.Context;
import expressions.BoundedExpression;
import expressions.BoundedVariable;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public class Proxy
	extends BoundedVariable 
{
	BoundedExpression	_target		= null;
	Context				_context	= null;
	Object[]			_path		= null;
	
	private BoundedExpression get_target() {return _target;}
	private void set_target(BoundedExpression target) {_target = target;}
	
	private Context get_context() {return _context;}
	
	public Proxy(Object name, Context context, Object[] path) {
		super(name, Type.INTEGER);
		_context = context;
		_path = path;
	}

	public BoundedExpression getTarget() {
		BoundedExpression target = get_target();
		if (target == null) {
			Object object = get_context().fetch(_path);
			if (object != null) {
				target = (BoundedExpression)object;
				set_target(target);
				setType(target.getType());
				target.addDomainListener(this);
				onChanged();
			}
		}
		return target;
	}
	
	public void constrainOperands () {
		BoundedExpression target = getTarget();
		if (target != null) {
			target.changeMinIndex(getMinIndex());
			target.changeMaxIndex(getMaxIndex());
		}
		return;
	}
	
	public void constrainOperation () {
		BoundedExpression target = getTarget();
		if (target != null) {
			changeMinIndex(target.getMinIndex());
			changeMaxIndex(target.getMaxIndex());
		}
		return;
	}
	
}

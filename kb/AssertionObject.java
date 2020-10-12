/*
 * Created on Jun 22, 2005
 *
 
 */
package kb;

import api.Assertion;
import expressions.BoundedExpression;
import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class AssertionObject 
	extends KMObject 
	implements Assertion 
{
	private Object[]			_path;
	private Object				_relation;
	private Object				_value;
	private BoundedExpression	_expression = null;

	public Object[] getPath() {return _path;}
	public Object getRelation() {return _relation;}
	public Object getValue() {return _value;}
	public void setPath(Object[] path) {_path = path; return;}
	public void setRelation(Object relation) {_relation = relation; return;}
	public void setValue(Object value) {_value = value; return;}
	
	protected BoundedExpression getExpression(){return _expression;}
	protected void setExpression(BoundedExpression expression){ _expression = expression; return;}
	
	public AssertionObject() {super();}
	
	public AssertionObject(Object[] path, Object relation, Object value) {
		this();
		_path = path;
		_relation = relation;
		_value = value;
	}
	
	public String toString() {
		String string = toPathString(getPath()) + getRelation() + getValue();
		return string;
	}
}

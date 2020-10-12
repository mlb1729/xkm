/*
 * Created on Jun 22, 2005
 *
 
 */
package kb;

import api.Inclusion;
import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class InclusionObject 
	extends KMObject 
	implements Inclusion 
{
	private Object[]			_path;
	private Object				_descriptor;

	public Object[] getPath() {return _path;}
	public Object getDescriptor() {return _descriptor;}
	public void setPath(Object[] path) {_path = path; return;}
	public void setDescriptor(Object descriptor) {_descriptor = descriptor; return;}
	
	public InclusionObject() {super();}
	
	public InclusionObject(Object[] path, Object value) {
		this();
		_path = path;
		_descriptor = value;
	}
	
	public String toString() {
		String string = toPathString(getPath()) + ":=" + getDescriptor();
		return string;
	}
}

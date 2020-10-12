/*
 * Created on Jul 22, 2005
 *
 
 */
package kb;

import api.Interval;
import api.Binding;
import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class BindingObject 
	extends KMObject 
	implements Binding
{
	private Object[]	_path = null;
	private Interval 	_interval = null;

	public Object[]	getPath() {return _path;}
	public Interval getInterval() {return _interval;}

	public void setPath(Object[] path) {_path = path; return;}
	public void setInterval(Interval interval) {_interval = interval; return;}

	public BindingObject(){super();}
	
	public BindingObject(Object[] path, Interval interval) {
		this();
		setPath(path);
		setInterval(interval);
	}
	
	public String toString() {
		String string = toPathString(getPath()) + "=" + getInterval();
		return string;
	}
}

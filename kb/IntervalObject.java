/*
 * Created on Jul 22, 2005
 *
 
 */
package kb;

import api.Interval;
import km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class IntervalObject 
	extends KMObject 
	implements Interval 
{
	private Object	_min = null;
	private Object 	_max = null;

	public Object getMin() {return _min;}
	public Object getMax() {return _max;}

	public void setMin(Object value) {_min = value; return;}
	public void setMax(Object value) {_max = value; return;}

	public IntervalObject() {super();}
	
	public IntervalObject(Object min, Object max) {
		this();
		setMin(min);
		setMax(max);
	}

	public boolean isBound() {
		boolean isBound = getMin().equals(getMax());
		return isBound;
	}
	
	public String toString() {
		Object min = getMin();
		String string = toString(min);
		if (!isBound()) {
			if (min instanceof Boolean) {
				string = "unknown";
			} else {
				string += ":" + getMax(); 
			}
		}
		return string;
	}

}

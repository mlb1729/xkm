/*
 * Created on Jul 22, 2005
 *
 
 */
package api;

/**
 * @author MLB
 *
 *
 */
public interface Binding {
	Object[]	getPath();
	Interval	getInterval();
	void		setPath(Object[] path);
	void		setInterval(Interval interval);
}

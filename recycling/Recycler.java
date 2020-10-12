/*
 * Created on Sep 17, 2004
 *
 
 */
package recycling;

/**
 * @author MLB
 *
 *
 */
public interface Recycler {
	Recyclable	getObject();
	Recyclable	getObject(Class objectClass);
	void		recycle(Recyclable recyclable);
}

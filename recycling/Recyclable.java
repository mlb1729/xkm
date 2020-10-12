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
public interface Recyclable 
{
	Recycler	getRecycler();
	void		setRecycler(Recycler recycler);
	void		release();
}

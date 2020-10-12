/*
 * Created on Sep 17, 2004
 *
 
 */
package changes;

import recycling.Recyclable;

/**
 * @author MLB
 *
 *
 */
public interface Change 
	extends Recyclable 
{
	public void undo();
}

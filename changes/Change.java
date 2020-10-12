/*
 * Created on Sep 17, 2004
 *
 
 */
package changes;

import com.resonant.xkm.recycling.Recyclable;

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

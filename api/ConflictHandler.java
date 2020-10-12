/*
 * Created on Aug 25, 2005
 *
 
 */
package api;

import java.io.Serializable;

/**
 * @author MLB
 *
 *
 */
public interface ConflictHandler 
	extends Serializable
{
	void	handleConflict(Object info);
}

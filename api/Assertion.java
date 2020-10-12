/*
 * Created on Jun 22, 2005
 *
 
 */
package api;

/**
 * @author MLB
 *
 *
 */
public interface Assertion 
	extends Command
{
	Object []	getPath();
	Object		getRelation();
	Object		getValue();
	void		setPath(Object [] path);
	void		setRelation(Object relation);
	void		setValue(Object value);
}

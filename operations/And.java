/*
 * Created on Oct 5, 2004
 *
 
 */
package operations;

public class And 
	extends NaryBoolean 
{
	public And(){super(true, true);}
	
	public And(Object name) {
		super(name);
		initBooleans(true, true);
	}
}

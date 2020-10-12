/*
 * Created on Sep 27, 2004
 *
 
 */
package domains;

/**
 * @author MLB
 *
 *
 */
public class ChangeIndex 
	extends ChangeManagedPoint 
{
	private int	_index;

	private int get_index() {return _index;}
	private void set_index(int index) {_index = index;}
	
	public ChangeIndex() {super();}
	
	public void initPoint(ManagedPoint point) {
		super.initPoint(point);
		set_index(point.getIndex());
		return;
	}
	
	public void undo() {
		getPoint().setIndex(get_index());
		return;
	}			

}

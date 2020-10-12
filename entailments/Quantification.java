 /*
 * Created on Aug 9, 2005
 *
 
 */
package entailments;

import java.util.Iterator;
import java.util.List;

import domains.Domain;
import expressions.BasisListener;
import loader.LoadableEntailment;

/**
 * @author MLB
 *
 *
 */
public class Quantification 
	extends Herd
	implements BasisListener
{
	private Object				_freeVariableName		= null;
	private LoadableEntailment	_bodyLoadableEntailment	= null;
	
	private Object get_freeVariableName() {return _freeVariableName;}
	private void set_freeVariableName(Object name) {_freeVariableName = getName(name);}
	
	private LoadableEntailment get_bodyLoadableEntailment() {return _bodyLoadableEntailment;}
	private void set_bodyLoadableEntailment(LoadableEntailment entailment) {_bodyLoadableEntailment = entailment;}

	public Quantification() {super();}
	
	public void initializeLocalNameAndEntailment(Object freeVariableName, 
													Entailment basisEntailment, 
													LoadableEntailment bodyLoadableEntailment) {
		initFreeVariableName(freeVariableName);
		initBodyLoadableEntailment(bodyLoadableEntailment);
		basisEntailment.addBasisListener(this);
		List basis = basisEntailment.getBasis();
		if (basis != null){
			Iterator it = basis.iterator();
			while(it.hasNext()) {
				newBasisElement((Domain)(it.next()));
			}
		}
		return;
	}
	
	public Object getFreeVariableName() {
		Object freeVariableName = get_freeVariableName();
		if (freeVariableName == null) {
			error("Attempt to access uninitialized local name in " + this);
		}
		return freeVariableName;
	}
	
	public void initFreeVariableName(Object freeVariableName) {
		Object old = get_freeVariableName();
		if (old != null) {
			error("Attempt to re-initialize loadable in " + this + " from " + old + " to " + freeVariableName);
		}
		set_freeVariableName(freeVariableName);
		return;
	}
	
	public LoadableEntailment getBodyLoadableEntailment() {
		LoadableEntailment entailment = get_bodyLoadableEntailment();
		if (entailment == null) {
			error("Attempt to access uninitialized basis entailmentr in " + this);
		}
		return entailment;
	}
	
	public void initBodyLoadableEntailment (LoadableEntailment entailment) {
		Object old = get_bodyLoadableEntailment();
		if (old != null) {
			error("Attempt to re-initialize entailment in " + this + " from " + old + " to " + entailment);
		}
		set_bodyLoadableEntailment(entailment);
		return;
	}

	public void newBasisElement(Domain newOperand) {
		if (newOperand instanceof Entailment) {
			Closure closure = addClosure(getFreeVariableName(), 
											(Entailment) newOperand, 
											getBodyLoadableEntailment());
			super.addNewBasisElement(closure);
		}
		return;
	}

}

/*
 * Created on Jul 6, 2005
 *
 
 */
package api;

import com.resonant.xkm.caboodle.Caboodle;


/**
 * @author MLB
 *
 *
 */
public interface KB 
{
	Assertion	newAssertion(Object[] path, Object relation, Object value);
	Assertion 	newAssertion(Object[] path, Object value);
	Assertion 	newAssertion(Object[] path);
	Inclusion	newInclusion(Object[] path, Object descriptor);
	
	Interval	newInterval(Object min, Object max);
	Binding		newBinding(Object[] path, Interval interval);
	
	boolean 	assertAssertion(Assertion assertion);
	boolean 	deassertAssertion(Assertion assertion);
	
	boolean 	wrappedAssertAssertion(Assertion assertion) 
					throws WrappedKBException;
	boolean 	wrappedDeassertAssertion(Assertion assertion) 
					throws WrappedKBException;
	
//	Assertion[] getAssertions		();

	Command[] 	getCommands			();
	boolean		addCommands			(Command[] commands);
	
	boolean		wrappedAddCommands	(Command[] commands)
		throws WrappedKBException;

	void 		commitAll();
	void 		undoAll();
	
	Interval	getVariableInterval(Object[] path);
	
	Interval	wrappedGetVariableInterval(Object[] path)
		throws WrappedKBException;

	Binding[]	getChangedVariables();
	void 		clearChangedVariables();
	
	Object		getUserObject(Object[] path);
	void		setUserObject(Object[] path, Object object);
	
	Object		getAnnotation(Object[] path);
	void		setAnnotation(Object[] path, Object object);
	
	Caboodle	getCaboodle			();
	void		initCaboodle		(Caboodle caboodle);
	
	ConflictHandler	getConflictHandler();
	void			setConflictHandler(ConflictHandler handler);
	
	Object[]	getStructureNames	();
	
	boolean 	ensureMember		(Object[] path, Object descriptor);
	boolean 	ensureMember		(Inclusion inclusion);
	
	boolean 	wrappedEnsureMember	(Object[] path, Object descriptor) 
					throws WrappedKBException;
	boolean 	wrappedEnsureMember	(Inclusion inclusion) 
					throws WrappedKBException;
	
	boolean		retractMember		(Object object);
	boolean		restoreMember		(Object object);
	
	// temporary
//	Object[]	getRawChangedVariables	();
	
}
/*
 * Created on Sep 28, 2004
 *
 
 */
package contexts;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import caboodle.Caboodle;
import containers.KMSet;
import expressions.BoundedExpression;
import expressions.Expression;
import kb.KBMember;
import km.Fetcher;
import km.Named;
import operators.Operator;


/**
 * @author MLB
 *
 *
 */
public interface Context
	extends KBMember, Fetcher, Named
{
	Operator 	getOperator			(Object name);
	Expression 	getVariable			(Object name);
	Operator 	findOperator		(Object name);
	Expression 	findVariable		(Object name);
	List		getAllVariables		();
	Object[]	getStructureNames	();
//	List		getConstraints		();
//	Set			getMembers			();
//	Iterator	iterator			();
	boolean		addOperator			(Operator operator);
	boolean		addVariable			(Expression variable);
	boolean		addOwnVariable		(Expression variable);
	Iterator 	ownVariablesIterator();
	boolean		addMember			(KBMember member);
	String		toString			(String newLine);
	
	BoundedExpression	getStory	();
	void				initStory	(BoundedExpression story);
	boolean				addToStory	(Expression expression);
	boolean				addToStory	(Expression expression, Object reason);
	
	Map			getConstraintMap	();
	
	Context				getParent	();
//	Set					getMembers	();
//	List				getMembers	();
	KMSet				getMembers	();
	
	void 				getVariablePath(List list);
	
	Caboodle	getCaboodle			();
	void		initCaboodle		(Caboodle caboodle);
	
	void		setName				(Object name);
	
	boolean 	matchesVariables		(List testVariables);
	List		getMatchTestVariables	();
	Object 		getMatchingMember		(Context testContext);
	
	Object		find(Object[] path);
	
	void 		collectVariableChanges	(List bindings);
	void 		clearVariableChanges	();
}
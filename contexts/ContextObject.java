/*
 * Created on Sep 28, 2004
 *
 
 */
package contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import api.Binding;
import caboodle.Caboodle;
import caboodle.CaboodleObject;
import containers.KMSet;
import containers.Namespace;
import containers.NamespaceObject;
import domains.Domain;
import entailments.Entailment;
import expressions.BoundedExpression;
import expressions.BoundedVariable;
import expressions.Expression;
import expressions.Operation;
import expressions.SetObject;
import expressions.Variable;
import kb.KBMember;
import kb.KBMemberObject;
import kb.KBObject;
import operators.Operator;
import operators.RootOperator;
import operators.VariableOperator;

/**
 * @author MLB
 *
 *
 */
public class ContextObject 
	extends KBMemberObject 
	implements Context 
{
	private static int		_INITIAL_MEMBER_SET_SIZE		= 4;
	private static int		_INITIAL_CONSTRAINT_MAP_SIZE	= 4;
	
	private ContextObject	_parent			= null;
//	private Set				_members		= null;
	private KMSet			_members		= null;
	private Namespace		_operators		= null;
	private Namespace		_variables		= null;
	private	Map				_constraintMap	= null;
	private Caboodle		_caboodle		= null;
	private Object			_name			= "";
	
	private BoundedExpression	_story	= null;

	private ContextObject get_parent() {return _parent;}
//	private Set get_members() {return _members;}
//	private void set_members(Set members) {_members = members;}
	private KMSet get_members() {return _members;}
	private void set_members(KMSet members) {_members = members;}
	private Namespace get_operators() {return _operators;}
	private void set_operators(Namespace operators) {_operators = operators;}
	private Namespace get_variables() {return _variables;}
	private void set_variables(Namespace variables) {_variables = variables;}
	private Map get_constraintMap() {return _constraintMap;}
	private void set_constraintMap(Map map) {_constraintMap = map;}
	
	private Caboodle get_caboodle() {return _caboodle;}
	private void set_caboodle(Caboodle caboodle) {_caboodle = caboodle;}
	
	private Object get_name() {return _name;}
	private void set_name(Object name) {_name = getName(name);}
	
	private BoundedExpression get_story() {return _story;}
	private void set_story(BoundedExpression story) {_story = story;}

	public ContextObject () {
		super();
	}
	
	public ContextObject (ContextObject parent) {
		this();
		_parent = parent;
		if (parent != null) {
			parent.addMember(this);
		}
	}
	
	public Context getParent () {
		Context parent = get_parent();
		return parent;
	}
	
//	public Set getMembers () {
	public KMSet getMembers () {
//		Set members = get_members();
		KMSet members = get_members();
		if (members == null) {
//			members = new HashSet(_INITIAL_MEMBER_SET_SIZE);
//			members = new ArrayList(_INITIAL_MEMBER_SET_SIZE);
			members = new KMSet(_INITIAL_MEMBER_SET_SIZE);
			set_members(members);
		}
		return members;
	}
	
	protected Namespace operatorNamespace() {
		Namespace operators = get_operators();
		if (operators == null) {
			operators = new NamespaceObject();
			ContextObject parent = get_parent();
			operators.setParent((parent == null) ?
									RootOperator.namespace() :
									parent.operatorNamespace());
			set_operators(operators);
		}
		return operators;
	}
	
	protected Namespace variableNamespace() {
		Namespace variables = get_variables();
		if (variables == null) {
			variables = new NamespaceObject();
			ContextObject parent = get_parent();
			if (parent != null) {
				variables.setParent(parent.variableNamespace());
			}
			set_variables(variables);
		}
		return variables;
	}
	
	public void getVariablePath(List list) {
		// variableNamespace().getPath(null, list);
		Context parent = getParent();
		if (parent != null) {
			parent.getVariablePath(list);
//			if (getName() != "") {	// filter anonymous contours (eg top level)
			if (!(getName().equals(""))) {	// filter anonymous contours (eg top level)
				list.add(this);
			}
		}
		return;
	}
	
	public Map getConstraintMap () {
		Map map = get_constraintMap();
		return map;
	}
	
	public Map constraintMap() {
		Map map = getConstraintMap();
		if (map == null) {
			map = new HashMap(_INITIAL_CONSTRAINT_MAP_SIZE);
			set_constraintMap(map);
		}
		return map;
	}
	
	public Caboodle getCaboodle() {
		Caboodle caboodle = get_caboodle();
		if (caboodle == null) {
			Context parent = getParent();
			if (parent != null) {
				caboodle = parent.getCaboodle();
			} else {
				caboodle = new CaboodleObject();	// ToDo MLB: generalize
			}
			set_caboodle(caboodle);
		}
		return caboodle;
	}
	
	public void initCaboodle(Caboodle caboodle) {
		Caboodle oldCaboodle = get_caboodle();
		if ((oldCaboodle != null) && (oldCaboodle != caboodle)) {
			error("Can't change from " + oldCaboodle.getClass() + 
					" " + oldCaboodle + " to " + caboodle);
		} else {
			set_caboodle(caboodle);
		}
		return;
	}
	
	public Object getName() {
		Object name = get_name();
		return name;
	}
	
	public void setName(Object name) {
		set_name(name);
// 		variableNamespace().setName(name);
		return;
	}

	public Operator getOperator (Object name) {
// 		Operator operator = (Operator)(operatorNamespace().get(name));
		Operator operator = getCaboodle().getOperator(name);
		return operator;
	}
	
	public Expression getVariable (Object name) {
		Expression variable = (Expression)(variableNamespace().get(name));
		return variable;
	}
	
	public Operator findOperator (Object name) {
//		Operator operator = (Operator)(operatorNamespace().find(name));
		Operator operator = getCaboodle().getOperator(name);
		return operator;
	}
	
	public Expression findVariable (Object name) {
		Expression variable = (Expression)(variableNamespace().find(name));
		return variable;
	}
		
	public Object get (Object key) {
		Object object = findVariable(key);
		if ((object == null) && (key instanceof Entailment)) {
			object = getMatchingMember((Entailment)key);
		}
		return object;
	}
	
	public boolean addOperator (Operator operator) {
		boolean isAdded = operatorNamespace().add(operator);
		return isAdded;
	}
	
	public boolean addVariable (Expression variable) {
		boolean isAdded = variableNamespace().add(variable);
		if (isAdded) {
			addMember(variable);
		}
		return isAdded;
	}
	
	public boolean addOwnVariable (Expression variable) {
		boolean isAdded = addVariable(variable);
		if (isAdded) {
			VariableOperator variableOperator = ((VariableOperator)variable.getOperator());
			variableOperator.initContext(this);
		}
		return isAdded;
	}
	
	public boolean addToStory(Expression expression) {
		boolean isAdded = getStory().getOperands().add(expression);
		if (isAdded) {
			addMember(expression);
		}
		return isAdded;
	}
	
	public boolean addToStory(Expression expression, Object reason) {
		boolean isAdded = addToStory(expression);
		if (isAdded) {
			expression.initReason(reason);
			Object description = expression.getReason();
			Map map = constraintMap();
			Object name = getName(description);
			Object oldExpression = map.get(name);
			if (oldExpression != null){
				String message = "Duplicate constraint name: " + name + 
					"\n  " + oldExpression +
					"\n  " + expression; 
				System.out.println(message);
//				error(message);
				map.remove(oldExpression);
			} else {
			}
			map.put(name, expression);
			map.put(expression, name);
		}
		return isAdded;
	}
	
	public boolean addMember (KBMember member) {
		member.setKB(getKB());
		boolean isAdded = getMembers().add(member);
		if (isAdded){
			member.setLocalContext(this);
		}
		return isAdded;
	}
	
	public List collectOwnClassMembers (List list, Class memberClass) {
		Context parent = getParent();
		Namespace variables = get_variables();
		collectClassMembers(ownVariablesIterator(), list, memberClass);
		return list;
	}
	
	public List getAllVariables () {
		Context parent = getParent();
		List list = ((parent == null) ? new ArrayList() : parent.getAllVariables());
		collectOwnClassMembers(list, Variable.class);
		return list;
	}
	
	public Object[] getStructureNames() {
		List list = new ArrayList();
		Iterator iterator = ownVariablesIterator();
		while (iterator.hasNext()){
			Object object = iterator.next();
			if (object instanceof Entailment){
				Entailment entailment = (Entailment)object;
				if (entailment.getContext().getParent() == this){
					list.add(getName(entailment));
				}
			}
		}
		Object[] names = list.toArray();
		return names;
	}
	
	public Iterator ownVariablesIterator(){
		Namespace variables = get_variables();
		// Iterator iterator = iterator(variables);
		Iterator iterator = ((variables == null) ? EMPTY_ITERATOR : variables.iterator());
		return iterator;
	}
	
//	public Iterator iterator () {
//		Set members = get_members();
//		Iterator iterator = ((members == null) ? EMPTY_ITERATOR : (new TreeSet(members)).iterator());
//		return iterator;
//	}
	
	/*
	public List getConstraints () {
		Set members = get_members();
		boolean hasMembers = (members != null);
		List list = (hasMembers ? new ArrayList() : Collections.EMPTY_LIST);
		if (hasMembers) {
			list = collectClassMembers(members.iterator(), list, Constraint.class);
		}
		return list;
	}
	*/
	
	public void setKB (KBObject kb) {
		super.setKB(kb);
//		Set members = get_members();
		KMSet members = get_members();
		if (members != null) {
			Iterator it = members.iterator();
			while (it.hasNext()) {
				Object object = it.next();
				if (object instanceof KBMember) {
					((KBMember)object).setKB(kb);
				}
			}
		}
		return;
	}
	
	public boolean activate () {
		boolean isActivated = super.activate();
		if (isActivated) {
//			Set members = get_members();
			KMSet members = get_members();
			if (members != null) {
				Iterator it = members.iterator();
				while (it.hasNext()) {
					Object object = it.next();
					if (object instanceof KBMember) {
						((KBMember)object).activate();
					}
				}
			}
		}
		return isActivated;
	}
	
	public boolean suspend () {
		super.suspend();
		boolean isSuspending = wasActive();
		if (isSuspending) {
//			Set members = get_members();
			KMSet members = get_members();
			if (members != null) {
				Iterator it = members.iterator();
				while (it.hasNext()) {
					Object object = it.next();
					if (object instanceof KBMember) {
						((KBMember)object).suspend();
					}
				}
			}
		}
		return isSuspending;
	}
	
	public String toString (String newLine) {
		String string = "";
		Namespace variables = get_variables();
		if (variables != null) {
			Iterator it = collectClassMembers(variables.iterator(), new ArrayList(), Variable.class).iterator();
			while (it.hasNext()) {
				Operation variable = (Operation)(it.next());
				string += newLine;
				if (variable instanceof SetObject) {
					SetObject set = (SetObject)variable;
					string += set.getName();
					string += " = " + set.toStringOperatorOperands();
				} else if (variable instanceof Entailment) {
					Entailment module = ((Entailment)variable);
					string += variable + ": " + module.toStringValue();
					Context context = module.getContext();
					if (context != this) {
						string += " {" + context.toString(newLine + "  ") + newLine + "}";
					}
				} else {
					string += variable + " = " + variable.toStringValue();
				}
			}
		}
		Iterator it = collectClassMembers(getMembers().iterator(), new ArrayList(), Expression.class).iterator();
		while (it.hasNext()) {
			Expression expression = (Expression)(it.next());
			if (!(expression instanceof Variable)) {
				string += newLine + expression + ": " + expression.toStringValue(); 
			}
		}
		return string;
	}
	
	public String toString () {
		String string = toString("\n");
		return string;
	}
	
	public BoundedExpression getStory () {
		return get_story();
	}
	
	public void initStory (BoundedExpression story) {
		BoundedExpression oldStory = getStory();
		if ((oldStory != null) && (story != oldStory)) {
			error("Can't re-initialize story expression of context " + this + 
					" from " + oldStory + " to " + story);
		}
		set_story(story);
		return;
	}
	
	public boolean matchesVariables(List testVariables) {
		boolean matches = true;
		Iterator it = testVariables.iterator();
		while(it.hasNext()) {
			BoundedVariable testVariable = ((BoundedVariable)it.next());
			Object name = testVariable.getName();
			BoundedVariable contextVariable = ((BoundedVariable)getVariable(name));
			if ((contextVariable == null) ||
					!contextVariable.isKnownToBe(testVariable.getMinIndex())) {
				matches = false;
				break;
			}
		}
		return matches;
	}
	
	public List getMatchTestVariables() {
		List testVariables = new ArrayList();
		Iterator it = getAllVariables().iterator();
		while (it.hasNext()) {
			Object variable = it.next();
			if (variable instanceof BoundedVariable) {
				BoundedVariable testVariable = ((BoundedVariable)variable);
				if (testVariable.isPoint()) {
					testVariables.add(testVariable);
				} else {
					testVariables = null;
					break;
				}
			}
		}
		return testVariables;
	}
	
	public Object getMatchingMember(Context testContext) {
		Object result = null;
		List testVariables = testContext.getMatchTestVariables();
		if (testVariables != null) {
			Iterator it = getMembers().iterator();
			while (it.hasNext()) {
				Object member = it.next();
				if (member instanceof Entailment) {
					Context memberContext = ((Entailment)member).getContext();
					if ((memberContext != null) &&
							memberContext.matchesVariables(testVariables)) {
						if (result == null) {
							result = member;
						} else {
							error("Pattern matched more than one member.");
						}
					}
				}
			}
		}
		return result;
	}
	
	public Object getMatchingMember(Entailment testEntailment) {
		Object object = getMatchingMember(testEntailment.getContext());
		return object;
	}
	
	public Object fetch(Object[] path){
		Object object = super.fetch(getKB().resolvePath(path));
		return object;
	}
	
	public Object find(Object[] path) {
		Object result = fetch(path);
		if (result == null) {
			Context parent = getParent();
			if (parent != null) {
				result = parent.find(path);
			}
		}
		return result;
	}
	
	public void resetAllMembers(){
		KMSet members = get_members();
		if (members != null){
			Iterator iterator = members.iterator();
			while (iterator.hasNext()){
				Object member = iterator.next();
				if (member instanceof Domain) {
					((Domain)member).reset();
				} else if (member instanceof ContextObject){
					((ContextObject)member).resetAllMembers();
				} else {
					error("Bad class " + member.getClass() + " for context member " + member);
				}
			}
		}
		return;
	}

	public void collectVariableChanges(List bindings){
		KMSet members = get_members();
		if (members != null){
			Iterator iterator = members.iterator();
			while (iterator.hasNext()){
				Object member = iterator.next();
				if (member instanceof BoundedVariable) {
					Binding change = ((BoundedVariable)member).getChangedBinding();
					if (change != null){
						bindings.add(change);
					}
				} else if (member instanceof ContextObject){
					((ContextObject)member).collectVariableChanges(bindings);
				}
			}
		}
		return;
	}
	
	public void clearVariableChanges(){
		KMSet members = get_members();
		if (members != null){
			Iterator iterator = members.iterator();
			while (iterator.hasNext()){
				Object member = iterator.next();
				if (member instanceof BoundedVariable) {
					((BoundedVariable)member).updateOldBounds();
				} else if (member instanceof ContextObject){
					((ContextObject)member).clearVariableChanges();
				}
			}
		}
		return;
	}

}

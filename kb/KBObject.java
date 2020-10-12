/*
 * Created on Sep 9, 2004
 *
 
 */
package kb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import api.Assertion;
import api.Binding;
import api.Command;
import api.ConflictHandler;
import api.Inclusion;
import api.Interval;
import api.KB;
import api.WrappedKBException;
import caboodle.Caboodle;
import changes.Change;
import changes.ChangeManager;
import contexts.Context;
import contexts.ContextObject;
import domains.ChangeIndex;
import domains.Domain;
import domains.ManagedPoint;
import entailments.Entailment;
import entailments.Herd;
import entailments.TopLevelEntailment;
import exceptions.Contradiction;
import exceptions.KMException;
import expressions.BoundedExpression;
import expressions.Expression;
import expressions.Variable;
import km.Fetcher;
import loader.Loadable;
import loader.LoadableConstant;
import loader.LoadableConstrainment;
import loader.LoadableEntailment;
import loader.LoadableExpression;
import loader.LoadableGlobal;
import operators.EntailmentOperator;
import points.Point;
import points.PointListener;
import types.Type;

/**
 * @author MLB
 *
 *
 */
public final class KBObject 
	extends ChangeManager 
	implements Fetcher, KB
{
	private static final ConflictHandler _defaultConflictHandler =  new DefaultConflictHandler();
	
	// someday KBs might have parents...
	
	private final 	Context	_context	= new ContextObject();
//	private final 	Set		_members	= new HashSet();
//	private final 	List	_members	= new ArrayList();
	private final 	List	_assertions = new ArrayList();
	private final 	List	_basis		= new ArrayList();
	private final 	List	_pending	= new ArrayList();
	private final 	List	_commands	= new ArrayList();
	
	private		ConflictHandler	_conflictHandler	= _defaultConflictHandler;
	
	private Context get_context() {return _context;}
//	private Set get_members() {return _members;}
//	private List get_members() {return _members;}
	private List get_assertions() {return _assertions;}
	private List get_basis() {return _basis;}
	private List get_pending() {return _pending;}
	private List get_commands() {return _commands;}
	
	private int _UID = 0;
	
	public ConflictHandler	getConflictHandler(){return _conflictHandler;}
	public void				setConflictHandler(ConflictHandler handler){_conflictHandler = handler; return;}
	
	private	final HashMap	_statistics = new HashMap();
	
	public HashMap	getStatistics() {return _statistics;}
	
	public void clearStatistics(){
		getStatistics().clear();
		return;
	}
	
	public String statisticsToString() {
		String string = "KB Statistics:\n";
		HashMap statistics = getStatistics();
		Set keys = statistics.keySet();
		Iterator keyIterator = keys.iterator();
		ArrayList list = new ArrayList();
		while (keyIterator.hasNext()){
			Object key = keyIterator.next();
			list.add(key);
		}
		Collections.sort(list);
		Iterator listIterator = list.iterator();
		while (listIterator.hasNext()){
			Object key = listIterator.next();
			Object value = getStatistic(key);
			string += "  " + toString(key) + ": " + toString(value) + "\n";
		}
		return string;
	}
	
	public Integer getStatistic(Object name){
		String key = toString(name);
		HashMap statistics = getStatistics();
		Object value = statistics.get(key);
		if (value == null){
			value = new Integer(0);
		}
		Integer statistic = (Integer)value;
		return statistic;
	}
	
	public void setStatistic(Object name, Integer integer){
		String key = toString(name);
		HashMap statistics = getStatistics();
		statistics.put(key, integer);
		return;
	}
	
	public void setStatistic(Object name, int integer){
		setStatistic(name, new Integer(integer));
		return;
	}
	
	public void incrementStatistic(Object name){
		setStatistic(name, getStatistic(name).intValue() + 1);
		return;
	}
	
	public void decrementStatistic(Object name){
		setStatistic(name, getStatistic(name).intValue() - 1);
		return;
	}
	
	
//	private			boolean	_loading	= false;
//	private			boolean	_deferring	= false;
//	public boolean isLoading(){return _loading;}
//	public void setLoading(boolean loading){_loading = loading;}
//	public boolean isDeferring(){return _deferring;}
//	public void setDeferring(boolean deferring){_deferring = deferring;}
	
	public KBObject() {
		super();
		Context context = getContext();
		context.activate();
		addMember(context);
		
		EntailmentOperator operator = new EntailmentOperator("$", TopLevelEntailment.class);		
		List operands = new ArrayList();
		Expression expression = operator.newExpression(operands, context);
		// expression.setKB(this);
		BoundedExpression story = (BoundedExpression)expression;
		context.initStory(story);
		addMember(story);
		story.activate();
		// story.changeToBe(true, this);
		commitAll();
	}
	
	public Context getContext() {
		return get_context();
	}

	public Caboodle getCaboodle() {
		Caboodle caboodle = getContext().getCaboodle();
		return caboodle;
	}

	public void initCaboodle(Caboodle caboodle) {
		getContext().initCaboodle(caboodle);
		return;
	}

	public int getUID () {
		return _UID;
	}
	
	public int nextUID () {
		return ++_UID;
	}
	
	public boolean addMember (KBMember member) {
//		boolean isAdded = get_members().add(member);
		boolean isAdded = true;
		if (isAdded) {
			member.setKB(this);
		}
		return isAdded;
	}

	public boolean removeMember (KBMember member) {
//		boolean isRemoved = get_members().remove(member);
		boolean isRemoved = true;
		if (isRemoved) {
			member.setKB(null);
		}
		return isRemoved;
	}
	
	public boolean retractMember (Object object) {
		boolean isRestored = true;
		if (object instanceof KBMember){
			KBMember member = (KBMember)object;
			if ((member.getKB() == this) &&
					!(member.isRetracted())) {
				resetAllMembers();
				member.retract();
				isRestored = restoreBasis();
			}
		}
		return isRestored;
	}
	
	// ToDo MLB: not sure if this is a good idea...
	public boolean restoreMember (Object object) {
		boolean isRestored = true;
		if (object instanceof KBMember){
			KBMember member = (KBMember)object;
			if ((member.getKB() == this) &&
					(member.isRetracted())) {
				resetAllMembers();
				isRestored = member.restore();
				if (isRestored){
					isRestored = restoreBasis();
				}
			}
		}
		return isRestored;
	}

	public void load(Loadable model) {
		model.load(getContext());
		return;
	}

	public boolean addEntailmentAtPathAs(LoadableEntailment loadable, Object[] path, Object name) {
		Context context = getContext();
		Object object = context.fetch(path);
		if (object instanceof Entailment) {
			Entailment container = ((Entailment)object);
			context = container.getContext();
		}
		Object member = context.get(name);
		boolean isNew = (member == null);
		if (isNew) {
			loadable.load(context, name);
		} else {
			// ToDo MLB: throw up or whatever 
		}
		return isNew;
	}
	
	public boolean wrappedEnsureMember(Object[] path, Object descriptor)
		throws WrappedKBException
	{
		boolean result=false;
		try {
			result = ensureMember(path, descriptor);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public boolean ensureMember(Object[] path, Object descriptor) 
	{
		Inclusion inclusion = newInclusion(path, descriptor);
		boolean exists = ensureMember(inclusion);
		return exists;
	}
	
	public boolean wrappedEnsureMember(Inclusion inclusion)
		throws WrappedKBException
	{
		boolean result=false;
		try {
			result = ensureMember(inclusion);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public boolean ensureMember(Inclusion inclusion) 
	{
		Object[] path = inclusion.getPath();
		Object descriptor = inclusion.getDescriptor();
		LoadableEntailment loadable = (LoadableEntailment)descriptor;
		boolean exists = ensureMemberExists(path, loadable);
		return exists;
	}
	
	public boolean ensureMemberExists(Object[] path, LoadableEntailment loadable) {
//		System.out.println("\nEnsuring " + toPathString(path) + " :: " + loadable);
		boolean exists = false;
		int last = path.length-1;
		Object name = path[last];
		Context context = getContext();
		Object container = null;
		if (last>0) {
			Object[] butLast = new Object[last];
			for (int i=0; i<last; i++) { butLast[i] = path[i]; }
			container = context.fetch(butLast, false);
			if (container instanceof Entailment) {
				context = ((Entailment)container).getContext();
			} else {
				context = null;
			}
		}
		if (context != null) {
			Object object = context.get(name);
			exists = (object != null);
			if (!exists) {
				assertDeferredAssertions();
				Inclusion command = newInclusion(path, loadable);
				get_commands().add(command);
				if ((loadable == null) && (container instanceof Herd)){
					loadable = ((Herd)container).getLoadable();
				}
				BoundedExpression newLoad = (BoundedExpression)(loadable.load(context, name));
//				incrementStatistic("Ensure new member loads");
				if (container instanceof Entailment) {
					((Entailment)container).addNewOperand(newLoad);
				}
				assertDeferredAssertions();
				Assertion assertion = newAssertion(path);
				exists = assertAssertion(assertion);
			} 
		}
		return exists;
	}

	public Expression fetchConstraint(Object[] path) {
//		System.out.println("\nFetching constraint at " + toPathString(path));
		Expression expression = null;
		int last = path.length-1;
		Object name = path[last];
		Context context = getContext();
		Object container = null;
		if (last>0) {
			Object[] butLast = new Object[last];
			for (int i=0; i<last; i++) { butLast[i] = path[i]; }
			container = context.fetch(butLast, false);
			if (container instanceof Entailment) {
				context = ((Entailment)container).getContext();
			} else {
				context = null;
			}
		}
		if (context != null){
			Map map = context.getConstraintMap();
			if (map != null){
				expression = (Expression)(map.get(name));
			}
		}
		return expression;
	}

	
//	public Object getMemberAtPath(Object[] path, boolean mayCreate) {
//		Context context = getContext();
//		Object object = context.fetch(path);
//		if ((object == null)  && mayCreate) {
//			List prePath = toList(path);
//			int last = path.length-1;
//			prePath.remove(last);
//			Object container = context.fetch(prePath.toArray());
//			if (container instanceof Herd) {
//				Herd herd = ((Herd)container);
//				object = herd.getMember(path[last]);
//			}
//		}
//		return object;
//	}

	public Object getMemberAtPath(Object[] path, boolean mayCreate) {
		Object object = fetch(path, mayCreate);
		return object;
	}

	public Object get (Object key) {
		Object object = getContext().get(key);
		return object;
	}
	
	public Object[] resolvePath(Object[] path) {
		int size = path.length;
		Object[] resolvedPath = new Object[size];
		for (int i=0; i<size; i++){
			Object element = path[i];
			if (element instanceof Object[]){
				Object[] subscripts = (Object[])element;
				int subSize = subscripts.length;
				Object[] resolvedSubscripts = new Object[subSize];
				for (int j=0; j<subSize; j++){
					Object index = subscripts[j];
					if (index instanceof Object[]){
						index = fetch((Object[])index);
						if (index instanceof Entailment){
							index = ((Entailment)index).getOwnIndexKey();
						}
					}
					resolvedSubscripts[j] = index;
				}
				element = resolvedSubscripts;
			}
			resolvedPath[i] = element;
		}
		return resolvedPath;
	}
	
	public Object fetch(Object[] path, boolean mayCreate){
		Object[] resolvedPath = resolvePath(path);
		Object object = super.fetch(resolvedPath, mayCreate);
		return object;
	}
	
//	public Iterator iterator () {
//		Iterator iterator = (new TreeSet(get_members())).iterator();
//		return iterator;
//	}
	
	public Object[]	getStructureNames () {
		Object[] names = getContext().getStructureNames();
		return names;
	}
	
//	public void resetAllMembers(){
//		Iterator iterator = get_members().iterator();
//		while (iterator.hasNext()){
//			Object member = iterator.next();
//			if (member instanceof Domain) {
//				((Domain)member).reset();
//			}
//		}
//		return;
//	}
	
	public void resetAllMembers(){
		((ContextObject)getContext()).resetAllMembers();
//		incrementStatistic("Reset all members");
		return;
	}

	public boolean restoreBasis(){
		boolean allRestored = true;
		List basis = get_basis();
		int size = basis.size();
		for (int k=0; k<size; k++){
			BoundedExpression expression = (BoundedExpression)(basis.get(k));
			allRestored = reify(expression);
			if (!allRestored) break;
		}
//		incrementStatistic("Restore Basis");
		return allRestored;
	}
	
	public Assertion newAssertion(Object[] path, Object relation, Object value) {
		Assertion assertion = new AssertionObject(path, relation, value);
		return assertion;
	}
	
	public Assertion newAssertion(Object[] path, Object value) {
		Assertion assertion = newAssertion(path, "==", value);
		return assertion;
	}

	public Assertion newAssertion(Object[] path) {
		Assertion assertion = newAssertion(path, Boolean.TRUE);
		return assertion;
	}
		
	public Assertion[] getAssertions() {
		Assertion[] assertions = (Assertion[])(get_assertions().toArray());
		return assertions;
	}
	
	public Inclusion newInclusion(Object[] path, Object descriptor) {
		Inclusion inclusion = new InclusionObject(path, descriptor);
		return inclusion;
	}
	
	public Command[] getCommands() {
		List list = get_commands();
		int size = list.size();
		Command[] commands = new Command[size];
		for (int i=0; i<size; i++){
			commands[i] = (Command)(list.get(i));
		}
		return commands;
	}
	
	public boolean wrappedAddCommands(Command[] commands)
		throws WrappedKBException
	{
		boolean result=false;
		try {
			result = addCommands(commands);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public boolean addCommands(Command[] commands){
		boolean isAdded = true;
		for (int i=0; i<commands.length; i++){
			Command command = commands[i];
			if (command instanceof Assertion){
				isAdded = assertAssertion((Assertion)command);
			} else if (command instanceof Inclusion) {
				isAdded = ensureMember((Inclusion)command);
			} else {	
				error("Unhandled command " + command + " of class " + className(command.getClass()));
			}
			if (!isAdded) break;
		}	
		return isAdded;
	}
	
	// this helper function doesn't actually refer to any KB things
	protected static LoadableConstrainment loadableConstrainment(Assertion assertion) {
		Object[] path = assertion.getPath();
		LoadableGlobal global = new LoadableGlobal(path);
		Object value = assertion.getValue();
		
//		Object[] constantArgs = {Type.objectType(value), value};
		Object[] constantArgs = {value};
		LoadableConstant constant = new LoadableConstant(constantArgs);
		
		Object[] expressionArgs = {assertion.getRelation(), global, constant};
		LoadableExpression expression = new LoadableExpression(expressionArgs);
		
		Object[] constrainmentArgs = {expression, assertion};
		LoadableConstrainment constrainment = new LoadableConstrainment(constrainmentArgs);
		
		return constrainment;
	}

	public boolean reify(BoundedExpression expression){
		boolean isReified = false;
		readAndClearLatch();
		try {
			expression.changeToBe(true);
			isReified = true;
//			incrementStatistic("Reifications");
		} catch(Contradiction c) {
			// don't set Reified
			isReified = false;
		}
		return isReified;
	}
	
	protected boolean addAssertion(Assertion assertion) {
//		System.out.println("\nAdding assertion " + assertion);
		boolean isAdded = false;
		Object[] path = assertion.getPath();
		Object target = fetch(path, false);
		if (target != null) {
			AssertionObject assertionObject = (AssertionObject)assertion;
			BoundedExpression expression = assertionObject.getExpression();
			if (expression == null){
				LoadableConstrainment constraint = loadableConstrainment(assertion);
				expression = (BoundedExpression)(constraint.load(getContext()));
				assertionObject.setExpression(expression);
			}
			isAdded = reify(expression);
			if (isAdded) {
				get_assertions().add(assertion);
				get_basis().add(expression);
			} else {
// 				expression.retract();
			}
		}
		return isAdded;
	}

	protected int findAssertionIndex(Assertion assertion){
		Object[] path = assertion.getPath();
		Object relation = assertion.getRelation();
		int index = -1;
		List assertions = get_assertions();
		int size = assertions.size();
		for (int k=0; k<size; k++){
			AssertionObject assertionK = (AssertionObject)(assertions.get(k));
//			if (assertionK.getRelation().equals(relation) &&
//					assertionK.getPath().equals(path)) {
			if (assertionK.getRelation().equals(relation) &&
					Arrays.equals(assertionK.getPath(), path)) {
				index = k;
				break;
			}
		}
		return index;
	}
	
	protected void removeAssertion(int index) {
		boolean isRemoved = false;
		List assertions = get_assertions();
		if (index >= 0) {
			List basis = get_basis();
			BoundedExpression expression = (BoundedExpression)(basis.get(index));
//			System.out.println("\nRemoving assertion " + expression);
//			expression.retract();
			resetAllMembers();
			basis.remove(expression);
			removeMember(expression);
			// ToDo MLB: clean up; move all this housekeeping into appropriate classes 
			Context context = getContext();
			context.getMembers().remove(expression);
			ContextObject localContext = expression.getLocalContext();
			Map map = localContext.constraintMap();
			Object name = map.get(expression);
			map.remove(expression);
			map.remove(name);
			BoundedExpression story = context.getStory();
			story.getOperands().remove(expression);	
			Object assertion = assertions.get(index);
			((AssertionObject)assertion).setExpression(null);
			assertions.remove(assertion);
			restoreBasis();
//			System.out.println("\nRemoved assertion " + expression);
//			System.out.println(context);
		}
		return;
	}
	
	public boolean wrappedAssertAssertion (Assertion assertion)
		throws WrappedKBException
	{
		boolean result=false;
		try {
			result = assertAssertion (assertion);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public boolean assertAssertion (Assertion assertion) {
		boolean isAsserted = false;
		if (assertion.getValue() == null){
			deassertAssertion(assertion);
		} else {
			isAsserted = assertAssertionInternal(assertion);
		}
		return isAsserted;
	}
	
	public boolean assertAssertionInternal (Assertion assertion) {
		boolean isAsserted = false;
		Assertion oldAssertion = null;
		int index = findAssertionIndex(assertion);
		if (index >= 0) {
			oldAssertion = (Assertion)(get_assertions().get(index));
			isAsserted = oldAssertion.getValue().equals(assertion.getValue());
			if (!isAsserted) {
				removeAssertion(index);
			}
		}
		if (!isAsserted) {
			isAsserted = addAssertion(assertion);
			if (isAsserted){
				Assertion command = 
					newAssertion(assertion.getPath(), assertion.getRelation(), assertion.getValue());
				get_commands().add(command);
			} else {
				handleAssertionConflict(assertion);
				AssertionObject assertionObject = (AssertionObject)assertion;
				BoundedExpression expression = assertionObject.getExpression();
				retractMember(expression);
				assertionObject.setExpression(null);
				if (oldAssertion != null) {
					addAssertion(oldAssertion);
				}
			}
		}
		return isAsserted;
	}
	
	public void handleAssertionConflict(Assertion assertion) {
		Object latchObject = getLatchObject();
		Point point = readAndClearLatch();
		Object support = point;
		if (point != null) {
			if (point instanceof ManagedPoint) {
				PointListener listener = ((ManagedPoint)point).getListener();
				if (listener instanceof Domain) {
					support = ((Domain)listener).getAllSupport();
				}
			}
		}
		ConflictHandler handler = getConflictHandler();
		// Currently we call the handler with the following list contents:
		//  the KB
		//  the conflicting Assertion
		//  the "point" of conflict (aka "decision variable")
		//  the contradiction object that was thrown
		//  the "support" for the point
		//    (either just the point itself or the entire support set)
		if (handler != null) {
			List info = new ArrayList();
			info.add(this);
			info.add(assertion);
			info.add(point);
			info.add(latchObject);
			info.add(support);
			handler.handleConflict(info);
		}
		return;
	}
	
	public boolean wrappedDeassertAssertion (Assertion assertion)
		throws WrappedKBException
	{
		boolean result=false;
		try {
			result = deassertAssertion (assertion);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public boolean deassertAssertion (Assertion assertion) {
		boolean isDeasserted = deassertAssertionInternal(assertion);
		if (isDeasserted) {
			Assertion command = assertion;
			if (command.getValue() != null) {
				command = newAssertion(assertion.getPath(), assertion.getRelation(), null);
			}
			get_commands().add(command);
		}
		return isDeasserted;
	}
	
	public boolean deassertAssertionInternal (Assertion assertion) {
		boolean isDeasserted = false;
		int index = findAssertionIndex(assertion);
		if (index >= 0) {
			Assertion oldAssertion = (Assertion)(get_assertions().get(index));
			Object value = assertion.getValue();
			isDeasserted = ((value == null) || oldAssertion.getValue().equals(value));
			if (isDeasserted) {
				removeAssertion(index);
			}
		}
		return isDeasserted;
	}
	
	public boolean addDeferredAssertion(Assertion assertion) {
//		System.out.println("Adding deferred assertion " + assertion + " ..."); 
		boolean isAdded = get_pending().add(assertion);
		return isAdded;
	}
	
	public void assertDeferredAssertions() {
		List pending = get_pending();
		Object[] array = pending.toArray();
		for (int i=0;i<array.length;i++){
			Assertion assertion = (Assertion)(array[i]);
			boolean isAsserted = assertAssertion(assertion);
//			System.out.println("Deferred Assertion " + assertion + 
//					(isAsserted ?  " succeeded" : " failed"));
		}
		pending.clear();
		return;
	}
	
//	public Object[] getRawChangedVariables() {
//		Set changes = new HashSet();
//		Iterator it = changeIterator();
//		while (it.hasNext()) {
//			Object change = it.next();
//			if (change instanceof ChangeIndex) {
//				PointListener listener = ((ChangeIndex)change).getPoint().getListener();
//				if (listener instanceof BoundedVariable){
//					changes.add(listener);
//				}
//			}
//		}
//		Object[] changedVariables = changes.toArray();
//		return changedVariables;
//	}
	
	public Object[] getRawChangedEntailments() {
		Set changes = new HashSet();
		Iterator it = changeIterator();
		while (it.hasNext()) {
			Object change = it.next();
			if (change instanceof ChangeIndex) {
				PointListener listener = ((ChangeIndex)change).getPoint().getListener();
				if ((listener instanceof Entailment) &&
						!(listener instanceof TopLevelEntailment)){
					changes.add(listener);
				}
			}
		}
		Object[] changedEntailments = changes.toArray();
		return changedEntailments;
	}
	
	public Binding newBinding(Object[] path, Interval interval) {
		Binding binding = new BindingObject(path, interval);
		return binding;
	}
	
//	public Binding[] getChangedVariables() {
//		Object[] rawVariables = getRawChangedVariables();
//		List variables = new ArrayList();
//		for (int i=0; i<rawVariables.length; i++) {
//			BoundedVariable variable = ((BoundedVariable)rawVariables[i]);
//			Object[] path = variable.getVariablePath();
//			if (path != null) {
//				Interval interval = makeVariableInterval(variable);
//				variables.add(newBinding(path, interval));
//			}
//		}
//		int size = variables.size();
//		Binding[] bindings = new Binding[size];
//		for (int i=0; i<size; i++) {
//			bindings[i] = ((Binding)variables.get(i));
//		}
//		return bindings;
//	}
	
	public Binding[] getChangedVariables() {
		List changes = new ArrayList();
		getContext().collectVariableChanges(changes);
		int size = changes.size();
		Binding[] bindings = new Binding[size];
		for (int i=0; i<size; i++) {
			bindings[i] = ((Binding)changes.get(i));
		}
		return bindings;
	}
	
	public void clearChangedVariables() {
		getContext().clearVariableChanges();
		return;
	}
	
	public Binding[] getChangedEntailments() {
		Object[] rawEntailments = getRawChangedEntailments();
		List entailments = new ArrayList();
		for (int i=0; i<rawEntailments.length; i++) {
			Entailment entailment = ((Entailment)rawEntailments[i]);
			Object[] path = entailment.getPath();
			if (path != null) {
				Interval interval = makeVariableInterval(entailment);
				entailments.add(newBinding(path, interval));
			}
		}
		int size = entailments.size();
		Binding[] bindings = new Binding[size];
		for (int i=0; i<size; i++) {
			bindings[i] = ((Binding)entailments.get(i));
		}
		return bindings;
	}
	
	public static List getPathNames(Context context, Object name) {
		ArrayList trail = new ArrayList();
		if (context != null) {
			context.getVariablePath(trail);
		}	
		coerceNames(trail);
		return trail;
	}

	
	public Interval newInterval(Object min, Object max) {
		Interval interval = new IntervalObject(min, max);
		return interval;
	}
	
	protected Interval makeVariableInterval(BoundedExpression variable) {
		Type type =  variable.getType();
		Object min = type.getObject(variable.getMinIndex());
		Object max = type.getObject(variable.getMaxIndex());
		Interval interval = newInterval(min, max);
		return interval;
	}
	
	public Interval wrappedGetVariableInterval(Object[] path)
		throws WrappedKBException
	{
		Interval result=null;
		try {
			result = getVariableInterval(path);
		} catch (KMException exception) {
			throw new WrappedKBException(exception);
		}
		return result;
	}

	public Interval getVariableInterval(Object[] path) {
		Object object = fetch(path, false);
		BoundedExpression variable = ((BoundedExpression)object);
		if (variable == null)
			debugError("Can't make a bounded expression from " + StringUtils.join(path, "."));
		Interval interval = makeVariableInterval(variable);
		return interval;
	}
	
	public KBMember getKBMember(Object[] path) {
		KBMember member = null;
		Object target = fetch(path);
		if (target instanceof KBMember) {
			member = (KBMember)target;
		} else if (target instanceof KBObject) {
			member = ((KBObject)target).getContext();
		}
		return member;
	}
	
	public Object getUserObject(Object[] path) {
		Object object = null;
		KBMember member = getKBMember(path);
		if (member != null) {
			object = member.getUserObject();
		}
		return object;
	}

	public void setUserObject(Object[] path, Object object) {
		KBMember member = getKBMember(path);
		member.setUserObject(object);
		return;
	}

	public Object getAnnotation(Object[] path) {
		Object object = null;
		KBMember member = getKBMember(path);
		if ((member != null) && (member instanceof Variable)) {
			object = ((Variable)member).getAnnotation();
		}
		return object;
	}

	public void setAnnotation(Object[] path, Object object) {
		KBMember member = getKBMember(path);
		if ((member != null) && (member instanceof Variable)) {
			((Variable)member).setAnnotation(object);
		}
		return;
	}
	
	public void commitAll(){
//		incrementStatistic("Commits");		
		super.commitAll();
		return;
	}
	
	private void updateChangeStatistics(){
		setStatistic("Change allocations", getChangeAllocations());
		setStatistic("Change reuses", getChangeReuses());
		setStatistic("Changes created", getChangesCreated());
		setStatistic("Changes pooled", getChangesPooled());
		setStatistic("Change recycles", getChangeRecycles());
		setStatistic("Change pools", getChangePools());		
		return;
	}
	
	public Change getChange (Class changeClass) {
		Change change = super.getChange(changeClass);
		incrementStatistic("Allocated Changes for " + changeClass);
		incrementStatistic("Allocated Changes for all classes");
		incrementStatistic("Net Changes for " + changeClass);
		incrementStatistic("Net Changes for all classes");
		updateChangeStatistics();
		return change;
	}

	public void recycleChange (Change change) {
		super.recycleChange(change);
		Class changeClass = change.getClass();
		incrementStatistic("Recycled Changes for " + changeClass);
		incrementStatistic("Recycled Changes for all classes");
		decrementStatistic("Net Changes for " + changeClass);
		decrementStatistic("Net Changes for all classes");
		updateChangeStatistics();
		return ;
	}

}

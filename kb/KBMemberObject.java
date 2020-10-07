/*
 * Created on Aug 23, 2004
 *
 */
package com.resonant.xkm.kb;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.contexts.ContextObject;
import com.resonant.xkm.exceptions.Contradiction;
import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class KBMemberObject
	extends KMObject
	implements KBMember
{
	private ContextObject	_localContext	= null;
	private KBObject		_KB				= null;
	private boolean			_active			= false;
	private boolean			_wasActive		= false;
	private int				_UID			= 0;
	private boolean			_retracted		= false;
	private Object			_userObject		= null;

	private boolean get_active() {return _active;}
	private void set_active(boolean active) {_active = active;}
	private boolean get_wasActive() {return _wasActive;}
	private void set_wasActive(boolean wasActive) {_wasActive = wasActive;}
	private KBObject get_KB() {return _KB;}
	private void set_KB(KBObject kb) {_KB = kb;}
	private int get_UID() {return _UID;}
	private void set_UID(int uid) {_UID = uid;}
	private boolean get_retracted() {return _retracted;}
	private void set_retracted(boolean retracted) {_retracted = retracted;}
	public ContextObject getLocalContext() {return _localContext;}
	public void setLocalContext(ContextObject context) {_localContext = context;}
	
	public final Object getUserObject() {return _userObject;}
	public final void setUserObject(Object object) {_userObject = object;}
	
	public KBMemberObject() {super();}

	public KBObject getKB() {
		return get_KB();
	}
	
	public void setKB(KBObject kb) {
		KBObject oldKB = get_KB();
		if (kb != oldKB) {
			if (oldKB != null) {
				set_KB(null);
				set_UID(0);
				oldKB.removeMember(this);
//				oldKB.decrementStatistic("Members");
//				oldKB.decrementStatistic(" " + className());
			}
			if (kb != null) {
				set_KB(kb);
				set_UID(kb.nextUID());
				kb.addMember(this);
//				kb.incrementStatistic("Members");
//				kb.incrementStatistic(" " + className());
			}
			if (isActive()) {
				set_active(false);
				activate();
			}
		}
		return;
	}

	public Context getGlobalContext(){
		Context context = getKB().getContext();
		return context;
	}

	public boolean isActive() {
		boolean isActive = (get_active() && !isRetracted() && (getKB() != null));
		return isActive;
	}

	protected boolean wasActive() {
		boolean wasActive = get_wasActive();
		return wasActive;
	}

	public boolean isActivating() {
		boolean isActivating = (!wasActive() && isActive());
		return isActivating;
	}

	public boolean suspend () {
		boolean isActive = isActive();
		set_wasActive(isActive);
		set_active(false);
		if (isActive){
//			System.out.println("\nSuspended " + this);
			KBObject kb = getKB();
			if (kb != null){
//				kb.incrementStatistic("Suspensions");
			}
		}
		return isActive;
	}

	public boolean activate () {
		boolean isActive = isActive();
		boolean isActivating = !isActive;
		set_wasActive(isActive);
		set_active(true);
		if (isActivating) {
//		System.out.println("\nActivating " + this);
			KBObject kb = getKB();
			if (kb != null){
//				kb.incrementStatistic("Activations");
			}
			onActivation();
		}
		set_wasActive(true);
		return isActivating;
	}

	public void onActivation () {
		return;
	}

	public String toString () {
		String string = toStringBody();
		if (!isActive()) {
			string = "'" + string + "'";
		}
		// string = string + "_" + getUID();
		return string;
	}

	public boolean isRetracted() {
		boolean isRetracted = get_retracted();
		return isRetracted;
	}
	
	public void retract () {
// 		System.out.println("\nTop level " + this + ".retract() ...");
 		if (!isRetracted()) {
			Set restoreSet = new HashSet();
			retractThis(restoreSet);
			restore(restoreSet, false);
			set_retracted(true);
		}
//		System.out.println("\n...done: Top level " + this + ".retract()");
		return;
	}
	
	public boolean restore() {
		boolean isRestored = false;
		if (isRetracted()) {
//			System.out.println("\nTop level " + this + ".restore() ...");
			try {
				set_retracted(false);
				activate();
				isRestored = true;
			} catch (Contradiction c) {
				// ToDo MLB: do we need to clean up any changes?
				retract();
				signal(c);
			}
//			System.out.println("\n...done: Top level " + this + ".restore()");
		}
		return isRestored;
	}
	
	protected void restore(Set restoreSet, boolean includeThis) {
		Iterator it = restoreSet.iterator();
		while (it.hasNext()) {
			KBMemberObject object = (KBMemberObject)(it.next());
//			System.out.println("\n...restoring " + this + " " + object);
			if (includeThis || (object != this)) {
				object.activate();
			}
//			System.out.println("\n...restored " + this + " " + object);
		}
		return;
	}

	public boolean retractThis (Set restoreSet) {
		boolean suspended = suspend();
		return suspended;
	}

//	public boolean basisReset(Set restoreSet) {
//		boolean isAdded = false;
//		if (isActive()) {
//			isAdded = restoreSet.add(this);
//			if (isAdded) {
//				suspend();
//			}
//		}
//		return isAdded;
//	}
	
	protected Set getRestoreSet(Set set){
		if (set == null) {
			set = new HashSet();
		}
		return set;
	}
	
	public boolean resetsOnBasisChange(){
		return true;
	}
	
	public Set basisReset(Set restoreSet) {
		if (resetsOnBasisChange()){
			restoreSet = onBasisReset(restoreSet);
		}
		return restoreSet;
	}
	
	public Set onBasisReset(Set restoreSet) {
		boolean isAdded = false;
		if (isActive()) {
			restoreSet = getRestoreSet(restoreSet);
			isAdded = restoreSet.add(this);
			if (isAdded) {
				suspend();
			}
		} 
		if (!isAdded) {
			restoreSet = null;
		}
		return restoreSet;
	}
	
	public boolean basisRestore(Set restoreSet) {
		boolean contains = false;
			if (restoreSet != null){
				contains = restoreSet.contains(this);
				if (contains) {
					activate();
					restoreSet.remove(this);
				}
			}
		return contains;
	}

	public int getUID() {
		int uid = get_UID();
		return uid;
	}

	public int compareTo(Object that) {
		int comparison = 0;
		if (compareMore(that, KBMember.class)) {
				comparison = getUID() - ((KBMember)that).getUID();
		} else {
			throw new ClassCastException(noComparisonString(that));
		}
		return comparison;
	}
	
}

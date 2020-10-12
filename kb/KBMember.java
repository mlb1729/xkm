/*
 * Created on Sep 15, 2004
 *
 
 */
package kb;

import java.util.Set;

import com.resonant.xkm.contexts.Context;
import com.resonant.xkm.contexts.ContextObject;

/**
 * @author MLB
 *
 *
 */
public interface KBMember
{
	KBObject		getKB			();
	void			setKB			(KBObject kb);
	Context			getGlobalContext();
	ContextObject	getLocalContext	();
	void			setLocalContext	(ContextObject context);
	boolean			isActive		();
	boolean			suspend			();
	boolean			activate		();
	void			onActivation	();
	void			retract			();
	boolean			restore			();
	boolean			isRetracted		();
	boolean			retractThis		(Set restoreSet);
	int				getUID			();
	Object			getUserObject	();
	void			setUserObject	(Object object);
}
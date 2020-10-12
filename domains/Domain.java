/*
 * Created on Sep 21, 2004
 *
 
 */
package domains;

import java.util.Set;

import kb.KBMember;
import types.Typed;

/**
 * @author MLB
 *
 *
 */
public interface Domain 
	extends Typed, KBMember
{
	boolean 	addDomainListener		(DomainListener listener);
//	boolean 	removeDomainListener	(DomainListener listener);
	void 		reset					();
	void 		retractSupport			(Object object, Set restoreSet);
	Set			getAllSupport			();
	void		gatherSupport			(Set support, Set visited);
	void		accumulateSupport		(Set support, Set visited);
}

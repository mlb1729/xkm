/*
 * Created on Mar 29, 2005
 *
 
 */
package compiler;

import loader.Loadable;
import reader.Combo;

/**
 * @author MLB
 *
 *
 */
public interface Compiler 
{
	Loadable	compile(Combo combo, Frame frame);
}

/*
 * Created on Mar 29, 2005
 *
 
 */
package com.resonant.xkm.compiler;

import com.resonant.xkm.loader.Loadable;
import com.resonant.xkm.reader.Combo;

/**
 * @author MLB
 *
 *
 */
public interface Compiler 
{
	Loadable	compile(Combo combo, Frame frame);
}

/*
 * Created on Sep 17, 2004
 *
 
 */
package com.resonant.xkm.recycling;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class RecyclableObject 
	extends KMObject 
	implements Recyclable
{
	private Recycler _recycler = null;
	
	private Recycler get_recycler() {return _recycler;}
	private void set_recycler(Recycler recycler) {_recycler = recycler;}
	
	public RecyclableObject() {
		super();
	}

	public Recycler getRecycler() {
		return get_recycler();
	}

	public void setRecycler(Recycler recycler) {
		set_recycler(recycler);
		return;
	}
	
	public void release() {
		return;
	}
	
	public static void recycle(Recyclable recyclable) {
		Recycler recycler = recyclable.getRecycler();
		if (recycler == null) {
			recyclable.release();
		} else {
			recycler.recycle(recyclable);
		}
		return;
	}

}

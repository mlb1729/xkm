/*
 * Created on Sep 7, 2004
 *
 
 */
package com.resonant.xkm.types;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class TypedObject
	extends KMObject
	implements Typed, Externalizable
//	implements Typed
{
	private Type _type;
	
	private Type get_type() {return _type;}
	private void set_type(Type type) {_type = type;}
	
	public TypedObject () {super();}

	public TypedObject (Type type) {
		super();
		set_type(type);
	}

	public Type getType () {
		return get_type();
	}
	
	public Type checkType (Typed that) {
		Type thatType = checkType(this, that);
		return thatType;
	}
		
	public static Type checkType(Typed thing1, Typed thing2) {
		Type type1 = thing1.getType();
		Type type2 = thing2.getType();
		if (type1 != type2) {
			error(type1 + " of " + thing1 + 
					" isn't " + type2 + " of " + thing2);
		}
		return type2;
	}
	
	private static int _level = 0;
	
	private String indent(String string){
		String result = string;
		for (int i = 0; i<_level; i++){
			result = "| " + result;
		}
		return result;
	}
	
	public final void writeExternal(ObjectOutput stream) 
	throws IOException
	{
//		System.out.println(indent("Writing " + className() + " " + hashCode() + "..."));
//		_level++;
		writeExternalInternal(stream);
//		_level--;
//		System.out.println(indent("Wrote " + className() + " " + this + " " + hashCode()));
		return;
	}

	public void writeExternalInternal(ObjectOutput stream) 
	throws IOException
	{
		Type.writeExternalType(stream, getType());
		return;
	}

	public final void readExternal(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
//		System.out.println(indent("Reading " + className() + " " + hashCode() + "..."));	
//		_level++;
		readExternalInternal(stream);
//		_level--;
//		System.out.println(indent("Read " + className() + " " + this + " " + hashCode()));
		return;
	}

	public void readExternalInternal(ObjectInput stream) 
	throws ClassNotFoundException, IOException {
		Type type = Type.readExternalType(stream);
		set_type(type);
		return;
	}
}

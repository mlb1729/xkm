/*
 * Created on Sep 7, 2004
 *
 
 */
package types;




/**
 * @author MLB
 *
 *
 */
public interface Typed
{
	Type	getType		();
	Type	checkType	(Typed that);
	
//	void	writeSerializedType	(ObjectOutputStream stream) throws IOException;
//	void	readSerializedType	(ObjectInputStream stream) throws ClassNotFoundException, IOException; 	
}

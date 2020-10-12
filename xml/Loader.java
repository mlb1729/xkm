/*
 * Created on Dec 8, 2004
 *
 
 */
package xml;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.resonant.util.Utilities;
import com.resonant.xkm.km.KMObject;

/**
 * @author MLB
 *
 *
 */
public class Loader 
	extends KMObject 
{

	public void readTypedFiles(String path, String extension, Map map) 
	{
		try {
			File handle = new File(path);
			if (handle.exists())
			{
				if (handle.isDirectory())
				{
					File files[] = handle.listFiles();
					for (int cc = 0; cc < files.length; cc++)
					{
						readTypedFiles(files[cc].getAbsolutePath(), extension, map);
					}
				} else { 
					String fileName = handle.getName();
					if (fileName.endsWith(extension))
					{
						String contents = Utilities.ReadFile(handle.getAbsolutePath());				
						String name = fileName.substring(0,
								fileName.length() - extension.length());
						map.put(name, contents);
					}
				}
			}
		} catch (IOException ex) {
			error("Unable to load file " + path + ": " + ex);
		}
	}
	
	public void readXMLFiles(String path, Map map) 
	{
		readTypedFiles(path, ".xml", map);
		return;
	}
	
}

package nikoBlex.tools.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderManager{
	public static ClassLoader getClassLoader()
	{
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static URLClassLoader getURLClassLoader(String path) throws MalformedURLException
	{		
		File dir=new File(path);
		URL[] urls={dir.toURI().toURL()};
		return new URLClassLoader(urls);		
	}

}

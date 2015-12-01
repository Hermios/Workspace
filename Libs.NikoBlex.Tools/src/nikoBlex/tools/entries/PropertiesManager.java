package nikoBlex.tools.entries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;




import nikoBlex.tools.utils.GenericFunctions;
import nikoBlex.tools.utils.PathConstructor;


public class PropertiesManager {
	private static PropertiesManager _instance;
	private final static String ERROR_PROPERTIES_UNAVAILABLE="Properties were not defined";
	private static String PROPERTIES_DIR="Properties";
	private static String APPLICATION_DIR="Application";
	private static String RESOURCEBUNDLES_DIR="ResourceBundles";
	private static String PROPERTY_EXTENSION="properties";
	private List<File> _listResourceBundlesFiles=new ArrayList<>();
	private Properties _allProperties=new Properties();
	private String _resourceBundlePath;
	
	private void addProperties(String contextRoot) throws Exception
	{
		File rootFile=new File(contextRoot);
		loadPropertiesFromJar(getClass());
		loadPropertiesFromDir(rootFile.getAbsolutePath(),false);
		String root=rootFile.getParentFile().getParentFile().getAbsolutePath();
		loadPropertiesFromDir(PathConstructor.getPath(root,PROPERTIES_DIR,APPLICATION_DIR),true);
		loadResourceBundlesFromDir(root);		
	}
	
	public static void loadProperties(String root) throws Exception
	{		
		if(_instance==null)
		_instance=new PropertiesManager();
		_instance.addProperties(root);
	}
	
	private void loadPropertiesFromJar(Class<?> currentClass) throws Exception
	{
		for(JarFile jarFile:GenericFunctions.getJarFiles(currentClass))
		{
			for(JarEntry jarEntry:GenericFunctions.getJarEntriesFromFile(jarFile, PROPERTY_EXTENSION))
			{
				System.out.println("load "+jarEntry.getName());
				loadPropertiesFromStream(jarFile.getInputStream(jarEntry),false);
			}
		}
	}
	
	private void loadPropertiesFromDir(String root,boolean override) throws Exception
	{
		for(File file:GenericFunctions.getFiles(root, PROPERTY_EXTENSION))
		{
			System.out.println("load "+file.getName());
			loadPropertiesFromStream(new FileInputStream(file),override);	
		}
		
	}	
	
	private void loadResourceBundlesFromDir(String root)throws Exception
	{		
		_resourceBundlePath=PathConstructor.getPath(root,PROPERTIES_DIR,RESOURCEBUNDLES_DIR);
		_listResourceBundlesFiles=GenericFunctions.getFiles(_resourceBundlePath, PROPERTY_EXTENSION);
	}
		
	private void loadPropertiesFromStream(InputStream fis,boolean override) throws IOException
	{
		Properties result=new Properties();				
		result.load(fis);		
		fis.close();
		Enumeration<Object> enumKeys=result.keys();
		while(enumKeys.hasMoreElements())
		{
			String currentElement=enumKeys.nextElement().toString();
			if(override||!_allProperties.containsKey(currentElement))
			_allProperties.setProperty(currentElement, result.getProperty(currentElement));
		}
	}
	
	public static Collection<File> getAllResourceBundles() throws Exception
	{
		if(_instance==null)
			throw new Exception(ERROR_PROPERTIES_UNAVAILABLE);
		return _instance._listResourceBundlesFiles;
	}	
			
	public static String getString(EnumPropertieKeys key)
	{
		return _instance==null?"":_instance._allProperties.getProperty(key.getKey());
	}
	
	public static String getString(EnumPropertieKeys key,String addData)
	{
		return _instance==null?"":_instance._allProperties.getProperty(key.getKey()+"."+addData);
	}
	
	public static boolean getBoolean(EnumPropertieKeys key)
	{
		return getString(key).equals("true");
	}
	
	public static int getInteger(EnumPropertieKeys key)
	{
		return Integer.parseInt(getString(key));
	}
		
	public static String getPropertiyValue(String key)
	{
		return _instance==null?null:_instance._allProperties.getProperty(key);		
	}

	public static List<String> getList(EnumPropertieKeys key,String separator)
	{
		return Arrays.asList(getString(key).split(separator));
	}
	
	public static Properties getAllProperties()  throws Exception
	{
		if(_instance==null)
			throw new Exception(ERROR_PROPERTIES_UNAVAILABLE);
		return _instance._allProperties;
	}	
	
	public static void clear()
	{
		if(_instance==null)
			return;
		_instance._allProperties.clear();
		_instance=null;
	}
	
	public static String getResourceBundlesPath() throws Exception
	{
		if(_instance==null)
			throw new Exception(ERROR_PROPERTIES_UNAVAILABLE);
		return _instance._resourceBundlePath;
	}	
}

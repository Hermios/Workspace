package nikoBlex.resources.tools;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.serviceLocators.IServiceLocator;
import nikoBlex.tools.utils.ClassLoaderManager;
import nikoBlex.tools.utils.GenericFunctions;

public class ResourceBundleManager implements IServiceLocator {
	URLClassLoader _resourceBundleCl;
	public String get(String key,Locale locale)
	{
		key=key.toLowerCase();
		String fileName=GenericFunctions.setFirstUpperCase(key.split("\\.")[0]);
		ResourceBundle resourceBundle=ResourceBundle.getBundle(fileName,locale,_resourceBundleCl);
		try
		{
			return (null==resourceBundle)?key:resourceBundle.getString(key);
		}
		catch(MissingResourceException mse)
		{
			return key;
		}
	}
	
	public void loadResourceBundleClass()
	{
		try
		{				
			String resourceBundleDir= PropertiesManager.getResourceBundlesPath();
			_resourceBundleCl=ClassLoaderManager.getURLClassLoader(resourceBundleDir);						
		}		
		catch(MissingResourceException mse)
		{
			
		}
		catch(MalformedURLException mfe){}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void initServiceLocator(ClassesManager cm, ServletContext context)
			throws Exception {
		loadResourceBundleClass();
	}

	@Override
	public void endServiceLocator() {
		
		
	}	

}

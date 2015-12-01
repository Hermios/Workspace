package nikoBlex.tools.entries;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;

import nikoBlex.tools.utils.GenericFunctions;


public class ClassesManager{
	private static String CLASS_DIRECTORY="classes";
	private static String CLASS_EXTENSION="class";
	private List<Class<?>> _listClasses=new ArrayList<Class<?>>();	
	
	
	public ClassesManager(String root,ClassLoader loader) throws Exception
	{
		addFrom(getClass());
		addFrom(root,loader);
	}	
	
	private void addFrom(Class<?> currentClass) throws Exception
	{
		for(JarEntry jarEntry:GenericFunctions.getEntries(currentClass, CLASS_EXTENSION))
			addClass(jarEntry.getName(),currentClass.getClassLoader());
	}
	
	private void addFrom(String root,ClassLoader loader) throws Exception
	{		
		for(File file:GenericFunctions.getFiles(root,CLASS_EXTENSION))
			addClass(file.getAbsolutePath().split(CLASS_DIRECTORY)[1],loader);		
	}
	
	private void addClass(String fileName,ClassLoader loader) throws ClassNotFoundException 
	{
		if(fileName.contains("$"))
			return;
		String className=fileName.replace(File.separator, ".");
		className=className.replace('/', '.');
		if(className.startsWith("."))
				className=className.substring(1);
		className=className.replace("."+CLASS_EXTENSION,"");
		Class<?> foundClass=Class.forName(className,false,loader);
 	   	if(!_listClasses.contains(foundClass))
 	   		_listClasses.add (foundClass); 	   	
	}
			
	public <T> List<T> getClasses(T filter)
	{		
		return getClasses(_listClasses, filter);
	}
	
	public List<Class<?>> getAllClasses()
	{
		return _listClasses;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getClasses(List<Class<?>> listClasses,T filter)
	{
		Iterator<Class<?>> classesIterator=listClasses.iterator();
		List<T> result=new ArrayList<T>();
		while(classesIterator.hasNext())
		{					
			Class<?> currentClass=classesIterator.next();
			if(currentClass.isInterface() || Modifier.isAbstract(currentClass.getModifiers()))
				continue;
			if(hasInterface(currentClass, filter)) 				
					result.add((T)currentClass);			
		}
		return result;
	}
	
	private static <T> boolean hasInterface(Class<?> currentClass,T filter)
	{
		boolean result=false;
		if(null==currentClass)
			return false;
		if(currentClass==filter)
			return true;
		Class<?>[] interfaces = currentClass.getInterfaces();
		int i=0;		
		while(!result && i<interfaces.length)
		{
			result=hasInterface(interfaces[i], filter);			
			i++;
		}
		if(!result && !currentClass.isInterface())
			result=hasInterface(currentClass.getSuperclass(), filter);
		return result;				
	}	
	
}

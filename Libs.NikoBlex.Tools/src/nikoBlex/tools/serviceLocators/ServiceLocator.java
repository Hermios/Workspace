package nikoBlex.tools.serviceLocators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;

public class ServiceLocator {
	private List<IServiceLocator> _listServices=new ArrayList<IServiceLocator>();
	
	public ServiceLocator(boolean stopOnError,ClassesManager cm,ServletContext context) throws Exception
	{		
		System.out.println("Initialize Services");		
		for(Class<IServiceLocator> serviceClass:cm.getClasses(IServiceLocator.class))
		{
			System.out.println("Initialize "+serviceClass.getSimpleName());
			try
			{
				IServiceLocator service=serviceClass.newInstance();
				service.initServiceLocator(cm,context);
				if(addService(service))
					System.out.println(serviceClass.getSimpleName()+" initialized");
			}
			catch(Exception e)
			{
				System.out.println("ERROR WHILE INITIALIZING "+serviceClass.getName());
				e.printStackTrace();
				if(stopOnError)
				{
					java.lang.System.exit(1); 
					return;
				}							
			}
		}
	}
			
	private boolean addService(IServiceLocator service)
	{		
		if(getService(service.getClass())!=null)
		{
			System.out.println("INFO : Service "+service.getClass().getSimpleName()+" was already added");
			return false;
		}
		_listServices.add(service);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IServiceLocator> T getService(Class<T> serviceLocationClass)
	{		
		Iterator<IServiceLocator> iterator=_listServices.iterator();
		T result=null;
		while(result==null && iterator.hasNext())
		{
			IServiceLocator currentItem=iterator.next();			
			if(currentItem.getClass()==serviceLocationClass)
				result=(T)currentItem;
		}
		return result;		
	}
	
	public List<IServiceLocator> getAllServices()
	{
		return _listServices;
	}			
}

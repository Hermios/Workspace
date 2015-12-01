package nikoBlex.servlet;

import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import nikoBlex.businessmodels.database.GraphHandler;
import nikoBlex.requests.keys.ServletContextPropertyKeys;
import nikoBlex.requests.tools.ContextManager;
import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.serviceLocators.IServiceLocator;
import nikoBlex.tools.serviceLocators.ServiceLocator;

public abstract class ApplicationServletContextListener implements ServletContextListener{
		protected ClassesManager _cm=null;
		protected ContextManager _contextManager;
		
		public void contextInitialized(ServletContextEvent servletContextEvent) {
			_contextManager=new ContextManager(servletContextEvent.getServletContext());
			System.out.println("Starting context "+_contextManager.getName());
			String root=_contextManager.getRoot();		
			
			System.out.println("Load properties");
			try {				
				PropertiesManager.loadProperties(root);
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("ERROR WHILE LOADING PROPERTIES");
				return;
			}
			System.out.println("Properties loaded");
					
			boolean stopOnError=PropertiesManager.getBoolean(EnumPropertieKeys.SERVER_INTERRUPT);
			
			System.out.println("Load classes");
			try {
				_cm=new ClassesManager(root,getClass().getClassLoader());
				System.out.println("Classes loaded");
			} catch (Exception e1) {
				System.out.println("ERROR WHILE Loading classes");
				e1.printStackTrace();
				if(stopOnError)
				{
					java.lang.System.exit(1); 
					return;
				}						
			}
			
			System.out.println("Initialize ServiceLocator");
			try {
				_contextManager.setAttribute(ServletContextPropertyKeys.serviceLocator,new ServiceLocator(stopOnError,_cm,servletContextEvent.getServletContext()));
				System.out.println("ServiceLocator initialized");
			} catch (Exception e) {
				System.out.println("ERROR WHILE INITIALIZING ServiceLocator");
				e.printStackTrace();
				if(stopOnError)
				{
					java.lang.System.exit(1); 
					return;
				}						
			}
			
			System.out.println("Connect to database");
			GraphHandler.connectToDatabase();					
						
			InitializeTasksCaller();
			
			_contextManager.setAttribute(ServletContextPropertyKeys.classesManager, _cm.getAllClasses());
			System.out.println(_contextManager.getName()+" context started");
		}
		
		public void contextDestroyed(ServletContextEvent servletContextEvent) {
			System.out.println("End servicelocator");			
			ServiceLocator sl=(ServiceLocator)_contextManager.getAttribute(ServletContextPropertyKeys.serviceLocator);
			for(IServiceLocator serviceLocator:sl.getAllServices())
			{
				serviceLocator.endServiceLocator();
				System.out.println(serviceLocator.getClass().getName() + " is closed");
			}
			System.out.println("Servicelocator ended");
			
			System.out.println("Clear ressources");
			ResourceBundle.clearCache();		
			System.out.println("ressources cleared");

			System.out.println("Clear Singletons");
			GraphHandler.disconnectFromDatabase();
			PropertiesManager.clear();
			System.out.println("Singletons cleared");
		}
		
		protected abstract void InitializeTasksCaller();

}

package nikoBlex.tools.serviceLocators;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;

public interface IServiceLocator {
	void initServiceLocator(ClassesManager cm,ServletContext context) throws Exception;
	void endServiceLocator();	
}

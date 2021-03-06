package nikoBlex.requests.get;

import java.util.HashMap;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.serviceLocators.IServiceLocator;

public class GetDataHelpersManager implements IServiceLocator {
	private HashMap<String,Class<? extends IGetter>> _mapDynamicVelocity=new HashMap<>();
	
	@Override
	public void initServiceLocator(ClassesManager cm, ServletContext context)
			throws Exception {
		for(Class<? extends IGetter> currentClass:cm.getClasses(IGetter.class))
			_mapDynamicVelocity.put(currentClass.getSimpleName(), currentClass);
	}

	public HashMap<String,Class<? extends IGetter>> getListRequestGetters()
	{
		return _mapDynamicVelocity;
	}
	
	public Class<? extends IGetter> get(String key)
	{
		return _mapDynamicVelocity.get(key);
	}
	
	@Override
	public void endServiceLocator() {
		
	}

}

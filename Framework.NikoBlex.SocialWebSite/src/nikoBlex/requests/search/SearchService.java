package nikoBlex.requests.search;

import java.util.List;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.serviceLocators.IServiceLocator;

public class SearchService implements IServiceLocator{
	private List<Class<ISearch>> _listSearchModules;
	
	@Override
	public void initServiceLocator(ClassesManager cm, ServletContext context)
			throws Exception {
		_listSearchModules=cm.getClasses(ISearch.class);
		
	}	

	@Override
	public void endServiceLocator() {				
	}

	public List<Class<ISearch>> getListSearchModules()
	{
		return _listSearchModules;
	}
	
}

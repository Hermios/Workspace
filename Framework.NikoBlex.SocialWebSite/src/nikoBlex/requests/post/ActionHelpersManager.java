package nikoBlex.requests.post;

import java.util.HashMap;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.serviceLocators.IServiceLocator;

public class ActionHelpersManager implements IServiceLocator{
		HashMap<String, Class<? extends ISetter>> _mapActionHelpers=new HashMap<>();
		
		public Class<? extends ISetter> getActionHelper(String key)
		{
			return _mapActionHelpers.get(key);
		}

		@Override
		public void endServiceLocator() {
		}

		@Override
		public void initServiceLocator(ClassesManager cm,ServletContext context) throws Exception {
			for(Class<? extends ISetter> actionHelper:cm.getClasses(ISetter.class))
				_mapActionHelpers.put(actionHelper.getSimpleName(), actionHelper);			
		}
		
		public HashMap<String,Class<? extends ISetter>> getListActions()
		{
			return _mapActionHelpers;
		}
}

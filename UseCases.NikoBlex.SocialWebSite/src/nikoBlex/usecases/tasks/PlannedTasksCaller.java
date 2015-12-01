package nikoBlex.usecases.tasks;

import nikoBlex.requests.tools.ContextManager;
import nikoBlex.tools.entries.ClassesManager;
import nikoBlex.tools.entries.PropertiesManager;

public class PlannedTasksCaller {
	
	public static void callStartupTasks(ClassesManager cm,ContextManager contextManager)
	{
		System.out.println("Call startup tasks");
		for(Class<? extends IStartupTask> startupTaskClass:cm.getClasses(IStartupTask.class))
		{
			try {
				IStartupTask startupTask=startupTaskClass.newInstance();
				if(!PropertiesManager.getBoolean(startupTask.conditioningStartupTask()))
					continue;
				System.out.println("Startup "+startupTaskClass.getName());	
				startupTask.startupTaskAction(contextManager);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}

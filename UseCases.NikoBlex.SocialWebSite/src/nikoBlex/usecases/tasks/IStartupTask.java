package nikoBlex.usecases.tasks;


import nikoBlex.requests.tools.ContextManager;
import nikoBlex.tools.entries.EnumPropertieKeys;

public interface IStartupTask {
	void startupTaskAction(ContextManager contextManager);
	
	EnumPropertieKeys conditioningStartupTask();
}

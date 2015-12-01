package nikoBlex.businessmodels.database;

public class GraphHandler {
	public static void connectToDatabase()
	{
		GraphDatabase.getInstance();		
	}
	
	public static void disconnectFromDatabase()
	{
		GraphDatabase.disconnect();
	}
	
	public static String sendCommand(String command)
	{
		return GraphDatabase.sendCommand(command);
	}
}

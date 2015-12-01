package main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mamshops.tools.utils.GenericFunctions;

public class Main {

	public static void main(String[] args) {
		if(args.length<1)
		{
			System.err.println("not enough arguments");
			return;
		}
		
		//Get directories
		File tomcatRoot=new File(args[0]);
		File logsDir=new File(tomcatRoot,"logs");
		File graphDir=new File(tomcatRoot,"database");
		graphDir=new File(graphDir,"GraphDB");
		
		String os=System.getProperty("os.name").toLowerCase();
		String genericCommand=tomcatRoot.getAbsolutePath()+(os.indexOf("win") >= 0?"\\bin\\%s.bat":"/bin/%s.sh");
		//Stop server
		try 
		{
			Process proc = Runtime.getRuntime().exec(String.format(genericCommand,new Object[]{"shutdown"}));
			proc.waitFor();
		}
			catch (IOException|InterruptedException e) 
		{			
			e.printStackTrace();
			return;
		}
		
		//create new directory
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd");
		File currentArchiv=new File(tomcatRoot,"archiv");
		currentArchiv=new File(currentArchiv,sdf.format(Calendar.getInstance().getTime()));
		currentArchiv.mkdirs();
		File currentArchivLogs=new File(currentArchiv,"logs");
		File currentArchivDb=new File(currentArchiv,"GraphDB");
		
		//Copy data
		try 
		{
			GenericFunctions.copyFolder(logsDir, currentArchivLogs);
			GenericFunctions.copyFolder(graphDir, currentArchivDb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Remove data
		GenericFunctions.deleteFolderContent(logsDir, false);
		GenericFunctions.deleteFolderContent(new File(tomcatRoot,"work"),true);
		
		//Restart server
		try {
			Runtime.getRuntime().exec(String.format(genericCommand,new Object[]{"startup"}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

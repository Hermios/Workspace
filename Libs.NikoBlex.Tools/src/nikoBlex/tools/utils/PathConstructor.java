package nikoBlex.tools.utils;

import java.io.File;

public class PathConstructor {
	private static String getPath(String separator,String...paths)
	{
		StringBuilder sb=new StringBuilder();
		String result;
		if(paths.length==0)
			return "";
		if(paths.length==1)
			result=paths[0];
		else
		{
			for(int i=1;i<paths.length;i++)
			{
				sb.append(paths[i-1]);
				if(!paths[i-1].endsWith(separator) && !paths[i].startsWith(separator))
					sb.append(separator);
			}
			sb.append(paths[paths.length-1]);
			result=sb.toString();			
		}
		return result;
	}
	
	public static String getURL(String...paths)
	{		
		return PathConstructor.getPath("/",paths);			
	}
	
	public static String getPath(String...paths)
	{
		return getPath(File.separator,paths);
	}
	
	public static String getLastElement(String path,char separator)
	{
		return path.substring(path.lastIndexOf(separator)+1,path.length());
	}
}

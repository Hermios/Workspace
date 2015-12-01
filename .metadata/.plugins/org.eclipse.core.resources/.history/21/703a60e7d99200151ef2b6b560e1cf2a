package nikoBlex.requests.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestGenericTools {

	public static void writeDataInResponse(String filePath,HttpServletResponse response) throws IOException
	{			
		filePath=filePath.replace("/", File.separator);
		File file=new File(filePath);
		if(!file.exists())
			throw new FileNotFoundException(filePath);
	    response.setContentLength((int) file.length());
	    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

	    BufferedInputStream input = null;
	    BufferedOutputStream output = null;

	    try {
	        input = new BufferedInputStream(new FileInputStream(file));
	        output = new BufferedOutputStream(response.getOutputStream());

	        byte[] buffer = new byte[(int)file.length()];
	        int length;
	        while ((length = input.read(buffer)) > 0) {
	            output.write(buffer, 0, length);
	        }
	       
	    } finally {
	        if (output != null) try { output.close(); } catch (IOException ignore) {}
	        if (input != null) try { input.close(); } catch (IOException ignore) {}
	    }
	}	
	
	public static String getTarget(HttpServletRequest request)
	{
		String result=request.getRequestURI();
		result=result.substring(request.getContextPath().length());
		while(result.startsWith("/"))
			result=result.substring(1);
		return result;
	}	

	public static String getCookieData(HttpServletRequest request,String key)
	{
		Cookie[] listCookies=request.getCookies();
		boolean cookieFound=false;
		int i=0;
		while(!cookieFound && i<listCookies.length)
		{
			cookieFound=listCookies[i].getName().equals(key);
			i++;
		}
		return cookieFound?	listCookies[i-1].getValue():null;			
		
	}	
		
}

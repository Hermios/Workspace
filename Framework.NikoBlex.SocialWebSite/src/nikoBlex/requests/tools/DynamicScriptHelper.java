package nikoBlex.requests.tools;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nikoBlex.requests.get.VelocityManager;
import nikoBlex.requests.keys.ServletContextPropertyKeys;
import nikoBlex.resources.tools.ResourceTools;
import nikoBlex.tools.serviceLocators.ServiceLocator;
import nikoBlex.tools.utils.PathConstructor;

public class DynamicScriptHelper{		
	
	public static void doGet(HttpServletRequest request, HttpServletResponse response,int userStatusMask){
		String target=RequestGenericTools.getTarget(request);		
		target=PathConstructor.getPath(ResourceTools.getStaticResourceRoot(request.getRequestURI()),target);
		ServiceLocator sl=(ServiceLocator)new ContextManager(request.getServletContext()).getAttribute(ServletContextPropertyKeys.serviceLocator);
		VelocityManager vm=sl.getService(VelocityManager.class);		 		 				 
		PrintWriter pw=null;		
		try 
		{
			pw = response.getWriter();		
			try
			{
				vm.generateCode(target,request, pw,userStatusMask,null);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			pw.println();
		} 
		catch (IOException e) 	
		{
			e.printStackTrace();
		}
	}	
}

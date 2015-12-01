package nikoBlex.requests.tools;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nikoBlex.requests.get.VelocityManager;
import nikoBlex.requests.keys.ServletContextPropertyKeys;
import nikoBlex.requests.keys.SessionAttributeKeys;

import org.apache.commons.lang.NullArgumentException;

import nikoBlex.resources.tools.ResourceBundleManager;
import nikoBlex.resources.tools.ResourceTools;
import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.serviceLocators.ServiceLocator;
import nikoBlex.tools.utils.MailManager;
import nikoBlex.tools.utils.PathConstructor;

public class MailSender{
	private static final String START_MAILS_SUBJECT="mail.header.";
	
	private HttpSession _session;
	private HttpServletRequest _request;
	
	public MailSender(HttpServletRequest request) 
	{
		_request=request;
		_session=request.getSession();
	}
		
	public void sendMailToUser(String templateName,HashMap<String,Object> addedProperties,String email,int authorizationLevel) throws Exception
	{
		Long userId=(Long)_session.getAttribute(SessionAttributeKeys.currentUser.toString());
		if(userId==null)
			throw new NullArgumentException("userId");
		sendMail(templateName, new String[]{email},addedProperties,authorizationLevel);
	}
	
	public void sendMailToMams(String templateName,MamsDestination mamsDestination,HashMap<String,Object> addedProperties,int authorizationLevel) throws Exception
	{
		sendMail(templateName, new String[]{PropertiesManager.getString(EnumPropertieKeys.MAMS_MAIL_START,mamsDestination.toString())},addedProperties,authorizationLevel);
	}
	
	public void sendMail(String templateName,String destinations[],HashMap<String,Object> addedProperties,int authorizationLevel) throws Exception
	{
		String target=PathConstructor.getPath(ResourceTools.getStaticResourceRoot(templateName),templateName);
		ServiceLocator sl=(ServiceLocator)new ContextManager(_request.getServletContext()).getAttribute(ServletContextPropertyKeys.serviceLocator);
		VelocityManager vm=sl.getService(VelocityManager.class);
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		PrintWriter pw=new PrintWriter(baos,true);		
		if(addedProperties==null)
			addedProperties=new HashMap<>();
		addedProperties.put("email", destinations);
		vm.generateCode(target,_request, pw,authorizationLevel,addedProperties);
		pw.println();
		ResourceBundleManager rbm=sl.getService(ResourceBundleManager.class);
		String subject=rbm.get(START_MAILS_SUBJECT+templateName.split("\\.")[0], _request.getLocale());
		MailManager.sendMail(subject, baos.toString(), destinations,
				PropertiesManager.getString(EnumPropertieKeys.MAMS_MAIL_START,MamsDestination.postmaster.toString()),
				PropertiesManager.getString(EnumPropertieKeys.SMTP_PASSWORD),
				PropertiesManager.getString(EnumPropertieKeys.SMTP_NAME));
	}
	
	public enum MamsDestination
	{
		admin,
		all,
		design,
		grafic,		
		postmaster,
		webmaster
	}
}

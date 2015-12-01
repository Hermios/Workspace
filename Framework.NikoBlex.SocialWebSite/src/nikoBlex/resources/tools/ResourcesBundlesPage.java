package nikoBlex.resources.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import nikoBlex.requests.get.IGetter;
import nikoBlex.requests.getset.AbstractGetSet;
import nikoBlex.requests.keys.ServletContextPropertyKeys;
import nikoBlex.requests.keys.SessionAttributeKeys;
import nikoBlex.requests.tools.ContextManager;
import nikoBlex.requests.tools.AbstractRequestUri;
import nikoBlex.tools.serviceLocators.ServiceLocator;

public class ResourcesBundlesPage extends AbstractGetSet implements IGetter{	
	
	private ResourceBundleManager _resourceBundleManager;
	private boolean _showAdminInfo;
	
	public ResourcesBundlesPage()
	{
		super(null, null);
	}
	
	public String get()
	{
		return get(_parameter);
	}
	
//	public String get(String key,String... parameters)
//	{				
//		String result=_resourceBundleManager.get(key, _request.getLocale());
//		if(!result.equals(key) && _showAdminInfo)
//			result+="{"+key+"}";
//		return result;		
//	}
	
	public boolean showInfo()
	{
		return _showAdminInfo;
	}
	
	public String get(String key,Object... parameters)
	{
		String  result="";
		result=_resourceBundleManager.get(key, _request.getLocale());
		if(!result.equals(key) && _showAdminInfo)
			result+="{"+key+"}";
		Pattern p=Pattern.compile("<(\\d+)>");
		Matcher m=p.matcher(result);
		while(m.find())
			result=result.replace(m.group(0), parameters[Integer.parseInt(m.group(1))].toString());
		if(!_showAdminInfo)
			return result;
		
		for(int i=0;i<parameters.length;i++)
			if(!result.contains(parameters[i].toString()))
				result+=",{"+i+":"+parameters[i]+"}";
		return result;
	}
	
	@Override
	public void initialize(HttpServletRequest request){	
		AbstractRequestUri ru=construct(AbstractRequestUri.class);
		Boolean sessionShowInfo=(Boolean)_session.getAttribute(SessionAttributeKeys.showAdminInfoResourceBundle.toString());
		if (sessionShowInfo==null)
			sessionShowInfo=false;
		_showAdminInfo=ru.isAdmin() && sessionShowInfo; 
		ServiceLocator sl=(ServiceLocator)new ContextManager(_request.getServletContext()).getAttribute(ServletContextPropertyKeys.serviceLocator);
		_resourceBundleManager=sl.getService(ResourceBundleManager.class);
		
	}
}

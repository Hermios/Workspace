package nikoBlex.requests.authorizationlevels;

import javax.servlet.http.HttpServletRequest;

import nikoBlex.requests.get.IGetter;
import nikoBlex.requests.getset.AbstractGetSet;
import nikoBlex.tools.entries.PropertiesManager;

public class RequestAdminUri extends AbstractGetSet implements IGetter{
		
	public RequestAdminUri(IUserAuthorization userAuthorization) {
		super(userAuthorization, null);
	}

	public String getProperty()
	{
		String result= getProperty(_parameter);
		return result!=null?result:"doesn't exists";
	}
	
	public String getProperty(String key)
	{
		return PropertiesManager.getPropertiyValue(key);
	}

	@Override
	public void initialize(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
}

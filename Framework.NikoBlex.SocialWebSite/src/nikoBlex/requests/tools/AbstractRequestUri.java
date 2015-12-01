package nikoBlex.requests.tools;

import javax.servlet.http.HttpServletRequest;

import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.get.IGetter;
import nikoBlex.requests.getset.AbstractGetSet;
import nikoBlex.requests.keys.SessionAttributeKeys;
import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.utils.PathConstructor;

public abstract class AbstractRequestUri extends AbstractGetSet implements IGetter {
	String _contextPath;
	String _protocol;
	public AbstractRequestUri(IUserAuthorization authorizationLevel,SessionAttributeKeys sessionAttributeKey) {
		super(authorizationLevel, sessionAttributeKey);
	}

	public String get(String ressourceName)
	{
		return PathConstructor.getURL(_contextPath,ressourceName);
	}
	
	public String getLong(String ressourceName)
	{
		return PathConstructor.getURL(_protocol+PropertiesManager.getString(EnumPropertieKeys.SERVER_DNS),ressourceName);
	}
	
	public abstract boolean isAdmin();
	
	@Override
	public void initialize(HttpServletRequest request){
		_protocol=request.isSecure()?"https":"http";
		_protocol+="://";
		_contextPath=request.getServletContext().getContextPath();
	}
}

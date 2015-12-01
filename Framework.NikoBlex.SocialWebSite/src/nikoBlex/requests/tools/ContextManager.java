package nikoBlex.requests.tools;

import javax.servlet.ServletContext;

import nikoBlex.requests.keys.ServletContextPropertyKeys;

public class ContextManager {
	private ServletContext _servletContext;
	
	public ContextManager(ServletContext servletContext)
	{
		_servletContext=servletContext;
	}
		
	public String getPath()
	{
		return _servletContext.getContextPath();
	}
	
	public String getName()
	{
		return _servletContext.getServletContextName();
	}
	
	public Object getAttribute(ServletContextPropertyKeys key)
	{
		return _servletContext.getAttribute(key.toString());
	}

	public String getRoot()
	{
		return _servletContext.getRealPath("");
	}
	
	public void setAttribute(ServletContextPropertyKeys key,Object value)
	{
		_servletContext.setAttribute(key.toString(), value);
	}
}

package nikoBlex.requests.getset;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nikoBlex.businessmodels.database.GraphNode;
import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.getset.AbstractNodeGetSet;
import nikoBlex.requests.keys.SessionAttributeKeys;

public abstract class AbstractNodeGetSet<NodeLabel extends Enum<NodeLabel>> extends AbstractGetSet{
	protected HttpSession _session;
	protected HttpServletRequest _request;
	protected ServletContext _servletContext;
	protected GraphNode _currentNode;	
	protected String[] _parameters;	
	protected String _parameter;
	protected String _currentValue;
	protected INodePropertyKey _refKey;
	protected NodeLabel _nodeLabel;
	
	protected AbstractNodeGetSet(NodeLabel nl,IUserAuthorization methodAccess,INodePropertyKey refKey,SessionAttributeKeys sessionAttributeKey)
	{
		super(methodAccess,sessionAttributeKey);
		_nodeLabel=nl;
		_refKey=refKey;
	}
	
	public Long getId()
	{
		return _currentNode!=null?_currentNode.getId():-1;
	}
	
	public <T extends AbstractNodeGetSet<NodeLabel>> T construct(Class<T> getset,GraphNode node)
	{
		T instance;
		try {
			instance = getset.newInstance();
			instance.initialize(_request);
			instance.setNode(node);			
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static <NodeLabel extends Enum<NodeLabel>,T extends AbstractNodeGetSet<NodeLabel>> T constructWithNode(Class<T> getset,GraphNode node)
	{
		T instance;
		try {
			instance = getset.newInstance();
			instance.setNode(node);			
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	protected void setNode(GraphNode node)
	{
		_currentNode=node;
	}

	public GraphNode getNode()
	{
		return _currentNode;
	}
	
	public void initialize(HttpServletRequest request){
		super.initialize(request);			
		_currentNode=null;		
		getCurrentNode(_parameter);
		if(_currentNode==null)
			getCurrentNode(_currentValue);
		if(_currentNode==null && _refKey!=null && _parameter!=null)
			_currentNode=GraphNode.get(_nodeLabel, _refKey, _parameter, false);
	}
	
	private void getCurrentNode(String parameter)
	{
		try
		{			
			if(parameter==null)
				throw new NumberFormatException();
			Long nodeId=Long.parseLong(parameter);
			_currentNode=new GraphNode(nodeId);
		}
		catch(NumberFormatException nfe)
		{
		}		
		if(_currentNode!=null && (!_currentNode.exists()))
			_currentNode=null;
	}
	
}

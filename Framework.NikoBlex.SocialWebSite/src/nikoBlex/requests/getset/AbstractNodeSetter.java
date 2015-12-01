package nikoBlex.requests.getset;

import java.util.List;

import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.getset.AbstractNodeGetSet;
import nikoBlex.requests.getset.AbstractNodeSetter;
import nikoBlex.requests.keys.SessionAttributeKeys;
import nikoBlex.requests.post.ISetter;
import nikoBlex.businessmodels.database.GraphNode;
import nikoBlex.businessmodels.database.GraphRelationship;
import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.businessmodels.relationships.types.IGraphRelationshipType;

public abstract class AbstractNodeSetter<NodeLabel extends Enum<NodeLabel>> extends AbstractNodeGetSet<NodeLabel> implements ISetter{	
		
	
	protected AbstractNodeSetter(IUserAuthorization methodAccess,
			NodeLabel nodeLabel, INodePropertyKey refKey,
			SessionAttributeKeys sessionAttributeKey) {
		super(nodeLabel, methodAccess, refKey, sessionAttributeKey);
	}

	public static <NodeLabel extends Enum<NodeLabel>,T extends AbstractNodeSetter<NodeLabel>> T SetterFactory(GraphNode node,Class<T> setter) throws InstantiationException, IllegalAccessException
	{
		T instance=setter.newInstance();
		instance._currentNode=node;
		return instance;
	}
		
	protected String setAttribute(INodePropertyKey id)
	{
		if(_currentNode!=null)
			_currentNode.setProperty(id, _parameter);		
		return _currentNode.getProperty(id).equals(_parameter)?"":"error.attribute.changed";
	}

	protected String addRelation(IGraphRelationshipType relType)
	{
		Long targetId=Long.parseLong(_parameter);		
		_currentNode.createRelationship(targetId, relType);
		return _currentNode.getRelationships(new GraphNode(targetId), relType)!=null?"":"error.relation.added";
	}
	
	protected String replaceRelation(IGraphRelationshipType relType)
	{
		List<GraphRelationship> listRels=_currentNode.getRelationships(relType, true, true);
		if(!listRels.isEmpty())			
			listRels.get(0).remove();
		return addRelation(relType).isEmpty() && _currentNode.getRelationships(relType, true, true).size()==1?"":"error.relation.changed";
		
	}
	
	protected String deleteRelation(IGraphRelationshipType relType)
	{
		Long targetId=Long.parseLong(_parameter);
		GraphNode targetNode=new GraphNode(targetId);
		_currentNode.getRelationships(targetNode, relType).get(0).remove();
		return _currentNode.getRelationships(targetNode, relType)==null?"":"error.relation.deleted";
	}

	public boolean equals(AbstractNodeSetter<NodeLabel> getter)
	{
		return getter.getId()==getId();
	}

}

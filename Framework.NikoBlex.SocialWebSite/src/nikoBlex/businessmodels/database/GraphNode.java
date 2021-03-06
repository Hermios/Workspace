package nikoBlex.businessmodels.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nikoBlex.businessmodels.enums.GenericPropertyKey;
import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.businessmodels.relationships.types.IGraphRelationshipType;

import org.neo4j.graphdb.Node;

public class GraphNode
{
	private Node _node;	
	
	public GraphNode(Long nodeId)
	{
		_node=GraphDatabase.getNode(nodeId);
	}
	
	public GraphNode(Node node)
	{
		_node=node;
	}
	
	public static <NodeLabel extends Enum<NodeLabel>> GraphNode create(NodeLabel nle)
	{		
		Node node=GraphDatabase.createNode(nle);
		if(node==null)
			return null;
		GraphNode graphNode=new GraphNode(node);
		graphNode.setProperty(GenericPropertyKey.registerDate, Calendar.getInstance().getTimeInMillis());
		return graphNode;
	}

	public static <NodeLabel extends Enum<NodeLabel>> GraphNode create(NodeLabel nle,INodePropertyKey key,Object value,boolean getExistingOne)
	{		
		Node node=GraphDatabase.createNode(nle, key, value,getExistingOne);
		if(node==null)
			return null;
		GraphNode graphNode=new GraphNode(node);
		graphNode.setProperty(GenericPropertyKey.registerDate, Calendar.getInstance().getTimeInMillis());
		return graphNode;		
	}
	
	public boolean exists()
	{
		return _node!=null;
	}
		
	public void setProperties(HashMap<INodePropertyKey,Object> listProperties)
	{		
		for(Map.Entry<INodePropertyKey, Object> entry:listProperties.entrySet())
				GraphDatabase.setProperty(_node, entry.getKey(),entry.getValue());
	}
	
	public void setProperty(INodePropertyKey key, Object value)
	{
		HashMap<INodePropertyKey,Object> listProperties=new HashMap<>();
		listProperties.put(key, value);
		setProperties(listProperties);
	}
	
	public void removeProperties(List<INodePropertyKey> listKeys)
	{
		HashMap<INodePropertyKey,Object> listKeysHashMap=new HashMap<>();
		for(INodePropertyKey key:listKeys)
			listKeysHashMap.put(key, null);
		setProperties(listKeysHashMap);
	}
	
	public void removeProperty(INodePropertyKey key)
	{
		List<INodePropertyKey> listKeys=new ArrayList<>();
		listKeys.add(key);
		removeProperties(listKeys);
	}

	public Object getProperty(INodePropertyKey key)
	{
		return GraphDatabase.getProperty(_node, key);
	}
			
	public static <NL extends Enum<NL>> GraphNode get(NL nodeType,INodePropertyKey id,Object value,boolean keepAdmin)
	{
		List<Node> nodes=GraphDatabase.findNodesByLabelAndProperty(nodeType, id, value);
		return nodes.size()!=0?new GraphNode(nodes.get(0)):null;
	}
	
	public void delete()
	{
		for(GraphRelationship relationship:getRelationships(null, true, true))
			relationship.remove();
		GraphDatabase.deleteNode(_node);
	}
		
	public static <NL extends Enum<NL>> List<GraphNode> getNodes(NL nodeType)
	{
		List<Node> listNodes=GraphDatabase.getNodes(nodeType);
		return getListNode(listNodes);
	}
	
	public static <NL extends Enum<NL>> List<GraphNode> getNodes(NL nodeType,INodePropertyKey id,Object value)
	{
		List<Node> listNodes=GraphDatabase.findNodesByLabelAndProperty(nodeType, id, value);
		return getListNode(listNodes);
	}
	
	public static <NL extends Enum<NL>> List<GraphNode> getNodesWithRegex(NL nodeType,String regexValue,INodePropertyKey... keys)
	{
		List<Node> listNodes=GraphDatabase.findNodesByLabelAndRegexProperty(nodeType, regexValue, keys);
		return getListNode(listNodes);
	}
	
	public List<GraphRelationship> getRelationships(IGraphRelationshipType type,boolean startNode,boolean endNode)
	{
		return GraphRelationship.getListGraphRelationship(GraphDatabase.getRelationshipsFromNode(_node, GraphRelationship.getRelationshipType(type),startNode,endNode));
	}	
	
	public List<GraphRelationship> getRelationships(GraphNode endNode,IGraphRelationshipType... types)
	{
		return GraphRelationship.getListGraphRelationship(GraphDatabase.getRelationships(_node, endNode._node,GraphRelationship.getRelationshipTypes(types)));
	}	
	
	public GraphRelationship createRelationship(Long targetId,IGraphRelationshipType type)
	{
		return new GraphRelationship(GraphDatabase.createRelationshipTo(_node, new GraphNode(targetId)._node,GraphRelationship.getRelationshipType(type)));
	}	
	
	public List<GraphNode> getRelatedNodes(GraphRelationship... listRelationships)
	{
		List<Node> result=new ArrayList<>();
		for(GraphRelationship relationship:listRelationships)
			result.add(GraphDatabase.getOtherNode(_node,relationship.getRelationship()));
		return getListNode(result);
	}
	
	public List<GraphNode> getNodes(GraphNode finalMamsNode,Integer depth,boolean uniqueResult,IGraphRelationshipType... listMamsRelationshipsTypes)
	{
		Node finalNode=finalMamsNode==null?null:finalMamsNode._node;
		List<Node> listNodes=GraphDatabase.getNodes(_node, finalNode,depth,uniqueResult,GraphRelationship.getRelationshipTypes(listMamsRelationshipsTypes));
		return getListNode(listNodes);
	}
	
	public <T extends Enum<T>> T  getLabel(Class<T> classT)
	{				
		return T.valueOf(classT, GraphDatabase.getNodeLabel(_node));
	}
		
	public Long getId()
	{
		return _node.getId();
	}

	static List<GraphNode> getListNode(List<Node> listNodes)
	{
		List<GraphNode> result=new ArrayList<>();
		for(Node node:listNodes)
		{
			GraphNode nodes=new GraphNode(node);
				result.add(nodes);
		}
		return result;
	}

	Node getNode()
	{
		return _node;
	}
}

package nikoBlex.businessmodels.database;

import java.util.ArrayList;
import java.util.List;

import nikoBlex.businessmodels.relationships.types.IGraphRelationshipType;

import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class GraphRelationship
{
	private Relationship _relationship;
	
	public GraphRelationship(Long id)
	{
		_relationship=GraphDatabase.getRelationship(id);		
	}
	
	public GraphRelationship(Relationship relationship)
	{
		_relationship=relationship;		
	}
	
	
	
	
	public GraphNode getNode(boolean startNode)
	{
		return new GraphNode(GraphDatabase.getRelationshipNode(_relationship, startNode));
	}	
	
	public GraphNode getOtherNode(Long firstNode)
	{
		return new GraphNode(GraphDatabase.getOtherNode(new GraphNode(firstNode).getNode(), _relationship));
	}
	
	public void remove()
	{
		GraphDatabase.removeRelationShip(_relationship);
	}
	
	public String getType()
	{
		return GraphDatabase.getRelationshipType(_relationship);
	}
	
	public Long getId()
	{
		return _relationship.getId();
	}
	
	static List<GraphRelationship> getListGraphRelationship(List<Relationship> listRelationships)
	{
		List<GraphRelationship> result=new ArrayList<>();
		for(Relationship relationship:listRelationships)
			result.add(new GraphRelationship(relationship));
		return result;
	}

	static List<Relationship> getListRelationship(List<GraphRelationship> listMamsRelationships)
	{
		List<Relationship> result=new ArrayList<>();
		for(GraphRelationship mamsRelationship:listMamsRelationships)
			result.add(mamsRelationship._relationship);
		return result;
	}
	
	static List<RelationshipType> getRelationshipTypes(final IGraphRelationshipType... types)
	{
		List<RelationshipType> result=new ArrayList<>();
		for(IGraphRelationshipType type:types)
			result.add(getRelationshipType(type));
		return result;
	}
	
	static RelationshipType getRelationshipType(final IGraphRelationshipType type)
	{
		RelationshipType result=new RelationshipType() {
			
			public String name() {
				if(type==null)
					return null;
				return type.getClass().getName().replace(".", "_")+"_"+type.toString();
			}
		};
		return result;
	}
	
	
	Relationship getRelationship()
	{
		return _relationship;
	}
}
package nikoBlex.usecases.comment;

import java.util.ArrayList;
import java.util.List;

import nikoBlex.businessmodels.database.GraphRelationship;
import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.businessmodels.relationships.types.CommentRelationshipType;
import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.getset.AbstractNodeGetter;
import nikoBlex.requests.keys.SessionAttributeKeys;

public abstract class AbstractCommentGetter<NodeLabel extends Enum<NodeLabel>> extends AbstractNodeGetter<NodeLabel>{

	public AbstractCommentGetter(IUserAuthorization authorizationLEvel,NodeLabel nodeLabel,INodePropertyKey refKey,
			SessionAttributeKeys sessionAttributeKey) 
	{
		super(authorizationLEvel, nodeLabel,refKey,sessionAttributeKey);
	}

	public String getDescription()
	{
		return getAttribute(CommentPropertyKey.description, true);
	}
		
	public abstract AbstractNodeGetter<NodeLabel> getTarget();

	@SuppressWarnings("unchecked")
	public List<AbstractCommentGetter<NodeLabel>> getPreviousComments()
	{		
		List<AbstractCommentGetter<NodeLabel>> result=new ArrayList<>();
		if(exists())
			result.add(this);
		List<GraphRelationship> listRels=_currentNode.getRelationships(CommentRelationshipType.target, true, false);
		AbstractCommentGetter<NodeLabel> previousComment;
		if(listRels.size()>0)
		{
			previousComment=construct(AbstractCommentGetter.class,listRels.get(0).getOtherNode(_currentNode.getId()));
			if(previousComment.exists())
				result.addAll(previousComment.getPreviousComments());
		}
		return result;
	}
}

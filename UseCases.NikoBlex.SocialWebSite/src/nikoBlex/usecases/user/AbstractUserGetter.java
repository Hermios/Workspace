package nikoBlex.usecases.user;

import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.getset.AbstractNodeGetter;
import nikoBlex.requests.keys.SessionAttributeKeys;

public abstract class AbstractUserGetter<NodeLabel extends Enum<NodeLabel>> extends AbstractNodeGetter<NodeLabel>{

	public AbstractUserGetter(IUserAuthorization methodAccess,
			NodeLabel expectedNodeLabel, INodePropertyKey refKey,
			SessionAttributeKeys sessionAttributeKey) {
		super(methodAccess, expectedNodeLabel, refKey, sessionAttributeKey);
	}
	
	public abstract String getPortrait(boolean resize,boolean cut,Integer width,Integer height);
}

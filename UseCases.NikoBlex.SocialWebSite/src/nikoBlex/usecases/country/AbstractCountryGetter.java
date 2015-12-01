package nikoBlex.usecases.country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import nikoBlex.businessmodels.database.GraphNode;
import nikoBlex.businessmodels.enums.INodePropertyKey;
import nikoBlex.requests.authorizationlevels.IUserAuthorization;
import nikoBlex.requests.getset.AbstractNodeGetter;
import nikoBlex.requests.keys.RequestParameterKeys;
import nikoBlex.requests.keys.SessionAttributeKeys;
import nikoBlex.requests.tools.RequestGenericTools;
import nikoBlex.tools.annotations.HelperAnnotation;
import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;

public class AbstractCountryGetter<NodeLabel extends Enum<NodeLabel>> extends AbstractNodeGetter<NodeLabel> {
	private static final String COOKIE_COUNTRY="userCountry";
	AbstractCountryGetter<NodeLabel> _localCountryGetter;
	
	public AbstractCountryGetter(IUserAuthorization authorizationLevel,
			NodeLabel nodeLabel, INodePropertyKey refKey,
			SessionAttributeKeys sessionAttributeKey) {
		super(authorizationLevel, nodeLabel, refKey, sessionAttributeKey);
		// TODO Auto-generated constructor stub
	}
		
	@HelperAnnotation(description="get code of the current country")
	public String getCountryCode()
	{
		return _currentNode==null?"":_currentNode.getProperty(CountryPropertyKey.code).toString();
	}
	
	@SuppressWarnings("unchecked")
	@HelperAnnotation(description="return all the countries")
	public List<AbstractCountryGetter<NodeLabel>> getAllCountries(boolean undefinedAuthorized)
	{
		List<AbstractCountryGetter<NodeLabel>> result=new ArrayList<>();
		for(GraphNode node:GraphNode.getNodes(_nodeLabel))
		{
			result.add(construct(AbstractCountryGetter.class,node));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@HelperAnnotation(description="get code of the geolocalized country")
	public AbstractCountryGetter<NodeLabel> getLocalCountry()
	{
		//Is code already locally stored
		if(_localCountryGetter!=null)
			return _localCountryGetter;
		String countryCode="";
		//Search if code is stored in a cookie
		countryCode=RequestGenericTools.getCookieData(_request, COOKIE_COUNTRY);
		
		if(countryCode==null)
		{
			String remoteAdress=_request.getRemoteAddr();
			if(remoteAdress!=null)
			{
			try {
				Document doc=Jsoup.connect(PropertiesManager.getString(EnumPropertieKeys.COUNTRIES_WEBSITE)+remoteAdress).get();
				countryCode= doc.getElementsByTag("countryAbbrev").get(0).text();
			} 
			catch (IOException e) {}
			}
		}
		if(countryCode==null||countryCode.equalsIgnoreCase("xx"))
			countryCode=PropertiesManager.getString(EnumPropertieKeys.DEFAULT_COUNTRY);
		//add to cookie
		HttpServletResponse response=(HttpServletResponse)_request.getAttribute(RequestParameterKeys.response.toString());
		response.addCookie(new Cookie(COOKIE_COUNTRY, countryCode));
		GraphNode countryNode=GraphNode.get(_nodeLabel, CountryPropertyKey.code, countryCode.toLowerCase(),false);
		return this.construct(AbstractCountryGetter.class,countryNode);
	}	
	
	@Override
	public void initialize(HttpServletRequest request){
		super.initialize(request);
		_localCountryGetter=null;
	}	
}

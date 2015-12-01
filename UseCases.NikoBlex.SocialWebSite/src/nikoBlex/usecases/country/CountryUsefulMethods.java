package nikoBlex.usecases.country;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import nikoBlex.businessmodels.database.GraphNode;
import nikoBlex.resources.tools.ResourceBundlesGetter;
import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;

import org.apache.commons.lang.NullArgumentException;

public class CountryUsefulMethods {
	public static <NodeLabel extends Enum<NodeLabel>> void addListCountries(NodeLabel nodeLabel) throws MalformedURLException, Exception
	{
		List<GraphNode> listCountrieNodes=GraphNode.getNodes(nodeLabel);
		if(!listCountrieNodes.isEmpty() && !PropertiesManager.getBoolean(EnumPropertieKeys.CHECK_COUNTRY))
			return;
		System.out.println("load all countries");
		Iterator<ResourceBundlesGetter> enumRBG=new ResourceBundlesGetter().getAllResourceBundles().iterator();
		ResourceBundlesGetter countryRGB=null;
		while(countryRGB==null && enumRBG.hasNext())
		{
			ResourceBundlesGetter currentRGB=enumRBG.next();
			if(currentRGB.getName().equalsIgnoreCase("Country"))
				countryRGB=currentRGB;
		}
		if(countryRGB==null)
			throw new NullArgumentException("country ResourceBundleGetter");
		for(String key:countryRGB.getEntries().keySet())
		{
			String[] keyArray=key.split("\\.");
			String countryCode=keyArray[keyArray.length-1];
			GraphNode.create(nodeLabel, CountryPropertyKey.code, countryCode,true);
		}			
	}
}

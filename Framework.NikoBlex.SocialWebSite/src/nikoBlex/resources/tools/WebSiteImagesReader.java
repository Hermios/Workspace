package nikoBlex.resources.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;

public class WebSiteImagesReader{	
	
	private String _url;
	private List<String> _ignoredPictures;
	private List<String> _listAllImages;		
	private int filterLevel;
	private int minHeight;
	private int minWidth;
	
	public WebSiteImagesReader(String url)
	{
		_ignoredPictures=new ArrayList<>();
		_listAllImages=new ArrayList<>();
		_url=url;
		minHeight=PropertiesManager.getInteger(EnumPropertieKeys.EXTERN_IMAGES_MIN_HEIGHT);
		minWidth=PropertiesManager.getInteger(EnumPropertieKeys.EXTERN_IMAGES_MIN_WIDTH);
		filterLevel=PropertiesManager.getInteger(EnumPropertieKeys.EXTERN_IMAGES_FILTER_LEVEL);
	}
	
	public List<String> getWebSiteImages() throws IOException
	{	
		_ignoredPictures.clear();
		_listAllImages.clear();
		Connection connection=null;
		_url=_url.trim();
		try
		{
			connection=Jsoup.connect(_url);
		}
		catch(IllegalArgumentException iae)
		{
			if(!_url.startsWith("http://"))
			{
				_url="http://"+_url;
				try
				{
					connection=Jsoup.connect(_url);
				}
				catch(IllegalArgumentException ia)
				{					
					ia.printStackTrace();
					return _listAllImages;
				}
			}
			else
				return _listAllImages;
		}
				
		connection.followRedirects(false);
	    Response response = connection.execute();
	    connection.cookies(response.cookies());
	    connection.followRedirects(true);
	    response=connection.execute();
	    for(Element image:response.parse().getElementsByTag("img"))
		{
			String basicUrl=image.absUrl("src");
			if(manageImageList(basicUrl))
				continue;
			URL url=new URL(_url);
			String editedUrl=url.getProtocol()+"://"+url.getHost()+basicUrl;
			manageImageList(editedUrl);
		}
		return _listAllImages;
	}		
		
	private boolean manageImageList(String url)
	{
		if(!isImageSavable(url))
			return true;
		try
		{
			Jsoup.connect(url);
			if(isImageRelevant(url))
				_listAllImages.add(url);
			return true;
		}		
		catch(IllegalArgumentException iae)
		{
			return false;
		}
	}
	
	private boolean isImageSavable(String urlString)
	{
		
		if(_ignoredPictures.contains(urlString))
			return false;
		if(_listAllImages.contains(urlString))
		{
			if(PropertiesManager.getBoolean(EnumPropertieKeys.EXTERN_IMAGES_DOUBLONRE_REMOVE))
				_listAllImages.remove(urlString);
			_ignoredPictures.add(urlString);
			return false;
		}
		return true;
	}
	
	private boolean isImageRelevant(String url)
	{
		BufferedImage image;
		try 
		{
			image = ImageIO.read(new URL(url));
			if(image==null)
				return false;
			if(filterLevel>1)
				return true;
			if(image.getHeight()<minHeight)
				return false;
			if(image.getWidth()<minWidth)
				return false;
			return true;
		} catch (IOException e) {
			return filterLevel>0;			
		}
	}
}

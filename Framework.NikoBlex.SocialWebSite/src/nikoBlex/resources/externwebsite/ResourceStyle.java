package nikoBlex.resources.externwebsite;

import java.io.IOException;
import java.net.MalformedURLException;

public class ResourceStyle extends AbstractResource{
	ResourceStyle(String urlRoot){
		super(urlRoot);
	}

	ResourceStyle(String urlRoot,String url,String rootPath) throws MalformedURLException, IOException
	{
		super(urlRoot,url,rootPath,true);
	}
	
	@Override
	String analyseLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}
}

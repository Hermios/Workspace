package nikoBlex.resources.externwebsite;

import java.io.IOException;
import java.net.MalformedURLException;

public class ResourceScript extends AbstractResource {

	ResourceScript(String urlRoot){
		super(urlRoot);
	}

	ResourceScript(String urlRoot,String url,String rootPath) throws MalformedURLException, IOException
	{
		super(urlRoot,url,rootPath,true);
	}
	
	@Override
	String analyseLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}
}

package nikoBlex.resources.externwebsite;

import java.io.IOException;
import java.net.MalformedURLException;

public class ResourceHtml extends AbstractResource{
		
	ResourceHtml(String urlRoot){
		super(urlRoot);
	}

	ResourceHtml(String urlRoot,String url,String rootPath) throws MalformedURLException, IOException
	{
		super(urlRoot,url,rootPath,true);
	}
	
	@Override
	void analyseFile() {
		
		
	}

	@Override
	String analyseLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}
}

package nikoBlex.resources.externwebsite;

import java.io.IOException;
import java.net.MalformedURLException;

public class ResourceImage extends AbstractResource{

	ResourceImage(String urlRoot, String url, String rootPath)
			throws MalformedURLException, IOException {
		super(urlRoot, url, rootPath,false);
	}

	@Override
	String analyseLine(String line) {
		return null;
	}

}

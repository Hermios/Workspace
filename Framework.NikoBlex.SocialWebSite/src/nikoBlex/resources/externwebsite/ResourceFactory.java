package nikoBlex.resources.externwebsite;

//import java.io.BufferedInputStream;
import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLConnection;

public class ResourceFactory {
	public static AbstractResource getResource(File file) throws IOException
	{
		//InputStream is = new BufferedInputStream(new FileInputStream(file));
		//String mimeType= URLConnection.guessContentTypeFromStream(is);
		return null;
		/*if(mimeType!=null)
		{
			if(mimeType.startsWith("image"))
				return null;
			if(mimeType.contains("htm"))
				return new ResourceHtml();
			if(mimeType.contains("css"))
				return new ResourceStyle();
			if(mimeType.contains("javascript"))
				return new ResourceScript();
			return null;
		}
		else
		{
			String filePath=file.getPath();
			if(filePath.endsWith(".htm")||filePath.endsWith(".html"))
				return new ResourceHtml();
			if(filePath.endsWith(".css"))
				return new ResourceStyle();
			if(filePath.endsWith(".js"))
				return new ResourceScript();
			return null;
		}*/
			
		
		
	}
}

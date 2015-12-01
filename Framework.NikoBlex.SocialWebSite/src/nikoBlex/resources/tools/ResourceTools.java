package nikoBlex.resources.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.utils.GenericFunctions;

public class ResourceTools {
	public static String getStaticResourceRoot(String content)
	{
		String result=null;
		String extension=GenericFunctions.getFileExtension(content);
		if(extension!=null)
			extension=extension.toLowerCase();
		result=PropertiesManager.getString(EnumPropertieKeys.WEBCONTENT_DIR_START,extension);
		if(result!=null)
			return result;
		return PropertiesManager.getString(EnumPropertieKeys.WEBCONTENT_DIR_START,getContentType(content).split("/")[0].toLowerCase());
	}
	
	public static String getContentType(String filePath)
	{
		String extension=GenericFunctions.getFileExtension(filePath);
		if(extension!=null)
			extension=extension.toLowerCase();
		String result=PropertiesManager.getString(EnumPropertieKeys.CONTENTTYPE_START,extension);
		return result;
	}
	
	public static String getFileHash(String filePath) throws NoSuchAlgorithmException, IOException
	{
		FileInputStream fis=new FileInputStream(filePath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) 
        	outputStream.write(buf, 0, readNum);
        byte[] data = outputStream.toByteArray();
        fis.close();
        outputStream.close();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        byte[] hash = md.digest();
        StringBuffer result=new StringBuffer();
        for (int i=0; i < hash.length; i++) 
            result.append(Integer.toString( ( hash[i] & 0xff ) + 0x100, 16).substring( 1 ));
                         
        return result.toString();
	}
}

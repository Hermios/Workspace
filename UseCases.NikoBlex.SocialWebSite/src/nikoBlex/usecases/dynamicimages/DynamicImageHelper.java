package nikoBlex.usecases.dynamicimages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import nikoBlex.resources.enums.EnumResourceSource;
import nikoBlex.resources.tools.ResourceTools;
import nikoBlex.resources.tools.ResourcesParameterKeys;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.utils.GenericFunctions;
import nikoBlex.tools.utils.PathConstructor;

public class DynamicImageHelper{
	
	public static <NodeLabel extends Enum<NodeLabel>> void uploadImage(HttpServletRequest request, Long id,IImageType category) throws Exception
	{
		if(!ServletFileUpload.isMultipartContent(request))
			return;
		DiskFileItemFactory dfi=new DiskFileItemFactory();
		ServletFileUpload sfu=new ServletFileUpload(dfi);
		FileItem fileItem=sfu.parseRequest(request).iterator().next();
		String fileName=fileItem.getName();
		if(fileName==null)
			return;
		String extension=GenericFunctions.getFileExtension(fileName);
		DynamicImageManager<NodeLabel> dim=new DynamicImageManager<>(id, category);
		dim.createImageForNode(fileItem.getInputStream(), extension);
	}
	
	public static void uploadDefaultImage(String imageName,Long id,IImageType category) throws IOException
	{
		uploadImageFromLocal(id, category,
				PropertiesManager.getString(EnumPropertieKeys.WEBCONTENT_ROOT),
				ResourceTools.getStaticResourceRoot(imageName),
				imageName);
	}
	
	public static void uploadImageFromTemp(String imageName,Long id,IImageType category) throws IOException
	{
		File file=new File(uploadImageFromLocal(id, category,PropertiesManager.getString(EnumPropertieKeys.TEMP_DIRECTORY),imageName+"."+PropertiesManager.getString(EnumPropertieKeys.PAGE2IMAGES_EXTENSION)));
		final String extension="."+GenericFunctions.getFileExtension(file.getName());
		final String fileName=file.getName().replace(extension, "");
		FilenameFilter filter=new FilenameFilter() {
			
			public boolean accept(File file, String currentFileName) {
				return currentFileName.matches(fileName+"_.*"+extension);
			}
		};
		for(File thisFile:file.getParentFile().listFiles(filter))
			thisFile.delete();
		file.delete();		
	}
	
	public static <NodeLabel extends Enum<NodeLabel>> String uploadImageFromLocal(Long id,IImageType category,String...paths)throws IOException
	{
		String path=PathConstructor.getPath(paths);
		DynamicImageManager<NodeLabel> dim=new DynamicImageManager<>(id, category);
		String extension=GenericFunctions.getFileExtension(path);
		FileInputStream fis=new FileInputStream(path);
		dim.createImageForNode(fis, extension);
		fis.close();
		return path;
	}
	
	public static String uploadImageFromSnapshot(String url) throws IOException
	{
		String urlRoot=GenericFunctions.getUrlRoot(url);
		return DynamicImageManager.createImageFromSnapshot(url, urlRoot);
	}
	
	public static String uploadImageFromUrl(String urlString) throws IOException
	{
		return DynamicImageManager.createImageFromUrl(urlString, null);
	}

	public static <NodeLabel extends Enum<NodeLabel>> String getChangedImage(Long id, IImageType category, boolean resize,boolean cut,Integer x,Integer y,int width,int height)
	{
		String result;
		DynamicImageManager<NodeLabel> dim;
		try {
			dim = new DynamicImageManager<>(id, category);			
			result= dim.getChangedPicture(resize, cut, x, y, width, height);
			return getFormattedRequest(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}				
	}
	
	public static String getChangedImage(String filePath, String fileDestination, boolean resize,boolean cut,Integer x,Integer y,int width,int height)
	{
		String result;
		try {
			result= DynamicImageManager.getChangedPicture(new File(filePath),new File(fileDestination), resize, cut,x, y, width, height);
			return getFormattedRequest(result);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}				
	}	
	
	public static <NodeLabel extends Enum<NodeLabel>> void removeAllImages(Long id,IImageType category)
	{
		try {
			new DynamicImageManager<NodeLabel>(id, category).removeImagesFromId();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFormattedRequest(String ressourceName)
	{
		if(ressourceName==null)
			return "";
		while(ressourceName.startsWith(File.separator))
			ressourceName=ressourceName.substring(1);
		ressourceName=ressourceName.replace(File.separator, "/")+"?"+ResourcesParameterKeys.getResourceSourceKey()+"="+EnumResourceSource.DynamicImage.toString();
		return PathConstructor.getURL("",ressourceName);
	}

	
}

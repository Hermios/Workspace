package nikoBlex.tools.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;


public class GenericFunctions{
	
	public static List<File> getFiles(String root,String extension) throws ClassNotFoundException,IOException
	{
		List<File> result=new ArrayList<>();
		File rootFile=new File(root);
		if(!rootFile.exists()||!rootFile.isDirectory())
			return result;
		for(File thisFile:rootFile.listFiles())
		{
			if (thisFile.isDirectory())
				result.addAll(getFiles(thisFile.getAbsolutePath(),extension));
			else if(thisFile.getName().endsWith("."+extension))			
				result.add(thisFile);			
		}		
		return result;
	}	
	
	public static List<JarFile> getJarFiles(Class<?> currentClass)
	{
		List<JarFile> result=new ArrayList<>(); 
	    
		String currentJarPath=currentClass.getProtectionDomain().getCodeSource().getLocation().getPath();
	    File dirPath=new File(currentJarPath).getParentFile();
	    for(File file:dirPath.listFiles())
		{
	    	try 
	    	{
				result.add(new JarFile(file));
			} catch (IOException e){}
		}
	    return result;
	}
	
	public static List<JarEntry> getJarEntriesFromFile(JarFile jarFile,String extension)
	{
		
		List<JarEntry> result=new ArrayList<>(); 
	    Enumeration<JarEntry> jarEntries = jarFile.entries();

	    while (jarEntries.hasMoreElements()) {
	        JarEntry jarEntry = (JarEntry) jarEntries.nextElement();
	        if (jarEntry.getName().endsWith(extension))
	            result.add(jarEntry);	        
	    }
	    return result;
	}
	
	public static List<JarEntry> getEntries(Class<?> currentClass,String extension) throws ClassNotFoundException,IOException
	{
		List<JarEntry> result=new ArrayList<>(); 
	    for(JarFile jarFile:getJarFiles(currentClass))
	    	result.addAll(getJarEntriesFromFile(jarFile, extension));
		return result;
	}	
	
	public static void copyFolder(File src, File dest)
	    	throws IOException{
	 
	    	if(src.isDirectory()){
	 
	    		//if directory not exists, create it
	    		if(!dest.exists())
	    		   dest.mkdir();	    		   
	    		
	    		//list all the directory contents
	    		String files[] = src.list();
	 
	    		for (String file : files) {
	    		   //construct the src and dest file structure
	    		   File srcFile = new File(src, file);
	    		   File destFile = new File(dest, file);
	    		   //recursive copy
	    		   copyFolder(srcFile,destFile);
	    		}
	 
	    	}else{
	    		//if file, then copy it
	    		//Use bytes stream to support all file types
	    		dest.createNewFile();
	    		InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 
	 
	    	        byte[] buffer = new byte[1024];
	 
	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }
	 
	    	        in.close();
	    	        out.close();	    	        
	    	}
	    }

	public static void deleteFolderContent(File folder,boolean includeFolder)
	{
		if(!folder.exists())
			return;
		for(File file:folder.listFiles())
		{
			if(file.isDirectory())
				deleteFolderContent(file,true);
			else
				file.delete();
		}
		if(includeFolder)
			folder.delete();
	}
	
	public static String getFileExtension(String filePath)
	{
		if(!filePath.contains("."))
			return null;
		return filePath.substring(filePath.lastIndexOf(".")+1,filePath.length());
	}
	
	public static String getCryptedPassword(String pwd)
	{		
		try
		{
			byte[] bom=pwd.getBytes("UTF-8");
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] resultByte= md.digest(bom);
			StringBuilder sbResult=new StringBuilder();
			for(byte thisByte :resultByte)
				sbResult.append(thisByte);
			return sbResult.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;			
		}
	}

	public static <T> List<T> getEditedList(List<T> initialList,Integer maximalCount,boolean random)
	{
		if(initialList==null || initialList.isEmpty())
			return new ArrayList<>();
		int maximalValues=initialList.size();
		if(maximalCount==null || maximalCount>=maximalValues)
			return initialList;
		if(!random)
			return initialList.subList(0, maximalCount);
		List<T> result=new ArrayList<>();
		List<Integer> listIndexes=new ArrayList<>();
		int i=0;
		while(i<maximalCount)
		{
			int index=(int)Math.floor(Math.random()*maximalValues);
			if(listIndexes.contains(index))
				continue;
			listIndexes.add(index);
			result.add(initialList.get(index));
			i++;
		}
		return result;
		
	}

	public static boolean websiteExists(String strUrl)
	{
		try 
		{
			URL url = new URL(strUrl);			 
			HttpURLConnection.setFollowRedirects(true);			
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setUseCaches(false);
			huc.setAllowUserInteraction(false);
			huc.setRequestMethod("HEAD");		
			int responseCode=huc.getResponseCode();				
			huc.disconnect();
			return responseCode == HttpURLConnection.HTTP_OK;
		}
		catch (Exception e) 
		{
			return false;
		}
	}

	public static HashMap<String,String> getMapFromJSON(String jsonData)
	{
		HashMap<String,String> result=new HashMap<>();
		jsonData=jsonData.substring(1,jsonData.length()-2);
		for(String currentEntry:jsonData.split(","))
		{			
			Pattern p=Pattern.compile("\"(.+?)\":\"(.+?)\"");
			Matcher m=p.matcher(currentEntry);
			if(m.find())
				result.put(m.group(1), m.group(2));
		}
		return result;
		
	}
	
	public static String getUrlRoot(String url)
	{
		if(url.contains("://"))
			url=url.split("://")[1];
		if(url.contains("/"))
			url=url.split("/")[0];
		if(url.contains("."))
		{
			String[] urlArray=url.split("\\.");
			url=urlArray[urlArray.length-2];
		}
		return url;
	}

	public static String setFirstUpperCase(String originalString)
	{
		String stringLowerCase=originalString.toLowerCase();
		char firstLetter=Character.toUpperCase(stringLowerCase.charAt(0));
		return firstLetter+stringLowerCase.substring(1);
	}

	public static String findUniqueFile(String path,final String fileStartName)
	{
		File startingDir=new File(path);
		FilenameFilter filter=new FilenameFilter() {
			
			public boolean accept(File dir, String regexFileName) {
				// TODO Auto-generated method stub
				return regexFileName.matches(fileStartName+"\\..*");
			}
		};
		
		File[] listFiles=startingDir.listFiles(filter);
			return listFiles!=null && listFiles.length==1?listFiles[0].getAbsolutePath():null;
		
	}

	public static boolean isMailValid(String email)
	{
		return email.matches("[\\.\\w]+@\\w+\\.[A-z]{2,}");
	}
	
	public void startupTaskAction(ServletContext servletContext) 
	{
		for(File tempImage : new File(PropertiesManager.getString(EnumPropertieKeys.TEMP_DIRECTORY)).listFiles())
			tempImage.delete();
	}
}

package nikoBlex.resources.externwebsite;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nikoBlex.resources.enums.EnumResourceSource;
import nikoBlex.resources.tools.ResourcesParameterKeys;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import nikoBlex.tools.entries.EnumPropertieKeys;
import nikoBlex.tools.entries.PropertiesManager;
import nikoBlex.tools.utils.Pair;
import nikoBlex.tools.utils.PathConstructor;

public class ExternalWebsiteLoader {	
	private static HashMap<String,String> _mapWebsitesContent=new HashMap<>();	
	private static final String REGEX_URL_PATTERN="(?:http:\\/\\/[\\.\\w]+)?(\\/?\\w\\S+\\w)";
	
	private static final String REGEX_SUB_URL_PATTERN="\\S+\\.\\w{2,4}(?:$|\\?.*)";
	private String webSiteUrl;
	private String webSitePathRoot;
	private String webSiteUrlRoot;
	private String webSiteCode;
	private Pattern patternUrl;	
	
	public ExternalWebsiteLoader(String webSiteUrl) throws IOException
	{
		this.webSiteUrl=webSiteUrl;				
		if(_mapWebsitesContent.containsKey(webSiteUrl))
			return;
		webSiteCode=RandomStringUtils.randomAlphanumeric(25);
		webSitePathRoot=PathConstructor.getPath(PropertiesManager.getString(EnumPropertieKeys.TEMP_DIRECTORY),webSiteCode);
		String resourceUrl="";
		if(webSiteUrl.contains("?"))
		{
			String webSiteUrlArray[]=webSiteUrl.split("\\?");
			webSiteUrlRoot=webSiteUrlArray[0];
			resourceUrl="?"+webSiteUrlArray[1];
		}
		else
			webSiteUrlRoot=webSiteUrl;
		
		patternUrl=Pattern.compile(REGEX_URL_PATTERN);
		
		Pair<File,String> pairHtmlFile=LoadResource(resourceUrl,webSiteUrlRoot);
		pairHtmlFile.value_1.delete();
		_mapWebsitesContent.put(webSiteUrl, pairHtmlFile.value_2);
	}
	
	private Pair<File,String> LoadResource(String resourceUrl,String urlRoot) throws IOException,FileNotFoundException
	{
		Matcher resourceMatcher=patternUrl.matcher(resourceUrl);
		String fileName=resourceMatcher.find(0)?resourceMatcher.group(1):"temp.html";
		if(fileName.contains("?"))
			fileName=fileName.split("\\?")[0];
		while (fileName.endsWith("/"))
			fileName=fileName.substring(0,fileName.length()-1);
		File resourceFile=new File(webSitePathRoot,fileName.replace("/", File.separator));
		if(resourceFile.exists())
			return null;
		if(!resourceFile.getParentFile().exists())
			resourceFile.getParentFile().mkdirs();				
		URL resourceFullUrl = null;
		if(!resourceMatcher.find(0))
			resourceFullUrl=new URL(urlRoot);
		else if(resourceMatcher.group(0).startsWith("http://"))
		{
			urlRoot=resourceMatcher.group(0).substring(0,resourceMatcher.group(0).lastIndexOf("/"));			
			resourceFullUrl=new URL(resourceMatcher.group(0));
		}
		else
			resourceFullUrl=new URL(PathConstructor.getURL(urlRoot,resourceMatcher.group(0)));
		
		FileUtils.copyURLToFile(resourceFullUrl, resourceFile);
		AbstractResource currentResource=ResourceFactory.getResource(resourceFile);
		if(currentResource==null)
			return null;
		StringBuffer fileContentBuffer=new StringBuffer();
		BufferedReader br=new BufferedReader(new FileReader(resourceFile));
		String readedLine;
		resourceFile.createNewFile();
		while((readedLine=br.readLine())!=null)
		{
			Matcher contentMatcher=patternUrl.matcher(readedLine);			
			
			while(contentMatcher.find())
			{
				if(!contentMatcher.group(1).matches(REGEX_SUB_URL_PATTERN))
					continue;
				try
				{
				LoadResource(contentMatcher.group(0),urlRoot);
				readedLine=readedLine.replace(contentMatcher.group(0), formateUrl(contentMatcher.group(1)));
				}
				catch(FileNotFoundException fnfe)
				{
					readedLine=readedLine.replace(contentMatcher.group(0), "");
				}
			}
			
			fileContentBuffer.append(readedLine);
			fileContentBuffer.append(System.lineSeparator());
		}			
		br.close();
		BufferedWriter bw=new BufferedWriter(new FileWriter(resourceFile));
		bw.write(fileContentBuffer.toString());
		bw.close();
		return new Pair<File,String>(resourceFile,fileContentBuffer.toString());		
	}
	
	private String formateUrl(String originalUrl)
	{
		String result="?";
		if(originalUrl.contains("?"))
			result=originalUrl.split("\\?").length==1?"":"&";
		result+=ResourcesParameterKeys.getResourceSourceKey()+"="+EnumResourceSource.Temp;
		return PathConstructor.getURL("",webSiteCode,originalUrl+result);
	}
	
	public static boolean isImage(File file) throws IOException
	{
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		String mimeType= URLConnection.guessContentTypeFromStream(is);
		return (mimeType!=null && mimeType.startsWith("image"));
	}
	
	public String getPageContent()
	{
		return _mapWebsitesContent.get(webSiteUrl);
	}
}

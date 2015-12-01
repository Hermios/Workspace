package nikoBlex.resources.externwebsite;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nikoBlex.tools.utils.PathConstructor;

import org.apache.commons.io.FileUtils;

public abstract class AbstractResource {
	protected final String url;
	protected final String urlRoot;
	protected final String dataPath;
	protected final String rootPath;
	protected final File localFile;
	protected List<String> fileContent=new ArrayList<>();
	AbstractResource(String urlRoot)
	{
		this.url=null;			
		this.dataPath=null;
		this.rootPath=null;
		this.urlRoot=urlRoot;
		this.localFile=null;
	}
	
	AbstractResource(String urlRoot,String url,String rootPath,boolean readData) throws MalformedURLException, IOException
	{
		this.url=url;			
		this.rootPath=rootPath;
		this.urlRoot=url.startsWith("http://")?url.substring(url.lastIndexOf("/")):urlRoot;
		String uri=url.contains("?")?url.split("\\?")[0]:url;
		uri=uri.replace("http://", "");
		this.dataPath=uri.substring(uri.indexOf("/"),uri.length()).replace("/", File.separator);
		String fullUrl=url.startsWith("http://")?url:PathConstructor.getURL(urlRoot,url);
		this.localFile=new File(rootPath,dataPath);
		FileUtils.copyURLToFile(new URL(fullUrl), localFile);
		if(!readData)
			return;
		BufferedReader br=new BufferedReader(new FileReader(localFile));
		String readedLine;
		
		while((readedLine=br.readLine())!=null)
			fileContent.add(readedLine);
		br.close();
	}
	
	String getUrl()
	{
		return dataPath.replace("\\", "/");
	}
	
	void analyseFile() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter(localFile));
		for(String line:fileContent)
		{
			bw.write(analyseLine(line));
			bw.write(System.lineSeparator());
		}
		bw.close();
	}
	
	abstract String analyseLine(String line);
}

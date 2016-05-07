package com.capiq.sec.filling.analysis.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import com.capiq.sec.filling.analysis.exception.SymantecAnalysisGeneralException;
import com.capiq.sec.filling.analysis.file.FileAccessorUtil;
import com.capiq.sec.filling.analysis.file.IFileAccessor;
import com.capiq.sec.filling.analysis.parse.stat.Sec10KDownloadStatData;
import com.capiq.sec.filling.common.util.PropertiesUtil;

public class SecDocHttpClient {

	public Sec10KDownloadStatData saveHttpResponse(String fileName, String filePath, String url, boolean saveResponse) throws SymantecAnalysisGeneralException  {
		InputStream content = null;
		IFileAccessor fileAccessor = null;
		boolean isHdfsStorage = Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.hdfs.storage"));
		
		try {
			URI uri = new URI(url);
			HttpGet httpget = new HttpGet(uri);
			HttpClient httpClient = null;
			
			if(Boolean.parseBoolean(PropertiesUtil.getProperty("file.sec.download.proxy.required"))){
				String proxyServer = PropertiesUtil.getProperty("file.sec.download.proxy.server.host");
				String userName = PropertiesUtil.getProperty("file.sec.download.proxy.server.username");
				String password = PropertiesUtil.getProperty("file.sec.download.proxy.server.password");
				Integer proxyPort = Integer.parseInt(PropertiesUtil.getProperty("file.sec.download.proxy.server.port"));

				HttpHost proxy = new HttpHost(proxyServer, proxyPort);
				Credentials credentials = new UsernamePasswordCredentials(userName , password);
				AuthScope authScope = new AuthScope(proxyServer, proxyPort);
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(authScope, credentials);
				httpClient = HttpClientBuilder.create().setProxy(proxy).setDefaultCredentialsProvider(credsProvider).build();			
			} else{
				httpClient = HttpClientBuilder.create().build();
			}
			
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			content = entity.getContent();

			fileAccessor = FileAccessorUtil.getFileAccesor(filePath, isHdfsStorage);
			
			OutputStream outputStream = fileAccessor.getOutputStream();
			IOUtils.copyLarge(content, outputStream);
			
			return new Sec10KDownloadStatData(fileName, filePath, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(content != null) content.close();
				if(fileAccessor != null) fileAccessor.closeIOStream();
			} catch (IOException exx) {
				exx.printStackTrace();;
			}
		}
		return new Sec10KDownloadStatData(fileName, filePath, false);
	}
}

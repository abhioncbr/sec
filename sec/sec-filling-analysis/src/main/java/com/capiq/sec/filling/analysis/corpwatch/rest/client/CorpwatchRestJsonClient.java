package com.capiq.sec.filling.analysis.corpwatch.rest.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.capiq.sec.filling.analysis.corpwatch.rest.model.JsonResponseCik;
import com.capiq.sec.filling.analysis.corpwatch.rest.model.JsonResponseFilings;
import com.capiq.sec.filling.analysis.corpwatch.rest.util.CorpwatchRestUriConstant;

public class CorpwatchRestJsonClient {
	
	public JsonResponseCik getCik(String cik){
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters); 

		//A simple GET request, the response will be mapped to JsonResponseCik.class
		JsonResponseCik result = restTemplate.getForObject(CorpwatchRestUriConstant.CIK_URI, JsonResponseCik.class,cik);
		//System.out.println(result);
		
		return result;
	}	
	
	public JsonResponseFilings getFiling(String year, String cwId){
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters); 

		//A simple GET request, the response will be mapped to JsonResponseFilings.class
		JsonResponseFilings result = restTemplate.getForObject(CorpwatchRestUriConstant.FILING_URI, JsonResponseFilings.class, year, cwId);
		//System.out.println(result);
		
		return result;
	}	

}

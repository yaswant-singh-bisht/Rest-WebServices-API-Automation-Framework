package com.qa.tests;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.BaseClass;
import com.qa.client.RestClient;
import com.qa.data.Users;

public class PostAPITest extends BaseClass {
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;
	
	@BeforeMethod
	public void setUp() {
		new BaseClass();
		String serviceURL = prop.getProperty("ServiceURL");
		String apiURL = prop.getProperty("apiURL");
		url = serviceURL + apiURL;		
	}
	
	@Test
	public void postAPI() throws ClientProtocolException, IOException {
		System.out.println("\nTest1: postAPI");
		restClient = new RestClient();
		// for pay load
		//jackson API
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus", "leader");
		//Object to json file
		mapper.writeValue(new File("D:\\Selenium\\Rest WebServices API Automation Framework\\src\\main\\java\\com\\qa\\data\\users.json"), users);
		
		//convert Java Object (POJO) to JSON Object (Marshelling)
		String userJsonString = mapper.writeValueAsString(users);
		
		//for header
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
			
		closeableHttpResponse = restClient.post(url, userJsonString, headerMap);
		
		//a. Status Code
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code ==> " + statusCode);
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_201, "Status code is not as expected:");
		
		//b. Json String
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("\nResponse in JSON format ==> " + responseJson);
		
		// convert JSON Object to Java Object (POJO) (Un-Marshelling)
		Users userResObj = mapper.readValue(responseString, Users.class);
		System.out.println("userResObj  ==> " + userResObj);
		
		Assert.assertEquals(users.getName(), userResObj.getName());
		Assert.assertEquals(users.getJob(), userResObj.getJob());
		System.out.println("Id: " + userResObj.getId());
		System.out.println("CreatedAt: " + userResObj.getCreatedAt());
	}
	

	
	
	
	
}

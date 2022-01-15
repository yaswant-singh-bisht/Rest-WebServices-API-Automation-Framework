package com.qa.tests;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.base.*;
import com.qa.client.RestClient;
import com.qa.utils.Utilities;

public class GetAPITest extends BaseClass {

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
	
	@Test (priority=2)
	public void getAPIWithoutHeader() throws ClientProtocolException, IOException {
		System.out.println("\nTest1: getAPIWithoutHeader");
		restClient = new RestClient();
		closeableHttpResponse = restClient.get(url);
		
		//a. Status Code
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code ==> " + statusCode);
		
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not as expected:");
		
		//b. Json String
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("\nResponse in JSON format ==> " + responseJson);
		
		//page
		String page = Utilities.getValueByJPath(responseJson, "/page");
		System.out.println("\nPage: "+ page);
		Assert.assertEquals(Integer.parseInt(page), 1);
		//per_page
		String perPage = Utilities.getValueByJPath(responseJson, "/per_page");
		System.out.println("Per Ppage: "+ perPage);
		Assert.assertEquals(Integer.parseInt(perPage), 6);
		//total
		String total = Utilities.getValueByJPath(responseJson, "/total");
		System.out.println("Total: "+ total);
		Assert.assertEquals(Integer.parseInt(total), 12);
		//total pages
		String totalPages = Utilities.getValueByJPath(responseJson, "/total_pages");
		System.out.println("Total Pages: "+ totalPages);
		Assert.assertEquals(Integer.parseInt(totalPages), 2);
		
		//data - 1st record - get the value of JSON array
		System.out.println("\nData from 1st record...");
		String id = Utilities.getValueByJPath(responseJson, "/data[0]/id");
		System.out.println("Id: "+ id);
		Assert.assertEquals(Integer.parseInt(id), 1);
		
		String email = Utilities.getValueByJPath(responseJson, "/data[0]/email");
		System.out.println("Email: "+ email);
		Assert.assertEquals(email, "george.bluth@reqres.in");
		
		String first_name = Utilities.getValueByJPath(responseJson, "/data[0]/first_name");
		System.out.println("First Name: "+ first_name);
		Assert.assertEquals(first_name, "George");
		
		String last_name = Utilities.getValueByJPath(responseJson, "/data[0]/last_name");
		System.out.println("Last Name: "+ last_name);
		Assert.assertEquals(last_name, "Bluth");
		
		String avatar = Utilities.getValueByJPath(responseJson, "/data[0]/avatar");
		System.out.println("Avatar: "+ avatar);
		Assert.assertEquals(avatar, "https://reqres.in/img/faces/1-image.jpg");
		
		
		//c. All headers
		Header[] headersArray = closeableHttpResponse.getAllHeaders();
		HashMap<String, String> allHeaders = new HashMap<String, String>();
		for (Header header : headersArray) {
			allHeaders.put(header.getName(), header.getValue());
		}
		System.out.println("\nHeader array ==> " + allHeaders);
	
		//data - 1st record - get the value of JSON array
//		String headerTransferEncoding = Utilities.getValueByJPath(responseJson, "/Transfer-Encoding");
//		System.out.println("x-powered-by: "+ headerTransferEncoding);
//		Assert.assertEquals(headerTransferEncoding, "chunked");
				
		
	}
	
	
	
	@Test (priority=1)
	public void getAPIWithHeader() throws ClientProtocolException, IOException {
		System.out.println("\nTest2: getAPIWithHeader");
		restClient = new RestClient();
		
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Accept-Encoding", "gzip, deflate, br");
		headerMap.put("Connection", "keep-alive");
		
		closeableHttpResponse = restClient.get(url, headerMap);
		
		//a. Status Code
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code ==> " + statusCode);
		
		Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200, "Status code is not as expected:");
		
		
	}
	
}

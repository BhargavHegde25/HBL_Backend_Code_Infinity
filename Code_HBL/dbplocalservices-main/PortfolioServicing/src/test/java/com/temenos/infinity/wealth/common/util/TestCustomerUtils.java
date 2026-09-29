package com.temenos.infinity.wealth.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * TestCustomerUtils does the following:-
 *   1) Test the testSingleCustomer
 *   2) Test the testMultiCustomer
 *   with sample request & response data
 *   
 * @author muthukumarv
 */

public class TestCustomerUtils {
	
	private static String singleCustomerDataTAP, multiCustomerDataTAP, updateSingleCustomerData, backendId, operation1, operation2;
	private static String [] arr;
	
	@BeforeClass
	public static void initializeData() {
		singleCustomerDataTAP = "{\"array\":[{\"customerCode\":\"190429\",\"totalAssetValue\":\"17000\",\"currencyCode\":\"USD\",\"customerName\":\"Thomas Wilson\"}]}";
		multiCustomerDataTAP = "{\"array\":[{\"customerCode\":\"190429\",\"totalAssetValue\":\"17000\",\"currencyCode\":\"USD\",\"customerName\":\"Thomas Wilson\"}, {\"customerCode\":\"190430\",\"totalAssetValue\":\"20000\",\"currencyCode\":\"USD\",\"customerName\":\"James Arthur\"}, {\"customerCode\":\"190426\",\"totalAssetValue\":\"5000\",\"currencyCode\":\"EUR\",\"customerName\":\"Daniel Jones\"}]}";
		updateSingleCustomerData = "190345,190270,190600";
		backendId = "190345";
		operation1 = "Add";
		operation2 = "Remove";
	}

	@Test
	public void testSingleCustomer() {
		
		JSONObject actualResultCustomer = CustomerUtils.getCustomerDetailsFromTAP(new JSONObject(singleCustomerDataTAP).getJSONArray("array"));
		JSONObject custJSON = actualResultCustomer.getJSONArray("Customers2").getJSONObject(0);
		assertNotNull(actualResultCustomer);
		
		assertNotNull(custJSON.get("customerId"));
		assertEquals(1, actualResultCustomer.getJSONArray("Customers2").length());
		assertNotEquals("", custJSON.get("customerId"));
	}
	
	
	@Test
	public void testMultiCustomer() {
		
		JSONObject actualResultCustomer = CustomerUtils.getCustomerDetailsFromTAP(new JSONObject(multiCustomerDataTAP).getJSONArray("array"));
		JSONObject custJSON = actualResultCustomer.getJSONArray("Customers2").getJSONObject(0);
		assertNotNull(actualResultCustomer);
		
		assertNotNull(custJSON.get("customerId"));
		assertEquals(3, actualResultCustomer.getJSONArray("Customers2").length());
		assertNotEquals("", custJSON.get("customerId"));
	}
	
	@Test
    public void updateAddTestSingleCustomer() {
		
		arr = updateSingleCustomerData.split(",");
		JSONObject actualResultCustomer = CustomerUtils.updateCustomerDetailsFromTAP(updateSingleCustomerData,arr,backendId,operation1);
		String custJSON = (String) actualResultCustomer.get("msg");
		assertNotNull(actualResultCustomer);
		
		assertNotNull(custJSON);
		assertEquals(1, actualResultCustomer.length());
		assertNotEquals("", custJSON);
	}
	
	@Test
    public void updateRemoveTestSingleCustomer() {
		
		arr = updateSingleCustomerData.split(",");
		JSONObject actualResultCustomer = CustomerUtils.updateCustomerDetailsFromTAP(updateSingleCustomerData,arr,backendId,operation2);
		String custJSON = (String) actualResultCustomer.get("msg");
		assertNotNull(actualResultCustomer);
		
		assertNotNull(custJSON);
		assertEquals(2, actualResultCustomer.length());
		assertNotEquals("", custJSON);
	}
	
}

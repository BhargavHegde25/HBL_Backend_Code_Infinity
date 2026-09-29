package com.temenos.dbx.product.transactionservices.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.resource.api.GeneralTransactionsResource;

@SuppressWarnings("deprecation")
public class GeneralTransactionsResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	GeneralTransactionsResource generalTransactionsResource;
	DataControllerRequest request;
	DataControllerResponse response;
	BusinessDelegateFactory businessDelegateFactory;
	GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	
	//This will run once for the class before all the test methods
		@BeforeClass 
		public static void setUpBeforeClass() throws Exception {
			mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
			mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		}

		@AfterClass
		public static void cleanup() throws Exception {
			mockedStaticForDBPAPIFactoryImpl.close();
			mockedStaticLog4j2Configurator.close();
		}
	
		@After
		public void tearDown() throws Exception {
		}

		
	@Before //This will run before each test case
	public void setUp() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		generalTransactionBusinessDelegate = mock(GeneralTransactionsBusinessDelegate.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class)).thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(GeneralTransactionsBusinessDelegate.class)).thenReturn(generalTransactionBusinessDelegate);
		
		generalTransactionsResource = new GeneralTransactionsResourceImpl();
	}
	
	@Test
	public void testInvokeGetPurposeCodes() throws Exception {
		Result result = new Result();
		
		JSONArray categoryArr = new JSONArray();
		categoryArr.put("BKDF - Bank Loan");
		categoryArr.put("Delayed");
		JSONObject categoryObj = new JSONObject();
		categoryObj.put("categoryPurposeCode",categoryArr);
		JSONArray namesArr = new JSONArray();
		namesArr.put(categoryObj);
		JSONObject namesObj = new JSONObject();
		namesObj.put("names",namesArr);
		JSONArray extCodesArr = new JSONArray();
		extCodesArr.put(namesObj);
		JSONObject extCodesObj = new JSONObject();
		extCodesObj.put("PurposeCodes",extCodesArr);
		result = JSONToResult.convert(extCodesObj.toString());
		//Mock should return expected results
		when(generalTransactionBusinessDelegate.getPurposeCodesById(methodID, inputArray, request, response)).thenReturn(result);
		
		//Actual call should return actualResults
		actualResult = (Result) generalTransactionsResource.getPurposeCodesById(methodID, inputArray, request, response);
		String actualResultJson = ResultToJSON.convert(actualResult);
		JSONObject actualResultJsonObj = new JSONObject(actualResultJson);
		JSONArray purposeCodesDetails = actualResultJsonObj.getJSONArray("PurposeCodes");
		
		assertEquals(purposeCodesDetails.toString(), "[\"BKDF - Bank Loan Delayed\"]");
	}
	
	@Test
	public void testInvokeGetPurposeCodesException() throws Exception {
		Result result = new Result();
		
		JSONArray extCodesArr = new JSONArray();
		extCodesArr.put("sample");
		JSONObject extCodesObj = new JSONObject();
		extCodesObj.put("Purpose",extCodesArr);
		result = JSONToResult.convert(extCodesObj.toString());
		//Mock should return expected results
		when(generalTransactionBusinessDelegate.getPurposeCodesById(methodID, inputArray, request, response)).thenReturn(result);
		
		//Actual call should return actualResults
		actualResult = (Result) generalTransactionsResource.getPurposeCodesById(methodID, inputArray, request, response);
		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), "12000");
	}

	@Test
	public void testInvokeGetExternalCodes() throws Exception {
		Result result = new Result();
		
		JSONArray categoryArr = new JSONArray();
		categoryArr.put("BKDF - Bank Loan");
		categoryArr.put("Delayed");
		JSONObject categoryObj = new JSONObject();
		categoryObj.put("categoryPurposeCode",categoryArr);
		JSONArray namesArr = new JSONArray();
		namesArr.put(categoryObj);
		JSONObject namesObj = new JSONObject();
		namesObj.put("names",namesArr);
		JSONArray extCodesArr = new JSONArray();
		extCodesArr.put(namesObj);
		JSONObject extCodesObj = new JSONObject();
		extCodesObj.put("ExternalCodes",extCodesArr);
		result = JSONToResult.convert(extCodesObj.toString());
		//Mock should return expected results
		when(generalTransactionBusinessDelegate.getExternalCodes(methodID, inputArray, request, response)).thenReturn(result);
		
		//Actual call should return actualResults
		actualResult = (Result) generalTransactionsResource.getExternalCodes(methodID, inputArray, request, response);
		String actualResultJson = ResultToJSON.convert(actualResult);
		assertEquals(actualResultJson, "{\"ExternalCodeDetails\":\"[\\\"BKDF - Bank Loan Delayed\\\"]\"}");
	}
	
	@Test
	public void testInvokeGetExternalCodesException() throws Exception {
		Result result = new Result();
		
		JSONArray extCodesArr = new JSONArray();
		extCodesArr.put("sample");
		JSONObject extCodesObj = new JSONObject();
		extCodesObj.put("Ext",extCodesArr);
		result = JSONToResult.convert(extCodesObj.toString());
		//Mock should return expected results
		when(generalTransactionBusinessDelegate.getExternalCodes(methodID, inputArray, request, response)).thenReturn(result);
		
		//Actual call should return actualResults
		actualResult = (Result) generalTransactionsResource.getExternalCodes(methodID, inputArray, request, response);
		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), "12000");
	}
}

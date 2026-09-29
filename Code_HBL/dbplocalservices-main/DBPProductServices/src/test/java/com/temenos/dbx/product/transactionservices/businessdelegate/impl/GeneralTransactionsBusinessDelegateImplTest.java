package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.GeneralTransactionsBusinessDelegate;

public class GeneralTransactionsBusinessDelegateImplTest {

	DataControllerRequest request;
	DataControllerResponse response;
	BusinessDelegateFactory businessDelegateFactory;
	GeneralTransactionsBusinessDelegate generalTransactionBusinessDelegate;
	GeneralTransactionsBusinessDelegateImpl generalTransactionBusinessDelegateImpl;
	CommonUtils commonUtils;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	private static MockedStatic<CommonUtils> mockedStaticCommonUtils;
	
	//This will run once for the class before all the test methods
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		mockedStaticCommonUtils = Mockito.mockStatic(CommonUtils.class);
	}

	@AfterClass
	public static void cleanup() throws Exception {
		mockedStaticCommonUtils.close();
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
	generalTransactionBusinessDelegateImpl = new GeneralTransactionsBusinessDelegateImpl();
}

@Test
public void testInvokeGetPurposeCodes() throws Exception {
	Result result = new Result();
	result.addParam("status","success");
	String serviceName = "T24ISPaymentsView";
	String operationName = "getPurposeCodes";
	Map<String,Object> hm = new HashMap<String,Object>();
	
	mockedStaticCommonUtils.when(() -> CommonUtils.callIntegrationService(request, (Map<String, Object>) inputArray[1] , hm, serviceName, operationName,
					true)).thenReturn(result);
	
	Result actualResult = new Result();
	actualResult = generalTransactionBusinessDelegateImpl.getPurposeCodesById(operationName, inputArray, request, response);
	
	assertEquals("success", actualResult.getParamValueByName("status"));
	

}

@Test
public void testInvokeGetExternalCodes() throws Exception {
	Result result = new Result();
	result.addParam("status","success");
	String serviceName = "T24ISPaymentsView";
	String operationName = "getExternalCodes";
	Map<String,Object> hm = new HashMap<String,Object>();
	
	mockedStaticCommonUtils.when(() -> CommonUtils.callIntegrationService(request, (Map<String, Object>) inputArray[1] , hm, serviceName, operationName,
					true)).thenReturn(result);
	
	Result actualResult = new Result();
	actualResult = generalTransactionBusinessDelegateImpl.getExternalCodes(operationName, inputArray, request, response);
	
	assertEquals("success", actualResult.getParamValueByName("status"));
	

}

@Test
public void testInvokeGetPurposeCodesException() throws Exception {
	String serviceName = "T24ISPaymentsView";
	String operationName = "getPurposeCodes";
	Map<String,Object> hm = new HashMap<String,Object>();
	
	Result expectedResult = new Result();
	expectedResult.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_12000.getErrorCodeAsString()));
	expectedResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_12000.getMessage()));
	
	mockedStaticCommonUtils.when(() -> CommonUtils.callIntegrationService(request, (Map<String, Object>) inputArray[1] , hm, serviceName, operationName,
					true)).thenThrow(new RuntimeException());
	
	Result actualResult = new Result();
	actualResult = generalTransactionBusinessDelegateImpl.getPurposeCodesById(operationName, inputArray, request, response);
	
	assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
	assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
}

@Test
public void testInvokeGetExternalCodesException() throws Exception {
	String serviceName = "T24ISPaymentsView";
	String operationName = "getExternalCodes";
	Map<String,Object> hm = new HashMap<String,Object>();
	
	Result expectedResult = new Result();
	expectedResult.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_12000.getErrorCodeAsString()));
	expectedResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_12000.getMessage()));
	
	mockedStaticCommonUtils.when(() -> CommonUtils.callIntegrationService(request, (Map<String, Object>) inputArray[1] , hm, serviceName, operationName,
					true)).thenThrow(new RuntimeException());
	
	Result actualResult = new Result();
	actualResult = generalTransactionBusinessDelegateImpl.getExternalCodes(operationName, inputArray, request, response);
	
	assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
	assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
}
	
}

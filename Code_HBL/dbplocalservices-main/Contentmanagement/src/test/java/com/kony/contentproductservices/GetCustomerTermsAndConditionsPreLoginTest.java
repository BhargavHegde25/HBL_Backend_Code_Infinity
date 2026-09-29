package com.kony.contentproductservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/*
 * author : kushboo.tackiar
*/

public class GetCustomerTermsAndConditionsPreLoginTest {

	DataControllerRequest dcRequest;
	DataControllerResponse response;
	JavaService2 javaService2;
	DBPUtilitiesConstants dBPUtilitiesConstants;
	String methodID;
	Object[] inputArray;
	ServicesManagerHelper serverManager;
	ServicesManager servicesManager;
	private static MockedStatic<DBPUtilitiesConstants> mockedStatic;
	private static MockedStatic<EnvironmentConfigurationsHandler> environmentConfiguration;
	private static MockedStatic<ContentManagementUtils> contentManagementUtils;
	private static MockedStatic<HelperMethods> helperMethods;
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPUtilitiesConstants.class);
		environmentConfiguration = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		contentManagementUtils = Mockito.mockStatic(ContentManagementUtils.class);
		helperMethods = Mockito.mockStatic(HelperMethods.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		environmentConfiguration.close();
		contentManagementUtils.close();
		helperMethods.close();
	}

	@Before // This will run before each test case
	public void setup() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[1];
		dcRequest = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		javaService2 = new GetCustomerTermsAndConditions();

	}

	@Test
	public void testInvoke() throws Exception {
		Result result = new Result();
		Result expectedResult = new Result();
		expectedResult.addOpstatusParam(0);
		expectedResult.addHttpStatusCodeParam(0);
		String termsAndConditionsCode = "Login_TnC";
		String leid = "GB0010001";
		String appId = "RETAIL_AND_BUSINESS_BANKING";
		String languageCode = "en-US";
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("languageCode", "en-US");
		inputParams.put("termsAndConditionsCode", "Login_TnC");
		inputParams.put("legalEntityId", "GB0010001");
		inputParams.put("lastLoginTime", "2023-11-16");
		inputParams.put("tNCRefreshPeriod", "30");
		inputParams.put("lastModifiedTSforTNCRefresh", "2023-11-07");

		String[] inputArray = { "languageCode:en-US", "termsAndConditionsCode:Login_TnC" };
		when(HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);
		
		when(ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode, leid, appId, languageCode))
				.thenReturn(expectedResult);
		result = (Result) javaService2.invoke(methodID, inputArray, dcRequest, response);
		assertEquals(expectedResult.getOpstatusParamValue(), result.getOpstatusParamValue());
		assertEquals(expectedResult.getHttpStatusCodeParamValue(), result.getHttpStatusCodeParamValue());
	}
	@Test
	public void testInvokeException() throws Exception {
		Result actualResult = new Result();
		Result expectedResult = new Result();
		expectedResult.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_10184.getErrorCodeAsString()));
		expectedResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_10001.getMessage()));
		
		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, response);
		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
		assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
	}

}

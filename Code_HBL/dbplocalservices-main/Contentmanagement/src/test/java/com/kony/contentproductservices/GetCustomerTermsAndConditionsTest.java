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

import com.kony.contentproductservices.utils.ContentManagementConstants;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class GetCustomerTermsAndConditionsTest {

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
	private static MockedStatic<LegalEntityUtil> legalEntityUtil;

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPUtilitiesConstants.class);
		environmentConfiguration = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		contentManagementUtils = Mockito.mockStatic(ContentManagementUtils.class);
		helperMethods = Mockito.mockStatic(HelperMethods.class);
		legalEntityUtil = Mockito.mockStatic(LegalEntityUtil.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		environmentConfiguration.close();
		contentManagementUtils.close();
		helperMethods.close();
		legalEntityUtil.close();
	}

	@Before // This will run before each test case
	public void setup() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[0];
		dcRequest = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		javaService2 = new GetCustomerTermsAndConditions();
	}

	@Test
	public void testInvoke() throws Exception {
		Result result = new Result();
		Result expectedResult = new Result();
		String customerId = "3961512056";
		when(HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn(customerId);
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("languageCode", "en-US");
		inputParams.put("termsAndConditionsCode", "Login_TnC");
		inputParams.put("legalEntityId", "GB0010001");
		inputParams.put("lastLoginTime", "2023-11-16");
		inputParams.put("tNCRefreshPeriod", "30");
		inputParams.put("lastModifiedTSforTNCRefresh", "2023-11-07");
		String appId = "RETAIL_AND_BUSINESS_BANKING";

		String[] inputArray = { "languageCode:en-US", "termsAndConditionsCode:Login_TnC" };
		when(HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);

		expectedResult.addOpstatusParam(0);
		expectedResult.addHttpStatusCodeParam(0);
		String termsAndConditionsCode = "Login_TnC";
		String leid = "GB0010001";
		String languageCode = "en-US";

		when(ContentManagementUtils.getTermsAndConditions(termsAndConditionsCode, leid, appId, languageCode))
				.thenReturn(expectedResult);
		String consentBackend = ContentManagementConstants.CONSENT_BACKEND_DBXDB;
		String CUSTOMER_TERMSANDCONDITIONS_GET = "customerTermsAndConditions.readRecord";
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND")).thenReturn(consentBackend);
		String filter = "customerId" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND
				+ "termsAndConditionsCode" + DBPUtilitiesConstants.EQUAL + termsAndConditionsCode
				+ DBPUtilitiesConstants.AND + "languageCode" + DBPUtilitiesConstants.EQUAL + languageCode;
		when(HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				CUSTOMER_TERMSANDCONDITIONS_GET)).thenReturn(expectedResult);
		when(LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest)).thenReturn("GB0010001");
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

	@Test
	public void throwErrorWhenTNCCodeBlank() throws Exception {
		Result result = new Result();
		String customerId = "3961512056";
		when(HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn(customerId);
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("languageCode", "en-US");
		inputParams.put("lastLoginTime", "2023-11-16");
		inputParams.put("tNCRefreshPeriod", "30");
		inputParams.put("lastModifiedTSforTNCRefresh", "2023-11-07");
		when(HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);
		
		result = (Result) javaService2.invoke(methodID, inputArray, dcRequest, response);
		assertEquals(String.valueOf(ErrorCodeEnum.ERR_10184.getErrorCode()),
				result.getParamByName("dbpErrCode").getValue());
	}
	
	@Test
	public void throwErrorWhenLegalEntityIdBlank() throws Exception {
		Result result = new Result();
		String customerId = "3961512056";
		when(HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn(customerId);
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("languageCode", "en-US");
		inputParams.put("lastLoginTime", "2023-11-16");
		inputParams.put("tNCRefreshPeriod", "30");
		inputParams.put("lastModifiedTSforTNCRefresh", "2023-11-07");
		inputParams.put("termsAndConditionsCode", "Login_TnC");
		when(HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);

		result = (Result) javaService2.invoke(methodID, inputArray, dcRequest, response);
		assertEquals(String.valueOf(ErrorCodeEnum.ERR_29040.getErrorCode()),
				result.getParamByName("dbpErrCode").getValue());
	}
	
}

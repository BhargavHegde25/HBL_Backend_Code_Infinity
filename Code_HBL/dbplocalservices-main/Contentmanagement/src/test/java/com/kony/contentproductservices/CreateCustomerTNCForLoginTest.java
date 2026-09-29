package com.kony.contentproductservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.constants.DBPConstants;
import com.kony.contentproductservices.utils.ContentManagementConstants;
import com.kony.contentproductservices.utils.ContentManagementUtils;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

@PrepareForTest({ HelperMethods.class, ContentManagementUtils.class, EnvironmentConfigurationsHandler.class,
		LegalEntityUtil.class })
public class CreateCustomerTNCForLoginTest {

	private DataControllerRequest dcRequest;
	private DataControllerResponse dcResponse;
	private String methodID = "methodID";
	private Object[] inputArray;
	JavaService2 javaService2;
	Result actualResult;

	private static MockedStatic<EnvironmentConfigurationsHandler> mockedEnvironmentConfigurationsHandler;
	private static MockedStatic<ContentManagementUtils> mockedContentManagementUtils;
	private static MockedStatic<HelperMethods> mockedHelperMethods;
	private static MockedStatic<LegalEntityUtil> mockedLegalEntityUtil;

	// This will run before the first test case
	@BeforeClass
	public static void init() {
		mockedEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		mockedContentManagementUtils = Mockito.mockStatic(ContentManagementUtils.class);
		mockedHelperMethods = Mockito.mockStatic(HelperMethods.class);
		mockedLegalEntityUtil = Mockito.mockStatic(LegalEntityUtil.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedEnvironmentConfigurationsHandler.close();
		mockedContentManagementUtils.close();
		mockedHelperMethods.close();
		mockedLegalEntityUtil.close();
	}

	@Before
	public void setup() throws Exception {
		inputArray = new Object[10];
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		javaService2 = new CreateCustomerTNCForLogin();
		actualResult = new Result();
	}

	@Test
	public void testInvoke() throws Exception {
		Map<String, String> inputParams = new HashMap<>();
		String reportingParams = "%7B%22os%22:%22119.0.0.0%22,%22dm%22:%22%22,%22did%22:%22AD256BE6-2A6F-44FF-A386-3FBD3B721FFC%22,%22ua%22:%22Mozilla/5.0%20(Windows%20NT%2010.0;%20Win64;%20x64)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/119.0.0.0%20Safari/537.36%22,%22aid%22:%22OnlineBanking%22,%22aname%22:%22OnlineBanking%22,%22chnl%22:%22desktop%22,%22plat%22:%22web%22,%22aver%22:%221.0.0%22,%22atype%22:%22spa%22,%22stype%22:%22b2c%22,%22kuid%22:%22%22,%22mfaid%22:%22442f261b-783d-4913-8152-1937f98e2f7b%22,%22mfbaseid%22:%22b7179128-7047-4c29-9e4d-68de57396dc5%22,%22mfaname%22:%22OnlineBanking_Composite%22,%22sdkversion%22:%22202310.0.0%22,%22sdktype%22:%22js%22,%22fid%22:%22frmLogin%22,%22sessiontype%22:%22I%22,%22clientUUID%22:%221700562502250-b649-2188-9c57%22,%22rsid%22:%221700562504974-5d89-4ef7-4874%22,%22svcid%22:%22login_DbxUserLogin%22%7D";
		inputParams.put("languageCode", "en-US");

		Result termsAndConditionsFromAdmin = new Result();
		Result termsAndConditions = mock(Result.class);
		termsAndConditionsFromAdmin.addParam("versionId", "2.0");
		termsAndConditionsFromAdmin.addParam("leid", "GB0010001");

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);
		mockedHelperMethods.when(() -> HelperMethods.getAppId(dcRequest)).thenReturn("RETAIL_AND_BUSINESS_BANKING");
		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);

		mockedLegalEntityUtil.when(() -> LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest))
				.thenReturn("GB0010001");

		mockedContentManagementUtils.when(
				() -> ContentManagementUtils.getTermsAndConditions(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(termsAndConditionsFromAdmin);
		mockedHelperMethods
				.when(() -> HelperMethods.callGetApi((DataControllerRequest) any(), anyString(), anyMap(), anyString()))
				.thenReturn(termsAndConditions);
		mockedHelperMethods.when(() -> HelperMethods.hasRecords((Result) any())).thenReturn(false);
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "companyLegalUnit"))
				.thenReturn("GB0010001");
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "versionId")).thenReturn("1.0");
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "id")).thenReturn("45654fei879");

		when(dcRequest.getHeader("X-Kony-ReportingParams")).thenReturn(reportingParams);
		mockedHelperMethods
				.when(() -> HelperMethods.callApi((DataControllerRequest) any(), anyMap(), anyMap(), anyString()))
				.thenReturn(new Result());

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(0, actualResult.getAllParams().size());
	}

	@Test
	public void testInvokeExceptionLEEmpty() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, ErrorCodeEnum.ERR_29040.getErrorCodeAsString()));
		expectedResult.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_29040.getMessage()));

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(new HashMap<>());

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testInvokeException() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, ErrorCodeEnum.ERR_10185.getErrorCodeAsString()));
		expectedResult.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_10185.getMessage()));

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(new HashMap<>());
		when(dcRequest.getParameter("legalEntityId")).thenReturn("GB0010001");
		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);

		mockedContentManagementUtils.when(
				() -> ContentManagementUtils.getTermsAndConditions(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(null);
		mockedHelperMethods.when(() -> HelperMethods.hasDBPErrorMSG(null)).thenReturn(true);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testInvokeExceptionVersionEmpty() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, ErrorCodeEnum.ERR_10187.getErrorCodeAsString()));
		expectedResult.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_10187.getMessage()));

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(new HashMap<>());
		when(dcRequest.getParameter("legalEntityId")).thenReturn("GB0010001");
		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);
		mockedContentManagementUtils.when(
				() -> ContentManagementUtils.getTermsAndConditions(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(mock(Result.class));

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testInvokeUpdateTnC() throws Exception {
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("languageCode", "en-US");
		Result termsAndConditionsFromAdmin = new Result();
		termsAndConditionsFromAdmin.addParam("versionId", "2.0");
		termsAndConditionsFromAdmin.addParam("leid", "GB0010001");
		String getTnCResponsePath = Paths
				.get(getClass().getClassLoader().getResource("GetCustomerTermsAndConditionsResponse.json").toURI())
				.toString();
		String getTnCContent = new String(Files.readAllBytes(Paths.get(getTnCResponsePath)));
		Result termsAndConditions = ConvertJsonToResult.convert(getTnCContent);

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);
		mockedHelperMethods.when(() -> HelperMethods.getAppId(dcRequest)).thenReturn("RETAIL_AND_BUSINESS_BANKING");
		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);
		mockedLegalEntityUtil.when(() -> LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest))
				.thenReturn("GB0010001");
		mockedContentManagementUtils.when(
				() -> ContentManagementUtils.getTermsAndConditions(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(termsAndConditionsFromAdmin);
		mockedHelperMethods
				.when(() -> HelperMethods.callGetApi((DataControllerRequest) any(), anyString(), anyMap(), anyString()))
				.thenReturn(termsAndConditions);
		mockedHelperMethods.when(() -> HelperMethods.hasRecords((Result) any())).thenReturn(true);
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "versionId")).thenReturn("1.0");
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "companyLegalUnit"))
				.thenReturn("GB0010001");
		mockedHelperMethods
				.when(() -> HelperMethods.callApi((DataControllerRequest) any(), anyMap(), anyMap(), anyString()))
				.thenReturn(new Result());

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(0, actualResult.getAllParams().size());
	}

	@Test
	public void testInvokeTnCUpdated() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, ErrorCodeEnum.ERR_10186.getErrorCodeAsString()));
		expectedResult.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_10186.getMessage()));
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("languageCode", "en-US");
		Result termsAndConditionsFromAdmin = new Result();
		termsAndConditionsFromAdmin.addParam("versionId", "1.0");
		termsAndConditionsFromAdmin.addParam("leid", "GB0010001");
		String getTnCResponsePath = Paths
				.get(getClass().getClassLoader().getResource("GetCustomerTermsAndConditionsResponse.json").toURI())
				.toString();
		String getTnCContent = new String(Files.readAllBytes(Paths.get(getTnCResponsePath)));
		Result termsAndConditions = ConvertJsonToResult.convert(getTnCContent);

		mockedHelperMethods.when(() -> HelperMethods.getCustomerIdFromSession(dcRequest)).thenReturn("2598100797");
		mockedHelperMethods.when(() -> HelperMethods.getInputParamMap(inputArray)).thenReturn(inputParams);
		mockedHelperMethods.when(() -> HelperMethods.getAppId(dcRequest)).thenReturn("RETAIL_AND_BUSINESS_BANKING");
		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);
		mockedLegalEntityUtil.when(() -> LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest))
				.thenReturn("GB0010001");
		mockedContentManagementUtils.when(
				() -> ContentManagementUtils.getTermsAndConditions(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(termsAndConditionsFromAdmin);
		mockedHelperMethods
				.when(() -> HelperMethods.callGetApi((DataControllerRequest) any(), anyString(), anyMap(), anyString()))
				.thenReturn(termsAndConditions);
		mockedHelperMethods.when(() -> HelperMethods.hasRecords((Result) any())).thenReturn(true);
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "versionId")).thenReturn("1.0");
		mockedHelperMethods.when(() -> HelperMethods.getFieldValue(termsAndConditions, "companyLegalUnit"))
				.thenReturn("GB0010001");

		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

}

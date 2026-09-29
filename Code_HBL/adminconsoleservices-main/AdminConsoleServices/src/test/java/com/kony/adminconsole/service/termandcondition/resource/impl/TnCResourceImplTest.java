package com.kony.adminconsole.service.termandcondition.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.AuthenticationC360;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.service.termandcondition.businessdelegate.api.TnCBusinessDelegate;
import com.kony.adminconsole.service.termandcondition.preprocessor.TNCTokenPreProcessor;
import com.kony.adminconsole.service.termandcondition.resource.api.TnCResource;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

@PrepareForTest({ EnvironmentConfigurationsHandler.class, DBPAPIAbstractFactoryImpl.class, TnCBusinessDelegate.class,
		TnCResourceImpl.class, EnvironmentConfiguration.class })
public class TnCResourceImplTest {

	private TnCResource tncResource;
	private TnCBusinessDelegate tncBusinessDelegate;
	private BusinessDelegateFactory businessDelegateFactory;
	private DBPAPIAbstractFactory dbpAPIAbstractFactory;
	private UserDetailsBean loggedInUserDetails;
	private DataControllerRequest dcRequest;
	private DataControllerResponse dataControllerResponse;
	private Log4j2Configurator log4j2Configurator;
	private String methodId = "methodId";
	Map<String, Object> req = new HashMap<String, Object>();
	private Object[] inputArray;
	TNCTokenPreProcessor authObj;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<AuthenticationC360> authenticationC360MockedStatic;
	private static MockedStatic<EnvironmentConfigurationsHandler> environmentConfigurationsHandlerMockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	private static MockedStatic<LoggedInUserHandler> loggedInUserHandlerMockedStatic;

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		environmentConfigurationsHandlerMockedStatic = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		authenticationC360MockedStatic = Mockito.mockStatic(AuthenticationC360.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
		loggedInUserHandlerMockedStatic = Mockito.mockStatic(LoggedInUserHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		environmentConfigurationsHandlerMockedStatic.close();
		authenticationC360MockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
		loggedInUserHandlerMockedStatic.close();
	}

	@Before
	public void setup() throws IOException {
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		tncBusinessDelegate = mock(TnCBusinessDelegate.class);
		inputArray = new Object[2];
		dcRequest = mock(DataControllerRequest.class);
		dataControllerResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(TnCBusinessDelegate.class)).thenReturn(tncBusinessDelegate);

	}

	@Test
	public void testGetAllTermsAndConditions() throws Exception {
		Result result = mock(Result.class);
		tncResource = new TnCResourceImpl();

		authObj = mock(TNCTokenPreProcessor.class);
		when(dcRequest.getParameter("legalEntityId")).thenReturn("GB0010001");
		when(dcRequest.getParameter("Authorization")).thenReturn("X-Kony-Authorization");

		when(authObj.execute(eq(null), eq(dcRequest), eq(dataControllerResponse), eq(result))).thenReturn(true);

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");

		authenticationC360MockedStatic.when(() -> AuthenticationC360.getAuthToken(eq(dcRequest))).thenReturn("TOKEN");

		Map<String, Object> postParametersMap = new HashMap<>();
		postParametersMap.put("legalEntityId", "GB0010001");
		boolean isDBXDBIntegrated = true;

		when(tncBusinessDelegate.getAllTermsAndConditions(eq(postParametersMap), anyString(), eq(dcRequest),
				eq(isDBXDBIntegrated))).thenReturn(result);

		Result actualResult = tncResource.getAllTermsAndConditions(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(0, actualResult.getAllParams().size());

	}

	@Test
	public void testEditTermsAndConditions() throws Exception {

		Result result = mock(Result.class);
		tncResource = new TnCResourceImpl();
		authObj = mock(TNCTokenPreProcessor.class);

		loggedInUserDetails = mock(UserDetailsBean.class);
		loggedInUserHandlerMockedStatic.when(() -> LoggedInUserHandler.getUserDetails(eq(dcRequest)))
				.thenReturn(loggedInUserDetails);

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");

		authenticationC360MockedStatic.when(() -> AuthenticationC360.getAuthToken(eq(dcRequest))).thenReturn("TOKEN");

		when(authObj.execute(eq(null), eq(dcRequest), eq(dataControllerResponse), eq(result))).thenReturn(true);

		when(loggedInUserDetails.getId()).thenReturn("987654");
		when(dcRequest.getParameter("termsAndConditionsCode")).thenReturn("Login_TnC");
		when(dcRequest.getParameter("languageCode")).thenReturn("en-US");
		when(dcRequest.getParameter("contentType")).thenReturn("TEXT");
		when(dcRequest.getParameter("termsAndConditionsTitle")).thenReturn("Login");
		when(dcRequest.getParameter("termsAndConditionsDescription")).thenReturn("Login Updated Description");
		when(dcRequest.getParameter("termsAndConditionsContent")).thenReturn("");
		when(dcRequest.getParameter("legalEntityId")).thenReturn("GB0010001");
		when(dcRequest.getParameter("Authorization")).thenReturn("X-Kony-Authorization");

		Map<String, Object> postParametersMap = new HashMap<>();
		postParametersMap.put("loggedInUserId", "987654");
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "TEXT");
		postParametersMap.put("termAndConditionTitle", "Login");
		postParametersMap.put("termAndConditionDescription", "Login Updated Description");
		postParametersMap.put("termAndConditionContent", "");
		postParametersMap.put("legalEntityId", "GB0010001");

		when(tncBusinessDelegate.editTermsAndConditionsDBXDB(eq(postParametersMap), anyString(),
				eq(loggedInUserDetails))).thenReturn(result);

		Result actualResult = tncResource.editTermsAndConditions(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(0, actualResult.getAllParams().size());

	}

	@Test
	public void testCreateTermsAndConditionsVersion() throws Exception {
		tncResource = new TnCResourceImpl();
		authObj = mock(TNCTokenPreProcessor.class);

		UserDetailsBean userDetailsBeanInstance = mock(UserDetailsBean.class);
		loggedInUserHandlerMockedStatic.when(() -> LoggedInUserHandler.getUserDetails(eq(dcRequest)))
				.thenReturn(userDetailsBeanInstance);
		when(dcRequest.getParameter("termsAndConditionsCode")).thenReturn("Login_TnC");
		when(dcRequest.getParameter("languageCode")).thenReturn("en-US");
		when(dcRequest.getParameter("contentType")).thenReturn("TEXT");
		when(dcRequest.getParameter("versionDescription")).thenReturn("Login Updated Description");
		when(dcRequest.getParameter("termsAndConditionsContent")).thenReturn("Login Updated Content");
		when(dcRequest.getParameter("isSave")).thenReturn("true");
		when(dcRequest.getParameter("legalEntityId")).thenReturn("GB0010001");
		when(dcRequest.getParameter("Authorization")).thenReturn("X-Kony-Authorization");
		when(userDetailsBeanInstance.getId()).thenReturn("987654");
		when(userDetailsBeanInstance.getUserName()).thenReturn("admin1");
		authenticationC360MockedStatic.when(() -> AuthenticationC360.getAuthToken(eq(dcRequest))).thenReturn("TOKEN");
		when(tncBusinessDelegate.createTermsAndConditionsVersion(anyMap(), anyString())).thenReturn(mock(Result.class));
		Result actualResult = tncResource.createTermsAndConditionsVersion(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(0, actualResult.getAllParams().size());
	}

	@Test
	public void testCreateTermsAndConditionsVersionTncCodeEmpty() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20266.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20266.getMessage()));
		tncResource = new TnCResourceImpl();
		when(tncBusinessDelegate.createTermsAndConditionsVersion(anyMap(), anyString())).thenReturn(mock(Result.class));
		Result actualResult = tncResource.createTermsAndConditionsVersion(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testCreateTermsAndConditionsVersionContentTypeEmpty() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20272.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20272.getMessage()));
		tncResource = new TnCResourceImpl();
		when(dcRequest.getParameter("termsAndConditionsCode")).thenReturn("Login_TnC");
		when(tncBusinessDelegate.createTermsAndConditionsVersion(anyMap(), anyString())).thenReturn(mock(Result.class));
		Result actualResult = tncResource.createTermsAndConditionsVersion(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testCreateTermsAndConditionsVersionTncContentEmpty() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20270.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20270.getMessage()));
		tncResource = new TnCResourceImpl();
		when(dcRequest.getParameter("termsAndConditionsCode")).thenReturn("Login_TnC");
		when(dcRequest.getParameter("contentType")).thenReturn("TEXT");
		when(tncBusinessDelegate.createTermsAndConditionsVersion(anyMap(), anyString())).thenReturn(mock(Result.class));
		Result actualResult = tncResource.createTermsAndConditionsVersion(methodId, inputArray, dcRequest,
				dataControllerResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testDeleteTermsAndConditions() throws Exception {
		Result mockResult = new Result();
		mockResult.addParam(new Param("status", "Success"));
		tncResource = new TnCResourceImpl();
		when(dcRequest.getParameter("termsAndConditionsCode")).thenReturn("OnlineBanking_Access_TnC");
		when(dcRequest.getParameter("languageCode")).thenReturn("en-US");
		when(tncBusinessDelegate.deleteTermsAndConditionsVersionDBXDB(anyMap())).thenReturn(mockResult);
		environmentConfigurationsHandlerMockedStatic
		.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
		.thenReturn(ACConstants.DBXDB_BACKEND);
		Result actualResult = tncResource.deleteTermsAndConditionsVersion(methodId, inputArray, dcRequest, dataControllerResponse);
		assertEquals("Success",
				actualResult.getParamByName("status").getValue());
	}
	
	@Test
	public void testDeleteTermsAndConditionsMissingPayload() throws Exception {
		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20266.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20266.getMessage()));
		tncResource = new TnCResourceImpl();
		when(dcRequest.getParameter("languageCode")).thenReturn("en-US");
		when(tncBusinessDelegate.deleteTermsAndConditionsVersionDBXDB(anyMap())).thenReturn(mock(Result.class));
		environmentConfigurationsHandlerMockedStatic
		.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
		.thenReturn(ACConstants.DBXDB_BACKEND);
		Result actualResult = tncResource.deleteTermsAndConditionsVersion(methodId, inputArray, dcRequest, dataControllerResponse);
		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}
}

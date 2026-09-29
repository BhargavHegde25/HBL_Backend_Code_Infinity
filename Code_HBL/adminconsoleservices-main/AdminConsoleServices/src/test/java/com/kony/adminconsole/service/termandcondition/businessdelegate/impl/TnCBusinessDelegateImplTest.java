package com.kony.adminconsole.service.termandcondition.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.service.termandcondition.businessdelegate.api.TnCBusinessDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

@PrepareForTest({ EnvironmentConfigurationsHandler.class, DBPAPIAbstractFactoryImpl.class, TnCBusinessDelegate.class })
public class TnCBusinessDelegateImplTest {

	private TnCBusinessDelegate tncBusinessDelegate;
	private DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	private DBPServiceExecutor dbpServiceExecutor;
	private UserDetailsBean loggedInUserDetails;
	DataControllerRequest request;
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStatic;
	private static MockedStatic<EnvironmentConfigurationsHandler> environmentConfigurationsHandlerMockedStatic;
	private static MockedStatic<LoggedInUserHandler> loggedInUserHandlerMockedStatic;
	private static MockedStatic<Executor> executorMockedStatic;

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		environmentConfigurationsHandlerMockedStatic = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		loggedInUserHandlerMockedStatic = Mockito.mockStatic(LoggedInUserHandler.class);
		executorMockedStatic = Mockito.mockStatic(Executor.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		environmentConfigurationsHandlerMockedStatic.close();
		loggedInUserHandlerMockedStatic.close();
		executorMockedStatic.close();
	}

	@Before
	public void setup() throws IOException {
		request = mock(DataControllerRequest.class);
		tncBusinessDelegate = new TnCBusinessDelegateImpl();
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
	}

	@Test
	public void testEditTermsAndConditionsDBXDB() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("termAndConditionTitle", "Login");
		postParametersMap.put("termAndConditionDescription", "Updated Description");
		loggedInUserDetails = mock(UserDetailsBean.class);
		when(loggedInUserDetails.getUserName()).thenReturn("admin1");

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");

		String leId = "GB0010001";
		String termAndConditionCode = "Login_TnC";

		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		inputBodyMap.put(ODataQueryConstants.FILTER,
				"Code eq '" + termAndConditionCode + "' and companyLegalUnit eq '" + leId + "'");
		inputBodyMap.put(ODataQueryConstants.SELECT, "id");

		String getTNCResPath = new File(
				getClass().getClassLoader().getResource("GetTermAndConditionResponse.json").getFile()).getPath();

		String getTNCRes = new String(Files.readAllBytes(Paths.get(getTNCResPath)));

		JSONObject readTermAndConditionResponseJSON = CommonUtilities.getStringAsJSONObject(getTNCRes);
		JSONArray readTermAndConditionJSONArray = readTermAndConditionResponseJSON.optJSONArray("termandcondition");
		JSONObject currTermAndConditionRecord = readTermAndConditionJSONArray.getJSONObject(0);
		String termAndConditionId = currTermAndConditionRecord.getString("id");

		String updateTNCResPath = new File(
				getClass().getClassLoader().getResource("UpdateTermAndConditionResponse.json").getFile()).getPath();

		String updateTNCRes = new String(Files.readAllBytes(Paths.get(updateTNCResPath)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);

		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);

		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);

		when(dbpServiceExecutorBuilder.withRequestParameters(inputBodyMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTNCRes);

		Map<String, Object> hm = new HashMap<String, Object>();
		hm.put("id", termAndConditionId);
		hm.put("Title", "Login");
		hm.put("Description", "Updated Description");
		hm.put("modifiedby", "admin1");
		hm.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);

		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_UPDATE))
				.thenReturn(dbpServiceExecutorBuilder);

		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(updateTNCRes);

		Result actualResult = tncBusinessDelegate.editTermsAndConditionsDBXDB(postParametersMap, "TOKEN",
				loggedInUserDetails);

		assertEquals("Success", actualResult.getParamValueByName("status"));

	}
	
	
	@Test
	public void testGetAllTermsAndConditions() throws Exception {

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");

		Map<String, String> inputBodyMap = new HashMap<String, String>();
		inputBodyMap.put(ODataQueryConstants.SELECT, "id, Name");
		String leId = "GB0010001";
		inputBodyMap.put(ODataQueryConstants.FILTER, "companyLegalUnit eq '" + leId + "'");

		String getAppResPath = new File(getClass().getClassLoader().getResource("GetAppReadResponse.json").getFile())
				.getPath();

		String getAppRes = new String(Files.readAllBytes(Paths.get(getAppResPath)));

		executorMockedStatic
				.when(() -> Executor.invokeService(ServiceURLEnum.APP_READ, inputBodyMap, null, request))
				.thenReturn(getAppRes);
		
		Map<String,String> hm = new HashMap<String,String>();
		hm.put(ODataQueryConstants.SELECT, "id, Name");
		
		String contentTypeResPath = new File(getClass().getClassLoader().getResource("GetContentTypeResponse.json").getFile())
				.getPath();

		String contentTypeRes = new String(Files.readAllBytes(Paths.get(contentTypeResPath)));
		
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.CONTENTTYPE_READ, hm, null, request))
		.thenReturn(contentTypeRes);
		
		String localeResPath = new File(getClass().getClassLoader().getResource("GetLocaleResponse.json").getFile())
				.getPath();

		String localeRes = new String(Files.readAllBytes(Paths.get(localeResPath)));
		Map<String,String> localePayload = new HashMap<String,String>();
		localePayload.put(ODataQueryConstants.SELECT, "Code, Language");
		
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.LOCALE_READ, localePayload, null, request))
		.thenReturn(localeRes);
		
		Map<String,String> termAndConditionPayload = new HashMap<String,String>();
		termAndConditionPayload.put(ODataQueryConstants.SELECT, "id, Code, Title, Description");
		termAndConditionPayload.put(ODataQueryConstants.FILTER, "companyLegalUnit eq '" + leId + "'");
		
		String getTNCResPath = new File(getClass().getClassLoader().getResource("GetTermAndConditionReadResponse.json").getFile())
				.getPath();

		String getTNCRes = new String(Files.readAllBytes(Paths.get(getTNCResPath)));
		
		
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.TERMANDCONDITION_READ, termAndConditionPayload, null, request))
		.thenReturn(getTNCRes);
		
		
		String getContentFromTNCResponsePath = new File(getClass().getClassLoader().getResource("GetContentFromTNCResponse.json").getFile())
				.getPath();

		String getContentFromTNCResponse = new String(Files.readAllBytes(Paths.get(getContentFromTNCResponsePath)));
		
		
		Map<String,String> tncTextPayload = new HashMap<String,String>();
		tncTextPayload.put(ODataQueryConstants.FILTER,
				"TermAndConditionId eq '"+ "1920004" + "' and LanguageCode eq '"
						+ "en-US" + "' and companyLegalUnit eq '" + leId
						+ "' and Status_id eq '" + "SID_TANDC_ACTIVE" + "'");
		tncTextPayload.put(ODataQueryConstants.SELECT, "Content");
		
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.TERMANDCONDITIONTEXT_READ, tncTextPayload, null, request))
		.thenReturn(getContentFromTNCResponse);
		
		
		Map<String,String> getTNCAppPayload = new HashMap<String,String>();
		getTNCAppPayload.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + "1920004"
				+ "' and companyLegalUnit eq '" + leId + "'");
		getTNCAppPayload.put(ODataQueryConstants.SELECT, "AppId");
		
		String getTermAndConditionAppResponsePath = new File(getClass().getClassLoader().getResource("GetTermAndConditionAppResponse.json").getFile())
				.getPath();

		String getTermAndConditionAppResponse = new String(Files.readAllBytes(Paths.get(getTermAndConditionAppResponsePath)));
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.TERMANDCONDITIONAPP_READ, getTNCAppPayload, null, request))
		.thenReturn(getTermAndConditionAppResponse);
		
		
		Map<String,String> getTNCTextPayload = new HashMap<String,String>();
		getTNCTextPayload.put(ODataQueryConstants.FILTER, "TermAndConditionId eq '" + "1920004"
				+ "' and companyLegalUnit eq '" + leId + "'");
		getTNCTextPayload.put(ODataQueryConstants.SELECT,
				"LanguageCode, Content, ContentType_id, ContentModifiedBy, ContentModifiedOn, Status_id, Version_Id, Description");
		
		String getTNCTextResPath = new File(getClass().getClassLoader().getResource("GetTermAndConditionTextReadResponse.json").getFile())
				.getPath();

		String getTNCTextRes = new String(Files.readAllBytes(Paths.get(getTNCTextResPath)));
		executorMockedStatic
		.when(() -> Executor.invokeService(ServiceURLEnum.TERMANDCONDITIONTEXT_READ, getTNCTextPayload, null, request))
		.thenReturn(getTNCTextRes);
		

		Result actualResult = tncBusinessDelegate.getAllTermsAndConditions(new HashMap<String, Object>(), "TOKEN",
				request, true);
		
		
		assertEquals("Success", actualResult.getParamValueByName("status"));

	}

	@Test
	public void testCreateTermsAndConditionsDBXDB() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "Text");
		postParametersMap.put("termAndConditionVersionDescription", "Login TnC Description");
		postParametersMap.put("termAndConditionContent", "Login TnC content");
		postParametersMap.put("isSave", "true");
		postParametersMap.put("legalEntityId", null);
		postParametersMap.put("loggedInUserDetailsName", "admin1");
		postParametersMap.put("loggedInUserId", "UID10");

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");
		String responsePath = Paths
				.get(getClass().getClassLoader().getResource("GetTermAndConditionResponse.json").toURI()).toString();
		String getTnCResponse = new String(Files.readAllBytes(Paths.get(responsePath)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTnCResponse);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);

		Result actualResult = tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, "TOKEN");

		assertEquals("Success", actualResult.getParamValueByName("status"));
	}

	@Test
	public void testCreateTermsAndConditionsDBXDBSave() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "Text");
		postParametersMap.put("termAndConditionVersionDescription", "Login TnC Description");
		postParametersMap.put("termAndConditionContent", "Login TnC content");
		postParametersMap.put("isSave", "true");
		postParametersMap.put("legalEntityId", null);
		postParametersMap.put("loggedInUserDetailsName", "admin1");
		postParametersMap.put("loggedInUserId", "UID10");

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");
		String responsePath = Paths
				.get(getClass().getClassLoader().getResource("GetTermAndConditionTextResponse.json").toURI())
				.toString();
		String getTnCTextResponse = new String(Files.readAllBytes(Paths.get(responsePath)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTnCTextResponse);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_UPDATE))
				.thenReturn(dbpServiceExecutorBuilder);

		Result actualResult = tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, "TOKEN");

		assertEquals("Success", actualResult.getParamValueByName("status"));
	}

	@Test
	public void testCreateTermsAndConditionsDBXDBException() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "Text");
		postParametersMap.put("termAndConditionVersionDescription", "Login TnC Description");
		postParametersMap.put("termAndConditionContent", "Login TnC content");
		postParametersMap.put("isSave", "true");
		postParametersMap.put("legalEntityId", null);
		postParametersMap.put("loggedInUserDetailsName", "admin1");
		postParametersMap.put("loggedInUserId", "UID10");

		Result expectedResult = new Result();
		expectedResult
				.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20264.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20264.getMessage()));

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(null);

		Result actualResult = tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, "TOKEN");
		assertEquals(expectedResult.getParamByName("dbpErrCode").getValue(),
				actualResult.getParamByName("dbpErrCode").getValue());
		assertEquals(expectedResult.getParamByName("dbpErrMsg").getValue(),
				actualResult.getParamByName("dbpErrMsg").getValue());
	}

	@Test
	public void testCreateTermsAndConditionsDBXDBPublishCase1() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "Text");
		postParametersMap.put("termAndConditionVersionDescription", "Login TnC Description");
		postParametersMap.put("termAndConditionContent", "Login TnC content");
		postParametersMap.put("isSave", "false");
		postParametersMap.put("legalEntityId", null);
		postParametersMap.put("loggedInUserDetailsName", "admin1");
		postParametersMap.put("loggedInUserId", "UID10");

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");
		String responsePath = Paths
				.get(getClass().getClassLoader().getResource("GetTermAndConditionResponse.json").toURI()).toString();
		String getTnCResponse = new String(Files.readAllBytes(Paths.get(responsePath)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTnCResponse);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_UPDATE))
				.thenReturn(dbpServiceExecutorBuilder);

		Result actualResult = tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, "TOKEN");

		assertEquals(0, actualResult.getAllParams().size());
	}

	@Test
	public void testCreateTermsAndConditionsDBXDBPublishCase2() throws Exception {
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		postParametersMap.put("contentType", "Text");
		postParametersMap.put("termAndConditionVersionDescription", "Login TnC Description");
		postParametersMap.put("termAndConditionContent", "Login TnC content");
		postParametersMap.put("isSave", "false");
		postParametersMap.put("legalEntityId", null);
		postParametersMap.put("loggedInUserDetailsName", "admin1");
		postParametersMap.put("loggedInUserId", "UID10");

		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
				.thenReturn("GB0010001");
		String responsePath = Paths
				.get(getClass().getClassLoader().getResource("GetTermAndConditionTextResponse.json").toURI())
				.toString();
		String getTnCTextResponse = new String(Files.readAllBytes(Paths.get(responsePath)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTnCTextResponse);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_UPDATE))
				.thenReturn(dbpServiceExecutorBuilder);

		Result actualResult = tncBusinessDelegate.createTermsAndConditionsVersion(postParametersMap, "TOKEN");

		assertEquals("Success", actualResult.getParamValueByName("status"));
	}

	@Test
	public void testDeleteTermsAndConditionsDBXDB() throws Exception {//testCreateTermsAndConditionsDBXDB
		Map<String, Object> postParametersMap = new HashMap<String, Object>();
		postParametersMap.put("termAndConditionCode", "Login_TnC");
		postParametersMap.put("languageCode", "en-US");
		environmentConfigurationsHandlerMockedStatic
		.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
		.thenReturn(ACConstants.DBXDB_BACKEND);
		environmentConfigurationsHandlerMockedStatic
		.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("BRANCH_ID_REFERENCE"))
		.thenReturn("GB0010001");
		
		String tncTextResponse = Paths
				.get(getClass().getClassLoader().getResource("GetTermAndConditionTextResponse.json").toURI())
				.toString();
		String getTnCTextResponse = new String(Files.readAllBytes(Paths.get(tncTextResponse)));

		mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNC_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getTnCTextResponse);
		
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_GET))
		.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_TNCTEXT_DELETE))
		.thenReturn(dbpServiceExecutorBuilder);
		
		Result actualResult = tncBusinessDelegate.deleteTermsAndConditionsVersionDBXDB(postParametersMap);
		assertEquals("Success", actualResult.getParamValueByName("status"));
		
	}
}

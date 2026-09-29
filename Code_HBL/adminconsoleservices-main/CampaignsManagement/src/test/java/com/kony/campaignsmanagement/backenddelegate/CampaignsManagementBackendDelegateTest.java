package com.kony.campaignsmanagement.backenddelegate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.backenddelegate.impl.CampaignsManagementBackendDelegateImpl;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

public class CampaignsManagementBackendDelegateTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	CampaignsManagementBackendDelegate campaignsManagementBackendDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	Result actualResult;
	JSONObject actualJSONObject;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;
	String pCondition = "[{\"profileConditionId\": \"PC1046746195\",\"conditionExpression\": \"dvdsfdd\",\"dataContextId\": \"DC001\"}]";

	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPServiceExecutorBuilder.close();
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}

	@Before
	public void executedBefore() throws Exception {
		actualResult = new Result();
		actualJSONObject = new JSONObject();
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		campaignsManagementBackendDelegate = mock(CampaignsManagementBackendDelegate.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);

		logger = mock(Logger.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);

		postParametersMap = new HashMap<>();
		headerMap = new HashMap<>();
		headerMap.put("Authorization", "dummyToken");
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(CampaignsManagementBackendDelegate.class))
				.thenReturn(campaignsManagementBackendDelegate);

	}

	@Test
	public void testGetAllPlaceHoldersDB() throws Exception {

		String getResponseFile = new File(getClass().getClassLoader().getResource("get_placeholders_db.json").getFile())
				.getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJSON = new JSONObject(getResponseContent);
		JSONArray expectedResult = expectedJSON.optJSONArray(CMConstants.PLACEHOLDER_DBXDB);
		JSONArray actualResult = new JSONArray();

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_PLACEHOLDER_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getAllPlaceHolders(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("placeholderId"),
				expectedResult.getJSONObject(0).getString("placeholderId"));
	}

	@Test
	public void testGetAllPlaceHoldersMS() throws Exception {

		String getResponseFile = new File(getClass().getClassLoader().getResource("get_placeholders_ms.json").getFile())
				.getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJSON = new JSONObject(getResponseContent);
		JSONArray expectedResult = expectedJSON.optJSONArray(CMConstants.PLACEHOLDERS_MS);
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		
		JSONArray actualResult = new JSONArray();

		when(dcRequest.getParameter("Authorization")).thenReturn("dummyToken");
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CAMPAIGNSMS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.GET_PLACEHOLDERS))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputBodyMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getAllPlaceHoldersMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("placeholderId"),
				expectedResult.getJSONObject(0).getString("placeholderId"));
	}

	@Test
	public void testGetAllEventTriggersDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_eventtriggers_db.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJSON = new JSONObject(getResponseContent);
		JSONArray expectedResult = expectedJSON.optJSONArray(CMConstants.EVENTTRIGGERS_DBXDB);

		JSONArray actualResult = new JSONArray();

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_EVENTTRIGGERS_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getEventTriggers(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("eventTriggerId"),
				expectedResult.getJSONObject(0).getString("eventTriggerId"));
	}

	@Test
	public void testGetAllEventTriggersMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_eventtriggers_ms.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJSON = new JSONObject(getResponseContent);
		JSONArray expectedResult = expectedJSON.optJSONArray("eventTrigger");
		
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();

		JSONArray actualResult = new JSONArray();
		when(dcRequest.getParameter("Authorization")).thenReturn("dummyToken");
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CAMPAIGNSMS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.GET_EVENTS))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputBodyMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getEventTriggersMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("eventTriggerId"),
				expectedResult.getJSONObject(0).getString("eventTriggerId"));
	}

	//@Test
	public void testCreateProfileDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		JSONObject actualResult = new JSONObject();
		
		Map<String,Object> requestParametersMap = new HashMap<String, Object>();
        requestParametersMap.put(CMConstants.PROFILE_ID, "PRF100");
        requestParametersMap.put(CMConstants.PROFILE_NAME, "Test profile");
        requestParametersMap.put(CMConstants.PROFILE_DESC, "Test profile");
        requestParametersMap.put(CMConstants.PROFILE_STATUS, CMConstants.ACTIVE_STATUS);
        requestParametersMap.put("numberOfUsers",0);
        requestParametersMap.put("profileCreationDate", CommonUtilities.getISOFormattedLocalTimestamp());
        requestParametersMap.put("profileDeactivatedDate", null);
		
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn("Test profile");
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn("Test profile");
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(pCondition);
		when(dcRequest.getParameter(CMConstants.PROFILE_ID)).thenReturn("PRF100");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_PROFILE_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_PROFILECONDITION_CREATE))
		.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.createProfileInDBXDB(dcRequest);

		assertEquals(actualResult.getString("id"), expectedResult.getString("id"));
	}

	//@Test
	public void testCreateProfileMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		JSONObject actualResult = new JSONObject();
		
		Map<String,Object> requestParametersMap = new HashMap<String, Object>();
		
        requestParametersMap.put(CMConstants.PROFILE_NAME, "Test profile");
        requestParametersMap.put(CMConstants.PROFILE_DESC, "Test profile");
        requestParametersMap.put(CMConstants.PROFILE_CONDITION, pCondition);
        
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn("Test profile");
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn("Test profile");
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(pCondition);
		when(dcRequest.getParameter("Authorization")).thenReturn("dummyToken");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CAMPAIGNSMS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.OP_CREATE_PROFILE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.createProfileInMS(dcRequest);

		assertEquals(actualResult.getString("id"), expectedResult.getString("id"));
	}

	//@Test
	public void testCreateProfileConditionDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);
		String conditions = pCondition;
		JSONObject actualResult = new JSONObject();
		
		Map<String,Object> requestParametersMap = new HashMap<String, Object>();
        requestParametersMap.put(CMConstants.PROFILE_ID, "PRF100");
        requestParametersMap.put(CMConstants.DATACONTEXT_ID, "DC002");
        requestParametersMap.put(CMConstants.PROFILE_CONDITION_ID, "PC001");
        requestParametersMap.put(CMConstants.CONDITION_EXPRESSION, "EstMonthlyExpense%20gt%20%271000%27");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_PROFILECONDITION_CREATE))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.createProfileConditionInDBXDB(dcRequest,conditions,"PRF100");

		assertEquals(actualResult.getString("id"), expectedResult.getString("id"));
	}
	
	
	@Test
	public void testGetProfilesDBXDB() throws Exception {

		String getResponseFile = new File(getClass().getClassLoader().getResource("get_profiles_proc_response.json").getFile())
				.getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJSON = new JSONObject(getResponseContent);
		//JSONArray expectedResult = expectedJSON.optJSONArray(CMConstants.ALL_PROFILES);
		JSONObject actualResult = new JSONObject();

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_FETCH_PROFILES_PROC))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getProfilesDBXDB
				(dcRequest);

		assertEquals(actualResult.getJSONArray("records").getJSONObject(0).getString("profileId"),
				expectedJSON.getJSONArray("records").getJSONObject(0).getString("profileId"));
	}

	@Test
	public void testGetProfilesMS() throws Exception {

		String getResponseFile = new File(getClass().getClassLoader().getResource("get_all_profiles_response.json").getFile())
				.getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		JSONArray expectedResult = expectedJson.getJSONArray(CMConstants.ALL_PROFILES);
		Map<String, Object> inputBodyMap = new HashMap<String, Object>();
		
		JSONArray actualResult = new JSONArray();

		when(dcRequest.getParameter("Authorization")).thenReturn("dummyToken");
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CAMPAIGNSMS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.OP_GET_PROFILES)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputBodyMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getProfilesMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("profileId"),
				expectedResult.getJSONObject(0).getString("profileId"));
	}

	@Test
	public void testUpdateProfileDB() throws Exception {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_PROFILE_UPDATE;
		String operationName1 = OperationName.DB_PROFILECONDITION_UPDATE;
		String operationName2 = OperationName.DB_PROFILECONDITION_CREATE;
		
		Map<String,Object> requestParametersMap = new HashMap<String, Object>();
        requestParametersMap.put(CMConstants.PROFILE_ID, "PRF0011");
        requestParametersMap.put(CMConstants.PROFILE_NAME, "ABCDEF");
        requestParametersMap.put(CMConstants.PROFILE_DESC, "ABCDEF");
        requestParametersMap.put(CMConstants.PROFILE_CONDITION, pCondition);
        requestParametersMap.put(CMConstants.PROFILE_STATUS, "Active");
        
        Map<String,Object> requestParametersMap1 = new HashMap<String, Object>();
        requestParametersMap1.put(CMConstants.PROFILE_ID, "PRF0011");
        requestParametersMap1.put(CMConstants.DATACONTEXT_ID, "DC001");
        requestParametersMap1.put(CMConstants.CONDITION_EXPRESSION, "dvdsfdd");
        requestParametersMap1.put(CMConstants.PROFILE_CONDITION_ID, "PC1046746195");
        

		String getPayloadFile = new File(
				getClass().getClassLoader().getResource("update_profile_payload.json").getFile()).getPath();

		String getPayloadContent = new String(Files.readAllBytes(Paths.get(getPayloadFile)));
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);
		JSONObject profile = new JSONObject(getPayloadContent).optJSONObject("profile");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName1))
		.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName2))
		.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap1)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		
		 CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		 JSONObject actualResult = campaignsManagementBackendDelegateImpl.updateProfile(profile, dcRequest);
		 
		 assertEquals(actualResult.getString("profileId"), expectedResult.getString("profileId"));
	}

	//@Test
	public void testUpdateProfileMS() throws Exception {
		String serviceName = ServiceId.CAMPAIGNSMS;
		String operationName = OperationName.UPDATE_PROFILE;
		String getPayloadFile = new File(
				getClass().getClassLoader().getResource("update_profile_payload.json").getFile()).getPath();

		String getPayloadContent = new String(Files.readAllBytes(Paths.get(getPayloadFile)));
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject profile = new JSONObject(getPayloadContent).optJSONObject("profile");
		JSONObject expectedResult = new JSONObject(getResponseContent);

		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(CMConstants.AUTHORIZATION, "dummyToken");
		Map<String,Object> requestParametersMap = new HashMap<String, Object>();
        requestParametersMap.put(CMConstants.PROFILE_ID, "PRF0011");
        requestParametersMap.put(CMConstants.PROFILE_NAME, "ABCDEF");
        requestParametersMap.put(CMConstants.PROFILE_DESC, "ABCDEF");
        requestParametersMap.put(CMConstants.PROFILE_CONDITION, pCondition);
        requestParametersMap.put(CMConstants.PROFILE_STATUS, "Active");
        
		when(dcRequest.getParameter(CMConstants.AUTHORIZATION)).thenReturn("dummyToken");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		 JSONObject actualResult = campaignsManagementBackendDelegateImpl.updateProfileMS(dcRequest, profile);
		 
		 assertEquals(actualResult.getString("profileId"), expectedResult.getString("profileId"));
	}
	
	@Test
	public void testGetProfileConditionsDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profile_conditions_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONObject(getResponseContent).optJSONArray("profilecondition");
		String profileId = "PRF0011";
		JSONArray actualResult = new JSONArray();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(CMConstants.SELECT, CMConstants.PROFILE_CONDITION_ID);
		inputMap.put(CMConstants.FILTER, "profileId eq " + profileId);
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_PROFILECONDITION_GET))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getProfileConditionsDB(profileId);

		assertEquals(actualResult.getJSONObject(0).getString("profileConditionId"),
				expectedResult.getJSONObject(0).getString("profileConditionId"));
	}
}

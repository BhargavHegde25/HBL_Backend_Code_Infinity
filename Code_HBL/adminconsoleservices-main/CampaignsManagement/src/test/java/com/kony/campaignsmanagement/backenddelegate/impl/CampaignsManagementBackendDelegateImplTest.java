package com.kony.campaignsmanagement.backenddelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.skyscreamer.jsonassert.JSONAssert;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;

//import flexjson.JSON;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class})
public class CampaignsManagementBackendDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	CampaignsManagementBackendDelegate campaignsManagementBackendDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	JSONObject actualJSONObject;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	EnvironmentConfigurationsHandler environmentConfigurationsHandler;
	DBPServiceExecutor dbpServiceExecutor;
	CommonUtilities commonUtilities;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;
	ServicesManager serviceManager;
	IdentityHandler identityHandler;
	
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static MockedStatic<DBPServiceInvocationWrapper> mockedstaticForDBPServiceInvocationWrapper;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedCommonUtilities;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedEnvironmentConfigurationsHandler;
	
	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		mockedstaticForDBPServiceInvocationWrapper = Mockito.mockStatic(DBPServiceInvocationWrapper.class);
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPServiceExecutorBuilder.close();
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedCommonUtilities.close();
		mockedEnvironmentConfigurationsHandler.close();
		mockedstaticForDBPServiceInvocationWrapper.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		actualJSONObject = new JSONObject();
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		campaignsManagementBackendDelegate = mock(CampaignsManagementBackendDelegate.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		commonUtilities = mock(CommonUtilities.class);
		environmentConfigurationsHandler = mock(EnvironmentConfigurationsHandler.class);
		logger = mock(Logger.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);

		postParametersMap = new HashMap<>();
		headerMap = new HashMap<>();

		serviceManager =  mock(ServicesManager.class);
		identityHandler = mock(IdentityHandler.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(CampaignsManagementBackendDelegate.class))
				.thenReturn(campaignsManagementBackendDelegate);
		
	}
	
	@Test
	public void testCreateCampaignDBXDB() throws Exception{
		
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("DBXDB");
		JSONObject actualResult = new JSONObject();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
				OperationName.DB_CREATE_CAMPAIGN_PROC,postParametersMap , headerMap, "")).thenReturn("{\"opstatus\":\"true\"}");
		
		when(CommonUtilities.getStringAsJSONObject("{\"opstatus\":\"true\"}")).thenReturn(expectedResult);
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.createCampaign(postParametersMap,headerMap);
		
		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));
	}
	
	@Test
	public void testCreateCampaignMS() throws Exception{
		
	
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);
		
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		JSONObject actualResult = new JSONObject();
		Map<String, Object> inputMap = new HashMap<>();
		headerMap.put("x-kony-authorization","token");
		when(CommonUtilities.getStringAsJSONObject("{\"opstatus\":\"true\"}")).thenReturn(expectedResult);
		Map<String, String> inputMapStr = new HashMap<>();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.CREATE_CAMPAIGN, postParametersMap, null, "token")).thenReturn("{\"opstatus\":\"true\"}");
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.createCampaign(inputMap,headerMap);
		
		assertEquals(actualResult.get(CMConstants.OPSTATUS), "true");
	}
	
	@Test
	public void testUpdateCampaignDBXDB() throws Exception{
		
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("DBXDB");
		JSONObject actualResult = new JSONObject();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
				OperationName.DB_UPDATE_CAMPAIGN_PROC,postParametersMap , headerMap, "")).thenReturn("{\"opstatus\":\"true\"}");
		
		when(CommonUtilities.getStringAsJSONObject("{\"opstatus\":\"true\"}")).thenReturn(expectedResult);
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.updateCampaign(postParametersMap,headerMap);
		
		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));
	}
	
	@Test
	public void testUpdateCampaignMS() throws Exception{
		
	
		JSONObject expectedResult = new JSONObject();
		//expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);
		
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		JSONObject actualResult = new JSONObject();
		Map<String, Object> inputMap = new HashMap<>();
		headerMap.put("x-kony-authorization","token");
		when(CommonUtilities.getStringAsJSONObject(null)).thenReturn(expectedResult);
		Map<String, String> inputMapStr = new HashMap<>();
//		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.UPDATE_CAMPAIGN, postParametersMap, null, "token")).thenReturn("{\"opstatus\":\"true\"}");
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.updateCampaign(inputMap,headerMap);
		
		assertEquals(expectedResult.length(),actualResult.length());
	}
	@Test
	public void testUpdateCampaignException() throws Exception{
		
	
		JSONObject expectedResult = new JSONObject();
		
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		JSONObject actualResult = new JSONObject();
		Map<String, Object> inputMap = new HashMap<>();
		headerMap.put("x-kony-authorization","token");
		when(CommonUtilities.getStringAsJSONObject("{\"opstatus\":\"true\"}")).thenReturn(expectedResult);
		Map<String, String> inputMapStr = new HashMap<>();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.UPDATE_CAMPAIGN, postParametersMap, null, "token")).thenThrow(new NullPointerException());
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.updateCampaign(inputMap,headerMap);
		
		assertEquals(expectedResult.length(),actualResult.length());
	}
	
	
	@Test
	public void testGetCampaignsDBXDB() throws Exception{
		
		String response = "{\r\n"
				+ "    \"records\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignType\": \"SINGLE\",\r\n"
				+ "            \"productId\": \"PR23046IVA5A\",\r\n"
				+ "            \"campaignPriority\": \"25\",\r\n"
				+ "            \"endDate\": \"2024-02-29T00:00\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"productGroupId\": \"REWARD.GROUP\",\r\n"
				+ "            \"campaignStatus\": \"SCHEDULED_ACTIVE_COMPLETED\",\r\n"
				+ "            \"campaignDescription\": \"Sridhar AD\",\r\n"
				+ "            \"campaignName\": \"Sridhar AD\",\r\n"
				+ "            \"objectiveType\": \"Marketing\",\r\n"
				+ "            \"startDate\": \"2024-02-08T00:00\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records6\": [\r\n"
				+ "        {\r\n"
				+ "            \"placeholderId\": \"PH007\",\r\n"
				+ "            \"bannerTitle\": \"\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"imageURL\": \"https://retailbanking1.konycloud.com/dbimages/campaign-mobile-prelogin-1x-1.jpg\",\r\n"
				+ "            \"callToActionButtonLabel\": \"\",\r\n"
				+ "            \"callToActionTargetURL\": \"\",\r\n"
				+ "            \"showCloseIcon\": \"\",\r\n"
				+ "            \"bannerDescription\": \"\",\r\n"
				+ "            \"targetURL\": \"https://retailbanking1.konycloud.com/dbimages/campaign-mobile-prelogin-1x-1.jpg\",\r\n"
				+ "            \"onlineContentId\": \"OC0718122611\",\r\n"
				+ "            \"showReadLaterButton\": \"\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records7\": [\r\n"
				+ "        {\r\n"
				+ "            \"profileName\": \"TestProfile\",\r\n"
				+ "            \"profileCreationDate\": \"2023-07-24T00:00\",\r\n"
				+ "            \"profileDeactivatedDate\": \"2028-07-24T00:00\",\r\n"
				+ "            \"profileDescription\": \"TestProfile\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\",\r\n"
				+ "            \"numberOfUsers\": \"0\",\r\n"
				+ "            \"profileStatus\": \"ACTIVE\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records8\": [\r\n"
				+ "        {\r\n"
				+ "            \"conditionExpression\": \"expression1\",\r\n"
				+ "            \"dataContextId\": \"DC002\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\",\r\n"
				+ "            \"profileConditionId\": \"COndition1\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records9\": [\r\n"
				+ "        {\r\n"
				+ "            \"placeholderIdentifier\": \"PRELOGIN\",\r\n"
				+ "            \"placeholderId\": \"PH007\",\r\n"
				+ "            \"application\": \"RETAIL BANKING\",\r\n"
				+ "            \"placeholderDescription\": \"Sample place holder description\",\r\n"
				+ "            \"placeholderName\": \"Homepage\",\r\n"
				+ "            \"imageSize\": \"51200\",\r\n"
				+ "            \"channelSubType\": \"MOBILE\",\r\n"
				+ "            \"imageResolution\": \"375x106\",\r\n"
				+ "            \"imageScale\": \"1x\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records2\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records3\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelType\": \"OFFLINE\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelType\": \"ONLINE\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records4\": [\r\n"
				+ "        \r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelPriority\": \"2\",\r\n"
				+ "            \"channelSubType\": \"SMS\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelPriority\": \"2\",\r\n"
				+ "            \"channelSubType\": \"WEB\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records5\": [\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT0624822610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"\",\r\n"
				+ "            \"channelSubType\": \"PUSH NOTIFICATIONS\",\r\n"
				+ "            \"content\": \"UHVzaA==\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT1226422610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"\",\r\n"
				+ "            \"channelSubType\": \"SMS\",\r\n"
				+ "            \"content\": \"U01T\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT4616922610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"RU1haWw=\",\r\n"
				+ "            \"channelSubType\": \"EMAIL\",\r\n"
				+ "            \"content\": \"RW1haWw=\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records1\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"eventTriggerId\": \"ET002\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"opstatus\": 0,\r\n"
				+ "    \"records10\": [\r\n"
				+ "        {\r\n"
				+ "            \"eventCode\": \"OVERDRAWNACCOUNT\",\r\n"
				+ "            \"eventTriggerId\": \"ET002\",\r\n"
				+ "            \"eventTriggerType\": \"External\",\r\n"
				+ "            \"eventDescription\": \"The account in consideration is overdrawn\",\r\n"
				+ "            \"eventSource\": \"Transact/DS\",\r\n"
				+ "            \"eventName\": \"OverdrawnAccount\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records11\": [\r\n"
				+ "        {\r\n"
				+ "            \"dataContextName\": \"Customer Attrition Model flow\",\r\n"
				+ "            \"dataContextId\": \"DC002\",\r\n"
				+ "            \"dataContextEndPoints\": \"http://40.127.187.34/APIServices/odata/Tenant1/XAIAttritionResultsDetailed\",\r\n"
				+ "            \"dataContextSource\": \"Analytics\",\r\n"
				+ "            \"dataContextDescription\": \"DataContext created for Customer Attrition Model flow Profiles\",\r\n"
				+ "            \"dataContextServiceName\": \"Dataset_AllCustomersAttrition\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"httpStatusCode\": 0\r\n"
				+ "}";
		
		JSONObject expectedResult = new JSONObject();
		JSONObject respObj =  new JSONObject(response);// CommonUtilities.getStringAsJSONObject(response);
		expectedResult.put(CMConstants.OPSTATUS, 0);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("DBXDB");
		JSONObject actualResult = new JSONObject();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CRUDLAYER, null,
				OperationName.DB_GET_CAMPAIGNS_PROC,postParametersMap , headerMap, "")).thenReturn("Resp");
		
		when(CommonUtilities.getStringAsJSONObject("Resp")).thenReturn(respObj);
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getCampaigns(postParametersMap,headerMap);
		
		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));
	}
	
	@Test
	public void testGetCampaignsMS() throws Exception{
		
		String response = "{\r\n"
				+ "    \"records\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignType\": \"SINGLE\",\r\n"
				+ "            \"productId\": \"PR23046IVA5A\",\r\n"
				+ "            \"campaignPriority\": \"25\",\r\n"
				+ "            \"endDate\": \"2024-02-29T00:00\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"productGroupId\": \"REWARD.GROUP\",\r\n"
				+ "            \"campaignStatus\": \"SCHEDULED_ACTIVE_COMPLETED\",\r\n"
				+ "            \"campaignDescription\": \"Sridhar AD\",\r\n"
				+ "            \"campaignName\": \"Sridhar AD\",\r\n"
				+ "            \"objectiveType\": \"Marketing\",\r\n"
				+ "            \"startDate\": \"2024-02-08T00:00\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records6\": [\r\n"
				+ "        {\r\n"
				+ "            \"placeholderId\": \"PH007\",\r\n"
				+ "            \"bannerTitle\": \"\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"imageURL\": \"https://retailbanking1.konycloud.com/dbimages/campaign-mobile-prelogin-1x-1.jpg\",\r\n"
				+ "            \"callToActionButtonLabel\": \"\",\r\n"
				+ "            \"callToActionTargetURL\": \"\",\r\n"
				+ "            \"showCloseIcon\": \"\",\r\n"
				+ "            \"bannerDescription\": \"\",\r\n"
				+ "            \"targetURL\": \"https://retailbanking1.konycloud.com/dbimages/campaign-mobile-prelogin-1x-1.jpg\",\r\n"
				+ "            \"onlineContentId\": \"OC0718122611\",\r\n"
				+ "            \"showReadLaterButton\": \"\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records7\": [\r\n"
				+ "        {\r\n"
				+ "            \"profileName\": \"TestProfile\",\r\n"
				+ "            \"profileCreationDate\": \"2023-07-24T00:00\",\r\n"
				+ "            \"profileDeactivatedDate\": \"2028-07-24T00:00\",\r\n"
				+ "            \"profileDescription\": \"TestProfile\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\",\r\n"
				+ "            \"numberOfUsers\": \"0\",\r\n"
				+ "            \"profileStatus\": \"ACTIVE\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records8\": [\r\n"
				+ "        {\r\n"
				+ "            \"conditionExpression\": \"expression1\",\r\n"
				+ "            \"dataContextId\": \"DC002\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\",\r\n"
				+ "            \"profileConditionId\": \"COndition1\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records9\": [\r\n"
				+ "        {\r\n"
				+ "            \"placeholderIdentifier\": \"PRELOGIN\",\r\n"
				+ "            \"placeholderId\": \"PH007\",\r\n"
				+ "            \"application\": \"RETAIL BANKING\",\r\n"
				+ "            \"placeholderDescription\": \"Sample place holder description\",\r\n"
				+ "            \"placeholderName\": \"Homepage\",\r\n"
				+ "            \"imageSize\": \"51200\",\r\n"
				+ "            \"channelSubType\": \"MOBILE\",\r\n"
				+ "            \"imageResolution\": \"375x106\",\r\n"
				+ "            \"imageScale\": \"1x\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records2\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"profileId\": \"PRF2320537993\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records3\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelType\": \"OFFLINE\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelType\": \"ONLINE\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records4\": [\r\n"
				+ "        \r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelPriority\": \"2\",\r\n"
				+ "            \"channelSubType\": \"SMS\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"channelPriority\": \"2\",\r\n"
				+ "            \"channelSubType\": \"WEB\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records5\": [\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT0624822610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"\",\r\n"
				+ "            \"channelSubType\": \"PUSH NOTIFICATIONS\",\r\n"
				+ "            \"content\": \"UHVzaA==\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT1226422610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"\",\r\n"
				+ "            \"channelSubType\": \"SMS\",\r\n"
				+ "            \"content\": \"U01T\"\r\n"
				+ "        },\r\n"
				+ "        {\r\n"
				+ "            \"offlineTemplateId\": \"OT4616922610\",\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"subject\": \"RU1haWw=\",\r\n"
				+ "            \"channelSubType\": \"EMAIL\",\r\n"
				+ "            \"content\": \"RW1haWw=\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records1\": [\r\n"
				+ "        {\r\n"
				+ "            \"campaignId\": \"CP5812556032\",\r\n"
				+ "            \"eventTriggerId\": \"ET002\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"opstatus\": 0,\r\n"
				+ "    \"records10\": [\r\n"
				+ "        {\r\n"
				+ "            \"eventCode\": \"OVERDRAWNACCOUNT\",\r\n"
				+ "            \"eventTriggerId\": \"ET002\",\r\n"
				+ "            \"eventTriggerType\": \"External\",\r\n"
				+ "            \"eventDescription\": \"The account in consideration is overdrawn\",\r\n"
				+ "            \"eventSource\": \"Transact/DS\",\r\n"
				+ "            \"eventName\": \"OverdrawnAccount\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"records11\": [\r\n"
				+ "        {\r\n"
				+ "            \"dataContextName\": \"Customer Attrition Model flow\",\r\n"
				+ "            \"dataContextId\": \"DC002\",\r\n"
				+ "            \"dataContextEndPoints\": \"http://40.127.187.34/APIServices/odata/Tenant1/XAIAttritionResultsDetailed\",\r\n"
				+ "            \"dataContextSource\": \"Analytics\",\r\n"
				+ "            \"dataContextDescription\": \"DataContext created for Customer Attrition Model flow Profiles\",\r\n"
				+ "            \"dataContextServiceName\": \"Dataset_AllCustomersAttrition\"\r\n"
				+ "        }\r\n"
				+ "    ],\r\n"
				+ "    \"httpStatusCode\": 0\r\n"
				+ "}";
		
		JSONObject expectedResult = new JSONObject();
		JSONObject respObj =  new JSONObject(response);// CommonUtilities.getStringAsJSONObject(response);
		expectedResult.put(CMConstants.OPSTATUS, 0);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		JSONObject actualResult = new JSONObject();
		when(DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.CAMPAIGN_MANAGEMENT_MS, null, OperationName.GET_CAMPAIGNS, postParametersMap, null, "token")).thenReturn("Resp");
		headerMap.put("x-kony-authorization", "token");
		when(CommonUtilities.getStringAsJSONObject("Resp")).thenReturn(respObj);
		CampaignsManagementBackendDelegateImpl campaignsManagementBackendDelegateImpl = new CampaignsManagementBackendDelegateImpl();
		actualResult = campaignsManagementBackendDelegateImpl.getCampaigns(postParametersMap,headerMap);
		
		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));
	}
	
	
	
	/*
	@Test
	public void testGetDashboardCounts() throws Exception{
		HashMap<String,String> map = new HashMap<>();
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_mc_approvalrequests_view_response.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));

		String getResponseFile1 = new File(
				getClass().getClassLoader().getResource("getMakerCheckerRequests_DB_response.json").getFile()).getPath();
		String getResponseContent1 = new String(Files.readAllBytes(Paths.get(getResponseFile1)));
		JSONObject expectedJson1 = new JSONObject(getResponseContent1);
		
		Map<String, String> userAttributes = new HashMap<>();
		
		JSONObject userAttr = new JSONObject();
		userAttr.put(CMConstants.LEGAL_ENTITY_ID, "GB0010001,AU0010001");
		userAttr.put(CMConstants.USERNAME, "admin1");
		
		JSONObject rawresponse = new JSONObject();
		rawresponse.put(CMConstants.USER_ATTRIBUTES, userAttr);
		
		HashMap<String,Object> securityAttributes= new HashMap<>();
		securityAttributes.put(CMConstants.RAW_RESPONSE, rawresponse);
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		Set<String> set2= new HashSet<String>();
		set1.add("ApproveCreateContract");
		set1.add("ApproveUpdateContract");
		set1.add("ApproveCreateCustomer");
		set1.add("ApproveUpdateCustomer");
		set2.add("ApproveCreateContract");
		set2.add("ApproveUpdateContract");
		set2.add("ApproveCreateCustomer");
		set2.add("ApproveUpdateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		leWisePermissionMap.put("AU0010001", set2);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		Map<String, Object> requestParameters1 = new HashMap<String, Object>();
		String legalEntityIds = "GB0010001,AU0010001";
		String username = "admin1";
		Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));
		Set<String> leSet = Stream.of(legalEntityIds.trim().split(",")).collect(Collectors.toSet());
		String filter = CMConstants.CREATED_BY+" eq '" + username + "' and "+CMConstants.STATUS+" eq 'Pending' and ";
		filter = filter + "(" + CMConstants.COMPANY_LEGAL_UNIT + " eq " + 
				String.join(" or " + CMConstants.COMPANY_LEGAL_UNIT + " eq ", leSet)
				+ ")";
		
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		
		when(CommonUtilities.getLoggedInUserAttributes(dcRequest)).thenReturn(userAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(CMConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(CMConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(CMConstants.AC_MAKER_CHECKER_CRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(CMConstants.DB_GET_MC_APPROVALREQUESTS_VIEW)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
		
		String filter1 = "";
		for (Map.Entry<String, Set<String>> entry : leWisePermissionMap.entrySet()) {
			String key = entry.getKey();
			Set<String> val = entry.getValue();
			filter1 = filter1 + "( "+CMConstants.COMPANY_LEGAL_UNIT+" eq '" + key + "' and " +
					CMConstants.CREATED_BY +" ne '"+username+"' and " +CMConstants.STATUS +" eq 'Pending' and ";
			filter1 += "(" + CMConstants.APPROVAL_PERMISSION_NAME + " eq "
					+ String.join(" or " + CMConstants.APPROVAL_PERMISSION_NAME + " eq ", val) + ") )";
			filter1 = filter1 + (" or ");
		}
		
		filter1 = filter1.substring(0, filter1.length() - 4);
		requestParameters1.put(ODataQueryConstants.FILTER, filter1);
		
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters1)).thenReturn(dbpServiceExecutorBuilder);
	
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualJSONObject = makerCheckerBackendDelegateImpl.getDashboardCounts(dcRequest, map);
		
		JSONAssert.assertEquals(actualJSONObject, expectedJson1, false);
		
	}*/
}

package com.kony.campaignsmanagement.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;


@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class})
public class CampaignsManagementBusinessDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	CampaignsManagementBackendDelegate campaignsManagementBackendDelegate;
	CampaignsManagementBusinessDelegateImpl campaignsManagementBusinessDelegateImpl;
	String methodID;
	EnvironmentConfigurationsHandler environmentConfigurationsHandler;
	Object[] inputArray;
	CommonUtilities commonUtilities;
	Map<String, String> inputMap;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	ServicesManager serviceManager;
	IdentityHandler identityHandler;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedCommonUtilities;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedEnvironmentConfigurationsHandler;
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedCommonUtilities.close();
		mockedEnvironmentConfigurationsHandler.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		
		
		methodID = "METHODID";
		inputArray = new Object[2];
		inputMap = new HashMap<String, String>();
		session = mock(Session.class);
		environmentConfigurationsHandler = mock(EnvironmentConfigurationsHandler.class);
		dcRequest  = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		campaignsManagementBackendDelegate = mock(CampaignsManagementBackendDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);
		
		commonUtilities = mock(CommonUtilities.class);
		serviceManager =  mock(ServicesManager.class);
		identityHandler = mock(IdentityHandler.class);
		//when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(CampaignsManagementBackendDelegate.class)).thenReturn(campaignsManagementBackendDelegate);
		
		/*when(DBPAPIAbstractFactoryImpl.getBusinessDelegate(CampaignsManagementBusinessDelegate.class))
		.thenReturn(campaignManagementManagementBusinessDelegate);*/
		
		campaignsManagementBusinessDelegateImpl = new CampaignsManagementBusinessDelegateImpl();
		
		}
	
	@Test
	public void testCreateCampaignMSSuccess() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);		
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		when(campaignsManagementBackendDelegate.createCampaign(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.createCampaign(postParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	@Test
	public void testCreateCampaignDBXDBSuccess() throws Exception{
		headerMap = new HashMap<String, Object>();
		postParametersMap = new HashMap<String, Object>();
		
		/*postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "");*/
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, null);
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, null);
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, null);
		postParametersMap.put(CMConstants.START_DATE, null);
		postParametersMap.put(CMConstants.END_DATE, null);
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, null);
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, null);
		postParametersMap.put(CMConstants.PRODUCTID, null);
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, null);
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS,null);
		postParametersMap.put(CMConstants.CAMPAIGN_ID,"CP"+null);
		//{campaignType=null, productId=null, endDate=null, offlineTemplate=, campaignId=CPnull, productGroupId=null, eventTriggerIdList=, channelType=, onlineContent=, channelDetails=, objectiveType=null, campaignPriority=null, profileIdList=, campaignStatus=null, campaignDescription=null, campaignName=null, startDate=null}
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);	
		 
		 when(CommonUtilities.getFormattedTimeStamp(null)).thenReturn(null);
		when(CommonUtilities.getFormattedTimeStamp(null, null)).thenReturn(null);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("DBXDB");
		when(campaignsManagementBackendDelegate.createCampaign(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		Map<String,Object> businessPostParametersMap = new HashMap<String, Object>();
		
		businessPostParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "");
		businessPostParametersMap.put(CMConstants.PROFILEID_LIST, "");
		businessPostParametersMap.put(CMConstants.CHANNEL_TYPE, "");
		businessPostParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"");
		businessPostParametersMap.put(CMConstants.ONLINE_CONTENT, "");
		businessPostParametersMap.put(CMConstants.CHANNEL_DETAILS, "");
		
		businessPostParametersMap.put(CMConstants.CAMPAIGN_NAME, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, null);
		businessPostParametersMap.put(CMConstants.START_DATE, null);
		businessPostParametersMap.put(CMConstants.END_DATE, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_TYPE, null);
		businessPostParametersMap.put(CMConstants.OBJECTIVE_TYPE, null);
		businessPostParametersMap.put(CMConstants.PRODUCTID, null);
		businessPostParametersMap.put(CMConstants.PRODUCT_GROUPID, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_STATUS,null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_ID,"CP"+null);
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.createCampaign(businessPostParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	
	@Test
	public void testCreateCampaignException() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		when(campaignsManagementBackendDelegate.createCampaign(postParametersMap,headerMap))
		.thenThrow(new NullPointerException());
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.createCampaign(postParametersMap,headerMap);

		assertEquals(actualResult.length(), expectedResult.length());		
	}
	
	@Test
	public void testGetCampaignsSuccess() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);		
		when(campaignsManagementBackendDelegate.getCampaigns(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.getCampaigns(postParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	
	@Test
	public void testGetCampaignsUnSuccess() throws Exception{
		
		
		JSONObject expectedResult = null;

		//expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);		
		when(campaignsManagementBackendDelegate.getCampaigns(postParametersMap,headerMap))
		.thenReturn(null);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.getCampaigns(postParametersMap,headerMap);

		assertEquals(actualResult, expectedResult);		
	}
	
	@Test
	public void testUpdateCampaignMSSuccess() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);		
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("MS");
		when(campaignsManagementBackendDelegate.updateCampaign(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.updateCampaign(postParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	
	@Test
	public void testUpdateCampaignDBXDBSuccess() throws Exception{
		headerMap = new HashMap<String, Object>();
		postParametersMap = new HashMap<String, Object>();
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, null);
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, null);
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, null);
		postParametersMap.put(CMConstants.START_DATE, null);
		postParametersMap.put(CMConstants.END_DATE, null);
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, null);
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, null);
		postParametersMap.put(CMConstants.PRODUCTID, null);
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, null);
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS,null);
		postParametersMap.put(CMConstants.CAMPAIGN_ID,"CP"+null);
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST,"ET005|ET006");
		postParametersMap.put(CMConstants.PROFILEID_LIST,"123|456");
		postParametersMap.put(CMConstants.CHANNEL_TYPE,"1|2");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"OTnull$CS$S$MC|OTnull$CS$S$MC");
		postParametersMap.put(CMConstants.ONLINE_CONTENT,"OCnull$PH001$TURL$IURL$$$$$$|OCnull$PH001$TURL$IURL$$$$$$");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS,"SMS$2|EMAIL$2");
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);	
		 
		 when(CommonUtilities.getFormattedTimeStamp(null)).thenReturn(null);
		when(CommonUtilities.getFormattedTimeStamp(null, null)).thenReturn(null);
		when(EnvironmentConfigurationsHandler.getServerAppProperty("CAMPAIGNS_BACKEND")).thenReturn("DBXDB");
		when(campaignsManagementBackendDelegate.updateCampaign(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		Map<String,Object> businessPostParametersMap = new HashMap<String, Object>();
		
		businessPostParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "[{\"id\":\"ET005\"},{\"id\":\"ET006\"}]");
		businessPostParametersMap.put(CMConstants.PROFILEID_LIST,  "[{\"id\":\"123\"},{\"id\":\"456\"}]");
		businessPostParametersMap.put(CMConstants.CHANNEL_TYPE, "[{\"type\":\"1\"},{\"type\":\"2\"}]");
		businessPostParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"[{\"offlineTemplateId\":\"OT1\",\"channelSubType\":\"CS\",\"subject\":\"S\",\"messageContent\":\"MC\"},{\"offlineTemplateId\":\"OT2\",\"channelSubType\":\"CS\",\"subject\":\"S\",\"messageContent\":\"MC\"}]");
		businessPostParametersMap.put(CMConstants.ONLINE_CONTENT, "[{\"onlineContentId\":\"OC1\",\"placeholderId\":\"PH001\",\"targetURL\":\"TURL\",\"imageURL\":\"IURL\"},{\"onlineContentId\":\"OC2\",\"placeholderId\":\"PH001\",\"targetURL\":\"TURL\",\"imageURL\":\"IURL\"}]");
		businessPostParametersMap.put(CMConstants.CHANNEL_DETAILS, "[{\"channelSubType\":\"SMS\",\"channelPriority\":\"2\"},{\"channelSubType\":\"EMAIL\",\"channelPriority\":\"2\"}]");
		
		businessPostParametersMap.put(CMConstants.CAMPAIGN_NAME, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, null);
		businessPostParametersMap.put(CMConstants.START_DATE, null);
		businessPostParametersMap.put(CMConstants.END_DATE, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_TYPE, null);
		businessPostParametersMap.put(CMConstants.OBJECTIVE_TYPE, null);
		businessPostParametersMap.put(CMConstants.PRODUCTID, null);
		businessPostParametersMap.put(CMConstants.PRODUCT_GROUPID, null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_STATUS,null);
		businessPostParametersMap.put(CMConstants.CAMPAIGN_ID,"CP"+null);
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.updateCampaign(businessPostParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	
	@Test
	public void testUpdateCampaignException() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		when(campaignsManagementBackendDelegate.updateCampaign(postParametersMap,headerMap))
		.thenThrow(new NullPointerException());
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.updateCampaign(postParametersMap,headerMap);

		assertEquals(actualResult.length(), expectedResult.length());		
	}
	
	@Test
	public void testGetCampaignSuccess() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		expectedResult.put(CMConstants.OPSTATUS, CMConstants.TRUE);		
		when(campaignsManagementBackendDelegate.getCampaigns(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.getCampaigns(postParametersMap,headerMap);

		assertEquals(actualResult.get(CMConstants.OPSTATUS), expectedResult.get(CMConstants.OPSTATUS));		
	}
	
	@Test
	public void testGetCampaignUnSuccess() throws Exception{
		
		
		JSONObject expectedResult = null;

		when(campaignsManagementBackendDelegate.getCampaigns(postParametersMap,headerMap))
		.thenReturn(expectedResult);
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.getCampaigns(postParametersMap,headerMap);

		assertEquals(actualResult, expectedResult);		
	}
	@Test
	public void testGetCampaignException() throws Exception{
		
		
		JSONObject expectedResult = new JSONObject();

		when(campaignsManagementBackendDelegate.getCampaigns(postParametersMap,headerMap))
		.thenThrow(new NullPointerException());
		
		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.getCampaigns(postParametersMap,headerMap);

		assertEquals(actualResult.length(), expectedResult.length());		
	}
}

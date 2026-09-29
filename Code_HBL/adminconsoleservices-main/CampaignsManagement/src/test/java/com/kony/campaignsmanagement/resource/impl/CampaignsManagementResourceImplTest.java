package com.kony.campaignsmanagement.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.utils.ErrorCodesEnum;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class})
public class CampaignsManagementResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	CampaignsManagementResourceImpl campaignsManagementResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	CampaignsManagementBusinessDelegate campaignManagementManagementBusinessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
	String legalEntityId = "GB0010001";
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}

	@Before
	public void executedBefore() throws Exception {

		methodID = "METHODID";
		inputArray = new Object[2];
		inputArray[0] = "";
		inputArray[1] = new HashMap<String, String>();
		actualResult = new Result();
		
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		postParametersMap = new HashMap<String,Object>();
		headerMap  = new HashMap<String,Object>();
		campaignManagementManagementBusinessDelegate = mock(CampaignsManagementBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);

		/*when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);*/
		
		
		when(DBPAPIAbstractFactoryImpl.getBusinessDelegate(CampaignsManagementBusinessDelegate.class))
				.thenReturn(campaignManagementManagementBusinessDelegate);
	}

	@Test
	public void testCreateCampaignSuccess() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();
		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("Test Campaign");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		
		when(campaignManagementManagementBusinessDelegate.createCampaign(postParametersMap,headerMap)).thenReturn(expectedCreateCampaignResponse);
		expectedResult.addParam(CMConstants.OPSTATUS,expectedCreateCampaignResponse.get(CMConstants.OPSTATUS).toString());
		actualResult = campaignsManagementResourceImpl.createCampaign(methodID, inputArray, dcRequest, dcResponse);
		//System.out.println(actualResult);
		//System.out.println(expectedResult);
		assertEquals(actualResult.getParamValueByName(CMConstants.OPSTATUS), expectedResult.getParamValueByName(CMConstants.OPSTATUS));
	}
	
	
	
	@Test
	public void testCreateCampaignNullResponse() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("Test Campaign");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		
		when(campaignManagementManagementBusinessDelegate.createCampaign(postParametersMap,headerMap)).thenReturn(null);
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22246.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22246.getMessage()));

		actualResult = campaignsManagementResourceImpl.createCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testCreateCampaignException() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22246.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22246.getMessage()));

		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("Test Campaign");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		
		when(campaignManagementManagementBusinessDelegate.createCampaign(postParametersMap,headerMap)).thenThrow(new NullPointerException());
		
		
		actualResult = campaignsManagementResourceImpl.createCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testCreateCampaignInvalidInput() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();
		
		when(dcRequest.getParameter(CMConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22245.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22245.getMessage()));

		actualResult = campaignsManagementResourceImpl.createCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testGetCampaignsSuccess() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();
		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter("eventCode")).thenReturn("Event Code");
		when(dcRequest.getParameter("campaignStatus")).thenReturn("status");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put("_eventCode", "Event Code");
		postParametersMap.put("_status", "status");
		
		when(campaignManagementManagementBusinessDelegate.getCampaigns(postParametersMap,headerMap)).thenReturn(expectedCreateCampaignResponse);
		expectedResult.addParam(CMConstants.OPSTATUS,expectedCreateCampaignResponse.get(CMConstants.OPSTATUS).toString());
		actualResult = campaignsManagementResourceImpl.getCampaigns(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName(CMConstants.OPSTATUS), expectedResult.getParamValueByName(CMConstants.OPSTATUS));
	}
	
	@Test
	public void testGetCampaignsNullResponse() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter("eventCode")).thenReturn("Event Code");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put("_eventCode", "Event Code");
		
		when(campaignManagementManagementBusinessDelegate.getCampaigns(postParametersMap,headerMap)).thenReturn(null);
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22251.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22251.getMessage()));

		actualResult = campaignsManagementResourceImpl.getCampaigns(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testGetCampaignsException() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22251.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22251.getMessage()));

		when(dcRequest.getParameter("eventCode")).thenReturn("Event Code");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put("_eventCode", "Event Code");
		
		
		when(campaignManagementManagementBusinessDelegate.getCampaigns(postParametersMap,headerMap)).thenThrow(new NullPointerException());
		
		
		actualResult = campaignsManagementResourceImpl.getCampaigns(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testUpdateCampaignSuccess() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();
		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put("id","CP0000000001");
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("SCHEDULED_ACTIVE_COMPLETED");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_ID)).thenReturn("CP0000000001");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		
		postParametersMap.put(CMConstants.CAMPAIGN_ID, "CP0000000001");
		when(campaignManagementManagementBusinessDelegate.updateCampaign(postParametersMap,headerMap)).thenReturn(expectedCreateCampaignResponse);
		expectedResult.addParam("id",expectedCreateCampaignResponse.get("id").toString());
		expectedResult.addParam(CMConstants.OPSTATUS,expectedCreateCampaignResponse.get(CMConstants.OPSTATUS).toString());
		actualResult = campaignsManagementResourceImpl.updateCampaign(methodID, inputArray, dcRequest, dcResponse);
		//System.out.println(actualResult);
		//System.out.println(expectedResult);
		assertEquals(actualResult.getParamValueByName("id"), expectedResult.getParamValueByName("id"));
	}
	
	
	
	@Test
	public void testUpdateCampaignNullResponse() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		JSONObject expectedCreateCampaignResponse = new JSONObject();
		expectedCreateCampaignResponse.put(CMConstants.OPSTATUS,"0");
		
		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("SCHEDULED_ACTIVE_COMPLETED");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_ID)).thenReturn("CP0000000001");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		postParametersMap.put(CMConstants.CAMPAIGN_ID, "CP0000000001");
		
		when(campaignManagementManagementBusinessDelegate.updateCampaign(postParametersMap,headerMap)).thenReturn(null);
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22252.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22252.getMessage()));

		actualResult = campaignsManagementResourceImpl.updateCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testUpdateCampaignException() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22252.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22252.getMessage()));

		when(dcRequest.getParameter(CMConstants.EVENT_TRIGGERID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PROFILEID_LIST)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OFFLINE_TEMPLATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.ONLINE_CONTENT)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CHANNEL_DETAILS)).thenReturn("Test Campaign");
		
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_NAME)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_DESCRIPTION)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_PRIORITY)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.START_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.END_DATE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.OBJECTIVE_TYPE)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCTID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.PRODUCT_GROUPID)).thenReturn("Test Campaign");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_STATUS)).thenReturn("SCHEDULED_ACTIVE_COMPLETED");
		when(dcRequest.getParameter(CMConstants.CAMPAIGN_ID)).thenReturn("CP0000000001");
		
		when(dcRequest.getHeaderMap()).thenReturn(new HashMap<String, Object>());
		postParametersMap.put(CMConstants.EVENT_TRIGGERID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.PROFILEID_LIST, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OFFLINE_TEMPLATE,"Test Campaign");
		postParametersMap.put(CMConstants.ONLINE_CONTENT, "Test Campaign");
		postParametersMap.put(CMConstants.CHANNEL_DETAILS, "Test Campaign");
		
		postParametersMap.put(CMConstants.CAMPAIGN_NAME, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_DESCRIPTION, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_PRIORITY, "Test Campaign");
		postParametersMap.put(CMConstants.START_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.END_DATE, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.OBJECTIVE_TYPE, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCTID, "Test Campaign");
		postParametersMap.put(CMConstants.PRODUCT_GROUPID, "Test Campaign");
		postParametersMap.put(CMConstants.CAMPAIGN_STATUS, "SCHEDULED_ACTIVE_COMPLETED");
		postParametersMap.put(CMConstants.CAMPAIGN_ID, "CP0000000001");
		
		when(campaignManagementManagementBusinessDelegate.updateCampaign(postParametersMap,headerMap)).thenThrow(new NullPointerException());
		
		
		actualResult = campaignsManagementResourceImpl.updateCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testUpdateCampaignInvalidInput() throws Exception {
		
		campaignsManagementResourceImpl = new CampaignsManagementResourceImpl();
		
		when(dcRequest.getParameter(CMConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_22245.getErrorCodeAsString()));
		expectedResult.addParam(new Param(CMConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_22245.getMessage()));

		actualResult = campaignsManagementResourceImpl.createCampaign(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(CMConstants.DBP_ERR_MSG).getValue());
	}
	
}

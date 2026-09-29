package com.kony.campaign.businessdelegate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
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

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.kony.campaign.businessdelegate.impl.CampaignBusinessDelegateImpl;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.resource.impl.CampaignResourceImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CampaignBusinessDelegateImplTest {
	
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	CampaignBusinessDelegateImpl campaignBusinessDelegateImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	CampaignBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
	String legalEntityId = "GB0010001";
	
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

		methodID = "METHODID";
		inputArray = new Object[2];
		inputArray[0] = "";
		inputArray[1] = new HashMap<String, String>();
		actualResult = new Result();
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);

		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);

	}
	@Test
	public void testGetAllCampaignsForAnEvent() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_campaigns_response.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));

		Map<String, Object> inputMap = new HashMap<>();	   
		inputMap.put(CampaignConstants.PARAM_EVENT_CODE,"EV001");		
		inputMap.put(CampaignConstants.PARAM_PLACEHOLDER_CODE,"PH001");		
		inputMap.put(CampaignConstants.PARAM_SCALE,"1366");
		inputMap.put(CampaignConstants.PARAM_CHANNEL_TYPE,"SMS");
		inputMap.put(CampaignConstants.PARAM_CORE_CUSTOMER_ID,"100600");
		
		when(dcRequest.getParameter(CampaignConstants.PARAM_EVENT_CODE)).thenReturn("EV001");		
		when(dcRequest.getParameter(CampaignConstants.PARAM_PLACEHOLDER_CODE)).thenReturn("PH001");		
		when(dcRequest.getParameter(CampaignConstants.PARAM_SCALE)).thenReturn("1366");
		when(dcRequest.getParameter(CampaignConstants.PARAM_CHANNEL_TYPE)).thenReturn("SMS");
		when(dcRequest.getParameter(CampaignConstants.PARAM_CORE_CUSTOMER_ID)).thenReturn("100600");
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		String serviceName=CampaignConstants.CAMPAIGN_MANAGEMENT_JAVA;
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		String operationName=CampaignConstants.GET_CAMPAIGNS_OPERATION;
		when(dbpServiceExecutorBuilder.withOperationId(operationName))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		
		campaignBusinessDelegateImpl = new CampaignBusinessDelegateImpl();
		
		Result expectedResult = JSONToResult.convert(getResponseContent);

		actualResult = campaignBusinessDelegateImpl.getAllCampaignsForAnEvent(dcRequest);

		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
	}
	
	@Test
	public void testGetDefaultCampaignsForAnEvent() throws Exception{
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_default_campaigns_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		campaignBusinessDelegateImpl = new CampaignBusinessDelegateImpl();
		Map<String, Object> inputMap = new HashMap<>();	   
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		String serviceName=CampaignConstants.CAMPAIGN_MANAGEMENT_JAVA;
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		String operationName=CampaignConstants.GET_ALL_DEFAULT_CAMPAIGNS;
		when(dbpServiceExecutorBuilder.withOperationId(operationName))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withDataControllerRequest(dcRequest)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		Result expectedResult = JSONToResult.convert(getResponseContent);

		actualResult = campaignBusinessDelegateImpl.getDefaultCampaigns(dcRequest);

		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
	}
}

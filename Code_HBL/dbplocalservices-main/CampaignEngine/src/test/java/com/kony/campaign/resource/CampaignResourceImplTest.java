package com.kony.campaign.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.resource.impl.CampaignResourceImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CampaignResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	CampaignResourceImpl campaignResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	CampaignBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
	String legalEntityId = "GB0010001";

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

		businessDelegate = mock(CampaignBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(CampaignBusinessDelegate.class))
				.thenReturn(businessDelegate);
	}
	@Test
	public void testGetAllCampaignsForAnEvent() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_campaigns_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		campaignResourceImpl = new CampaignResourceImpl();
		
		Result expectedResult = JSONToResult.convert(getResponseContent);

		when(businessDelegate.getAllCampaignsForAnEvent(dcRequest)).thenReturn(expectedResult);
		when(dcRequest.getParameter(CampaignConstants.PARAM_EVENT_CODE)).thenReturn("ACCOUNTDASHBOARD");
		actualResult = campaignResourceImpl.getAllCampaignsForAnEvent(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
	}
	
	@Test
	public void testGetDefaultCampaignsForAnEvent() throws Exception{
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_default_campaigns_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		campaignResourceImpl = new CampaignResourceImpl();
		when(dcRequest.getParameter(CampaignConstants.PARAM_EVENT_CODE)).thenReturn("ACCOUNTDASHBOARD");

		Result expectedResult = JSONToResult.convert(getResponseContent);

		when(businessDelegate.getDefaultCampaigns(dcRequest)).thenReturn(expectedResult);

		actualResult = campaignResourceImpl.getDefaultCampaigns(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
	}
}

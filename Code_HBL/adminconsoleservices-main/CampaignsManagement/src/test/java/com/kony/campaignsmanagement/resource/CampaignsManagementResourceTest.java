package com.kony.campaignsmanagement.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
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
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.campaignsmanagement.businessdelegate.api.CampaignsManagementBusinessDelegate;
import com.kony.campaignsmanagement.resource.impl.CampaignsManagementResourceImpl;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.campaignsmanagement.utils.CMConstants;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })
public class CampaignsManagementResourceTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	CampaignsManagementResourceImpl resource;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	CampaignsManagementBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	CampaignUtil campaignUtil;
	CommonUtilities commonUtilities;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CampaignUtil> mockedStaticCampaignUtil;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCampaignUtil = Mockito.mockStatic(CampaignUtil.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticCampaignUtil.close();
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

		businessDelegate = mock(CampaignsManagementBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(CampaignsManagementBusinessDelegate.class))
				.thenReturn(businessDelegate);
	}

	@Test
	public void testGetAllPlaceHoldersMS() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_placeholders_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.MS);
		when(businessDelegate.getAllPlaceHoldersMS(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getAllPlaceHolders(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}

	@Test
	public void testGetAllPlaceHolders() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_placeholders_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();

		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.DBXDB);
		when(businessDelegate.getAllPlaceHolders(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getAllPlaceHolders(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}

	@Test
	public void testGetAllEventTriggersMS() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_eventtriggers_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.MS);
		when(businessDelegate.getEventTriggersMS(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getEventTriggers(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}

	@Test
	public void testGetAllEventTriggers() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_eventtriggers_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();

		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.DBXDB);
		when(businessDelegate.getEventTriggers(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getEventTriggers(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}

	@Test
	public void testCreateProfileDB() throws IOException {

		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject responseObj = new JSONObject(getResponseContent);
		String profileName = "Test profile";
		String profileDesc = "Test profile";
		String profileConditions = "[{\"profileConditionId\":\"\",\r\n"
				+ "                       \"conditionExpression\":\"EstMonthlyExpense%20gt%20%271000%27\",\r\n"
				+ "                       \"dataContextId\":\"DC002\"}]";

		Result expectedResult = new Result();
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn(profileName);
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn(profileDesc);
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(profileConditions);
		//when(CommonUtilities.getNumericId()).thenReturn((long) 100);
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.DBXDB);
		when(businessDelegate.createProfileDBXDB(dcRequest)).thenReturn(responseObj);

		Result actualResult = resource.createProfile(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));

	}

	@Test
	public void testCreateProfileMS() throws IOException {

		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject responseObj = new JSONObject(getResponseContent);
		String profileName = "Test profile";
		String profileDesc = "Test profile";
		String profileConditions = "[{\"profileConditionId\":\"\",\r\n"
				+ "                       \"conditionExpression\":\"EstMonthlyExpense%20gt%20%271000%27\",\r\n"
				+ "                       \"dataContextId\":\"DC002\"}]";

		Result expectedResult = new Result();
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn(profileName);
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn(profileDesc);
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(profileConditions);
		//when(CommonUtilities.getNumericId()).thenReturn((long) 100);
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.MS);
		when(businessDelegate.createProfileMS(dcRequest)).thenReturn(responseObj);

		Result actualResult = resource.createProfile(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}
	
	@Test
	public void testGetProfilesDBXDB() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profiles_db_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();

		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.DBXDB);
		when(businessDelegate.getProfilesDBXDB(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getProfiles(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}
	
	@Test
	public void testGetProfilesMS() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profiles_ms_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray responseArray = new JSONArray(getResponseContent);
		Result expectedResult = new Result();

		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.MS);
		when(businessDelegate.getProfilesMS(dcRequest)).thenReturn(responseArray);

		Result actualResult = resource.getProfiles(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("dbpErrCode"), actualResult.getParamByName("dbpErrCode"));
		assertEquals(expectedResult.getParamByName("dbpErrMsg"), actualResult.getParamByName("dbpErrMsg"));
	}
	
	@Test
	public void testUpdateProfileMS() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject responseObj = new JSONObject(getResponseContent);
		String profileName = "ABCDEF";
		String profileDesc = "ABCDEF";
		String profileConditions = "[\r\n"
				+ "            {\r\n"
				+ "                \"profileConditionId\": \"PC1046746195\",\r\n"
				+ "                \"conditionExpression\": \"dvdsfdd\",\r\n"
				+ "                \"dataContextId\": \"DC001\"\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"profileConditionId\": \"PC1685920737\",\r\n"
				+ "                \"conditionExpression\": \"dvsaebwrbeqfqe\",\r\n"
				+ "                \"dataContextId\": \"DC001\"\r\n"
				+ "            }\r\n"
				+ "        ]";
		String profileId = "PRF0011";
		String profileStatus = "Active";
		
		String getPayloadFile = new File(
				getClass().getClassLoader().getResource("update_profile_payload.json").getFile()).getPath();
		JSONObject json = new JSONObject();
		String getPayloadContent = new String(Files.readAllBytes(Paths.get(getPayloadFile)));
		JSONObject profile = new JSONObject(getPayloadContent).optJSONObject("profile");
		JSONArray profileConditionsArr = new JSONArray(profileConditions);
		Result expectedResult = new Result();
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn(profileName);
		when(dcRequest.getParameter(CMConstants.PROFILE_STATUS)).thenReturn(profileStatus);
		when(dcRequest.getParameter(CMConstants.PROFILE_ID)).thenReturn(profileId);
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn(profileDesc);
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(profileConditions);
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.MS);
		when(businessDelegate.updateProfileMS(dcRequest, profile)).thenReturn(responseObj);
		//when(businessDelegate.getProfileConditionsDB(profileId)).thenReturn(profileConditionsArr);
		Result actualResult = resource.updateProfile(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("opstatus"), actualResult.getParamByName("opstatus"));
	}
	
	@Test
	public void testUpdateProfileDB() throws IOException {
		resource = new CampaignsManagementResourceImpl();

		String getPayloadFile = new File(
				getClass().getClassLoader().getResource("update_profile_payload.json").getFile()).getPath();
		JSONObject json = new JSONObject();
		String getPayloadContent = new String(Files.readAllBytes(Paths.get(getPayloadFile)));
		JSONObject profile = new JSONObject(getPayloadContent).optJSONObject("profile");
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject responseObj = new JSONObject(getResponseContent);
		
		String getProfilesFile = new File(
				getClass().getClassLoader().getResource("get_profiles_response.json").getFile()).getPath();

		String getProfilesContent = new String(Files.readAllBytes(Paths.get(getProfilesFile)));
		JSONArray profilesArr = new JSONArray(getProfilesContent);
		
		String profileName = "ABCDEF";
		String profileDesc = "ABCDEF";
		String profileConditions = "[\r\n"
				+ "            {\r\n"
				+ "                \"profileConditionId\": \"PC1046746195\",\r\n"
				+ "                \"conditionExpression\": \"dvdsfdd\",\r\n"
				+ "                \"dataContextId\": \"DC001\"\r\n"
				+ "            },\r\n"
				+ "            {\r\n"
				+ "                \"profileConditionId\": \"PC1685920737\",\r\n"
				+ "                \"conditionExpression\": \"dvsaebwrbeqfqe\",\r\n"
				+ "                \"dataContextId\": \"DC001\"\r\n"
				+ "            }\r\n"
				+ "        ]";
		String profileId = "PRF0011";
		String profileStatus = "Active";
		List<String> deletedConditions = new ArrayList<>(List.of());
		Result expectedResult = new Result();
		when(dcRequest.getParameter(CMConstants.PROFILE_NAME)).thenReturn(profileName);
		when(dcRequest.getParameter(CMConstants.PROFILE_DESC)).thenReturn(profileDesc);
		when(dcRequest.getParameter(CMConstants.PROFILE_STATUS)).thenReturn(profileStatus);
		when(dcRequest.getParameter(CMConstants.PROFILE_ID)).thenReturn(profileId);
		when(dcRequest.getParameter(CMConstants.PROFILE_CONDITION)).thenReturn(profileConditions);
		when(CampaignUtil.getServerProperty(CMConstants.CAMPAIGNS_BACKEND, CMConstants.DEFAULT_CAMPAIGNS_BACKEND_VALUE))
				.thenReturn(CMConstants.DBXDB);
		when(businessDelegate.updateProfile(profile,dcRequest)).thenReturn(responseObj);
		when(businessDelegate.getProfilesDB(profileName, profileId)).thenReturn(profilesArr);
		when(businessDelegate.getProfileConditionsDB(profileId)).thenReturn(profilesArr);
		when(businessDelegate.deleteRemovedProfileConditions(profileId,deletedConditions)).thenReturn(null);
		Result actualResult = resource.updateProfile(getResponseContent, inputArray, dcRequest, dcResponse);

		assertEquals(expectedResult.getParamByName("opstatus"), actualResult.getParamByName("opstatus"));
	}
}

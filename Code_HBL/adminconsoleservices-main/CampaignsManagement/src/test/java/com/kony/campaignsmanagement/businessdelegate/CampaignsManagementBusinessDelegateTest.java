package com.kony.campaignsmanagement.businessdelegate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import com.kony.campaignsmanagement.backenddelegate.api.CampaignsManagementBackendDelegate;
import com.kony.campaignsmanagement.businessdelegate.impl.CampaignsManagementBusinessDelegateImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

public class CampaignsManagementBusinessDelegateTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	CampaignsManagementBackendDelegate campaignsManagementBackendDelegate;
	CampaignsManagementBusinessDelegateImpl campaignsManagementBusinessDelegateImpl;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;

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

		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		campaignsManagementBackendDelegate = mock(CampaignsManagementBackendDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(CampaignsManagementBackendDelegate.class))
				.thenReturn(campaignsManagementBackendDelegate);
		campaignsManagementBusinessDelegateImpl = new CampaignsManagementBusinessDelegateImpl();

	}

	@Test
	public void testGetAllPlaceHoldersDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_placeholders_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getAllPlaceHolders(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getAllPlaceHolders(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("placeholderId"),
				expectedResult.getJSONObject(0).getString("placeholderId"));
	}

	@Test
	public void testGetAllPlaceHoldersMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_placeholders_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getAllPlaceHoldersMS(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getAllPlaceHoldersMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("placeholderId"),
				expectedResult.getJSONObject(0).getString("placeholderId"));
	}

	@Test
	public void testGetAllEventTriggersDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_eventtriggers_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getEventTriggers(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getEventTriggers(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("eventTriggerId"),
				expectedResult.getJSONObject(0).getString("eventTriggerId"));
	}

	@Test
	public void testGetAllEventTriggersMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_all_eventtriggers_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getEventTriggersMS(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getEventTriggersMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("eventTriggerId"),
				expectedResult.getJSONObject(0).getString("eventTriggerId"));
	}

	@Test
	public void testCreateProfileDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		when(campaignsManagementBackendDelegate.createProfileInDBXDB(dcRequest)).thenReturn(expectedResult);

		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.createProfileDBXDB(dcRequest);

		assertEquals(actualResult.getString("id"), expectedResult.getString("id"));
	}

	@Test
	public void testCreateProfileMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("createProfile_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		when(campaignsManagementBackendDelegate.createProfileInMS(dcRequest)).thenReturn(expectedResult);

		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.createProfileMS(dcRequest);

		assertEquals(actualResult.getString("id"), expectedResult.getString("id"));
	}
	
	@Test
	public void testGetProfilesDB() throws IOException {
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profiles_response.json").getFile()).getPath();
		String profileName = "";
		String profileId = "";
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getProfilesDB(profileName, profileId)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getProfilesDB(profileName, profileId);

		assertEquals(actualResult.getJSONObject(0).getString("profileId"),
				expectedResult.getJSONObject(0).getString("profileId"));
		}
	
	@Test
	public void testGetProfilesDBXDB() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profiles_proc_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);
        JSONArray expectedResultArr = expectedResult.getJSONArray("records");
		when(campaignsManagementBackendDelegate.getProfilesDBXDB(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getProfilesDBXDB(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("profileId"),
				expectedResultArr.getJSONObject(0).getString("profileId"));
	}

	@Test
	public void testGetProfilesMS() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profiles_ms_response.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONArray(getResponseContent);

		when(campaignsManagementBackendDelegate.getProfilesMS(dcRequest)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getProfilesMS(dcRequest);

		assertEquals(actualResult.getJSONObject(0).getString("profileId"),
				expectedResult.getJSONObject(0).getString("profileId"));
	}
	@Test
	public void testDeleteRemovedProfileConditions() throws IOException {
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("delete_profile_condition_response.json").getFile()).getPath();
		String profileId = "";
		List<String> profileConditions = List.of("");
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		when(campaignsManagementBackendDelegate.deleteRemovedProfileConditions(profileId, profileConditions)).thenReturn(expectedResult);

		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.deleteRemovedProfileConditions(profileId, profileConditions);

		assertEquals(actualResult.getString("success"), expectedResult.getString("success"));
	}
	@Test
	public void testUpdateProfileDB() throws IOException {
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();
		JSONObject profile = new JSONObject();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		when(campaignsManagementBackendDelegate.updateProfile(profile, dcRequest)).thenReturn(expectedResult);

		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.updateProfile(profile, dcRequest);

		assertEquals(actualResult.getString("profileId"), expectedResult.getString("profileId"));
	}
	@Test
	public void testUpdateProfileMS() throws IOException {
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("update_profile_response.json").getFile()).getPath();
		JSONObject profile = new JSONObject();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedResult = new JSONObject(getResponseContent);

		when(campaignsManagementBackendDelegate.updateProfileMS(dcRequest, profile)).thenReturn(expectedResult);

		JSONObject actualResult = campaignsManagementBusinessDelegateImpl.updateProfileMS(dcRequest, profile);

		assertEquals(actualResult.getString("profileId"), expectedResult.getString("profileId"));
	}
	
	@Test
	public void testGetProfileConditionsDB() throws IOException {
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_profile_conditions_response.json").getFile()).getPath();
		String profileId = "";
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray expectedResult = new JSONObject(getResponseContent).optJSONArray("profilecondition");

		when(campaignsManagementBackendDelegate.getProfileConditionsDB(profileId)).thenReturn(expectedResult);

		JSONArray actualResult = campaignsManagementBusinessDelegateImpl.getProfileConditionsDB(profileId);

		assertEquals(actualResult.getJSONObject(0).getString("profileConditionId"),
				expectedResult.getJSONObject(0).getString("profileConditionId"));
	}
	
}

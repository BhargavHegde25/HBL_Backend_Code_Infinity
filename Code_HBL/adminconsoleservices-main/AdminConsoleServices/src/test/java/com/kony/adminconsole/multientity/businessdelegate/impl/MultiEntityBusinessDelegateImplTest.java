package com.kony.adminconsole.multientity.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.multientity.backenddelegate.api.MultiEntityBackendDelegate;
import com.kony.adminconsole.multientity.businessdelegate.impl.MultiEntityBusinessDelegateImpl;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utils.CommonUtilitiesTest;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class,})
public class MultiEntityBusinessDelegateImplTest {
	
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	MultiEntityBackendDelegate multiEntityBackendDelegate;
	MultiEntityBusinessDelegateImpl multiEntityBusinessDelegateImpl;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Executor executor;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	CommonUtilities commonUtilities;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedStaticCommonUtilities;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedStaticEnvironmentConfigHandler;


	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedStaticEnvironmentConfigHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticCommonUtilities.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		
		
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		
		session = mock(Session.class);
		dcRequest  = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		multiEntityBackendDelegate = mock(MultiEntityBackendDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(MultiEntityBackendDelegate.class)).thenReturn(multiEntityBackendDelegate);
		
		multiEntityBusinessDelegateImpl = new MultiEntityBusinessDelegateImpl();
		
		}
	
//	@Test
	public void testGetAllCompanyLegalUnitsSingleEntity() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = true;
		Object sessionId = new Object();
		
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetCompanyLegalUnitsResponse_SystemConfig.json").getFile()).getPath();

		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
	
		JSONObject expectedJson = new JSONObject();
		JSONArray resultJsonArray = new JSONArray(getCompanyLEsResponseContent);
		expectedJson = resultJsonArray.getJSONObject(0);

		Dataset ds = CommonUtilitiesTest.constructDatasetFromJSONArray(resultJsonArray);
		Result expectedResult = new Result();
		expectedResult.setDataSet(ds);
		
		when(session.getId()).thenReturn("1");
		when(multiEntityBackendDelegate.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId))
		.thenReturn(expectedJson);
		when(CommonUtilities.constructResultFromJSONObject(expectedJson)).thenReturn(expectedResult);
		
		actualResult = multiEntityBusinessDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId);
		
		List<Param> expectedListOfParams = expectedResult.getDataSets().get(0).getRecords().get(0).getAllParams();
		List<Param> actualListOfParams = actualResult.getDataSets().get(0).getRecords().get(0).getAllParams();
		
		System.out.println(expectedListOfParams);
		System.out.println(actualListOfParams);
		
		assertEquals(expectedListOfParams, actualListOfParams);
		
	}
	
//	@Test
	public void testGetAllCompanyLegalUnitsMultiEntity() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = false;
		Object sessionId = new Object();
		
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetCompanyLegalUnitsResponse_ORDMS.json").getFile()).getPath();

		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
	
		JSONObject expectedJson = new JSONObject();
		JSONArray expectedJSONArray = new JSONArray(getCompanyLEsResponseContent);
		//expectedJson = expectedJSONArray.getJSONObject(0);
		expectedJson.put("records", expectedJSONArray);
		Dataset ds = CommonUtilitiesTest.constructDatasetFromJSONArray(expectedJSONArray);
		Result expectedResult = new Result();
		expectedResult.setDataSet(ds);
		
		when(session.getId()).thenReturn("1");
		when(multiEntityBackendDelegate.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId))
		.thenReturn(expectedJson);
		when(CommonUtilities.constructResultFromJSONObject(expectedJson)).thenReturn(expectedResult);
		
		actualResult = multiEntityBusinessDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId);
		
		List<Param> expectedListOfParams = expectedResult.getDataSets().get(0).getRecords().get(0).getAllParams();
		List<Param> actualListOfParams = actualResult.getDataSets().get(0).getRecords().get(0).getAllParams();
		
		System.out.println(actualResult.getDataSets().get(0).getAllRecords());
		System.out.println(expectedResult.getDataSets().get(0).getAllRecords());

//		System.out.println(expectedListOfParams);
//		System.out.println(actualListOfParams);
		
		assertEquals(expectedListOfParams, actualListOfParams);
		
	}
	
}

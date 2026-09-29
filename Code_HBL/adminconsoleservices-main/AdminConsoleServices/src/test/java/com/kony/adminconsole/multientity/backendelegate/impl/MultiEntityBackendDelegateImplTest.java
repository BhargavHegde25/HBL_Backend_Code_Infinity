package com.kony.adminconsole.multientity.backendelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
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
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.multientity.backenddelegate.api.MultiEntityBackendDelegate;
import com.kony.adminconsole.multientity.backenddelegate.impl.MultiEntityBackendDelegateImpl;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utils.CommonUtilitiesTest;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class, 
	MemoryManager.class, DBPServiceExecutorBuilder.class, DBPServiceExecutor.class})
public class MultiEntityBackendDelegateImplTest {
	
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	MultiEntityBackendDelegate multiEntityBackendDelegate;
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
	MemoryManager memoryManager;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;
	 
	private static MockedStatic<MemoryManager> mockedStaticForMemoryManager;
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedStaticCommonUtilities;
	
	@BeforeClass
	public static void init() {
		mockedStaticForMemoryManager = Mockito.mockStatic(MemoryManager.class);
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForMemoryManager.close();
		mockedStaticForDBPServiceExecutorBuilder.close();
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
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		multiEntityBackendDelegate = mock(MultiEntityBackendDelegate.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		
		logger = mock(Logger.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);

		postParametersMap = new HashMap<>();
		headerMap = new HashMap<>();
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(MultiEntityBackendDelegate.class))
				.thenReturn(multiEntityBackendDelegate);
		
	}
	
	@Test
	public void testGetAllCompanyLegalUnitsFromConfig_Cache() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = true;
		Object sessionId = new Object();
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetCompanyLegalUnitsResponse_SystemConfig2.json").getFile()).getPath();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
		
		JSONObject expectedJson = new JSONObject(getCompanyLEsResponseContent);		

		String groupsResponse = expectedJson.toString();
		Result expectedResult = new Result();
		expectedResult =  CommonUtilitiesTest.constructResultFromJSONObject(expectedJson);
		
		JSONObject actualResult = new JSONObject();
		
		when(session.getId()).thenReturn("1");
		when(CommonUtilities.constructResultFromJSONObject(expectedJson)).thenReturn(expectedResult);	
		when(MemoryManager.getFromCache("companyLegalUnits"+ session.toString())).thenReturn(expectedJson);
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.CRUDLAYER)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.DB_FINANCIALINSTITUTION_GET_PROC)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(expectedJson.toString());
		when(CommonUtilities.getStringAsJSONObject(groupsResponse)).thenReturn(expectedJson);
		String config_value = expectedJson.getJSONArray("configurations").getJSONObject(0).get("config_value").toString();
		when(CommonUtilities.getStringAsJSONArray(config_value)).thenReturn(new JSONArray(config_value));
		
		MultiEntityBackendDelegateImpl multiEntityBackendDelegateImpl = new MultiEntityBackendDelegateImpl();
		actualResult = multiEntityBackendDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, session);
		System.out.println("ExpectedJson : "+expectedJson);
		System.out.println("Actual result : "+actualResult);

		String expectedResultString = expectedJson.getJSONArray("records").getJSONObject(0).toString();
		JSONObject expectedResultJson = new JSONObject(expectedResultString);
		
		assertEquals(actualResult.getJSONArray("companyLegalUnits").getJSONObject(0).get("id") , 
				expectedResultJson.get("id"));
		assertEquals(actualResult.getJSONArray("companyLegalUnits").getJSONObject(0).get("companyName") , 
				expectedResultJson.get("companyName"));
		}
	
	@Test
	public void testGetAllCompanyLegalUnitsFromMS_Cache() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = false;
		Object sessionId = new Object();
		headerMap.put("backendToken", authToken);
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetCompanyLegalUnitsResponse_ORDMS.json").getFile()).getPath();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
	
		JSONObject expectedJson = new JSONObject();
		JSONArray resultJsonArray = new JSONArray(getCompanyLEsResponseContent);
		expectedJson.put("financialInstitutions", resultJsonArray);
		expectedJson.put("opstatus", 0);
		
		Dataset ds = CommonUtilitiesTest.constructDatasetFromJSONArray(resultJsonArray);
		Result expectedResult = new Result();
		expectedResult.setDataSet(ds);
		
		JSONObject actualResult = new JSONObject();
		
		when(session.getId()).thenReturn("1");
		when(multiEntityBackendDelegate.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId))
		.thenReturn(expectedJson);
		when(CommonUtilities.constructResultFromJSONObject(expectedJson)).thenReturn(expectedResult);
		
		when(MemoryManager.getFromCache("companyLegalUnits"+ session.toString())).thenReturn(null);
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.MULTI_ENTITY_MS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.OP_GET_COMPANY_LEGAL_UNITS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(postParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(expectedJson.toString());
		when(CommonUtilities.getStringAsJSONArray(expectedJson.toString())).thenReturn(resultJsonArray);
		when(CommonUtilities.getStringAsJSONObject(expectedJson.toString())).thenReturn(expectedJson);
		
		MultiEntityBackendDelegateImpl multiEntityBackendDelegateImpl = new MultiEntityBackendDelegateImpl();
		actualResult = multiEntityBackendDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, session);
		
		assertEquals(actualResult.getJSONArray("companyLegalUnits").toString(), expectedJson.getJSONArray("financialInstitutions").toString());
//		System.out.println(actualResult.getJSONArray("companyLegalUnits").toString());
//		System.out.println(expectedJson.getJSONArray("financialInstitutions").toString());
	}
	
	@Test
	public void testGetAllCompanyLegalUnitsFromMS() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = false;
		Object sessionId = new Object();
		headerMap.put("backendToken", authToken);
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetCompanyLegalUnitsResponse_ORDMS.json").getFile()).getPath();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
	
		JSONObject expectedJson = new JSONObject();
		JSONArray resultJsonArray = new JSONArray(getCompanyLEsResponseContent);
		expectedJson.put("financialInstitutions", resultJsonArray);
		expectedJson.put("opstatus", 0);
		
		Dataset ds = CommonUtilitiesTest.constructDatasetFromJSONArray(resultJsonArray);
		Result expectedResult = new Result();
		expectedResult.setDataSet(ds);
		
		JSONObject actualResult = new JSONObject();
		
		when(session.getId()).thenReturn("1");
		when(multiEntityBackendDelegate.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId))
		.thenReturn(expectedJson);
		when(CommonUtilities.constructResultFromJSONObject(expectedJson)).thenReturn(expectedResult);
		
		when(MemoryManager.getFromCache("companyLegalUnits"+ session.toString())).thenReturn(expectedJson);
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.MULTI_ENTITY_MS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(OperationName.OP_GET_COMPANY_LEGAL_UNITS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestHeaders(headerMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(postParametersMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(expectedJson.toString());
		when(CommonUtilities.getStringAsJSONArray(expectedJson.toString())).thenReturn(resultJsonArray);
		when(CommonUtilities.getStringAsJSONObject(expectedJson.toString())).thenReturn(expectedJson);
		
		MultiEntityBackendDelegateImpl multiEntityBackendDelegateImpl = new MultiEntityBackendDelegateImpl();
		actualResult = multiEntityBackendDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, session);
		
		assertEquals(actualResult.getJSONArray("companyLegalUnits").toString(), expectedJson.getJSONArray("financialInstitutions").toString());
//		System.out.println(actualResult.getJSONArray("companyLegalUnits").toString());
//		System.out.println(expectedJson.getJSONArray("financialInstitutions").toString());
	}
	
	@Test
	public void testGetAllCompanyLegalUnitsException() throws Exception{
		
		String authToken = "authTokenDummy";
		boolean isSingleEntity = true;
		Object sessionId = new Object();
		JSONObject expectedJson = null;
		sessionId = mock(Session.class);
		//when(MemoryManager.getFromCache("companyLegalUnits"+ sessionId.toString())).thenThrow(new RuntimeException());
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(ServiceId.MULTI_ENTITY_MS)).thenThrow(new RuntimeException());
		
		MultiEntityBackendDelegateImpl multiEntityBackendDelegateImpl = new MultiEntityBackendDelegateImpl();
		JSONObject actualResultObject = multiEntityBackendDelegateImpl.getAllCompanyLegalUnits(authToken, isSingleEntity, sessionId);
		
		assertEquals(actualResultObject, expectedJson);
		
	}
}


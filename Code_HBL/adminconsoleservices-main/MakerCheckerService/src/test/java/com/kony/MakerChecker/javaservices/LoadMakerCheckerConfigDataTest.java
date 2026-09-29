package com.kony.MakerChecker.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class LoadMakerCheckerConfigDataTest {
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	LoadMakerCheckerConfigData loadConfigDataService;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;

	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	// This will run before the first test case
	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
	}

	/*
	 * This will run after the last test case Cleanup is mandatory for staticmocks
	 * org.mockito.exceptions.base.MockitoException: For
	 * com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl, static mocking is
	 * already registered in the current thread
	 */
	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPServiceExecutorBuilder.close();
	}
	
	//This will run before each test case
	@Before
	public void executedBefore() throws Exception {

		
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		loadConfigDataService = new LoadMakerCheckerConfigData();

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);

		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		
		javaService2 = new LoadMakerCheckerConfigData();
	}
	
	@Test
	public void testInvoke() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigFullResponse.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.OPSTATUS, "0", UtilConstants.INT));
		expectedResult.addParam(new Param(UtilConstants.HTTP_STATUS_CODE, "0", UtilConstants.STRING));
        
		//Mock should return expected results
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKERCHECKERCRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.MAKER_CHECKER_CONFIG_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		//Actual call should return actualResults
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		
		//Compare expected and actual results. Here, Ideally we should check for success params
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
	}
}

package com.kony.adminconsole.multientity.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.multientity.javaservice.FetchMultiEntityOperation;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

//This annotation tells PowerMock to prepare certain classes for testing. 
//It prepares bytecode
@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })
public class FetchMultiEntityOperationTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	MultiEntityResource resource;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Log4j2Configurator log4jConfigurator;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	
	//This will run before the first test case
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
	}
	
	/*  This will run after the last test case
		Cleanup is mandatory for staticmocks
		org.mockito.exceptions.base.MockitoException: 
		For com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl, 
		static mocking is already registered in the current thread
	*/
	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}
	
	//This will run before each test case
	@Before
	public void executedBefore() throws Exception {

		
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		resource = mock(MultiEntityResource.class);

		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(MultiEntityResource.class)).thenReturn(resource);
		when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);
		
		javaService2 = new FetchMultiEntityOperation();
	}

	

	@Test
	public void testInvoke() throws Exception {

		Result expectedResult = new Result();
		expectedResult.addParam(new Param("opstatus", "0", FabricConstants.INT));
		expectedResult.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));
        
		Result result = new Result();
		//Mock should return expected results
		when(resource.getAllCompanyLegalUnits(request)).thenReturn(result);
		
		//Actual call should return actualResults
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		
		//Compare expected and actual results. Here, Ideally we should check for success params
		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
	}

	@Test
	public void testInvokeException() throws Exception {

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(ACConstants.DBP_ERROR_CODE, ErrorCodeEnum.ERR_20001.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ACConstants.DBP_ERROR_MESSAGE, ErrorCodeEnum.ERR_20001.getMessage()));

		when(resource.getAllCompanyLegalUnits(request)).thenThrow(new RuntimeException());
		
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		
		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
		assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
	}
}

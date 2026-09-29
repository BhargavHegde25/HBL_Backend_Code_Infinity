package com.kony.MakerChecker.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.javaservice.CommonRequestDetailsOperation;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })
public class CommonRequestDetailsOperationTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	MakerCheckerResource resource;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
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
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		resource = mock(MakerCheckerResource.class);

		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(MakerCheckerResource.class)).thenReturn(resource);
		when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);

		javaService2 = new CommonRequestDetailsOperation();
	}

	@Test
	public void testInvoke() throws Exception {
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.OPSTATUS, "0", UtilConstants.INT));
		expectedResult.addParam(new Param(UtilConstants.HTTP_STATUS_CODE, "0", UtilConstants.STRING));
		when(resource.parseApprovalRequestDetails(methodID, inputArray, request, response)).thenReturn(expectedResult);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(2, actualResult.getAllParams().size());
		assertEquals(actualResult.getParamByName(UtilConstants.OPSTATUS),
				expectedResult.getParamByName(UtilConstants.OPSTATUS));
	}
	
	
	 @Test
		public void testInvokeException() throws Exception {

			Result expectedResult = new Result();
			expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10028.getErrorCodeAsString()));
			expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10028.getMessage()));

			when(resource.parseApprovalRequestDetails(methodID,inputArray,request,response)).thenThrow(new RuntimeException());
			
			actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
			
			assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
			assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
		}
	

}

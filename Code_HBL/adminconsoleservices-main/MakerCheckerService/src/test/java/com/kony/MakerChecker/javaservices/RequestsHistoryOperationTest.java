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
import com.kony.makerchecker.javaservice.RequestsHistoryOperation;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class })
public class RequestsHistoryOperationTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	MakerCheckerResource resource;
	String methodID;
	Object[] inputArray;
	DataControllerRequest request;
	DataControllerResponse response;
	JavaService2 javaService2;
	Result actualResult;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
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
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(MakerCheckerResource.class)).thenReturn(resource);

		javaService2 = new RequestsHistoryOperation();
	}

	@Test
	public void testInvoke() throws Exception {

		Result actualResult = new Result();
		actualResult.addParam(new Param(UtilConstants.OPSTATUS, "0", UtilConstants.INT));
		actualResult.addParam(new Param(UtilConstants.HTTP_STATUS_CODE, "0", UtilConstants.STRING));
		
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.OPSTATUS, "0", UtilConstants.INT));
		expectedResult.addParam(new Param(UtilConstants.HTTP_STATUS_CODE, "0", UtilConstants.STRING));

		when(resource.requestsHistory(methodID, inputArray, request, response)).thenReturn(actualResult);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);

		assertEquals(actualResult.getParamValueByName(UtilConstants.OPSTATUS),
				expectedResult.getParamValueByName(UtilConstants.OPSTATUS));
		assertEquals(actualResult.getParamValueByName(UtilConstants.HTTP_STATUS_CODE),
				expectedResult.getParamValueByName(UtilConstants.HTTP_STATUS_CODE));
	}
	
	@Test
	public void testInvokeWhenException() throws Exception {

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, "10005", UtilConstants.INT));
		
		when(resource.requestsHistory(methodID, inputArray, request, response)).thenThrow(new RuntimeException());
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);

		assertEquals(actualResult.getParamValueByName(UtilConstants.DBP_ERR_CODE),
				expectedResult.getParamValueByName(UtilConstants.DBP_ERR_CODE));
		
	}
}

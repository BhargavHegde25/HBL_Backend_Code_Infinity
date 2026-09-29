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
import com.kony.makerchecker.javaservice.WithdrawRequest;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class })
public class WithdrawRequestTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	MakerCheckerResource makerCheckerResource;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodId;
	Object[] inputArray;

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
		methodId = "METHODID";
		inputArray = new Object[2];

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		makerCheckerResource = mock(MakerCheckerResource.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(MakerCheckerResource.class)).thenReturn(makerCheckerResource);

		javaService2 = new WithdrawRequest();
	}

	@Test
	public void testInvoke() throws Exception {
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.OPSTATUS, "0", UtilConstants.INT));
		expectedResult.addParam(new Param(UtilConstants.HTTP_STATUS_CODE, "0", UtilConstants.STRING));

		when(makerCheckerResource.withdrawRequest(methodId, inputArray, request, response)).thenReturn(expectedResult);
		Result actualResult = (Result) javaService2.invoke(methodId, inputArray, request, response);

		assertEquals(2, actualResult.getAllParams().size());
		assertEquals(expectedResult.getParamByName(UtilConstants.OPSTATUS),
				actualResult.getParamByName(UtilConstants.OPSTATUS));
		assertEquals(expectedResult.getParamByName(UtilConstants.HTTP_STATUS_CODE),
				actualResult.getParamByName(UtilConstants.HTTP_STATUS_CODE));
	}
	
	@Test
	public void testInvokeException() throws Exception {
		when(makerCheckerResource.withdrawRequest(methodId, inputArray, request, response)).thenThrow(new RuntimeException());
		Result actualResult = (Result) javaService2.invoke(methodId, inputArray, request, response);

		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10005.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10005.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

}

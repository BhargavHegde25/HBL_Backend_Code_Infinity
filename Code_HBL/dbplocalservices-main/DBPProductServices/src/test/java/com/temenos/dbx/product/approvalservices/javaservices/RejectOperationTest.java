package com.temenos.dbx.product.approvalservices.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.resource.api.ApprovalQueueResource;
	

public class RejectOperationTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	ApprovalQueueResource approvalQueueResource;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	private static MockedStatic<JSONUtils> JSONUtilsMockedStatic;
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
		JSONUtilsMockedStatic = Mockito.mockStatic(JSONUtils.class);
	}
	

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
		JSONUtilsMockedStatic.close();
	}

	@Before
	public void executedBefore() throws IOException {

		javaService2 = new RejectOperation();
		methodID = "METHODID";
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		approvalQueueResource = mock(ApprovalQueueResource.class);
		actualResult = new Result();
		resourceFactory = mock(ResourceFactory.class);	
	}

	@Test
	public void testInvoke() throws Exception {
		Result result = mock(Result.class);
		
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getResource(ApprovalQueueResource.class))
				.thenReturn(approvalQueueResource);
		when(approvalQueueResource.reject(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(result);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	@Test
	public void testInvokeException() throws Exception {

		Result expectedResult = new Result();
		expectedResult.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_12000.getErrorCodeAsString()));
		expectedResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_12000.getMessage()));
		
		Mockito.doThrow(RuntimeException.class).when(approvalQueueResource)
        .reject(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any());
		
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
		assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
	}
}

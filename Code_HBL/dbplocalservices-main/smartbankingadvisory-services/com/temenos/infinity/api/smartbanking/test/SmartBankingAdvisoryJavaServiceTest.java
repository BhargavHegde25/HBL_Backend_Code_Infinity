package com.temenos.infinity.api.smartbanking.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.javaservices.GetAccountsReceivableOperation;
import com.temenos.infinity.smartbanking.advisory.resource.api.SmartBankingAdvisoryResource;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, DBPServiceExecutorBuilder.class,
		DBPServiceExecutor.class })
public class SmartBankingAdvisoryJavaServiceTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;

	@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })
	public class FetchMultiEntityOperationTest {

		DBPAPIAbstractFactory dbpAPIAbstractFactory;
		ResourceFactory resourceFactory;
		JavaService2 javaService2;
		SmartBankingAdvisoryResource resource;
		DataControllerRequest request;
		DataControllerResponse response;
		String methodID;
		Object[] inputArray;
		Result actualResult;
		Log4j2Configurator log4jConfigurator;

		private MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
		private MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;

		@BeforeClass
		public void init() {
			mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
			mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		}

		@AfterClass
		public void cleanup() {
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
			resource = mock(SmartBankingAdvisoryResource.class);

			dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
			log4jConfigurator = mock(Log4j2Configurator.class);
			resourceFactory = mock(ResourceFactory.class);

			when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
			when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
			when(resourceFactory.getResource(SmartBankingAdvisoryResource.class)).thenReturn(resource);
			when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);

			javaService2 = new GetAccountsReceivableOperation();
		}

		@Test
		public void testInvoke() throws Exception {

			Result expectedResult = new Result();
			expectedResult.addParam(new Param("opstatus", "0", FabricConstants.INT));
			expectedResult.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));

			Result result = new Result();
			when(resource.getAccountsReceivable(methodID, inputArray, request, response)).thenReturn(result);
			actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
			assertEquals(actualResult.getParamByName("opstatus"), expectedResult.getParamByName("opstatus"));
			assertEquals(actualResult.getParamByName("httpStatusCode"),
					expectedResult.getParamByName("httpStatusCode"));
		}

	}
}

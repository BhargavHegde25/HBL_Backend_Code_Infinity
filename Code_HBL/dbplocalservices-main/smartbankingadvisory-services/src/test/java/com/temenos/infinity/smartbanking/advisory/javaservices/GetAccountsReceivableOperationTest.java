package com.temenos.infinity.smartbanking.advisory.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockedStatic.Verification;
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
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.javaservices.GetAccountsReceivableOperation;
import com.temenos.infinity.smartbanking.advisory.resource.api.SmartBankingAdvisoryResource;
import com.temenos.logger.Logger;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPServiceExecutorBuilder.class, DBPServiceExecutor.class })
public class GetAccountsReceivableOperationTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Session session;
	SmartBankingAdvisoryResource resource;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
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

		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
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
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetAccountsReceivableFinalResponse.json").toURI()).toString();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
		result = Utilities.constructResultFromJSONObject(new JSONObject(getCompanyLEsResponseContent));

		when(resource.getAccountsReceivable(methodID, inputArray, dcRequest, dcResponse)).thenReturn(result);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName("opstatus"), expectedResult.getParamValueByName("opstatus"));
		assertEquals(actualResult.getParamValueByName("httpStatusCode"),
				expectedResult.getParamValueByName("httpStatusCode"));
	}

}

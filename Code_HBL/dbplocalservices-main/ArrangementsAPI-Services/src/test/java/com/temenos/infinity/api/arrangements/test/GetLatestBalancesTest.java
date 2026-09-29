/**
 * 
 */
package com.temenos.infinity.api.arrangements.test;

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
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.javaservice.GetLatestBalances;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;


/**
 * @author amitabh.kotha
 *
 */
@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPAPIAbstractFactory.class, Log4j2Configurator.class})
public class GetLatestBalancesTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	ArrangementsResource arrangementsResource;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
	}
	

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
	}
	
	@Before
	public void executedBefore() throws IOException {
		javaService2 = new GetLatestBalances();
		methodID = "METHODID";
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		actualResult = new Result();
		log4j2Configurator = mock(Log4j2Configurator.class);
		resourceFactory = mock(ResourceFactory.class);
		arrangementsResource = mock(ArrangementsResource.class);
	}

	@Test
	public void testInvoke() throws Exception {
		Result result = mock(Result.class);
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		when(Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(result.getOpstatusParamValue(), actualResult.getOpstatusParamValue());
		assertEquals(result.getHttpStatusCodeParamValue(), actualResult.getHttpStatusCodeParamValue());
	}
}

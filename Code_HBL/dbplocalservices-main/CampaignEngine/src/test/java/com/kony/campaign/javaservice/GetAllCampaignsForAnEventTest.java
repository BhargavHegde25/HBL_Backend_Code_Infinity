package com.kony.campaign.javaservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.campaign.resource.api.CampaignResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public class GetAllCampaignsForAnEventTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	CampaignResource resource;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	
	//This will run once for the class before all the test methods
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}

	@Before //This will run before each test case
	public void setUp() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();

		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		resource = mock(CampaignResource.class);

		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(CampaignResource.class)).thenReturn(resource);
		
		javaService2 = new GetAllCampaignsForAnEvent();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvoke() throws Exception {
		Result expectedResult = new Result();
		expectedResult.addParam(new Param("opstatus", "0", FabricConstants.INT));
		expectedResult.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));
        
		Result result = new Result();
		//Mock should return expected results
		when(resource.getAllCampaignsForAnEvent(methodID, inputArray, request, response)).thenReturn(result);
		result.addParam(new Param("opstatus", "0", FabricConstants.INT));
		result.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));
		
		//Actual call should return actualResults
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		
		//Compare expected and actual results. Here, Ideally we should check for success params
		assertEquals(actualResult.getOpstatusParamValue(), expectedResult.getOpstatusParamValue());
		assertEquals(actualResult.getHttpStatusCodeParamValue(), expectedResult.getHttpStatusCodeParamValue());
	}
}

package com.kony.adminconsole.service.termandcondition.javaservices;

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
import com.kony.adminconsole.service.termandcondition.resource.api.TnCResource;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * @author shubham.ahuja
 *
 */
@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPAPIAbstractFactory.class, Log4j2Configurator.class })
public class TermAndConditionManageServiceTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	TnCResource tncResource;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	TermAndConditionManageService tncManageService;
	String methodID;
	Object[] inputArray;
	Result actualResult;
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

		javaService2 = new TermAndConditionManageService();
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		tncResource = mock(TnCResource.class);
		actualResult = new Result();
		resourceFactory = mock(ResourceFactory.class);
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(TnCResource.class)).thenReturn(tncResource);
	}

	@Test
	public void testCreateTermsAndConditionsVersionInvoke() throws Exception {

		Result result = mock(Result.class);
		methodID = "createTermsAndConditionsVersion";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.createTermsAndConditionsVersion(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	
	@Test
	public void testEditTermsAndConditionsInvoke() throws Exception {

		Result result = mock(Result.class);
		methodID = "editTermsAndConditions";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.editTermsAndConditions(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	
	@Test
	public void testGetTermsAndConditions() throws Exception {

		Result result = mock(Result.class);
		methodID = "getTermsAndConditions";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.getTermsAndConditions(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}	
	
	@Test
	public void testGetAllTermsAndConditions() throws Exception {

		Result result = mock(Result.class);
		methodID = "getAllTermsAndConditions";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.getAllTermsAndConditions(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	
	@Test
	public void testDeleteTermsAndConditionsVersion() throws Exception {

		Result result = mock(Result.class);
		methodID = "deleteTermsAndConditionsVersion";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.deleteTermsAndConditionsVersion(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	
	@Test
	public void testGetRequiredTermsAndConditions() throws Exception {

		Result result = mock(Result.class);
		methodID = "getRequiredTermsAndConditions";
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance())
				.thenReturn(log4j2Configurator);
		
		when(tncResource.getRequiredTermsAndConditions(methodID, inputArray, request, response)).thenReturn(result);

		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	

}

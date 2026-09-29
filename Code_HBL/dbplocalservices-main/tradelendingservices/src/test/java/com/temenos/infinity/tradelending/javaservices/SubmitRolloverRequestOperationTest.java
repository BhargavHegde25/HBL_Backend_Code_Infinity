/**
 * 
 */
package com.temenos.infinity.tradelending.javaservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockedStatic.Verification;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;
import com.temenos.infinity.tradelending.resource.api.RolloverRequestResource;

/**
 * @author mrunalini.adepu
 *
 */
@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPAPIAbstractFactory.class, Log4j2Configurator.class})
public class SubmitRolloverRequestOperationTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	static RolloverRequestResource rolloverRequestResource;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	static RolloverRequestDTO rolloverRequestDTO;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	private static MockedStatic<JSONUtils> JSONUtilsMockedStatic;
	
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
		JSONUtilsMockedStatic = Mockito.mockStatic(JSONUtils.class);
		try {
			rolloverRequestDTO = mock(RolloverRequestDTO.class);
			rolloverRequestResource = mock(RolloverRequestResource.class);
			JSONUtilsMockedStatic.when((MockedStatic.Verification) JSONUtils.parse(new JSONObject().toString(), RolloverRequestDTO.class)).thenReturn(rolloverRequestDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getResource(RolloverRequestResource.class))
		.thenReturn(rolloverRequestResource);
		
	}
	

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
		JSONUtilsMockedStatic.close();
	}

	@Before
	public void executedBefore() throws IOException {

		javaService2 = new SubmitRolloverRequestOperation();
		methodID = "METHODID";
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		actualResult = new Result();
		resourceFactory = mock(ResourceFactory.class);
		
	}

	@Test
	public void testInvoke() throws Exception {

		Result result = mock(Result.class);
		
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
		
		when(rolloverRequestResource.submitRolloverRequest(rolloverRequestDTO,request)).thenReturn(result);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
	
	@Test(expected = Exception.class)
    public void testInvoke_ThrowsException() throws Exception {
        log4j2ConfiguratorMockedStatic.when((MockedStatic.Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
        when(rolloverRequestResource.submitRolloverRequest(rolloverRequestDTO, null)).thenThrow(Exception.class);
        assertThrows(Exception.class, () -> javaService2.invoke(methodID, inputArray, request, response));
    }
}

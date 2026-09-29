/**
 * 
 */
package com.temenos.infinity.tradelending.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockedStatic.Verification;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.businessdelegate.api.RolloverRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;
import com.temenos.infinity.tradelending.resource.api.RolloverRequestResource;

/**
 * @author mrunalini.adepu
 *
 */
@PrepareForTest({ RolloverRequestResourceImpl.class,DBPAPIAbstractFactoryImpl.class })
public class RolloverRequestResourceImplTest {

	private RolloverRequestResource rolloverRequestResource;
	private BusinessDelegateFactory businessDelegateFactroy;
	private DBPAPIAbstractFactory dbpAPIAbstractFactory;
	private DataControllerRequest dcRequest;
	private DataControllerResponse dataControllerResponse;
	private String methodId = "methodId";
	private Object[] inputArray;
	private static final String PROPERTY_VALUE = "propertyValue";
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;

	private RolloverRequestDTO inputDto;
	static RolloverRequestBusinessDelegate rolloverRequestBusinessDelegate;
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		
	    try {
	    	rolloverRequestBusinessDelegate = mock(RolloverRequestBusinessDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}

		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBusinessDelegate(RolloverRequestBusinessDelegate.class))
		.thenReturn(rolloverRequestBusinessDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {

		rolloverRequestResource = new RolloverRequestResourceImpl();
		businessDelegateFactroy = mock(BusinessDelegateFactory.class);
		inputArray = new Object[10];
		dcRequest = mock(DataControllerRequest.class);
		dataControllerResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		inputDto = mock(RolloverRequestDTO.class);
		
	}
	
	@Test
	public void testSubmitRolloverRequest() throws Exception {
        Result result = new Result();
        
		String rolloverRequestResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("RolloverRequestPayload.json").getFile()).getPath();

		String rolloverRequestResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(rolloverRequestResponseMetaDataAndPIPath)));
		JSONObject rolloverRequestResponseobj = new JSONObject(rolloverRequestResponseMetaDataAndPIContent);
		RolloverRequestDTO rolloverRequestDto = JSONUtils.parse(rolloverRequestResponseobj.toString(), RolloverRequestDTO.class);

		when(rolloverRequestBusinessDelegate.createRolloverRequest(rolloverRequestDto, dcRequest))
		.thenReturn(rolloverRequestDto);
		
		result = rolloverRequestResource.submitRolloverRequest(rolloverRequestDto, dcRequest);
		
		assertEquals("10000003", result.getParamValueByName("rolloverRequestId"));
	}
}

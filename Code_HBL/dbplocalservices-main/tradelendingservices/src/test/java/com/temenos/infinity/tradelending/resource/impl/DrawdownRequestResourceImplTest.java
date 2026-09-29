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
import com.temenos.infinity.tradelending.businessdelegate.api.DrawdownRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;
import com.temenos.infinity.tradelending.resource.api.DrawdownRequestResource;

@PrepareForTest({ DrawdownRequestResourceImpl.class,DBPAPIAbstractFactoryImpl.class })
public class DrawdownRequestResourceImplTest {
	
	private DrawdownRequestResource drawdownRequestResource;
	private BusinessDelegateFactory businessDelegateFactroy;
	private DBPAPIAbstractFactory dbpAPIAbstractFactory;
	private DataControllerRequest dcRequest;
	private DataControllerResponse dataControllerResponse;
	private String methodId = "methodId";
	private Object[] inputArray;
	private static final String PROPERTY_VALUE = "propertyValue";
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;

	private DrawdownRequestDTO inputDto;
	static DrawdownRequestBusinessDelegate drawdownRequestBusinessDelegate;
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		
	    try {
	    	drawdownRequestBusinessDelegate = mock(DrawdownRequestBusinessDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}

		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBusinessDelegate(DrawdownRequestBusinessDelegate.class))
		.thenReturn(drawdownRequestBusinessDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {

		drawdownRequestResource = new DrawdownRequestResourceImpl();
		businessDelegateFactroy = mock(BusinessDelegateFactory.class);
		inputArray = new Object[10];
		dcRequest = mock(DataControllerRequest.class);
		dataControllerResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		inputDto = mock(DrawdownRequestDTO.class);
		
	}
	
	@Test
	public void testSubmitDrawdownRequest() throws Exception {
        Result result = new Result();
        
		String drawdownRequestResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("DrawdownRequestPayload.json").getFile()).getPath();

		String drawdownRequestResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(drawdownRequestResponseMetaDataAndPIPath)));
		JSONObject drawdownRequestResponseobj = new JSONObject(drawdownRequestResponseMetaDataAndPIContent);
		DrawdownRequestDTO drawdownRequestDTO = JSONUtils.parse(drawdownRequestResponseobj.toString(), DrawdownRequestDTO.class);

		when(drawdownRequestBusinessDelegate.createDrawdownRequest(drawdownRequestDTO, dcRequest))
		.thenReturn(drawdownRequestDTO);
		
		result = drawdownRequestResource.submitDrawdownRequest(drawdownRequestDTO, dcRequest);
		
		assertEquals("10000003", result.getParamValueByName("drawdownRequestId"));
	}

}

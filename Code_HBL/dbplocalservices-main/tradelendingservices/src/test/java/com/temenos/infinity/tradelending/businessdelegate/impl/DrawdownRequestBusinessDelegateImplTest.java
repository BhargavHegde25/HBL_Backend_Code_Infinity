package com.temenos.infinity.tradelending.businessdelegate.impl;

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

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.DrawdownRequestBackendDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.DrawdownRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;

public class DrawdownRequestBusinessDelegateImplTest {
	
	DrawdownRequestBusinessDelegate drawdownRequestBusinessDelegate;
	DataControllerRequest request;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactroy;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	DrawdownRequestDTO inputDto;
	static DrawdownRequestBackendDelegate drawdownRequestBackendDelegate;
	

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		try {
			drawdownRequestBackendDelegate = mock(DrawdownRequestBackendDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBackendDelegate(DrawdownRequestBackendDelegate.class))
		.thenReturn(drawdownRequestBackendDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		drawdownRequestBusinessDelegate = new DrawdownRequestBusinessDelegateImpl();
		request = mock(DataControllerRequest.class);
		inputDto = mock(DrawdownRequestDTO.class);
	    dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
	    businessDelegateFactroy = mock(BusinessDelegateFactory.class);   
	}
	
	@Test
	public void createDrawdownRequestTest() throws DBPApplicationException, Exception {	
		
		String drawdownRequestResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("DrawdownRequestPayload.json").getFile()).getPath();

		String drawdownRequestResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(drawdownRequestResponseMetaDataAndPIPath)));
		JSONObject drawdownRequestResponseobj = new JSONObject(drawdownRequestResponseMetaDataAndPIContent);
		DrawdownRequestDTO drawdownRequestDto = JSONUtils.parse(drawdownRequestResponseobj.toString(), DrawdownRequestDTO.class);

		when(drawdownRequestBackendDelegate.createDrawdownRequest(drawdownRequestDto, request)).thenReturn(drawdownRequestDto);
		
		drawdownRequestDto = drawdownRequestBackendDelegate.createDrawdownRequest(drawdownRequestDto, request);
		
		assertEquals("10000003", drawdownRequestDto.getDrawdownRequestId());
	}

}

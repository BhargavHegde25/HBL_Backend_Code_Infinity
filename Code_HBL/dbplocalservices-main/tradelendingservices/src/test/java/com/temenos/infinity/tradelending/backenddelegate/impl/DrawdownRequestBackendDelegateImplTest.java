package com.temenos.infinity.tradelending.backenddelegate.impl;

import static org.junit.Assert.assertEquals;
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

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.DrawdownRequestBackendDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;
import com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils;
import com.temenos.infinity.tradelending.utils.TradeLendingDBXDBUtils;

public class DrawdownRequestBackendDelegateImplTest {
	DrawdownRequestBackendDelegate drawdownRequestBackendDelegate;
	DataControllerRequest request;
	BusinessDelegateFactory businessDelegateFactroy;
	TradeLendingDBXDBUtils tradeLendingDBXDBUtils;
	private static MockedStatic<TradeLendingDBXDBUtils> mockedStatic;
	private static MockedStatic<TradeLendingCommonUtils> tradelendingCommonUtilsMockedStatic;
	private static MockedStatic<HelperMethods> helperMethodsMockedStatic;
	private static MockedStatic<EnvironmentConfigurationsHandler> environmentConfigurationsHandlerMockedStatic;

	DrawdownRequestDTO inputDto;

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(TradeLendingDBXDBUtils.class);
		tradelendingCommonUtilsMockedStatic = Mockito.mockStatic(TradeLendingCommonUtils.class);
		helperMethodsMockedStatic = Mockito.mockStatic(HelperMethods.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		tradelendingCommonUtilsMockedStatic.close();
		helperMethodsMockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		drawdownRequestBackendDelegate = new DrawdownRequestBackendDelegateImpl();

		tradeLendingDBXDBUtils = Mockito.mock(TradeLendingDBXDBUtils.class);
		request = Mockito.mock(DataControllerRequest.class);
		inputDto = Mockito.mock(DrawdownRequestDTO.class);

	}

	@Test
	public void testCreateDrawdownRequest() throws DBPApplicationException, Exception {

		String drawdownRequestAndPIPath = new File(
				getClass().getClassLoader().getResource("DrawdownRequestPayload.json").getFile()).getPath();

		String drawdownRequestMetaDataAndPIContent = new String(
				Files.readAllBytes(Paths.get(drawdownRequestAndPIPath)));

		JSONObject saveDrawdownRequestMetaDataAndPIObj = new JSONObject(
				drawdownRequestMetaDataAndPIContent);

		DrawdownRequestDTO saveDrawdownRequestDto = JSONUtils
				.parse(saveDrawdownRequestMetaDataAndPIObj.toString(), DrawdownRequestDTO.class);

		when(request.getParameterNames()).thenReturn(saveDrawdownRequestMetaDataAndPIObj.keys());
		
		mockedStatic.when(() -> TradeLendingDBXDBUtils.getInstance()).thenReturn(tradeLendingDBXDBUtils);
		tradelendingCommonUtilsMockedStatic.when(() -> TradeLendingCommonUtils.getCoreCustomerId(request)).thenReturn("100100");
		helperMethodsMockedStatic.when(() -> HelperMethods.getUserIdFromSession(request)).thenReturn("rmuser");
		
		when(tradeLendingDBXDBUtils.addDataControllerRequest(request)).thenReturn(tradeLendingDBXDBUtils);
		when(tradeLendingDBXDBUtils.addRecord()).thenReturn(tradeLendingDBXDBUtils);
		when(tradeLendingDBXDBUtils.addRequestBody(saveDrawdownRequestDto)).thenReturn(tradeLendingDBXDBUtils);
		when(tradeLendingDBXDBUtils.addModule("DrawdownRequestModule")).thenReturn(tradeLendingDBXDBUtils);
		when(tradeLendingDBXDBUtils.makeRequest()).thenReturn(tradeLendingDBXDBUtils);
		when(tradeLendingDBXDBUtils.getResponse()).thenReturn(saveDrawdownRequestMetaDataAndPIObj);
		
		
		DrawdownRequestDTO responseDTO = drawdownRequestBackendDelegate.createDrawdownRequest(saveDrawdownRequestDto, request);

		assertEquals("10000003", responseDTO.getDrawdownRequestId());
	}

}

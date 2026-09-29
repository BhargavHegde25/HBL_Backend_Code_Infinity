package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorFundingRequestBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceSRMSUtils;

public class AnchorFundingRequestBackendDelegateImplTest {
	AnchorFundingRequestBackendDelegate anchorFundingRequestBackendDelegate;
	DataControllerRequest request;
	BusinessDelegateFactory businessDelegateFactroy;
	TradeSupplyFinanceSRMSUtils tradeSupplyFinanceSRMSUtils;
	private static MockedStatic<TradeSupplyFinanceSRMSUtils> mockedStatic;
	private static MockedStatic<TradeSupplyFinanceCommonUtils> tradeSupplyFinanceCommonUtilsMockedStatic;
	private static MockedStatic<HelperMethods> helperMethodsMockedStatic;
	private static MockedStatic<EnvironmentConfigurationsHandler> environmentConfigurationsHandlerMockedStatic;

	AnchorFundingRequestDTO inputDto;

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(TradeSupplyFinanceSRMSUtils.class);
		tradeSupplyFinanceCommonUtilsMockedStatic = Mockito.mockStatic(TradeSupplyFinanceCommonUtils.class);
		helperMethodsMockedStatic = Mockito.mockStatic(HelperMethods.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		tradeSupplyFinanceCommonUtilsMockedStatic.close();
		helperMethodsMockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		anchorFundingRequestBackendDelegate = new AnchorFundingRequestBackendDelegateImpl();

		tradeSupplyFinanceSRMSUtils = Mockito.mock(TradeSupplyFinanceSRMSUtils.class);
		request = Mockito.mock(DataControllerRequest.class);
		inputDto = Mockito.mock(AnchorFundingRequestDTO.class);

	}

	@Test
	public void testcreateAnchorFundingRequest() throws DBPApplicationException, Exception {

		String SaveAnchorFundingRequestAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorFundingRequest.json").getFile()).getPath();

		String saveAnchorFundingRequestMetaDataAndPIContent = new String(
				Files.readAllBytes(Paths.get(SaveAnchorFundingRequestAndPIPath)));

		JSONObject saveAnchorFundingRequestMetaDataAndPIObj = new JSONObject(
				saveAnchorFundingRequestMetaDataAndPIContent);

		AnchorFundingRequestDTO saveAnchorFundingRequestDto = JSONUtils
				.parse(saveAnchorFundingRequestMetaDataAndPIObj.toString(), AnchorFundingRequestDTO.class);

		when(request.getParameterNames()).thenReturn(saveAnchorFundingRequestMetaDataAndPIObj.keys());

		String receivableSingleBillResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String saveAnchorFundingResponseMetaDataAndPIContent = new String(
				Files.readAllBytes(Paths.get(receivableSingleBillResponseMetaDataAndPIPath)));
		JSONObject saveAnchorFundingResponseobj = new JSONObject(saveAnchorFundingResponseMetaDataAndPIContent);
		AnchorFundingRequestDTO saveAnchorFundingResponseDto = JSONUtils.parse(saveAnchorFundingResponseobj.toString(),
				AnchorFundingRequestDTO.class);

		mockedStatic.when(() -> TradeSupplyFinanceSRMSUtils.invoke()).thenReturn(tradeSupplyFinanceSRMSUtils);
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getCoreCustomerId(request)).thenReturn("100100");
		helperMethodsMockedStatic.when(() -> HelperMethods.getUserIdFromSession(request)).thenReturn("rmuser");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getScfBackend()).thenReturn("SRMS");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getUniqueIdParamName("SRMS")).thenReturn("fundingRequestId");
		
		when(tradeSupplyFinanceSRMSUtils.createOrder()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addRequestBody(saveAnchorFundingRequestDto))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addDataControllerRequest(request)).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addTypeAndSubType("AnchorFundingRequestType", "AnchorFundingRequestSubType"))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.sendRequest()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.fetchResponse()).thenReturn(saveAnchorFundingResponseobj);

		AnchorFundingRequestDTO responseDTO = anchorFundingRequestBackendDelegate
				.createAnchorFundingRequest(saveAnchorFundingRequestDto, request);

		assertEquals("AFR23307UR3YI", responseDTO.getFundingRequestId());
	}

	@Test
	public void testUpdateAnchorFundingRequest() throws DBPApplicationException, Exception {

		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject fundingRequestReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(fundingRequestReq.toString(),
				AnchorFundingRequestDTO.class);

		mockedStatic.when(() -> TradeSupplyFinanceSRMSUtils.invoke()).thenReturn(tradeSupplyFinanceSRMSUtils);
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getCoreCustomerId(request)).thenReturn("100100");
		helperMethodsMockedStatic.when(() -> HelperMethods.getUserIdFromSession(request)).thenReturn("rmuser");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getScfBackend()).thenReturn("SRMS");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getUniqueIdParamName("SRMS")).thenReturn("fundingRequestId");
		
		when(tradeSupplyFinanceSRMSUtils.updateOrder()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addServiceRequestId(anchorFundingRequestDTO.getFundingRequestId()))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addRequestBody(anchorFundingRequestDTO))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addDataControllerRequest(request)).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addTypeAndSubType("ReceivableSingleBillType", "ReceivableSingleBillSubType"))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.sendRequest()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.fetchResponse()).thenReturn(fundingRequestReq);

		AnchorFundingRequestDTO responseDTO = anchorFundingRequestBackendDelegate
				.updateAnchorFundingRequest(anchorFundingRequestDTO, request);

		assertEquals("AFR23307UR3YI", responseDTO.getFundingRequestId());
	}
	
	@Test
	public void testgetAllFundingRequest() throws DBPApplicationException, Exception {
		
		List<AnchorFundingRequestDTO> fundindRequests = new ArrayList<AnchorFundingRequestDTO>();
		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject fundingRequestReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(fundingRequestReq.toString(),
				AnchorFundingRequestDTO.class);
		fundindRequests.add(anchorFundingRequestDTO);

		mockedStatic.when(() -> TradeSupplyFinanceSRMSUtils.invoke()).thenReturn(tradeSupplyFinanceSRMSUtils);
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getCoreCustomerId(request)).thenReturn("100100");
		helperMethodsMockedStatic.when(() -> HelperMethods.getUserIdFromSession(request)).thenReturn("rmuser");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getScfBackend()).thenReturn("SRMS");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getUniqueIdParamName("SRMS")).thenReturn("fundingRequestId");
		
		when(tradeSupplyFinanceSRMSUtils.addDTO(AnchorFundingRequestDTO.class)).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addDataControllerRequest(request)).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addTypeAndSubType("AnchorFundingRequestType", "AnchorFundingRequestSubType"))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.getOrders()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.sendRequest()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.fetchOrdersResponseWithDTO()).thenReturn(fundindRequests);

		fundindRequests = anchorFundingRequestBackendDelegate
				.getAllFundingRequest(request);
		anchorFundingRequestDTO = fundindRequests.get(0);

		assertEquals("AFR23307UR3YI", anchorFundingRequestDTO.getFundingRequestId());
	}
}

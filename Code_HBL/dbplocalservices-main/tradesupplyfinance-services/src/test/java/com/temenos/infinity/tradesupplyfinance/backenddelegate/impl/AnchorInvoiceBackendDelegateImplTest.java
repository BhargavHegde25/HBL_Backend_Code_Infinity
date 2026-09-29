package com.temenos.infinity.tradesupplyfinance.backenddelegate.impl;

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
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceSRMSUtils;

public class AnchorInvoiceBackendDelegateImplTest {
	AnchorInvoiceBackendDelegate anchorInvoiceBackendDelegate;
	DataControllerRequest request;
	BusinessDelegateFactory businessDelegateFactroy;
	TradeSupplyFinanceSRMSUtils tradeSupplyFinanceSRMSUtils;
	private static MockedStatic<TradeSupplyFinanceSRMSUtils> mockedStatic;
	private static MockedStatic<TradeSupplyFinanceCommonUtils> tradeSupplyFinanceCommonUtilsMockedStatic;
	private static MockedStatic<HelperMethods> helperMethodsMockedStatic;

	AnchorInvoiceDTO inputDto;

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
		anchorInvoiceBackendDelegate = new AnchorInvoiceBackendDelegateImpl();

		tradeSupplyFinanceSRMSUtils = Mockito.mock(TradeSupplyFinanceSRMSUtils.class);
		request = Mockito.mock(DataControllerRequest.class);
		inputDto = Mockito.mock(AnchorInvoiceDTO.class);
	}
	
	@Test
	public void testupdateAnchorInvoice() throws DBPApplicationException, Exception {

		String approveAnchorInvoiceResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorInvoiceResponse.json").getFile()).getPath();

		String approveAnchorInvoiceResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(approveAnchorInvoiceResponseMetaDataAndPIPath)));
		JSONObject approveAnchorInvoiceResponseobj = new JSONObject(approveAnchorInvoiceResponseMetaDataAndPIContent);
		AnchorInvoiceDTO approveAnchorInvoiceRequestDto = JSONUtils.parse(approveAnchorInvoiceResponseobj.toString(), AnchorInvoiceDTO.class);


		mockedStatic.when(() -> TradeSupplyFinanceSRMSUtils.invoke()).thenReturn(tradeSupplyFinanceSRMSUtils);
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getCoreCustomerId(request)).thenReturn("100100");
		helperMethodsMockedStatic.when(() -> HelperMethods.getUserIdFromSession(request)).thenReturn("rmuser");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getScfBackend()).thenReturn("SRMS");
		tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getUniqueIdParamName("SRMS")).thenReturn("invoiceReference");
		
		when(tradeSupplyFinanceSRMSUtils.updateOrder()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addServiceRequestId(approveAnchorInvoiceRequestDto.getInvoiceReference()))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addRequestBody(approveAnchorInvoiceRequestDto))
				.thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.addDataControllerRequest(request)).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.sendRequest()).thenReturn(tradeSupplyFinanceSRMSUtils);
		when(tradeSupplyFinanceSRMSUtils.fetchResponse()).thenReturn(approveAnchorInvoiceResponseobj);

		AnchorInvoiceDTO responseDTO = anchorInvoiceBackendDelegate.updateAnchorInvoice(approveAnchorInvoiceRequestDto, request);

		assertEquals("CFI23340IT9LU", responseDTO.getInvoiceReference());
	}
}

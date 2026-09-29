package com.temenos.infinity.tradesupplyfinance.resource.impl;

import static com.temenos.infinity.tradesupplyfinance.config.TradeSupplyFinanceAPIServices.TRANSACT_SCF_CREATE_INVOICE;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.HTTP_HEADER_X_KONY_AUTHORIZATION;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.HTTP_HEADER_X_KONY_REPORTING_PARAMS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorInvoiceResource;
import com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils;

@PrepareForTest({ AnchorInvoiceResourceImpl.class,DBPAPIAbstractFactoryImpl.class, CommonUtils.class})

public class AnchorInvoiceResourceImplTest {
	private AnchorInvoiceResource anchorInvoiceResource;
	private BusinessDelegateFactory businessDelegateFactroy;
	private DBPAPIAbstractFactory dbpAPIAbstractFactory;
	private DataControllerRequest dcRequest;
	private DataControllerResponse dataControllerResponse;
	private String methodId = "methodId";
	private Object[] inputArray;
	private static final String PROPERTY_VALUE = "propertyValue";
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<CommonUtils> commonUtilsMockedStatic;
	private static MockedStatic<TradeSupplyFinanceCommonUtils> tradeSupplyFinanceCommonUtilsMockedStatic;
	private AnchorInvoiceDTO inputDto;
	static AnchorInvoiceBusinessDelegate anchorInvoiceBusinessDelegate;
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		commonUtilsMockedStatic = Mockito.mockStatic(CommonUtils.class);
		tradeSupplyFinanceCommonUtilsMockedStatic = Mockito.mockStatic(TradeSupplyFinanceCommonUtils.class);
	    try {
	    	anchorInvoiceBusinessDelegate = mock(AnchorInvoiceBusinessDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}

		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorInvoiceBusinessDelegate.class))
		.thenReturn(anchorInvoiceBusinessDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		commonUtilsMockedStatic.close();
		tradeSupplyFinanceCommonUtilsMockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {

		anchorInvoiceResource = new AnchorInvoiceResourceImpl();
		businessDelegateFactroy = mock(BusinessDelegateFactory.class);
		inputArray = new Object[10];
		dcRequest = mock(DataControllerRequest.class);
		dataControllerResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		inputDto = mock(AnchorInvoiceDTO.class);
		
	}
	
	@Test
	public void testapproveAnchorInvoice() throws Exception {
        Result result = new Result();
        
		String approveAnchorInvoiceResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorInvoiceResponse.json").getFile()).getPath();

		String approveAnchorInvoiceResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(approveAnchorInvoiceResponseMetaDataAndPIPath)));
		JSONObject approveAnchorInvoiceResponseobj = new JSONObject(approveAnchorInvoiceResponseMetaDataAndPIContent);
		AnchorInvoiceDTO approveAnchorInvoiceRequestDto = JSONUtils.parse(approveAnchorInvoiceResponseobj.toString(), AnchorInvoiceDTO.class);
		
		when(anchorInvoiceBusinessDelegate.getAnchorInvoiceById("CFI23340IT9LU", dcRequest))
		.thenReturn(approveAnchorInvoiceRequestDto);

		when(anchorInvoiceBusinessDelegate.updateAnchorInvoice(approveAnchorInvoiceRequestDto, dcRequest))
		.thenReturn(approveAnchorInvoiceRequestDto);
		
		
		Map<String, Object> inputMap = new ObjectMapper().convertValue(approveAnchorInvoiceRequestDto, Map.class);
		HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(HTTP_HEADER_X_KONY_AUTHORIZATION, null);
        headerMap.put(HTTP_HEADER_X_KONY_REPORTING_PARAMS, null);
        
        Result transactResult = JSONToResult.convert(String.valueOf(new JSONObject("{\"header\":{\"id\":\"CFI23340IT9LU\",\"status\":\"success\"}}}")));
        
        tradeSupplyFinanceCommonUtilsMockedStatic.when(() -> TradeSupplyFinanceCommonUtils.getHeadersMap(dcRequest)).thenReturn(headerMap);
        
        Map<String,Object> payload = new HashMap<String,Object>();
        payload.put("billType","INVOICE");
        payload.put("invoiceReference","CFI23340IT9LU");
        payload.put("status","Approved");
       
        
		commonUtilsMockedStatic.when(() -> CommonUtils.callIntegrationService(dcRequest, payload, headerMap, "T24SupplyChainFinance", "PostInvoiceCapture", false)).thenReturn(transactResult);
		
		result = anchorInvoiceResource.approveAnchorInvoice(approveAnchorInvoiceRequestDto, dcRequest);
		
		assertEquals("Approved", result.getParamValueByName("status"));
		assertEquals("CFI23340IT9LU", result.getParamValueByName("invoiceReference"));
	}
	
	@Test
	public void testrejectAnchorInvoice() throws Exception {
        Result result = new Result();
        
		String approveAnchorInvoiceResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorInvoiceResponse.json").getFile()).getPath();

		String approveAnchorInvoiceResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(approveAnchorInvoiceResponseMetaDataAndPIPath)));
		JSONObject approveAnchorInvoiceResponseobj = new JSONObject(approveAnchorInvoiceResponseMetaDataAndPIContent);
		
		AnchorInvoiceDTO approveAnchorInvoiceRequestDto = JSONUtils.parse(approveAnchorInvoiceResponseobj.toString(), AnchorInvoiceDTO.class);
			
		when(anchorInvoiceBusinessDelegate.getAnchorInvoiceById("CFI23340IT9LU", dcRequest))
		.thenReturn(approveAnchorInvoiceRequestDto);
		
		when(anchorInvoiceBusinessDelegate.updateAnchorInvoice(approveAnchorInvoiceRequestDto, dcRequest))
		.thenReturn(approveAnchorInvoiceRequestDto);
		
		result = anchorInvoiceResource.rejectAnchorInvoice(approveAnchorInvoiceRequestDto, dcRequest);
		
		assertEquals("Rejected", result.getParamValueByName("status"));
		assertEquals("CFI23340IT9LU", result.getParamValueByName("invoiceReference"));
	}
	
	public static HashMap<String, Object> getHeadersMap(DataControllerRequest request) {
        HashMap<String, Object> headerMap = new HashMap<>();
        headerMap.put(HTTP_HEADER_X_KONY_AUTHORIZATION, request.getHeader(HTTP_HEADER_X_KONY_AUTHORIZATION));
        headerMap.put(HTTP_HEADER_X_KONY_REPORTING_PARAMS, request.getHeader(HTTP_HEADER_X_KONY_REPORTING_PARAMS));
        return headerMap;
    }
}

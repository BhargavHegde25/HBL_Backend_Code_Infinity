package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

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
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorInvoiceBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorInvoiceBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;

public class AnchorInvoiceBusinessDelegateImplTest {
	AnchorInvoiceBusinessDelegate anchorInvoiceBusinessDelegate;
	DataControllerRequest request;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactroy;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	AnchorInvoiceDTO inputDto;
	static AnchorInvoiceBackendDelegate anchorInvoiceBackendDelegate;
	

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		try {
			anchorInvoiceBackendDelegate = mock(AnchorInvoiceBackendDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorInvoiceBackendDelegate.class))
		.thenReturn(anchorInvoiceBackendDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		anchorInvoiceBusinessDelegate = new AnchorInvoiceBusinessDelegateImpl();
		request = mock(DataControllerRequest.class);
		inputDto = mock(AnchorInvoiceDTO.class);
	    dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
	    businessDelegateFactroy = mock(BusinessDelegateFactory.class);   
	}
	
	@Test
	public void updateAnchorInvoiceTest() throws DBPApplicationException, Exception {	
		
		String approveAnchorInvoiceResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorInvoiceResponse.json").getFile()).getPath();

		String approveAnchorInvoiceResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(approveAnchorInvoiceResponseMetaDataAndPIPath)));
		JSONObject approveAnchorInvoiceResponseobj = new JSONObject(approveAnchorInvoiceResponseMetaDataAndPIContent);
		AnchorInvoiceDTO approveAnchorInvoiceRequestDto = JSONUtils.parse(approveAnchorInvoiceResponseobj.toString(), AnchorInvoiceDTO.class);

		when(anchorInvoiceBackendDelegate.updateAnchorInvoice(approveAnchorInvoiceRequestDto, request))
		.thenReturn(approveAnchorInvoiceRequestDto);
		
		approveAnchorInvoiceRequestDto = anchorInvoiceBusinessDelegate
				.updateAnchorInvoice(approveAnchorInvoiceRequestDto, request);
		
		assertEquals("CFI23340IT9LU", approveAnchorInvoiceRequestDto.getInvoiceReference());
	}
	
}

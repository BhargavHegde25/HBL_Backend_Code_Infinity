package com.temenos.infinity.tradesupplyfinance.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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

import org.mockito.MockedStatic.Verification;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.api.AnchorFundingRequestBackendDelegate;
import com.temenos.infinity.tradesupplyfinance.backenddelegate.impl.AnchorFundingRequestBackendDelegateImpl;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorFundingRequestBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;

public class AnchorFundingRequestBusinessDelegateImplTest {
	
	AnchorFundingRequestBusinessDelegate anchorFundingRequestBusinessDelegate;
	DataControllerRequest request;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactroy;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	AnchorFundingRequestDTO inputDto;
	static AnchorFundingRequestBackendDelegate anchorFundingRequestBackendDelegate;
	

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		try {
			anchorFundingRequestBackendDelegate = mock(AnchorFundingRequestBackendDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBackendDelegate(AnchorFundingRequestBackendDelegate.class))
		.thenReturn(anchorFundingRequestBackendDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		anchorFundingRequestBusinessDelegate = new AnchorFundingRequestBusinessDelegateImpl();
		request = mock(DataControllerRequest.class);
		inputDto = mock(AnchorFundingRequestDTO.class);
		 dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		 businessDelegateFactroy = mock(BusinessDelegateFactory.class);
		 
         
	}

	@Test
	public void createAnchorFundingRequestTest() throws IOException {
		
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

		when(anchorFundingRequestBackendDelegate.createAnchorFundingRequest(saveAnchorFundingRequestDto, request))
				.thenReturn(saveAnchorFundingResponseDto);

		AnchorFundingRequestDTO responseDTO;
		responseDTO = anchorFundingRequestBusinessDelegate.createAnchorFundingRequest(saveAnchorFundingRequestDto,
				request);
		assertEquals("AFR23307UR3YI", responseDTO.getFundingRequestId());

	}
	
	@Test
	public void updateAnchorFundingRequestTest() throws DBPApplicationException, Exception {

		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject createProsProfReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(createProsProfReq.toString(),
				AnchorFundingRequestDTO.class);

		when(anchorFundingRequestBackendDelegate.updateAnchorFundingRequest(anchorFundingRequestDTO, request))
				.thenReturn(anchorFundingRequestDTO);

		anchorFundingRequestDTO = anchorFundingRequestBusinessDelegate
				.updateAnchorFundingRequest(anchorFundingRequestDTO, request);

		assertEquals("AFR23307UR3YI", anchorFundingRequestDTO.getFundingRequestId());

	}
	
	@Test
	public void getAllFundingRequestTest() throws DBPApplicationException, Exception {

		List<AnchorFundingRequestDTO> fundindRequests = new ArrayList<AnchorFundingRequestDTO>();
		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject createProsProfReq = new JSONObject(submitFundingReqContent);
		
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(createProsProfReq.toString(), AnchorFundingRequestDTO.class);
		
		fundindRequests.add(anchorFundingRequestDTO);

		when(anchorFundingRequestBackendDelegate.getAllFundingRequest(request))
				.thenReturn(fundindRequests);

		fundindRequests = anchorFundingRequestBusinessDelegate.getAllFundingRequest(request);
		anchorFundingRequestDTO = fundindRequests.get(0);
		
		assertEquals("AFR23307UR3YI", anchorFundingRequestDTO.getFundingRequestId());

	}

}

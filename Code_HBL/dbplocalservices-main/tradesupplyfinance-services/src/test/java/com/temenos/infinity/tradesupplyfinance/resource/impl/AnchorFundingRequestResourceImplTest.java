package com.temenos.infinity.tradesupplyfinance.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.temenos.infinity.tradesupplyfinance.businessdelegate.api.AnchorFundingRequestBusinessDelegate;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorFundingRequestResource;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockedStatic.Verification;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;


@PrepareForTest({ AnchorFundingRequestResourceImpl.class,DBPAPIAbstractFactoryImpl.class, CommonUtils.class})

public class AnchorFundingRequestResourceImplTest {
	
	private AnchorFundingRequestResource anchorFundingRequestResource;
	private BusinessDelegateFactory businessDelegateFactroy;
	private DBPAPIAbstractFactory dbpAPIAbstractFactory;
	private DataControllerRequest dcRequest;
	private DataControllerResponse dataControllerResponse;
	private String methodId = "methodId";
	private Object[] inputArray;
	private static final String PROPERTY_VALUE = "propertyValue";
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private AnchorFundingRequestDTO inputDto;
	static AnchorFundingRequestBusinessDelegate fundingBusinessDelegate;
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
	    try {
			fundingBusinessDelegate = mock(AnchorFundingRequestBusinessDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}

		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBusinessDelegate(AnchorFundingRequestBusinessDelegate.class))
		.thenReturn(fundingBusinessDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {

		anchorFundingRequestResource = new AnchorFundingRequestResourceImpl();
		businessDelegateFactroy = mock(BusinessDelegateFactory.class);
		inputArray = new Object[10];
		dcRequest = mock(DataControllerRequest.class);
		dataControllerResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		 inputDto = mock(AnchorFundingRequestDTO.class);
		
	}
	
	
	@Test
	public void testsaveAnchorFundingRequest() throws Exception {
        Result result = new Result();
        // Mandatory fields check
        String SaveAnchorFundingRequestAndPIPath = new File(getClass().getClassLoader().getResource("AnchorFundingRequest.json").
				  getFile()).getPath();
				  
				  String saveAnchorFundingRequestMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(SaveAnchorFundingRequestAndPIPath)));
				  
				  JSONObject saveAnchorFundingRequestMetaDataAndPIObj = new JSONObject(saveAnchorFundingRequestMetaDataAndPIContent);
				  
				  AnchorFundingRequestDTO saveAnchorFundingRequestDto = JSONUtils.parse(saveAnchorFundingRequestMetaDataAndPIObj.toString(),
						  AnchorFundingRequestDTO.class);
		
		when(dcRequest.getParameterNames()).thenReturn(saveAnchorFundingRequestMetaDataAndPIObj.keys());
		
		
		String receivableSingleBillResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String saveAnchorFundingResponseMetaDataAndPIContent = new String(
				Files.readAllBytes(Paths.get(receivableSingleBillResponseMetaDataAndPIPath)));
		JSONObject saveAnchorFundingResponseobj = new JSONObject(saveAnchorFundingResponseMetaDataAndPIContent);
		AnchorFundingRequestDTO saveAnchorFundingResponseDto = JSONUtils.parse(saveAnchorFundingResponseobj.toString(), AnchorFundingRequestDTO.class);

		     
		when(fundingBusinessDelegate.getAnchorFundingRequestById("", dcRequest)).thenReturn(new AnchorFundingRequestDTO());
            when(fundingBusinessDelegate.createAnchorFundingRequest(saveAnchorFundingRequestDto, dcRequest))
			.thenReturn(saveAnchorFundingResponseDto);

            when(fundingBusinessDelegate.updateAnchorFundingRequest(saveAnchorFundingRequestDto, dcRequest))
            .thenReturn(saveAnchorFundingResponseDto);
            

        
        result = anchorFundingRequestResource.saveAnchorFundingRequest(saveAnchorFundingRequestDto, dcRequest);
        
        assertEquals("AFR23307UR3YI", result.getParamValueByName("fundingRequestId"));
	}
	
	@Test
	public void testSubmitAnchorFundingRequest() throws Exception {
		Result result = new Result();
		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject createProsProfReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(createProsProfReq.toString(), AnchorFundingRequestDTO.class);

	   
		when(inputDto.getFundingRequestId()).thenReturn("AFR23307UR3YI");

		
		when(fundingBusinessDelegate.getAnchorFundingRequestById("AFR23307UR3YI", dcRequest))
				.thenReturn(anchorFundingRequestDTO);

		when(fundingBusinessDelegate.updateAnchorFundingRequest(anchorFundingRequestDTO, dcRequest))
		.thenReturn(anchorFundingRequestDTO);

		result = anchorFundingRequestResource.submitAnchorFundingRequest(anchorFundingRequestDTO,dcRequest);

		assertEquals("AFR23307UR3YI", result.getParamValueByName("fundingRequestId"));

	}
	
	@Test
	public void testgetAllFundingRequest() throws Exception{
		Result result = new Result();
		 List<AnchorFundingRequestDTO> fundindRequests = new ArrayList<AnchorFundingRequestDTO>();
		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject createProsProfReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(createProsProfReq.toString(), AnchorFundingRequestDTO.class);
		fundindRequests.add(anchorFundingRequestDTO);
	   
		when(fundingBusinessDelegate.getAllFundingRequest(dcRequest))
		.thenReturn(fundindRequests);

		result = anchorFundingRequestResource.getAllFundingRequest(anchorFundingRequestDTO,dcRequest);
		Dataset dataset= result.getDatasetById("FundingRequests");
		Record record = dataset.getRecord(0);
		

		assertEquals("AFR23307UR3YI", record.getParamValueByName("fundingRequestId"));
		
	}
	
	@Test
	public void testCancelAnchorFundingRequest() throws Exception {
		Result result = new Result();
		String submitFundingReqFilePath = new File(
				getClass().getClassLoader().getResource("AnchorFundingResponse.json").getFile()).getPath();

		String submitFundingReqContent = new String(Files.readAllBytes(Paths.get(submitFundingReqFilePath)));
		JSONObject createProsProfReq = new JSONObject(submitFundingReqContent);
		AnchorFundingRequestDTO anchorFundingRequestDTO = JSONUtils.parse(createProsProfReq.toString(), AnchorFundingRequestDTO.class);

	   
		when(inputDto.getFundingRequestId()).thenReturn("AFR23307UR3YI");

		
		when(fundingBusinessDelegate.getAnchorFundingRequestById("AFR23307UR3YI", dcRequest))
				.thenReturn(anchorFundingRequestDTO);

		when(fundingBusinessDelegate.updateAnchorFundingRequest(anchorFundingRequestDTO, dcRequest))
		.thenReturn(anchorFundingRequestDTO);

		result = anchorFundingRequestResource.cancelAnchorFundingRequest(anchorFundingRequestDTO,dcRequest);

		assertEquals("AFR23307UR3YI", result.getParamValueByName("fundingRequestId"));

	}

}

package com.kony.MakerChecker.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.businessdelegate.api.MakerCheckerBusinessDelegate;
import com.kony.makerchecker.resource.impl.MakerCheckerResourceImpl;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class})
public class MakerCheckerResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	MakerCheckerResourceImpl makerCheckerResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	MakerCheckerBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
	String legalEntityId = "GB0010001";
	MakerCheckerUtils makerCheckerUtils;
	LoggedInUserHandler loggedInUserHandler;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<MakerCheckerUtils> mockedMakerCheckerUtils;
	private static MockedStatic<LoggedInUserHandler> mockedLoggedInUserHandler;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedMakerCheckerUtils = Mockito.mockStatic(MakerCheckerUtils.class);
		mockedLoggedInUserHandler = Mockito.mockStatic(LoggedInUserHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedMakerCheckerUtils.close();
		mockedLoggedInUserHandler.close();
	}

	@Before
	public void executedBefore() throws Exception {

		methodID = "METHODID";
		inputArray = new Object[2];
		inputArray[0] = "";
		inputArray[1] = new HashMap<String, String>();
		actualResult = new Result();
		
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		makerCheckerUtils = mock(MakerCheckerUtils.class);
		businessDelegate = mock(MakerCheckerBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(MakerCheckerBusinessDelegate.class))
				.thenReturn(businessDelegate);
	}

	@Test
	public void testIsMakerCheckerDisabled() throws Exception {
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Result expectedResult = new Result();
		Map<String, String> expectedMap = new HashMap<String, String>();
		expectedMap.put(UtilConstants.IS_APPROVAL_REQ, UtilConstants.FALSE);
		
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		when(businessDelegate.isMakerCheckerEnabled(dcRequest, expApiOperationName , legalEntityId)).thenReturn(expectedMap);
		expectedResult.addParam(UtilConstants.IS_APPROVAL_REQ,expectedMap.get(UtilConstants.IS_APPROVAL_REQ));
		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
	}
	
	@Test
	public void testIsMakerCheckerEnabled() throws Exception {
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		
		Map<String, String> expectedMap = new HashMap<String, String>();
		expectedMap.put(UtilConstants.IS_APPROVAL_REQ, UtilConstants.TRUE);
		
		JSONObject jsonObjForStroePayload = new JSONObject();
		jsonObjForStroePayload.put(UtilConstants.REQUEST_ID, "REQ_123456");
		
		Result expectedResult = new Result();
		expectedResult.addParam(UtilConstants.IS_APPROVAL_REQ,expectedMap.get(UtilConstants.IS_APPROVAL_REQ));
		expectedResult.addParam(UtilConstants.APPROVAL_REQ_ID,jsonObjForStroePayload.getString(UtilConstants.REQUEST_ID));
		
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		when(businessDelegate.isMakerCheckerEnabled(dcRequest, expApiOperationName , legalEntityId)).thenReturn(expectedMap);
		when(businessDelegate.storePayloadForRequest(dcRequest, (HashMap<String, String>)inputArray[1])).thenReturn(jsonObjForStroePayload);
		
		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.IS_APPROVAL_REQ).getValue(), expectedResult.getParamByName(UtilConstants.IS_APPROVAL_REQ).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.APPROVAL_REQ_ID).getValue(), expectedResult.getParamByName(UtilConstants.APPROVAL_REQ_ID).getValue());
	}
	
	@Test
	public void testIsMakerCheckerEnabledNullResponse() throws Exception {
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();
		when(businessDelegate.isMakerCheckerEnabled(dcRequest, expApiOperationName , legalEntityId)).thenReturn(null);
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10003.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10003.getMessage()));

		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testIsMakerCheckerEnabledException() throws Exception {
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10002.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10002.getMessage()));

		when(businessDelegate.isMakerCheckerEnabled(dcRequest, expApiOperationName , legalEntityId)).thenThrow(new Exception());
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		
		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testIsMakerCheckerEnabledNullOperationName() throws Exception {
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10001.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10001.getMessage()));

		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	//@Test
	public void testIsMakerCheckerEnabledNullLegalEntityId() throws Exception {

		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10004.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10004.getMessage()));

		actualResult = makerCheckerResourceImpl.isMakerCheckerEnabled(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testGetDashboardCounts() throws Exception {
		Map<String, String> inputMap = new HashMap<String, String>();
		inputArray[1] = inputMap;
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();

		when(businessDelegate.getDashboardCounts(dcRequest, inputMap)).thenReturn(new JSONObject());

		actualResult = makerCheckerResourceImpl.getDashboardCounts(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
	}
	
	@Test
	public void testGetDashboardCountsException() throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();
		inputArray[1] = inputMap;
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10007.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10007.getMessage()));

		when(businessDelegate.getDashboardCounts(dcRequest, inputMap)).thenThrow(new Exception());

		actualResult = makerCheckerResourceImpl.getDashboardCounts(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testGetMakerCheckerPendingRequests() throws Exception {
		Map<String, String> inputMap = new HashMap<>();
		Object[] inputArray = new Object[2];

		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("module", "ModuleValue");
		inputParams.put("action", "ActionValue");
		inputParams.put("recordId", "RecordIdValue");
		inputParams.put("companyLegalUnit", "CompanyLegalUnitValue");

		inputArray[1] = inputParams;
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Result expectedResult = new Result();
		when(businessDelegate.getMakerCheckerPendingRequests(dcRequest, inputParams)).thenReturn(new JSONObject());
		actualResult = makerCheckerResourceImpl.getMakerCheckerPendingRequests(methodID, inputArray, dcRequest,
				dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE),expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG),expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
	}
		
	@Test
	public void testGetMakerCheckerPendingRequestsInValidPayloadException() throws Exception{
		Map<String, String> inputMap = new HashMap<String, String>();
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10015.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10015.getMessage()));

		actualResult = makerCheckerResourceImpl.getMakerCheckerPendingRequests(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
    public void testGetMakerCheckerPendingRequestException() throws Exception{
		Object[] inputArray = new Object[2];

		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("module", "contractmanagement|customermanagement|usermanagement");
		inputParams.put("action", "create|edit|update");
		inputParams.put("recordId", "101010|100010,9593935265|119999,7592343258");
		inputParams.put("companyLegalUnit", "GB0010001|AU0010001|NL0010001");

		inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();
 
        Result expectedResult = new Result();
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10013.getErrorCodeAsString()));
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10013.getMessage()));
 
        when(businessDelegate.getMakerCheckerPendingRequests(dcRequest, inputParams)).thenThrow(new Exception());
 
        actualResult = makerCheckerResourceImpl.getMakerCheckerPendingRequests(methodID, inputArray, dcRequest, dcResponse);
 
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
    }

	
	@Test
    public void testApprovalRequestViewDetailsInvalidInput() throws Exception{
        Map<String, String> inputMap = new HashMap<String, String>();
        inputArray[1] = inputMap;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();

        Result expectedResult = new Result();
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10017.getErrorCodeAsString()));
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10017.getMessage()));

        actualResult = makerCheckerResourceImpl.approvalRequestViewDetails(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
    }
	
	@Test
    public void testApprovalRequestViewDetails() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.REQUEST_ID, "REQ1234");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();
        
        Result expectedResult = new Result();

        when(businessDelegate.approvalRequestViewDetails(dcRequest, inputParams)).thenReturn(new JSONObject());

        actualResult = makerCheckerResourceImpl.approvalRequestViewDetails(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
    }
	
	@Test
    public void testApprovalRequestViewDetailsException() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.REQUEST_ID, "REQ1234");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();

        Result expectedResult = new Result();
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10016.getErrorCodeAsString()));
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10016.getMessage()));

        when(businessDelegate.approvalRequestViewDetails(dcRequest, inputParams)).thenThrow(new Exception());

        actualResult = makerCheckerResourceImpl.approvalRequestViewDetails(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
    }
    
	@Test
	public void testApproveRequest() throws Exception{
		Result actualResult = new Result();
		HashMap<String,String> map = new HashMap<String, String>();
		map.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		map.put(UtilConstants.REQ_PAYLOAD, "{\"userdetails\":\"krian\"}");
		map.put(UtilConstants.PERMISSION_NAME, "ApproveCreateContract");
		map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
		String[] permsisionsArray = {"ApproveCreateContract"};
		JSONObject obj = new JSONObject();
		obj.put("status", "success");
		
		when(dcRequest.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ123");
		when(dcRequest.getParameter(UtilConstants.ACTION)).thenReturn("SID_APPROVED");
		when(dcRequest.getParameter(UtilConstants.COMMENTS)).thenReturn("");
		when(makerCheckerUtils.getApprovalRequestDetails(dcRequest, "REQ123")).thenReturn(map);
		when(loggedInUserHandler.hasAccessToLegalEntity(dcRequest,permsisionsArray)).thenReturn(true);
		when(businessDelegate.approveRejectRequest(dcRequest, "REQ123", "SID_APPROVED", "", map)).thenReturn(obj);
		
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();
        actualResult = makerCheckerResourceImpl.approveRejectRequest(methodID, inputArray, dcRequest, dcResponse);
        assertEquals(actualResult.getParamByName(UtilConstants.STATUS).getValue(), "success");
        
	}
	
	@Test
	public void testRejectRequest() throws Exception{
		Result actualResult = new Result();
		HashMap<String,String> map = new HashMap<String, String>();
		map.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		map.put(UtilConstants.REQ_PAYLOAD, "{\"userdetails\":\"krian\"}");
		map.put(UtilConstants.PERMISSION_NAME, "ApproveCreateContract");
		map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
		String[] permsisionsArray = {"ApproveCreateContract"};
		JSONObject obj = new JSONObject();
		obj.put("status", "success");
		
		when(dcRequest.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ123");
		when(dcRequest.getParameter(UtilConstants.ACTION)).thenReturn("SID_REJECTED");
		when(dcRequest.getParameter(UtilConstants.COMMENTS)).thenReturn("Not Allowed");
		when(dcRequest.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ123");
		
		when(dcRequest.getParameter(UtilConstants.ACTION)).thenReturn("SID_REJECTED");
		when(dcRequest.getParameter(UtilConstants.COMMENTS)).thenReturn("");
		when(makerCheckerUtils.getApprovalRequestDetails(dcRequest, "REQ123")).thenReturn(map);
		when(loggedInUserHandler.hasAccessToLegalEntity(dcRequest,permsisionsArray)).thenReturn(true);
		when(businessDelegate.approveRejectRequest(dcRequest, "REQ123", "SID_REJECTED", "", map)).thenReturn(obj);
		
	}
	
	@Test
	public void testParseApprovalRequestDetails() throws Exception {

		when(dcRequest.getParameter("requestId")).thenReturn("Req_987");
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		when(businessDelegate.getApprovalRequests(dcRequest, "Req_987", null)).thenReturn("Success Response");

		Result actualResult = new Result();
		actualResult = makerCheckerResourceImpl.parseApprovalRequestDetails(methodID, inputArray, dcRequest,
				dcResponse);
		assertEquals(1, actualResult.getAllParams().size());
	}
	
	@Test
	public void testParseApprovalRequestDetailsException() throws Exception {
		
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10028.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10028.getMessage()));

		when(dcRequest.getParameter("requestId")).thenReturn("Req_987");
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		when(businessDelegate.getApprovalRequests(dcRequest, "Req_987", null)).thenThrow(new RuntimeException());

		Result actualResult = new Result();
		actualResult = makerCheckerResourceImpl.parseApprovalRequestDetails(methodID, inputArray, dcRequest,
				dcResponse);
		assertEquals(2, actualResult.getAllParams().size());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testRequestsHistory() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("companyLegalUnit", "GB0010001");
		inputParams.put("userType", "Maker");
		inputArray[1] = inputParams;

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("RequestsHistoryResponse.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		when(businessDelegate.getRequestsHistory(dcRequest, inputParams)).thenReturn(expectedJson);
		Result actualResult = new Result();
		actualResult = makerCheckerResourceImpl.requestsHistory(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getOpstatusParamValue(), "0");
		assertEquals(actualResult.getHttpStatusCodeParamValue(), "0");
	}
	
	@Test
	public void testRequestsHistoryWhenPayloadEmpty() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputArray[1] = inputParams;

		JSONObject jsonObject = new JSONObject();
		when(businessDelegate.getRequestsHistory(dcRequest, inputParams)).thenReturn(jsonObject);
		Result actualResult = new Result();
		actualResult = makerCheckerResourceImpl.requestsHistory(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName(UtilConstants.DBP_ERR_CODE), ErrorCodesEnum.ERR_10035.getErrorCodeAsString());
		assertEquals(actualResult.getParamValueByName(UtilConstants.DBP_ERR_MSG), ErrorCodesEnum.ERR_10035.getMessage());
	}
	
	@Test
	public void testRequestsHistoryWhenException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("companyLegalUnit", "GB0010001");
		inputParams.put("userType", "Maker");
		inputArray[1] = inputParams;

		when(businessDelegate.getRequestsHistory(dcRequest, inputParams)).thenThrow(new RuntimeException());
		Result actualResult = new Result();
		actualResult = makerCheckerResourceImpl.requestsHistory(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName(UtilConstants.DBP_ERR_CODE), ErrorCodesEnum.ERR_10036.getErrorCodeAsString());
		assertEquals(actualResult.getParamValueByName(UtilConstants.DBP_ERR_MSG),ErrorCodesEnum.ERR_10036.getMessage());
	}
	public void testGetMakerCheckerConfig() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("legalEntityId", "GB0010001");
		inputParams.put("languageCode", UtilConstants.DEFAULT_LANGUAGE_CODE);

		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getMakerCheckerConfigurationsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject responseObj = new JSONObject(getContent);

		when(request.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		when(businessDelegate.getMakerCheckerConfig(request, inputParams)).thenReturn(responseObj);

		actualResult = makerCheckerResourceImpl.getMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals(1, actualResult.getAllDatasets().size());
		assertEquals("MakerCheckerConfigData", actualResult.getAllDatasets().get(0).getId());
	}

	@Test
	public void testGetMakerCheckerConfigException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;

		when(request.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		when(businessDelegate.getMakerCheckerConfig(request, new HashMap<String, String>()))
				.thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.getMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10024.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10024.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testGetMakerCheckerConfigInvalidInput() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		actualResult = makerCheckerResourceImpl.getMakerCheckerConfig(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10004.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10004.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testGetAllMakerPendingRequests() throws Exception {
		Object[] inputArray = new Object[2];

		Map<String, String> inputParams = new HashMap<>();
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.SUBMITTEDDATE, "2024-02-20");
		inputParams.put(UtilConstants.PAGEOFFSET, "0");
		inputParams.put(UtilConstants.PAGESIZE, "10");
		inputArray[1] = inputParams;
		
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Result expectedResult = new Result();
		when(businessDelegate.getAllMakerPendingRequests(dcRequest, inputParams)).thenReturn(new JSONObject());
		actualResult = makerCheckerResourceImpl.getAllMakerPendingRequests(methodID, inputArray, dcRequest,
				dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE),expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG),expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
	}
		
	@Test
	public void testGetAllMakerPendingRequestsInValidPayloadException() throws Exception{
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10004.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10004.getMessage()));

		actualResult = makerCheckerResourceImpl.getAllMakerPendingRequests(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testGetAllMakerPendingRequestsException() throws Exception {
		Object[] inputArray = new Object[2];

		Map<String, String> inputParams = new HashMap<>();
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.SUBMITTEDDATE, "2024-02-20");
		inputParams.put(UtilConstants.PAGEOFFSET, "0");
		inputParams.put(UtilConstants.PAGESIZE, "10");
		inputArray[1] = inputParams;

		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		Result expectedResult = new Result();
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10013.getErrorCodeAsString()));
		expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10013.getMessage()));
		
		when(businessDelegate.getAllMakerPendingRequests(dcRequest, inputParams)).thenThrow(new Exception());
		actualResult = makerCheckerResourceImpl.getAllMakerPendingRequests(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(),expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(),expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
@Test
    public void testGetCheckerApprovalRequests() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();
        
        Result expectedResult = new Result();

        when(businessDelegate.getCheckerApprovalRequests(dcRequest, inputParams)).thenReturn(new JSONObject());

        actualResult = makerCheckerResourceImpl.getCheckerApprovalRequests(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
    }
	
	@Test
    public void testGetCheckerApprovalRequestsException() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();

        Result expectedResult = new Result();
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10033.getErrorCodeAsString()));
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10033.getMessage()));

        when(businessDelegate.getCheckerApprovalRequests(dcRequest, inputParams)).thenThrow(new Exception());

        actualResult = makerCheckerResourceImpl.getCheckerApprovalRequests(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
    }
	
	@Test
    public void testGetMCModuleAction() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LANGUAGECODE, "en-GB");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();
        
        Result expectedResult = new Result();

        when(businessDelegate.getMCModuleActionOperation(dcRequest, inputParams)).thenReturn(new JSONObject());

        actualResult = makerCheckerResourceImpl.getMCModuleActionOperation(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE));
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG));
    }
	
	@Test
    public void testGetMCModuleActionException() throws Exception{
        Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LANGUAGECODE, "en-GB");
        inputArray[1] = inputParams;
        makerCheckerResourceImpl = new MakerCheckerResourceImpl();

        Result expectedResult = new Result();
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_CODE, ErrorCodesEnum.ERR_10038.getErrorCodeAsString()));
        expectedResult.addParam(new Param(UtilConstants.DBP_ERR_MSG, ErrorCodesEnum.ERR_10038.getMessage()));

        when(businessDelegate.getMCModuleActionOperation(dcRequest, inputParams)).thenThrow(new Exception());

        actualResult = makerCheckerResourceImpl.getMCModuleActionOperation(methodID, inputArray, dcRequest, dcResponse);

        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
        assertEquals(actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue(), expectedResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
    }
	
	@Test
	public void testUpdateMakerCheckerConfigResNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONArray requestObj = new JSONObject(getContent).getJSONArray("makerCheckerConfig");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", requestObj);

		when(request.getParameter("makerCheckerConfig")).thenReturn(requestObj.toString());
		when(businessDelegate.updateMakerCheckerConfig(request, inputParams)).thenReturn(null);
		actualResult = makerCheckerResourceImpl.updateMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testUpdateMakerCheckerConfigException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONArray requestObj = new JSONObject(getContent).getJSONArray("makerCheckerConfig");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", requestObj);
		when(request.getParameter("makerCheckerConfig")).thenReturn(requestObj.toString());
		when(businessDelegate.updateMakerCheckerConfig(request, new HashMap<String, Object>()))
				.thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.updateMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testUpdateMakerCheckerConfigInvalidInput() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONArray requestObj = new JSONObject(getContent).getJSONArray("makerCheckerConfig");
		JSONObject data = (JSONObject) requestObj.get(0);
		data.put("id", "");

		when(request.getParameter("makerCheckerConfig")).thenReturn(requestObj.toString());
		actualResult = makerCheckerResourceImpl.updateMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10041.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10041.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testUpdateMakerCheckerConfig() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, 0);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, 0);

		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONArray requestObj = new JSONObject(getContent).getJSONArray("makerCheckerConfig");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", requestObj);

		when(request.getParameter("makerCheckerConfig")).thenReturn(requestObj.toString());
		when(businessDelegate.updateMakerCheckerConfig(any(), anyMap())).thenReturn(responseObj);
		actualResult = makerCheckerResourceImpl.updateMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals("success", actualResult.getParamValueByName(UtilConstants.STATUS));
	}

	@Test
	public void testUpdateMakerCheckerConfigFailure() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, -1);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, -1);

		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONArray requestObj = new JSONObject(getContent).getJSONArray("makerCheckerConfig");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", requestObj);

		when(request.getParameter("makerCheckerConfig")).thenReturn(requestObj.toString());
		when(businessDelegate.updateMakerCheckerConfig(any(), anyMap())).thenReturn(responseObj);
		actualResult = makerCheckerResourceImpl.updateMakerCheckerConfig(methodID, inputArray, request, dcResponse);
		assertEquals("failure", actualResult.getParamValueByName(UtilConstants.STATUS));
	}
	
	@Test
	public void testWithdrawRequest() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, 0);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, 0);
		responseObj.put(UtilConstants.STATUS, "Request withdrawn successfully");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		map.put(UtilConstants.REQ_PAYLOAD, "{\"userdetails\":\"krian\"}");
		map.put(UtilConstants.PERMISSION_NAME, "ApproveCreateContract");
		map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");

		when(request.getParameter(UtilConstants.ACTION)).thenReturn("SID_WITHDRAWN");
		when(request.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ_1202093572");
		mockedMakerCheckerUtils.when(() -> MakerCheckerUtils.getApprovalRequestDetails(request, "REQ_1202093572"))
				.thenReturn(map);

		when(businessDelegate.withdrawRequest(any(), anyMap())).thenReturn(responseObj);
		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(3, actualResult.getAllParams().size());
		assertEquals("Request withdrawn successfully", actualResult.getParamValueByName(UtilConstants.STATUS));
	}

	@Test
	public void testWithdrawRequestInvalidInput() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		when(request.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ_1202093572");
		when(request.getParameter(UtilConstants.ACTION)).thenReturn("SID_PENDING");

		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10046.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10046.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testWithdrawRequestRequestNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		when(request.getParameter(UtilConstants.ACTION)).thenReturn("SID_WITHDRAWN");

		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testWithdrawRequestActionNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;

		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10018.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10018.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testWithdrawRequestApprovalDetailsEmpty() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		when(request.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ_1202093572");
		when(request.getParameter(UtilConstants.ACTION)).thenReturn("SID_WITHDRAWN");
		mockedMakerCheckerUtils.when(() -> MakerCheckerUtils.getApprovalRequestDetails(request, "REQ_1202093572"))
				.thenReturn(anyMap());

		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10027.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10027.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testWithdrawRequestException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		DataControllerRequest request = dcRequest;
		when(request.getParameter(UtilConstants.ACTION)).thenReturn("SID_WITHDRAWN");
		when(request.getParameter(UtilConstants.REQUEST_ID)).thenReturn("REQ_1202093572");
		mockedMakerCheckerUtils.when(() -> MakerCheckerUtils.getApprovalRequestDetails(request, "REQ_1202093572"))
				.thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.withdrawRequest(methodID, inputArray, request, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10045.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10045.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testEnrollCustomerViewDetails() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_1281594362");
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, 0);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, 0);

		when(businessDelegate.enrollCustomerViewDetails(dcRequest, inputParams)).thenReturn(responseObj);

		actualResult = makerCheckerResourceImpl.enrollCustomerViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(2, actualResult.getAllParams().size());
	}

	@Test
	public void testEnrollCustomerViewDetailsException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_1281594362");
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");
		when(businessDelegate.enrollCustomerViewDetails(dcRequest, inputParams)).thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.enrollCustomerViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testEnrollCustomerViewDetailsRequestNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		actualResult = makerCheckerResourceImpl.enrollCustomerViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testCustomerViewDetails() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_12345678");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_12345678");

		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, 0);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, 0);

		when(businessDelegate.viewCustomerDetails(dcRequest, dcResponse, inputParams)).thenReturn(responseObj);

		actualResult = makerCheckerResourceImpl.viewCustomerDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(2, actualResult.getAllParams().size());
	}
	
	@Test
	public void testCustomerViewDetailsWhenException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_12345678");
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_12345678");
		when(businessDelegate.viewCustomerDetails(dcRequest, dcResponse, inputParams)).thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.viewCustomerDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10048.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10048.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testCustomerViewDetailsWhenRequestIsNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		actualResult = makerCheckerResourceImpl.viewCustomerDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
	
	@Test
	public void testCreateContractViewDetails() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_1281594362");
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.OPSTATUS, 0);
		responseObj.put(UtilConstants.HTTP_STATUS_CODE, 0);

		when(businessDelegate.createContractViewDetails(dcRequest, inputParams)).thenReturn(responseObj);

		actualResult = makerCheckerResourceImpl.createContractViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(2, actualResult.getAllParams().size());
	}

	@Test
	public void testCreateContractViewDetailsWhenException() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();
		when(dcRequest.getParameter("requestId")).thenReturn("REQ_1281594362");
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");
		when(businessDelegate.createContractViewDetails(dcRequest, inputParams)).thenThrow(new RuntimeException());

		actualResult = makerCheckerResourceImpl.createContractViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10056.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10056.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}

	@Test
	public void testCreateContractViewDetailsWhenRequestNull() throws Exception {
		makerCheckerResourceImpl = new MakerCheckerResourceImpl();

		actualResult = makerCheckerResourceImpl.createContractViewDetails(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getErrorCodeAsString()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_CODE).getValue());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10019.getMessage()),
				actualResult.getParamByName(UtilConstants.DBP_ERR_MSG).getValue());
	}
}

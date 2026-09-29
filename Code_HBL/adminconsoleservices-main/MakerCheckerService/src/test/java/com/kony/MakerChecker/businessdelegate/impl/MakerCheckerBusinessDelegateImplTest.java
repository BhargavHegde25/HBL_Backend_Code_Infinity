package com.kony.MakerChecker.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.backenddelegate.api.MakerCheckerBackendDelegate;
import com.kony.makerchecker.businessdelegate.impl.MakerCheckerBusinessDelegateImpl;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.logger.Logger;


@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class})
public class MakerCheckerBusinessDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	MakerCheckerBackendDelegate makerCheckerBackendDelegate;
	MakerCheckerBusinessDelegateImpl makerCheckerBusinessDelegateImpl;
	String methodID;
	Object[] inputArray;
	HashMap<String, Object> securityAttributes;
	CommonUtilities commonUtilities;
	Map<String, String> inputMap;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	ServicesManager serviceManager;
	IdentityHandler identityHandler;
	LoadMakerCheckerConfigData loadMakerCheckerConfigData;
	MakerCheckerUtils makerCheckerUtils;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedCommonUtilities;
	private static MockedStatic<LoadMakerCheckerConfigData> mockedLoadMakerCheckerConfigData;
	private static MockedStatic<MakerCheckerUtils> mockedMakerCheckerUtils;
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedLoadMakerCheckerConfigData = Mockito.mockStatic(LoadMakerCheckerConfigData.class);
		mockedMakerCheckerUtils = Mockito.mockStatic(MakerCheckerUtils.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedCommonUtilities.close();
		mockedLoadMakerCheckerConfigData.close();
		mockedMakerCheckerUtils.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		
		
		methodID = "METHODID";
		inputArray = new Object[2];
		inputMap = new HashMap<String, String>();
		securityAttributes = new HashMap<String, Object>();
		
		session = mock(Session.class);
		dcRequest  = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		makerCheckerBackendDelegate = mock(MakerCheckerBackendDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);
		commonUtilities = mock(CommonUtilities.class);
		serviceManager =  mock(ServicesManager.class);
		identityHandler = mock(IdentityHandler.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(MakerCheckerBackendDelegate.class)).thenReturn(makerCheckerBackendDelegate);
		makerCheckerBusinessDelegateImpl = new MakerCheckerBusinessDelegateImpl();
		
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);

		
		}
	
	@Test
	public void testIsMakerCheckerEnabled() throws Exception{
		
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
		String legalEntityId = "GB0010001";
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put(UtilConstants.IS_APPROVAL_REQ, UtilConstants.TRUE);		
		
		when(makerCheckerBackendDelegate.isMakerCheckerEnabled(dcRequest, expApiOperationName, legalEntityId))
		.thenReturn(expectedResult);
		
		Map<String, String> actualResult = makerCheckerBusinessDelegateImpl.isMakerCheckerEnabled(dcRequest, expApiOperationName, legalEntityId);

		assertEquals(actualResult.get(UtilConstants.IS_APPROVAL_REQ), expectedResult.get(UtilConstants.IS_APPROVAL_REQ));		
	}
	
	@Test
	public void testStorePayload() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("testStorePayload.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		
		when(makerCheckerBackendDelegate.storePayloadForRequest(dcRequest, inputMap))
		.thenReturn(expectedJson);
		
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.storePayloadForRequest(dcRequest, inputMap);
		
		assertEquals(actualResult.get(UtilConstants.OPSTATUS), expectedJson.get(UtilConstants.OPSTATUS));		
	}
	
	@Test
	public void testGetDashboardCounts() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("BackendDelegate_getDashboardCountsResponse.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001,AU0010001");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		
		
		String getBusinessResponseFile = new File(
				getClass().getClassLoader().getResource("BusinessDelegate_getDashboardCountsResponse.json").getFile()).getPath();
		String getBusinessResponseContent = new String(Files.readAllBytes(Paths.get(getBusinessResponseFile)));
		JSONObject expectedBusinessJson = new JSONObject(getBusinessResponseContent);
		
	
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(commonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(commonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);
		when(commonUtilities.getStringAsJSONObject(rawresponse.toString())).thenReturn(rawresponse);
		when(makerCheckerBackendDelegate.getDashboardCounts(dcRequest, inputMap)).thenReturn(expectedJson);
		
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getDashboardCounts(dcRequest, inputMap);
		
		assertEquals(actualResult.getJSONObject(UtilConstants.MAKER).get("GB0010001"), expectedBusinessJson.getJSONObject(UtilConstants.MAKER).get("GB0010001"));		
		assertEquals(actualResult.getJSONObject(UtilConstants.MAKER).get("AU0010001"), expectedBusinessJson.getJSONObject(UtilConstants.MAKER).get("AU0010001"));
		assertEquals(actualResult.getJSONObject(UtilConstants.CHECKER).get("GB0010001"), expectedBusinessJson.getJSONObject(UtilConstants.CHECKER).get("GB0010001"));		
		assertEquals(actualResult.getJSONObject(UtilConstants.CHECKER).get("AU0010001"), expectedBusinessJson.getJSONObject(UtilConstants.CHECKER).get("AU0010001"));
	}
	
	@Test
	public void testGetMakerCheckerPendingRequests() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("getMakerCheckerPendingRequests.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		
		when(makerCheckerBackendDelegate.getMakerCheckerPendingRequests(dcRequest, inputMap)).thenReturn(expectedJson);
		
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getMakerCheckerPendingRequests(dcRequest, inputMap);
		
		assertEquals(actualResult.get(UtilConstants.OPSTATUS), expectedJson.get(UtilConstants.OPSTATUS));		
	}
	
	
	
	@Test
	public void testApprovalRequestViewDetails() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("approvalRequestViewDetailsExpectedResult.json").getFile())
						.getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);

		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1086002759");
		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1086002759")).thenReturn(null);
		when(makerCheckerBackendDelegate.approvalRequestViewDetails(dcRequest, inputParams)).thenReturn(expectedJson);
		when(makerCheckerBackendDelegate.updateApprovalRequests(any(), anyMap())).thenReturn(new JSONObject());

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.approvalRequestViewDetails(dcRequest, inputParams);
		assertEquals(expectedJson.get(UtilConstants.OPSTATUS), actualResult.get(UtilConstants.OPSTATUS));
	}
	
	@Test
	public void testApprovalRequestViewDetailsFromdbxdb() throws Exception {

		String getApprovalReqResponse = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").toURI())
				.toString();
		String getApprovalReqContent = new String(Files.readAllBytes(Paths.get(getApprovalReqResponse)));
		JSONObject expectedApprovalReqJson = new JSONObject(getApprovalReqContent);
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1086002759");
		
		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1086002759")).thenReturn(expectedApprovalReqJson);;
		when(makerCheckerBackendDelegate.updateApprovalRequests(any(), anyMap())).thenReturn(new JSONObject());

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.approvalRequestViewDetails(dcRequest, inputParams);
		assertEquals(0, actualResult.get(UtilConstants.OPSTATUS));
	}
	
	@Test
    public void testGetApprovalRequests() throws Exception{
        
        String getResponseFile = new File(
                getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").getFile()).getPath();

        String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
        JSONObject expectedJson = new JSONObject(getResponseContent);
        
        when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "Req_987"))
        .thenReturn(expectedJson);
        
        String actualResult = makerCheckerBusinessDelegateImpl.getApprovalRequests(dcRequest, "Req_987" , null);
        assertNotEquals(actualResult.length() , 0);
        assertNotEquals(actualResult, null);
    }
	
	@Test
    public void testApproveRequestSuccess() throws Exception{
        HashMap<String,String> map=  new HashMap<String,String>();
        map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
        map.put(UtilConstants.EXPAPIOPNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
        map.put(UtilConstants.REQ_PAYLOAD, "{\"userid\":\"100100\"}");
        
		JSONObject userAttributes = new JSONObject();
		userAttributes.put("username","admin1");
		
        JSONObject rawresponse = new JSONObject();
		rawresponse.put("user_attributes", userAttributes);
		
		securityAttributes.put("raw_response",rawresponse);
        
        JSONObject obj = new JSONObject();
        obj.put("status", "success");
        
        String getResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

        String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
        JSONArray configData = new JSONArray(getResponseContent);

		when(commonUtilities.getStringAsJSONObject(rawresponse.toString())).thenReturn(rawresponse);
		when(commonUtilities.getStringAsJSONObject(userAttributes.toString())).thenReturn(userAttributes);
        when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
        
        when(makerCheckerUtils.updateApprovalRequestStatus(dcRequest, "REQ_123",UtilConstants.SID_PROCESSING, null, UtilConstants.SID_PENDING, "admin1")).thenReturn(obj);
        JSONObject actualResult = makerCheckerBusinessDelegateImpl.approveRejectRequest(dcRequest, "REQ_123" , "SID_APPROVED","", map);
        assertEquals(actualResult.getString("status") , "Request is approved and submitted for processing.");
    }
	
	@Test
    public void testApproveRequestFailure() throws Exception{
        HashMap<String,String> map=  new HashMap<String,String>();
        map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
        map.put(UtilConstants.EXPAPIOPNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
        map.put(UtilConstants.REQ_PAYLOAD, "{\"userid\":\"100100\"}");
        JSONObject userAttributes = new JSONObject();
		userAttributes.put("username","admin1");
		
        JSONObject rawresponse = new JSONObject();
		rawresponse.put("user_attributes", userAttributes);
		
		securityAttributes.put("raw_response",rawresponse);
        
        JSONObject obj = new JSONObject();
        obj.put("status", "failure");
        
        JSONObject resultObj =  new JSONObject();
        resultObj.put("dbpErrCode", "10023");
        resultObj.put("dbpErrMsg", "Error occurred while approving the request");
        
        String getResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

        String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
        JSONArray configData = new JSONArray(getResponseContent);
        when(commonUtilities.getStringAsJSONObject(rawresponse.toString())).thenReturn(rawresponse);
		when(commonUtilities.getStringAsJSONObject(userAttributes.toString())).thenReturn(userAttributes);
 
        when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
        
        when(makerCheckerUtils.updateApprovalRequestStatus(dcRequest, "REQ_123",UtilConstants.SID_PROCESSING, null, UtilConstants.SID_PENDING,"admin1")).thenReturn(obj);
        JSONObject actualResultObj = makerCheckerBusinessDelegateImpl.approveRejectRequest(dcRequest, "REQ_123" , "SID_APPROVED","", map);
        assertEquals(actualResultObj.getString("dbpErrCode") , "10023");
        assertEquals(actualResultObj.getString("dbpErrMsg") , "Error occurred while approving the request");
    }
	
	@Test
    public void testRejectRequestSuccess() throws Exception{
        HashMap<String,String> map=  new HashMap<String,String>();
        map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
        map.put(UtilConstants.EXPAPIOPNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
        map.put(UtilConstants.REQ_PAYLOAD, "{\"userid\":\"100100\"}");
        
        JSONObject userAttributes = new JSONObject();
		userAttributes.put("username","admin1");
		
        JSONObject rawresponse = new JSONObject();
		rawresponse.put("user_attributes", userAttributes);
		
		securityAttributes.put("raw_response",rawresponse);
        
        
        JSONObject obj = new JSONObject();
        obj.put("status", "success");
        
        String getResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

        String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
        JSONArray configData = new JSONArray(getResponseContent);
        
        when(commonUtilities.getStringAsJSONObject(rawresponse.toString())).thenReturn(rawresponse);
		when(commonUtilities.getStringAsJSONObject(userAttributes.toString())).thenReturn(userAttributes);
		when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
        
        when(makerCheckerUtils.updateApprovalRequestStatus(dcRequest, "REQ_123",UtilConstants.SID_REJECTED, "", UtilConstants.SID_PENDING,"admin1")).thenReturn(obj);
        JSONObject actualResultObj = makerCheckerBusinessDelegateImpl.approveRejectRequest(dcRequest, "REQ_123" , "SID_REJECTED","", map);
        assertEquals(actualResultObj.getString("status") , "Request is rejected successfully");
        
    }
	
	@Test
    public void testRejectRequestFailure() throws Exception{
        HashMap<String,String> map=  new HashMap<String,String>();
        map.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
        map.put(UtilConstants.EXPAPIOPNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
        map.put(UtilConstants.REQ_PAYLOAD, "{\"userid\":\"100100\"}");
        
        JSONObject userAttributes = new JSONObject();
		userAttributes.put("username","admin1");
		
        JSONObject rawresponse = new JSONObject();
		rawresponse.put("user_attributes", userAttributes);
		
		securityAttributes.put("raw_response",rawresponse);
        
        JSONObject obj = new JSONObject();
        obj.put("status", "failure");
        
        JSONObject resultObj =  new JSONObject();
        resultObj.put("dbpErrCode", "10022");
        resultObj.put("dbpErrMsg", "Error occurred while rejecting the request");
        
        String getResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

        String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
        JSONArray configData = new JSONArray(getResponseContent);
        
        when(commonUtilities.getStringAsJSONObject(rawresponse.toString())).thenReturn(rawresponse);
		when(commonUtilities.getStringAsJSONObject(userAttributes.toString())).thenReturn(userAttributes);
		when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
        
        when(makerCheckerUtils.updateApprovalRequestStatus(dcRequest, "REQ_123",UtilConstants.SID_REJECTED, "", UtilConstants.SID_PENDING,"admin1")).thenReturn(obj);
        JSONObject actualResultObj = makerCheckerBusinessDelegateImpl.approveRejectRequest(dcRequest, "REQ_123" , "SID_REJECTED","", map);
        assertEquals(actualResultObj.getString("dbpErrCode") , "10022");
        assertEquals(actualResultObj.getString("dbpErrMsg") , "Error occurred while rejecting the request");
    }
	
	@Test
	public void testRequestsHistoryWhenMaker() throws Exception {

		String requestsHistoryResponse = new File(
				getClass().getClassLoader().getResource("RequestsHistoryResponse.json").getFile()).getPath();
		String responseContent = new String(Files.readAllBytes(Paths.get(requestsHistoryResponse)));
		JSONObject expectedJson = new JSONObject(responseContent);

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("companyLegalUnit", "GB0010001");
		inputParams.put("userType", UtilConstants.MAKER);
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		set1.add("ApproveCreateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		leWisePermissionMap.put("SHARED", set1);
		
		Set<String> legalEntitySet = new HashSet<String>(
				Arrays.asList(inputParams.get("companyLegalUnit").toString()));
		
		Map<String, Object> requestPayload = new HashMap<String, Object>();
		Map<String, Object> requestMap1 = new HashMap<String, Object>();
		Map<String, Object> requestMap2 = new HashMap<String, Object>();
		requestMap1.put("$filter",
				"createdby eq 'admin1' and (status eq SID_COMPLETED or status eq  SID_REJECTED or status eq  SID_FAILED or status eq  SID_WITHDRAWN) and (companyLegalUnit eq GB0010001 or companyLegalUnit eq SHARED)");
		requestMap1.put("$orderby", "actionedDate desc");
		requestMap2.put("$filter",
				"createdby eq 'admin1' and (status eq SID_COMPLETED or status eq  SID_REJECTED or status eq  SID_FAILED or status eq  SID_WITHDRAWN) and (companyLegalUnit eq GB0010001)");
		requestMap2.put("$orderby", "actionedDate desc");
		requestPayload.put("requestWithPaginationParams", requestMap1);
		requestPayload.put("requestWithOutPaginationParams", requestMap2);

		HashMap<String, Object> securityAttributes = new HashMap<>();
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		userAttr.put(UtilConstants.USERNAME, "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		when(CommonUtilities.getLoggedInUserAttributes(dcRequest)).thenReturn(new HashMap<>());
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString()))
				.thenReturn(userAttr);
		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
		when(makerCheckerBackendDelegate
				.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithPaginationParams")))
				.thenReturn(expectedJson);
		when(makerCheckerBackendDelegate
				.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithOutPaginationParams")))
				.thenReturn(expectedJson);

		JSONObject actualObj = makerCheckerBusinessDelegateImpl.getRequestsHistory(dcRequest, inputParams);
		assertEquals(2, actualObj.getJSONArray("requests").length());
	}
    
    @Test
    public void testRequestsHistoryWhenChecker() throws Exception{
    	
    	String requestsHistoryResponse = new File(
				getClass().getClassLoader().getResource("RequestsHistoryResponse.json").getFile()).getPath();
    	String responseContent = new String(Files.readAllBytes(Paths.get(requestsHistoryResponse)));
		JSONObject expectedJson = new JSONObject(responseContent);
    	
    	Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("companyLegalUnit", "GB0010001");
		inputParams.put("userType", UtilConstants.CHECKER);
		
		Map<String, Object> requestPayload = new HashMap<String, Object>();
		Map<String, Object> requestMap1 = new HashMap<String, Object>();
		Map<String, Object> requestMap2 = new HashMap<String, Object>();
		requestMap1.put("$filter", "(companyLegalUnit eq GB0010001 or companyLegalUnit eq SHARED) and createdby ne 'admin1' and (status eq SID_COMPLETED or status eq  SID_REJECTED or status eq  SID_FAILED) and (approvalPermissionName eq ApproveCreateCustomer)");
		requestMap1.put("$orderby", "actionedDate desc");
		requestMap2.put("$filter", "(companyLegalUnit eq GB0010001 or companyLegalUnit eq SHARED) and createdby ne 'admin1' and (status eq SID_COMPLETED or status eq  SID_REJECTED or status eq  SID_FAILED) and (approvalPermissionName eq ApproveCreateCustomer)");
		requestMap2.put("$orderby", "actionedDate desc");
		requestPayload.put("requestWithPaginationParams", requestMap1);
		requestPayload.put("requestWithOutPaginationParams", requestMap2);
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		set1.add("ApproveCreateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		leWisePermissionMap.put("SHARED", set1);
		
		Set<String> legalEntitySet = new HashSet<String>(
				Arrays.asList(inputParams.get("companyLegalUnit").toString()));
		
		HashMap<String,Object> securityAttributes= new HashMap<>();
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		userAttr.put(UtilConstants.USERNAME, "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		
		when(CommonUtilities.getLoggedInUserAttributes(dcRequest)).thenReturn(new HashMap<>());
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);
		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
		when(makerCheckerBackendDelegate.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithPaginationParams"))).thenReturn(expectedJson);
		when(makerCheckerBackendDelegate.getRequestsHistory((Map<String, Object>) requestPayload.get("requestWithOutPaginationParams"))).thenReturn(expectedJson);
		
		JSONObject actualObj = makerCheckerBusinessDelegateImpl.getRequestsHistory(dcRequest, inputParams);
		assertEquals(2, actualObj.getJSONArray("requests").length());
    }
    
   
	
	@Test
	public void testGetMakerCheckerConfig() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getMakerCheckerConfigurationsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject responseObj = new JSONObject(getContent);

		when(makerCheckerBackendDelegate.getMakerCheckerConfig(dcRequest, inputMap)).thenReturn(responseObj);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getMakerCheckerConfig(dcRequest, inputMap);
		assertEquals(3, actualResult.keySet().size());
		assertEquals(0, actualResult.getInt(UtilConstants.OPSTATUS));
	}

	@Test
	public void testUpdateMakerCheckerConfig() throws Exception {
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(UtilConstants.OPSTATUS, 0);
		expectedResult.put(UtilConstants.HTTP_STATUS_CODE, 0);

		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject reponse = new JSONObject(getContent);
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", reponse.getJSONArray("makerCheckerConfig"));

		when(makerCheckerBackendDelegate.updateMakerCheckerConfig(dcRequest, inputParams)).thenReturn(expectedResult);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.updateMakerCheckerConfig(dcRequest, inputParams);
		assertEquals(expectedResult.get(UtilConstants.OPSTATUS), actualResult.get(UtilConstants.OPSTATUS));
		assertEquals(expectedResult.get(UtilConstants.HTTP_STATUS_CODE),
				expectedResult.get(UtilConstants.HTTP_STATUS_CODE));
	}
	
	@Test
	public void testGetAllMakerPendingRequests() throws Exception {

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("fetchAllMakerPendingRequestsResponse.json").getFile())
				.getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		Map<String, String> inputParams = new HashMap<String, String>();

		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.SUBMITTEDDATE, "2024-02-20");
		inputParams.put(UtilConstants.PAGEOFFSET, "0");
		inputParams.put(UtilConstants.PAGESIZE, "10");

		HashMap<String, Object> securityAttributes = new HashMap<>();
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.USERNAME, "admin1");
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("_action", inputParams.get(UtilConstants.REQUESTTYPE));
		requestParameters.put("_pageOffset", inputParams.get(UtilConstants.PAGEOFFSET));
		requestParameters.put("_submittedDate", inputParams.get(UtilConstants.SUBMITTEDDATE));
		requestParameters.put("_legalEntityId", inputParams.get(UtilConstants.LEGAL_ENTITY_ID) + "," + UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY);
		requestParameters.put("_pageSize", inputParams.get(UtilConstants.PAGESIZE));
		requestParameters.put("_module", inputParams.get(UtilConstants.MODULE));
		requestParameters.put("_userName", userAttr.get("username"));

		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(CommonUtilities.getStringAsJSONObject(dcRequest.getServicesManager().getIdentityHandler()
				.getSecurityAttributes().get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);
		when(MakerCheckerUtils.calculateOffset(dcRequest)).thenReturn("0");
		when(makerCheckerBackendDelegate.getAllMakerPendingRequests(dcRequest, requestParameters)).thenReturn(expectedJson);

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getAllMakerPendingRequests(dcRequest, inputParams);

		assertEquals(actualResult.get(UtilConstants.OPSTATUS), expectedJson.get(UtilConstants.OPSTATUS));
	}
	
	
	@Test
	public void testGetCheckerApprovalRequests() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("GetCheckerApprovalRequestsExpectedResults.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		
		HashMap<String,Object> securityAttributes= new HashMap<>();
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		userAttr.put(UtilConstants.USERNAME, "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		
        inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
        inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
        inputMap.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
        inputMap.put(UtilConstants.SUBMITTEDDATE, "2024-02-14");
        inputMap.put(UtilConstants.PAGESIZE, "10");
        inputMap.put(UtilConstants.PAGEOFFSET, "1");
		
		String getBusinessResponseFile = new File(
				getClass().getClassLoader().getResource("GetCheckerApprovalRequestsExpectedResults.json").getFile()).getPath();
		String getBusinessResponseContent = new String(Files.readAllBytes(Paths.get(getBusinessResponseFile)));
		JSONObject expectedBusinessJson = new JSONObject(getBusinessResponseContent);
		
        String getMCConfigResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigDataLEId.json").getFile()).getPath();

        String getMCConfigResponseContent = new String(Files.readAllBytes(Paths.get(getMCConfigResponseFile)));
        JSONArray configData = new JSONArray(getMCConfigResponseContent);
        
        when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		set1.add("ApproveCreateContract");
		set1.add("ApproveUpdateContract");
		set1.add("ApproveCreateCustomer");
		set1.add("ApproveUpdateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		
		String legalEntityIds = "GB0010001";
		Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));
		
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);

		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
        
		
		when(makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputMap)).thenReturn(expectedJson);
		
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getCheckerApprovalRequests(dcRequest, inputMap);
		
		assertEquals(actualResult.get(UtilConstants.TOTALNUMBEROFRECORDS),expectedBusinessJson.get(UtilConstants.TOTALNUMBEROFRECORDS).toString()); 
		}
	
	@Test
	public void testGetCheckerApprovalRequestsFailure() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("GetCheckerApprovalRequestsExpectedResults.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONObject expectedJson = new JSONObject(getResponseContent);
		
		HashMap<String,Object> securityAttributes= new HashMap<>();
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		userAttr.put(UtilConstants.USERNAME, "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		
        inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "AU0010001");
        inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
        inputMap.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
        inputMap.put(UtilConstants.SUBMITTEDDATE, "2024-02-14");
        inputMap.put(UtilConstants.PAGESIZE, "10");
        inputMap.put(UtilConstants.PAGEOFFSET, "1");
		
        String getMCConfigResponseFile = new File(
                getClass().getClassLoader().getResource("makerCheckerConfigDataLEId.json").getFile()).getPath();

        String getMCConfigResponseContent = new String(Files.readAllBytes(Paths.get(getMCConfigResponseFile)));
        JSONArray configData = new JSONArray(getMCConfigResponseContent);
        
        when(loadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(configData);
		
        JSONObject resultObj =  new JSONObject();
        resultObj.put(UtilConstants.DBP_ERR_CODE, "10031");
        resultObj.put(UtilConstants.DBP_ERR_MSG, "User don't have permission to Approval with request type for selected Legal entity");
        
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		set1.add("ApproveCreateContract");
		set1.add("ApproveUpdateContract");
		set1.add("ApproveCreateCustomer");
		set1.add("ApproveUpdateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		
		String legalEntityIds = "GB0010001";
		Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));
		
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);

		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
        
		when(makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputMap)).thenReturn(expectedJson);
		
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getCheckerApprovalRequests(dcRequest, inputMap);
		
		assertEquals(actualResult.getString(UtilConstants.DBP_ERR_CODE) , resultObj.get(UtilConstants.DBP_ERR_CODE));
        assertEquals(actualResult.getString(UtilConstants.DBP_ERR_MSG) , resultObj.get(UtilConstants.DBP_ERR_MSG));
		}
	
		
	@Test
	public void testGetMCModuleAction() throws Exception {

		String getBackendResponseFile = new File(
				getClass().getClassLoader().getResource("GetMCModuleActionBackendExpectedResults.json").getFile())
				.getPath();
		String getBackendResponseContent = new String(Files.readAllBytes(Paths.get(getBackendResponseFile)));
		JSONObject expectedBackendJson = new JSONObject(getBackendResponseContent);

		when(makerCheckerBackendDelegate.getMCModuleActionOperation(dcRequest, inputMap))
				.thenReturn(expectedBackendJson);

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.getMCModuleActionOperation(dcRequest, inputMap);

		assertEquals(actualResult.getJSONArray(UtilConstants.MOUDLES).getJSONObject(0).get(UtilConstants.MODULE_ID),
				expectedBackendJson.getJSONArray(UtilConstants.GET_MC_MODULEACTIONNAME_VIEW).getJSONObject(0)
						.get(UtilConstants.MODULEID));
	}
	
	@Test
	public void testWithdrawRequest() throws Exception {
		DataControllerRequest request = dcRequest;
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put("username", "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		HashMap<String, String> approvalRequestDetails = new HashMap<String, String>();
		approvalRequestDetails.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		approvalRequestDetails.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_1202093572");
		inputParams.put("action", "SID_WITHDRAWN");
		inputParams.put("approvalRequestDetails", approvalRequestDetails);

		when(request.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString()))
				.thenReturn(userAttr);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.toString()))
				.thenReturn(rawresponse);

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray configData = new JSONArray(getResponseContent);
		mockedLoadMakerCheckerConfigData.when(() -> LoadMakerCheckerConfigData.getMakerCheckerConfigData())
				.thenReturn(configData);

		mockedMakerCheckerUtils
				.when(() -> MakerCheckerUtils.updateApprovalRequestStatus(request, "REQ_1202093572",
						UtilConstants.SID_WITHDRAWN, null, UtilConstants.SID_PENDING, "admin1"))
				.thenReturn(new JSONObject("{\"status\":\"success\"}"));
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.withdrawRequest(request, inputParams);
		assertEquals("Request withdrawn successfully.", actualResult.getString("status"));
	}

	@Test
	public void testWithdrawRequestFailure() throws Exception {
		DataControllerRequest request = dcRequest;
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put("username", "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		HashMap<String, String> approvalRequestDetails = new HashMap<String, String>();
		approvalRequestDetails.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		approvalRequestDetails.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_1202093572");
		inputParams.put("action", "SID_WITHDRAWN");
		inputParams.put("approvalRequestDetails", approvalRequestDetails);

		when(request.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString()))
				.thenReturn(userAttr);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.toString()))
				.thenReturn(rawresponse);

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray configData = new JSONArray(getResponseContent);
		mockedLoadMakerCheckerConfigData.when(() -> LoadMakerCheckerConfigData.getMakerCheckerConfigData())
				.thenReturn(configData);

		mockedMakerCheckerUtils
				.when(() -> MakerCheckerUtils.updateApprovalRequestStatus(request, "REQ_1202093572",
						UtilConstants.SID_WITHDRAWN, null, UtilConstants.SID_PENDING, "admin1"))
				.thenReturn(new JSONObject("{\"status\":\"failure\"}"));
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.withdrawRequest(request, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10045.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10045.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}

	@Test
	public void testWithdrawRequestResponseNull() throws Exception {
		DataControllerRequest request = dcRequest;
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put("username", "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		HashMap<String, String> approvalRequestDetails = new HashMap<String, String>();
		approvalRequestDetails.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		approvalRequestDetails.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_1202093572");
		inputParams.put("action", "SID_WITHDRAWN");
		inputParams.put("approvalRequestDetails", approvalRequestDetails);

		when(request.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString()))
				.thenReturn(userAttr);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.toString()))
				.thenReturn(rawresponse);

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray configData = new JSONArray(getResponseContent);
		mockedLoadMakerCheckerConfigData.when(() -> LoadMakerCheckerConfigData.getMakerCheckerConfigData())
				.thenReturn(configData);

		mockedMakerCheckerUtils
				.when(() -> MakerCheckerUtils.updateApprovalRequestStatus(request, "REQ_1202093572",
						UtilConstants.SID_WITHDRAWN, null, UtilConstants.SID_PENDING, "admin1"))
				.thenReturn(new JSONObject());
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.withdrawRequest(request, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10026.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10026.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}

	@Test
	public void testWithdrawRequestInvalidInput() throws Exception {
		DataControllerRequest request = dcRequest;
		JSONObject rawresponse = new JSONObject();
		JSONObject userAttr = new JSONObject();
		userAttr.put("username", "admin1");
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);

		HashMap<String, String> approvalRequestDetails = new HashMap<String, String>();
		approvalRequestDetails.put(UtilConstants.EXPAPIOPNAME, "ContractManagementObjService_Contract_createContract");
		approvalRequestDetails.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("requestId", "REQ_1202093572");
		inputParams.put("action", "SID_PENDING");
		inputParams.put("approvalRequestDetails", approvalRequestDetails);

		when(request.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString()))
				.thenReturn(userAttr);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(rawresponse.toString()))
				.thenReturn(rawresponse);

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		JSONArray configData = new JSONArray(getResponseContent);
		mockedLoadMakerCheckerConfigData.when(() -> LoadMakerCheckerConfigData.getMakerCheckerConfigData())
				.thenReturn(configData);

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.withdrawRequest(request, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10046.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10046.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}

	@Test
	public void testWithdrawRequestException() throws Exception {
		DataControllerRequest request = dcRequest;
		JSONObject rawresponse = new JSONObject();
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		when(request.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		mockedCommonUtilities.when(() -> CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString()))
				.thenReturn(rawresponse);

		JSONObject actualResult = makerCheckerBusinessDelegateImpl.withdrawRequest(request, new HashMap<String, Object>());
		assertEquals(0, actualResult.keySet().size());
	}
	
	@Test
	public void testEnrollCustomerViewDetails() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject approvalRequestsResponse = new JSONObject(getContent);
		
		String getEnrollCusResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getEnrollCustomerViewDetailsInfoResponse.json").toURI())
				.toString();
		String getEnrollCusContent = new String(Files.readAllBytes(Paths.get(getEnrollCusResponsePath)));
		JSONObject enrollCustomerViewDetailsResponse = new JSONObject(getEnrollCusContent);
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(approvalRequestsResponse);
		when(makerCheckerBackendDelegate.getEnrollCustomerViewDetails(any(), anyMap())).thenReturn(enrollCustomerViewDetailsResponse);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.enrollCustomerViewDetails(dcRequest, inputParams);
		assertEquals(6, actualResult.keySet().size());
	}
	
	@Test
	public void testEnrollCustomerViewDetailsException() throws Exception {	
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenThrow(new RuntimeException());
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.enrollCustomerViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
	
	@Test
	public void testEnrollCustomerViewDetailsFailure() throws Exception {	
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(null);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.enrollCustomerViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10009.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10009.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
	
	@Test
	public void testEnrollCustomerViewDetailsNull() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject approvalRequestsResponse = new JSONObject(getContent);
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(approvalRequestsResponse);
		when(makerCheckerBackendDelegate.getEnrollCustomerViewDetails(any(), anyMap())).thenReturn(null);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.enrollCustomerViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
	
	@Test
	public void testCreateContractViewDetails() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsRespForContract.json").toURI())
				.toString();
		String getRequestContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject approvalRequestsResponse = new JSONObject(getRequestContent);
		
		String responsePath = Paths
				.get(getClass().getClassLoader().getResource("getEnrollCustomerViewDetailsInfoResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(responsePath)));
		JSONObject viewDetailsResponse = new JSONObject(getContent);
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(approvalRequestsResponse);
		when(makerCheckerBackendDelegate.getEnrollCustomerViewDetails(any(), anyMap())).thenReturn(viewDetailsResponse);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.createContractViewDetails(dcRequest, inputParams);
		assertEquals(13, actualResult.keySet().size());
	}
	
	@Test
	public void testCreateContractViewDetailsWhenException() throws Exception {	
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenThrow(new RuntimeException());
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.createContractViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10056.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10056.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
	
	@Test
	public void testCreateContractViewDetailsWhenFailure() throws Exception {	
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(null);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.createContractViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10009.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10009.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
	
	@Test
	public void testCreateContractViewDetailsWhenNull() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject approvalRequestsResponse = new JSONObject(getContent);
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("requestId", "REQ_1281594362");

		when(makerCheckerBackendDelegate.getApprovalRequests(dcRequest, "REQ_1281594362")).thenReturn(approvalRequestsResponse);
		when(makerCheckerBackendDelegate.getEnrollCustomerViewDetails(any(), anyMap())).thenReturn(null);
		JSONObject actualResult = makerCheckerBusinessDelegateImpl.createContractViewDetails(dcRequest, inputParams);
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE).toString());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG).toString());
	}
}

package com.kony.MakerChecker.backenddelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.skyscreamer.jsonassert.JSONAssert;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.backenddelegate.api.MakerCheckerBackendDelegate;
import com.kony.makerchecker.backenddelegate.impl.MakerCheckerBackendDelegateImpl;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, AuditHandler.class })
public class MakerCheckerBackendDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	MakerCheckerBackendDelegate makerCheckerBackendDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	JSONObject actualJSONObject;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	CommonUtilities commonUtilities;
	Map<String, Object> postParametersMap;
	Map<String, Object> headerMap;
	LoadMakerCheckerConfigData loadConfigDataService;
	MakerCheckerUtils makerCheckerUtils;
	ServicesManager serviceManager;
	IdentityHandler identityHandler;
	
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedCommonUtilities;
	private static MockedStatic<LoadMakerCheckerConfigData> mockedLoadConfigDataService;
	private static MockedStatic<MakerCheckerUtils> mockedMakerCheckerUtils;
	private static MockedStatic<AuditHandler> mockedAuditHandler;
	
	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedLoadConfigDataService = Mockito.mockStatic(LoadMakerCheckerConfigData.class);
		mockedMakerCheckerUtils = Mockito.mockStatic(MakerCheckerUtils.class);
		mockedAuditHandler = Mockito.mockStatic(AuditHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPServiceExecutorBuilder.close();
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedCommonUtilities.close();
		mockedLoadConfigDataService.close();
		mockedMakerCheckerUtils.close();
		mockedAuditHandler.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		actualJSONObject = new JSONObject();
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		makerCheckerBackendDelegate = mock(MakerCheckerBackendDelegate.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		commonUtilities = mock(CommonUtilities.class);
		loadConfigDataService = mock(LoadMakerCheckerConfigData.class);
		
		logger = mock(Logger.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);

		postParametersMap = new HashMap<>();
		headerMap = new HashMap<>();

		serviceManager =  mock(ServicesManager.class);
		identityHandler = mock(IdentityHandler.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(MakerCheckerBackendDelegate.class))
				.thenReturn(makerCheckerBackendDelegate);
		
	}
	
	@Test
	public void testIsMakerCheckerEnabled() throws Exception{
		
		String getDBResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();
		String getDBResponseContent = new String(Files.readAllBytes(Paths.get(getDBResponseFile)));
		JSONArray getDBResponseJSONArray = new JSONArray(getDBResponseContent);
		String expApiOperationName = "CustomerManagementObjService_InfinityUser_createInfinityUser";
		String legalEntityId = "GB0010001";
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put(UtilConstants.IS_APPROVAL_REQ, UtilConstants.TRUE);
		
		Map<String, String> actualResult = new HashMap<String, String>();

		when(LoadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(getDBResponseJSONArray);
		
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.isMakerCheckerEnabled(dcRequest, expApiOperationName, legalEntityId);
		
		assertEquals(actualResult.get(UtilConstants.IS_APPROVAL_REQ), expectedResult.get(UtilConstants.IS_APPROVAL_REQ));
	}
	
	@Test
	public void testStorePayload() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("testStorePayload.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
	
		JSONObject actualResult = new JSONObject();
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.REQUEST_ID, "REQ_111");
		inputMap.put(UtilConstants.REQ_PAYLOAD, "{\"key1\":\"value1\",\"key2\":{\"key21\":\"value21\"}}");
		inputMap.put(UtilConstants.RECORD_ID, "363467");
		inputMap.put(UtilConstants.EXPAPIOPNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
		inputMap.put(UtilConstants.STATUS, "SID_PENDING");
		inputMap.put(UtilConstants.MODULE, "Customer Management");
		inputMap.put(UtilConstants.ACTION, "Customer");
		inputMap.put(UtilConstants.COMPANY_LEGAL_UNIT, "GB0010001");
		inputMap.put(UtilConstants.CREATED_BY, "admin1");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveCreateCustomer");
		
		Map<String, String> inputMapStr = new HashMap<>();
		when(CommonUtilities.getRandomId()).thenReturn((long) 111);			
		when(dcRequest.getParameter(UtilConstants.PAYLOAD)).thenReturn("{\"key1\":\"value1\",\"key2\":{\"key21\":\"value21\"}}");
		when(dcRequest.getParameter(UtilConstants.RECORD_ID)).thenReturn("363467");
		when(dcRequest.getParameter(UtilConstants.EXPAPIOPERATIONNAME)).thenReturn("CustomerManagementObjService_InfinityUser_createInfinityUser");
		when(dcRequest.getParameter(UtilConstants.LEGAL_ENTITY_ID)).thenReturn("GB0010001");
		when(dcRequest.getParameter(UtilConstants.MODULE)).thenReturn("Customer Management");
		when(dcRequest.getParameter(UtilConstants.ACTION)).thenReturn("Customer");
		when(dcRequest.getParameter(UtilConstants.CREATED_BY)).thenReturn("admin1");
		when(dcRequest.getParameter(UtilConstants.PERMISSION_NAME)).thenReturn("ApproveCreateCustomer");
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKERCHECKERCRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.APPROVAL_REQUESTS_CREATE)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
	
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.storePayloadForRequest(dcRequest, inputMapStr);
		
		assertEquals(actualResult.get(UtilConstants.REQUEST_ID), "REQ_111");
		assertEquals(actualResult.get(UtilConstants.RECORD_ID), "363467");
		assertEquals(actualResult.get(UtilConstants.EXPAPIOPNAME), "CustomerManagementObjService_InfinityUser_createInfinityUser");
	}
	
	@Test
	public void testGetDashboardCounts() throws Exception{
		HashMap<String,String> map = new HashMap<>();
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("get_mc_approvalrequests_view_response.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));

		String getResponseFile1 = new File(
				getClass().getClassLoader().getResource("getMakerCheckerRequests_DB_response.json").getFile()).getPath();
		String getResponseContent1 = new String(Files.readAllBytes(Paths.get(getResponseFile1)));
		JSONObject expectedJson1 = new JSONObject(getResponseContent1);
		
		Map<String, String> userAttributes = new HashMap<>();
		
		JSONObject userAttr = new JSONObject();
		userAttr.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001,AU0010001");
		userAttr.put(UtilConstants.USERNAME, "admin1");
		
		JSONObject rawresponse = new JSONObject();
		rawresponse.put(UtilConstants.USER_ATTRIBUTES, userAttr);
		
		HashMap<String,Object> securityAttributes= new HashMap<>();
		securityAttributes.put(UtilConstants.RAW_RESPONSE, rawresponse);
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		Set<String> set2= new HashSet<String>();
		set1.add("ApproveCreateContract");
		set1.add("ApproveUpdateContract");
		set1.add("ApproveCreateCustomer");
		set1.add("ApproveUpdateCustomer");
		set2.add("ApproveCreateContract");
		set2.add("ApproveUpdateContract");
		set2.add("ApproveCreateCustomer");
		set2.add("ApproveUpdateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		leWisePermissionMap.put("AU0010001", set2);
		leWisePermissionMap.put("SHARED", set2);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		Map<String, Object> requestParameters1 = new HashMap<String, Object>();
		String legalEntityIds = "GB0010001,AU0010001,SHARED";
		String username = "admin1";
		Set<String> legalEntitySet = new HashSet<String>(Arrays.asList(legalEntityIds.split(",")));
		Set<String> leSet = Stream.of(legalEntityIds.trim().split(",")).collect(Collectors.toSet());
		String filter = UtilConstants.CREATED_BY+" eq '" + username + "' and "+UtilConstants.STATUS+" eq 'SID_PENDING' and ";
		filter = filter + "(" + UtilConstants.COMPANY_LEGAL_UNIT + " eq " + 
				String.join(" or " + UtilConstants.COMPANY_LEGAL_UNIT + " eq ", leSet)
				+ ")";
		
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		when(dcRequest.getServicesManager()).thenReturn(serviceManager);
		when(serviceManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getSecurityAttributes()).thenReturn(securityAttributes);
		
		when(CommonUtilities.getLoggedInUserAttributes(dcRequest)).thenReturn(userAttributes);
		when(CommonUtilities.getStringAsJSONObject(securityAttributes.get(UtilConstants.RAW_RESPONSE).toString())).thenReturn(rawresponse);
		when(CommonUtilities.getStringAsJSONObject(rawresponse.get(UtilConstants.USER_ATTRIBUTES).toString())).thenReturn(userAttr);
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.AC_MAKER_CHECKER_CRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.DB_GET_MC_APPROVALREQUESTS_VIEW)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		when(MakerCheckerUtils.getLEWisePermissions(dcRequest, legalEntitySet)).thenReturn(leWisePermissionMap);
		
		String filter1 = "";
		for (Map.Entry<String, Set<String>> entry : leWisePermissionMap.entrySet()) {
			String key = entry.getKey();
			Set<String> val = entry.getValue();
			filter1 = filter1 + "( "+UtilConstants.COMPANY_LEGAL_UNIT+" eq '" + key + "' and " +
					UtilConstants.CREATED_BY +" ne '"+username+"' and " +UtilConstants.STATUS +" eq 'SID_PENDING' and ";
			filter1 += "(" + UtilConstants.APPROVAL_PERMISSION_NAME + " eq "
					+ String.join(" or " + UtilConstants.APPROVAL_PERMISSION_NAME + " eq ", val) + ") )";
			filter1 = filter1 + (" or ");
		}
		
		filter1 = filter1.substring(0, filter1.length() - 4);
		requestParameters1.put(ODataQueryConstants.FILTER, filter1);
		
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters1)).thenReturn(dbpServiceExecutorBuilder);
	
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualJSONObject = makerCheckerBackendDelegateImpl.getDashboardCounts(dcRequest, map);
		
		JSONAssert.assertEquals(actualJSONObject, expectedJson1, false);
		
	}
	
	@Test
	public void testGetMakerCheckerPendingRequests() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("getMakerCheckerPendingRequests.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
	
		JSONObject actualResult = new JSONObject();
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("_module", "contractmanagement|customermanagement|usermanagement");
		inputParams.put("_action", "create|edit|update");
		inputParams.put("_record", "101010|100010,9593935265|119999,7592343258");
		inputParams.put("_companyLegalUnitId", "GB0010001|AU0010001|NL0010001");
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("_module", inputParams.get(UtilConstants.MODULE));
		requestParameters.put("_action", inputParams.get(UtilConstants.ACTION));
		requestParameters.put("_record", inputParams.get(UtilConstants.RECORD_ID));
		requestParameters.put("_companyLegalUnitId", inputParams.get(UtilConstants.COMPANY_LEGAL_UNIT));

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKERCHECKERCRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.DB_GET_PENDINGREQUESTS)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getMakerCheckerPendingRequests(dcRequest, inputParams);
		assertNotNull(actualResult.get("records"));
	}
	
	@Test
	public void testApprovalRequestViewDetails() throws Exception{
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.REQUEST_ID, "REQ1234");
		String[] params = null;
		
		String getDBResponseFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigViewAPIdata.json").getFile()).getPath();
		String getDBResponseContent = new String(Files.readAllBytes(Paths.get(getDBResponseFile)));
		JSONArray getDBResponseJSONArray = new JSONArray(getDBResponseContent);
		
		String getViewDetailsResponseFile = new File(
				getClass().getClassLoader().getResource("approvalRequestViewDetailsExpectedResult.json").getFile()).getPath();
		String getViewDetailsResponseContent = new String(Files.readAllBytes(Paths.get(getViewDetailsResponseFile)));
		JSONObject getViewDetailsResponseJSONObj = new JSONObject(getViewDetailsResponseContent);
		
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put("bankName", "Infinity");
		expectedResult.put(UtilConstants.OPSTATUS, "0");
		
		JSONObject actualResult = new JSONObject();

		when(LoadMakerCheckerConfigData.getMakerCheckerConfigData()).thenReturn(getDBResponseJSONArray);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(UtilConstants.REQUEST_ID, "REQ1234");
		for(int i = 0; i<getDBResponseJSONArray.length(); i++) {
			JSONObject jsonObj = (JSONObject) getDBResponseJSONArray.get(i);
			if(StringUtils.equals(jsonObj.getString(UtilConstants.ACTION), inputParams.get(UtilConstants.ACTION))) {
				String viewDetailsAPI = jsonObj.get(UtilConstants.VIEWDETAILS_API).toString();
				params = viewDetailsAPI.split(":");
			}
		}
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(params[0])).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(params[1])).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getViewDetailsResponseContent);
		
		when(makerCheckerBackendDelegate.approvalRequestViewDetails(dcRequest, inputParams))
        .thenReturn(getViewDetailsResponseJSONObj);
		
		
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult =  makerCheckerBackendDelegateImpl.approvalRequestViewDetails(dcRequest, inputParams);
		
		assertEquals(actualResult.get("bankName"), expectedResult.get("bankName"));
	}
	
	@Test
	public void testGetApprovalRequests() throws Exception {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_GET;
		String reqId = "Req_987";
		String filter = UtilConstants.REQUEST_ID + " eq '" + reqId + "'";

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filter);

		String getResponseFile = new File(
				getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getApprovalRequests(dcRequest, reqId);

		assertEquals(actualResult.optJSONArray("approvalrequests").optJSONObject(0).optString("requestId"), reqId);
	}
	
	@Test
	public void testGetAllMakerPendingRequests() throws Exception{
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("fetchAllMakerPendingRequests_DB_Response.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
	
		JSONObject actualResult = new JSONObject();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("_action", "CREATE_CUSTOMER");
		inputParams.put("_pageOffset", "0");
		inputParams.put("_submittedDate", "2024-02-20");
		inputParams.put("_legalEntityId", "GB0010001");
		inputParams.put("_pageSize", "10");
		inputParams.put("_module", "CUSTOMER_MANAGEMENT");
		inputParams.put("_userName", "admin1");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKERCHECKERCRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.DB_FETCH_MAKER_PENDING_REQUESTS_PROC)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputParams)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);

		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getAllMakerPendingRequests(dcRequest, inputParams);
		assertNotNull(actualResult.get("makerPendingRequests"));
	}
		
	@Test
	public void testGetCheckerApprovalRequests() throws Exception{		
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputParams.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputParams.put(UtilConstants.REQUESTTYPE, "CREATE_CUSTOMER");
		inputParams.put(UtilConstants.USER_NAME, "admin1");		
		inputParams.put(UtilConstants.SUBMITTEDDATE, "2024-02-14");
		inputParams.put(UtilConstants.PAGEOFFSET, "0");
		inputParams.put(UtilConstants.PAGESIZE, "10");
		
		String totalRecords = "9";
		String getCheckerApprovalResponseFile = new File(
				getClass().getClassLoader().getResource("GetCheckerApprovalBackendRequestsExpectedResults.json").getFile()).getPath();
		String getCheckerContent = new String(Files.readAllBytes(Paths.get(getCheckerApprovalResponseFile)));
		
		String getBackendResponseFile = new File(
				getClass().getClassLoader().getResource("GetCheckerApprovalRequestsExpectedResults.json").getFile()).getPath();
		String getBackendResponseContent = new String(Files.readAllBytes(Paths.get(getBackendResponseFile)));
		JSONObject expectedBackendJson = new JSONObject(getBackendResponseContent);
		
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put(UtilConstants.OPSTATUS, "0");
		
		JSONObject actualResult = new JSONObject();
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("_legalEntityId", "GB0010001");
		requestParameters.put("_module", "CUSTOMER_MANAGEMENT");
		requestParameters.put("_action", "CREATE_CUSTOMER");
		requestParameters.put("_userName", "admin1");
		requestParameters.put("_submittedDate", "2024-02-14");
		requestParameters.put("_pageSize", 0);
		requestParameters.put("_pageOffset",10);
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKER_CHECKER_CRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.DB_GET_CHECKERPENDING_REQUESTS_PROC)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getCheckerContent);
		
		when(makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputParams))
        .thenReturn(expectedBackendJson);
				
		actualResult =  makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputParams);
		
		assertEquals(actualResult.get("totalNumberOfRecords"), totalRecords);
	}
	

	@Test
	public void testGetMCModuleAction() throws Exception{		
		
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LANGUAGECODE, "en-GB");
		
		String getModuleActionMasterDataFile = new File(
				getClass().getClassLoader().getResource("GetMCModuleActionBackendExpectedResults.json").getFile()).getPath();
		String getModuleActionMasterDataContent = new String(Files.readAllBytes(Paths.get(getModuleActionMasterDataFile)));
		JSONObject expectedBackendJson = new JSONObject(getModuleActionMasterDataContent);
		
		Map<String, String> expectedResult = new HashMap<String, String>();
		expectedResult.put(UtilConstants.OPSTATUS, "0");
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = UtilConstants.LANGUAGECODE + " eq " + inputParams.get(UtilConstants.LANGUAGECODE);
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.MAKER_CHECKER_CRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.DB_GET_MC_MODULEACTIONNAME_VIEW_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(requestParameters)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getModuleActionMasterDataContent);
		
		when(makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputParams))
        .thenReturn(expectedBackendJson);
		
		JSONObject actualResult = new JSONObject();
				
		actualResult =  makerCheckerBackendDelegate.getCheckerApprovalRequests(dcRequest, inputParams);
		
		assertEquals(actualResult.getJSONArray(UtilConstants.GET_MC_MODULEACTIONNAME_VIEW).getJSONObject(0).get(UtilConstants.MODULEID) , 
		expectedBackendJson.getJSONArray(UtilConstants.GET_MC_MODULEACTIONNAME_VIEW).getJSONObject(0).get(UtilConstants.MODULEID));
	}	
	
	@Test
	public void testGetMakerCheckerConfigException() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_MAKER_CHECKER_CONFIG_VIEW;

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenThrow(new RuntimeException());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getMakerCheckerConfig(dcRequest, anyMap());
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10024.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10024.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}

	@Test
	public void testGetMakerCheckerConfig() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_MAKER_CHECKER_CONFIG_VIEW;
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LANGUAGE_CODE, "en-US");
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getMakerCheckerConfigurationsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject getContentObj = new JSONObject(getContent);
		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.MAKER_CHECKER_CONFIG_VIEW, getContentObj.get("MakerCheckerConfigData"));

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(responseObj.toString());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getMakerCheckerConfig(dcRequest, inputParams);
		assertEquals(1, actualResult.keySet().size());

	}

	@Test
	public void testGetMakerCheckerConfigError() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_MAKER_CHECKER_CONFIG_VIEW;
		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put(UtilConstants.LANGUAGE_CODE, "en-US");
		inputParams.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		JSONObject responseObj = new JSONObject();
		responseObj.put(UtilConstants.ERRORCODE, "20024");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(responseObj.toString());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getMakerCheckerConfig(dcRequest, inputParams);
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10012.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10012.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}

	@Test
	public void testUpdateMakerCheckerConfigException() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_UPDATE_MAKER_CHECKER_CONFIG_PROC;

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenThrow(new RuntimeException());
		doNothing().when(AuditHandler.class);
		AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
				ActivityStatusEnum.FAILED, "Maker Checker Configurations update failed");

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.updateMakerCheckerConfig(dcRequest, anyMap());
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10040.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}

	@Test
	public void testUpdateMakerCheckerConfig() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_UPDATE_MAKER_CHECKER_CONFIG_PROC;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject reponse = new JSONObject(getContent);
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", reponse.getJSONArray("makerCheckerConfig"));
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(UtilConstants.OPSTATUS, 0);
		expectedResult.put(UtilConstants.HTTP_STATUS_CODE, 0);

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(expectedResult.toString());
		LoadMakerCheckerConfigData loadMakerCheckerConfigData = mock(LoadMakerCheckerConfigData.class);
		when(loadMakerCheckerConfigData.loadDataFromDB()).thenReturn(mock(Result.class));
		doNothing().when(AuditHandler.class);
		AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL, "Maker Checker Configurations update success");

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.updateMakerCheckerConfig(dcRequest, inputParams);
		assertEquals(expectedResult.get(UtilConstants.OPSTATUS), actualResult.get(UtilConstants.OPSTATUS));
		assertEquals(expectedResult.get(UtilConstants.HTTP_STATUS_CODE),
				expectedResult.get(UtilConstants.HTTP_STATUS_CODE));

	}

	@Test
	public void testUpdateMakerCheckerConfigError() throws Exception {
		String serviceName = UtilConstants.AC_MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_UPDATE_MAKER_CHECKER_CONFIG_PROC;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("updateMakerCheckerConfigRequestPayload.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject reponse = new JSONObject(getContent);
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("mcConfigData", reponse.getJSONArray("makerCheckerConfig"));
		JSONObject expectedResult = new JSONObject();
		expectedResult.put(UtilConstants.OPSTATUS, -1);
		expectedResult.put(UtilConstants.HTTP_STATUS_CODE, -1);

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(expectedResult.toString());
		doNothing().when(AuditHandler.class);
		AuditHandler.auditAdminActivity(dcRequest, ModuleNameEnum.MAKERCHECKERCONFIGURATIONS, EventEnum.UPDATE,
				ActivityStatusEnum.FAILED, "Maker Checker Configurations update failed");

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.updateMakerCheckerConfig(dcRequest, inputParams);
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10012.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10012.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}

	@Test
	public void testUpdateApprovalRequests() throws Exception {
		String serviceName = UtilConstants.MAKERCHECKERCRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_UPDATE;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getApprovalRequestsResponse.json").toURI()).toString();
		String getResponse = new String(Files.readAllBytes(Paths.get(getResponsePath)));

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponse);

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.updateApprovalRequests(dcRequest, new HashMap<String, Object>());
		assertTrue(actualResult.length() > 0);
	}

	@Test
	public void testUpdateApprovalRequestsException() throws Exception {
		String serviceName = UtilConstants.MAKERCHECKERCRUD;
		String operationName = UtilConstants.APPROVAL_REQUESTS_UPDATE;

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenThrow(new RuntimeException());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.updateApprovalRequests(dcRequest, new HashMap<String, Object>());
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10051.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10051.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}
	
	@Test
	public void testGetEnrollCustomerViewDetails() throws Exception {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_ENROLLCUSTOMER_VIEWDETAILS_PROC;
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("getEnrollCustomerViewDetailsInfoResponse.json").toURI())
				.toString();
		String getResponse = new String(Files.readAllBytes(Paths.get(getResponsePath)));

		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("_companyLegalUnit", "GB0010001");
		inputParams.put("_roleId", "DEFAULT_GROUP");
		inputParams.put("_cif", "190615");
		inputParams.put("_servicedefId", "5801fa32-a416-45b6-af01-b22e2de93777");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponse);

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getEnrollCustomerViewDetails(dcRequest, inputParams);
		assertEquals(8, actualResult.length());
	}

	@Test
	public void testGetEnrollCustomerViewDetailsException() throws Exception {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_ENROLLCUSTOMER_VIEWDETAILS_PROC;

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenThrow(new RuntimeException());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getEnrollCustomerViewDetails(dcRequest,
				new HashMap<String, String>());
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10049.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}
	
	@Test
	public void testGetEditCustomerViewDetails() throws Exception {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_EDITCUSTOMER_VIEWDETAILS_PROC;
		String editCustomerViewDetailsProcResPath = Paths
				.get(getClass().getClassLoader().getResource("editCustomerViewDetailsProcRes.json").toURI())
				.toString();
		String res = new String(Files.readAllBytes(Paths.get(editCustomerViewDetailsProcResPath)));

		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("_legalEntityId", "GB0010001");
		inputParams.put("_customerId", "1002496540");

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(res);
		
		

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getEditCustomerViewDetails(dcRequest, inputParams);
		
		JSONArray editCustomerViewDetailsArray = actualResult.optJSONArray("records");
		String customerId = editCustomerViewDetailsArray.optJSONObject(0).optString("customerId");
		
		assertEquals("1002496540" , customerId);
	}
	
	
	@Test
	public void testGetEditCustomerViewDetailsException() throws Exception {
		String serviceName = UtilConstants.MAKER_CHECKER_CRUD;
		String operationName = UtilConstants.DB_GET_EDITCUSTOMER_VIEWDETAILS_PROC;

		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationName)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenThrow(new RuntimeException());

		JSONObject actualResult = new JSONObject();
		MakerCheckerBackendDelegateImpl makerCheckerBackendDelegateImpl = new MakerCheckerBackendDelegateImpl();
		actualResult = makerCheckerBackendDelegateImpl.getEditCustomerViewDetails(dcRequest,
				new HashMap<String, String>());
		assertEquals(2, actualResult.keySet().size());
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10052.getErrorCodeAsString()),
				actualResult.get(UtilConstants.DBP_ERR_CODE));
		assertEquals(String.valueOf(ErrorCodesEnum.ERR_10052.getMessage()),
				actualResult.get(UtilConstants.DBP_ERR_MSG));
	}
	
	
}
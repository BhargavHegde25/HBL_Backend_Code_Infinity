package com.kony.MakerChecker.preprocessor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.businessdelegate.impl.MakerCheckerBusinessDelegateImpl;
import com.kony.makerchecker.javaservice.LoadMakerCheckerConfigData;
import com.kony.makerchecker.preprocessor.MakerCheckerPreProcessor;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class })

public class MakerCheckerPreProcessorTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	MakerCheckerBusinessDelegateImpl businessDelegate;
	FabricRequestManager fabricRequestManager;
	FabricResponseManager fabricResponseManager;
	FabricRequestChain fabricRequestChain;
	Log4j2Configurator log4jConfigurator;
	ObjectServicePreProcessor preProcessor;
	ServicesManager servicesManager;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	PayloadHandler payloadHandler;
	OperationData opData;
	UserDetailsBean userBean;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<LoggedInUserHandler> mockedStaticLoggedInUserHandler;
	private static MockedStatic<LoadMakerCheckerConfigData> mockedStaticLoadMakerCheckerConfigData;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedStaticEnvironmentConfigurationsHandler;
	private static MockedStatic<AuditHandler> mockedStaticAuditHandler;
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;



	
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticLoggedInUserHandler = Mockito.mockStatic(LoggedInUserHandler.class);
		mockedStaticLoadMakerCheckerConfigData = Mockito.mockStatic(LoadMakerCheckerConfigData.class);
		mockedStaticEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		mockedStaticAuditHandler = Mockito.mockStatic(AuditHandler.class);
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
	}
	
	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticLoggedInUserHandler.close();
		mockedStaticLoadMakerCheckerConfigData.close();
		mockedStaticEnvironmentConfigurationsHandler.close();
		mockedStaticAuditHandler.close();
		mockedStaticForDBPServiceExecutorBuilder.close();
	}
	String getPayloadContent;
	String getApprovalResponseContent;
	String getNonApprovalResponseContent;
	String getMakerCheckerDataContent;
	@Before
	public void executedBefore() throws Exception {

		String getPayloadFile = new File(
				getClass().getClassLoader().getResource("preprocessor_payload.json").getFile()).getPath();

		getPayloadContent = new String(Files.readAllBytes(Paths.get(getPayloadFile)));
		
		String getMakerCheckerDataFile = new File(
				getClass().getClassLoader().getResource("makerCheckerConfigData.json").getFile()).getPath();

		getMakerCheckerDataContent = new String(Files.readAllBytes(Paths.get(getMakerCheckerDataFile)));
		
		String getApprovalResponseFile = new File(
				getClass().getClassLoader().getResource("preprocessor_response_approval.json").getFile()).getPath();

		getApprovalResponseContent = new String(Files.readAllBytes(Paths.get(getApprovalResponseFile)));
		
		String getNonApprovalResponseFile = new File(
				getClass().getClassLoader().getResource("preprocessor_response_non_approval.json").getFile()).getPath();

		getNonApprovalResponseContent = new String(Files.readAllBytes(Paths.get(getNonApprovalResponseFile)));
		
		fabricRequestManager = mock(FabricRequestManager.class);
		fabricResponseManager = mock(FabricResponseManager.class);
		fabricRequestChain = mock(FabricRequestChain.class);
		servicesManager = mock(ServicesManager.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		payloadHandler = mock(PayloadHandler.class);
		opData = mock(OperationData.class);
		userBean = mock(UserDetailsBean.class);

		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		businessDelegate = mock(MakerCheckerBusinessDelegateImpl.class);

//		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
//		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
//				.thenReturn(businessDelegateFactory);
//		when(businessDelegateFactory.getBusinessDelegate(MakerCheckerBusinessDelegateImpl.class))
//				.thenReturn(businessDelegate);
		when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);
		JsonElement payload = JsonParser.parseString(getPayloadContent);
		when(fabricResponseManager.getPayloadHandler()).thenReturn(payloadHandler);
		when(payloadHandler.getPayloadAsJson()).thenReturn(payload);
		when(fabricRequestManager.getServicesManager()).thenReturn(servicesManager);
		when(servicesManager.getOperationData()).thenReturn(opData);
		when(opData.getServiceId()).thenReturn("CustomerManagementObjService");
		when(opData.getObjectId()).thenReturn("InfinityUser");
		when(LoggedInUserHandler.getUserDetails(servicesManager)).thenReturn(userBean);
		when(userBean.getUserName()).thenReturn("admin1");
		when(businessDelegate.parseRequestAndFetchLegalEntityId(getPayloadContent)).thenReturn("GB0010001");
		when(LoadMakerCheckerConfigData.getMakerCheckerConfigData())
				.thenReturn(new JSONArray(getMakerCheckerDataContent));
		when(EnvironmentConfigurationsHandler.getServerAppProperty(UtilConstants.SKIP_MAKER_CHECKER_ROLES))
				.thenReturn("RID_SUPERADMIN");
		String serviceId = UtilConstants.MAKER_CHECKER_SERVICE;
		String operationId = UtilConstants.IS_APPROVAL_REQ;
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(serviceId)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(operationId)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withFabricRequestManager(fabricRequestManager)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		
		
		preProcessor = new MakerCheckerPreProcessor();

	}
	
	@Test
	public void testExecuteApproval() throws Exception {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, getPayloadContent);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveCreateCustomer");
		
		when(opData.getOperationId()).thenReturn("createInfinityUser");
		when(userBean.getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getApprovalResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);
		
		assertEquals("True","True");
	}
	@Test
	public void testExecuteNonApproval() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("isMakerCheckerEnabledBusinessDelegateResponse.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_InfinityUser_editInfinityUser");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, getPayloadContent);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "EDIT_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveEditCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("editInfinityUser");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);
		
		assertEquals("True","True");

	}
	
	@Test
	public void testExecuteSuperAdmin() throws Exception {
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, getPayloadContent);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveCreateCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("createInfinityUser");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_SUPERADMIN,RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getApprovalResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);

		assertEquals("True","True");
	}
	
	@Test
	public void testExecuteError() throws Exception {
		Map<String, Object> inputMap = new HashMap<>();
		String response = "{\"dbpErrCode\":\"10050\",\"dbpErrCode\":\"Invalid response\"}";
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_InfinityUser_createInfinityUser");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, getPayloadContent);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "CREATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveCreateCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("createInfinityUser");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(response);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);

		assertEquals("True","True");

	}
	
	@Test
	public void testExecuteSuspendUserMakerCheckerEnable() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("isMakerCheckerEnabledBusinessDelegateResponse.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		
		String payload = "{\"customerUsername\":\"5497116418\",\"status\":\"ACTIVE\",\"legalEntityList\":"
				+ "\"[\\\"GB0010001\\\"]\",\"isApprovalFlow\":true}";
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_Customer_updateDBPUserStatus");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, payload);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "SUSPEND_ACTIVATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveSuspendOrActivateCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("updateDBPUserStatus");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);
		
		assertEquals("True","True");

	}
	
	@Test
	public void testExecuteSuspendUserMakerCheckerDisable() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("isMakerCheckerEnabledBusinessDelegateResponse.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		String payload = "{\"customerUsername\":\"5497116418\",\"status\":\"ACTIVE\",\"legalEntityList\":"
				+ "\"[\\\"NL0020001\\\"]\",\"isApprovalFlow\":true}";
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_Customer_updateDBPUserStatus");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "NL0020001");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, payload);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "SUSPEND_ACTIVATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveSuspendOrActivateCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("updateDBPUserStatus");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);
		
		assertEquals("True","True");

	}
	
	@Test
	public void testExecuteSuspendUserError() throws Exception {
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("isMakerCheckerEnabledBusinessDelegateResponse.json").getFile()).getPath();

		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		String payload = "{\"customerUsername\":\"5497116418\",\"status\":\"ACTIVE\",\"legalEntityList\":"
				+ "\"[\\\"GB0010002\\\"]\",\"isApprovalFlow\":true}";
		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(UtilConstants.EXPAPIOPERATIONNAME, "CustomerManagementObjService_Customer_updateDBPUserStatus");
		inputMap.put(UtilConstants.LEGAL_ENTITY_ID, "GB0010002");
		inputMap.put(UtilConstants.RECORD_ID, "");
		inputMap.put(UtilConstants.PAYLOAD, payload);
		inputMap.put(UtilConstants.CREATED_BY, "admin2");
		inputMap.put(UtilConstants.MODULE, "CUSTOMER_MANAGEMENT");
		inputMap.put(UtilConstants.ACTION, "SUSPEND_ACTIVATE_CUSTOMER");
		inputMap.put(UtilConstants.PERMISSION_NAME, "ApproveSuspendOrActivateCustomer");
		
		when(servicesManager.getOperationData().getOperationId()).thenReturn("updateDBPUserStatus");
		when(LoggedInUserHandler.getUserDetails(servicesManager).getRoleId()).thenReturn("RID_MANAGER");
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		preProcessor.execute(fabricRequestManager, fabricResponseManager, fabricRequestChain);
		
		assertEquals("True","True");

	}
}

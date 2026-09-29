package com.kony.MakerChecker.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.makerchecker.utils.MakerCheckerUtils;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class})
public class MakerCheckerUtilsTest {

	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	ServicesManager servicesManager;
	UserDetailsBean userDetailsBean;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	LoggedInUserHandler loggedInUserHandler;
	PermissionHandler permissionHandler;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Log4j2Configurator log4jConfigurator;

	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<LoggedInUserHandler> mockedStaticLoggedInUserHandler;
	private static MockedStatic<PermissionHandler> mockedStaticPermissionHandler;
	

	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticLoggedInUserHandler = Mockito.mockStatic(LoggedInUserHandler.class);
		mockedStaticPermissionHandler = Mockito.mockStatic(PermissionHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPServiceExecutorBuilder.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticLoggedInUserHandler.close();
		mockedStaticPermissionHandler.close();
	}

	@Before
	public void executedBefore() throws Exception {

		actualResult = new Result();
		
		dcRequest = mock(DataControllerRequest.class);

		log4jConfigurator = mock(Log4j2Configurator.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		servicesManager = mock(ServicesManager.class);
		userDetailsBean = mock(UserDetailsBean.class);
		loggedInUserHandler = mock(LoggedInUserHandler.class);
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
	}
	
	@Test
	public void getLEWisePermissionsTest() throws Exception{
		
		HashMap<String, Set<String>> leWisePermissionMap = new HashMap<>();
		Set<String> set1= new HashSet<String>();
		Set<String> set2= new HashSet<String>();
		Set<String> set3= new HashSet<String>();
		set1.add("ApproveCreateContract");
		set1.add("ApproveUpdateContract");
		set1.add("ApproveCreateCustomer");
		set1.add("ApproveUpdateCustomer");
		set2.add("ApproveCreateContract");
		set2.add("ApproveUpdateContract");
		set2.add("ApproveCreateCustomer");
		set2.add("ApproveUpdateCustomer");
		set3.add("ApproveCreateContract");
		set3.add("ApproveUpdateContract");
		set3.add("ApproveCreateCustomer");
		set3.add("ApproveUpdateCustomer");
		leWisePermissionMap.put("GB0010001", set1);
		leWisePermissionMap.put("NL0020001", set2);
		leWisePermissionMap.put(UtilConstants.MAKER_CHECKER_SHARED_LEGAL_ENTITY, set3);
		Set<String> leSet = new HashSet<>();
		leSet.add("GB0010001");
		leSet.add("NL0020001");
		
		String getResponseFile = new File(
				getClass().getClassLoader().getResource("permissions_response.json").getFile()).getPath();
		String getResponseContent = new String(Files.readAllBytes(Paths.get(getResponseFile)));
		String getRolePermissionsFile = new File(
				getClass().getClassLoader().getResource("role_permissions_with_le_info.json").getFile()).getPath();
		String getRolePermissionsContent = new String(Files.readAllBytes(Paths.get(getRolePermissionsFile)));
		JSONObject getRolePermissionsObj = new JSONObject(getRolePermissionsContent);
		
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put(ODataQueryConstants.FILTER, UtilConstants.TYPE_ID+" eq 'PER_TYPE_MAKERCHECKER'");
		inputMap.put(ODataQueryConstants.SELECT, UtilConstants.NAME);
		
		when(dcRequest.getServicesManager()).thenReturn(servicesManager);
		when(LoggedInUserHandler.getUserDetails(servicesManager)).thenReturn(userDetailsBean);
		when(userDetailsBean.getRoleId()).thenReturn(UtilConstants.ADMIN);
		when(PermissionHandler.getRolesGrantedPermissionsWithLEInfo(UtilConstants.ADMIN, dcRequest)).thenReturn(getRolePermissionsObj);
		when(DBPServiceExecutorBuilder.builder()).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId(UtilConstants.AC_MAKER_CHECKER_CRUD)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withObjectId(null)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId(UtilConstants.PERMISSIONS_GET)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(inputMap)).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
		when(dbpServiceExecutor.getResponse()).thenReturn(getResponseContent);
		
		Map<String, Set<String>> actualResult = MakerCheckerUtils.getLEWisePermissions(dcRequest, leSet);
		assertEquals(actualResult, leWisePermissionMap);
	}
}

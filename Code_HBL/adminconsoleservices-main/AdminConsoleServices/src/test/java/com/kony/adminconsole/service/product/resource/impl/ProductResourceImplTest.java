package com.kony.adminconsole.service.product.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.kony.adminconsole.preprocessor.ProductsAuthorizationPreProcessor;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.ProductBusinessDelegate;
import com.kony.adminconsole.service.productmanagement.resource.impl.ProductResourceImpl;
import com.kony.adminconsole.preprocessor.ProductsAuthorizationPreProcessor;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class,
		ApplicationParametersHandler.class, AuditHandler.class})
public class ProductResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	ProductResourceImpl productResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	ProductBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	EnvironmentConfigurationsHandler environmentConfigurationsHandler;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Executor executor;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	ApplicationParametersHandler applicationParametersHandler;
	CommonUtilities commonUtilities;
	CommonUtilsC360 commonUtilsC360;
	AuditHandler auditHandler;
	ProductsAuthorizationPreProcessor authObj;
	StringEscapeUtils stringEscapeUtils;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedStaticCommonUtilities;
	private static MockedStatic<CommonUtilsC360> mockedStaticCommonUtilsC360;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedEnvironmentConfigurationsHandler;
	private static MockedStatic<ApplicationParametersHandler> mockedStaticApplicationParametersHandler;
    private static MockedStatic<AuditHandler> mockedAuditHandler;
    private static MockedStatic<ProductsAuthorizationPreProcessor> mockedProductsAuthorizationPreProcessor;
    private static MockedStatic<StringEscapeUtils> mockedStringEscapeUtils;
    
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedStaticApplicationParametersHandler = Mockito.mockStatic(ApplicationParametersHandler.class);
		mockedAuditHandler = Mockito.mockStatic(AuditHandler.class);
		mockedEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		mockedStaticCommonUtilsC360 = Mockito.mockStatic(CommonUtilsC360.class);
		mockedProductsAuthorizationPreProcessor = Mockito.mockStatic(ProductsAuthorizationPreProcessor.class);
		mockedStringEscapeUtils = Mockito.mockStatic(StringEscapeUtils.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticCommonUtilities.close();
		mockedStaticApplicationParametersHandler.close();
		mockedEnvironmentConfigurationsHandler.close();
		mockedAuditHandler.close();
		mockedStaticCommonUtilsC360.close();
		mockedProductsAuthorizationPreProcessor.close();
		mockedStringEscapeUtils.close();
	}

	@Before
	public void executedBefore() throws Exception {

		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		
		HashMap<String,String> map = new HashMap<>();
		Result expectedResult = new Result();
		
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);

		businessDelegate = mock(ProductBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		applicationParametersHandler = mock(ApplicationParametersHandler.class);
		authObj = mock(ProductsAuthorizationPreProcessor.class);
		log4jConfigurator = mock(Log4j2Configurator.class);
		auditHandler = mock(AuditHandler.class);
		commonUtilsC360 = mock(CommonUtilsC360.class);
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(ProductBusinessDelegate.class))
				.thenReturn(businessDelegate);
		ProductsAuthorizationPreProcessor authObj = new ProductsAuthorizationPreProcessor();
	}

	@Test
	public void testInvoke() throws Exception {
		
		productResourceImpl = new ProductResourceImpl();
		HashMap<String,String> map = new HashMap<>();
			Result expectedResult = new Result();
		String methodID = new String();
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("ProductResponse.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		JSONObject appData = new JSONObject(appDataContent);	
		when(EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",dcRequest)).thenReturn("DBXDB");
		when(dcRequest.getParameter("Authorization")).thenReturn("1234");
		when(dcRequest.getParameter("branchRef")).thenReturn("GB0010001");
		when(dcRequest.getParameter("productRef")).thenReturn("SAVINGS.ACCOUNT");
		
		Map<String, Object> postParametersMap = new HashMap<>();
		
		postParametersMap.put("branchRef", "GB0010001");
    	postParametersMap.put("productRef", "SAVINGS.ACCOUNT");
    	postParametersMap.put("marketingCatalogBackend", "DBXDB");
    	boolean isAuthSuccess = true;
    	when(authObj.execute(map, dcRequest,dcResponse,expectedResult)).thenReturn(isAuthSuccess);
    	when(CommonUtilsC360.setCloudAuthenticationHeaders(dcRequest,TemenosConstantsC360.MARKETING_CATALOGUE_DEPLOYMENT_PLATFORM,TemenosConstantsC360.MARKETING_CATALOGUE_AUTHORIZATION_KEY)).thenReturn(true);
    	when(CommonUtilsC360.setAuthenticationHeader(dcRequest,TemenosConstantsC360.POST_LOGIN_FLOW,"ms")).thenReturn(true);
    	when(businessDelegate.getProducts(postParametersMap, "1234")).thenReturn(appData);
    	when(CommonUtilities.constructResultFromJSONObject(appData)).thenReturn(expectedResult);
    	
//    	when(auditHandler.auditAdminActivity(dcRequest, "product", EventEnum.SEARCH,
//    			ActivityStatusEnum.SUCCESSFUL, "Successfully fetched product details for ProductRef : "+ "1234")).thenReturn("SUCCESS");  	

    	actualResult = productResourceImpl.getProducts(methodID, inputArray, dcRequest, dcResponse);

		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
	}
	
	@Test
	public void testInvoke1() throws Exception {
		
		productResourceImpl = new ProductResourceImpl();
		HashMap<String,String> map = new HashMap<>();
			Result expectedResult = new Result();
		String methodID = new String();
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("ProductFacilityDeleteResponse.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		JSONObject appData = new JSONObject(appDataContent);	
		when(EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",dcRequest)).thenReturn("DBXDB");
		when(dcRequest.getParameter("productId")).thenReturn("PR231202BT7Q");
		when(dcRequest.getParameter("facilityId")).thenReturn("FC23311Q4ZZK");
		when(dcRequest.getParameter("productFacilityId")).thenReturn("PF23338R2TGC");
		when(dcRequest.getParameter("Authorization")).thenReturn("1234");
		Map<String, Object> postParametersMap = new HashMap<>();
		
		postParametersMap.put("productId", "PR231202BT7Q");
    	postParametersMap.put("facilityId", "FC23311Q4ZZK");
    	postParametersMap.put("productFacilityId", "PF23338R2TGC");
    	postParametersMap.put("marketingCatalogBackend", "DBXDB");
    	boolean isAuthSuccess = true;
    	when(authObj.execute(map, dcRequest,dcResponse,expectedResult)).thenReturn(isAuthSuccess);
    	when(CommonUtilsC360.setCloudAuthenticationHeaders(dcRequest,TemenosConstantsC360.MARKETING_CATALOGUE_DEPLOYMENT_PLATFORM,TemenosConstantsC360.MARKETING_CATALOGUE_AUTHORIZATION_KEY)).thenReturn(true);
    	when(CommonUtilsC360.setAuthenticationHeader(dcRequest,TemenosConstantsC360.POST_LOGIN_FLOW,"ms")).thenReturn(true);
    	when(businessDelegate.deleteProductFacility(postParametersMap, "1234")).thenReturn(appData);
    	when(CommonUtilities.constructResultFromJSONObject(appData)).thenReturn(expectedResult);
    	
    	actualResult = productResourceImpl.deleteProductFacility(methodID, inputArray, dcRequest, dcResponse);

    	//actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(appData.getString("status"),actualResult.getParamByName("status").getValue());
	}
    
}

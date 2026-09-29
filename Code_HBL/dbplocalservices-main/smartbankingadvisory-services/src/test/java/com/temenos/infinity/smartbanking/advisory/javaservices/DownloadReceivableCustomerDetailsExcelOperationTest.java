package com.temenos.infinity.smartbanking.advisory.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.logger.Logger;



@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPServiceExecutorBuilder.class, DBPServiceExecutor.class })
public class DownloadReceivableCustomerDetailsExcelOperationTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Session session;
	SmartBankingAdvisoryBusinessDelegate SBABusinessDel;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	BusinessDelegateFactory businessDelegateFactory;
	JavaService2 javaService2;
	Log4j2Configurator log4jConfigurator;
	

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}

	@Before
	public void executedBefore() throws Exception {

		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();

		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		SBABusinessDel = mock(SmartBankingAdvisoryBusinessDelegate.class);
		
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		log4jConfigurator = mock(Log4j2Configurator.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		javaService2 = mock(DownloadReceivableCustomerDetailsExcelOperation.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
		.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class))
		.thenReturn(SBABusinessDel);
		
		when(Log4j2Configurator.getInstance()).thenReturn(log4jConfigurator);

	}

	@Test
	public void testInvoke() throws Exception {
		Result expectedResult = new Result();
		expectedResult.addOpstatusParam(0);
		expectedResult.addHttpStatusCodeParam(200);

		Map<String, Object> payloadMap = new HashMap<>();
		payloadMap.put("queryParam", "we");
		payloadMap.put("orderParam", "we");
		payloadMap.put("modelName", "we");
		payloadMap.put("Subscriber", "we");
		payloadMap.put("topParam", "we");
		payloadMap.put("businessName", "we");
		payloadMap.put("subTitle", "we");
		payloadMap.put("type", "Overdue");
		
		inputArray[1] = payloadMap;
		Result result = new Result();
		
		String downloadReceivableCustomerResponse = new File(
				getClass().getClassLoader().getResource("getReceivableCustomerDetailsXLResp.json").getFile()).getPath();
		String downloadReceivableCustomerResponseContent = new String(Files.readAllBytes(Paths.get(downloadReceivableCustomerResponse)));
		result = JSONToResult.convert(downloadReceivableCustomerResponseContent);
		
		String getReceivableSumBusinessResponse = new File(
				getClass().getClassLoader().getResource("DownloadReceivableSummaryBusinessResponse.json").getFile()).getPath();
		String getReceivableSumBusinessResponseContent = new String(Files.readAllBytes(Paths.get(getReceivableSumBusinessResponse)));
		JSONObject jsObj = new JSONObject(getReceivableSumBusinessResponseContent);
		
		Map<String, Object> updatedCustomerData = new LinkedHashMap<>();
		for(String key: jsObj.keySet()) {
			updatedCustomerData.put(key, jsObj.get(key));
		}
		
		when(SBABusinessDel.getReceivableOverdueExcel(payloadMap)).thenReturn(updatedCustomerData);

		doReturn(result).when(javaService2).invoke(methodID, inputArray, dcRequest, dcResponse);
		
		actualResult = (Result) javaService2.invoke(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName("opstatus"), expectedResult.getParamValueByName("opstatus"));
		assertEquals(actualResult.getParamValueByName("httpStatusCode"),
				expectedResult.getParamValueByName("httpStatusCode"));
	}

}

package com.temenos.infinity.smartbanking.advisory.backenddelegate.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.temenos.infinity.smartbanking.advisory.utils.BackendCommonUtils;
import com.temenos.infinity.smartbanking.advisory.utils.CommonUtils;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.impl.SmartBankingAdvisoryBackendDelegateImpl;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;

@PrepareForTest({ DBPServiceExecutorBuilder.class })
public class SmartBankingAdvisoryBackendDelegateImplTest {
	Map<String, Object> payloadMap = new HashMap<>();
	private SmartBankingAdvisoryBackendDelegateImpl backendDelegate;
	private DBPServiceExecutorBuilder serviceExecutorBuilder;
	private DBPServiceExecutor serviceExecutor;
	DBPServiceExecutorBuilder dbpServiceExecutorBuilder;

	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
	}

	@AfterClass
	public static void tearDown() {
		mockedStaticForDBPServiceExecutorBuilder.close();
	}

	@Before
	public void setUp() throws DBPApplicationException {
		payloadMap.put("queryParam", "someValue");
		payloadMap.put("modelName", "someValue");
		backendDelegate = new SmartBankingAdvisoryBackendDelegateImpl();
		serviceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		serviceExecutor = mock(DBPServiceExecutor.class);
		mock(Result.class);
		mock(Result.class);

		mockedStaticForDBPServiceExecutorBuilder.when(DBPServiceExecutorBuilder::builder)
				.thenReturn(serviceExecutorBuilder);
		when(serviceExecutorBuilder.withServiceId("AnalyticsJSONServices")).thenReturn(serviceExecutorBuilder);
		when(serviceExecutorBuilder.withOperationId("GetDetails")).thenReturn(serviceExecutorBuilder);
		when(serviceExecutorBuilder.withRequestParameters(payloadMap)).thenReturn(serviceExecutorBuilder);
		when(serviceExecutorBuilder.build()).thenReturn(serviceExecutor);
	}

	@Test
	public void testGetAccountsReceivableSuccess() throws Exception {
		Result result = new Result();
		Result expectedresult = new Result();
		expectedresult.addParam("overdue0to10Days", "3143325");
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetAccountsReceivableResponse.json").getFile()).getPath();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
		JSONObject expectedJson = new JSONObject(getCompanyLEsResponseContent);
		mockStatic(BackendCommonUtils.class);
		when(BackendCommonUtils.isBackendResponseSuccess(any(Result.class), eq("httpStatusCode"))).thenReturn(true);
		String groupsResponse = expectedJson.toString();
		Result expectedResult = new Result();
		expectedResult = Utilities.constructResultFromJSONObject(expectedJson);
		when(serviceExecutor.getResult()).thenReturn(expectedResult);
		result = backendDelegate.getAccountsReceivable(payloadMap);
		Dataset dataset = result.getDatasetById("value");
		Record record = dataset.getRecord(0);
		assertEquals("value", expectedresult.getParamByName("overdue0to10Days").getValue(),
				record.getParamByName("overdue0to10Days").getValue());
	}
}

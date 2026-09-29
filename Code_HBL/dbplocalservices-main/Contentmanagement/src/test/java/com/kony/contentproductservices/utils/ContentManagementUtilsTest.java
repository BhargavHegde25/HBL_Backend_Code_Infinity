package com.kony.contentproductservices.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

@PrepareForTest({ EnvironmentConfigurationsHandler.class, DBPServiceExecutor.class, DBPServiceExecutorBuilder.class })
public class ContentManagementUtilsTest {

	private static MockedStatic<EnvironmentConfigurationsHandler> mockedEnvironmentConfigurationsHandler;
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStaticForDBPServiceExecutorBuilder;
	private static DBPServiceExecutor serviceExecutor;
	private static DBPServiceExecutorBuilder dbpServiceExecutorBuilder;

	@BeforeClass
	public static void init() {
		mockedEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
		mockedStaticForDBPServiceExecutorBuilder = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedEnvironmentConfigurationsHandler.close();
		mockedStaticForDBPServiceExecutorBuilder.close();
	}

	@Before
	public void setup() throws Exception {
		dbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		serviceExecutor = mock(DBPServiceExecutor.class);

		mockedEnvironmentConfigurationsHandler
				.when(() -> EnvironmentConfigurationsHandler.getServerAppProperty("CONSENT_BACKEND"))
				.thenReturn(ContentManagementConstants.CONSENT_BACKEND_DBXDB);
		mockedStaticForDBPServiceExecutorBuilder.when(() -> DBPServiceExecutorBuilder.builder())
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withServiceId("TNCDBServices")).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId("dbxdb_termandcondition_get"))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withOperationId("dbxdb_termandconditiontext_get"))
				.thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(dbpServiceExecutorBuilder);
		when(dbpServiceExecutorBuilder.build()).thenReturn(serviceExecutor);
	}

	@Test
	public void testGetTermsAndConditions() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("ContentManagementUtilsResponses.json").toURI())
				.toString();
		String response = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		when(serviceExecutor.getResponse()).thenReturn(response);
		Result actualResult = ContentManagementUtils.getTermsAndConditions("Login_TnC", "GB0010001",
				"RETAIL_AND_BUSINESS_BANKING", "en-US");
		assertEquals(4, actualResult.getAllParams().size());
		assertEquals("Success", actualResult.getParamByName("status").getValue());
	}

	@Test
	public void testTermAndConditionResponseNull() throws Exception {
		when(serviceExecutor.getResponse()).thenReturn(null);
		Result result = ContentManagementUtils.getTermsAndConditions("Login_TnC", "GB0010001",
				"RETAIL_AND_BUSINESS_BANKING", "en-US");
		assertEquals(5, result.getAllParams().size());
		assertEquals(String.valueOf(ErrorCodeEnum.ERR_29063.getErrorCode()),
				result.getParamByName("dbpErrCode").getValue());
		assertEquals("Failure", result.getParamByName("status").getValue());
	}

	@Test
	public void testTermAndConditionResponseEmpty() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("ContentManagementUtilsResponses.json").toURI())
				.toString();
		String response = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject responseJSON = new JSONObject(response);
		responseJSON.optJSONArray("termandcondition").remove(0);
		when(serviceExecutor.getResponse()).thenReturn(responseJSON.toString());
		Result result = ContentManagementUtils.getTermsAndConditions("Login_TnC", "GB0010001",
				"RETAIL_AND_BUSINESS_BANKING", "en-US");
		assertEquals(5, result.getAllParams().size());
		assertEquals(String.valueOf(ErrorCodeEnum.ERR_29063.getErrorCode()),
				result.getParamByName("dbpErrCode").getValue());
		assertEquals("Failure", result.getParamByName("status").getValue());
	}

	@Test
	public void testGetTermsAndConditionsException() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("ContentManagementUtilsResponses.json").toURI())
				.toString();
		String response = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		JSONObject responseJSON = new JSONObject(response);
		responseJSON.optJSONArray("termandconditiontext").remove(0);
		when(serviceExecutor.getResponse()).thenReturn(responseJSON.toString());

		Result actualResult = ContentManagementUtils.getTermsAndConditions("Login_TnC", "GB0010001",
				"RETAIL_AND_BUSINESS_BANKING", "en-US");
		assertEquals(0, actualResult.getAllParams().size());
	}
}

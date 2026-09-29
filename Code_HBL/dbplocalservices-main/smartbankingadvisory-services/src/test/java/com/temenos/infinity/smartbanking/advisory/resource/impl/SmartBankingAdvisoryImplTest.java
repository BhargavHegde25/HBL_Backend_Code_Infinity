package com.temenos.infinity.smartbanking.advisory.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;
import com.temenos.infinity.smartbanking.advisory.utils.BackendCommonUtils;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class })
public class SmartBankingAdvisoryImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	SmartBankingAdvisoryResourceImpl SmartBankingAdvisoryResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	SmartBankingAdvisoryBusinessDelegate SmartBankingAdvisoryBusinessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Logger logger;
	Session session;

	@Mock
	private SmartBankingAdvisoryBusinessDelegate businessDelegate;

	@InjectMocks
	private SmartBankingAdvisoryResourceImpl smartBankingAdvisoryResource;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
	}

	@AfterClass
	public static void tearDown() {
		mockedStaticForDBPAPIFactoryImpl.close();
	}

	@Before
	public void executedBefore() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		session = mock(Session.class);
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		businessDelegate = mock(SmartBankingAdvisoryBusinessDelegate.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(SmartBankingAdvisoryBusinessDelegate.class))
				.thenReturn(businessDelegate);

		smartBankingAdvisoryResource = new SmartBankingAdvisoryResourceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetReceivablesAccount() throws Exception {
		Map<String, Object> payloadMap = new HashMap<>();
		payloadMap.put("queryParam", "someValue");
		payloadMap.put("modelName", "someValue");
		Result expectedOpstatusResult = new Result();
		expectedOpstatusResult.addParam(new Param("opstatus", "0", FabricConstants.STRING));
		expectedOpstatusResult.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetAccountsReceivableResponse.json").getFile()).getPath();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
		JSONObject expectedJson = new JSONObject(getCompanyLEsResponseContent);
		Result expectedResult = new Result();
		expectedResult = Utilities.constructResultFromJSONObject(expectedJson);
		Dataset value = expectedResult.getDatasetById("value");
		when(businessDelegate.getAccountsReceivable(payloadMap)).thenReturn(value);
		Result resultFromMethod = smartBankingAdvisoryResource.getAccountsReceivable(methodID, inputArray, dcRequest,
				dcResponse);
		assertEquals("opstatus should be '0'", expectedOpstatusResult.getParamByName("opstatus").getValue(),
				resultFromMethod.getParamByName("opstatus").getValue());

		assertEquals("httpStatusCode should be '200'",
				expectedOpstatusResult.getParamByName("httpStatusCode").getValue(),
				resultFromMethod.getParamByName("httpStatusCode").getValue());
	}

	@Test
	public void testErrorScenario() throws Exception {
		Map<String, Object> payloadMap = new HashMap<>();
		doThrow(new NullPointerException("Simulated NullPointerException")).when(businessDelegate)
				.getAccountsReceivable(payloadMap);
		Result resultFromMethod = smartBankingAdvisoryResource.getAccountsReceivable(methodID, inputArray, dcRequest,
				dcResponse);
		Param dbpErrMsgParam = resultFromMethod.getParamByName("dbpErrMsg");
		assertNotNull("dbpErrMsg parameter should exist", dbpErrMsgParam);
		assertEquals("Error from BackendResponse", dbpErrMsgParam.getValue());
	}
}

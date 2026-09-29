package com.infinity.dbx.temenos.accounts;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ CommonUtils.class, TokenUtils.class, DataControllerRequest.class, DataControllerResponse.class,
		Authentication.class })
public class AccountDetailsForCombinedStatementsOperationTest implements AccountsConstants, TemenosConstants {

	private static DataControllerRequest request;
	private static DataControllerResponse response;
	private static MockedStatic<CommonUtils> mockedCommonUtils;
	private static MockedStatic<TokenUtils> mockedTokenUtils;
	private Object[] inputArray;
	JavaService2 javaService2;

	@BeforeClass
	public static void init() {
		mockedCommonUtils = Mockito.mockStatic(CommonUtils.class);
		mockedTokenUtils = Mockito.mockStatic(TokenUtils.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedCommonUtils.close();
		mockedTokenUtils.close();
	}

	@Before
	public void setup() throws Exception {
		inputArray = new Object[10];
		request = Mockito.mock(DataControllerRequest.class);
		response = Mockito.mock(DataControllerResponse.class);
		javaService2 = new AccountDetailsForCombinedStatementsOperation();
	}

	@Test
	public void testInvoke() throws Exception {
		String getResponsePath = Paths.get(
				getClass().getClassLoader().getResource("AccountDetailsForCombinedStatementsResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		Result accountResult = ConvertJsonToResult.convert(getContent);

		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		headerParams.put(TemenosConstants.PARAM_AUTHORIZATION, "authToken");
		inputParams.put(DB_ACCOUNTID, "125725");
		Mockito.mock(Authentication.class);
		mockedCommonUtils.when(() -> CommonUtils.getParamValue(params, ACCOUNTID)).thenReturn("125725");
		mockedTokenUtils.when(() -> TokenUtils.getT24AuthToken(request)).thenReturn("authToken");
		mockedCommonUtils.when(() -> CommonUtils.callIntegrationService(request, inputParams, headerParams,
				TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_ACCOUNT_DETAILS_FOR_COMBINED_STATEMENT,
				false)).thenReturn(accountResult);
		Result actualResult = (Result) javaService2.invoke("methodID", inputArray, request, response);
		assertEquals(4, actualResult.getAllParams().size());
	}

}

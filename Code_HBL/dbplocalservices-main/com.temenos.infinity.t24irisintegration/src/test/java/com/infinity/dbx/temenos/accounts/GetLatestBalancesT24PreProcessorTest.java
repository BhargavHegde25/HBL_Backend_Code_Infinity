package com.infinity.dbx.temenos.accounts;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ LegalEntityUtil.class, TokenUtils.class, DataControllerRequest.class, DataControllerResponse.class,
		Result.class })
public class GetLatestBalancesT24PreProcessorTest {

	private static DataControllerRequest request;
	private static DataControllerResponse dcResponse;
	private static MockedStatic<LegalEntityUtil> mockedLegalEntityUtil;
	private static MockedStatic<TokenUtils> mockedTokenUtils;

	@BeforeClass
	public static void setup() {
		mockedLegalEntityUtil = Mockito.mockStatic(LegalEntityUtil.class);
		mockedTokenUtils = Mockito.mockStatic(TokenUtils.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedLegalEntityUtil.close();
		mockedTokenUtils.close();
	}

	@Test
	public void testExecute() throws Exception {
		request = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		when((String) request.getHeader("companyId")).thenReturn(null);
		when(request.getHeaderMap()).thenReturn(anyMap());
		mockedLegalEntityUtil.when(() -> LegalEntityUtil.getCurrentLegalEntityIdFromCache(request))
				.thenReturn("GB0010001");
		mockedTokenUtils.when(() -> TokenUtils.getT24AuthToken(request)).thenReturn("authToken");
		boolean response = new GetLatestBalancesT24PreProcessor().execute(new HashMap(), request, dcResponse,
				mock(Result.class));
		assertEquals(true, response);
	}

}
package com.infinity.dbx.temenos.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ DataControllerRequest.class, DataControllerResponse.class, LegalEntityUtil.class, TokenUtils.class,
		TemenosUtils.class, IdentityHandler.class, ServicesManager.class })
public class GetUserPreProcessorTest implements TemenosConstants {

	private static DataControllerRequest request;
	private static DataControllerResponse response;
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
		request = Mockito.mock(DataControllerRequest.class);
		response = Mockito.mock(DataControllerResponse.class);

		Mockito.mock(TemenosUtils.class);
		mockedLegalEntityUtil.when(() -> LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request))
				.thenReturn("GB0010001");
		mockedTokenUtils.when(() -> TokenUtils.getT24AuthToken(request)).thenReturn("authToken");
		when(request.getParameter("isLoggedInUser")).thenReturn("false");
		when(request.getParameter("current_appID")).thenReturn("ServiceName");
		IdentityHandler identityHandler = Mockito.mock(IdentityHandler.class);
		ServicesManager servicesManager = Mockito.mock(ServicesManager.class);
		when(request.getServicesManager()).thenReturn(servicesManager);
		when(servicesManager.getIdentityHandler()).thenReturn(identityHandler);
		when(identityHandler.getUserAttributes()).thenReturn(null);
		when(request.getParameter("Customer_id")).thenReturn("456346834");
		boolean result = new GetUserPreProcessor().execute(new HashMap(), request, response, mock(Result.class));
		assertEquals(true, result);
	}

}

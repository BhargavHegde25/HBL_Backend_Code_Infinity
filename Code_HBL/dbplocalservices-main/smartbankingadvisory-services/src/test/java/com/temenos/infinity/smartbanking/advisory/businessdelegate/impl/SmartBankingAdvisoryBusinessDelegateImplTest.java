package com.temenos.infinity.smartbanking.advisory.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.smartbanking.advisory.backenddelegate.api.SmartBankingAdvisoryBackendDelegate;
import com.temenos.infinity.smartbanking.advisory.businessdelegate.api.SmartBankingAdvisoryBusinessDelegate;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, SmartBankingAdvisoryBusinessDelegate.class,
		SmartBankingAdvisoryBackendDelegate.class, SmartBankingAdvisoryBusinessDelegateImpl.class })
public class SmartBankingAdvisoryBusinessDelegateImplTest {

	SmartBankingAdvisoryBusinessDelegate sbaBusinessDelegate;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	SmartBankingAdvisoryBackendDelegate SBABackendDelegate;

	@Before
	public void setup() throws IOException {
		SBABackendDelegate = mock(SmartBankingAdvisoryBackendDelegate.class);
		mockedStatic.when(
				(Verification) DBPAPIAbstractFactoryImpl.getBackendDelegate(SmartBankingAdvisoryBackendDelegate.class))
				.thenReturn(SBABackendDelegate);
		sbaBusinessDelegate = new SmartBankingAdvisoryBusinessDelegateImpl();
	}

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
	}

	@AfterClass
	public static void tearDown() {
		mockedStatic.close();
	}

	@Test
	public void testGetReceivablesAccount() throws Exception {
		Result backendResponse = new Result();
		String getCompanyLEsResponseFile = new File(
				getClass().getClassLoader().getResource("GetAccountsReceivableResponse.json").toURI()).toString();
		String getCompanyLEsResponseContent = new String(Files.readAllBytes(Paths.get(getCompanyLEsResponseFile)));
		backendResponse = Utilities.constructResultFromJSONObject(new JSONObject(getCompanyLEsResponseContent));

		when(SBABackendDelegate.getAccountsReceivable(anyMap())).thenReturn(backendResponse);
		Dataset result = sbaBusinessDelegate.getAccountsReceivable(anyMap());

		verify(SBABackendDelegate).getAccountsReceivable(anyMap());
		assertEquals(true, result.getAllRecords().get(0).getAllParams().size() > 0);
	}
}
package com.infinity.dbx.temenos.accounts;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.kony.dbputilities.util.ConvertJsonToResult;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetLatestBalancesT24PostProcessorTest {

	private static DataControllerRequest request;
	private static DataControllerResponse response;

	@Before
	public void setup() {
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
	}

	@Test
	public void testExecute() throws Exception {
		String getResponsePath = Paths
				.get(getClass().getClassLoader().getResource("GetLatestBalancesT24PostProcessorResponse.json").toURI())
				.toString();
		String getContent = new String(Files.readAllBytes(Paths.get(getResponsePath)));
		Result responseOutput = ConvertJsonToResult.convert(getContent);
		when(response.getResponse()).thenReturn(getContent);
		Result result = new GetLatestBalancesT24PostProcessor().execute(responseOutput, request, response);
		assertEquals(1, result.getAllDatasets().size());
		assertEquals("Accounts", result.getAllDatasets().get(0).getId());
		assertEquals(1, result.getDatasetById("Accounts").getAllRecords().size());
	}

	@Test
	public void testExecuteException() throws Exception {
		when(response.getResponse()).thenReturn("value");
		Result result = mock(Result.class);
		when(result.getDatasetById("Accounts")).thenReturn(null);
		Result actualResult = new GetLatestBalancesT24PostProcessor().execute(result, request, response);
		assertEquals(1, actualResult.getAllDatasets().size());
		assertEquals("Accounts", actualResult.getAllDatasets().get(0).getId());
		assertEquals(0, actualResult.getDatasetById("Accounts").getAllRecords().size());
	}

}

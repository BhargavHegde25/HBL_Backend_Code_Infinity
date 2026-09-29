package com.infinity.dbx.temenos.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

@PrepareForTest({ DataControllerRequest.class, DataControllerResponse.class })
public class GetUserJavaPreProcessorTest {

	private static DataControllerRequest request;
	private static DataControllerResponse response;

	@Test
	public void testExecute() throws Exception {
		request = Mockito.mock(DataControllerRequest.class);
		response = Mockito.mock(DataControllerResponse.class);
		when(request.getParameter("partyID")).thenReturn(null);
		when(request.getParameter("loginUserId")).thenReturn("4536557897");
		boolean result = new GetUserJavaPreProcessor().execute(new HashMap(), request, response, mock(Result.class));
		assertEquals(true, result);
	}

}

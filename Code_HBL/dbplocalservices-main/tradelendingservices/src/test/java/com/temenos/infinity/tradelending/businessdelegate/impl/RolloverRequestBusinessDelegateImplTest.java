/**
 * 
 */
package com.temenos.infinity.tradelending.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
import org.mockito.Mockito;
import org.mockito.MockedStatic.Verification;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.RolloverRequestBackendDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.RolloverRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;

/**
 * @author mrunalini.adepu
 *
 */
public class RolloverRequestBusinessDelegateImplTest {

	RolloverRequestBusinessDelegate rolloverRequestBusinessDelegate;
	DataControllerRequest request;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactroy;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	RolloverRequestDTO inputDto;
	static RolloverRequestBackendDelegate rolloverRequestBackendDelegate;
	

	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		try {
			rolloverRequestBackendDelegate = mock(RolloverRequestBackendDelegate.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getBackendDelegate(RolloverRequestBackendDelegate.class))
		.thenReturn(rolloverRequestBackendDelegate);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}

	@Before
	public void executedBeforeEach() throws IOException {
		rolloverRequestBusinessDelegate = new RolloverRequestBusinessDelegateImpl();
		request = mock(DataControllerRequest.class);
		inputDto = mock(RolloverRequestDTO.class);
	    dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
	    businessDelegateFactroy = mock(BusinessDelegateFactory.class);   
	}
	
	@Test
	public void createRolloverRequestTest() throws DBPApplicationException, Exception {	
		
		String rolloverRequestResponseMetaDataAndPIPath = new File(
				getClass().getClassLoader().getResource("RolloverRequestPayload.json").getFile()).getPath();

		String rolloverRequestResponseMetaDataAndPIContent = new String(Files.readAllBytes(Paths.get(rolloverRequestResponseMetaDataAndPIPath)));
		JSONObject rolloverRequestResponseobj = new JSONObject(rolloverRequestResponseMetaDataAndPIContent);
		RolloverRequestDTO rolloverRequestDto = JSONUtils.parse(rolloverRequestResponseobj.toString(), RolloverRequestDTO.class);

		when(rolloverRequestBackendDelegate.createRolloverRequest(rolloverRequestDto, request))
		.thenReturn(rolloverRequestDto);
		
		rolloverRequestDto = rolloverRequestBackendDelegate
				.createRolloverRequest(rolloverRequestDto, request);
		
		assertEquals("10000003", rolloverRequestDto.getRolloverRequestId());
	}
}

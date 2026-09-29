package com.temenos.infinity.tradesupplyfinance.javaservices;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.mockito.Mockito;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorInvoiceDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.AnchorInvoiceResource;

public class ApproveAnchorInvoiceOperationTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	AnchorInvoiceResource anchorInvoiceResource;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	AnchorInvoiceDTO anchorInvoiceDTO;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	private static MockedStatic<JSONUtils> JSONUtilsMockedStatic;
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
		JSONUtilsMockedStatic = Mockito.mockStatic(JSONUtils.class);
	}
	

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
		JSONUtilsMockedStatic.close();
	}

	@Before
	public void executedBefore() throws IOException {

		javaService2 = new ApproveAnchorInvoiceOperation();
		methodID = "METHODID";
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		anchorInvoiceResource = mock(AnchorInvoiceResource.class);
		actualResult = new Result();
		resourceFactory = mock(ResourceFactory.class);
		anchorInvoiceDTO = mock(AnchorInvoiceDTO.class);
	}

	@Test
	public void testInvoke() throws Exception {

		Result result = mock(Result.class);
		
		log4j2ConfiguratorMockedStatic.when((Verification) Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
		
		JSONUtilsMockedStatic.when((Verification)JSONUtils.parse(new JSONObject().toString(), AnchorInvoiceDTO.class)).thenReturn(anchorInvoiceDTO);
		mockedStatic.when((Verification) DBPAPIAbstractFactoryImpl.getResource(AnchorInvoiceResource.class))
				.thenReturn(anchorInvoiceResource);
		
		when(anchorInvoiceResource.approveAnchorInvoice(anchorInvoiceDTO,request)).thenReturn(result);
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(0, actualResult.getAllParams().size());
	}
}

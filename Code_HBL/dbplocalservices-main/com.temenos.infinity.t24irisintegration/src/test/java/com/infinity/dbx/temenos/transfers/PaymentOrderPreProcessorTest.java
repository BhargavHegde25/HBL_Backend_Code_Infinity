package com.infinity.dbx.temenos.transfers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.resource.api.GeneralTransactionsResource;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;

public class PaymentOrderPreProcessorTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	DataControllerRequest request;
	DataControllerResponse response;
	GeneralTransactionsResource resource;
	Boolean actualBooleanResult;
	Result result = new Result();
	HashMap<String, String> inputParams = new HashMap<>();
	TemenosBasePreProcessor PaymentOrderPreProcessorTestStreet;
	
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<TokenUtils> mockedStaticTokenUtils;
	private static MockedStatic<TemenosBasePreProcessor> mockedStaticTemenosBasePreProcessor;
	private static MockedStatic<EnvironmentConfigurationsHandler> mockedStaticEnvironmentConfigurationsHandler;
	
	//This will run once for the class before all the test methods
	@BeforeClass 
	public static void setUpBeforeClass() throws Exception {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticTokenUtils = Mockito.mockStatic(TokenUtils.class);
		mockedStaticTemenosBasePreProcessor = Mockito.mockStatic(TemenosBasePreProcessor.class);
		mockedStaticEnvironmentConfigurationsHandler = Mockito.mockStatic(EnvironmentConfigurationsHandler.class);
	}

	@AfterClass
	public static void cleanup() throws Exception {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticTemenosBasePreProcessor.close();
		mockedStaticTokenUtils.close();
		mockedStaticEnvironmentConfigurationsHandler.close();
	}

	@Before //This will run before each test case
	public void setUp() throws Exception {
		
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		resource = mock(GeneralTransactionsResource.class);
		
		inputParams.put(Constants.PARAM_TRANSACTION_TYPE, Constants.TRANSCTION_TYPE_EXTERNAL_TRANSFER);
		inputParams.put(Constants.PARAM_TRANSACTION_NOTES, "Notes");
		inputParams.put(TransferConstants.PARAM_STREET_NAME, "street name, second street");
		inputParams.put(TransferConstants.PARAM_BENEFICIARY_ADDR_LINE1, "address line one");
		inputParams.put(TransferConstants.PARAM_BENEFICIARY_ADDR_LINE2, "address line two");
		inputParams.put(TransferConstants.PARAM_BENEFICIARY_CITY, "bene address city");
		inputParams.put(TransferConstants.PARAM_BENEFICIARY_ZIP_CODE, "654321");
		inputParams.put(TransferConstants.PARAM_BENEFICIARY_COUNTRY, "OD");        
		
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		resourceFactory = mock(ResourceFactory.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(ResourceFactory.class)).thenReturn(resourceFactory);
		when(resourceFactory.getResource(GeneralTransactionsResource.class)).thenReturn(resource);
		mockedStaticTokenUtils.when(() -> TokenUtils.getT24AuthToken((DataControllerRequest) any())).thenReturn(anyString());
		PaymentOrderPreProcessorTestStreet = new PaymentOrderPreProcessor();
		
		result.addParam(new Param("opstatus", "0", FabricConstants.INT));
		result.addParam(new Param("httpStatusCode", "200", FabricConstants.STRING));
		when(request.getParameter("current_appID")).thenReturn("T24ISPAYMENTORDERS");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvoke() throws Exception {
		actualBooleanResult = PaymentOrderPreProcessorTestStreet.execute(inputParams, request, response, result);
		String actualStreetName = CommonUtils.getParamValue(inputParams, TransferConstants.PARAM_STREET_NAME);
		String actualBeneAddr = CommonUtils.getParamValue(inputParams, TransferConstants.PARAM_BENEFICIARY_ADDR_LINE1);
		assertEquals(actualBooleanResult, true);
		assertEquals(actualStreetName,"[{\"swiftAddress\":\"street name, second street\"}]");
		assertEquals(actualBeneAddr,"[{\"beneficiaryAddress\":\"address line one\"},{\"beneficiaryAddress\":\"address line two\"},{\"beneficiaryAddress\":\"bene address city,654321\"},{\"beneficiaryAddress\":\"OD\"}]");
	}

}

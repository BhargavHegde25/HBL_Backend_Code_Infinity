/**
 * 
 */
package com.temenos.infinity.api.arrangements.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.javaservice.CreateCustomerTNCForPayOffSimulation;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;


/**
 * @author amitabh.kotha
 *
 */
@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, DBPAPIAbstractFactory.class, Log4j2Configurator.class})
public class CreateCustomerTNCForPayOffSimulationTest {
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	Log4j2Configurator log4j2Configurator;
	ResourceFactory resourceFactory;
	JavaService2 javaService2;
	DataControllerRequest request;
	DataControllerResponse response;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	AdminUtil adminUtil;
	ServiceCallHelper serviceCallHelper;
	HelperMethods helperMethods;
	
	ArrangementsResource arrangementsResource;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStatic;
	private static MockedStatic<Log4j2Configurator> log4j2ConfiguratorMockedStatic;
	private static MockedStatic<AdminUtil> mockedStaticadminUtil;
	private static MockedStatic<ServiceCallHelper> mockedStaticserviceCallHelper;
	private static MockedStatic<HelperMethods> mockedhelperMethods;
	
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		log4j2ConfiguratorMockedStatic = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticadminUtil = Mockito.mockStatic(AdminUtil.class);
		mockedStaticserviceCallHelper = Mockito.mockStatic(ServiceCallHelper.class);
		mockedhelperMethods = Mockito.mockStatic(HelperMethods.class);
	}
	

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
		log4j2ConfiguratorMockedStatic.close();
		mockedStaticserviceCallHelper.close();
		mockedStaticadminUtil.close();
		mockedhelperMethods.close();
	}
	
	@Before
	public void executedBefore() throws IOException {
		javaService2 = new CreateCustomerTNCForPayOffSimulation();
		methodID = "METHODID";
		inputArray = new Object[10];
		request = mock(DataControllerRequest.class);
		response = mock(DataControllerResponse.class);
		log4j2Configurator = mock(Log4j2Configurator.class);	
		adminUtil = mock(AdminUtil.class);
		serviceCallHelper = mock(ServiceCallHelper.class);
		helperMethods = mock(HelperMethods.class);
	}

	@Test
	public void testInvoke() throws Exception {
		
		Map<String, String> outputArray = new HashMap<String, String>();
		String DataPath = new File(
				getClass().getClassLoader().getResource("GetTermsandConditions.json").getFile()).getPath();
		String DataContent = new String(Files.readAllBytes(Paths.get(DataPath)));
		JSONObject Data = new JSONObject(DataContent);		
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("CreateCustomerTNCForPayOffSimulationResponse.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		JSONObject appData = new JSONObject(appDataContent);		
		when(Log4j2Configurator.getInstance()).thenReturn(log4j2Configurator);
		when(HelperMethods.getCustomerIdFromSession(request)).thenReturn("12345");
		when(HelperMethods.getInputParamMap(inputArray)).thenReturn(outputArray);
		when(HelperMethods.getAppId(request)).thenReturn("1234");
		Map<String, String> inputParams = new HashMap<String, String>();
	//	Result result1=JSONToResult.convert(DataContent);	
		inputParams.put("termsAndConditionsCode", "EarlyPayOffSimulation_TnC");
        inputParams.put("languageCode", "en-US");
        inputParams.put("appId", "1234");
		when(AdminUtil.invokeAPI(inputParams, URLConstants.GET_TERMS_AND_CONDITIONS, request)).thenReturn(JSONToResult.convert(DataContent));
		//when(result.getParamValueByName("contentId")).thenReturn("AR12345");
		Map<String, Object> params = new HashMap<>();
		params.put("masterConsentId", DBPUtilitiesConstants.CONSENT_MASTER_ID);
        params.put("bankName", "Infinity Bank");
        params.put("externalUserId", "12345");
        params.put("retentionPeriod", "30");
        params.put("status", "ACTIVE");
        params.put("backendIdentifier", "12345");
        params.put("channelId", "Online");
        params.put("termsNConditionContentId","PCO233046JM8I");
        params.put("consentGiven", "true");
        params.put("partyId", "12345");
		when(ServiceCallHelper.invokeServiceAndGetResult(request, params,
                HelperMethods.getHeaders(request), URLConstants.CREATE_PARTY_CONSENT_DETAILS)).thenReturn(JSONToResult.convert(appDataContent));
		actualResult = (Result) javaService2.invoke(methodID, inputArray, request, response);
		assertEquals(appData.getString("partyConsentId"),actualResult.getParamByName("partyConsentId").getValue());
	
	}
}

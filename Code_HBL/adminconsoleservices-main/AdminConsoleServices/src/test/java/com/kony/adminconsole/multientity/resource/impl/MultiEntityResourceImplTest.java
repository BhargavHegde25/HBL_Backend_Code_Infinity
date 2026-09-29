package com.kony.adminconsole.multientity.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.multientity.businessdelegate.api.MultiEntityBusinessDelegate;
import com.kony.adminconsole.multientity.javaservice.FetchMultiEntityOperation;
import com.kony.adminconsole.multientity.resource.impl.MultiEntityResourceImpl;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;
import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class,
		ApplicationParametersHandler.class })
public class MultiEntityResourceImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	MultiEntityResourceImpl multiEntityResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	MultiEntityBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Executor executor;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	Session session;
	ApplicationParametersHandler applicationParametersHandler;
	CommonUtilities commonUtilities;

	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedStaticCommonUtilities;
	private static MockedStatic<ApplicationParametersHandler> mockedStaticApplicationParametersHandler;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedStaticApplicationParametersHandler = Mockito.mockStatic(ApplicationParametersHandler.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticCommonUtilities.close();
		mockedStaticApplicationParametersHandler.close();
	}

	@Before
	public void executedBefore() throws Exception {

		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		
		session = mock(Session.class);
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);

		businessDelegate = mock(MultiEntityBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		applicationParametersHandler = mock(ApplicationParametersHandler.class);
		log4jConfigurator = mock(Log4j2Configurator.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(MultiEntityBusinessDelegate.class))
				.thenReturn(businessDelegate);
		
		when(dcRequest.getSession()).thenReturn(session);
		when(session.getId()).thenReturn("1");

	}

	@Test
	public void testGetAllCompanyLegalUnits() throws Exception {

		multiEntityResourceImpl = new MultiEntityResourceImpl();
		String  authToken = "authTokenDummy";
		String singleentity = "false";
		Object sessionId = "1";

		Result expectedResult = new Result();

		when(ApplicationParametersHandler.fetchIsSingleEntity(dcRequest)).thenReturn(singleentity);
		when(CommonUtilities.getAuthToken(dcRequest)).thenReturn(authToken);
		when(businessDelegate.getAllCompanyLegalUnits(authToken, false , sessionId)).thenReturn(expectedResult);

		actualResult = multiEntityResourceImpl.getAllCompanyLegalUnits(dcRequest);

		assertEquals(actualResult.getParamByName("dbpErrCode"), expectedResult.getParamByName("dbpErrCode"));
		assertEquals(actualResult.getParamByName("dbpErrMsg"), expectedResult.getParamByName("dbpErrMsg"));
	}
	
	@Test
	public void testGetAllCompanyLegalUnitsException() throws Exception {
		
		String  authToken = "authTokenDummy";
		String singleentity = "false";
		Object sessionId = "1";
		
		multiEntityResourceImpl = new MultiEntityResourceImpl();

		Result expectedResult = new Result();
		expectedResult.addParam(new Param(ACConstants.DBP_ERROR_CODE, ErrorCodeEnum.ERR_22228.getErrorCodeAsString()));
		expectedResult.addParam(new Param(ACConstants.DBP_ERROR_MESSAGE, ErrorCodeEnum.ERR_22228.getMessage()));

		when(ApplicationParametersHandler.fetchIsSingleEntity(dcRequest)).thenReturn(singleentity);
		when(CommonUtilities.getAuthToken(dcRequest)).thenReturn(authToken);
		when(businessDelegate.getAllCompanyLegalUnits(authToken, false , sessionId)).thenThrow(new RuntimeException());

		actualResult = multiEntityResourceImpl.getAllCompanyLegalUnits(dcRequest);

		assertEquals(actualResult.getParamByName("dbpErrCode").getValue(), expectedResult.getParamByName("dbpErrCode").getValue());
		assertEquals(actualResult.getParamByName("dbpErrMsg").getValue(), expectedResult.getParamByName("dbpErrMsg").getValue());
	}
}

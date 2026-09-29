package com.kony.adminconsole.service.campaign.resource.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.dto.campaign.DataContext;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.campaign.resource.impl.UpdateUsersForSegmentsResourceImpl;
import com.kony.adminconsole.campaign.businessdelegate.api.UpdateUsersForSegmentsBusinessDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;


@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class,
		ApplicationParametersHandler.class })

public class UpdateUsersForSegmentsResourceImplTest {


	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactory;
	UpdateUsersForSegmentsResourceImpl updateUsersForSegmentsResourceImpl;
	DBPAPIAbstractFactoryImpl dbpAPIAbstractFactoryImpl;
	UpdateUsersForSegmentsBusinessDelegate businessDelegate;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Executor executor;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
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
		
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		businessDelegate = mock(UpdateUsersForSegmentsBusinessDelegate.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		dbpAPIAbstractFactoryImpl = mock(DBPAPIAbstractFactoryImpl.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		applicationParametersHandler = mock(ApplicationParametersHandler.class);
		log4jConfigurator = mock(Log4j2Configurator.class);

		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class))
				.thenReturn(businessDelegateFactory);
		when(businessDelegateFactory.getBusinessDelegate(UpdateUsersForSegmentsBusinessDelegate.class))
				.thenReturn(businessDelegate);
	}

	@Test
	public void testGetAndupdateActiveUsersForSegment() throws Exception {

		updateUsersForSegmentsResourceImpl = new UpdateUsersForSegmentsResourceImpl();
		Result expectedResult = new Result();
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("segment", "{\"profileId\":\"PRF2403648034\",\"profileConditions\":[{\"profileConditionId\":\"PC2403648035\",\"dataContextDescription\":\"DataContext created for Customer Attrition Model flow Profiles\",\"conditionExpression\":\"EstAnnualIncome%20le%20%271200000%27\",\"dataContextName\":\"Customer Attrition Model flow\",\"dataContextId\":\"DC002\",\"dataContextEndPoints\":\"\"}]}");
		inputArray[1] = inputParams;
		JsonObject segmentJsonObject =
                new JsonParser().parse(inputParams.get(ACConstants.SEGMENT)).getAsJsonObject();
        Segment segment = new Segment(segmentJsonObject.get(ACConstants.PROFILE_ID).getAsString());
        JsonArray profileArr = segmentJsonObject.get(ACConstants.PROFILE_CONDITIONSS).getAsJsonArray();
        List<DataContext> dcList = new ArrayList<>();
        for (JsonElement profileConditionJE : profileArr) {
			JsonObject profCond = profileConditionJE.getAsJsonObject();
			String dataContextId = profCond.get(ACConstants.DATA_CONTEXT_ID).getAsString();
			String profileConditionId = profCond.get(ACConstants.PROFILE_CONDITION_ID).getAsString();
			String conditionExpression = profCond.get(ACConstants.CONDITION_EXPRESSION).getAsString();
			String endPointURL = profCond.get(ACConstants.DATA_CONTEXT_END_POINTS).getAsString();
			DataContext dc = new DataContext(profileConditionId, dataContextId, endPointURL,
					conditionExpression);
			dcList.add(dc);
		}
        if (!dcList.isEmpty()) {
            segment.setDcList(dcList);
        }
		when(businessDelegate.getAndupdateActiveUsersForSegment(segment, null)).thenReturn(true);
		actualResult = updateUsersForSegmentsResourceImpl.getAndupdateActiveUsersForSegment(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName("opstatus"), expectedResult.getParamValueByName("opstatus"));
		assertEquals(actualResult.getParamValueByName("httpstatus"), expectedResult.getParamValueByName("httpstatus"));
	}
	
	@Test
	public void testGetAndupdateActiveUsersForSegmentJsonSyntaxException() throws Exception {

		updateUsersForSegmentsResourceImpl = new UpdateUsersForSegmentsResourceImpl();
		Result expectedResult = new Result();
		Map<String, String> inputParams = new HashMap<>();
		inputParams.put("segment", "{\"profileId\":\"PRF2403648034\",\"profileConditions\":[{\"profileConditionId\":\"PC2403648035\",\"dataContextDescription\":\"DataContext created for Customer Attrition Model flow Profiles\",\"conditionExpression\":\"EstAnnualIncome%20le%20%271200000%27\",\"dataContextName\":\"Customer Attrition Model flow\",\"dataContextId\":\"DC002\",\"dataContextEndPoints\":\"\"}]}");
		JsonObject segmentJsonObject =
                new JsonParser().parse(inputParams.get(ACConstants.SEGMENT)).getAsJsonObject();
        Segment segment = new Segment(segmentJsonObject.get(ACConstants.PROFILE_ID).getAsString());
        JsonArray profileArr = segmentJsonObject.get(ACConstants.PROFILE_CONDITIONSS).getAsJsonArray();
        List<DataContext> dcList = new ArrayList<>();
        for (JsonElement profileConditionJE : profileArr) {
			JsonObject profCond = profileConditionJE.getAsJsonObject();
			String dataContextId = profCond.get(ACConstants.DATA_CONTEXT_ID).getAsString();
			String profileConditionId = profCond.get(ACConstants.PROFILE_CONDITION_ID).getAsString();
			String conditionExpression = profCond.get(ACConstants.CONDITION_EXPRESSION).getAsString();
			String endPointURL = profCond.get(ACConstants.DATA_CONTEXT_END_POINTS).getAsString();
			DataContext dc = new DataContext(profileConditionId, dataContextId, endPointURL,
					conditionExpression);
			dcList.add(dc);
		}
        if (!dcList.isEmpty()) {
            segment.setDcList(dcList);
        }
		inputParams.clear();
		inputParams.put("segment", "{\"profileId\":\"PRF2403648034\",\"profileConditions\":[\"profileConditionId\":\"PC2403648035\",\"dataContextDescription\":\"DataContext created for Customer Attrition Model flow Profiles\",\"conditionExpression\":\"EstAnnualIncome%20le%20%271200000%27\",\"dataContextName\":\"Customer Attrition Model flow\",\"dataContextId\":\"DC002\",\"dataContextEndPoints\":\"\"}]}");
        inputArray[1] = inputParams;
		when(businessDelegate.getAndupdateActiveUsersForSegment(segment, null)).thenReturn(true);
		actualResult = updateUsersForSegmentsResourceImpl.getAndupdateActiveUsersForSegment(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName("opstatus"), expectedResult.getParamValueByName("opstatus"));
		assertEquals(actualResult.getParamValueByName("httpstatus"), expectedResult.getParamValueByName("httpstatus"));
	}
	
	@Test
	public void testGetAndupdateActiveUsersForSegmentException() throws Exception {

		updateUsersForSegmentsResourceImpl = new UpdateUsersForSegmentsResourceImpl();
		Result expectedResult = new Result();
		ErrorCodeEnum.ERR_21803.setErrorCode(expectedResult);
		Map<String, String> inputParams = new HashMap<>();
        inputArray[1] = inputParams;
        Segment segment = new Segment("");
		when(businessDelegate.getAndupdateActiveUsersForSegment(segment, null)).thenReturn(true);
		actualResult = updateUsersForSegmentsResourceImpl.getAndupdateActiveUsersForSegment(methodID, inputArray, dcRequest, dcResponse);
		assertEquals(actualResult.getParamValueByName("dbpErrCode"), expectedResult.getParamValueByName("dbpErrCode"));
		assertEquals(actualResult.getParamValueByName("dbpErrMsg"), expectedResult.getParamValueByName("dbpErrMsg"));
	}
}

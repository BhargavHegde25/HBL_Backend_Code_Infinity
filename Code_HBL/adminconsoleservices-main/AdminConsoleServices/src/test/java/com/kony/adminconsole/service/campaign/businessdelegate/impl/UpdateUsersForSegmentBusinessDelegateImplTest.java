package com.kony.adminconsole.service.campaign.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.campaign.SingleSegmentUpdateStratergy;
import com.kony.adminconsole.campaign.businessdelegate.impl.UpdateUsersForSegmentBusinessDelegateImpl;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.Executor;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.adminconsole.dto.campaign.Segment;
import com.kony.adminconsole.dto.campaign.DataContext;

import com.temenos.logger.Logger;

@PrepareForTest({ DBPAPIAbstractFactoryImpl.class, Log4j2Configurator.class, CommonUtilities.class, })

public class UpdateUsersForSegmentBusinessDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	UpdateUsersForSegmentBusinessDelegateImpl updateUsersForSegmentBusinessDelegateImpl;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	String methodID;
	Object[] inputArray;
	Result actualResult;
	Executor executor;
	Logger logger;
	Log4j2Configurator log4jConfigurator;
	CommonUtilities commonUtilities;
	CampaignUtil campaignUtil;
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	private static MockedStatic<CommonUtilities> mockedStaticCommonUtilities;
	private static MockedStatic<CampaignUtil> mockedStaticCampaignUtil;

	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
		mockedStaticCommonUtilities = Mockito.mockStatic(CommonUtilities.class);
		mockedStaticCampaignUtil = Mockito.mockStatic(CampaignUtil.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
		mockedStaticCommonUtilities.close();
		mockedStaticCampaignUtil.close();
	}

	@Before
	public void executedBefore() throws Exception {
		methodID = "METHODID";
		inputArray = new Object[2];
		actualResult = new Result();
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);
		campaignUtil = mock(CampaignUtil.class);
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);

		updateUsersForSegmentBusinessDelegateImpl = new UpdateUsersForSegmentBusinessDelegateImpl();

	}

	@Test
	public void testGetAndUpdateActiveUsersForSegment() throws Exception {
		boolean expectedResult = true;
		Segment segment = new Segment("Segment");
		segment.setDcList(new ArrayList<DataContext>());
		segment.getDcList().add(new DataContext("DC001"));
		segment.getDcList().add(new DataContext("DC002"));
		segment.getDcList().add(new DataContext("DC003"));
		boolean actualResult = true;
		List<Segment> segmentList = new ArrayList<>();
		segmentList.add(segment);
		try {
		actualResult = updateUsersForSegmentBusinessDelegateImpl.getAndupdateActiveUsersForSegment(segment,
				null);
		} catch(Exception e) {}
		assertEquals(expectedResult, actualResult);
	}
}

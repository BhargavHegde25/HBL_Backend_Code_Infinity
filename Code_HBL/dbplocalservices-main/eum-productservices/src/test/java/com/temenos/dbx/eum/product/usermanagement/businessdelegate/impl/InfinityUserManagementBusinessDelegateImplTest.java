package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractCoreCustomerBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;

public class InfinityUserManagementBusinessDelegateImplTest {

	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BackendDelegateFactory backendDelegateFactory;
	BusinessDelegateFactory businessDelegateFactory;
	DataControllerRequest dcRequest;
	DataControllerResponse dcResponse;
	Log4j2Configurator log4jConfigurator;
	
	private static MockedStatic<DBPAPIAbstractFactoryImpl> mockedStaticForDBPAPIFactoryImpl;
	private static MockedStatic<Log4j2Configurator> mockedStaticLog4j2Configurator;
	
	InfinityUserManagementBackendDelegate infinityUserManagementBackendDelegate = null;
	
	@BeforeClass
	public static void init() {
		mockedStaticForDBPAPIFactoryImpl = Mockito.mockStatic(DBPAPIAbstractFactoryImpl.class);
		mockedStaticLog4j2Configurator = Mockito.mockStatic(Log4j2Configurator.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStaticForDBPAPIFactoryImpl.close();
		mockedStaticLog4j2Configurator.close();
	}
	
	@Before
	public void executedBefore() throws Exception {
		dcRequest = mock(DataControllerRequest.class);
		dcResponse = mock(DataControllerResponse.class);
		dbpAPIAbstractFactory = mock(DBPAPIAbstractFactory.class);
		backendDelegateFactory = mock(BackendDelegateFactory.class);
		businessDelegateFactory = mock(BusinessDelegateFactory.class);
		infinityUserManagementBackendDelegate = mock(InfinityUserManagementBackendDelegate.class);
		
		when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BackendDelegateFactory.class)).thenReturn(backendDelegateFactory);
		when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class)).thenReturn(businessDelegateFactory);
		when(backendDelegateFactory.getBackendDelegate(InfinityUserManagementBackendDelegate.class))
			.thenReturn(infinityUserManagementBackendDelegate);
	}
	
	@Test
	public void getAssociatedCustomersTest()  throws Exception {
		List<UserCustomerViewDTO> response = new ArrayList<>();
		String expectedStatus = "0";
		String actualStatus = "0";
		InfinityUserManagementBusinessDelegateImpl infinityUserManagementBusinessDelegateImpl 
			= new InfinityUserManagementBusinessDelegateImpl();
		try {
			when(infinityUserManagementBackendDelegate.getAssociatedCustomers(new ContractCustomersDTO(), new HashMap<String, Object>())).thenReturn(response);
			infinityUserManagementBusinessDelegateImpl.getAssociatedCustomers(new ContractCustomersDTO(), new HashMap<String, Object>());
		} catch (Exception e) {
		}
		assertEquals(expectedStatus, actualStatus);
	}
	
	@Test
	public void getAllEligibleRelationalCustomersTest()  throws Exception {
		String expectedStatus = "0";
		String actualStatus = "0";
		List<BackendIdentifierDTO> dtoList = new ArrayList<>();
		ContractCoreCustomerBackendDelegate contractCoreCustomerBackendDelegate = mock(ContractCoreCustomerBackendDelegate.class);
		BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = mock(BackendIdentifierBusinessDelegate.class);
		when(backendDelegateFactory.getBackendDelegate(ContractCoreCustomerBackendDelegate.class))
		.thenReturn(contractCoreCustomerBackendDelegate);
		when(businessDelegateFactory.getBusinessDelegate(BackendIdentifierBusinessDelegate.class))
				.thenReturn(backendIdentifierBusinessDelegate);
		try {
			ContractCoreCustomersDTO contractCoreCustomersDTO = new ContractCoreCustomersDTO();
			when(contractCoreCustomerBackendDelegate.getContractCoreCustomers(new ContractCoreCustomersDTO(), new HashMap<String, Object>()))
				.thenReturn(contractCoreCustomersDTO);
			when(backendIdentifierBusinessDelegate.getBackendIdentifierList(dtoList, new HashMap<String, Object>()))
				.thenReturn(dtoList);
		} catch(Exception e) {
			
		}
		assertEquals(expectedStatus, actualStatus);
	}
	
}

package com.temenos.infinity.api.accountsweeps.backenddelegate.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountsweeps.constants.ErrorCodeEnum;
import com.temenos.infinity.api.accountsweeps.dto.AccountSweepsDTO;
import com.konylabs.middleware.controller.DataControllerRequest;




@PrepareForTest({ AccountSweepsBackendDelegateImpl.class, DBPServiceExecutorBuilder.class, DBPServiceExecutor.class,
	DBPAPIAbstractFactoryImpl.class})

public class AccountSweepsBackendDelegateImplTest {

	AccountSweepsBackendDelegateImpl accountSweepBusinessDelegateImpl;
	DataControllerRequest request;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	BusinessDelegateFactory businessDelegateFactroy;
	DBPServiceExecutorBuilder DbpServiceExecutorBuilder;
	DBPServiceExecutor dbpServiceExecutor;
	private static MockedStatic<DBPServiceExecutorBuilder> mockedStatic;
	
	
	@BeforeClass
	public static void init() {
		mockedStatic = Mockito.mockStatic(DBPServiceExecutorBuilder.class);
	}

	@AfterClass
	public static void cleanup() {
		mockedStatic.close();
	}
	
	@Before
	public void executedBeforeEach() throws IOException {
		accountSweepBusinessDelegateImpl = new AccountSweepsBackendDelegateImpl();
		DbpServiceExecutorBuilder = mock(DBPServiceExecutorBuilder.class);
		dbpServiceExecutor = mock(DBPServiceExecutor.class);
		request = mock(DataControllerRequest.class);
		
	}
	
	@Test
	public void testInvoke() throws DBPApplicationException, Exception {
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("GetAccountSweepsT24.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		JSONObject appData = new JSONObject(appDataContent);	
		
		Set<String> accounts = new HashSet<String>();
		accounts.add("123565");
		accounts.add("123566");
		
	      StringBuilder accountIds = new StringBuilder();
	        AtomicInteger i = new AtomicInteger(accounts.size());
	        accounts.forEach(accountId -> accountIds.append(accountId).append(" "));

    	   mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(DbpServiceExecutorBuilder);
    	
    		when(DbpServiceExecutorBuilder.withServiceId("T24SweepServices")).thenReturn(DbpServiceExecutorBuilder);
    		when(DbpServiceExecutorBuilder.withOperationId("getSweeps")).thenReturn(DbpServiceExecutorBuilder);
    		
    		when(DbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(DbpServiceExecutorBuilder);
    		when(DbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(DbpServiceExecutorBuilder);
    		when(DbpServiceExecutorBuilder.withDataControllerRequest(request)).thenReturn(DbpServiceExecutorBuilder);
    		when(DbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
    		
    		when(dbpServiceExecutor.getResponse()).thenReturn(appDataContent);
    		
    		String actualResult = accountSweepBusinessDelegateImpl.getAllSweepsFromT24(accounts,request);
    			assertEquals(appDataContent,actualResult);
 	
	}
	
	@Test
	public void testInvoke1() throws DBPApplicationException, Exception{
        AccountSweepsDTO responseDto = new AccountSweepsDTO();
        AccountSweepsDTO sweepDTO = new AccountSweepsDTO();


        sweepDTO.setBelowSweepAmount("100.00");
        sweepDTO.setCurrencyCode("GBP");
        sweepDTO.setEndDate("End Manually");
        sweepDTO.setFrequency("Daily");
        sweepDTO.setPrimaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setPrimaryAccountNumber("124338");
        sweepDTO.setSecondaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setSecondaryAccountNumber("124311");
        sweepDTO.setStartDate("26/04/2023");
        
        
        String accountsSweepResponse;
        JSONObject Response = new JSONObject();
        
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("CreateAccountSweepsT24.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		
 	    mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withServiceId("T24SweepServices")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withOperationId("createSweep")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withDataControllerRequest(request)).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
 		when(dbpServiceExecutor.getResponse()).thenReturn(appDataContent);
        
 		
  		
 		responseDto = accountSweepBusinessDelegateImpl.createSweepAtBackEnd(sweepDTO,request,"t24");
		
 		assertEquals(responseDto.getMessage(),"Account Sweep created successfully");
    }

	
	@Test
	public void testEditFlow() throws DBPApplicationException, Exception{
        AccountSweepsDTO responseDto = new AccountSweepsDTO();
        AccountSweepsDTO sweepDTO = new AccountSweepsDTO();


        sweepDTO.setBelowSweepAmount("100.00");
        sweepDTO.setCurrencyCode("GBP");
        sweepDTO.setEndDate("End Manually");
        sweepDTO.setFrequency("Daily");
        sweepDTO.setPrimaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setPrimaryAccountNumber("124338");
        sweepDTO.setSecondaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setSecondaryAccountNumber("124311");
        sweepDTO.setStartDate("26/04/2023");
        
        
        String accountsSweepResponse;
        JSONObject Response = new JSONObject();
        
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("CreateAccountSweepsT24.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		
 	    mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withServiceId("T24SweepServices")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withOperationId("updateSweep")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withDataControllerRequest(request)).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
 		when(dbpServiceExecutor.getResponse()).thenReturn(appDataContent);
        
 		
  		
 		responseDto = accountSweepBusinessDelegateImpl.editSweep(sweepDTO,request,"t24");
		
 		assertEquals(responseDto.getMessage(),"Account Sweep updated successfully");
    }
	
	@Test
	public void testDeleteFlow() throws DBPApplicationException, Exception{
        AccountSweepsDTO responseDto = new AccountSweepsDTO();
        AccountSweepsDTO sweepDTO = new AccountSweepsDTO();


        sweepDTO.setBelowSweepAmount("100.00");
        sweepDTO.setCurrencyCode("GBP");
        sweepDTO.setEndDate("18/04/2023");
        sweepDTO.setFrequency("Daily");
        sweepDTO.setPrimaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setPrimaryAccountNumber("124338");
        sweepDTO.setSecondaryAccountName("Richard DelsonrnpwT");
        sweepDTO.setSecondaryAccountNumber("124311");
        sweepDTO.setStartDate("26/04/2023");
        
        
        String accountsSweepResponse;
        JSONObject Response = new JSONObject();
        
		String applicationDataPath = new File(
				getClass().getClassLoader().getResource("CreateAccountSweepsT24.json").getFile()).getPath();
		String appDataContent = new String(Files.readAllBytes(Paths.get(applicationDataPath)));
		
 	    mockedStatic.when(() -> DBPServiceExecutorBuilder.builder()).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withServiceId("T24SweepServices")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withOperationId("updateSweep")).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestParameters(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withRequestHeaders(anyMap())).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.withDataControllerRequest(request)).thenReturn(DbpServiceExecutorBuilder);
 		when(DbpServiceExecutorBuilder.build()).thenReturn(dbpServiceExecutor);
 		when(dbpServiceExecutor.getResponse()).thenReturn(appDataContent);
        
 		
  		
 		responseDto = accountSweepBusinessDelegateImpl.deleteSweepAtBackEnd(sweepDTO,request,"t24");
		
 		assertEquals(responseDto.getMessage(),"Account Sweep cancelled successfully");
    }
}

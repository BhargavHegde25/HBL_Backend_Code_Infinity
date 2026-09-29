package com.temenos.dbx.product.approvalsframework.approvalmatrix.businessdelegate.impl;


import static org.junit.Assert.assertEquals;
import  org.mockito.Mockito;
import  org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.DBPAPIAbstractFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto.ApprovalRuleDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.dto.LimitsDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import org.powermock.modules.junit4.PowerMockRunner;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CustomerSession.class)
@Ignore("Test in this class is disabled")
public class ApprovalMatrixBusinessDelegateImplTest {
	
	DataControllerRequest request;
	DataControllerResponse response;
	BusinessDelegateFactory businessDelegateFactory;
	//ApprovalMatrixBusinessDelegate approvalMatrixBusinessDelegate;
	ApprovalMatrixBusinessDelegateImpl approvalMatrixBusinessDelegateImpl;
	ContractBusinessDelegate contractBusinessDelegate;
	ApprovalQueueBusinessDelegate approvalQueueBusinessDelegate;
	DBPAPIAbstractFactory dbpAPIAbstractFactory;
	ResourceFactory resourceFactory;
	LimitsDTO contractLimits;
	ApprovalRuleDTO approvalRuleDTO;
	CustomerSession customerSession ;

	
	Result actualResult;
	
	
	//This will run once for the class before all the test methods
		@BeforeClass 
		public static void setUpBeforeClass() throws Exception {
			
		}

		@AfterClass
		public static void cleanup() throws Exception {
			
		}

		@After
		public void tearDown() throws Exception {
		}
		
		@Before //This will run before each test case
		public void setUp() throws Exception {
			request = Mockito.mock(DataControllerRequest.class);
			response = Mockito.mock(DataControllerResponse.class);
			customerSession = PowerMockito.mock(CustomerSession.class);

			businessDelegateFactory = Mockito.mock(BusinessDelegateFactory.class);
			dbpAPIAbstractFactory = Mockito.mock(DBPAPIAbstractFactory.class);
			resourceFactory = Mockito.mock(ResourceFactory.class);
		
			//approvalMatrixBusinessDelegate = mock(ApprovalMatrixBusinessDelegate.class);
			contractLimits = Mockito.mock(LimitsDTO.class);
			approvalRuleDTO = Mockito.mock(ApprovalRuleDTO.class);
			approvalMatrixBusinessDelegateImpl = new ApprovalMatrixBusinessDelegateImpl();
			contractBusinessDelegate = Mockito.mock(ContractBusinessDelegate.class);
			approvalQueueBusinessDelegate = Mockito.mock(ApprovalQueueBusinessDelegate.class);
						
			when(DBPAPIAbstractFactoryImpl.getInstance()).thenReturn(dbpAPIAbstractFactory);
			when(dbpAPIAbstractFactory.getFactoryInstance(BusinessDelegateFactory.class)).thenReturn(businessDelegateFactory);
			when(businessDelegateFactory.getBusinessDelegate(ContractBusinessDelegate.class)).thenReturn(contractBusinessDelegate);
		}

	@Test
	public void test() throws ApplicationException {
		Result result = new Result();
		result.addParam("status","success");
				
		String contractId ="8255159784";
		String coreCustomerId = "190755";
		String featureActionId = "CHEQUE_BOOK_REQUEST_CREATE";
		Set<String> accountIds = new HashSet<>();
		String limitTypeId ="NON_MONETARY_LIMIT";
		String userId = "testDataUserId";
		String userfullName = "testDatauserfullName";
		Map<String, Object> customerMap = new HashMap<>();
		
		Map<String, Object> customer = new HashMap<>();
		customer.put("customer_id", "123"); 
		customer.put("FirstName", "Temenos"); 
		customer.put("LastName", "P.LTD"); 
		
		JSONArray limits = new JSONArray("[{\"groupRule\":\"[[2]]\",\"groupList\":\"[df197bb4-9fc4-4dba-be88-4a4453da6eec]\",\"lowerlimit\":\"-1.00\",\"upperlimit\":\"-1.00\"}]");
		when(contractBusinessDelegate.fetchLimits(contractId, coreCustomerId, featureActionId, limitTypeId)).thenReturn(contractLimits);
		PowerMockito.when(CustomerSession.getCustomerMap(request)).thenReturn(customerMap);
		PowerMockito.when(CustomerSession.IsAPIUser(customerMap)).thenReturn(false);
		PowerMockito.when(CustomerSession.getCustomerId(customerMap)).thenReturn(userId);
		PowerMockito.when(CustomerSession.getCustomerCompleteName(customerMap)).thenReturn(userfullName);
		
		Result actualResult = new Result();
		actualResult = approvalMatrixBusinessDelegateImpl.createOrUpdateApprovalRuleWithApprovalRuleCheck(contractId, coreCustomerId, featureActionId, accountIds, limitTypeId, limits, request);
		assertEquals("success", actualResult.getParamValueByName("status"));
	}

}

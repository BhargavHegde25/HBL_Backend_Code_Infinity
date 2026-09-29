package com.temenos.infinity.smartbanking.advisory.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface SmartBankingAdvisoryResource extends Resource{

	Result getBusinessScoreAndDriversDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getEnrollmentStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAccountingData(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result startProcess(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result get12MonthCashFlow(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getSimulationDetails(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getSBAEnrolmentStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result updateSBAStatus(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAccountsReceivable(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAccountsPayable(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getReceivablesDebtorDaysReq(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getPayablesDebtorDaysReq(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAccountsReceivableByCustomer(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getAccountsPayableBySupplier(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
	Result getSBAFeaturesActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
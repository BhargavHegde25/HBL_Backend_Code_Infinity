package com.temenos.infinity.smartbanking.advisory.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface SmartBankingAdvisoryBackendDelegate extends BackendDelegate{

	Map<String, Object> getBusinessScoreAndDriversDetails(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getEnrollmentStatus(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getAccountingData(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> startProcess(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> get12MonthCashFlow(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getAnalyticsSimulationDetails(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getXaiSimulationDetails(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getSBAEnrolmentStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException;
	
	Map<String, Object> updateSBAStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException;
	
	Result getAccountsReceivable(Map<String, Object> payloadMap) throws SBAException;
	
	Result getAccountsPayable(Map<String, Object> payloadMap) throws SBAException;
	
	Result getReceivablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getAccountsReceivableByCustomer(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getPayablesSummaryBySupplier(Map<String, Object> payloadMap) throws SBAException;
	
	Result getPayablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException;
	
	Result getSBAFeaturesActions(Map<String, Object> payloadMap) throws SBAException;

}
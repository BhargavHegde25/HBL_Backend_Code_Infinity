package com.temenos.infinity.smartbanking.advisory.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.dataobject.Dataset;
import com.temenos.infinity.smartbanking.advisory.errorhandling.SBAException;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface SmartBankingAdvisoryBusinessDelegate extends BusinessDelegate{

	Map<String, Object> getBusinessScoreAndDriversDetails(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getEnrollmentStatus(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getAccountingData(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> startProcess(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> get12MonthCashFlow(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getSimulationDetails(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getSBAEnrolmentStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException;
	
	Map<String, Object> updateSBAStatus(Map<String, Object> payloadMap, DataControllerRequest request) throws SBAException;
	
	Dataset getAccountsReceivable(Map<String, Object> payloadMap) throws SBAException;
	
	Dataset getAccountsPayable(Map<String, Object> payloadMap) throws SBAException;
	
	Dataset getReceivablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Map<String, Object>> getReceivablesSummary(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Map<String, Object>> getPayableSummary(Map<String, Object> payloadMap) throws SBAException;
	
	Dataset getPayablesDebtorDaysReq(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getAccountsReceivableByCustomer(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getReceivableOverdueExcel(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getReceivableUpcomingExcel(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> generateByCustomerExcelList(Map<String, Object> payloadMap) throws SBAException;

	Map<String, Object> getAccountsPayableBySupplier(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getPayableOverdueExcel(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getPayableUpcomingExcel(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> generateBySupplierExcelList(Map<String, Object> payloadMap) throws SBAException;
	
	Map<String, Object> getSBAFeaturesActions(Map<String, Object> payloadMap) throws SBAException;
}
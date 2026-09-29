package com.infinity.dbx.temenos.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
//import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetUserDetailsToAdmin implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	static final String CUSTOMER_SEARCH = "CUSTOMER_SEARCH";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		try{
			
			if(StringUtils.isBlank(request.getParameter("accountNumber")) ) {
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addErrMsgParam("Misssing input param accountNumber");
			}
			String accountNumber = request.getParameter("accountNumber");
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(DB_ACCOUNTID,accountNumber );
			request.addRequestParam_(DB_ACCOUNTID, accountNumber);
			Result serviceResult = CommonUtils.callIntegrationService(request, params, null, UserConstants.SERVICE_ID_ACCOUNT,
					UserConstants.OP_GET_USER_DETAILS_TO_ADMIN, true);
			Dataset accountDS = serviceResult.getDatasetById(AccountsConstants.DS_ACCOUNTS);
	        List<Record> accountRecords = accountDS != null ? accountDS.getAllRecords() : null;
	        if (null !=accountRecords && !accountRecords.isEmpty()) {
	        	
	        	List<Record> recordList = new ArrayList<Record>();
	        	for (Record account : accountRecords) {
	        		
//	        		String userId = account.getParamValueByName(MEMBERSHIP_ID);
	        		String customerId = account.getParamValueByName(MEMBERSHIP_ID);
	        		
//	        		String dbxUserId = getDBXUserIdFromBackendIdentifier(request, userId, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID, "1");
	        		
	        		if(accountRecords.size() == 1) {
	        			
//	        			return searchCustomer(request,dbxUserId);
	        			return searchCustomer(request,customerId);
	        		}
	        		
//	        		Result searchResult = searchCustomer(request,dbxUserId);
	        		Result searchResult = searchCustomer(request,customerId);
	        		Dataset ds = searchResult.getDatasetById("records");
	        		List<Record> recList = ds != null ? ds.getAllRecords() : null;
	        		if (null !=recList && !recList.isEmpty()) {
		        		for (Record rec : recList) {
		        			recordList.add(rec);
		        		}
	        		}
	        		
	        	}
	        	Dataset ds = new Dataset();
	        	ds.setId("records");
	        	ds.addAllRecords(recordList);
	        	result.addDataset(ds);
	        	
	        	return result;
				
	        } else {
	        	
//	        	List<String> custIdList = getCustomerIdList(request, accountNumber );
//	        	if(custIdList.isEmpty()) {
//	        		
//	        		Dataset ds = new Dataset();
//		        	ds.setId("records");
//		        	result.addDataset(ds);
//	        	} else if(custIdList.size() == 1){
//	        		
//	        		result = searchCustomer(request,custIdList.get(0));
//	        	} else {
//	        		
//	        		List<Record> recordList = new ArrayList<Record>();
//	        		for(String id : custIdList) {
//	        			
//	        			Result searchResult = searchCustomer(request,id);
//		        		Dataset ds = searchResult.getDatasetById("records");
//		        		List<Record> recList = ds != null ? ds.getAllRecords() : null;
//		        		if (null !=recList && !recList.isEmpty()) {
//			        		for (Record rec : recList) {
//			        			recordList.add(rec);
//			        		}
//		        		}
//	        		}
//	        		Dataset ds = new Dataset();
//		        	ds.setId("records");
//		        	ds.addAllRecords(recordList);
//		        	result.addDataset(ds);
//	        		
//	        	}
	        	
        		Dataset ds = new Dataset();
	        	ds.setId("records");
	        	result.addDataset(ds);
	        }

				
		} catch(Exception e) {
			alert.prepareError("Unexpected error", e).log();
			result.addParam(new Param("FailureReason", e.getMessage()));
			result.addParam(new Param("dbpErrCode", "20001", "int"));
	        result.addParam(new Param("dbpErrMsg", "Internal Error", "string"));
		}
		
		return result;
	}



//	public String getDBXUserIdFromBackendIdentifier(DataControllerRequest request, String customerId, String backendType,
//			String identifier_name, String sequenceNumber) {
//		
//		String dbxId = "";
//		try {
//
//			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
//			HashMap<String, Object> svcParams = new HashMap<String, Object>();
//			String filter = CommonUtils.buildOdataCondition(Constants.BACKEND_ID, Constants.EQUAL, customerId);
//			filter = StringUtils.isNotBlank(sequenceNumber)
//					? CommonUtils
//							.buildSearchGroupQuery(filter, Constants.AND,
//									CommonUtils.buildOdataCondition(Constants.PARAM_SEQUENCENUMBER,
//											Constants.EQUAL, sequenceNumber),
//									false)
//					: filter;
//			filter = StringUtils.isNotBlank(backendType)
//					? CommonUtils.buildSearchGroupQuery(filter, Constants.AND,
//							CommonUtils.buildOdataCondition(Constants.BACKEND_TYPE, Constants.EQUAL, backendType), false)
//					: filter;
//			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
//			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
//					Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
//			Dataset backendIdentifiersDataset = result.getDatasetById(Constants.DS_BACKEND_IDENTIFIER);
//			if ( null !=backendIdentifiersDataset ) {
//				for (Record rec : backendIdentifiersDataset.getAllRecords()) {
//					if (identifier_name.equals(rec.getParamValueByName(Constants.PARAM_IDENTIFIER_NAME))) {
//						dbxId = rec.getParamValueByName(Constants.CUSTOMER_ID);
//						break;
//					}
//
//				}
//			}
//			
//
//		} catch(Exception e) {
//			alert.prepareError("Error while retrieving dbxId form backendIdentifier  "+ e ).log();
//		}
//		
//		return dbxId;
//	}
	
	public Result searchCustomer(DataControllerRequest request, String customerId) {
		
		Result serviceResult = new Result();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			HashMap<String, Object> headers = new HashMap<String, Object>();
			params.put("_searchType", CUSTOMER_SEARCH);
			request.addRequestParam_("_searchType", CUSTOMER_SEARCH);
//			params.put("_id", dbxUserId);
//			request.addRequestParam_("_id", dbxUserId);
			params.put("_customerId", customerId);
			request.addRequestParam_("_customerId", customerId);
			params.put("_pageOffset", 0);
			request.addRequestParam_("_pageOffset", "0");
			params.put("_pageSize", 20);
			request.addRequestParam_("_pageSize", "20");
			params.put("_sortVariable", "name");
			request.addRequestParam_("_sortVariable", "name");
			params.put("_sortDirection", "ASC");
			request.addRequestParam_("_sortDirection", "ASC");
			serviceResult  = CommonUtils.callObjectService(request, params, headers, "RBObjects", "search",
					"customerSearch", true);
		} catch(Exception e) {
			alert.prepareError("Error while retrieving user details search/searchCustomer api "+ e ).log();
		}
		
		return serviceResult;
		
	}
	
//	public List<String> getCustomerIdList(DataControllerRequest request, String accountNumber) {
//		
//		List<String> customerIdList = new ArrayList<String>();  
//		
//		try {
//			
//			String filter = CommonUtils.buildOdataCondition(DB_ACCOUNTID, Constants.EQUAL, accountNumber);
//			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
//			HashMap<String, Object> svcParams = new HashMap<String, Object>();
//			
//			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
//			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
//					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OPERATION_CUSTOMER_ACCOUNTS_GET, false);
//			Dataset ds = result.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS);
//			if ( null !=ds ) {
//				
//        		List<Record> recList = ds != null ? ds.getAllRecords() : null;
//        		if (null !=recList && !recList.isEmpty()) {
//	        		for (Record rec : recList) {
//	        			customerIdList.add(rec.getParamValueByName(Constants.CUSTOMER_ID));
//	        		}
//        		}
//	
//			}
//			
//		} catch(Exception e) {
//			
//			alert.prepareError("Error while Customer_id from db  "+ e ).log();
//		}
//
//		return customerIdList;
//	}
}

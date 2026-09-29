package com.bct.custom.backenddeligate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.BranchdetailsBackendDelegate;
import com.bct.custom.backenddeligate.api.MerchantDetailsBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.BranchDetails;
import com.bct.custom.dto.MerchantDTO;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;


public class BranchDetailsBackendDelegateImpl implements BranchdetailsBackendDelegate{
	private static final Logger logger = LogManager.getLogger(BranchDetailsBackendDelegateImpl.class);
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String DBPERRCODE = "dbpErrCode";
	@Override
	public JSONArray getBranchDetails(String input, Map<String, Object> inputArray, DataControllerRequest dcRequest)
			throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter ="";
		filter = "Status  eq '"+ 1 + "'";
		if(StringUtils.isNotBlank(input)) {
		 filter = "and bank_cd  eq '" + input + "'";
		}
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::BranchDetailsBackendDeligateImpl:getBranchMngrEmail: inputmap:"+
				inputmap.toString());
		JSONArray response = new JSONArray();
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.BRANCH_DETAILS_GET_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::BranchDetailsBackendDeligateImpl: getBranchMngrEmail response:"+dbresponse);
			JSONObject responseJSON = new JSONObject(dbresponse);
			response = responseJSON.getJSONArray("branchdetails");
			
			
		}catch (Exception e) {
			logger.error("Exception caught while fetching getBranchMngrEmail:" +e.toString());
			
		}
		return response;
	}
	@Override
	public Result createBranchDetails(ArrayList<BranchDetails> branchDetailsArray, Map<String, String> inputRequestParams,
			DataControllerRequest dcRequest) throws ApplicationException {
		logger.debug("BCT::BranchDetailsBackendDelegateImpl:createBranchDetails: dto:"+
				branchDetailsArray.toString());
		MerchantDetailsBackendDeligateImpl merchantBackendDelegate =  new MerchantDetailsBackendDeligateImpl();
		Result response = new Result();
		BranchDetails dto;
		String[] arr = new String[branchDetailsArray.size()];
		String tableName="dbxdb.branchdetails";
		String colSpec="(id,bank_cd,bank_name,bank_sc,branch_cd,branch_name,bank_swift,Status,branch_mge_email)";
		if(branchDetailsArray.size()>0) {
		for(int i=0;i<branchDetailsArray.size();i++) {
			dto=branchDetailsArray.get(i);
		
		 Map<String, Object> inputParams = new HashMap<>();
	        inputParams.put("id", dto.getId());
	        inputParams.put("bank_cd", dto.getBank_cd());
		    inputParams.put("bank_name", dto.getBank_name());
			inputParams.put("bank_sc", dto.getBank_sc());
	        inputParams.put("branch_cd", dto.getBranch_cd());
	        inputParams.put("branch_name", dto.getBranch_name());
	        inputParams.put("bank_swift", dto.getBank_swift());
	        inputParams.put("Status", dto.getStatus());
	        inputParams.put("branch_mge_email", dto.getBranch_mge_email());
			logger.debug("BCT::BranchDetailsBackendDelegateImpl:createMerchantDetails: inputParams:"+
					inputParams.toString());
			//response.appendResult(makeServiceCall(inputs, request));
			String qryValues="('"+dto.getId()+"','"+dto.getBank_cd()+"','"+dto.getBank_name()+"'"; 
			if(StringUtils.isNotBlank(dto.getBank_sc()))
				qryValues=qryValues+",'"+dto.getBank_sc()+"'";
			if(StringUtils.isBlank(dto.getBank_sc())) 
				qryValues=qryValues+",''";
			
			if(StringUtils.isNotBlank(dto.getBranch_cd()))
				qryValues=qryValues+",'"+dto.getBranch_cd()+"'";
			if(StringUtils.isBlank(dto.getBranch_cd())) 
				qryValues=qryValues+",''";

			if(StringUtils.isNotBlank(dto.getBranch_name()))
				qryValues=qryValues+",'"+dto.getBranch_name()+"'";
			if(StringUtils.isBlank(dto.getBranch_name())) 
				qryValues=qryValues+",''";
			
			if(StringUtils.isNotBlank(dto.getBank_swift()))
				qryValues=qryValues+",'"+dto.getBank_swift()+"'";
			if(StringUtils.isBlank(dto.getBank_swift())) 
				qryValues=qryValues+",''";
			
				qryValues=qryValues+","+dto.getStatus()+"";
				
				if(StringUtils.isNotBlank(dto.getBranch_mge_email()))
					qryValues=qryValues+",'"+dto.getBranch_mge_email()+"'";
				 if(StringUtils.isBlank(dto.getBranch_mge_email())) {
					qryValues=qryValues+",''";
				}
				 qryValues=qryValues+");";
				 
			arr[i]=qryValues;
		}
		response.appendResult(HBLCommonUtility.batchInsert(tableName, colSpec, arr, dcRequest));
		}
			return response;
	}
	

}

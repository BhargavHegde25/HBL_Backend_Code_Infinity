package com.temenos.dbx.product.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApproverBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.FeatureActionBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FeatureActionDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.dto.AccountActionApproverListDTO;
import com.temenos.dbx.product.resource.api.ApproverResource;

/**
 * 
 * @author KH2627
 * @version 1.0 Extends the {@link ApproverResource}
 */

public class ApproverResourceImpl implements ApproverResource {


	@Override
	public Result getAccountActionApprovers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		AccountActionApproverListDTO inputDTO = new AccountActionApproverListDTO();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result result = new Result();

		// Initialization of Approver business delegate class

		List<AccountActionApproverListDTO> approverList = new ArrayList<>();

		if (preProcess(dcRequest, inputParams, inputDTO, result)) {
			ApproverBusinessDelegate approverBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApproverBusinessDelegate.class);

			approverList = approverBusinessDelegate.getAccountActionApproverList(inputDTO, getHeaders(dcRequest),
					URLConstants.ACCOUNT_ACTION_APPROVERS_PROC);
		}

		return postProcess(approverList, result);
	}

	private Result postProcess(List<AccountActionApproverListDTO> approverList, Result result) {

		if (approverList == null) {
			ErrorCodeEnum.ERR_10711.setErrorCode(result);
		} else if (approverList.isEmpty()) {
			ErrorCodeEnum.ERR_10712.setErrorCode(result);
		} else {
			Dataset ds = new Dataset();
			ds.setId("Approvers");
			for (AccountActionApproverListDTO approver : approverList) {
				Record record = new Record();
				record.addStringParam("id", approver.getId());
				record.addStringParam("name", approver.getUserName());
				record.addStringParam("role", approver.getGroupId());
				record.addStringParam("firstname", approver.getFirstName());
				record.addStringParam("lastname", approver.getLastName());
				ds.addRecord(record);
			}
			result.addDataset(ds);
		}
		return result;
	}

	public static Map<String, Object> getHeaders(DataControllerRequest dcRequest) {
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
				dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
		headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		return headerMap;
	}

	private boolean preProcess(DataControllerRequest dcRequest, Map<String, String> inputParams,
			AccountActionApproverListDTO inputDTO, Result result) {

		String actionId = (inputParams.get("actionId")==null)?"":inputParams.get("actionId").toString();
		String contractId = (inputParams.get("contractId")==null)?"":inputParams.get("contractId").toString();
		String cif = (inputParams.get("cif")==null)?"":inputParams.get("cif").toString();

		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
        List<String> accountIdList = new ArrayList<>();
        if( StringUtils.isEmpty( dcRequest.getParameter(Constants.ACCOUNTID) ) ) {
        	accountIdList = accountBusinessDelegate.fetchCifAccounts(cif, contractId);     	
        }
        else {
            accountIdList.add(dcRequest.getParameter(Constants.ACCOUNTID));
        }		
		String accountIds = String.join(",", accountIdList);
		
		if (StringUtils.isBlank(actionId) || StringUtils.isBlank(contractId) || StringUtils.isBlank(cif) || StringUtils.isBlank(accountIds)) {
			ErrorCodeEnum.ERR_10710.setErrorCode(result);
			return false;
		}

		FeatureActionBusinessDelegate featureActionBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeatureActionBusinessDelegate.class);
		FeatureActionDTO featureActionDTO = featureActionBusinessDelegate.getFeatureActionById(actionId);

		inputDTO.setContractId(contractId);
		inputDTO.setCif(cif);
		inputDTO.setAccountId(accountIds);
		inputDTO.setApprovalActionList(featureActionDTO.getApproveFeatureAction());
		inputDTO.setFeatureId(featureActionDTO.getFeatureId());

		return true;
	}

}

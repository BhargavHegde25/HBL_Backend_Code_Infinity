package com.temenos.dbx.product.signatorygroupservices.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.ApprovalModeBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.ApprovalModeBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

/**
 * 
 * @author
 * @version 1.0 Extends the {@link SignatoryGroupBusinessDelegate}
 */
public class ApprovalModeBusinessDelegateImpl implements ApprovalModeBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public ApprovalModeDTO fetchApprovalMode(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request) {
		try {
			ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ApprovalModeBackendDelegate.class);
			ApprovalModeDTO approvalMode = backendDelegate.fetchApprovalMode(coreCustomerId, contractId, headersMap,
					request);
			return approvalMode;

		} catch (Exception e) {
			alert.prepareError("Error while calling the microservice :" + e).log();
			return null;
		}
	}

	@Override
	public ApprovalModeDTO updateApprovalMode(String coreCustomerId, String contractId, Boolean isGroupLevel,
			Map<String, Object> headersMap, DataControllerRequest request) {
		try {
			ApprovalModeDTO approvalModeDTO = new ApprovalModeDTO();
			approvalModeDTO.setCoreCustomerId(coreCustomerId);
			approvalModeDTO.setIsGroupLevel(isGroupLevel);
			approvalModeDTO.setContractId(contractId);
			ApprovalModeDTO approvalMode = null;
			ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ApprovalModeBackendDelegate.class);
			ApprovalModeDTO fetchDTO = backendDelegate.fetchApprovalMode(coreCustomerId, contractId, headersMap,
					request);
			if (fetchDTO != null && fetchDTO.getId() != null) {
				approvalModeDTO.setId(fetchDTO.getId());
				approvalMode = backendDelegate.updateApprovalMode(approvalModeDTO, headersMap, request);
			} else {
				UUID uuidValue = UUID.randomUUID();
				approvalModeDTO.setId(uuidValue.toString());
				approvalMode = backendDelegate.createApprovalMode(approvalModeDTO, headersMap, request);
			}

			return approvalMode;

		} catch (Exception e) {
			alert.prepareError("Error while calling the microservice :" + e).log();
			return null;
		}
	}

	@Override
	public ApprovalModeDTO deleteApprovalMode(String coreCustomerId, String contractId, Map<String, Object> headersMap,
			DataControllerRequest request) {
		try {
			ApprovalModeDTO approvalModeDTO = new ApprovalModeDTO();
			approvalModeDTO.setCoreCustomerId(coreCustomerId);
			approvalModeDTO.setContractId(contractId);
			ApprovalModeDTO approvalMode = null;
			ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ApprovalModeBackendDelegate.class);

			ApprovalModeDTO fetchDTO = backendDelegate.fetchApprovalMode(coreCustomerId, contractId, headersMap,
					request);
			if (fetchDTO != null) {
				approvalModeDTO.setId(fetchDTO.getId());
				approvalMode = backendDelegate.deleteApprovalMode(approvalModeDTO, headersMap, request);
			} else {
				return null;
			}

			return approvalMode;

		} catch (Exception e) {
			alert.prepareError("Error while calling the microservice :" + e).log();
			return null;
		}
	}

	@Override
	public boolean checkForApprovalMatrixPermission(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request) {
		try {
			ApprovalModeBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ApprovalModeBackendDelegate.class);
			boolean isAllowed = backendDelegate.checkForApprovalMatrixPermission(coreCustomerId, contractId, userId,
					headersMap, request);
			return isAllowed;

		} catch (Exception e) {
			alert.prepareError("Error while calling the microservice :" + e).log();
			return false;
		}
	}
	
	@Override
	public boolean isGroupLevel(String coreCustomerId) {
		/*
		ContractBusinessDelegate contractBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractBusinessDelegate.fetchContractCustomers(customerId);
		String contractId = "";
		if(CollectionUtils.isNotEmpty(contracts)) {
			contractId = contracts.get(0).substring(0, contracts.get(0).indexOf("_"));
		}
		*/
		if(coreCustomerId == null || coreCustomerId.isEmpty()) {
			return false;
		}
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String response = DBPServiceExecutorBuilder.builder().
								withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
								withObjectId(null).
								withOperationId(OperationName.DB_APPROVALMODE_GET).
								withRequestParameters(requestParams).
								build().getResponse();

			JSONObject jsonRsponse = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			List<ApprovalModeDTO> modes = JSONUtils.parseAsList(jsonArray.toString(), ApprovalModeDTO.class);
			for(ApprovalModeDTO dto : modes) {
				if(dto.getIsGroupLevel()) {
					return true;
				}
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while fetching Approval Mode", e).log();
			return false;
		}
		return false;
	}

}

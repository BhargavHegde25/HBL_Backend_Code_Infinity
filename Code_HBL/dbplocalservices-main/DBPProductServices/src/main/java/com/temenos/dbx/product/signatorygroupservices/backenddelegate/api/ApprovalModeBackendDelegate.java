package com.temenos.dbx.product.signatorygroupservices.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;

public interface ApprovalModeBackendDelegate extends BackendDelegate {

	ApprovalModeDTO fetchApprovalMode(String coreCustomerId, String contractId, Map<String, Object> headersMap,
			DataControllerRequest request);

	ApprovalModeDTO updateApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request);

	ApprovalModeDTO createApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request);

	ApprovalModeDTO deleteApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request);

	boolean checkForApprovalMatrixPermission(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request);
}

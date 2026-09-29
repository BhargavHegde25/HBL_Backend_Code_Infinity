package com.temenos.dbx.product.approvalmatrixservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public interface ApprovalMatrixBackendDelegate extends BackendDelegate {

    public Map<String, JSONObject> fetchFeatureActionsEligibleForApproval(String legalEntityId);

    public Integer fetchApprovalMode(String coreCustomerId, String contractId, String legalEntityId);

}

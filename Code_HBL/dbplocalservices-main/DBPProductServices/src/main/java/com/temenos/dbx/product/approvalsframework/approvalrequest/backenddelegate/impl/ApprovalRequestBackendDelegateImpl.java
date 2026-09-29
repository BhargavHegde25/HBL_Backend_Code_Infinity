package com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.impl;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.approvalsframework.approvalrequest.backenddelegate.api.ApprovalRequestBackendDelegate;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class ApprovalRequestBackendDelegateImpl implements ApprovalRequestBackendDelegate {
    @Override
    public JSONArray createNewRequestForTalliedMatrixIdsInDBXDB(String customerId, JSONArray contractCifTalliedMatrixIds, String confirmationNumber, String featureActionId, String accountId, String transactionAmount, String serviceCharges, JSONObject additionalMetaInfo, String comments) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_contractCifMatrixIdsJSON", contractCifTalliedMatrixIds.toString());
        reqParams.put("_confirmationNumber", confirmationNumber);
        reqParams.put("_featureActionId", featureActionId);
        reqParams.put("_accountId", accountId);
        reqParams.put("_createdBy", customerId);
        reqParams.put("_additionalMetaJSON", additionalMetaInfo.toString());
        reqParams.put("_comments", comments);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_CREATE_NEW_COMPOSITE_REQUEST_IN_APPROVALQUEUE_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray approveRequestsInDBXDB(String customerId, JSONArray requestsJSON) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestMatrixDataJSON", requestsJSON.toString());
        reqParams.put("_customerId", customerId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_APPROVE_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                } else {
                    // TODO: throw error - no request created
                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray rejectRequestsInDBXDB(String customerId, JSONArray requestsJSON) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestMatrixDataJSON", requestsJSON.toString());
        reqParams.put("_customerId", customerId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_REJECT_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                } else {
                    // TODO: throw error - no requests rejected
                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray withdrawRequestsInDBXDB(String customerId, JSONArray requestsJSON) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestMatrixDataJSON", requestsJSON.toString());
        reqParams.put("_customerId", customerId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_WITHDRAW_PENDINGREQUESTS_IN_APPROVALQUEUE_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                } else {
                    // TODO: throw error - no requests rejected
                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchRequestsInApprovalQueueFromDBXDB(Set<String> requestIds, String customerId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "createdby" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "( " + String.join(DBPUtilitiesConstants.OR, requestIds.stream().map((a) -> ("requestId" + DBPUtilitiesConstants.EQUAL + a)).collect(Collectors.toList())) + " )");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_BBREQUEST_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("bbrequest")) {
                    return responseObj.getJSONArray("bbrequest");
                } else {

                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAssociatedRequestsInApprovalQueueFromDBXDB(Set<String> assocRequestIds, String customerId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "createdby" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "( " + String.join(DBPUtilitiesConstants.OR, assocRequestIds.stream().map((a) -> ("requestId" + DBPUtilitiesConstants.EQUAL + a)).collect(Collectors.toList())) + " )");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_BBREQUEST_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("bbrequest")) {
                    return responseObj.getJSONArray("bbrequest");
                } else {

                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchRequestsWithApprovalMatrixInfoFromDBXBD(Set<String> requestIds, boolean isAssociationId, JSONArray contractCifMapJSON, boolean isActiveRulesFetch) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestIds", String.join(",", requestIds));
        reqParams.put("_isAssociationId", isAssociationId ? "1" : "0");
        reqParams.put("_contractCifMapJSON", contractCifMapJSON == null ? "" : contractCifMapJSON.toString());
        reqParams.put("_isActiveRulesFetch", isActiveRulesFetch ? "1" : "0");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_REQUESTS_WITH_APPROVALMATRIXINFO_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                } else {
                    // TODO: log error
                }
            } else {
                // TODO: log error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchActedRequestIdsInHistoryQueueFromDBXDB(Set<String> requestIds, String customerId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "createdby" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + "status" + DBPUtilitiesConstants.EQUAL + "Approved" + DBPUtilitiesConstants.AND + "( " + String.join(DBPUtilitiesConstants.OR, requestIds.stream().map((a) -> ("requestId" + DBPUtilitiesConstants.EQUAL + a)).collect(Collectors.toList())) + " )");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_BBACTEDREQUEST_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("bbactedrequest")) {
                    return responseObj.getJSONArray("bbactedrequest");
                } else {

                }
            } else {
                // TODO: throw error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchApproveFeatureActionForFeatureActionIdsFromDBXDB(Set<String> featureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "status" + DBPUtilitiesConstants.EQUAL + "SID_ACTION_ACTIVE" + DBPUtilitiesConstants.AND + "(" + String.join(DBPUtilitiesConstants.OR, featureActionIds.stream().map((a) -> ("id" + DBPUtilitiesConstants.EQUAL + a)).collect(Collectors.toList())) + ")");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FEATUREACTION_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("featureaction")) {
                    return responseObj.getJSONArray("featureaction");
                } else {
                    // TODO: log error
                }
            } else {
                // TODO: LOG error
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchContractCifMapForCustomerFromDBXDB(String customerId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "customerId" + DBPUtilitiesConstants.EQUAL + customerId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_CONTRACTCUSTOMERS_GET)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("contractcustomers")) {
                    return responseObj.getJSONArray("contractcustomers");
                } else {
                    // TODO: throw error
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchPendingApproversInfoForRequestFromDBXDB(String requestId, boolean isAssociationId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestId", requestId);
        reqParams.put("_isAssociationId", isAssociationId ? "1" : "0");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_PENDINGAPPROVERS_FOR_REQUEST_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchRequestHistoryInfoFromDBXDB(String requestId, boolean isAssociationId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_requestId", requestId);
        reqParams.put("_isAssociationId", isAssociationId ? "1" : "0");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_REQUEST_HISTORYINFO_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAllPendingRequestsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_customerId", customerId);
        reqParams.put("_featureActionIds", String.join(",", permittedFeatureActionIds));
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ALL_PENDINGREQUESTS_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAllRequestHistoryFromDBXDB(String customerId, Set<String> permittedFeatureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_customerId", customerId);
        reqParams.put("_featureActionIds", String.join(",", permittedFeatureActionIds));
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ALL_REQUESTHISTORY_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAllPendingApprovalsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_customerId", customerId);
        reqParams.put("_featureActionIds", String.join(",", permittedFeatureActionIds));
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ALL_PENDINGAPPROVALS_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAllApprovalHistoryFromDBXDB(String customerId, Set<String> permittedFeatureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_customerId", customerId);
        reqParams.put("_featureActionIds", String.join(",", permittedFeatureActionIds));
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ALL_APPROVALHISTORY_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }

    @Override
    public JSONArray fetchAllApprovalRequestsCountsFromDBXDB(String customerId, Set<String> permittedFeatureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_customerId", customerId);
        reqParams.put("_featureActionIds", String.join(",", permittedFeatureActionIds));
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_ALL_APPROVALREQUESTS_COUNTS_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {

        } catch (DBPApplicationException dbpae) {

        }
        return null;
    }
}

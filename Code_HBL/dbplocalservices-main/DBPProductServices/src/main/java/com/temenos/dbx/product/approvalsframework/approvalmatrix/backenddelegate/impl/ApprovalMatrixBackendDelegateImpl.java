package com.temenos.dbx.product.approvalsframework.approvalmatrix.backenddelegate.impl;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.approvalsframework.approvalmatrix.backenddelegate.api.ApprovalMatrixBackendDelegate;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApprovalMatrixBackendDelegateImpl implements ApprovalMatrixBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean createOrUpdateDefaultApprovalMatrixEntryInDBXDB(String contractId, Set<String> coreCustomerIds, Set<String> featureActionIds, Set<String> accountIds, boolean isGroupMatrix, boolean isDefaultDisabled, String legalEntityCurrency) {
        Map<String, Object> reqParams = new HashMap<>();

        reqParams.put("_contractId", contractId);
        reqParams.put("_coreCustomerIds", String.join(",", coreCustomerIds));
        reqParams.put("_featureActionIds", String.join(",", featureActionIds));
        reqParams.put("_accountIds", String.join(",", accountIds));
        reqParams.put("_isGroupRule", isGroupMatrix ? "1" : "0");
        reqParams.put("_isDefaultDisabled", isDefaultDisabled ? "1" : "0");
        reqParams.put("_currency", legalEntityCurrency);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_APPROVALMATRIX_DEFAULT_INIT_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                return true;
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_APPROVALMATRIX_DEFAULT_INIT_PROC operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while processing DBXDB_APPROVALMATRIX_DEFAULT_INIT_PROC: " + dbpae).log();
        }
        return false;
    }

    @Override
    public JSONArray fetchCompositeApprovalModeFromDBXDB(JSONArray contractCifMapJSON) {
        Map<String, Object> reqParams = new HashMap<>();

        reqParams.put("_contractCifMapJSON", contractCifMapJSON.toString());
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_COMPOSITE_APPROVALMODE_PROC)
                    .withRequestParameters(reqParams)
                    .withRequestHeaders(null)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                if (resObj.has("records")) {
                    JSONArray resArr = resObj.getJSONArray("records");
                    if (resArr.length() > 0) {
                        return resArr;
                    } else {
                        alert.prepareWarn("APPROVALMODE table entries not found for the given contract and cifs: " + contractCifMapJSON).log();
                    }
                }
            } else {
                alert.prepareError("Failed to read DBXDB_FETCH_COMPOSITE_APPROVALMODE_PROC response.").log();
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_FETCH_COMPOSITE_APPROVALMODE_PROC operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while processing DBXDB_FETCH_COMPOSITE_APPROVALMODE_PROC: " + dbpae).log();
        }
        return null;
    }

    @Override
    public JSONArray fetchApprovalModeFromDBXDB(String coreCustomerId, String contractId) {
        Map<String, Object> reqParams = new HashMap<>();

        reqParams.put(DBPUtilitiesConstants.FILTER, "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId + DBPUtilitiesConstants.AND + "contractId" + DBPUtilitiesConstants.EQUAL + contractId);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_APPROVALMODE_GET)
                    .withRequestParameters(reqParams)
                    .withRequestHeaders(null)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                if (resObj.has("approvalmode")) {
                    JSONArray resArr = resObj.getJSONArray("approvalmode");
                    if (resArr.length() > 0) {
                        return resArr;
                    } else {
                        alert.prepareError("APPROVALMODE table entries not found for the given contract and cifs: " + contractId + " " + coreCustomerId).log();
                    }
                }
            } else {
                alert.prepareError("Failed to read APPROVALMODE table.").log();
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_APPROVALMODE_GET operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while reading APPROVALMODE table: " + dbpae).log();
        }
        return null;
    }

    @Override
    public Boolean updateApprovalModeInDBXDB(String coreCustomerId, String contractId, boolean isGroupLevel) {
        Map<String, Object> reqParams = new HashMap<>();

        reqParams.put("_contractId", contractId);
        reqParams.put("_coreCustomerId", coreCustomerId);
        reqParams.put("_isGroupMatrix", isGroupLevel ? "1" : "0");
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_UPDATE_APPROVALMODE_FOR_CONTRACTANDCIF_PROC)
                    .withRequestParameters(reqParams)
                    .withRequestHeaders(null)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                return true;
            } else {
                alert.prepareError("Failed to read DBXDB_UPDATE_APPROVALMODE_FOR_CONTRACTANDCIF_PROC response.").log();
                return false;
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_UPDATE_APPROVALMODE_FOR_CONTRACTANDCIF_PROC operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while processing DBXDB_UPDATE_APPROVALMODE_FOR_CONTRACTANDCIF_PROC: " + dbpae).log();
        }
        return null;
    }

    @Override
    public JSONArray fetchCompositeApprovalMatrixStatusFromDBXDB(JSONArray contractCifMapJSON, Set<String> featureActionIds) {
        Map<String, Object> reqParams = new HashMap<>();
        String featureActionIDtemp=String.join(",", featureActionIds);
        if(featureActionIDtemp.contains("EDIT_RECEPIENT_OPTIONAL")){
        try {
        	boolean hasEmptyId;
        	JSONObject cifsObject;
        	JSONObject jsonObject;
        	JSONArray cifsArray;
        	JSONArray jsonArrayOptinal = new JSONArray(); 
            // Iterate through the JSON array in reverse order to safely remove elements while iterating
            for (int i = contractCifMapJSON.length() - 1; i >= 0; i--) {
                 jsonObject = contractCifMapJSON.getJSONObject(i);
                 cifsArray = jsonObject.getJSONArray("cifs");
                // Check if the "cifs" array contains an object with an empty "id" field
                 hasEmptyId = false;
                for (int j = 0; j < cifsArray.length(); j++) {
                    cifsObject = cifsArray.getJSONObject(j);
                    if (cifsObject.getString("id").isEmpty()) {
                        hasEmptyId = true;
                        break;
                    }
                }
                if (!hasEmptyId) {
                	jsonArrayOptinal.put(contractCifMapJSON.get(i));
                }
            }
            reqParams.put("_contractCifMapJSON", jsonArrayOptinal.toString());
        } catch (JSONException e) {
        	alert.prepareError("Exception occurred while parsing contractCifMapJSON in fetchCompositeApprovalMatrixStatusFromDBXDB: " + e).log();
        }
        }else {
        reqParams.put("_contractCifMapJSON", contractCifMapJSON.toString());
        }
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_COMPOSITE_APPROVALMATRIXSTATUS_PROC)
                    .withRequestParameters(reqParams)
                    .withRequestHeaders(null)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                JSONArray resArr = resObj.getJSONArray("records");
                if (resArr.length() > 0) {
                    return resArr;
                } else {
                    alert.prepareError("MANAGEAPPROVALMATRIX table entries not found for the given contract and cifs: " + contractCifMapJSON).log();
                }
            } else {
                alert.prepareError("Failed to read DB_FETCH_COMPOSITE_APPROVALMATRIXSTATUS_PROC response.").log();
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DB_FETCH_COMPOSITE_APPROVALMATRIXSTATUS_PROC operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while reading DBXDB_FETCH_COMPOSITE_APPROVALMATRIXSTATUS_PROC response: " + dbpae).log();
        }
        return null;
    }

    @Override
    public JSONArray fetchApprovalMatrixStatusFromDBXDB(String coreCustomerId, String contractId) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(DBPUtilitiesConstants.FILTER, "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId + DBPUtilitiesConstants.AND + "contractId" + DBPUtilitiesConstants.EQUAL + contractId);

        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_MANAGEAPPROVALMATRIX_GET)
                    .withRequestParameters(reqParams)
                    .withRequestHeaders(null)
                    .build().getResponse();

            JSONObject resObj = new JSONObject(response);
            if (resObj.has("opstatus") && resObj.getInt("opstatus") == 0) {
                if (resObj.has("manageapprovalmatrix")) {
                    JSONArray resArr = resObj.getJSONArray("manageapprovalmatrix");
                    if (resArr.length() > 0) {
                        return resArr;
                    } else {
                        alert.prepareError("MANAGEAPPROVALMATRIX table entries not found for the given contract and cifs: " + contractId + " " + coreCustomerId).log();
                    }
                }
            } else {
                alert.prepareError("Failed to read MANAGEAPPROVALMATRIX table.").log();
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_MANAGEAPPROVALMATRIX operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while reading MANAGEAPPROVALMATRIX table: " + dbpae).log();
        }
        return null;
    }

    @Override
    public boolean createOrUpdateApprovalRuleInDBXDB(String contractId, String coreCustomerId, String featureActionId, Set<String> accountIds, String limitTypeId, JSONArray limits) {
        Map<String, Object> reqParams = new HashMap<>();

        reqParams.put("_contractId", contractId);
        reqParams.put("_coreCustomerId", coreCustomerId);
        reqParams.put("_featureActionId", featureActionId);
        reqParams.put("_accountIds", accountIds != null ? String.join(",", accountIds) : null);
        reqParams.put("_limitTypeId", limitTypeId);
        reqParams.put("_limitValuesJSON", limits.toString());
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_CREATE_APPROVALMATRIXRULE_FOR_FEATUREACTION_AND_LIMITTYPE_PROC)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                return true;
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_MANAGEAPPROVALMATRIX operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while reading MANAGEAPPROVALMATRIX table: " + dbpae).log();
        }
        return false;
    }

    @Override
    public JSONArray fetchCompositeApprovalMatrixRulesForContractCifMap(JSONArray contractCifMapJSON, Set<String> featureActionIds, String accountId) {
    	Map<String, Object> reqParams = new HashMap<>();
        String featureActionIDtemp=String.join(",", featureActionIds);
        if(featureActionIDtemp.contains("EDIT_RECEPIENT_OPTIONAL")){
        try {
        	boolean hasEmptyId;
        	JSONObject cifsObject;
        	JSONObject jsonObject;
        	JSONArray cifsArray;
        	JSONArray jsonArrayOptinal = new JSONArray(); 
            // Iterate through the JSON array in reverse order to safely remove elements while iterating
            for (int i = contractCifMapJSON.length() - 1; i >= 0; i--) {
                 jsonObject = contractCifMapJSON.getJSONObject(i);
                 cifsArray = jsonObject.getJSONArray("cifs");
                // Check if the "cifs" array contains an object with an empty "id" field
                 hasEmptyId = false;
                for (int j = 0; j < cifsArray.length(); j++) {
                    cifsObject = cifsArray.getJSONObject(j);
                    if (cifsObject.getString("id").isEmpty()) {
                        hasEmptyId = true;
                        break;
                    }
                }
                if (!hasEmptyId) {
                	jsonArrayOptinal.put(contractCifMapJSON.get(i));
                }
            }
            reqParams.put("_contractCifMapJSON", jsonArrayOptinal.toString());
        } catch (JSONException e) {
            alert.prepareError("Exception occurred while parsing contractCifMapJSON in fetchCompositeApprovalMatrixRulesForContractCifMap: " + e).log();
        }
        }else {
        reqParams.put("_contractCifMapJSON", contractCifMapJSON.toString());
        }
        reqParams.put("_featureActionIds", featureActionIDtemp);
        try {
            String response = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
                    .withObjectId(null)
                    .withOperationId(OperationName.DB_FETCH_APPROVALMATRIX_ACTIVERULES_FOR_CONTRACTCIFMAP)
                    .withRequestParameters(reqParams)
                    .build().getResponse();
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.has("opstatus") && responseObj.getInt("opstatus") == 0) {
                if (responseObj.has("records")) {
                    return responseObj.getJSONArray("records");
                }
            }
        } catch (JSONException je) {
            alert.prepareError("Exception occurred while parsing DBXDB_FETCH_APPROVALMATRIX_ACTIVERULES_FOR_CONTRACTCIFMAP operation JSON response: " + je).log();
        } catch (DBPApplicationException dbpae) {
            alert.prepareError("Exception occurred while reading DBXDB_FETCH_APPROVALMATRIX_ACTIVERULES_FOR_CONTRACTCIFMAP proc response: " + dbpae).log();
        }
        return null;
    }
}

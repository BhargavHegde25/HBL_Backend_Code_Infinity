package com.kony.adminconsole.service.approvalworkflow.businessdelegate.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.EventEnum;
import org.json.JSONArray;
import org.json.JSONObject;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.service.approvalworkflow.businessdelegate.api.MigrateDataBusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;


public class MigrateDataBusinessDelegateImpl implements MigrateDataBusinessDelegate {

    public JSONObject moveDataRecords(Map<String, Object> postParametersMap, DataControllerRequest request)
            throws DBPApplicationException {
        String status = postParametersMap.get("status").toString();
        String requestId = postParametersMap.get("requestId").toString();
        String authToken = CommonUtilities.getAuthToken(request);
        String recordId = postParametersMap.get("recordId").toString().replaceAll("\\\\","");
        JSONObject recordIdAsJSON = new JSONObject(recordId);
        JSONObject responseJSON = new JSONObject(postParametersMap.get("response").toString());
        JSONArray resultJSONArray =  CommonUtilities.sortJSONArrayOfJSONObjects(responseJSON.optJSONArray("permissionapprovalconfig"), "updateSequence", true, true);
        if (resultJSONArray != null && resultJSONArray.length() > 0) {
            String moduleName = null;
            JSONObject currJSONObject;
            for (Object currObject : resultJSONArray) {
                if (currObject instanceof JSONObject) {
                    currJSONObject = (JSONObject) currObject;
                    if(currJSONObject.optString("approvalTableName").equalsIgnoreCase("proc")) {
                    	callProcService(currJSONObject, recordIdAsJSON,status,requestId);
                    }
                    else {
                        if (status.equalsIgnoreCase("Approved") && !currJSONObject.getBoolean("cleanup")) {
                        	insertOrDeleteRecord(currJSONObject, recordIdAsJSON, authToken, "updateInsert",requestId);
                        } else if (status.equalsIgnoreCase("Approved") && currJSONObject.getBoolean("cleanup")) {
                            insertOrDeleteRecord(currJSONObject, recordIdAsJSON, authToken, "cleanupMode",requestId);
                        }
                    }
				}
			}
            JSONObject jsonObject = resultJSONArray.optJSONObject(0);
            if(jsonObject != null) {
                moduleName = jsonObject.optString("expAPINickName");
            }
            for (Object currObject : resultJSONArray) {
				if (currObject instanceof JSONObject) {
					currJSONObject = (JSONObject) currObject;
					if(!(currJSONObject.optString("approvalTableName").equalsIgnoreCase("proc"))) {
						insertOrDeleteRecord(currJSONObject, recordIdAsJSON, authToken, "deleteMode",requestId);
					}
                }
            }
            String approvedMessage = "User Approved the Request with RequestId: "+requestId+" Successfully";
            String rejectedMessage = "User Rejected the Request with RequestId: "+requestId+" Successfully";
            boolean approved = status.equalsIgnoreCase("approved");
            EventEnum eventEnum = approved ? EventEnum.APPROVEREQUEST : EventEnum.REJECTREQUEST;
            String message = approved ? approvedMessage : rejectedMessage;
            AuditHandler.auditAdminActivity(request, moduleName, eventEnum, ActivityStatusEnum.SUCCESSFUL, message);
        }
        return responseJSON;
    }

    private void insertOrDeleteRecord(JSONObject recordDetails, JSONObject recordIdAsJSON, String authToken,
                                      String mode,String requestId) {
        JSONObject responseObj = new JSONObject();
        JSONArray responseArray = new JSONArray();
        String serviceToInvoke = "";
        String responseCRUD = null;
        String tableKey = recordDetails.optString("approvalTableName");
        String operationToInvoke = "dbxdb_"; 
        if(mode.equalsIgnoreCase("updateInsert") || mode.equalsIgnoreCase("cleanupMode")) {
        	serviceToInvoke = "CRUDLayer";
    		responseObj = getDataFromTable(recordDetails, recordIdAsJSON, "ApprovalCRUDLayer",requestId,mode);
            responseArray = responseObj.getJSONArray(tableKey);
           if (responseArray != null && responseArray.length() > 0) {
               if (mode.equalsIgnoreCase("cleanupMode")) {
                   Map<String, Object> inputMap = new HashMap<>();
                   operationToInvoke += recordDetails.optString("originalTableName") + "_delete";
                 	 String query = getSQLFilterQueryNew(recordDetails,recordIdAsJSON,responseArray);
                     inputMap.put("$filter", query);
                     try {
                         responseCRUD = DBPServiceExecutorBuilder.builder().withServiceId(serviceToInvoke)
                                 .withOperationId(operationToInvoke).withRequestParameters(inputMap).build()
                                 .getResponse();
                     } catch (DBPApplicationException e) {
                         // alert.prepareError("Failed to delete service definition table: " + e).log();
                     }
               } else if(mode.equalsIgnoreCase("updateInsert")) {
                    JSONObject currJSONObject;
                    for (Object currObject : responseArray) {
                        Map<String, Object> inputMap = new HashMap<>();
                        operationToInvoke = "dbxdb_"; 
                        if (currObject instanceof JSONObject) {
                            currJSONObject = (JSONObject) currObject;
                            Iterator<String> keys = (Iterator<String>) currJSONObject.keys();
                          	if(currJSONObject.optString("crudAction").equalsIgnoreCase("INS")) {
                          		operationToInvoke += recordDetails.optString("originalTableName") + "_create";
                          	}
                          	else {
                          		operationToInvoke += recordDetails.optString("originalTableName") + "_update";
                          	}
                             while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    if (!(key.equalsIgnoreCase("synctimestamp")) && !(key.equalsIgnoreCase("lastmodifiedts"))
                                            && !(key.equalsIgnoreCase("createdts")) && !(key.equalsIgnoreCase("crudAction")) && !(key.equalsIgnoreCase("aprRequestId"))) {
                                        inputMap.put(key, currJSONObject.optString(key));
                                    }
                                }
                            try {
                                responseCRUD = DBPServiceExecutorBuilder.builder().withServiceId(serviceToInvoke)
                                        .withOperationId(operationToInvoke).withRequestParameters(inputMap).build()
                                        .getResponse();
                            } catch (DBPApplicationException e) {
                                // alert.prepareError("Failed to delete service definition table: " + e).log();
                            }
                        }
                    }
                }
           }
       }
       else if(mode.equalsIgnoreCase("deleteMode")) {
           Map<String, Object> inputMap = new HashMap<>();
           serviceToInvoke = "ApprovalCRUDLayer";
           operationToInvoke += recordDetails.optString("approvalTableName") + "_delete";
           String query = "aprRequestId eq '"+requestId+"'";   		   
           String primaryColumns = recordDetails.optString("keyColumnNames");
   		   String[] primaryKeyArray = primaryColumns.split(",");
   		   for (int i = 0; i < primaryKeyArray.length-1; ++i) {
          	 query += " and ";
        	 query += primaryKeyArray[i]+" ne ''";
            }
   		    query += " and "+primaryKeyArray[primaryKeyArray.length-1]+" ne ''";
           inputMap.put("$filter", query);
           try {
               responseCRUD = DBPServiceExecutorBuilder.builder().withServiceId(serviceToInvoke)
                       .withOperationId(operationToInvoke).withRequestParameters(inputMap).build()
                       .getResponse();
           } catch (DBPApplicationException e) {
               // alert.prepareError("Failed to delete service definition table: " + e).log();
           }
       }

    }

	private JSONObject getDataFromTable(JSONObject recordDetails, JSONObject recordIdAsJSON, String serviceToInvoke,String requestId, String mode) {
        JSONObject responseObj = new JSONObject();
		String operationToInvoke ="";
		if (serviceToInvoke.equals("ApprovalCRUDLayer")) {
			operationToInvoke = "dbxdb_" + recordDetails.optString("approvalTableName") + "_get";
		} else {
			operationToInvoke = "dbxdb_" + recordDetails.optString("originalTableName") + "_get";
		}
        String responseCRUD = null;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        JSONObject queryJSON= new JSONObject(recordDetails.optString("sqlquery"));
        String query = "";
        if(mode.equalsIgnoreCase("updateInsert")){
        	query="aprRequestId eq '"+requestId+"' and (crudAction eq 'INS' or crudAction eq 'UPD')";
        }
        else if(mode.equalsIgnoreCase("cleanupMode")) {
        	query="aprRequestId eq '"+requestId+"' and crudAction eq 'DEL'";
        }
        requestParameters.put("$filter", query);
        // get data from _approval table
        try {
            responseCRUD = DBPServiceExecutorBuilder.builder().withServiceId(serviceToInvoke)
                    .withOperationId(operationToInvoke).withRequestParameters(requestParameters).build().getResponse();
        } catch (DBPApplicationException e) {
            // alert.prepareError("Failed to get data from approval table: " + e).log();
        }
        responseObj = CommonUtilities.getStringAsJSONObject(responseCRUD);
        return responseObj;
    }

	private String getSQLFilterQueryNew(JSONObject recordDetails,JSONObject recordIdAsJSON, JSONArray responseArray) {
		String query="";
		JSONObject currJSONObject;
		String primaryColumns = recordDetails.optString("keyColumnNames");
		String[] primaryKeyArray = primaryColumns.split(",");
        for (int j = 0; j < responseArray.length(); j++) {
        	query += "(";
        	currJSONObject = responseArray.getJSONObject(j);
                for (int i = 0; i < primaryKeyArray.length-1; ++i) {
                	query += primaryKeyArray[i]+" eq '"+currJSONObject.optString(primaryKeyArray[i])+"'";
                	query += " and ";
                }
                query += primaryKeyArray[primaryKeyArray.length-1]+" eq '"+currJSONObject.optString(primaryKeyArray[primaryKeyArray.length-1])+"')";
                if(!(j == responseArray.length()-1)) {
                	query += " or ";
                }
        }
		return query;
	}
    private String getSQLFilterQuery(JSONObject queryJSON,JSONObject recordIdAsJSON) {
        String result = "";
        String queryKey = "query";
        String concatKey = "concateoperator";
        String currKey = concatKey;
        String currKeyIndex;
        for(int objIndex=1 ;objIndex <= queryJSON.length() ;objIndex++) {
            currKey = currKey.equalsIgnoreCase(queryKey)?concatKey : queryKey;
            if(currKey.equalsIgnoreCase(queryKey) && objIndex!=1 && !queryJSON.has(concatKey+(objIndex-1))) {
                break;
            }
            else if(currKey.equalsIgnoreCase(concatKey) && !queryJSON.has(queryKey+(objIndex+1))) {
                break;
            }
            else {
                currKeyIndex = currKey+objIndex;
                if(currKey.equalsIgnoreCase(queryKey)) {
                    JSONObject currObject = queryJSON.getJSONObject(currKeyIndex);
                    if(currObject.has("datatype") && currObject.optString("datatype").equalsIgnoreCase("string")) {
                        String val = recordIdAsJSON.optString(currObject.optString("value"));
                        result += currObject.optString("key")+" "+currObject.optString("operator")+" '"+val+"' ";
                    }
                    else if (currObject.has("datatype") && currObject.optString("datatype").equalsIgnoreCase("array")) {
                        JSONArray targetJSONArray = recordIdAsJSON.optJSONArray(currObject.optString("value"));
                        for (int indexVar = 0; indexVar < targetJSONArray.length(); indexVar++) {
                            result += currObject.optString("key")+" "+currObject.optString("operator")+" '"+targetJSONArray.getString(indexVar)+"' ";
                            if(indexVar<targetJSONArray.length()-1) {
                                result += currObject.optString("arrayconcatinator")+" ";
                            }
                        }
                    }
                }
                else if(currKey.equalsIgnoreCase(concatKey)) {
                    result += queryJSON.optString(currKeyIndex);
                }
            }
        }
        return result.trim();
    }
	private void callProcService(JSONObject recordDetails, JSONObject recordIdAsJSON, String status, String requestId) {
        String operationToInvoke ="dbxdb_" + recordDetails.optString("originalTableName");
        String serviceToInvoke = "ApprovalCRUDLayer";
        JSONObject queryJSON= new JSONObject(recordDetails.optString("sqlquery"));
        Iterator<String> keys = (Iterator<String>) queryJSON.keys();
        String responseCRUD = null;
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("_context",status);
        inputMap.put("_requestId",requestId);
        for(int objIndex=1 ;objIndex <= queryJSON.length() ;objIndex++) {
            if(queryJSON.has("query"+objIndex)) {
                JSONObject currObject = queryJSON.getJSONObject("query"+objIndex);
                String val = recordIdAsJSON.optString(currObject.optString("value"));
                String key = currObject.optString("key");
                inputMap.put(key,val);
            }
        }
        try {
            responseCRUD = DBPServiceExecutorBuilder.builder().withServiceId(serviceToInvoke)
                    .withOperationId(operationToInvoke).withRequestParameters(inputMap).build().getResponse();
        } catch (DBPApplicationException e) {
            // alert.prepareError("Failed to get data from approval table: " + e).log();
        }
    }
}
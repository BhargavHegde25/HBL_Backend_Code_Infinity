package com.kony.adminconsole.service.approvalrequests.businessdelegate.impl;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.DashboardCountsBean;
import com.kony.adminconsole.dto.RequestBean;
import com.kony.adminconsole.service.approvalrequests.backenddelegate.api.ApprovalRequestsBackendDelegate;
import com.kony.adminconsole.service.approvalrequests.businessdelegate.api.ApprovalRequestsBusinessDelegate;
import com.kony.adminconsole.utilities.ApprovalConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ApprovalRequestsBusinessDelegateImpl implements ApprovalRequestsBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    ApprovalRequestsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance().getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(ApprovalRequestsBackendDelegate.class);

    @Override
    public DashboardCountsBean getApprovalDashboardCounts(Set<String> permissionSet, String loggedInUserId) {

        DashboardCountsBean dashboardCountsDTO = new DashboardCountsBean();

        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_userId", loggedInUserId);
        reqParams.put("_permissionListArr", CommonUtilities.getSQLConcatenatedString(permissionSet));

        JSONArray records = backendDelegate.getApprovalRequestCountsDBData(reqParams, null);
        if(records == null){
            alert.prepareError("Error reading db records at ApprovalRequestsBusinessDelegateImpl - getDashboardCountsFromDB()!").log();
            return null;
        }
        dashboardCountsDTO.setDashboardCountsDataFromDBRecords(records);

        return dashboardCountsDTO;
    }

    @Override
    public List<RequestBean> getAllPendingRequests(String loggedInUserId) {

        List<RequestBean> pendingRequests = new ArrayList<>();

        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(ODataQueryConstants.FILTER, "createdby eq '" + loggedInUserId + "' and status eq 'Pending For Approval'");

        JSONArray records = backendDelegate.getApprovalRequestDBData(reqParams, null);
        if(records == null){
            alert.prepareError("Error reading db records at ApprovalRequestsBusinessDelegateImpl - getAllPendingRequests()!").log();
            return null;
        }

        for(Object obj: records){
            JSONObject record = (JSONObject) obj;
            RequestBean requestDto = new RequestBean(record);
            pendingRequests.add(requestDto);
        }
        return pendingRequests;
    }

    @Override
    public List<RequestBean> getAllPendingApprovals(Set<String> permissionSet, String loggedInUserId, Map<String, Object> filterParams) {

        List<RequestBean> pendingApprovals = new ArrayList<>();

        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_userId", loggedInUserId);
        reqParams.put("_permissionListArr", CommonUtilities.getSQLConcatenatedString(permissionSet));
        reqParams.put("_moduleListArr", filterParams.containsKey(ApprovalConstants.MODULE_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.MODULE_FILTER)) : null);
        reqParams.put("_featureListArr", filterParams.containsKey(ApprovalConstants.FEATURE_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.FEATURE_FILTER)) : null);
        reqParams.put("_actionListArr", filterParams.containsKey(ApprovalConstants.ACTION_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.ACTION_FILTER)) : null);
        reqParams.put("_searchStartDate", filterParams.containsKey(ApprovalConstants.START_DATE_FILTER) ? (String)filterParams.get(ApprovalConstants.START_DATE_FILTER) : null);
        reqParams.put("_searchEndDate", filterParams.containsKey(ApprovalConstants.END_DATE_FILTER) ? (String)filterParams.get(ApprovalConstants.END_DATE_FILTER) : null);
        reqParams.put("_sortParam", filterParams.containsKey(ApprovalConstants.SORT_PARAM_FILTER) ? (String)filterParams.get(ApprovalConstants.SORT_PARAM_FILTER) : null);
        reqParams.put("_sortOrder", filterParams.containsKey(ApprovalConstants.SORT_ORDER_FILTER) ? (String)filterParams.get(ApprovalConstants.SORT_ORDER_FILTER) : null);
        reqParams.put(ApprovalConstants.IS_KEYCLOAK_ENABLED, filterParams.containsKey(ApprovalConstants.IS_KEYCLOAK_ENABLED) ? (String) filterParams.get(ApprovalConstants.IS_KEYCLOAK_ENABLED) : null);

        JSONArray records = CommonUtilities.getPaginatedJSONArray(backendDelegate.getUserPendingApprovalsDBData(reqParams, null),
                (Integer) filterParams.getOrDefault(ApprovalConstants.PAGE_SIZE_FILTER, null),
                (Integer) filterParams.getOrDefault(ApprovalConstants.PAGE_OFFSET_FILTER, null));

        if(records == null){
            alert.prepareError("Error reading db records at ApprovalRequestsBusinessDelegateImpl - getAllPendingApprovals()!").log();
            return null;
        }

        for(Object obj: records){
            JSONObject record = (JSONObject) obj;
            RequestBean requestDto = new RequestBean(record);
            pendingApprovals.add(requestDto);
        }
        return pendingApprovals;
    }

    @Override
    public List<RequestBean> getAllRequestsHistory(String loggedInUserId) {

        List<RequestBean> requestHistory = new ArrayList<>();

        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put(ODataQueryConstants.FILTER, "createdby eq '" + loggedInUserId + "'");

        JSONArray records = backendDelegate.getApprovalRequestDBData(reqParams, null);
        if(records == null){
            alert.prepareError("Error reading db records at ApprovalRequestsBusinessDelegateImpl - getAllRequestsHistory()!").log();
            return null;
        }

        for(Object obj: records){
            JSONObject record = (JSONObject) obj;
            RequestBean requestDto = new RequestBean(record);
            requestHistory.add(requestDto);
        }
        return requestHistory;
    }

    @Override
    public List<RequestBean> getAllApprovalsHistory(String loggedInUserId, Map<String, Object> filterParams) {

        List<RequestBean> approvalHistory = new ArrayList<>();

        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("_userId", loggedInUserId);
        reqParams.put("_moduleListArr", filterParams.containsKey(ApprovalConstants.MODULE_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.MODULE_FILTER)) : null);
        reqParams.put("_featureListArr", filterParams.containsKey(ApprovalConstants.FEATURE_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.FEATURE_FILTER)) : null);
        reqParams.put("_actionListArr", filterParams.containsKey(ApprovalConstants.ACTION_FILTER) ? CommonUtilities.getSQLConcatenatedString((Set)filterParams.get(ApprovalConstants.ACTION_FILTER)) : null);
        reqParams.put("_searchStartDate", filterParams.containsKey(ApprovalConstants.START_DATE_FILTER) ? (String)filterParams.get(ApprovalConstants.START_DATE_FILTER) : null);
        reqParams.put("_searchEndDate", filterParams.containsKey(ApprovalConstants.END_DATE_FILTER) ? (String)filterParams.get(ApprovalConstants.END_DATE_FILTER) : null);
        reqParams.put("_sortParam", filterParams.containsKey(ApprovalConstants.SORT_PARAM_FILTER) ? (String)filterParams.get(ApprovalConstants.SORT_PARAM_FILTER) : null);
        reqParams.put("_sortOrder", filterParams.containsKey(ApprovalConstants.SORT_ORDER_FILTER) ? (String)filterParams.get(ApprovalConstants.SORT_ORDER_FILTER) : null);
        reqParams.put(ApprovalConstants.IS_KEYCLOAK_ENABLED, filterParams.containsKey(ApprovalConstants.IS_KEYCLOAK_ENABLED) ? (String) filterParams.get(ApprovalConstants.IS_KEYCLOAK_ENABLED) : null);

        JSONArray records = CommonUtilities.getPaginatedJSONArray(backendDelegate.getUserApprovalHistoryDBData(reqParams, null),
                (Integer) filterParams.getOrDefault(ApprovalConstants.PAGE_SIZE_FILTER, null),
                (Integer) filterParams.getOrDefault(ApprovalConstants.PAGE_OFFSET_FILTER, null));

        if(records == null){
            alert.prepareError("Error reading db records at ApprovalRequestsBusinessDelegateImpl - getAllApprovalsHistory()!").log();
            return null;
        }

        for(Object obj: records){
            JSONObject record = (JSONObject) obj;
            RequestBean requestDto = new RequestBean(record);
            approvalHistory.add(requestDto);
        }
        return approvalHistory;
    }
}

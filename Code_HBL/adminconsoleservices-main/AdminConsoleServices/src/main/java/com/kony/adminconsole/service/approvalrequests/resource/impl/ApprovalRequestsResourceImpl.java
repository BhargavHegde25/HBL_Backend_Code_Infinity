package com.kony.adminconsole.service.approvalrequests.resource.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kony.adminconsole.utilities.ApprovalConstants;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.DashboardCountsBean;
import com.kony.adminconsole.dto.RequestBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.service.approvalrequests.businessdelegate.api.ApprovalRequestsBusinessDelegate;
import com.kony.adminconsole.service.approvalrequests.resource.api.ApprovalRequestsResource;
import com.kony.adminconsole.utilities.ApprovalUtils;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;

public class ApprovalRequestsResourceImpl implements ApprovalRequestsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    ApprovalRequestsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ApprovalRequestsBusinessDelegate.class);

    @Override
    public Result getDashboardCounts(DataControllerRequest dcRequest) throws AppRegistryException, MiddlewareException, IOException {

        Set<String> permissionSet = null;
        String loggedInUserId = null;

        boolean isKeyCloakEnabled = false;

        try{
            isKeyCloakEnabled = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(dcRequest));
            diagnostic.prepareTrace("KeyCloak Enabled:" + isKeyCloakEnabled).log();
        } catch(Exception e) {
            
            alert.prepareError("KeyCloak configuration not found: " , e).log();
        }
        try {
        	if(isKeyCloakEnabled){

        		UserDetailsBean userAttributes;
        		userAttributes = LoggedInUserHandler.getUserDetails(dcRequest);
        		if (userAttributes != null) {
        			diagnostic.prepareTrace("Obtained Role Id From UserDetailsBean:" + userAttributes.getRoleId()).log();
        			permissionSet = LoggedInUserHandler.getLoggedInUserPermissions(dcRequest);
        			diagnostic.prepareTrace("Obtained Permissions Set:" + permissionSet).log();
        			loggedInUserId = StringUtils.isNotBlank(userAttributes.getId()) ? userAttributes.getId() : userAttributes.getUserId();
        			diagnostic.prepareTrace("Obtained loggedInUserId From UserDetailsBean:" + loggedInUserId).log();
        		}

        	} else {
        		permissionSet = LoggedInUserHandler.getLoggedInUserPermissions(dcRequest);
        		Map<String, String> loggedInUserAttributes = CommonUtilities.getLoggedInUserAttributes(dcRequest);
        		loggedInUserId = loggedInUserAttributes.get("id");
        	}
        } catch (ApplicationException ae) {
        	alert.prepareError("Exception",ae).log();
        }
        if (permissionSet == null) {
            alert.prepareError("User Permissions are empty!").log();
            return ErrorCodeEnum.ERR_22218.setErrorCode(new Result());
        }

        if (loggedInUserId == null) {
            alert.prepareError("User ID not found in user attributes!").log();
            return ErrorCodeEnum.ERR_22220.setErrorCode(new Result());
        }

        DashboardCountsBean countsBean = businessDelegate.getApprovalDashboardCounts(permissionSet, loggedInUserId);
        alert.prepareError("Count Bean" + countsBean).log();
        return countsBean.getConsolidatedCountsMapAsResultObject();
    }

    @Override
    public Result getAllPendingRequests(DataControllerRequest dcRequest) {

        Result result = new Result();
        Dataset pendingRequestsResultList = new Dataset("Requests");

        String loggedInUserId = null;

        boolean isKeyCloakEnabled = false;

        try{
            isKeyCloakEnabled = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(dcRequest));
            diagnostic.prepareTrace("KeyCloak Enabled:" + isKeyCloakEnabled).log();
        } catch(Exception e) {
            
            alert.prepareError("KeyCloak configuration not found: " ,e).log();
        }

        if (isKeyCloakEnabled) {
            try {
                UserDetailsBean userAttributes;
                userAttributes = LoggedInUserHandler.getUserDetails(dcRequest);
                if (userAttributes != null) {
                    loggedInUserId = StringUtils.isNotBlank(userAttributes.getId()) ? userAttributes.getId() : userAttributes.getUserId();
                    diagnostic.prepareTrace("Obtained loggedInUserId From UserDetailsBean:" + loggedInUserId).log();
                }
            } catch (ApplicationException ae) {
            	alert.prepareError("Exception",ae).log();
            }
        } else {
            Map<String, String> loggedInUserAttributes = CommonUtilities.getLoggedInUserAttributes(dcRequest);
            loggedInUserId = loggedInUserAttributes.get("id");
        }

        if (loggedInUserId == null) {
            alert.prepareError("User ID not found in user attributes!").log();
            return ErrorCodeEnum.ERR_22220.setErrorCode(result);
        }

        List<RequestBean> pendingRequests = businessDelegate.getAllPendingRequests(loggedInUserId);

        for (RequestBean requestObj : pendingRequests) {
            Record record = requestObj.getAsRecord();
            pendingRequestsResultList.addRecord(record);
        }

        result.addDataset(pendingRequestsResultList);
        return result;
    }

    @Override
    public Result getAllPendingApprovals(DataControllerRequest dcRequest) throws AppRegistryException, MiddlewareException, IOException {

        Result result = new Result();
        Dataset pendingApprovalsResultList = new Dataset("Requests");

        Set<String> permissionSet = null;
        String loggedInUserId = null;
        Map<String, Object> filterParams = ApprovalUtils.getFilterParams(dcRequest);

        boolean isKeyCloakEnabled = false;

        try{
            isKeyCloakEnabled = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(dcRequest));
            diagnostic.prepareTrace("KeyCloak Enabled:" + isKeyCloakEnabled).log();
        } catch(Exception e) {
           
            alert.prepareError("KeyCloak configuration not found: " , e).log();
        }
        try {
        	if (isKeyCloakEnabled) {

        		UserDetailsBean userAttributes;
        		userAttributes = LoggedInUserHandler.getUserDetails(dcRequest);
        		if (userAttributes != null) {
        			diagnostic.prepareTrace("Obtained Role Id From UserDetailsBean:" + userAttributes.getRoleId()).log();
        			permissionSet = LoggedInUserHandler.getLoggedInUserPermissions(dcRequest);
        			diagnostic.prepareTrace("Obtained Permissions Set:" + permissionSet).log();
        			loggedInUserId = StringUtils.isNotBlank(userAttributes.getId()) ? userAttributes.getId() : userAttributes.getUserId();
        			diagnostic.prepareTrace("Obtained loggedInUserId From UserDetailsBean:" + loggedInUserId).log();
        		}

        	} else {
        		permissionSet = LoggedInUserHandler.getLoggedInUserPermissions(dcRequest);
        		Map<String, String> loggedInUserAttributes = CommonUtilities.getLoggedInUserAttributes(dcRequest);
        		loggedInUserId = loggedInUserAttributes.get("id");
        	}
        } catch (ApplicationException ae) {
        	alert.prepareError("Exception",ae).log();
        }
        if (permissionSet == null) {
            alert.prepareError("User Permissions are empty!").log();
            return ErrorCodeEnum.ERR_22218.setErrorCode(new Result());
        }

        if (loggedInUserId == null) {
            alert.prepareError("User ID not found in user attributes!").log();
            return ErrorCodeEnum.ERR_22220.setErrorCode(result);
        }

        filterParams.put(ApprovalConstants.IS_KEYCLOAK_ENABLED, isKeyCloakEnabled ? "true" : "false");

        List<RequestBean> pendingApprovals = businessDelegate.getAllPendingApprovals(permissionSet, loggedInUserId, filterParams);
        alert.prepareError("Pending Approvals" + pendingApprovals).log();

        for (RequestBean requestObj : pendingApprovals) {

            Record record = requestObj.getAsRecord();
            pendingApprovalsResultList.addRecord(record);

        }

        result.addDataset(pendingApprovalsResultList);

        return result;

    }

    @Override
    public Result getAllRequestHistory(DataControllerRequest dcRequest) {

        Result result = new Result();
        Dataset requestHistoryResultList = new Dataset("Requests");

        String loggedInUserId = null;

        boolean isKeyCloakEnabled = false;

        try{
            isKeyCloakEnabled = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(dcRequest));
            diagnostic.prepareTrace("KeyCloak Enabled:" + isKeyCloakEnabled).log();
        } catch(Exception e) {
            
            alert.prepareError("KeyCloak configuration not found: " , e).log();
        }

        if (isKeyCloakEnabled) {
            try {
                UserDetailsBean userAttributes;
                userAttributes = LoggedInUserHandler.getUserDetails(dcRequest);
                if (userAttributes != null) {
                    diagnostic.prepareTrace("Obtained Role Id From UserDetailsBean:" + userAttributes.getRoleId()).log();
                    loggedInUserId = StringUtils.isNotBlank(userAttributes.getId()) ? userAttributes.getId() : userAttributes.getUserId();
                    diagnostic.prepareTrace("Obtained loggedInUserId From UserDetailsBean:" + loggedInUserId).log();
                }
            } catch (ApplicationException ae) {
            	alert.prepareError("Exception",ae).log();
            }
        } else {
            Map<String, String> loggedInUserAttributes = CommonUtilities.getLoggedInUserAttributes(dcRequest);
            loggedInUserId = loggedInUserAttributes.get("id");
        }

        if (loggedInUserId == null) {
            alert.prepareError("User ID not found in user attributes!").log();
            return ErrorCodeEnum.ERR_22220.setErrorCode(result);
        }

        List<RequestBean> requestHistory = businessDelegate.getAllRequestsHistory(loggedInUserId);

        for (RequestBean requestObj : requestHistory) {
            Record record = requestObj.getAsRecord();
            requestHistoryResultList.addRecord(record);
        }

        result.addDataset(requestHistoryResultList);
        return result;
    }

    @Override
    public Result getAllApprovalHistory(DataControllerRequest dcRequest) {

        Result result = new Result();
        Dataset approvalHistoryResultList = new Dataset("Requests");

        String loggedInUserId = null;
        Map<String, Object> filterParams = ApprovalUtils.getFilterParams(dcRequest);

        boolean isKeyCloakEnabled = false;

        try{
            isKeyCloakEnabled = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(dcRequest));
            diagnostic.prepareTrace("KeyCloak Enabled:" + isKeyCloakEnabled).log();
        } catch(Exception e) {
            
            alert.prepareError("KeyCloak configuration not found: " ,e).log();
        }

        if (isKeyCloakEnabled) {
            try {
                UserDetailsBean userAttributes;
                userAttributes = LoggedInUserHandler.getUserDetails(dcRequest);
                if (userAttributes != null) {
                    diagnostic.prepareTrace("Obtained Role Id From UserDetailsBean:" + userAttributes.getRoleId()).log();
                    loggedInUserId = StringUtils.isNotBlank(userAttributes.getId()) ? userAttributes.getId() : userAttributes.getUserId();
                    diagnostic.prepareTrace("Obtained loggedInUserId From UserDetailsBean:" + loggedInUserId).log();
                }
            } catch (ApplicationException ae) {
            	alert.prepareError("Exception",ae).log();
            }
        } else {
            Map<String, String> loggedInUserAttributes = CommonUtilities.getLoggedInUserAttributes(dcRequest);
            loggedInUserId = loggedInUserAttributes.get("id");
        }

        if (loggedInUserId == null) {
            alert.prepareError("User ID not found in user attributes!").log();
            return ErrorCodeEnum.ERR_22220.setErrorCode(result);
        }

        filterParams.put(ApprovalConstants.IS_KEYCLOAK_ENABLED, isKeyCloakEnabled ? "true" : "false");

        List<RequestBean> approvalHistory = businessDelegate.getAllApprovalsHistory(loggedInUserId, filterParams);

        for (RequestBean requestObj : approvalHistory) {
            Record record = requestObj.getAsRecord();
            approvalHistoryResultList.addRecord(record);
        }

        result.addDataset(approvalHistoryResultList);
        return result;
    }
}

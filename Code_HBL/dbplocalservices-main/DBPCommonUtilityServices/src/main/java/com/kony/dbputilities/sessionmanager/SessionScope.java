package com.kony.dbputilities.sessionmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.session.Session;

public final class SessionScope {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final String PERMISSIONS_IDENTITY_KEY = "permissions";
    
    private SessionScope() {
    }

    private static void setUserId(Session session, String userId) {
        session.setAttribute(DBPUtilitiesConstants.USER_ID, userId);
    }

    public static String getUserId(DataControllerRequest dcr) throws HttpCallException {
        String userId = (String) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_ID);

        if (userId == null) {
            userId = HelperMethods.getCustomerFromIdentityService(dcr).get("customer_id");
            setUserId(dcr.getSession(), userId);
        }
        diagnostic.prepareDebug("++ SeesionScope ++ getUserID: " + userId).log();
        return userId;
    }

    private static void setUserName(Session session, String userName) {
        session.setAttribute(DBPUtilitiesConstants.USER_NAME, userName);
    }

    public static String getUserName(DataControllerRequest dcr) throws HttpCallException {
        String userName = (String) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_NAME);

        if (userName == null) {
            userName = HelperMethods.getCustomerFromIdentityService(dcr).get("UserName");
            setUserName(dcr.getSession(), userName);
        }
        diagnostic.prepareDebug("++ SeesionScope ++ getUserName: " + userName).log();
        return userName;
    }

    private static void setStatusIds(Session session, HashMap<String, String> StatusIds) {
        session.setAttribute(DBPUtilitiesConstants.BB_STATUS_IDS, StatusIds);
    }

    public static HashMap<String, String> getStatusIds(DataControllerRequest dcr) throws HttpCallException {

        @SuppressWarnings("unchecked")
        HashMap<String, String> StatusIds = (HashMap<String, String>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.BB_STATUS_IDS);

        if (StatusIds == null) {
            StatusIds = new HashMap<>();
            Result result = HelperMethods.callApi(dcr, null, HelperMethods.getHeaders(dcr), URLConstants.BB_STATUS_GET);

            List<Record> records = result.getDatasetById("bbstatus").getAllRecords();

            for (int i = 0; i < records.size(); i++) {
                Record record = records.get(i);
                String id = record.getParam("id").getValue();
                String status = record.getParam("status").getValue();

                StatusIds.put(status, id);
            }
            setStatusIds(dcr.getSession(), StatusIds);
        }

        return StatusIds;
    }

    private static void setUserCompanyId(Session session, String userCompanyId) {
        session.setAttribute(DBPUtilitiesConstants.USER_COMPANY_ID, userCompanyId);
    }

    public static String getUserCompanyId(DataControllerRequest dcr) throws HttpCallException {
        String userCompanyId = (String) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_COMPANY_ID);
        diagnostic.prepareDebug("++ SeesionScope ++ getUserCompanyId ++ userCompanyId1: " + userCompanyId).log();
        if (userCompanyId == null) {
            userCompanyId = fetchUserCompanyId(dcr);
            diagnostic.prepareDebug("++ SeesionScope ++ getUserCompanyId ++ userCompanyId2: " + userCompanyId).log();
            setUserCompanyId(dcr.getSession(), userCompanyId);
        }

        return userCompanyId;
    }

    private static void setUserAddresses(Session session, JSONArray addresses) {
        session.setAttribute(DBPUtilitiesConstants.USER_ADDRESSES, addresses);
    }

    public static JSONArray getUserAddresses(DataControllerRequest dcr) throws HttpCallException {

        JSONArray addresses = (JSONArray) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_ADDRESSES);

        if (addresses == null) {
            fetchUserEntitlemnets(dcr);
            addresses = (JSONArray) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_ADDRESSES);
        }

        return addresses;
    }

    private static void setUserEmailIds(Session session, JSONArray emailIds) {
        session.setAttribute(DBPUtilitiesConstants.USER_EMAILIDS, emailIds);
    }

    public static JSONArray getUserEmailIds(DataControllerRequest dcr) throws HttpCallException {

        JSONArray emailIds = (JSONArray) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_EMAILIDS);

        if (emailIds == null) {
            fetchUserEntitlemnets(dcr);
            emailIds = (JSONArray) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_EMAILIDS);
        }

        return emailIds;
    }

    private static void setUserContactNumbers(Session session, JSONArray contactNumbers) {
        session.setAttribute(DBPUtilitiesConstants.USER_CONTACT_NUMBERS, contactNumbers);
    }

    public static JSONArray getUserContactNumbers(DataControllerRequest dcr) throws HttpCallException {

        JSONArray contactNumbers = (JSONArray) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_CONTACT_NUMBERS);

        if (contactNumbers == null) {
            fetchUserEntitlemnets(dcr);
            contactNumbers = (JSONArray) dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_CONTACT_NUMBERS);
        }

        return contactNumbers;
    }

    private static void setUserIsSecurityQuestionConfigured(Session session, String flag) {

        if (flag == null) {
            flag = "false";
        }
        session.setAttribute(DBPUtilitiesConstants.USER_IS_SECURITY_QUESTIONS_CONFIGURED, flag);
    }

    public static boolean getUserIsSecurityQuestionConfigured(DataControllerRequest dcr) throws HttpCallException {

        String flag = dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_IS_SECURITY_QUESTIONS_CONFIGURED)
                .toString();

        if (flag == null) {
            fetchUserEntitlemnets(dcr);
            flag = dcr.getSession().getAttribute(DBPUtilitiesConstants.USER_IS_SECURITY_QUESTIONS_CONFIGURED)
                    .toString();
        }

        return Boolean.parseBoolean(flag);
    }

    private static void setAllUserServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_ALL_SERVICES, services);
    }
    
    public static String getCustomerIdFromIdentityScope(DataControllerRequest dcr) {
    	
        try {
			IdentityHandler identityHandler = dcr.getServicesManager().getIdentityHandler();
			String customerId = identityHandler.getUserId();
			if(StringUtils.isNotBlank(customerId)) {
				return customerId;
			}
		} catch (Exception e) {
			alert.prepareError("Unable to parse the user data from the identity scope",e).log();
		}
       
        return null;
    }
    
    public static Set<String> getAllPermissionsFromIdentityScope(DataControllerRequest dcr) {
        Set<String> userPermissions = new HashSet<>();
        try {
			
			JSONObject permissions = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(dcr);
			
			if (permissions != null && permissions.has(PERMISSIONS_IDENTITY_KEY)) {
                String identityPermissions = permissions.getString(PERMISSIONS_IDENTITY_KEY);
                if (StringUtils.isNotBlank(identityPermissions)) {
                    JSONArray jsonArray = new JSONArray(identityPermissions);
                    if (jsonArray.length() > 0) {
                        jsonArray.forEach(permission -> userPermissions.add((String) permission));
                    }
                }
            }
			
		} catch (Exception e) {
			alert.prepareError("Unable to parse the user permissions from the identity scope",e).log();
		}
       
        return userPermissions;
    }

    private static void setUserCreateServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_CREATE_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserCreateServices(DataControllerRequest dcr)
            throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_CREATE_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_CREATE_SERVICES);
        }

        return services;
    }

    public static void setUserViewServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_VIEW_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserViewServices(DataControllerRequest dcr) throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_VIEW_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_VIEW_SERVICES);
        }

        return services;
    }

    private static void setUserSelfApproveServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_SELF_APPROVE_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserSelfApproveServices(DataControllerRequest dcr)
            throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_SELF_APPROVE_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_SELF_APPROVE_SERVICES);
        }

        return services;
    }

    private static void setUserApproveServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_APPROVE_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserApproveServices(DataControllerRequest dcr)
            throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_APPROVE_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_APPROVE_SERVICES);
        }

        return services;
    }

    private static void setUserManagePayeeServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_MANAGE_PAYEE_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserManagePayeeServices(DataControllerRequest dcr)
            throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_MANAGE_PAYEE_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_MANAGE_PAYEE_SERVICES);
        }

        return services;
    }

    private static void setUserManageTemplateServices(Session session, HashMap<String, JSONObject> services) {
        session.setAttribute(DBPUtilitiesConstants.USER_MANAGE_TEMPLATE_SERVICES, services);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, JSONObject> getUserManageTemplateServices(DataControllerRequest dcr)
            throws HttpCallException {

        HashMap<String, JSONObject> services = (HashMap<String, JSONObject>) dcr.getSession()
                .getAttribute(DBPUtilitiesConstants.USER_MANAGE_TEMPLATE_SERVICES);

        if (services == null) {
            fetchUserEntitlemnets(dcr);
            services = (HashMap<String, JSONObject>) dcr.getSession()
                    .getAttribute(DBPUtilitiesConstants.USER_MANAGE_TEMPLATE_SERVICES);
        }

        return services;
    }

    private static void fetchUserEntitlemnets(DataControllerRequest dcRequest) throws HttpCallException {

        try {
            Session session = dcRequest.getSession();

            HashMap<String, String> inputParams = new HashMap<>();
            Result result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ENTITLEMENT_OBJ_URL);

            Dataset addresses = result.getDatasetById("Addresses");
            Dataset emailIds = result.getDatasetById("EmailIds");
            Dataset contactNumbers = result.getDatasetById("ContactNumbers");
            Dataset services = result.getDatasetById("services");
            String isSecurityQuestionConfigured = "false";
            if (result.getNameOfAllParams().contains("isSecurityQuestionConfigured")) {
                isSecurityQuestionConfigured = result.getParamByName("isSecurityQuestionConfigured").getValue();
            }
            setUserAddresses(session, CommonUtils.convertDatasetToJSONArray(addresses));
            setUserEmailIds(session, CommonUtils.convertDatasetToJSONArray(emailIds));
            setUserContactNumbers(session, CommonUtils.convertDatasetToJSONArray(contactNumbers));
            setUserIsSecurityQuestionConfigured(session, isSecurityQuestionConfigured);
            saveUserServices(dcRequest, services);

        } catch (HttpCallException exp) {
            throw new HttpCallException("Failed to fetch the Entitlemnets from customer 360");
        }

    }

    private static void saveUserServices(DataControllerRequest dcRequest, Dataset services) throws HttpCallException {

        List<Record> records = new ArrayList<>();

        HashMap<String, JSONObject> allservices = new HashMap<>();
        HashMap<String, JSONObject> createServices = new HashMap<>();
        HashMap<String, JSONObject> approveServices = new HashMap<>();
        HashMap<String, JSONObject> selfApproveServices = new HashMap<>();
        HashMap<String, JSONObject> ManagePayeeServices = new HashMap<>();
        HashMap<String, JSONObject> viewServices = new HashMap<>();
        HashMap<String, JSONObject> manageTemplateServices = new HashMap<>();

        HashMap<String, String> listOfIds = CommonUtils.getAllTypeMappings(dcRequest);

        if (services != null && services.getAllRecords().size() != 0) {
            records = services.getAllRecords();
        }

        for (int i = 0; i < records.size(); i++) {
            JSONObject service = CommonUtils.convertRecordToJSONObject(records.get(i));
            String serviceId = service.getString("serviceId");

            allservices.put(serviceId, service);

            if (service.has("serviceName")) {
                String serviceFullName = service.getString("serviceName").trim().replaceAll(" +", " ");
                String[] serviceName = serviceFullName.split(" - ");
                String typeId = listOfIds.get(serviceName[0]);

                if (typeId == null || "".equals(typeId)) {
                    continue;
                }

                if (serviceFullName.contains(" - Self Approval")) {
                    selfApproveServices.put(typeId, service);
                } else if (serviceFullName.contains(" - Approve Transactions")) {
                    approveServices.put(typeId, service);
                } else if (serviceFullName.contains(" - Manage Payees")) {
                    ManagePayeeServices.put(typeId, service);
                } else if (serviceFullName.contains(" - View Transactions")) {
                    viewServices.put(typeId, service);
                } else if (serviceFullName.contains(" - Create Transaction")) {
                    createServices.put(typeId, service);
                } else if (serviceFullName.contains(" - Manage Templates")) {
                    manageTemplateServices.put(typeId, service);
                }
            }
        }

        Session session = dcRequest.getSession();

        setAllUserServices(session, allservices);
        setUserManagePayeeServices(session, ManagePayeeServices);
        setUserManageTemplateServices(session, manageTemplateServices);
        setUserCreateServices(session, createServices);
        setUserViewServices(session, viewServices);
        setUserSelfApproveServices(session, selfApproveServices);
        setUserApproveServices(session, approveServices);
    }

    public static void clear(DataControllerRequest dcRequest) {
        Session session = dcRequest.getSession();
        session.removeAttribute(DBPUtilitiesConstants.USER_ADDRESSES);
        session.removeAttribute(DBPUtilitiesConstants.USER_EMAILIDS);
        session.removeAttribute(DBPUtilitiesConstants.USER_CONTACT_NUMBERS);
        session.removeAttribute(DBPUtilitiesConstants.USER_IS_SECURITY_QUESTIONS_CONFIGURED);
        session.removeAttribute(DBPUtilitiesConstants.USER_ALL_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_CREATE_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_VIEW_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_APPROVE_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_SELF_APPROVE_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_MANAGE_PAYEE_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.BB_STATUS_IDS);
        session.removeAttribute(DBPUtilitiesConstants.USER_MANAGE_TEMPLATE_SERVICES);
        session.removeAttribute(DBPUtilitiesConstants.USER_COMPANY_ID);
        session.removeAttribute(DBPUtilitiesConstants.USER_ID);
        session.removeAttribute(DBPUtilitiesConstants.USER_NAME);
    }

    public static String fetchUserCompanyId(DataControllerRequest dcRequest) throws HttpCallException {

        try {
            String id = getUserId(dcRequest);
            diagnostic.prepareDebug("++ SeesionScope ++ fetchUserCompanyId - UserID: " + id).log();

            String companyId = HelperMethods.getOrganizationIDForUser(id, dcRequest);
            diagnostic.prepareDebug("++ SeesionScope ++ fetchUserCompanyId - CompanyId: " + companyId).log();
            return companyId;

        } catch (HttpCallException exp) {
            throw new HttpCallException("Failed to fetch user company ID");
        }

    }

    public static Set<String> getAllPermissionsFromIdentityScope(FabricRequestManager fabricRequestManager) {

        Set<String> userPermissions = new HashSet<String>();
        JSONObject permissions = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(fabricRequestManager);

        if (permissions != null && permissions.has(PERMISSIONS_IDENTITY_KEY)) {
            String identityPermissions = permissions.getString(DBPConstants.PERMISSIONS_IDENTITY_KEY);
            if (StringUtils.isNotBlank(identityPermissions)) {
                JSONArray jsonArray = new JSONArray(identityPermissions);
                if (jsonArray.length() > 0) {
                    jsonArray.forEach(permission -> userPermissions.add((String) permission));
                }
            }
        }
        
        return userPermissions;
    }
}

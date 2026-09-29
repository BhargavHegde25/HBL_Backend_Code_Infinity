package com.auth.hbl.businessdelegate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.auth.usermanagement.businessdelegate.api.AuthUserManagementBusinessDelegate;
import com.temenos.auth.usermanagement.businessdelegate.impl.AuthUserManagementBusinessDelegateImpl;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

public class AuthUserManagementBusinessDelegateImplExtn extends AuthUserManagementBusinessDelegateImpl{
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String HBL_VIEW_ONLY_ROLE_ID="HBL_VIEW_ONLY";
	@Override
	public Result getCustomerFeatureAndPermissions(String legalEntityId, String cacheKey,
			Map<String, String> userInfo) throws ApplicationException {
		diagnostic.prepareDebug("AuthUserManagementBusinessDelegateImplExtn... loggedin user is API user...").log();
		String isC360Admin = userInfo.get("isC360Admin");
		/**
		 * returns for API indentity login
		 */
		if("true".equalsIgnoreCase(isC360Admin)) {
			Result featureAndPermissions = new Result();
			diagnostic.prepareDebug("... loggedin user is API user...").log();
			JSONArray permissionsArray = new JSONArray();
			permissionsArray.put("API_ACCESS");
			featureAndPermissions.addStringParam("permissions",
					permissionsArray.toString());
			return featureAndPermissions;
		}
		
		/**
		 * returns from cache
		 */
		
		String featuresAndPermissionsStr = (String) MemoryManager.getFromCache(cacheKey);
		diagnostic.prepareDebug("retieved current leid features and permissions::" + featuresAndPermissionsStr).log();
		/*if(StringUtils.isNoneBlank(featuresAndPermissionsStr)) {
		return JSONToResult.convert(featuresAndPermissionsStr);
		}
		 */
		//ITSM-1985458
		diagnostic.prepareDebug("ITSM-1985458 featuresAndPermissionsStr:" + featuresAndPermissionsStr).log();
		if(StringUtils.isNoneBlank(featuresAndPermissionsStr) && !"[]".equalsIgnoreCase(featuresAndPermissionsStr)) {
		return JSONToResult.convert(featuresAndPermissionsStr);
		}
		
		/**
		 * Seems currentLegalEntity is not set
		 */
		if(StringUtils.isBlank(legalEntityId)) {
			Result featureAndPermissions = new Result();
			diagnostic.prepareDebug("... loggedin user has not set current legal entity...").log();
			JSONArray permissionsArray = new JSONArray();
			permissionsArray.put("ALLOW");
			featureAndPermissions.addStringParam("permissions",
					permissionsArray.toString());
			return featureAndPermissions;
		}
		
		/**
		 * populates cache
		 */
		
		CustomerActionsBusinessDelegate businessDelegate =
		        DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
		
		boolean isProspect = HelperMethods.isProspectUserType(userInfo.get("CustomerType_id"));
		diagnostic.prepareDebug("current legal entity id {}, isProspect {} ", legalEntityId, isProspect).log();
		Map<String, Object> map = new HashMap<>();
		map.put(InfinityConstants.isProspectFlow, isProspect);
		
		Map<String, Set<String>> securityAttributes =
		        businessDelegate.getSecurityAttributes(userInfo.get("customer_id"),map,legalEntityId);
		
		Set<String> actions = securityAttributes.get("actions");
		Set<String> features = securityAttributes.get("features");

		if (null == actions || null == features) {
		    actions = new HashSet<>();
		    features = new HashSet<>();
		}
		/* adding view only role Start */
		String customerId=userInfo.get("customer_id");
		String roleId=getCustomerRole(customerId, null, null);
		diagnostic.prepareDebug("roleId in AuthUserManagementBusinessDelegateImpl:" + roleId).log();
		if(StringUtils.isNotBlank(roleId) && roleId.equalsIgnoreCase(HBL_VIEW_ONLY_ROLE_ID)) {
		features.add("VIEW_ONLY_ROLE");
		}
		/* adding view only role END */
		Result featureAndPermissions = new Result();
		featureAndPermissions.addStringParam("permissions", JSONUtil.getJSONString(actions));
		featureAndPermissions.addStringParam("features", JSONUtil.getJSONString(features));
		featuresAndPermissionsStr = ResultToJSON.convert(featureAndPermissions);
		int EXPIRY_TIME = HelperMethods.getExpiryTime("FEATURE_PERMISSIONS_EXPIRY_TIME");
		diagnostic.prepareDebug("before saving current leid permissions::" + featuresAndPermissionsStr).log();
		MemoryManager.saveIntoCache(cacheKey, featuresAndPermissionsStr, EXPIRY_TIME);
		diagnostic.prepareDebug("saving current leid permissions::" + featuresAndPermissionsStr).log();
		return featureAndPermissions;
	}
	public String getCustomerRole(String customerId,String contractId, String coreCustomerId) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		
		String serviceId = ServiceId.DBPRBLOCALSERVICEDB;
		String operationId = OperationName.DB_CUSTOMERGROUP_GET;
		
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
			if(StringUtils.isNotBlank(contractId)) {
			filter=filter+DBPUtilitiesConstants.AND +"contractId" + DBPUtilitiesConstants.EQUAL + contractId;
			}
			if(StringUtils.isNotBlank(coreCustomerId)) {
			filter=filter+DBPUtilitiesConstants.AND +"coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
			}
		
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String userRoleResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceId).
					withObjectId(null).
					withOperationId(operationId).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject userRoleResponseJSON = new JSONObject(userRoleResponse);
			JSONArray customerGroupArray = CommonUtils.getFirstOccuringArray(userRoleResponseJSON);
			
			if(customerGroupArray != null && customerGroupArray.length() > 0) {
				JSONObject customerGroup = (JSONObject) customerGroupArray.get(0);
				String roleId = customerGroup.optString("Group_id");
				diagnostic.prepareDebug("AuthUserManagementBusinessDelegateImplExtn fetching user role Id:", roleId).log();
				return roleId;
			}
			
		} catch (Exception e) {
			diagnostic.prepareDebug("AuthUserManagementBusinessDelegateImplExtn Exception caught while fetching user role", e).log();
			return null;
		}
		return null;
	}

}

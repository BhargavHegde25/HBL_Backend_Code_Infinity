
package com.kony.adminconsole.service.authmodule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.dto.InternalUser;
import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.InternalUserHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.handler.RoleHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch User Profile and Security Attributes
 * 
 * @author Sri kavya Pitchika
 *
 */
public class KeyCloakSecurityAttributesService implements JavaService2 {

	private static final String USER_TYPE = "TYPE_ID_CUSTOMER360";

	private static final int SESSTION_TTL = -1;

	private static final String USERNAME_PARAM = "username";
	private static final String ROLES_PARAM = "roles";

	public static final String USER_ID_KEY = "user_id";
	public static final String ID_KEY = "id";
	public static final String USERNAME_KEY = "username";
	
	public static final String LOBNAME_KEY = "lobName"; 
	public static final String LOBID_KEY = "lobId";
	
	public static final String COMPANYLEGALUNIT_KEY = "legalEntityId";
	public static final String ROLETOLEMAPPING_KEY = "RoleToLEMapping";

	public static final String ROLE_ID_KEY = "roleId";
	public static final String USER_ROLE_KEY = "UserRole";

	public static final String EMAIL_KEY = "Email";
	public static final String EMAIL = "email";
	
	public static final String FIRST_NAME_KEY = "FirstName";
	public static final String FIRST_NAME_KEY_ = "first_name";

	public static final String LAST_NAME_KEY = "LastName";
	public static final String LAST_NAME_KEY_ = "LastName";
	
	public static final String GIVEN_NAME_KEY = "given_name";
	public static final String FAMILY_NAME_KEY = "family_name";
	
    public static final String APP_ID = "appId";
    public static final String SPOTLIGHT = "spotlight";
    public static final String ENDPOINT = "permissionsEndpoint";
    public static final String PERMISSIONS_ENDPOINT = "UserManagement/getUserPermissions";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		try {
			Result result = new Result();
			String username = requestInstance.getParameter(USERNAME_PARAM);
			String roles = requestInstance.getParameter(ROLES_PARAM);
			boolean isAuthenticated = BooleanUtils
					.toBoolean((String) requestInstance.getAttribute(FabricConstants.IS_AUTHENTICATED_KEY));
			String roleIds = getRoleIds(roles, requestInstance);
			if (StringUtils.isNotBlank(username) && isAuthenticated) {

				/* Fetch user attributes */
				diagnostic.prepareDebug("Constructing user attributes").log();
				result = getUserAttributes(roleIds, requestInstance);
				/* Fetch security attributes */
				diagnostic.prepareDebug("Constructing security attributes").log();
				
//				if (StringUtils.isNotBlank(roleIds)) {
//					List<Permission> permissionsList = PermissionHandler.getRolesGrantedPermissions(roleIds, requestInstance);
//					JSONArray permissionsArray = new JSONArray();
//					if (permissionsList != null && !permissionsList.isEmpty()) {
//						for (Permission permission : permissionsList) {
//							if(!(permissionsArray.toList().contains(permission.getName()))) {
//							permissionsArray.put(permission.getName());
//							}
//						}
//					}
//		           result.addParam(new Param(DBPConstants.PERMISSIONS_IDENTITY_KEY,
//					       permissionsArray.toString(), FabricConstants.STRING));  
//				}
				AuditHandler.auditAdminActivity(requestInstance, username, roles,
	                    ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.SUCCESSFUL, "Login successful");
			}
			else {
				AuditHandler.auditAdminActivity(requestInstance, username, roles,
                        ModuleNameEnum.LOGIN, EventEnum.LOGIN, ActivityStatusEnum.FAILED, "Login failed!");
			}
			return result;

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20008.setErrorCode(errorResult);
			return errorResult;
		}
	}

	/**
	 * Method to get the user attributes
	 * 
	 * @param username
	 * @param requestInstance
	 * @return Record
	 * @throws ApplicationException
	 * @throws IOException
	 */
	public Result getUserAttributes(String roleIds, DataControllerRequest requestInstance)
			throws ApplicationException, IOException {

		Record userAttributesRecord = new Record();
		Result result = new Result();
		userAttributesRecord.setId(FabricConstants.USER_ATTRIBUTES);

		/* set last name */
		String firstName = StringUtils.isBlank(requestInstance.getParameter(GIVEN_NAME_KEY)) ? StringUtils.EMPTY
				: requestInstance.getParameter(GIVEN_NAME_KEY);
		userAttributesRecord.addParam(new Param(FIRST_NAME_KEY, firstName, FabricConstants.STRING));
		userAttributesRecord.addParam(new Param(FIRST_NAME_KEY_, firstName, FabricConstants.STRING));

		/* set last name */
		String lastName = StringUtils.isBlank(requestInstance.getParameter(FAMILY_NAME_KEY)) ? StringUtils.EMPTY
				: requestInstance.getParameter(FAMILY_NAME_KEY);
		userAttributesRecord.addParam(new Param(LAST_NAME_KEY, lastName, FabricConstants.STRING));
		userAttributesRecord.addParam(new Param(LAST_NAME_KEY_, lastName, FabricConstants.STRING));
		
		/* set app id */
		userAttributesRecord.addParam(new Param(APP_ID, SPOTLIGHT, FabricConstants.STRING));
		
		/* set username */
		String username = StringUtils.isBlank(requestInstance.getParameter(USERNAME_PARAM)) ? StringUtils.EMPTY
				: requestInstance.getParameter(USERNAME_PARAM);
		userAttributesRecord.addParam(new Param(USERNAME_KEY, username, FabricConstants.STRING));

		/* set userId */
		String user_id = StringUtils.isBlank(requestInstance.getParameter(USER_ID_KEY)) ? StringUtils.EMPTY
				: requestInstance.getParameter(USER_ID_KEY);
		userAttributesRecord.addParam(new Param(USER_ID_KEY, user_id, FabricConstants.STRING));
		userAttributesRecord.addParam(new Param(ID_KEY, user_id, FabricConstants.STRING));
		
		if(StringUtils.isNotBlank(user_id)) {	
		    readInternalUser(user_id,  requestInstance, userAttributesRecord);
		}
		/* set email address */
		String email = StringUtils.isBlank(requestInstance.getParameter(EMAIL)) ? StringUtils.EMPTY
				: requestInstance.getParameter(EMAIL);
		userAttributesRecord.addParam(new Param(EMAIL_KEY, email, FabricConstants.STRING));
		
		String[] roleID= roleIds.split(",");
		List<String> Status_ids = new ArrayList<String> ();
		Set<String> Role_ids = new HashSet<String>();
        if(roleID.length>0)
        {
    		JSONArray details = getRoleDetails(roleID,requestInstance);
        	for(int i=0;i<details.length();i++) { 
        		Object Status_idobj = ((JSONObject) details.get(i)).get("Status_id");
        			Status_ids.add(Status_idobj.toString());
        			if(Status_idobj.equals("SID_ACTIVE")) {
        				Object Role_idobj =((JSONObject) details.get(i)).get("id");
        				Role_ids.add(Role_idobj.toString());
        			}
        			else {
        				details.remove(i);
        				i--;
        			}
        	}
			if(!(Status_ids.contains("SID_ACTIVE"))) {
				ErrorCodeEnum.ERR_20101.setErrorCode(result);
				result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE,
	                    Integer.toString(HttpServletResponse.SC_UNAUTHORIZED), FabricConstants.INT));
				return result;
			}
        	getUnionOfCompanyLegalUnitsForRole(details,userAttributesRecord);
			RoleToLEMapping(details,userAttributesRecord);
        }
        
		/* set role Id */
		String roleId = StringUtils.isBlank(roleIds) ? StringUtils.EMPTY : Role_ids.toString().replace("[", "").replace("]", "");
		if(StringUtils.isNotBlank(roleId))
		{
			roleId = roleId.replaceAll("\\s","");
		}
        userAttributesRecord.addParam(new Param(ROLE_ID_KEY, roleId, FabricConstants.STRING));
        
        userAttributesRecord.addParam(new Param(ENDPOINT, PERMISSIONS_ENDPOINT, FabricConstants.STRING));

        /*Add permissions to cache*/
        LoggedInUserHandler.savePermissionsToCache(requestInstance, user_id, roleId);
		/* set role name */
		String roleName = StringUtils.isBlank(requestInstance.getParameter(ROLES_PARAM)) ? StringUtils.EMPTY
				: requestInstance.getParameter(ROLES_PARAM);
		userAttributesRecord.addParam(new Param(USER_ROLE_KEY, roleName, FabricConstants.STRING));

		/* Set App name */
		userAttributesRecord
				.addParam(new Param(DBPConstants.CUSTOMER_TYPE_ID_IDENTITY_KEY, USER_TYPE, FabricConstants.STRING));
		result.addRecord(userAttributesRecord);
		return result;
	}

	public String getRoleIds(String roles, DataControllerRequest requestInstance)
			throws ApplicationException, IOException {
		String roleIds = null;
		if (StringUtils.isNotBlank(roles)) {
			String[] roleNames = roles.split(",");
			List<String> ids = new ArrayList<String>();
			for (String name : roleNames) {
				String roleId = RoleHandler.getRoleIdFromName(name, requestInstance);
				if (roleId != null)
					ids.add(roleId);
			}
			if (ids.size() > 0) {
				roleIds = StringUtils.join(ids.toArray(), ',');
			}
		}
		return roleIds;
	}
	
	public void readInternalUser(String user_id, DataControllerRequest requestInstance,Record userAttributesRecord) {
		JSONObject details = new JSONObject();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + user_id + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERSKC_VIEW_READ, postParametersMap,
				null, requestInstance);
		JSONObject readInternalUserKCResponseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
		if (((JSONArray) readInternalUserKCResponseJSON.get("internaluserskc_view")).length() > 0) {
			details = (JSONObject) ((JSONArray) readInternalUserKCResponseJSON.get("internaluserskc_view"))
					.get(0);
		}
		String lobName = details.has("lobName")?details.getString("lobName"):"";
		String lobId = details.has("lobId")?details.getString("lobId"):"";
		userAttributesRecord.addParam(new Param(LOBNAME_KEY, lobName, FabricConstants.STRING));
		userAttributesRecord.addParam(new Param(LOBID_KEY, lobId, FabricConstants.STRING));
	}
	public static void getUnionOfCompanyLegalUnitsForRole(JSONArray details,Record userAttributesRecord)throws ApplicationException, IOException {
		Set<String> companyLegalUnits = new HashSet<String> (); 
		JSONObject info = new JSONObject();
		for(int i=0;i<details.length();i++)
		{
		info=(JSONObject) ((JSONArray) details)
				.get(i);
		String companyLegalUnit = info.has("companyLegalUnit")?info.getString("companyLegalUnit"):"";
		companyLegalUnits.add(companyLegalUnit);
		}
		String CompanyLegalUnit = String.join(",", companyLegalUnits);
		userAttributesRecord.addParam(new Param(COMPANYLEGALUNIT_KEY,CompanyLegalUnit,FabricConstants.STRING));

    }
    public static JSONArray getRoleDetails(String[] roleIds, DataControllerRequest requestInstance)
    {
    	String filterQuery = "";        
        filterQuery += "(" + "id" + " eq "
                + String.join(" or " + "id" + " eq ",
                        roleIds)
                + ")";
        Map<String, String> bodyMap = new HashMap<>();
		bodyMap.put(ODataQueryConstants.FILTER, filterQuery);
		String response = Executor.invokeService(ServiceURLEnum.ROLE_READ, bodyMap, null,
                requestInstance);
		JSONObject ResponseJSON = CommonUtilities.getStringAsJSONObject(response);
		JSONArray details = new JSONArray();
		details = ((JSONArray) ResponseJSON.get("role"));
		return details;
    }
    public static void RoleToLEMapping(JSONArray details, Record userAttributesRecord)
            throws ApplicationException, IOException {
		JSONObject info = new JSONObject();
		JSONObject RoletoLEMapping = new JSONObject();
		for(int i=0;i<details.length();i++)
		{
		info=(JSONObject) ((JSONArray) details)
				.get(i);
		String role =info.has("id")?info.getString("id"):"";
		String companyLegalUnit = info.has("companyLegalUnit")?info.getString("companyLegalUnit"):"";
		RoletoLEMapping.append(role, companyLegalUnit);
		String RoletoLEMappings=RoletoLEMapping.toString();
		userAttributesRecord.addParam(new Param(ROLETOLEMAPPING_KEY,RoletoLEMappings,FabricConstants.STRING));

		}	
    }
}



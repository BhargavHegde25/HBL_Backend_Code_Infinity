
package com.kony.adminconsole.service.authmodule;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.commons.utils.WriteToLog;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.dto.InternalUser;
import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.InternalUserHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
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
 * @author Aditya Mankal
 *
 */
public class UserAndSecurityAttributesService implements JavaService2 {

    private static final String USER_TYPE = "TYPE_ID_CUSTOMER360";

    private static final int SESSTION_TTL = -1;

    private static final String USERNAME_PARAM = "username";

    public static final String USER_ID_KEY = "user_id";
    public static final String USERNAME_KEY = "username";
    public static final String ID_KEY = "id";
    
    public static final String LOBNAME_KEY = "lobName";
    public static final String LOBID_KEY = "lobId";

    public static final String ROLE_ID_KEY = "roleId";
    
    public static final String USER_ROLE_KEY = "UserRole";
    
    public static final String ROLE_TO_LE_MAPPING_KEY = "RoleToLEMapping";
    

    public static final String EMAIL_KEY = "Email";
    public static final String COMPANYLEGALUNIT_KEY = "legalEntityId";
	public static final String ROLETOLEMAPPING_KEY = "RoleToLEMapping";

    public static final String FIRST_NAME_KEY = "FirstName";
    public static final String FIRST_NAME_KEY_ = "first_name";

    public static final String MIDDLE_NAME_KEY = "MiddleName";
    public static final String MIDDLE_NAME_KEY_ = "middle_name";

    public static final String LAST_NAME_KEY = "LastName";
    public static final String LAST_NAME_KEY_ = "LastName";

    public static final String CREATED_BY_KEY = "CreatedBy";
    public static final String MODIFIED_BY_KEY = "ModifiedBy";

    public static final String SYNC_TIME_STAMP_KEY = "SyncTimeStamp";
    public static final String LAST_MODIFIED_TIME_STAMP_KEY = "LastModifiedTimeStamp";

    public static final String SOFT_DELETE_FLAG_KEY = "SoftDeleteFlag";
    
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
            boolean isAuthenticated = BooleanUtils
                    .toBoolean((String) requestInstance.getAttribute(FabricConstants.IS_AUTHENTICATED_KEY));

            if (StringUtils.isNotBlank(username) && isAuthenticated) {

                /* Fetch user profile */
//                diagnostic.prepareDebug("Fetching user attributes").log();
                try {
					ThreadExecutor.execute(
							()-> WriteToLog.debug("Fetching user attributes")
							);
				} catch (InterruptedException e) {
					
					alert.prepareError("Error occurred: ", e).log();
				}
                InternalUser userProfile = InternalUserHandler.getUserProfile(username, requestInstance);

                /* Fetch user attributes */
//                diagnostic.prepareDebug("Constructing user attributes").log();
                try {
					ThreadExecutor.execute(
							()-> WriteToLog.debug("Constructing user attributes")
							);
				} catch (InterruptedException e) {
					
					alert.prepareError("Error occurred: ", e).log();
				}
                Record userAttributes = getUserAttributes(userProfile, requestInstance);
                result.addRecord(userAttributes);

                /* Fetch security attributes */
//                diagnostic.prepareDebug("Constructing security attributes").log();
                try {
					ThreadExecutor.execute(
							()-> WriteToLog.debug("Constructing security attributes")
							);
				} catch (InterruptedException e) {
					
					alert.prepareError("Error occurred: ", e).log();
				}
                String userId = userProfile != null ? userProfile.getId() : StringUtils.EMPTY;
                Record securityAttributes = getSecurityAttributes(userId, requestInstance);
                securityAttributes.addRecord(userAttributes);
                result.addRecord(securityAttributes);
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
     * Method to get the Security Attributes of a user
     * 
     * @param userId
     * @param requestInstance
     * @return
     * @throws ApplicationException
     * @throws IOException
     */
    public Record getSecurityAttributes(String userId, DataControllerRequest requestInstance)
            throws ApplicationException, IOException {

        Record securityAttributesRecord = new Record();
        securityAttributesRecord.setId(FabricConstants.SECURITY_ATTRIBUTES);
        
        if (StringUtils.isNotBlank(userId)) {
            List<Permission> permissionsList = PermissionHandler.getUserGrantedPermissions(userId, requestInstance);
            JSONArray permissionsArray = new JSONArray();
            if (permissionsList != null && !permissionsList.isEmpty()) {
                for (Permission permission : permissionsList) {
                    permissionsArray.put(permission.getName());
                }
            }
       
            securityAttributesRecord.addParam(new Param(DBPConstants.PERMISSIONS_IDENTITY_KEY,
                    permissionsArray.toString(), FabricConstants.STRING));	     
            securityAttributesRecord.addParam(new Param(ENDPOINT, PERMISSIONS_ENDPOINT, FabricConstants.STRING));
          
            securityAttributesRecord.addParam(
                    new Param(FabricConstants.SESSION_TTL, Integer.toString(SESSTION_TTL), FabricConstants.STRING));
            securityAttributesRecord.addParam(
                    new Param(FabricConstants.SESSION_TOKEN, UUID.randomUUID().toString(), FabricConstants.STRING));
        }

        return securityAttributesRecord;
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
    public Record getUserAttributes(InternalUser userProfile, DataControllerRequest requestInstance)
            throws ApplicationException, IOException {

        Record userAttributesRecord = new Record();
        userAttributesRecord.setId(FabricConstants.USER_ATTRIBUTES);
        if (userProfile != null) {
            /* set username */
            String username = StringUtils.isBlank(userProfile.getUsername()) ? StringUtils.EMPTY
                    : userProfile.getUsername();
            userAttributesRecord.addParam(new Param(USERNAME_KEY, username, FabricConstants.STRING));

            /* set userId */
            String userId = StringUtils.isBlank(userProfile.getUsername()) ? StringUtils.EMPTY : userProfile.getUsername();
            userAttributesRecord.addParam(new Param(USER_ID_KEY, userId, FabricConstants.STRING));
            /* set userId */
            String id = StringUtils.isBlank(userProfile.getId()) ? StringUtils.EMPTY : userProfile.getId();
            userAttributesRecord.addParam(new Param(ID_KEY, id, FabricConstants.STRING));
            
            /* set app id */
            userAttributesRecord.addParam(new Param(APP_ID, SPOTLIGHT, FabricConstants.STRING));
            
            if(StringUtils.isNotBlank(id)) {	
    		    readInternalUser(id,  requestInstance, userAttributesRecord);
    		}


            /* set email address */
            String email = StringUtils.isBlank(userProfile.getEmail()) ? StringUtils.EMPTY : userProfile.getEmail();
            userAttributesRecord.addParam(new Param(EMAIL_KEY, email, FabricConstants.STRING));

            /* set role Id */
            String roleId = StringUtils.isBlank(userProfile.getRoleId()) ? StringUtils.EMPTY : userProfile.getRoleId();
            userAttributesRecord.addParam(new Param(ROLE_ID_KEY, roleId, FabricConstants.STRING));
            String[] roleIds=null;
            roleIds = roleId.split(",");
            if(roleIds.length>0)
            {
            	LoggedInUserHandler.savePermissionsToCache(requestInstance, userId, roleId);
            	getUnionOfCompanyLegalUnitsForRole(roleIds,requestInstance,userAttributesRecord);
				RoleToLEMapping(roleIds,requestInstance,userAttributesRecord);
            }
            
            /* set role name */
            String roleName = StringUtils.isBlank(userProfile.getRoleName()) ? StringUtils.EMPTY
                    : userProfile.getRoleName();
            userAttributesRecord.addParam(new Param(USER_ROLE_KEY, roleName, FabricConstants.STRING));

            /* set first name */
            String firstName = StringUtils.isBlank(userProfile.getFirstName()) ? StringUtils.EMPTY
                    : userProfile.getFirstName();
            userAttributesRecord.addParam(new Param(FIRST_NAME_KEY, firstName, FabricConstants.STRING));
            userAttributesRecord.addParam(new Param(FIRST_NAME_KEY_, firstName, FabricConstants.STRING));

            /* set middle name */
            String middleName = StringUtils.isBlank(userProfile.getMiddleName()) ? StringUtils.EMPTY
                    : userProfile.getMiddleName();
            userAttributesRecord.addParam(new Param(MIDDLE_NAME_KEY, middleName, FabricConstants.STRING));
            userAttributesRecord.addParam(new Param(MIDDLE_NAME_KEY_, middleName, FabricConstants.STRING));

            /* set last name */
            String lastName = StringUtils.isBlank(userProfile.getLastName()) ? StringUtils.EMPTY
                    : userProfile.getLastName();
            userAttributesRecord.addParam(new Param(LAST_NAME_KEY, lastName, FabricConstants.STRING));
            userAttributesRecord.addParam(new Param(LAST_NAME_KEY_, lastName, FabricConstants.STRING));

            /* set sync timestamp */
            String syncTimeStamp = CommonUtilities.convertTimetoISO8601Format(userProfile.getSyncTime());
            userAttributesRecord.addParam(new Param(SYNC_TIME_STAMP_KEY, syncTimeStamp, FabricConstants.STRING));

            /* Set createdby */
            String createdBy = StringUtils.isBlank(userProfile.getCreatedby()) ? StringUtils.EMPTY
                    : userProfile.getCreatedby();
            userAttributesRecord.addParam(new Param(CREATED_BY_KEY, createdBy, FabricConstants.STRING));

            /* Set modifiedby */
            String modifiedby = StringUtils.isBlank(userProfile.getModifiedby()) ? StringUtils.EMPTY
                    : userProfile.getCreatedby();
            userAttributesRecord.addParam(new Param(MODIFIED_BY_KEY, modifiedby, FabricConstants.STRING));

            /* Set last modified timestamp */
            String lastModifiedTimeStamp = CommonUtilities
                    .convertTimetoISO8601Format(userProfile.getLastModifiedTime());
            userAttributesRecord
                    .addParam(new Param(LAST_MODIFIED_TIME_STAMP_KEY, lastModifiedTimeStamp, FabricConstants.STRING));

            /* Set soft delete flag */
            String softDeleteFlag = String.valueOf(userProfile.isSoftDeleteFlag());
            userAttributesRecord.addParam(new Param(SOFT_DELETE_FLAG_KEY, softDeleteFlag, FabricConstants.STRING));

            /* Set App name */
            userAttributesRecord
                    .addParam(new Param(DBPConstants.CUSTOMER_TYPE_ID_IDENTITY_KEY, USER_TYPE, FabricConstants.STRING));
        }
        return userAttributesRecord;
    }
    
    public void readInternalUser(String userId, DataControllerRequest requestInstance,Record userAttributesRecord) {
		JSONObject details = new JSONObject();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userId + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ, postParametersMap,
				null, requestInstance);
		JSONObject readInternalUserKCResponseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
		if (((JSONArray) readInternalUserKCResponseJSON.get("internalusers_view")).length() > 0) {
			details = (JSONObject) ((JSONArray) readInternalUserKCResponseJSON.get("internalusers_view"))
					.get(0);
		}
		String lobName = details.has("lobName")?details.getString("lobName"):"";
		String lobId = details.has("lobId")?details.getString("lobId"):"";
		userAttributesRecord.addParam(new Param(LOBNAME_KEY, lobName, FabricConstants.STRING));
		userAttributesRecord.addParam(new Param(LOBID_KEY, lobId, FabricConstants.STRING));
	}
     public static void getUnionOfCompanyLegalUnitsForRole(String[] roleIds, DataControllerRequest requestInstance,Record userAttributesRecord)
            throws ApplicationException, IOException {
		JSONArray details =getRoleDetails(roleIds,requestInstance);
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
    public static void RoleToLEMapping(String[] roleIds, DataControllerRequest requestInstance,Record userAttributesRecord)
            throws ApplicationException, IOException {
		JSONArray details =getRoleDetails(roleIds,requestInstance);
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

package com.kony.adminconsole.core.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.SessionMemoryManager;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.service.authmodule.UserAndSecurityAttributesService;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.registry.AppRegistryException;

/**
 * <p>
 * Handler class to Fetch Logged In User Profile
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class LoggedInUserHandler {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    /**
     * <p>
     * Get logged-in user details
     * </p>
     * 
     * @param serviceManager
     * @return logged-in user details
     * @throws ApplicationException
     */
    @SuppressWarnings("unchecked")
	public static UserDetailsBean getUserDetails(ServicesManager serviceManager) throws ApplicationException {

        try {
            diagnostic.prepareDebug("Constructing Logged-In User DTO").log();
            Map<String, Object> userAttributes = new HashMap<String, Object>();
            // Read User Attributes
            //Map<String, Object> userAttributes = serviceManager.getIdentityHandler().getUserAttributes();
           
            if(serviceManager.getIdentityHandler().getSecurityAttributes() != null && serviceManager.getIdentityHandler().getSecurityAttributes().
            		  get(FabricConstants.USER_ATTRIBUTES) != null)
            {
            	
			  userAttributes = (Map<String, Object>) (serviceManager.getIdentityHandler().getSecurityAttributes().
            		  get(FabricConstants.USER_ATTRIBUTES));
            }else{
		      userAttributes = serviceManager.getIdentityHandler().getUserAttributes();
			}
            String userId = userAttributes.get(UserAndSecurityAttributesService.USER_ID_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.USER_ID_KEY).toString()
                    : StringUtils.EMPTY;
            String id = userAttributes.get(UserAndSecurityAttributesService.ID_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.ID_KEY).toString()
                    : StringUtils.EMPTY;
            String username = userAttributes.get(UserAndSecurityAttributesService.USERNAME_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.USERNAME_KEY).toString()
                    : StringUtils.EMPTY;
            String emailId = userAttributes.get(UserAndSecurityAttributesService.EMAIL_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.EMAIL_KEY).toString()
                    : StringUtils.EMPTY;

            String roleId = userAttributes.get(UserAndSecurityAttributesService.ROLE_ID_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.ROLE_ID_KEY).toString()
                    : StringUtils.EMPTY;

            String roleName = userAttributes.get(UserAndSecurityAttributesService.USER_ROLE_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.USER_ROLE_KEY).toString()
                    : StringUtils.EMPTY;

            String firstName = userAttributes.get(UserAndSecurityAttributesService.FIRST_NAME_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.FIRST_NAME_KEY).toString()
                    : StringUtils.EMPTY;

            String middleName = userAttributes.get(UserAndSecurityAttributesService.MIDDLE_NAME_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.MIDDLE_NAME_KEY).toString()
                    : StringUtils.EMPTY;

            String lastName = userAttributes.get(UserAndSecurityAttributesService.LAST_NAME_KEY) != null
                    ? userAttributes.get(UserAndSecurityAttributesService.LAST_NAME_KEY).toString()
                    : StringUtils.EMPTY;
                    
            String roleToLEMapping = userAttributes.get(UserAndSecurityAttributesService.ROLE_TO_LE_MAPPING_KEY) != null
                   ? userAttributes.get(UserAndSecurityAttributesService.ROLE_TO_LE_MAPPING_KEY).toString()
                   : StringUtils.EMPTY;

            boolean isAPIUser = StringUtils.equals(id, APICustomIdentityService.API_USER_ID);
            List<String> authData = new ArrayList<>();

            // Construct DTO
            UserDetailsBean userDetails = new UserDetailsBean(id, userId, username, emailId, firstName, middleName,
                    lastName, roleName, roleId, isAPIUser, authData,roleToLEMapping);

            // Return DTO
            diagnostic.prepareDebug("Constructed Logged-In User DTO. Returning response").log();
            return userDetails;

        } catch (Exception e) {
            alert.prepareError("Exception in constructing logged-in user DTO. Exception:", e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20008);
        }
    }

    /**
     * <p>
     * Get logged-in user details
     * </p>
     * 
     * @param requestInstance
     * @return logged-in user details
     * @throws ApplicationException
     */
    public static UserDetailsBean getUserDetails(DataControllerRequest requestInstance) throws ApplicationException {
        try {
            return getUserDetails(requestInstance.getServicesManager());
        } catch (Exception e) {
        	alert.prepareError("Exception in constructing logged-in user DTO. Exception:", e).log();
        	
        	try {
        	return getUserDetailsFromAuthToken(requestInstance);
        	} catch (Exception exp) {
	            alert.prepareError("Exception in fetching logged-in user DTO by authToken. Exception:", exp).log();
	            throw new ApplicationException(ErrorCodeEnum.ERR_20008);
        	}
        }
    }
    
    public static UserDetailsBean getUserDetailsFromAuthToken(DataControllerRequest requestInstance) throws Exception {
    	
    	String authToken = CommonUtilities.getAuthToken(requestInstance);
        if(StringUtils.isBlank(authToken)) {
    		throw new ApplicationException(ErrorCodeEnum.ERR_20000);
    	}
        
        Map<String, String> postParametersMap = new HashMap<>();

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Kony-Authorization", authToken);
        
        String responseStr = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_GET,
                postParametersMap, headerMap, requestInstance);
        
        JSONObject response = CommonUtilities.getStringAsJSONObject(responseStr);
        String username = response.getString("username");
        String user_id = response.getString("user_id");
		String id = response.getString("id");
        String UserRole = response.getString("UserRole");
        String roleId = response.getString("roleId");
        String FirstName = response.getString("FirstName");
        String LastName = response.getString("LastName");
        String Email = response.getString("Email");
        String roleToLEMapping = response.getString("RoleToLEMapping");
        
        boolean isAPIUser = StringUtils.equals(user_id, APICustomIdentityService.API_USER_ID);
        List<String> authData = new ArrayList<>();
        
        UserDetailsBean userDetails = new UserDetailsBean(id, user_id, username, Email, FirstName, "",
        		LastName, UserRole, roleId, isAPIUser, authData,roleToLEMapping);
        
        return userDetails;
        
    }
    
    public static boolean hasAccessToLegalEntity(DataControllerRequest request, String[] permissionInput) throws Exception {
        JSONObject userPermissions = new JSONObject();
        String userLEIdFromUserAttributes = null;
        Object loggedInUserLegalEntityID;
        boolean isKeyCloakEnabled=Boolean.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(request));
        //Getting legalEntityId of a user from userattributes.
        String User_id = LoggedInUserHandler.getUserDetails(request).getUserId();
        if(User_id .equals(APICustomIdentityService.API_USER_ID)) {
        	return true;
        }
        if(isKeyCloakEnabled){
        	JSONObject obj = CommonUtilities.getStringAsJSONObject(request.getServicesManager().getIdentityHandler()
            		.getSecurityAttributes().get("raw_response").toString());
        	loggedInUserLegalEntityID = CommonUtilities.getStringAsJSONObject(obj.get("user_attributes")
            		.toString()).get("legalEntityId");
        } 
        else{
        loggedInUserLegalEntityID = request.getServicesManager().getIdentityHandler().getUserAttributes()
                .get("legalEntityId");
        }
        
       if (loggedInUserLegalEntityID == null || loggedInUserLegalEntityID == "") {
            return false;
        } else {
            userLEIdFromUserAttributes = loggedInUserLegalEntityID.toString();
        }
        
        //If the logged in user has LE has ALL give him all access -this is for KonyBankingAdminConsoleAPIIdentityService
        if (userLEIdFromUserAttributes.equals("ALL")) {
            return true;
        }
		String legalEntityIdFromReq;
		if (!StringUtils.isBlank(request.getParameter("addedlegalEntityIdValue"))) {
			legalEntityIdFromReq = request.getParameter("addedlegalEntityIdValue");
		} else {
			legalEntityIdFromReq = getLegalEntityIdFromRequest(request);
		}
        List<String> loggedInUserLegalEntityIds = Arrays.asList(userLEIdFromUserAttributes.split(","));
        
        // Checking whether the LE sent in req is SHARED
        if(loggedInUserLegalEntityIds.size() > 0 && ACConstants.SHARED_LEGAL_ENTITY_ID.equalsIgnoreCase(legalEntityIdFromReq)) {
        	return true;
        }
        
        //Checking whether the user has the access to the LE sent in req
        if (!loggedInUserLegalEntityIds.contains(legalEntityIdFromReq)) {
            return false;
        }
        
        userPermissions = getUserPermissionsFromCache(request);
        
        String permissions = userPermissions.has(legalEntityIdFromReq)? userPermissions.getString(legalEntityIdFromReq):null;
        //If the user does not have any permissions in db then it will return false
        if(permissions==null)
            return false;
        
        List<String> permissionsList = Arrays.asList(permissions.split(","));
        List<String> requiredPermisssionsList = Arrays.asList(permissionInput);
        //If the api has permission as allow then return true
        if (requiredPermisssionsList.contains("ALLOW")) {
                    return true;
                }
        for (int i = 0; i < requiredPermisssionsList.size(); i++) {
            if (permissionsList.contains(requiredPermisssionsList.get(i))) {
                return true;
            }
        }
        
        return false;
    }



   private static JSONObject getUserPermissionsFromCache(DataControllerRequest request)
            throws MiddlewareException, AppRegistryException, ApplicationException, IOException {
        JSONObject userPermissions;
		String userPermissionsString;
        //Getting session_token of a user from security attributes to fetch permissions from cache
        Object session_id = request.getSession().getId();
        if(session_id == "" || session_id == null) {
        	String roles = getUserDetails(request.getServicesManager()).getRoleId();
            userPermissionsString = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roles, request).getString("legalEntityToRoleMapping");
            userPermissions = CommonUtilities.getStringAsJSONObject(userPermissionsString);
        }
        else {
        	
    	String userid  = null;
		if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
			userid = LoggedInUserHandler.getUserDetails(request).getId();
        }
		
        //Getting permissions from cache
		Object fromCache = MemoryManager.getFromCache("legalEntityToRoleMapping_" + userid);
        
        //If the cache is null or empty call getRolesGrantedPermissionsWithLEInfo service.
        if (fromCache == "" || fromCache == null) {
            String roles = LoggedInUserHandler.getUserDetails(request.getServicesManager()).getRoleId();
            userPermissionsString = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roles, request).getString("legalEntityToRoleMapping");
            userPermissions = CommonUtilities.getStringAsJSONObject(userPermissionsString);
            
        } else {
            userPermissions = CommonUtilities.getStringAsJSONObject(fromCache.toString());
            
        }
        }
        return userPermissions;
    }


   private static String getLegalEntityIdFromRequest(DataControllerRequest request) {
        HashMap<String, String> queryParamsMap = new HashMap<String, String>();
        queryParamsMap = (HashMap<String, String>) request.getAttribute("queryparams");        
        String legalEntityIdFromReq;
        if (queryParamsMap!= null && queryParamsMap.containsKey("legalEntityId")) {
            legalEntityIdFromReq = queryParamsMap.get("legalEntityId");
        }
        else
        	{
        	if(StringUtils.isBlank(request.getParameter("legalEntityId")))
        		//Getting legalEntityId from Request
        		legalEntityIdFromReq = request.getParameter("_legalEntityId");
        	else
                legalEntityIdFromReq = request.getParameter("legalEntityId");

        }
        return legalEntityIdFromReq;
    }
   
   public static Set<String> getLoggedInUserPermissions(DataControllerRequest request)
		   throws MiddlewareException, AppRegistryException, ApplicationException, IOException 
   {
	   String userPermissionsString;
	   Set<String> userPermissions = new HashSet<String>();
	   String roles = getUserDetails(request.getServicesManager()).getRoleId();
	   String userid = LoggedInUserHandler.getUserDetails(request).getUserId();
	   Object fromCache = MemoryManager.getFromCache("spotlight_"+userid+"_permissions");
	   if (fromCache != null && StringUtils.isNotBlank(fromCache.toString())) {
		   userPermissionsString = fromCache.toString();
	   } else {
		   userPermissionsString = savePermissionsToCache(request, userid, roles);
	   }
	   if (StringUtils.isNotBlank(userPermissionsString)) {
   		JSONArray jsonArray = new JSONArray(userPermissionsString);
   		if (jsonArray.length() > 0) {
   			for(int i=0;i<jsonArray.length();i++)
   			{
   				userPermissions.add(jsonArray.getString(i));
   		}
   	}
   }
	return userPermissions;
   }

   public static String savePermissionsToCache(DataControllerRequest request, String userId,String roleId) throws ApplicationException, IOException
   {
	   try {
		   JSONObject responseObject = PermissionHandler.getRolesGrantedPermissionsWithLEInfo(roleId, request);
		   MemoryManager.saveIntoCache("spotlight_"+userId+"_permissions", responseObject.getString("permissions"));
		   String permissions = responseObject.getString("permissions");
		   return permissions;
	   }
	   catch(Exception e)
	   {
		   
		   alert.prepareError("Error while saving permissions into Cache").log();
		   return null;
	   }
   }
}

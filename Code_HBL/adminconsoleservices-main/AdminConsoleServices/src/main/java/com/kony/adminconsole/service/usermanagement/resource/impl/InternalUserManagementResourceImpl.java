package com.kony.adminconsole.service.usermanagement.resource.impl;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.dto.Action;
import com.kony.adminconsole.dto.AddressBean;
import com.kony.adminconsole.dto.Permission;
import com.kony.adminconsole.dto.SystemUserBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.ActionHandler;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.EmailHandler;
import com.kony.adminconsole.handler.PermissionHandler;
import com.kony.adminconsole.service.usermanagement.businessdelegate.api.InternalUserManagementBusinessDelegate;
import com.kony.adminconsole.service.usermanagement.resource.api.InternalUserManagementResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.StatusEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author Rishi.Gupta
 *
 */
public class InternalUserManagementResourceImpl implements InternalUserManagementResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String INPUT_ID = "id";
	private static final String INPUT_USER_DETAILS = "userDetails";
	private static final String INPUT_COMPANY_LIST = "companyList";
	private static final String INPUT_ACCOUNT_LEVEL_PERMISSIONS = "accountLevelPermissions";
	private static final String INPUT_GLOBAL_LEVEL_PERMISSIONS = "globalLevelPermissions";
	private static final String INPUT_TRANSACTION_LIMITS = "transactionLimits";
	private static final String INPUT_CONTRACT_DETAILS = "contractDetails";
	private static final String INPUT_USERID = "userId";
	private static final String INPUT_SIGNATORY_GROUP = "signatoryGroups";
	private static final String SOFT_DELETE_FLAG = "0";
	private JSONArray roleDetailsArray = null;
	private JSONArray responseStatusJSONArray = null;
	private JSONArray responseUserJSONArray = null;
	InternalUserManagementBusinessDelegate InternalUserManagementBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(InternalUserManagementBusinessDelegate.class);

	@Override
	public Result getInternalUser(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		try {
			if (request.getParameter(INPUT_ID) == null) {
				ErrorCodeEnum.ERR_21991.setErrorCode(result);
				return result;
			} else {
				String userId = request.getParameter(INPUT_ID);
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("id", userId);
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
				JSONObject getCustomerresponse = InternalUserManagementBusinessDelegate
						.getInternalUser(postParametersMap, dbpServicesClaimsToken);
				if (getCustomerresponse == null || !getCustomerresponse.has(FabricConstants.OPSTATUS)
						|| getCustomerresponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_21992.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "Get infinity user failed");
					return result;
				} else if (getCustomerresponse.has("dbpErrMsg")) {
					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {

					result = CommonUtilities.constructResultFromJSONObject(getCustomerresponse);

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL, "Successfully fetched infinity user details. id= " + userId);

				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in get infinity user details", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_21993.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.CUSTOMERS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Infinity user details parse Failed");
		}
		return result;
	}

	@Override
	public Result createInternalUser(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {

		Result result = new Result();
		Param statusParam;
		SystemUserBean sysUserBean = new SystemUserBean();
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> input = (HashMap<String, String>) inputArray[1];
			String message = "";
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			if (CommonUtilities.containSpecialChars(input.get("Status_id"))) {
				message = "Status id cannot contain special characters ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			} else {
				sysUserBean.setStatus_id(String.valueOf(input.get("Status_id")));
			}

			input.put("Username", StringEscapeUtils.escapeHtml(input.get("Username")));
			if (CommonUtilities.containSpecialChars(input.get("Username"))) {
				message = "Username cannot contain special characters ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			} else {
				// sysUserBean.setUsername(String.valueOf(input.get("Username")));
				sysUserBean.setUsername(String
						.valueOf(CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(input.get("Username"))));
			}

			if (CommonUtilities.containSpecialChars(input.get("Email"))) {
				message = "Email cannot contain special characters like +,-,= ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			}
			sysUserBean.setEmail(String.valueOf(input.get("Email")));
			if (CommonUtilities.containSpecialChars(input.get("FirstName"))) {
				message = "FirstName cannot contain special characters ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			}
			sysUserBean.setFirstName(String.valueOf(input.get("FirstName")));

			if (StringUtils.isNotBlank(input.get("MiddleName"))) {

				if (CommonUtilities.containSpecialChars(input.get("MiddleName"))) {
					message = "MiddleName cannot contain special characters ";
					result.addParam(new Param("Error message", message, FabricConstants.STRING));
					return result;
				}
				sysUserBean.setMiddleName(input.get("MiddleName").toString());
			}
			if (CommonUtilities.containSpecialChars(input.get("LastName"))) {
				message = "LastName cannot contain special characters ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			}
			sysUserBean.setLastName(String.valueOf(input.get("LastName")));
			if (CommonUtilities.containSpecialChars(input.get("currUser"))) {
				message = "currUser cannot contain special characters ";
				result.addParam(new Param("Error message", message, FabricConstants.STRING));
				return result;
			}
			sysUserBean.setCurrUser(String.valueOf(input.get("currUser")));

			// ** Checking if username or email exists in backend **
			Map<String, String> systemuserTableMap = new HashMap<String, String>();
			systemuserTableMap.put(ODataQueryConstants.SELECT, "UserID");
			systemuserTableMap.put(ODataQueryConstants.FILTER,
					"Username eq '" + sysUserBean.getUsername() + "' or Email eq '" + sysUserBean.getEmail() + "'");

			String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_VIEW_READ,
					systemuserTableMap, null, requestInstance);
			JSONObject readSystemUserResponseJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);

			if (readSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readSystemUserResponseJSON.getJSONArray("systemuser_view").length() == 0) {

				Param userResponse = createInternalUser(sysUserBean, authToken, requestInstance);
				JSONArray newPermissions = new JSONArray(requestInstance.getParameter("permission_ids"));
				if (!StringUtils.equals(userResponse.getValue(), "ERROR")) {
					Param userStatusParam = new Param("UserStatus", "Success", FabricConstants.STRING);
					result.addParam(userStatusParam);
					Param userTypeRespose = createUserType(userResponse, input, authToken, requestInstance);
					if (!StringUtils.equals(userTypeRespose.getValue(), "ERROR")) {
						Param userTypeStatusParam = new Param("userTypeStatus", "Success", FabricConstants.STRING);
						result.addParam(userTypeStatusParam);
					} else {
						Param userTypeStatusParam = new Param("userTypeStatus", "Error While Creation",
								FabricConstants.STRING);
						result.addParam(userTypeStatusParam);
					}

					Param userManagerRespose = createUserManager(userResponse, input, authToken, requestInstance);
					if (!StringUtils.equals(userManagerRespose.getValue(), "ERROR")) {
						Param userManagerStatusParam = new Param("userManagerStatus", "Success",
								FabricConstants.STRING);
						result.addParam(userManagerStatusParam);
					} else {
						Param userManagerStatusParam = new Param("userManagerStatus", "Error While Creation",
								FabricConstants.STRING);
						result.addParam(userManagerStatusParam);
					}

					Param userlobResponse = createUserLob(userResponse, input, authToken, requestInstance);
					if (!StringUtils.equals(userlobResponse.getValue(), "ERROR")) {
						Param userLobStatusParam = new Param("UserLobStatus", "Success", FabricConstants.STRING);
						result.addParam(userLobStatusParam);
						JSONObject getBranchAddr = getBranchAddr(authToken, String.valueOf(input.get("WorkID")),
								requestInstance);
						String WorkID = ((JSONObject) ((JSONArray) getBranchAddr.get("location")).get(0))
								.getString("Address_id");
						Param addressRespose = createUserAddress(userResponse.getValue(), WorkID, "ADR_TYPE_WORK",
								sysUserBean, authToken, requestInstance);
						if (!StringUtils.equals(addressRespose.getValue(), "ERROR")) {
							Param userAddressStatusParam = new Param("userAddressStatus", "Success",
									FabricConstants.STRING);
							result.addParam(userAddressStatusParam);
						} else {
							Param userAddressStatusParam = new Param("userAddressStatus", "Error While Creation",
									FabricConstants.STRING);
							result.addParam(userAddressStatusParam);
							deleteInternalUser(userResponse, input, userlobResponse, authToken, requestInstance);
						}

						if (StringUtils.isNotBlank(input.get("role_id"))) {
							Param status = assignroleToUser(userResponse.getValue(), input.get("role_id").toString(),
									sysUserBean, authToken, requestInstance);
							if (StringUtils.equals(status.getValue(), "Success")) {
								Param roleStatusParam = new Param("RoleStatus", "Success", FabricConstants.STRING);
								result.addParam(roleStatusParam);
							}
						}
						if (newPermissions.length() != 0) {
							assignPermissionsToUser(newPermissions, authToken, sysUserBean, userResponse.getValue(),
									requestInstance);
						}
						// Email service
						String subject = "User Creation successful";
						String recipientEmailId = sysUserBean.getEmail();
						String emailType = "createUser";
						JSONObject context = new JSONObject();
						context.put("vizServerURL", requestInstance.getParameter("vizServerURL"));

						JSONObject eamilres = EmailHandler.invokeSendEmailObjectService(requestInstance, authToken,
								recipientEmailId, null, subject, emailType, context);

						Param EmailStatus;
						if (eamilres.getInt(FabricConstants.OPSTATUS) != 0) {
							EmailStatus = new Param("EmailStatus", "Error While Sending", FabricConstants.STRING);
						} else {
							EmailStatus = new Param("EmailStatus", "Success", FabricConstants.STRING);
						}

						result.addParam(EmailStatus);

					} else {
						Param userLobStatusParam = new Param("UserLobStatus", "Error While Creation",
								FabricConstants.STRING);
						result.addParam(userLobStatusParam);
						deleteInternalUser(userResponse, input, userlobResponse, authToken, requestInstance);
					}

				} else {
					Param userStatusParam = new Param("UserStatus", "Error While Creation", FabricConstants.STRING);
					result.addParam(userStatusParam);
				}
			} else {
				Param userStatusParam = new Param("UserStatus", "Error While Creation", FabricConstants.STRING);
				result.addParam(userStatusParam);
			}
		} catch (Exception e) {
			statusParam = new Param("Status", "Error", FabricConstants.STRING);
			result.addParam(statusParam);
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "User creation failed. Username:" + sysUserBean.getUsername());
		}
		return result;

	}

	private Param createUserType(Param userResponse, Map<String, String> input, String claimsToken,
			DataControllerRequest requestInstance) {
		Param statusParam;
		Map<String, String> inputMap = new HashMap<String, String>();
		String createUserTypeResponse = null;
		String id = CommonUtilities.getNewId().toString();
		String userType = String.valueOf(input.get("userType"));
		inputMap.put("userId", userResponse.getValue());
		inputMap.put("userType", userType);
		createUserTypeResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_CREATE, inputMap, null,
				requestInstance);

		JSONObject createUserTypeResponseJSON = CommonUtilities.getStringAsJSONObject(createUserTypeResponse);
		int getOpStatusCode = createUserTypeResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			statusParam = new Param("Status", "Success", FabricConstants.STRING);
		} else {
			statusParam = new Param("Status", "Error", FabricConstants.STRING);
		}
		return statusParam;
	}

	private Param createUserManager(Param userResponse, Map<String, String> input, String claimsToken,
			DataControllerRequest requestInstance) {
		Param statusParam;
		Map<String, String> inputMap = new HashMap<String, String>();
		String createUserManagerResponse = null;
		String id = CommonUtilities.getNewId().toString();
		String manager = String.valueOf(input.get("reportingManager"));
		inputMap.put("userId", userResponse.getValue());
		inputMap.put("manager", manager);
		createUserManagerResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_CREATE, inputMap, null,
				requestInstance);
		JSONObject createUserManagerResponseJSON = CommonUtilities.getStringAsJSONObject(createUserManagerResponse);
		int getOpStatusCode = createUserManagerResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			statusParam = new Param("Status", "Success", FabricConstants.STRING);
		} else {
			statusParam = new Param("Status", "Error", FabricConstants.STRING);
		}
		return statusParam;

	}

	private Param createUserLob(Param userResponse, Map<String, String> input, String claimsToken,
			DataControllerRequest requestInstance) {

		Map<String, String> lobMap = new HashMap<String, String>();
		String createUserLobResponse = null;
		String id = CommonUtilities.getNewId().toString();
		String[] lobArr = input.get("lineOfBusiness").split(",");
		lobMap.put("userId", userResponse.getValue());
		for (String lob : lobArr) {
			lobMap.put("lobId", lob);
			createUserLobResponse = Executor.invokeService(ServiceURLEnum.USERLOB_CREATE, lobMap, null,
					requestInstance);
		}
		Param userLobUUIDParam;
		JSONObject userLobCreationResponseJSON = CommonUtilities.getStringAsJSONObject(createUserLobResponse);
		int getOpStatusCode = userLobCreationResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode != 0) {
			userLobUUIDParam = new Param("UUID", id, FabricConstants.STRING);
		} else {
			userLobUUIDParam = new Param("UUID", id, FabricConstants.STRING);
		}
		return userLobUUIDParam;
	}

	private void deleteInternalUser(Param userResponse, Map<String, String> input, Param userlobResponse,
			String claimsToken, DataControllerRequest requestInstance) {
		Map<String, String> deleteUserMap = new HashMap<String, String>();
		deleteUserMap.clear();
		if (!StringUtils.equals(userlobResponse.getValue(), "ERROR")) {
			String[] lobArr = input.get("lineOfBusiness").split(",");
			for (String lob : lobArr) {
				deleteUserMap.clear();
				deleteUserMap.put("id", userResponse.getValue());
				deleteUserMap.put("lobId", lob);
				Executor.invokeService(ServiceURLEnum.USERLOB_DELETE, deleteUserMap, null, requestInstance);
			}
		}

		deleteUserMap.clear();
		deleteUserMap.put("id", userResponse.getValue());
		String userManager = input.get("reportingManager");
		deleteUserMap.put("manager", userResponse.getValue());
		Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_DELETE, deleteUserMap, null, requestInstance);

		deleteUserMap.clear();
		deleteUserMap.put("id", userResponse.getValue());
		String userType = input.get("userType");
		deleteUserMap.put("userType", userResponse.getValue());
		Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_DELETE, deleteUserMap, null, requestInstance);

		deleteUserMap.clear();
		deleteUserMap.put("id", userResponse.getValue());
		Executor.invokeService(ServiceURLEnum.SYSTEMUSER_DELETE, deleteUserMap, null, requestInstance);

	}

	private JSONObject deleteLobs(String claimsToken, List<String> removeLobs, String userID,
			DataControllerRequest requestInstance) {
		Map<String, String> deleteUserMap = new HashMap<String, String>();
		String deleteEndpointResponse = null;
		deleteUserMap.put("userId", userID);
		for (String lob : removeLobs) {
			deleteUserMap.put("lobId", lob);
			deleteEndpointResponse = Executor.invokeService(ServiceURLEnum.USERLOB_DELETE, deleteUserMap, null,
					requestInstance);
		}
		return CommonUtilities.getStringAsJSONObject(deleteEndpointResponse);
	}

	private JSONObject createLobs(String claimsToken, List<String> addLobs, String userID,
			DataControllerRequest requestInstance) {
		Map<String, String> lobMap = new HashMap<String, String>();
		String createEndpointResponse = null;
		lobMap.put("userId", userID);
		for (String lob : addLobs) {
			lobMap.put("lobId", lob);
			createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERLOB_CREATE, lobMap, null,
					requestInstance);
		}
		return CommonUtilities.getStringAsJSONObject(createEndpointResponse);
	}

	@SuppressWarnings("unused")
	@Override
	public Result editInternalUser(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		try {

			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String userID = requestInstance.getParameter("User_id");
			String firstName = requestInstance.getParameter("FirstName");
			String middleName = requestInstance.getParameter("MiddleName");
			String LastName = requestInstance.getParameter("LastName");
			String email = requestInstance.getParameter("Email");
			String modifiedByName = requestInstance.getParameter("ModifiedByName");
			String branchID = requestInstance.getParameter("BranchLocation_id");
			String branchName = requestInstance.getParameter("BranchLocation_Name");
			String roleID = requestInstance.getParameter("Role_id");
			String roleName = requestInstance.getParameter("Role_Name");
			String reportingManager = requestInstance.getParameter("reportingManager");
			String userType = requestInstance.getParameter("userType");
			String lineOfBusiness = requestInstance.getParameter("lineOfBusiness");
			JSONArray listOfAddedPermissions = null, listOfRemovedPermissions = null,
					listOfRemovedPermissionsNames = null, listOfAddedPermissionsNames = null;
			List<String> addLobs = new ArrayList<String>();
			List<String> removeLobs = new ArrayList<String>();
			JSONObject UserDetails = new JSONObject();
			String changedFields = null;
			String username = null;

			// get the isKeyCloakEnabled field from application table
			boolean isKeyCloakEnabled = Boolean
					.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
			if (!isKeyCloakEnabled) {
				if (requestInstance.getParameter("listOfAddedPermissions") != null)
					listOfAddedPermissions = new JSONArray(requestInstance.getParameter("listOfAddedPermissions"));
				if (requestInstance.getParameter("listOfRemovedPermissions") != null)
					listOfRemovedPermissions = new JSONArray(requestInstance.getParameter("listOfRemovedPermissions"));
				if (requestInstance.getParameter("listOfRemovedPermissionsNames") != null)
					listOfRemovedPermissionsNames = new JSONArray(
							requestInstance.getParameter("listOfRemovedPermissionsNames"));
				if (requestInstance.getParameter("listOfAddedPermissionsNames") != null)
					listOfAddedPermissionsNames = new JSONArray(
							requestInstance.getParameter("listOfAddedPermissionsNames"));
				// read from internal users view
				JSONObject readInternalUser = readInternalUser(authToken, userID, requestInstance);

				if (readInternalUser == null || !readInternalUser.has(FabricConstants.OPSTATUS)
						|| readInternalUser.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to read system user details. userID: " + userID);
					ErrorCodeEnum.ERR_21450.setErrorCode(result);
					return result;
				}
				UserDetails = (JSONObject) ((JSONArray) readInternalUser.get("internalusers_view")).get(0);
				changedFields = changedItems(UserDetails, firstName, middleName, LastName, email, null, roleID,
						roleName, listOfAddedPermissions, listOfRemovedPermissions, listOfAddedPermissionsNames,
						listOfRemovedPermissionsNames, lineOfBusiness, addLobs, removeLobs, userType, reportingManager,
						isKeyCloakEnabled);

				// if (StringUtils.isBlank(username)) {
				// username = UserDetails.getString("Username");
				// }

				username = UserDetails.getString("Username");
				username = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(username);

				JSONObject updateInternalUser = updateInternalUser(authToken, modifiedByName, userID, firstName,
						middleName, LastName, email, username, requestInstance);
				if (updateInternalUser == null || !updateInternalUser.has(FabricConstants.OPSTATUS)
						|| updateInternalUser.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to update system user details. username: " + username);
					ErrorCodeEnum.ERR_21451.setErrorCode(result);
					return result;
				}
				JSONObject getInternalUserHomeAddr = getInternalUserAddresses(authToken, userID, requestInstance);
				if (getInternalUserHomeAddr == null || !getInternalUserHomeAddr.has(FabricConstants.OPSTATUS)
						|| getInternalUserHomeAddr.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to read user address details. username: " + username);
					ErrorCodeEnum.ERR_21452.setErrorCode(result);
					return result;
				}

				String homeAddressID = null;
				JSONArray Addresses = ((JSONArray) getInternalUserHomeAddr.get("useraddress"));
				for (int i = 0; i < Addresses.length(); i++) {
					if (((JSONObject) Addresses.get(i)).get("Type_id").toString().equalsIgnoreCase("ADR_TYPE_HOME")) {
						homeAddressID = ((JSONObject) Addresses.get(i)).get("Address_id").toString();
					}
				}

			} else {
				JSONObject readInternalUserKC = readInternalUserKC(authToken, userID, requestInstance);
				if (readInternalUserKC == null || !readInternalUserKC.has(FabricConstants.OPSTATUS)
						|| readInternalUserKC.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to read system user details. userID: " + userID);
					ErrorCodeEnum.ERR_21450.setErrorCode(result);
					return result;
				}
				if (((JSONArray) readInternalUserKC.get("internaluserskc_view")).length() > 0) {
					UserDetails = (JSONObject) ((JSONArray) readInternalUserKC.get("internaluserskc_view")).get(0);
				}
				changedFields = "<br><b>Changed Fields</b><br>";
				setLobs(UserDetails, changedFields, lineOfBusiness, addLobs, removeLobs);
			}

			if (!(userType.equalsIgnoreCase(UserDetails.has("UserType") ? UserDetails.getString("UserType") : ""))) {
				changedFields += "<br><b>userType changed to</b> '" + userType + "'";
				JSONObject updateType = updateUserType(authToken, modifiedByName, userID, userType, requestInstance);
				if (updateType == null || !updateType.has(FabricConstants.OPSTATUS)
						|| updateType.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to update user type. userID: " + userID);
					ErrorCodeEnum.ERR_21455.setErrorCode(result);
					return result;
				} else {
					result.addParam(new Param("userType", "Success", FabricConstants.STRING));
				}
			}

			if (!(reportingManager.equalsIgnoreCase(
					UserDetails.has("ReportingManager") ? UserDetails.getString("ReportingManager") : ""))) {
				changedFields += "<br><b>ReportingManager changed to</b> '" + reportingManager + "'";
				JSONObject updateManager = updateUserManager(authToken, modifiedByName, userID, reportingManager,
						requestInstance);
				if (updateManager == null || !updateManager.has(FabricConstants.OPSTATUS)
						|| updateManager.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to update ReportingManager. userID: " + userID);
					ErrorCodeEnum.ERR_21455.setErrorCode(result);
					return result;
				} else {
					result.addParam(new Param("Manager", "Success", FabricConstants.STRING));
				}
			}

			if (lineOfBusiness != null) {
				if (removeLobs != null && removeLobs.size() >= 1) {
					JSONObject deleteUserLob = deleteLobs(authToken, removeLobs, userID, requestInstance);
					if (deleteUserLob == null || !deleteUserLob.has(FabricConstants.OPSTATUS)
							|| deleteUserLob.getInt(FabricConstants.OPSTATUS) != 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
								ActivityStatusEnum.FAILED,
								"Manage users failed. Unable to delete userlob details. userID: " + userID);
						ErrorCodeEnum.ERR_21459.setErrorCode(result);
						return result;
					} else {
						result.addParam(new Param("Userlob", "Success", FabricConstants.STRING));
					}
				}
				if (addLobs != null && addLobs.size() >= 1) {
					JSONObject createUserLob = createLobs(authToken, addLobs, userID, requestInstance);
					if (createUserLob == null || !createUserLob.has(FabricConstants.OPSTATUS)
							|| createUserLob.getInt(FabricConstants.OPSTATUS) != 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
								ActivityStatusEnum.FAILED,
								"Manage users failed. Unable to create userlob details. userID: " + userID);
						ErrorCodeEnum.ERR_21460.setErrorCode(result);
						return result;
					} else {
						result.addParam(new Param("userLob", "Success", FabricConstants.STRING));
					}
				}
			}
			if (StringUtils.isNotBlank(branchID)) {
				JSONObject getBranchAddr = getBranchAddr(authToken, branchID, requestInstance);
				if (getBranchAddr == null || !getBranchAddr.has(FabricConstants.OPSTATUS)
						|| getBranchAddr.getInt(FabricConstants.OPSTATUS) != 0) {
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED,
							"Manage users failed. Unable to read user branch address. username: " + username);
					ErrorCodeEnum.ERR_21454.setErrorCode(result);
					return result;
				}
				JSONObject location = ((JSONObject) ((JSONArray) getBranchAddr.get("location")).get(0));
				if (!(location.getString("Address_id").equalsIgnoreCase(
						UserDetails.has("Work_AddressID") ? UserDetails.getString("Work_AddressID") : ""))) {
					changedFields += "<br><b>Branch changed to</b> '" + branchName + "'";
					JSONObject updateWorkAddress = updateWorkAddress(authToken, modifiedByName, userID, username,
							location.getString("Address_id"), requestInstance);
					if (updateWorkAddress == null || !updateWorkAddress.has(FabricConstants.OPSTATUS)
							|| updateWorkAddress.getInt(FabricConstants.OPSTATUS) != 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
								ActivityStatusEnum.FAILED,
								"Manage users failed. Unable to update user branch address. username: " + username);
						ErrorCodeEnum.ERR_21455.setErrorCode(result);
						return result;
					}
				}

			}

			// For userrole table
			if (!isKeyCloakEnabled) {
				if (roleID != null) {
					JSONObject getRecordsForUserID = getRecordsForUserIDFromUserRole(authToken, userID,
							requestInstance);
					if (getRecordsForUserID == null || !getRecordsForUserID.has(FabricConstants.OPSTATUS)
							|| getRecordsForUserID.getInt(FabricConstants.OPSTATUS) != 0) {
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
								ActivityStatusEnum.FAILED,
								"Manage users failed. Unable to read user role details. username: " + username);
						ErrorCodeEnum.ERR_21456.setErrorCode(result);
						return result;
					}

					if (((JSONArray) getRecordsForUserID.get("userrole")).length() > 0) {
						JSONObject deleteResJSON = deleteUserRoles(authToken, userID, username,
								((JSONObject) ((JSONArray) getRecordsForUserID.get("userrole")).get(0))
										.getString("Role_id"),
								requestInstance);
						if (deleteResJSON == null || !deleteResJSON.has(FabricConstants.OPSTATUS)
								|| deleteResJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
									ActivityStatusEnum.FAILED,
									"Manage users failed. Unable to delete user role details. username: " + username);
							ErrorCodeEnum.ERR_21457.setErrorCode(result);
							return result;
						}
					}

					if (!roleID.equals("")) {
						JSONObject createResJSON = createUserRole(authToken, modifiedByName, userID, username, roleID,
								requestInstance);
						if (createResJSON == null || !createResJSON.has(FabricConstants.OPSTATUS)
								|| createResJSON.getInt(FabricConstants.OPSTATUS) != 0) {
							AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
									ActivityStatusEnum.FAILED,
									"Manage users failed. Unable to create user role. username: " + username);
							ErrorCodeEnum.ERR_21458.setErrorCode(result);
							return result;
						}
					}
				}

				Set<String> listOfPermissions = new HashSet<String>();
				if (listOfRemovedPermissions != null) {
					for (int indexVar = 0; indexVar < listOfRemovedPermissions.length(); indexVar++) {
						listOfPermissions.add(listOfRemovedPermissions.optString(indexVar));
					}
				}
				if (listOfAddedPermissions != null) {
					for (int indexVar = 0; indexVar < listOfAddedPermissions.length(); indexVar++) {
						listOfPermissions.add(listOfAddedPermissions.optString(indexVar));
					}
				}

				// Get the Composite Permission Information for all the permissions listed in
				// the added/removed list
				HashMap<String, ArrayList<Action>> compositeActionMapping = ActionHandler
						.getChildActions(listOfPermissions, requestInstance);

				// Remove user permissions
				if (listOfRemovedPermissions != null && listOfRemovedPermissions.length() >= 1) {
					deletePermissionsAndActions(authToken, listOfRemovedPermissions, userID, username, requestInstance);
				}

				// Insert user permissions
				if (listOfAddedPermissions != null && listOfAddedPermissions.length() >= 1) {
					createPermissionsAndActions(authToken, modifiedByName, listOfAddedPermissions, userID, username,
							requestInstance, compositeActionMapping);
				}

			}

			// Changed from permissions to actions for composite permissions
			/*
			 * HashMap<String, ArrayList<Permission>> compositePermissionMapping =
			 * PermissionHandler .getChildPermissions(listOfPermissions, requestInstance);
			 * 
			 * // Remove user permissions if (listOfRemovedPermissions != null &&
			 * listOfRemovedPermissions.length() >= 1) { deletePermissions(authToken,
			 * listOfRemovedPermissions, userID, username, requestInstance,
			 * compositePermissionMapping); }
			 * 
			 * // Insert user permissions if (listOfAddedPermissions != null &&
			 * listOfAddedPermissions.length() >= 1) { createPermissions(authToken,
			 * modifiedByName, listOfAddedPermissions, userID, username, requestInstance,
			 * compositePermissionMapping); }
			 */
			// Audit action
			if (!isKeyCloakEnabled) {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
						ActivityStatusEnum.SUCCESSFUL, "Username:" + username);

				// Send status change email
				String subject = "Account Edited";
				String recipientEmailId = UserDetails.getString("Email");
				String emailType = "InternalUserEdit";
				JSONObject AdditionalContext = new JSONObject();
				AdditionalContext.put("name", firstName);
				AdditionalContext.put("username", username);
				AdditionalContext.put("InternalUser_id", userID);
				AdditionalContext.put("ChangedFields", changedFields);
				JSONObject eamilres = EmailHandler.invokeSendEmailObjectService(requestInstance, authToken,
						recipientEmailId, null, subject, emailType, AdditionalContext);
				JSONObject eamilResponseForNewEmail = null;
				if ((email != null) && (!UserDetails.getString("Email").equals(email))) {
					eamilResponseForNewEmail = EmailHandler.invokeSendEmailObjectService(requestInstance, authToken,
							email, null, subject, emailType, AdditionalContext);
				}
				Param EmailStatus;
				if (eamilres.getInt(FabricConstants.OPSTATUS) != 0) {
					EmailStatus = new Param("EmailStatus", eamilres.toString(), FabricConstants.STRING);
				} else {
					EmailStatus = new Param("EmailStatus", "Success", FabricConstants.STRING);
				}

				if (eamilResponseForNewEmail != null) {
					if (eamilres.getInt(FabricConstants.OPSTATUS) != 0) {
						result.addParam(new Param("EmailStatusForNewEmail", eamilResponseForNewEmail.toString(),
								FabricConstants.STRING));
					} else {
						result.addParam(new Param("EmailStatusForNewEmail", "Success", FabricConstants.STRING));
					}
				}
				result.addParam(EmailStatus);
			}
			Param statusParam = new Param("Status", "Edited successfully", FabricConstants.STRING);
			result.addParam(statusParam);
			return result;

		} catch (ApplicationException e) {
			Result errorResult = new Result();
			alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
			e.getErrorCodeEnum().setErrorCode(errorResult);
			return errorResult;
		}
	}

	@Override
	public Result downloadUsersList(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		// TODO Auto-generated method stub
		Result result = new Result();

		String authToken = CommonUtilities.getAuthToken(requestInstance);
		if (StringUtils.isBlank(authToken)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20000);
		}

		Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
		String searchText = queryParamsMap.containsKey("searchText") ? queryParamsMap.get("searchText") : null;
		String role = queryParamsMap.containsKey("role") ? queryParamsMap.get("role") : null;
		String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status") : null;
		String createdStartDate = queryParamsMap.containsKey("createdStartDate")
				? queryParamsMap.get("createdStartDate")
				: null;
		String createdEndDate = queryParamsMap.containsKey("createdEndDate") ? queryParamsMap.get("createdEndDate")
				: null;
		String updatedStartDate = queryParamsMap.containsKey("updatedStartDate")
				? queryParamsMap.get("updatedStartDate")
				: null;
		String updatedEndDate = queryParamsMap.containsKey("updatedEndDate") ? queryParamsMap.get("updatedEndDate")
				: null;

		boolean isKeyCloakEnabled = Boolean
				.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
		if (isKeyCloakEnabled) {
			String readInternalUsersViewResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USERS,
					new HashMap<String, String>(), null, requestInstance);
			if (readInternalUsersViewResponse == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20000);
			}
			JSONObject readInternalUsersViewResponseJSON = CommonUtilities
					.getStringAsJSONObject(readInternalUsersViewResponse);
			if (readInternalUsersViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readInternalUsersViewResponseJSON.getJSONArray("internalusers_view") != null) {
				StringBuilder responseCsvBuilder = new StringBuilder();
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name", "Username", "Email", "Role", "No. of Permissions", "Status")
						.print(responseCsvBuilder);
				JSONArray internalUsers = readInternalUsersViewResponseJSON.getJSONArray("internalusers_view");
				for (int i = 0; i < internalUsers.length(); ++i) {
					JSONObject currRecordJSONObject = internalUsers.getJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					List<Permission> permissionsList = new ArrayList<>();
					JSONArray roleJsonArr = new JSONArray();
					map.put("keyCloakUserId", currRecordJSONObject.getString("User_id"));
					String readKeycloakUserrolesResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USER_ROLES,
							map, null, requestInstance);
					JSONObject readKeycloakUserrolesResponseJson = CommonUtilities
							.getStringAsJSONObject(readKeycloakUserrolesResponse);
					if (readKeycloakUserrolesResponseJson.getInt(FabricConstants.OPSTATUS) == 0) {
						String roleIds = readKeycloakUserrolesResponseJson.getString("roleIds");
						String[] roleIdArr = roleIds.split(",");
						for (String roleId : roleIdArr) {
							Map<String, String> postParametersMap = new HashMap<String, String>();
							postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + roleId + "'");
							String readEndpointResponse = Executor.invokeService(ServiceURLEnum.ROLE_READ,
									postParametersMap, null, requestInstance);
							JSONObject readUserRoleResponseJson = CommonUtilities
									.getStringAsJSONObject(readEndpointResponse);
							JSONArray roleDetails = ((JSONArray) readUserRoleResponseJson.get("role"));
							if (roleDetails.length() != 0) {
								roleJsonArr.put(((JSONObject) roleDetails.get(0)).get("Name"));
							}
						}
						permissionsList = PermissionHandler.getRolesGrantedPermissions(roleIds, requestInstance);
						currRecordJSONObject.put("Role", roleJsonArr.join(","));
						currRecordJSONObject.put("Rolepermissions", permissionsList.size());
					} else {
						return constructFailureResult(readKeycloakUserrolesResponseJson);
					}
					String nameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("FirstName") + " "
									+ internalUsers.getJSONObject(i).optString("LastName"));
					String userNameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Username"));
					String emailColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Email"));
					String statusColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							getStatusDes(internalUsers.getJSONObject(i).optString("Status_id"), requestInstance));
					String roleColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Role").replace("\"", ""));
					String rolePermissionColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Rolepermissions"));
					if (searchText == null
							|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())
									|| userNameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
						responseCsvPrinter.printRecord(nameColumn, userNameColumn, emailColumn, roleColumn,
								rolePermissionColumn, statusColumn);
					}
				}
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.SUCCESSFUL, "Users file download successful");

				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"Users_List.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
				responseInstance.setStatusCode(HttpStatus.SC_OK);

			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.FAILED, "Users file download failed");
			}

		} else {
			// ** Reading entries from 'internalusers_view' view **
			Map<String, String> internalUsersViewMap = new HashMap<String, String>();
			internalUsersViewMap.put(ODataQueryConstants.SELECT,
					"FirstName, LastName, Username, Email, Role_Name, Permission_Count, Status_Desc");

			StringBuilder filterString = new StringBuilder();

			if (searchText == null && status == null) {
				filterString.append("(Status_Desc eq 'Active' or Status_Desc eq 'Suspended')");
			} else {
				String[] statuses = status.split("_");
				filterString.append("(");
				for (int i = 0; i < statuses.length - 1; ++i) {
					if (statuses[i].equals("Disabled")) {
						filterString.append("Status_Desc eq 'Inactive'");
					} else {
						filterString.append("Status_Desc eq '" + statuses[i] + "'");
					}
					filterString.append(" or ");
				}
				if (statuses[statuses.length - 1].equals("Disabled")) {
					filterString.append("Status_Desc eq 'Inactive')");
				} else {
					filterString.append("Status_Desc eq '" + statuses[statuses.length - 1] + "')");
				}
			}

			if (role != null) {
				if (filterString != null) {
					filterString.append(" and ");
				}
				String[] roles = role.split("_");
				filterString.append("(");
				for (int i = 0; i < roles.length - 1; ++i) {
					filterString.append("Role_Name eq '" + roles[i] + "'");
					filterString.append(" or ");
				}
				filterString.append("Role_Name eq '" + roles[roles.length - 1] + "')");
			}
			if (createdStartDate != null && createdEndDate != null) {
				if (filterString != null) {
					filterString.append(" and ");
				}
				filterString.append("createdts ge '" + getStartDateInOAuthFormat(createdStartDate)
						+ "' and createdts le '" + getEndDateInOAuthFormat(createdEndDate) + "'");
			}
			if (updatedStartDate != null && updatedEndDate != null) {
				if (filterString != null) {
					filterString.append(" and ");
				}
				filterString.append("lastmodifiedts ge '" + getStartDateInOAuthFormat(updatedStartDate)
						+ "' and lastmodifiedts le '" + getEndDateInOAuthFormat(updatedEndDate) + "'");
			}

			if (filterString != null) {
				internalUsersViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
			}
			String readInternalUsersViewResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ,
					internalUsersViewMap, null, requestInstance);
			if (readInternalUsersViewResponse == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20000);
			}
			JSONObject readInternalUsersViewResponseJSON = CommonUtilities
					.getStringAsJSONObject(readInternalUsersViewResponse);

			if (readInternalUsersViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readInternalUsersViewResponseJSON.getJSONArray("internalusers_view") != null) {

				StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name", "Username", "Email", "Role", "No. of Permissions", "Status")
						.print(responseCsvBuilder);

				JSONArray internalUsers = readInternalUsersViewResponseJSON.getJSONArray("internalusers_view");

				for (int i = 0; i < internalUsers.length(); ++i) {

					String nameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("FirstName") + " "
									+ internalUsers.getJSONObject(i).optString("LastName"));
					String userNameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Username"));
					String emailColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Email"));
					String roleColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Role_Name"));
					String permissionCountColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Permission_Count"));
					String statusColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(
							internalUsers.getJSONObject(i).optString("Status_Desc"));

					if (searchText == null
							|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())
									|| userNameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
						responseCsvPrinter.printRecord(nameColumn, userNameColumn, emailColumn, roleColumn,
								permissionCountColumn, statusColumn);
					}
				}

				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.SUCCESSFUL, "Users file download successful");

				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"Users_List.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
				responseInstance.setStatusCode(HttpStatus.SC_OK);
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.FAILED, "Users file download failed");
			}

		}

		return result;
	}

	@Override
	public Result updateUserStatus(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		Result processedResult = new Result();
		String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		String userID = requestInstance.getParameter("User_id");
		String statusID = requestInstance.getParameter("Status_id");

		String username = null;
		try {
			JSONObject readInternalUser = readInternalUser(userID, requestInstance);
			username = (readInternalUser.getJSONArray("internalusers_view")).getJSONObject(0).getString("Username");
		} catch (Exception ignored) {
		}

		// change status
		JSONObject updateInternalUser = updateInternalUser(userID, statusID, requestInstance);
		if (updateInternalUser.getInt(FabricConstants.OPSTATUS) != 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED,
					"User status change failed. Username: " + username + " Status: " + statusID);
			return ErrorCodeEnum.ERR_20545.setErrorCode(processedResult);
		}

		// audit action
		AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL,
				"User status changed successfully. Username: " + username + " Status: " + statusID);

		// get details of user
		JSONObject readUserDetails = readUserDetails(userID, requestInstance);
		JSONArray array = readUserDetails.optJSONArray("internaluserdetails_view");
		JSONObject UserDetails = null;
		if (array != null && array.length() > 0) {
			UserDetails = array.optJSONObject(0);
		}
		// Send status change email
		String subject = "Status changed";
		String status = statusID.equalsIgnoreCase(StatusEnum.SID_ACTIVE.name()) ? "Activated"
				: (statusID.equalsIgnoreCase(StatusEnum.SID_SUSPENDED.name()) ? "Suspended" : "Deactivated");
		String recipientEmailId = UserDetails.getString("Email");
		String emailType = "InternalUserStatusChange";
		JSONObject additionalContext = new JSONObject();
		additionalContext.put("name", UserDetails.getString("FirstName"));
		additionalContext.put("username", UserDetails.getString("Username"));
		additionalContext.put("status", status);
		additionalContext.put("InternalUser_id", userID);
		JSONObject eamilres = EmailHandler.invokeSendEmailObjectService(requestInstance, authToken, recipientEmailId,
				null, subject, emailType, additionalContext);
		Param emailStatus;
		if (eamilres.getInt(FabricConstants.OPSTATUS) != 0) {
			emailStatus = new Param("EmailStatus", eamilres.toString(), FabricConstants.STRING);
		} else {
			emailStatus = new Param("EmailStatus", "Success", FabricConstants.STRING);
		}

		Param statusParam = new Param("Status", "status changed", FabricConstants.STRING);
		processedResult.addParam(statusParam);
		processedResult.addParam(emailStatus);
		return processedResult;
	}

	@Override
	public Result getUserProfile(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		Result processedResult = new Result();
		String AuthToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		String UserID = requestInstance.getParameter("User_id");
		String isEditString = requestInstance.getParameter("isEdit");
		boolean isEdit = false;
		if (isEditString != null) {
			isEdit = Boolean.parseBoolean(isEditString);
		}

		boolean isKeyCloakEnabled = Boolean
				.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
		if (isKeyCloakEnabled) {
			String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USERS,
					new HashMap<String, String>(), null, requestInstance);
			JSONObject readSystemUserResponseJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);
			int opStatusCode = readSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS);
			if (opStatusCode == 0) {
				JSONArray systemuserJSONArray = readSystemUserResponseJSON.getJSONArray("internalusers_view");
				for (int indexVar = 0; indexVar < systemuserJSONArray.length(); indexVar++) {
					JSONObject currRecordJSONObject = systemuserJSONArray.getJSONObject(indexVar);
					if (UserID.equals(currRecordJSONObject.getString("User_id"))) {
						List<Permission> permissionsList = new ArrayList<>();
						Map<String, String> map = new HashMap<String, String>();
						map.put("keyCloakUserId", UserID);
						String readKeycloakUserrolesResponse = Executor
								.invokeService(ServiceURLEnum.KEYCLOAK_USER_ROLES, map, null, requestInstance);
						JSONObject readKeycloakUserrolesResponseJson = CommonUtilities
								.getStringAsJSONObject(readKeycloakUserrolesResponse);
						if (readKeycloakUserrolesResponseJson.getInt(FabricConstants.OPSTATUS) == 0) {
							String roleIds = readKeycloakUserrolesResponseJson.getString("roleIds");
							if (StringUtils.isNotBlank(roleIds)) {
								permissionsList = PermissionHandler.getRolesGrantedPermissions(roleIds,
										requestInstance);
							}
							setRoleDesc(roleIds, currRecordJSONObject, AuthToken, requestInstance);
						} else {
							return constructFailureResult(readKeycloakUserrolesResponseJson);
						}
						JSONObject readInternalUserKCResponseJSON = readInternalUserKC(AuthToken,
								currRecordJSONObject.getString("User_id"), requestInstance);
						JSONObject details = new JSONObject();
						if (((JSONArray) readInternalUserKCResponseJSON.get("internaluserskc_view")).length() > 0) {
							details = (JSONObject) ((JSONArray) readInternalUserKCResponseJSON
									.get("internaluserskc_view")).get(0);
						}
						List<JSONObject> permissions = new ArrayList<>();
						if (permissionsList.size() > 0) {
							Set<String> setuinque = new HashSet<>();
							for (Permission permission : permissionsList) {
								if (!setuinque.contains(permission.getId())) {
									setuinque.add(permission.getId());
									Map<String, String> permissionMap = new HashMap<String, String>();
									JSONObject json = new JSONObject();
									json.put("Permission_isComposite", permission.getIsComposite());
									json.put("Permission_Name", permission.getName());
									json.put("Permission_id", permission.getId());
									json.put("User_id", currRecordJSONObject.getString("User_id"));
									json.put("Role_id", permission.getRole_id());
									json.put("LegalEntityID", permission.getCompanyLegalUnit());
									permissionMap.put(ODataQueryConstants.FILTER, "id eq '" + permission.getId() + "'");
									String readpermissionResponse = Executor.invokeService(
											ServiceURLEnum.PERMISSION_READ, permissionMap, null, requestInstance);
									JSONObject readpermissionResponseJson = CommonUtilities
											.getStringAsJSONObject(readpermissionResponse);
									JSONObject permissionDetails = (JSONObject) ((JSONArray) readpermissionResponseJson
											.get("permission")).get(0);
									json.put("Permission_Desc", permissionDetails.getString("Description"));
									permissions.add(json);

								} else {
									for (int i = 0; i < permissions.size(); i++) {
										if (permission.getId().equals(permissions.get(i).getString("Permission_id"))) {
											if (!(permission.getCompanyLegalUnit()
													.equals(permissions.get(i).getString("LegalEntityID")))) {
												permissions.get(i).put("LegalEntityID",
														permissions.get(i).getString("LegalEntityID").concat(",")
																.concat(permission.getCompanyLegalUnit()));
											}
										}
									}
								}
							}
						}

						long ts = (long) currRecordJSONObject.get("createdts");
						Date date = new Date(ts);
						DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						currRecordJSONObject.put("lastmodifiedts", dFormat.format(date).toString());
						currRecordJSONObject.put("createdts", dFormat.format(date).toString());
						String Status_Desc = getStatusDes(currRecordJSONObject.getString("Status_id"), requestInstance);
						currRecordJSONObject.put("Status_Desc", Status_Desc);
						currRecordJSONObject.put("UserType",
								details.has("UserType") ? details.getString("UserType") : "");
						currRecordJSONObject.put("ReportingManager",
								details.has("ReportingManager") ? details.getString("ReportingManager") : "");
						currRecordJSONObject.put("lobId", details.has("lobId") ? details.getString("lobId") : "");
						currRecordJSONObject.put("lobName", details.has("lobName") ? details.getString("lobName") : "");
						currRecordJSONObject.put("userTypeName",
								details.has("userTypeName") ? details.getString("userTypeName") : "");
						currRecordJSONObject.put("Work_AddressLine1",
								details.has("Work_AddressLine1") ? details.getString("Work_AddressLine1") : "");
						currRecordJSONObject.put("Work_AddressLine2",
								details.has("Work_AddressLine2") ? details.getString("Work_AddressLine2") : "");
						currRecordJSONObject.put("Work_CityName",
								details.has("Work_CityName") ? details.getString("Work_CityName") : "");
						currRecordJSONObject.put("Work_CityID",
								details.has("Work_CityID") ? details.getString("Work_CityID") : "");
						currRecordJSONObject.put("Work_StateName",
								details.has("Work_StateName") ? details.getString("Work_StateName") : "");
						currRecordJSONObject.put("Work_StateID",
								details.has("Work_StateID") ? details.getString("Work_StateID") : "");
						currRecordJSONObject.put("Work_CountryName",
								details.has("Work_CountryName") ? details.getString("Work_CountryName") : "");
						currRecordJSONObject.put("Work_CountryID",
								details.has("Work_CountryID") ? details.getString("Work_CountryID") : "");
						currRecordJSONObject.put("Work_Zipcode",
								details.has("Work_Zipcode") ? details.getString("Work_Zipcode") : "");
						currRecordJSONObject.put("branchName",
								details.has("branchName") ? details.getString("branchName") : "");
						currRecordJSONObject.put("Work_Addr",
								details.has("Work_Addr") ? details.getString("Work_Addr") : "");
						JSONObject object = new JSONObject();
						JSONArray UserDetails = new JSONArray();
						UserDetails.put(0, currRecordJSONObject);
						object.put("internalusers_view", UserDetails);
						object.put("userpermission_view", permissions);
						processedResult = CommonUtilities.getResultObjectFromJSONObject(object);
						break;
					}
				}
				Param statusParam = new Param("Status", "Succesful", FabricConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;
			} else {
				return constructFailureResult(readSystemUserResponseJSON);
			}

		}

		// read from internal users view
		JSONObject readInternalUser = readInternalUser(AuthToken, UserID, requestInstance);

		if (readInternalUser.getInt(FabricConstants.OPSTATUS) != 0) {
			return constructFailureResult(readInternalUser);
		}

		// read from userpermissions view
		JSONObject readUserPermissions = readUserPermissions(AuthToken, UserID, requestInstance);

		if (readUserPermissions.getInt(FabricConstants.OPSTATUS) != 0) {
			return constructFailureResult(readUserPermissions);
		}
		setRoleDetails(readInternalUser);

		// direct permissions
		JSONObject readUserDirectPermissions = readUserDirectPermissions(AuthToken, UserID, requestInstance);
		if (readUserDirectPermissions.getInt(FabricConstants.OPSTATUS) != 0) {
			return constructFailureResult(readUserDirectPermissions);
		}

		JSONArray UserDetails = (JSONArray) readInternalUser.get("internalusers_view");
		processedResult = CommonUtilities.getResultObjectFromJSONObject(readInternalUser);

		JSONArray userPermissions = (JSONArray) readUserPermissions.get("userpermission_view");
		Dataset userPermissionsArray = new Dataset();
		userPermissionsArray.setId("userpermission_view");
		for (int count = 0; count < userPermissions.length(); count++) {
			Record userpermission = constructRecordFromJSON((JSONObject) userPermissions.get(count));
			userPermissionsArray.addRecord(userpermission);
		}

		JSONArray userdirectpermissionJSON = (JSONArray) readUserDirectPermissions.get("userdirectpermission_view");
		Dataset userDirectPermissionsDataset = new Dataset();
		userDirectPermissionsDataset.setId("userdirectpermission_view");
		for (int count = 0; count < userdirectpermissionJSON.length(); count++) {
			Record userpermission = constructRecordFromJSON((JSONObject) userdirectpermissionJSON.get(count));
			userDirectPermissionsDataset.addRecord(userpermission);
		}

		if (isEdit) {
			// Read addresses for the user
			JSONObject getInternalUserAddresses = getInternalUserAddresses(AuthToken, UserID, requestInstance);
			if (getInternalUserAddresses.getInt(FabricConstants.OPSTATUS) != 0) {
				return constructFailureResult(getInternalUserAddresses);
			}

			String homeAddressID = null, workAddressID = null;
			JSONArray Addresses = ((JSONArray) getInternalUserAddresses.get("useraddress"));
			for (int i = 0; i < Addresses.length(); i++) {
				if (((JSONObject) Addresses.get(i)).get("Type_id").toString().equalsIgnoreCase("ADR_TYPE_HOME")) {
					homeAddressID = ((JSONObject) Addresses.get(i)).get("Address_id").toString();
				} else if (((JSONObject) Addresses.get(i)).get("Type_id").toString()
						.equalsIgnoreCase("ADR_TYPE_WORK")) {
					workAddressID = ((JSONObject) Addresses.get(i)).get("Address_id").toString();
				}
			}

			JSONObject getUserAddresses = getUserAddresses(AuthToken, homeAddressID, workAddressID, requestInstance);
			if (getUserAddresses.getInt(FabricConstants.OPSTATUS) != 0) {
				return constructFailureResult(getUserAddresses);
			}

			JSONObject homeAddressJSON = null, workAddressJSON = null;
			JSONArray UserAddresses = ((JSONArray) getUserAddresses.get("address"));
			for (int i = 0; i < UserAddresses.length(); i++) {
				if (((JSONObject) UserAddresses.get(i)).get("id").toString().equalsIgnoreCase(homeAddressID)) {
					homeAddressJSON = (JSONObject) UserAddresses.get(i);

				} else if (((JSONObject) UserAddresses.get(i)).get("id").toString().equalsIgnoreCase(workAddressID)) {
					workAddressJSON = (JSONObject) UserAddresses.get(i);
				}
			}
			// read country
			if (homeAddressJSON != null) {
				JSONObject readCountry = readCity(AuthToken, homeAddressJSON.getString("Region_id"), requestInstance);
				homeAddressJSON.put("Country_id",
						((JSONObject) ((JSONArray) readCountry.get("region")).get(0)).getString("Country_id"));
			}

			// Prepare Datasets
			Record homeAddrRecord = constructRecordFromJSON(homeAddressJSON);
			Dataset homeAddrDataset = new Dataset();
			homeAddrDataset.setId("Home_addr");
			homeAddrDataset.addRecord(homeAddrRecord);

			Record workAddrRecord = constructRecordFromJSON(workAddressJSON);
			Dataset workAddrDataset = new Dataset();
			workAddrDataset.setId("Work_addr");
			workAddrDataset.addRecord(workAddrRecord);

			processedResult.addDataset(homeAddrDataset);
			processedResult.addDataset(workAddrDataset);

			String roleID;
			try {
				roleID = ((JSONObject) UserDetails.get(0)).getString("Role_id");
			} catch (Exception e) {
				roleID = null;
			}

			// read roles
			JSONObject readRoles = readRoles(AuthToken, roleID, requestInstance);
			if (readRoles.getInt(FabricConstants.OPSTATUS) != 0) {
				return constructFailureResult(readRoles);
			}

			JSONArray unassignedRoles = ((JSONArray) readRoles.get("role"));
			Dataset unassignedRolesDataset = new Dataset();
			unassignedRolesDataset.setId("unassigned_Roles");
			for (int count = 0; count < unassignedRoles.length(); count++) {
				Record role = constructRecordFromJSON((JSONObject) unassignedRoles.get(count));
				unassignedRolesDataset.addRecord(role);
			}

			processedResult.addDataset(unassignedRolesDataset);

			// read all permissions
			JSONObject readAllPermissions = readAllPermissions(AuthToken, requestInstance);
			if (readAllPermissions.getInt(FabricConstants.OPSTATUS) != 0) {
				return constructFailureResult(readAllPermissions);
			}

			// read all role permissions
			JSONObject readRolePermissions = readRolePermissions(AuthToken, requestInstance);
			if (readRolePermissions.getInt(FabricConstants.OPSTATUS) != 0) {
				return constructFailureResult(readRolePermissions);
			}

			JSONArray AllPermissionsJSONArray = ((JSONArray) readAllPermissions.get("permission"));
			Dataset AllPermissionsDataset = new Dataset();
			AllPermissionsDataset.setId("Permissions");
			for (int count = 0; count < AllPermissionsJSONArray.length(); count++) {
				Record permission = constructRecordFromJSON((JSONObject) AllPermissionsJSONArray.get(count));
				AllPermissionsDataset.addRecord(permission);
			}

			JSONArray AllRolePermissionsJSONArray = ((JSONArray) readRolePermissions.get("rolepermission"));
			Dataset RolePermissionsDataset = new Dataset();
			RolePermissionsDataset.setId("RolePermissions");
			for (int count = 0; count < AllRolePermissionsJSONArray.length(); count++) {
				Record permission = constructRecordFromJSON((JSONObject) AllRolePermissionsJSONArray.get(count));
				RolePermissionsDataset.addRecord(permission);
			}

			processedResult.addDataset(AllPermissionsDataset);
			processedResult.addDataset(RolePermissionsDataset);
		}

		processedResult.addDataset(userPermissionsArray);
		processedResult.addDataset(userDirectPermissionsDataset);

		Param statusParam = new Param("Status", "Succesful", FabricConstants.STRING);
		processedResult.addParam(statusParam);
		return processedResult;
	}

	@Override
	public Result manageUserCompositeActions(String methodId, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) throws Exception {

		Result processedResult = new Result();
		ErrorCodeEnum errorInformation = null;
		String userId = requestInstance.getParameter("userId");
		String addedCompositeActions = requestInstance.getParameter("addedCompositeActions");
		String removedCompositeActions = requestInstance.getParameter("removedCompositeActions");
		String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);

		if (StringUtils.isEmpty(userId)) {
			errorInformation = ErrorCodeEnum.ERR_20522;
		} else {
			JSONArray listOfAddedCompositeActions = CommonUtilities.getStringAsJSONArray(addedCompositeActions);
			JSONArray listOfRemovedCompositeActions = CommonUtilities.getStringAsJSONArray(removedCompositeActions);

			Record initializeUserActionMappingOperationRecord = initUserCompositeActionMapping(userId,
					listOfAddedCompositeActions, listOfRemovedCompositeActions, userDetailsBeanInstance, authToken,
					requestInstance);
			processedResult.addRecord(initializeUserActionMappingOperationRecord);

			Record manageAddedCompositeActionsOperationRecord = processCompositeActions(userId,
					listOfAddedCompositeActions, userDetailsBeanInstance, true, authToken, requestInstance);
			if (manageAddedCompositeActionsOperationRecord != null) {
				if (manageAddedCompositeActionsOperationRecord.getParamByName("status") != null
						&& manageAddedCompositeActionsOperationRecord.getParamByName("status").getValue()
								.equalsIgnoreCase("failure")) {
					errorInformation = ErrorCodeEnum.ERR_20526;
				}
			}

			Record manageRemovedCompositeActionsOperationRecord = processCompositeActions(userId,
					listOfRemovedCompositeActions, userDetailsBeanInstance, false, authToken, requestInstance);
			if (manageRemovedCompositeActionsOperationRecord != null) {
				if (manageRemovedCompositeActionsOperationRecord.getParamByName("status") != null
						&& manageRemovedCompositeActionsOperationRecord.getParamByName("status").getValue()
								.equalsIgnoreCase("failure")) {
					errorInformation = ErrorCodeEnum.ERR_20526;
				}
			}
		}
		if (errorInformation != null) {
			errorInformation.setErrorCode(processedResult);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Composite Actions update failed for the user:" + userId);
			return processedResult;
		}
		AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.UPDATE,
				ActivityStatusEnum.SUCCESSFUL, "Composite Actions updated Successfully for the user:" + userId);
		return processedResult;

	}

	private Record processCompositeActions(String userId, JSONArray compositeActionsList,
			UserDetailsBean userDetailsBeanInstance, boolean isAddedActionsOperation, String authToken,
			DataControllerRequest requestInstance) {

		if (compositeActionsList == null || compositeActionsList.length() == 0) {
			return null;
		}

		Record addCompositeActionsResponse = new Record();
		Param operationStatus = new Param("status", "Successful", FabricConstants.STRING);
		addCompositeActionsResponse.addParam(operationStatus);

		String currActionId, currOperationResponse, isEnabledFlag;
		JSONObject currOperationResponseJSON;
		Param currOperationParam;

		if (isAddedActionsOperation) {
			addCompositeActionsResponse.setId("addCompositeActions");
			isEnabledFlag = "1";
		} else {
			addCompositeActionsResponse.setId("removedCompositeActions");
			isEnabledFlag = "0";
		}

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("User_id", userId);
		postParametersMap.put("isEnabled", isEnabledFlag);
		postParametersMap.put("modifiedby", userDetailsBeanInstance.getId());
		postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

		for (int indexVar = 0; indexVar < compositeActionsList.length(); indexVar++) {

			currActionId = compositeActionsList.optString(indexVar);
			postParametersMap.put("CompositeAction_id", currActionId);

			currOperationResponse = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_UPDATE, postParametersMap,
					null, requestInstance);
			currOperationResponseJSON = CommonUtilities.getStringAsJSONObject(currOperationResponse);
			if (currOperationResponseJSON == null || !currOperationResponseJSON.has(FabricConstants.OPSTATUS)
					|| currOperationResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
				operationStatus.setValue("Failure");
			}
			currOperationParam = new Param("action: " + currActionId, currOperationResponse, FabricConstants.STRING);
			addCompositeActionsResponse.addParam(currOperationParam);

		}
		return addCompositeActionsResponse;
	}

	private Record initUserCompositeActionMapping(String userId, JSONArray listOfAddedCompositeActions,
			JSONArray listOfRemovedCompositeActions, UserDetailsBean userDetailsBeanInstance, String authToken,
			DataControllerRequest requestInstance) {

		Record operationRecord = new Record();
		operationRecord.setId("initializeUserCompositeActionMapping");
		Map<String, String> postParametersMap = new HashMap<String, String>();

		postParametersMap.put(ODataQueryConstants.SELECT, "CompositeAction_id");
		postParametersMap.put(ODataQueryConstants.FILTER, "User_Id eq '" + userId + "'");
		String readUserCompositeActionResponse = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_READ,
				postParametersMap, null, requestInstance);

		Set<String> compositeActionSet = new HashSet<String>();
		String currPermissionId, currOperationResponse;

		if (listOfAddedCompositeActions != null && listOfAddedCompositeActions.length() > 0) {
			for (int indexVar = 0; indexVar < listOfAddedCompositeActions.length(); indexVar++) {
				currPermissionId = listOfAddedCompositeActions.optString(indexVar);
				compositeActionSet.add(currPermissionId);
			}
		}

		if (listOfRemovedCompositeActions != null && listOfRemovedCompositeActions.length() > 0) {
			for (int indexVar = 0; indexVar < listOfRemovedCompositeActions.length(); indexVar++) {
				currPermissionId = listOfRemovedCompositeActions.optString(indexVar);
				compositeActionSet.add(currPermissionId);
			}
		}

		JSONObject readUserCompositeActionResponseJSON = CommonUtilities
				.getStringAsJSONObject(readUserCompositeActionResponse);
		if (readUserCompositeActionResponseJSON != null
				&& readUserCompositeActionResponseJSON.has(FabricConstants.OPSTATUS)
				&& readUserCompositeActionResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readUserCompositeActionResponseJSON.has("usercompositeaction")) {
			JSONArray permissionsArray = readUserCompositeActionResponseJSON.getJSONArray("usercompositepermission");
			if (permissionsArray != null) {
				for (int indexVar = 0; indexVar < permissionsArray.length(); indexVar++) {
					compositeActionSet.remove(permissionsArray.get(indexVar));
				}
			}
		}

		postParametersMap.put("User_id", userId);
		postParametersMap.put("isEnabled", "0");
		postParametersMap.put("createdby", userDetailsBeanInstance.getId());
		postParametersMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
		postParametersMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
		postParametersMap.put("softdeleteflag", "0");

		Param currOperationParam = null;
		for (String currAction : compositeActionSet) {
			postParametersMap.put("CompositeAction_id", currAction);
			currOperationResponse = Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_CREATE, postParametersMap,
					null, requestInstance);
			currOperationParam = new Param("insertAction: " + currAction, currOperationResponse,
					FabricConstants.STRING);
			operationRecord.addParam(currOperationParam);
		}
		return operationRecord;
	}

	public JSONObject readCity(String AuthToken, String cityID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + cityID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.REGION_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readAllPermissions(String AuthToken, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "Status_id eq 'SID_ACTIVE'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.PERMISSION_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readRolePermissions(String AuthToken, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.ROLEPERMISSION_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readRoles(String AuthToken, String roleID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		if (roleID != null) {
			postParametersMap.put(ODataQueryConstants.FILTER, "id ne '" + roleID + "' and Status_id eq 'SID_ACTIVE'");
		} else {
			postParametersMap.put(ODataQueryConstants.FILTER, "Status_id eq 'SID_ACTIVE'");
		}
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.ROLE_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readUserPermissions(String AuthToken, String UserID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSION_VIEW_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readUserDirectPermissions(String AuthToken, String UserID,
			DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERDIRECTPERMISSION_VIEW_READ,
				postParametersMap, null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject getUserAddresses(String AuthToken, String homeAddressID, String workAddressID,
			DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER,
				"id eq '" + homeAddressID + "' or id eq '" + workAddressID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.ADDRESS_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public Result constructFailureResult(JSONObject ResponseJSON) {
		Result Result = new Result();
		Param errCodeParam = new Param("backend_error_code", "" + ResponseJSON.getInt(FabricConstants.OPSTATUS),
				FabricConstants.INT);
		Param errMsgParam = new Param("backend_error_message", ResponseJSON.toString(), FabricConstants.STRING);
		Param serviceMessageParam = new Param("errmsg", ResponseJSON.toString(), FabricConstants.STRING);
		Param statusParam = new Param("Status", "Failure", FabricConstants.STRING);
		Result.addParam(errCodeParam);
		Result.addParam(errMsgParam);
		Result.addParam(serviceMessageParam);
		Result.addParam(statusParam);
		return Result;
	}

	public Record constructRecordFromJSON(JSONObject JSON) {
		Record response = new Record();
		if (JSON == null || JSON.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSON.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			Param param = new Param(key, JSON.getString(key), FabricConstants.STRING);
			response.addParam(param);
		}

		return response;
	}

	public static String getStartDateInOAuthFormat(String inputDateString) throws ParseException {
		DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat oauthFormat = new SimpleDateFormat("yyyy-MM-dd");

		return oauthFormat.format(originalFormat.parse(inputDateString)) + "T00:00:00";
	}

	public static String getEndDateInOAuthFormat(String inputDateString) throws ParseException {
		DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat oauthFormat = new SimpleDateFormat("yyyy-MM-dd");

		return oauthFormat.format(originalFormat.parse(inputDateString)) + "T23:59:59";
	}

	public JSONObject readInternalUser(String UserID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject updateInternalUser(String UserID, String StatusID, DataControllerRequest requestInstance) {
		Map<String, String> headerParametersMap = new HashMap<String, String>();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("id", UserID);
		postParametersMap.put("Status_id", StatusID);
		postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

		String updateEndpointResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_UPDATE, postParametersMap,
				headerParametersMap, requestInstance);
		return CommonUtilities.getStringAsJSONObject(updateEndpointResponse);

	}

	public JSONObject readUserDetails(String UserID, DataControllerRequest requestInstance) {
		Map<String, String> headerParametersMap = new HashMap<String, String>();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + UserID + "'");
		postParametersMap.put(ODataQueryConstants.SELECT, "id,Status_id,Username,Email,FirstName,MiddleName,LastName");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_VIEW_READ,
				postParametersMap, headerParametersMap, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
	}

	private Param createInternalUser(SystemUserBean sysUserBean, String claimsToken,
			DataControllerRequest requestInstance) {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("Status_id", sysUserBean.getStatus_id());
		postParametersMap.put("Username", sysUserBean.getUsername());
		postParametersMap.put("Password", sysUserBean.getUsername());
		postParametersMap.put("Email", sysUserBean.getEmail());
		postParametersMap.put("FirstName", sysUserBean.getFirstName());
		postParametersMap.put("LastName", sysUserBean.getLastName());
		postParametersMap.put("MiddleName", sysUserBean.getMiddleName());
		postParametersMap.put("createdby", sysUserBean.getCurrUser());
		postParametersMap.put("modifiedby", sysUserBean.getCurrUser());
		postParametersMap.put("FailedCount", SOFT_DELETE_FLAG);

		Param userUUIDParam;

		String createUserResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_CREATE, postParametersMap, null,
				requestInstance);

		JSONObject userCreationResponseJSON = CommonUtilities.getStringAsJSONObject(createUserResponse);
		int getOpStatusCode = userCreationResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.CREATE,
					ActivityStatusEnum.SUCCESSFUL, "User created successfully. Username:" + sysUserBean.getUsername());
			userUUIDParam = new Param("UUID", id, FabricConstants.STRING);
		} else {
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS, EventEnum.CREATE,
					ActivityStatusEnum.FAILED, "User creation failed. Username:" + sysUserBean.getUsername());
			userUUIDParam = new Param("UUID", "ERROR", FabricConstants.STRING);
		}
		return userUUIDParam;

	}

	private Param createAddress(AddressBean addBean, SystemUserBean sysUserBean, String claimsToken,
			DataControllerRequest requestInstance) {
		Param addressID;
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.clear();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("cityName", addBean.getCity_id());
		postParametersMap.put("Region_id", addBean.getRegion_id());
		postParametersMap.put("addressLine1", addBean.getAddressLine1());
		postParametersMap.put("addressLine2", addBean.getAddressLine2());
		postParametersMap.put("addressLine3", addBean.getAddressLine3());
		postParametersMap.put("zipCode", addBean.getZipCode());
		postParametersMap.put("createdby", sysUserBean.getCurrUser());
		postParametersMap.put("modifiedby", sysUserBean.getCurrUser());
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);

		String createAddressResponse = Executor.invokeService(ServiceURLEnum.ADDRESS_CREATE, postParametersMap, null,
				requestInstance);

		JSONObject addressCreationResponseJSON = CommonUtilities.getStringAsJSONObject(createAddressResponse);
		int getOpStatusCode = addressCreationResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			addressID = new Param("UUID", id, FabricConstants.STRING);
		} else {
			addressID = new Param("UUID", "ERROR", FabricConstants.STRING);
		}
		return addressID;

	}

	private Param createUserAddress(String UserID, String AddID, String AddType, SystemUserBean sysUserBean,
			String claimsToken, DataControllerRequest requestInstance) {
		Param statusParam;
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.clear();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("User_id", UserID);
		postParametersMap.put("Address_id", AddID);
		postParametersMap.put("Type_id", AddType);
		postParametersMap.put("createdby", sysUserBean.getCurrUser());
		postParametersMap.put("modifiedby", sysUserBean.getCurrUser());

		String createUserAddressResponse = Executor.invokeService(ServiceURLEnum.USERADDRESS_CREATE, postParametersMap,
				null, requestInstance);

		JSONObject userAddressCreationResponseJSON = CommonUtilities.getStringAsJSONObject(createUserAddressResponse);
		int getOpStatusCode = userAddressCreationResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			statusParam = new Param("Status", "Success", FabricConstants.STRING);
		} else {
			statusParam = new Param("Status", "Error", FabricConstants.STRING);
		}
		return statusParam;
	}

	private Param assignroleToUser(String UserID, String roleId, SystemUserBean sysUserBean, String claimsToken,
			DataControllerRequest requestInstance) {
		Param statusParam;
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.clear();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("User_id", UserID);
		postParametersMap.put("Role_id", roleId);
		postParametersMap.put("hasSuperAdminPrivilages", SOFT_DELETE_FLAG);
		postParametersMap.put("createdby", sysUserBean.getCurrUser());
		postParametersMap.put("modifiedby", sysUserBean.getCurrUser());

		String createUserRoleResponse = Executor.invokeService(ServiceURLEnum.USERROLE_CREATE, postParametersMap, null,
				requestInstance);

		JSONObject userAddressRoleResponseJSON = CommonUtilities.getStringAsJSONObject(createUserRoleResponse);
		int getOpStatusCode = userAddressRoleResponseJSON.getInt(FabricConstants.OPSTATUS);
		if (getOpStatusCode == 0) {
			statusParam = new Param("Status", "Success", FabricConstants.STRING);
		} else {
			statusParam = new Param("Status", "Error", FabricConstants.STRING);
		}
		return statusParam;
	}

	private Param assignPermissionsToUser(JSONArray permission_ids, String claimsToken, SystemUserBean sysUserBean,
			String User_id, DataControllerRequest requestInstance) {
		Param statusParam = new Param("Status", "Success", FabricConstants.STRING);
		permission_ids.get(0);
		Map<String, String> postParametersMap = new HashMap<String, String>();
		for (int i = 0; i < permission_ids.length(); i++) {
			postParametersMap.clear();
			String id = CommonUtilities.getNewId().toString();
			String Permission_id = permission_ids.getString(i).toString();
			postParametersMap.put("id", id.toString());
			postParametersMap.put("Permission_id", Permission_id);
			postParametersMap.put("User_id", User_id.toString());
			postParametersMap.put("createdby", sysUserBean.getCurrUser());
			postParametersMap.put("modifiedby", sysUserBean.getCurrUser());

			String createUserPermissionResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSION_CREATE,
					postParametersMap, null, requestInstance);
			JSONObject userPermisionJSON = CommonUtilities.getStringAsJSONObject(createUserPermissionResponse);
			int getOpStatusCode = userPermisionJSON.getInt(FabricConstants.OPSTATUS);

			if (getOpStatusCode == 0) {
				statusParam = new Param("UserPermissionStatus", "Success", FabricConstants.STRING);
			} else {
				statusParam = new Param("UserPermissionStatus", "Error", FabricConstants.STRING);
				return statusParam;
			}
		}
		return statusParam;
	}

	public String changedItems(JSONObject UserDetails, String firstName, String middleName, String LastName,
			String email, String username, String roleID, String roleName, JSONArray listOfAddedPermissions,
			JSONArray listOfRemovedPermissions, JSONArray listOfAddedPermissionsNames,
			JSONArray listOfRemovedPermissionsNames, String lineOfBusiness, List<String> addLobs,
			List<String> removeLobs, String userType, String reportingManager, boolean isKeyCloakEnabled) {

		String changedFields = "<br><b>Changed Fields</b><br>";
		String check = changedFields;

		String previousFirstName = UserDetails.has("FirstName") ? UserDetails.getString("FirstName") : "";
		if (isChanged(previousFirstName, firstName))
			changedFields += "<br><b> First name:</b> '" + previousFirstName + "'  <b>CHANGED TO</b>  '" + firstName
					+ "'";

		String previousMiddleName = UserDetails.has("MiddleName") ? UserDetails.getString("MiddleName") : "";
		if (isChanged(previousMiddleName, middleName))
			changedFields += "<br><b> Middle name:</b> '" + previousMiddleName + "'  <b>CHANGED TO</b>  '" + middleName
					+ "'";

		String previousLastName = UserDetails.has("LastName") ? UserDetails.getString("LastName") : "";
		if (isChanged(previousLastName, LastName))
			changedFields += "<br><b>Last name:</b> '" + previousLastName + "' <b>CHANGED TO</b>  '" + LastName + "'";

		String previousEmail = UserDetails.has("Email") ? UserDetails.getString("Email") : "";
		if (isChanged(previousEmail, email))
			changedFields += "<br><b>Email:</b> " + previousEmail + "  <b>CHANGED TO</b>  '" + email + "'";

		// String previousUserName = UserDetails.has("Username") ?
		// UserDetails.getString("Username") : "";
		// if (isChanged(previousUserName, username))
		// changedFields += "<br><b>Username:</b> " + previousUserName + " <b>CHANGED
		// TO</b> '" + username + "'";

		String previousUserType = UserDetails.has("UserType") ? UserDetails.getString("UserType") : "";
		if (isChanged(previousUserType, userType))
			changedFields += "<br><b> UserType:</b> '" + previousUserType + "'  <b>CHANGED TO</b>  '" + userType + "'";

		String previousReportingManager = UserDetails.has("ReportingManager")
				? UserDetails.getString("ReportingManager")
				: "";
		if (isChanged(previousReportingManager, reportingManager))
			changedFields += "<br><b> ReportingManager:</b> '" + previousReportingManager + "'  <b>CHANGED TO</b>  '"
					+ reportingManager + "'";

		setLobs(UserDetails, changedFields, lineOfBusiness, addLobs, removeLobs);

		if (!isKeyCloakEnabled) {
			String previousRoleID = UserDetails.has("Role_id") ? UserDetails.getString("Role_id") : "";
			String previousRoleName = UserDetails.has("Role_Name") ? UserDetails.getString("Role_Name") : "";
			if (isChanged(previousRoleID, roleID)) {
				if (roleID.equals("")) {
					changedFields += "<br><b>Role:</b> '" + previousRoleName + "' <b>REMOVED</b>";
				} else {
					changedFields += "<br><b>Role:</b> '" + previousRoleName + "'  <b>CHANGED TO</b>  '" + roleName
							+ "'";
				}

			}

			if (listOfAddedPermissions != null && listOfAddedPermissions.length() > 0) {
				changedFields += "<br><b>Added Permissions:</b> " + listOfAddedPermissionsNames.join(", ");
			}

			if (listOfRemovedPermissions != null && listOfRemovedPermissions.length() > 0) {
				changedFields += "<br><b>Removed Permissions:</b> " + listOfRemovedPermissionsNames.join(", ");
			}
		}

		if (changedFields.equals(check))
			return "<br><b>Nothing changed</b><br>";

		return changedFields + "<br>";
	}

	public boolean isChanged(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		} else if (str1.equals(str2)) {
			return false;
		}
		return true;
	}

	private void setLobs(JSONObject UserDetails, String changedFields, String lineOfBusiness, List<String> addLobs,
			List<String> removeLobs) {
		String previousLobs = UserDetails.has("lobId") ? UserDetails.getString("lobId") : "";
		String[] previousLobsArr = previousLobs.split(",");
		String[] inputLobsArr = lineOfBusiness.split(",");
		if (!previousLobs.equals(lineOfBusiness)) {
			for (String inputLob : inputLobsArr) {
				if (!previousLobs.contains(inputLob)) {
					addLobs.add(inputLob);
				}
			}

			for (String previousLob : previousLobsArr) {
				if (!lineOfBusiness.contains(previousLob)) {
					removeLobs.add(previousLob);
				}
			}

			if (addLobs != null && addLobs.size() > 0) {
				changedFields += "<br><b>addlobId:</b> '" + addLobs;
			}
			if (removeLobs != null && removeLobs.size() > 0) {
				changedFields += "<br><b>removelobId:</b> '" + removeLobs;
			}

		}
	}

	public JSONObject readInternalUser(String AuthToken, String UserID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject readInternalUserKC(String AuthToken, String UserID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		JSONObject userJsonResult = null;
		String readEndpointResponse = "";

		if (responseUserJSONArray == null || responseUserJSONArray.length() == 0) {
			readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERSKC_VIEW_READ, postParametersMap,
					null, requestInstance);
			JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
			responseUserJSONArray = responseJSON.getJSONArray("internaluserskc_view");
		}
		for (int i = 0; i < responseUserJSONArray.length(); i++) {
			JSONObject userJson = responseUserJSONArray.getJSONObject(i);

			userJsonResult = new JSONObject();
			JSONArray userJsonArray = new JSONArray();
			if (userJson.getString("User_id").equals(UserID)) {
				userJsonArray.put(userJson);
				userJsonResult.put("internaluserskc_view", userJsonArray);
				break;
			}
		}
		if (!userJsonResult.has("internaluserskc_view")) {
			userJsonResult.put("internaluserskc_view", new JSONArray());
		}
		return userJsonResult;
	}

	public void deletePermissions(String authToken, JSONArray listOfRemovedPermissions, String userID, String username,
			DataControllerRequest requestInstance, HashMap<String, ArrayList<Permission>> compositePermissionMapping) {
		ArrayList<Permission> currentChildPermissions;
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("User_id", userID);
		for (int indexVar = 0; indexVar < listOfRemovedPermissions.length(); indexVar++) {
			String permissionID = listOfRemovedPermissions.getString(indexVar);
			postParametersMap.put("Permission_id", permissionID);

			// If the current permission is composite, then the corresponding child
			// permissions are to be removed
			if (compositePermissionMapping.containsKey(permissionID)) {
				currentChildPermissions = compositePermissionMapping.get(permissionID);
				for (Permission currPermission : currentChildPermissions) {
					postParametersMap.put("CompositePermission_id", currPermission.getId());
					Executor.invokeService(ServiceURLEnum.ROLECOMPOSITEPERMISSION_DELETE, postParametersMap, null,
							requestInstance);
				}
			}
			postParametersMap.remove("CompositePermission_id");

			Executor.invokeService(ServiceURLEnum.USERPERMISSION_DELETE, postParametersMap, null, requestInstance);
		}
	}

	public void createPermissions(String authToken, String modifiedByName, JSONArray listOfAddedPermissions,
			String userID, String username, DataControllerRequest requestInstance,
			HashMap<String, ArrayList<Permission>> compositePermissionMapping) {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		ArrayList<Permission> currentChildPermissions;
		postParametersMap.put("User_id", userID);
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
		postParametersMap.put("modifiedby", modifiedByName);
		for (int indexVar = 0; indexVar < listOfAddedPermissions.length(); indexVar++) {
			String permissionID = listOfAddedPermissions.getString(indexVar);

			// If the current permission is composite, then the corresponding child
			// permissions are to be added
			if (compositePermissionMapping.containsKey(permissionID)) {
				currentChildPermissions = compositePermissionMapping.get(permissionID);
				for (Permission currPermission : currentChildPermissions) {
					postParametersMap.put("CompositePermission_id", currPermission.getId());
					if (currPermission.isEnabled()) {
						postParametersMap.put("isEnabled", "1");
					} else {
						postParametersMap.put("isEnabled", "0");
					}
					Executor.invokeService(ServiceURLEnum.USERCOMPOSITEPERMISSION_CREATE, postParametersMap, null,
							requestInstance);
				}
			}
			postParametersMap.remove("CompositePermission_id");

			postParametersMap.put("Permission_id", permissionID);
			Executor.invokeService(ServiceURLEnum.USERPERMISSION_CREATE, postParametersMap, null, requestInstance);
		}
	}

	public void deletePermissionsAndActions(String authToken, JSONArray listOfRemovedPermissions, String userID,
			String username, DataControllerRequest requestInstance) throws ApplicationException {
		List<String> permissionsList = new ArrayList<String>();
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("_userId", userID);
		for (int indexVar = 0; indexVar < listOfRemovedPermissions.length(); indexVar++) {
			String permissionID = listOfRemovedPermissions.getString(indexVar);
			permissionsList.add(permissionID);
		}
		String permissions = StringUtils.join(permissionsList.toArray(), ',');
		postParametersMap.put("_PermissionIds", permissions);
		String getDeleteResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSIONDELETE_PROC, postParametersMap,
				null, requestInstance);
		JSONObject getDeleteResponseJson = CommonUtilities.getStringAsJSONObject(getDeleteResponse);
		if (getDeleteResponseJson == null || !getDeleteResponseJson.has(FabricConstants.OPSTATUS)
				|| getDeleteResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20527);
		}
	}

	/*
	 * @author Saikiran.Kaladi Updated createPermissionsAndActions Method. Optimized
	 * the CRUD calls triggering inside this method into StoredProcedure --
	 * userPermissionsAndActionsCreate_proc.
	 * 
	 */
	public void createPermissionsAndActions(String authToken, String modifiedByName, JSONArray listOfAddedPermissions,
			String userID, String username, DataControllerRequest requestInstance,
			HashMap<String, ArrayList<Action>> compositeActionMapping) throws ApplicationException {
		Map<String, String> postParametersMap = new HashMap<String, String>();

		postParametersMap.put("_userId", userID);
		postParametersMap.put("_loggedInUserId", modifiedByName);
		List<String> permissionsList = new ArrayList<String>();
		for (int indexVar = 0; indexVar < listOfAddedPermissions.length(); indexVar++) {
			String permissionID = listOfAddedPermissions.getString(indexVar);
			permissionsList.add(permissionID);
		}
		String permissions = StringUtils.join(permissionsList.toArray(), ',');
		postParametersMap.put("_listOfAddPermissions", permissions);

		String getCreateResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSIONCREATE_PROC, postParametersMap,
				null, requestInstance);
		JSONObject getCreateResponseJson = CommonUtilities.getStringAsJSONObject(getCreateResponse);

		if (getCreateResponseJson == null || !getCreateResponseJson.has(FabricConstants.OPSTATUS)
				|| getCreateResponseJson.getInt(FabricConstants.OPSTATUS) != 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_20527);
		}

	}

	/*
	 * public void createPermissionsAndActions(String authToken, String
	 * modifiedByName, JSONArray listOfAddedPermissions, String userID, String
	 * username, DataControllerRequest requestInstance, HashMap<String,
	 * ArrayList<Action>> compositeActionMapping) { Map<String, String>
	 * postParametersMap = new HashMap<String, String>(); Map<String, String>
	 * userActionMap = new HashMap<>(); ArrayList<Action> currentChildActions;
	 * postParametersMap.put("User_id", userID); postParametersMap.put("createdby",
	 * modifiedByName); postParametersMap.put("createdts",
	 * CommonUtilities.getISOFormattedLocalTimestamp());
	 * postParametersMap.put("modifiedby", modifiedByName); Map<String,Boolean>
	 * actionEnabledMap = new HashMap<String, Boolean>(); for (int indexVar = 0;
	 * indexVar < listOfAddedPermissions.length(); indexVar++) { String permissionID
	 * = listOfAddedPermissions.getString(indexVar);
	 * 
	 * // If the current permission is composite, then the corresponding child //
	 * permissions are to be added if
	 * (compositeActionMapping.containsKey(permissionID)) { currentChildActions =
	 * compositeActionMapping.get(permissionID); for (Action currAction :
	 * currentChildActions) {
	 * 
	 * postParametersMap.put("CompositeAction_id", currAction.getId()); String
	 * filter = "CompositeAction_id eq '" + currAction.getId() + "'" +
	 * " and User_id eq '" + userID + "'";
	 * userActionMap.put(ODataQueryConstants.FILTER, filter); String readEndpoint =
	 * Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_READ,
	 * userActionMap, null, requestInstance); JSONObject readEndpointResponse =
	 * CommonUtilities.getStringAsJSONObject(readEndpoint); JSONObject currJson =
	 * null; if (readEndpointResponse != null &&
	 * readEndpointResponse.has(FabricConstants.OPSTATUS) &&
	 * readEndpointResponse.getInt(FabricConstants.OPSTATUS) == 0) { JSONArray
	 * userActionJSONArray =
	 * readEndpointResponse.getJSONArray("usercompositeaction"); if
	 * (userActionJSONArray.length() > 0) { currJson =
	 * userActionJSONArray.optJSONObject(0); } } if (currAction.isEnabled() ||
	 * (actionEnabledMap.containsKey(currAction.getId()) &&
	 * actionEnabledMap.get(currAction.getId()))) {
	 * postParametersMap.put("isEnabled", "1"); } else {
	 * postParametersMap.put("isEnabled", "0"); } if (currJson == null) {
	 * Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_CREATE,
	 * postParametersMap, null, requestInstance); } else
	 * if((postParametersMap.get("isEnabled")=="1") &&
	 * (actionEnabledMap.containsKey(currAction.getId())) &&
	 * !(actionEnabledMap.get(currAction.getId()))) {
	 * Executor.invokeService(ServiceURLEnum.USERCOMPOSITEACTION_UPDATE,
	 * postParametersMap, null, requestInstance); }
	 * actionEnabledMap.put(currAction.getId(),
	 * postParametersMap.get("isEnabled")=="1"?true:false); } }
	 * postParametersMap.remove("CompositeAction_id");
	 * 
	 * postParametersMap.put("Permission_id", permissionID);
	 * Executor.invokeService(ServiceURLEnum.USERPERMISSION_CREATE,
	 * postParametersMap, null, requestInstance); } }
	 * 
	 */
	public JSONObject getRecordsForUserIDFromUserRole(String AuthToken, String UserID,
			DataControllerRequest requestInstance) {
		Map<String, String> headerParametersMap = new HashMap<String, String>();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERROLE_READ, postParametersMap,
				headerParametersMap, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject getAllRecordsForUserID(String AuthToken, String UserID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + UserID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERPERMISSION_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject createUserRole(String authToken, String modifiedByName, String userID, String username,
			String roleID, DataControllerRequest requestInstance) {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("User_id", userID);
		postParametersMap.put("Role_id", roleID);
		if (roleID.equalsIgnoreCase("RID_SUPERADMIN")) {
			postParametersMap.put("hasSuperAdminPrivilages", "1");
		} else {
			postParametersMap.put("hasSuperAdminPrivilages", "0");
		}

		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("lastmodifedts", CommonUtilities.getISOFormattedLocalTimestamp());
		postParametersMap.put("synctimestamp", CommonUtilities.getISOFormattedLocalTimestamp());
		postParametersMap.put("softdeleteflag", "0");
		String createResponse = Executor.invokeService(ServiceURLEnum.USERROLE_CREATE, postParametersMap, null,
				requestInstance);
		JSONObject createResponseJSON = CommonUtilities.getStringAsJSONObject(createResponse);
		return createResponseJSON;

	}

	public JSONObject deleteUserRoles(String authToken, String userID, String username, String roleID,
			DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("Role_id", roleID);
		postParametersMap.put("User_id", userID);
		String deleteResponse = Executor.invokeService(ServiceURLEnum.USERROLE_DELETE, postParametersMap, null,
				requestInstance);
		JSONObject deleteResponseJSON = CommonUtilities.getStringAsJSONObject(deleteResponse);
		return deleteResponseJSON;
	}

	public JSONObject updateHomeAddress(String authToken, String modifiedByName, String username, String addressID,
			String addr1, String addr2, String cityID, String stateID, String countryID, String zipcode,
			DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("id", addressID);
		postParametersMap.put("addressLine1", addr1);
		postParametersMap.put("addressLine2", addr2);
		postParametersMap.put("Region_id", stateID);
		postParametersMap.put("cityName", cityID);
		postParametersMap.put("zipCode", zipcode);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

		String updateEndpointResponse = Executor.invokeService(ServiceURLEnum.ADDRESS_UPDATE, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(updateEndpointResponse);

	}

	public JSONObject createHomeAddress(String AuthToken, String modifiedByName, String username, String userID,
			String addr1, String addr2, String cityID, String stateID, String countryID, String zipcode,
			DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		String id = CommonUtilities.getNewId().toString();
		postParametersMap.clear();
		postParametersMap.put("id", id.toString());
		postParametersMap.put("cityName", cityID);
		postParametersMap.put("Region_id", stateID);
		postParametersMap.put("addressLine1", addr1);
		postParametersMap.put("addressLine2", addr2);
		postParametersMap.put("zipCode", zipcode);
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);

		Executor.invokeService(ServiceURLEnum.ADDRESS_CREATE, postParametersMap, null, requestInstance);
		// create user home address
		postParametersMap.clear();
		postParametersMap.put("User_id", userID);
		postParametersMap.put("Address_id", id);
		postParametersMap.put("Type_id", "ADR_TYPE_HOME");
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);
		String createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERADDRESS_CREATE, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(createEndpointResponse);

	}

	public JSONObject updateWorkAddress(String AuthToken, String modifiedByName, String userID, String username,
			String addressID, DataControllerRequest requestInstance) {

		// read user address for the work address entry
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userID + "' and Type_id eq 'ADR_TYPE_WORK'");
		String readUserAddress = Executor.invokeService(ServiceURLEnum.USERADDRESS_READ, postParametersMap, null,
				requestInstance);
		JSONObject userAddress = CommonUtilities.getStringAsJSONObject(readUserAddress);

		if (((JSONArray) userAddress.get("useraddress")).length() > 0) {
			// Remove user address entry
			postParametersMap.clear();
			postParametersMap.put("User_id", userID);
			postParametersMap.put("Address_id",
					((JSONObject) ((JSONArray) userAddress.get("useraddress")).get(0)).getString("Address_id"));
			postParametersMap.put("Type_id", "ADR_TYPE_WORK");
			Executor.invokeService(ServiceURLEnum.USERADDRESS_DELETE, postParametersMap, null, requestInstance);
		}
		// Add user address entry
		postParametersMap.clear();
		postParametersMap.put("User_id", userID);
		postParametersMap.put("Address_id", addressID);
		postParametersMap.put("Type_id", "ADR_TYPE_WORK");
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);
		String createEndpointResponse = Executor.invokeService(ServiceURLEnum.USERADDRESS_CREATE, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(createEndpointResponse);

	}

	public JSONObject updateUserManager(String AuthToken, String modifiedByName, String userID, String userMananger,
			DataControllerRequest requestInstance) {

		// read user address for the work address entry
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "userId eq '" + userID + "'");
		String readUserManager = Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_READ, postParametersMap,
				null, requestInstance);
		JSONObject userManager = CommonUtilities.getStringAsJSONObject(readUserManager);

		if (((JSONArray) userManager.get("internalusermanager")).length() > 0) {
			// Remove user address entry
			postParametersMap.clear();
			postParametersMap.put("userId", userID);
			postParametersMap.put("manager",
					((JSONObject) ((JSONArray) userManager.get("internalusermanager")).get(0)).getString("manager"));
			Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_DELETE, postParametersMap, null, requestInstance);
		}
		// Add user address entry
		postParametersMap.clear();
		postParametersMap.put("userId", userID);
		postParametersMap.put("manager", userMananger);
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);
		String createEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_CREATE,
				postParametersMap, null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(createEndpointResponse);

	}

	public JSONObject updateUserType(String AuthToken, String modifiedByName, String userID, String userType,
			DataControllerRequest requestInstance) {

		// read user address for the work address entry
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "userId eq '" + userID + "'");
		String readUserType = Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_READ, postParametersMap, null,
				requestInstance);
		JSONObject userTypeJSON = CommonUtilities.getStringAsJSONObject(readUserType);

		if (((JSONArray) userTypeJSON.get("internalusertype")).length() > 0) {
			// Remove user address entry
			postParametersMap.clear();
			postParametersMap.put("userId", userID);
			postParametersMap.put("userType",
					((JSONObject) ((JSONArray) userTypeJSON.get("internalusertype")).get(0)).getString("userType"));
			Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_DELETE, postParametersMap, null, requestInstance);
		}
		// Add user address entry
		postParametersMap.clear();
		postParametersMap.put("userId", userID);
		postParametersMap.put("userType", userType);
		postParametersMap.put("createdby", modifiedByName);
		postParametersMap.put("modifiedby", modifiedByName);
		postParametersMap.put("softdeleteflag", SOFT_DELETE_FLAG);
		String createEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_CREATE,
				postParametersMap, null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(createEndpointResponse);

	}

	public JSONObject updateInternalUser(String AuthToken, String modifiedByName, String userID, String firstName,
			String middleName, String LastName, String email, String username, DataControllerRequest requestInstance) {
		Map<String, String> headerParametersMap = new HashMap<String, String>();

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("id", userID);

		if (StringUtils.isNotBlank(firstName) && !CommonUtilities.containSpecialChars(firstName))
			postParametersMap.put("FirstName", firstName);

		if (StringUtils.isNotBlank(middleName) && !CommonUtilities.containSpecialChars(middleName))
			postParametersMap.put("MiddleName", middleName);

		if (StringUtils.isNotBlank(LastName) && !CommonUtilities.containSpecialChars(LastName))
			postParametersMap.put("LastName", LastName);

		if (StringUtils.isNotBlank(email) && !CommonUtilities.containSpecialChars(email))
			postParametersMap.put("Email", email);

		if (StringUtils.isNotBlank(username) && !CommonUtilities.containSpecialChars(username))
			postParametersMap.put("Username", username);

		if (StringUtils.isNotBlank(modifiedByName) && !CommonUtilities.containSpecialChars(modifiedByName))
			postParametersMap.put("modifiedby", modifiedByName);

		if (postParametersMap.size() > 1) {
			postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

			String updateEndpointResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_UPDATE, postParametersMap,
					headerParametersMap, requestInstance);
			return CommonUtilities.getStringAsJSONObject(updateEndpointResponse);
		}

		return null;
	}

	public JSONObject getBranchAddr(String AuthToken, String branchID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "id eq '" + branchID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.LOCATION_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject getInternalUserAddresses(String AuthToken, String userID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.USERADDRESS_READ, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);

	}

	public JSONObject getUserManager(String AuthToken, String userID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERMANAGER_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
	}

	public JSONObject getUserType(String AuthToken, String userID, DataControllerRequest requestInstance) {

		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put(ODataQueryConstants.FILTER, "User_id eq '" + userID + "'");
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERTYPE_READ, postParametersMap,
				null, requestInstance);
		return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
	}

	private String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str)) {

			return "\"\"";
		} else if (str.contains("\\")) {
			str = str.replace("\\", "\\\\");
		}
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}

	@Override
	public Result getUserList(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		// TODO Auto-generated method stub
		String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		boolean isKeyCloakEnabled = Boolean
				.parseBoolean(ApplicationParametersHandler.fetchIsKeyCloakEnabled(requestInstance));
		if (isKeyCloakEnabled) {

			String keycloakAuthToken = getKeycloakAuthToken(requestInstance);

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("Authorization", "Bearer " + keycloakAuthToken);
			String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_USERS_LIST,
					new HashMap<String, String>(), headerMap, requestInstance);

			JSONObject readSystemUserResponseJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);
			int opStatusCode = readSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS);
			if (opStatusCode == 0) {
				JSONArray systemuserJSONArray = readSystemUserResponseJSON.getJSONArray("internalusers_view");
				for (int indexVar = 0; indexVar < systemuserJSONArray.length(); indexVar++) {
					JSONObject currRecordJSONObject = systemuserJSONArray.getJSONObject(indexVar);
					JSONObject readInternalUserKCResponseJSON = readInternalUserKC(authToken,
							currRecordJSONObject.getString("User_id"), requestInstance);

					JSONObject details = new JSONObject();
					if (((JSONArray) readInternalUserKCResponseJSON.get("internaluserskc_view")).length() > 0) {
						details = (JSONObject) ((JSONArray) readInternalUserKCResponseJSON.get("internaluserskc_view"))
								.get(0);
					}
					long ts = (long) currRecordJSONObject.get("createdts");
					Date date = new Date(ts);
					DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					currRecordJSONObject.put("lastmodifiedts", dFormat.format(date).toString());
					currRecordJSONObject.put("createdts", dFormat.format(date).toString());
					String Status_Desc = getStatusDes(currRecordJSONObject.getString("Status_id"), requestInstance);
					currRecordJSONObject.put("Status_Desc", Status_Desc);
					currRecordJSONObject.put("UserType", details.has("UserType") ? details.getString("UserType") : "");
					currRecordJSONObject.put("ReportingManager",
							details.has("ReportingManager") ? details.getString("ReportingManager") : "");
					currRecordJSONObject.put("lobId", details.has("lobId") ? details.getString("lobId") : "");
					currRecordJSONObject.put("lobName", details.has("lobName") ? details.getString("lobName") : "");
					currRecordJSONObject.put("userTypeName",
							details.has("userTypeName") ? details.getString("userTypeName") : "");
					currRecordJSONObject.put("Work_AddressLine1",
							details.has("Work_AddressLine1") ? details.getString("Work_AddressLine1") : "");
					currRecordJSONObject.put("Work_AddressLine2",
							details.has("Work_AddressLine2") ? details.getString("Work_AddressLine2") : "");
					currRecordJSONObject.put("Work_CityName",
							details.has("Work_CityName") ? details.getString("Work_CityName") : "");
					currRecordJSONObject.put("Work_CityID",
							details.has("Work_CityID") ? details.getString("Work_CityID") : "");
					currRecordJSONObject.put("Work_StateName",
							details.has("Work_StateName") ? details.getString("Work_StateName") : "");
					currRecordJSONObject.put("Work_StateID",
							details.has("Work_StateID") ? details.getString("Work_StateID") : "");
					currRecordJSONObject.put("Work_CountryName",
							details.has("Work_CountryName") ? details.getString("Work_CountryName") : "");
					currRecordJSONObject.put("Work_CountryID",
							details.has("Work_CountryID") ? details.getString("Work_CountryID") : "");
					currRecordJSONObject.put("Work_Zipcode",
							details.has("Work_Zipcode") ? details.getString("Work_Zipcode") : "");
					currRecordJSONObject.put("branchName",
							details.has("branchName") ? details.getString("branchName") : "");
					currRecordJSONObject.put("Work_Addr",
							details.has("Work_Addr") ? details.getString("Work_Addr") : "");
					Map<String, String> map = new HashMap<String, String>();
					map.put("keyCloakUserId", currRecordJSONObject.getString("User_id"));
					String readKeycloakUserrolesResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_ROLES, map,
							headerMap, requestInstance);

					JSONObject readKeycloakUserrolesResponseJson = CommonUtilities
							.getStringAsJSONObject(readKeycloakUserrolesResponse);
					if (readKeycloakUserrolesResponseJson.getInt(FabricConstants.OPSTATUS) == 0) {
						String roleIds = readKeycloakUserrolesResponseJson.getString("roleIds");
						setRoleDesc(roleIds, currRecordJSONObject, authToken, requestInstance);

					} else {
						return constructFailureResult(readKeycloakUserrolesResponseJson);
					}

				}
				return CommonUtilities.getResultObjectFromJSONObject(readSystemUserResponseJSON);
			}
			return constructFailureResult(readSystemUserResponseJSON);

		}
		Map<String, String> postParametersMap = new HashMap<String, String>();
		String readEndpointResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERS_VIEW_READ, postParametersMap,
				null, requestInstance);
		JSONObject readInternalUser = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
		JSONArray userDetails = new JSONArray();
		JSONArray result = readInternalUser.getJSONArray("internalusers_view");
		for (int i = 0; i < result.length(); i++) {
			JSONObject currRole = result.getJSONObject(i);
			JSONObject role = new JSONObject();
			JSONArray roles = new JSONArray();
			role.put("rolename", currRole.optString("Role_Name"));
			role.put("roledesc", currRole.optString("Role_Desc"));
			role.put("roleId", currRole.optString("Role_id"));
			role.put("legalEntityId", currRole.optString("companyLegalUnit"));
			roles.put(role);
			currRole.remove("Role_Name");
			currRole.remove("Role_Desc");
			currRole.remove("Role_id");
			currRole.remove("companyLegalUnit");
			currRole.put("roleDetails", roles);
			userDetails.put(currRole);
		}
		readInternalUser.put("internalusers_view", userDetails);
		return CommonUtilities.getResultObjectFromJSONObject(readInternalUser);
	}

	public String getStatusDes(String Status_id, DataControllerRequest requestInstance) {
		String attributeValue = "";
		String readEndpointResponse = "";

		if (responseStatusJSONArray == null || responseStatusJSONArray.length() == 0) {
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.put(ODataQueryConstants.SELECT, "id,Description");
			readEndpointResponse = Executor.invokeService(ServiceURLEnum.STATUS_READ, postParametersMap, null,
					requestInstance);

			JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(readEndpointResponse);
			responseStatusJSONArray = responseJSON.getJSONArray("status");
		}
		for (int i = 0; i < responseStatusJSONArray.length(); i++) {
			JSONObject statusJson = responseStatusJSONArray.getJSONObject(i);
			if (statusJson.getString("id") == Status_id) {
				attributeValue = statusJson.getString("Description");
				break;
			}
		}
		return attributeValue;
	}

	private void setRoleDesc(String roleIds, JSONObject currRecordJSONObject, String AuthToken,
			DataControllerRequest requestInstance) {
		JSONArray roleJsonArr = new JSONArray();
		if (StringUtils.isNotBlank(roleIds)) {
			String[] roleIdArr = roleIds.split(",");
			String readUserRoleResponse = "";

			if (roleDetailsArray == null || roleDetailsArray.length() == 0) {
				Map<String, String> roleMap = new HashMap<String, String>();
				// roleMap.put(ODataQueryConstants.FILTER, "id eq '" + roleId + "'");
				roleMap.put(ODataQueryConstants.SELECT, "id,Name,Description,companyLegalUnit");
				readUserRoleResponse = Executor.invokeService(ServiceURLEnum.ROLE_READ, roleMap, null, requestInstance);
				JSONObject readUserRoleResponseJson = CommonUtilities.getStringAsJSONObject(readUserRoleResponse);
				roleDetailsArray = ((JSONArray) readUserRoleResponseJson.get("role"));
			}

			for (String roleId : roleIdArr) {
				JSONObject roleJsonObject = new JSONObject();
				StringBuilder legalEntityId = new StringBuilder();
				for (int i = 0; i < roleDetailsArray.length(); i++) {
					JSONObject roleDetails = (JSONObject) roleDetailsArray.get(i);
					if (roleDetails.getString("id").equals(roleId)) {
						String legalEntity = roleDetails.getString("companyLegalUnit");
						legalEntityId.append(legalEntity).append(",");
						roleJsonObject.put("roleId", roleDetails.getString("id"));
						roleJsonObject.put("rolename", roleDetails.getString("Name"));
						roleJsonObject.put("roledesc", roleDetails.getString("Description"));

					}
				}
				roleJsonObject.put("legalEntityId", legalEntityId.substring(0, legalEntityId.length() - 1));
				roleJsonArr.put(roleJsonObject);
			}
		}
		currRecordJSONObject.put("roleDetails", roleJsonArr);
	}

	private void setRoleDetails(JSONObject readInternalUser) {
		JSONObject internalUserDetails = (JSONObject) ((JSONArray) readInternalUser.get("internalusers_view")).get(0);
		JSONArray roleJsonArr = new JSONArray();
		if (StringUtils
				.isNotBlank(internalUserDetails.has("Role_id") ? internalUserDetails.getString("Role_id") : "")) {
			JSONObject rolesJsonObject = new JSONObject();
			rolesJsonObject.put("roleId", internalUserDetails.getString("Role_id"));
			rolesJsonObject.put("rolename", internalUserDetails.getString("Role_Name"));
			rolesJsonObject.put("roledesc", internalUserDetails.getString("Role_Desc"));
			roleJsonArr.put(rolesJsonObject);
		}
		internalUserDetails.put("roleDetails", roleJsonArr);
	}
	
	public String getKeycloakAuthToken(DataControllerRequest request) {
		String keycloakAuthToken = "";
		String getServiceAccountBackendTokenResponse = Executor.invokeService(ServiceURLEnum.KEYCLOAK_TOKEN,
				new HashMap<String, String>(), null, request);
		JSONObject getServiceAccountBackendTokenJson = CommonUtilities
				.getStringAsJSONObject(getServiceAccountBackendTokenResponse);

		keycloakAuthToken = getServiceAccountBackendTokenJson.getString("access_token");
		return keycloakAuthToken;
	}
}

package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * CustomerUpdateBasicInfo service will update the basic information of a
 * customer
 * 
 * @author Alahari Prudhvi Akhil (KH2346)
 * 
 */
public class CustomerUpdateDetails implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();
		try {

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			String customerId = requestInstance.getParameter("Customer_id");
			String customerUsername = StringUtils.isNotBlank(requestInstance.getParameter("userName")) ?
					requestInstance.getParameter("userName") : "";
			String legalEntityId = requestInstance.getParameter("legalEntityId");
			String isCustomerEnrolled = requestInstance.getParameter("isCustomerEnrolled");
			if (StringUtils.isBlank(customerId) && StringUtils.isBlank(customerUsername)) {
				ErrorCodeEnum.ERR_20613.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Edit failure", FabricConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;
			}
			if (!StringUtils.isAlphanumeric(customerId)) {
				ErrorCodeEnum.ERR_20554.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Edit failure", FabricConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;
			}
			if(StringUtils.isBlank(legalEntityId)) {
				ErrorCodeEnum.ERR_22232.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Edit failure", FabricConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;
			}
			if(!StringUtils.isAlphanumeric(legalEntityId)) {
				ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
				Param statusParam = new Param("Status", "Edit failure", FabricConstants.STRING);
				processedResult.addParam(statusParam);
				return processedResult;
			}
			String[] reqPermissions = {PermissionName.UPDATE_CUSTOMER_CONTACT};
			if (!LoggedInUserHandler.hasAccessToLegalEntity(requestInstance, reqPermissions)) {
				processedResult.addParam(new Param("Status", "Edit failure Access Denied", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(processedResult);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return processedResult;
			}
			if (LoggedInUserHandler.getUserDetails(requestInstance).isAPIUser() == false) {
				// Check the access control for this customer for current logged-in internal
				// user
				if (StringUtils.isNotBlank(isCustomerEnrolled) && isCustomerEnrolled.equalsIgnoreCase("true")) {
					CustomerHandler.doesCurrentLoggedinUserHasAccessToCustomer(null, customerId, requestInstance,
							processedResult);
					if (processedResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						return processedResult;
					}
					// End of access check
				}
			}
			Map<String, String> postParametersMap = new HashMap<String, String>();

			String Salutation = requestInstance.getParameter("Salutation");
			String CustomerStatus_id = requestInstance.getParameter("Status_id");

			String MaritialStatus = requestInstance.getParameter("MaritalStatus_id");
			String MiddleName = requestInstance.getParameter("MiddleName");
			String DrivingLicenseNumber = requestInstance.getParameter("DrivingLicenseNumber");
			String EmployementStatus_id = requestInstance.getParameter("EmployementStatus_id");
			String SpouseName = requestInstance.getParameter("SpouseName");
			String modifiedByName = userDetailsBeanInstance.getUserName();
			String eagreementStatus = requestInstance.getParameter("eagreementStatus");
			JSONArray listOfAddedRisks = null;
			String identities = null, phoneNumbers = null, Addresses = null, EmailIds = null;
			JSONArray listOfRemovedRisks = null;


			postParametersMap.put("Customer_id", customerId);
			postParametersMap.put("legalEntityId", legalEntityId);
			
			if(StringUtils.isNotBlank(CustomerStatus_id)){
				postParametersMap.put("Status_id", CustomerStatus_id);
			}
						
			if(StringUtils.isNotBlank(Salutation)){
				postParametersMap.put("Salutation", Salutation);
			}
			
			if(StringUtils.isNotBlank(MaritialStatus)){
				postParametersMap.put("MaritalStatus_id", MaritialStatus);
			}
			
			
			if(StringUtils.isNotBlank(MiddleName)){
				postParametersMap.put("MiddleName", MiddleName);
			}
			
			if(StringUtils.isNotBlank(DrivingLicenseNumber)){
				postParametersMap.put("DrivingLicenseNumber", DrivingLicenseNumber);
			}
			
			if(StringUtils.isNotBlank(SpouseName)){
				postParametersMap.put("SpouseName", SpouseName);
			}
			
			if(StringUtils.isNotBlank(EmployementStatus_id)){
				postParametersMap.put("EmployementStatus_id", EmployementStatus_id);
			}
			
			if(StringUtils.isNotBlank(modifiedByName)){
				postParametersMap.put("ModifiedByName", modifiedByName);
			}
			
			if(StringUtils.isNotBlank(eagreementStatus)){
				postParametersMap.put("eagreementStatus", eagreementStatus);
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("BankingAccess"))){
				postParametersMap.put("BankingAccess", requestInstance.getParameter("BankingAccess"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("newUserName"))){
				postParametersMap.put("newUserName", requestInstance.getParameter("newUserName"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("preferredContactTime"))){
				postParametersMap.put("preferredContactTime", requestInstance.getParameter("preferredContactTime"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("preferredContactMethod"))){
				postParametersMap.put("preferredContactMethod", requestInstance.getParameter("preferredContactMethod"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("deleteAddressID"))){
				postParametersMap.put("deleteAddressID", requestInstance.getParameter("deleteAddressID"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("deleteCommunicationID"))){
				postParametersMap.put("deleteCommunicationID", requestInstance.getParameter("deleteCommunicationID"));
			}
			
			if(StringUtils.isNotBlank(requestInstance.getParameter("TaxId"))){
				postParametersMap.put("TaxId", requestInstance.getParameter("TaxId"));
			}

			if (requestInstance.getParameter("listOfRemovedRisks") != null) {
				listOfRemovedRisks = new JSONArray(requestInstance.getParameter("listOfRemovedRisks"));
			}
			if (requestInstance.getParameter("listOfAddedRisks") != null) {
				listOfAddedRisks = new JSONArray(requestInstance.getParameter("listOfAddedRisks"));
			}
			if (listOfRemovedRisks != null) {
				postParametersMap.put("listOfRemovedRisks", listOfRemovedRisks.toString());
			}
			if (listOfAddedRisks != null) {
				postParametersMap.put("listOfAddedRisks", listOfAddedRisks.toString());
			}
			if (requestInstance.getParameter("identities") != null) {
				identities = requestInstance.getParameter("identities");
				postParametersMap.put("identities", identities);

			}
			if (requestInstance.getParameter("phoneNumbers") != null) {
				phoneNumbers = requestInstance.getParameter("phoneNumbers");
				postParametersMap.put("phoneNumbers", stringifyForVelocityTemplate(phoneNumbers));
			}
			if (requestInstance.getParameter("EmailIds") != null) {
				EmailIds = requestInstance.getParameter("EmailIds");
				postParametersMap.put("EmailIds", stringifyForVelocityTemplate(EmailIds));
			}
			if (requestInstance.getParameter("Addresses") != null) {
				Addresses = requestInstance.getParameter("Addresses");
				postParametersMap.put("Addresses", stringifyForVelocityTemplate(Addresses));
			}
			postParametersMap.put("userName", customerUsername);
			alert.prepareError("Postparametermap : "+postParametersMap).log();
			JSONObject getResponseJSON = DBPServices.updateCustomerBasicInfo(requestInstance, postParametersMap);
			if (getResponseJSON == null  || !getResponseJSON.has(FabricConstants.OPSTATUS)
					|| getResponseJSON.getInt(FabricConstants.OPSTATUS) != 0) {
				
				ErrorCodeEnum.ERR_22087.setErrorCode(processedResult);
				processedResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Customer Contact Update failed: "+ customerId );
			} else if (getResponseJSON.has("errmsg") || getResponseJSON.has("dbpErrMsg")) {
				processedResult.addParam(new Param("status", "Failure", FabricConstants.STRING));
				processedResult.addParam(new Param("dbpErrMsg", getResponseJSON.getString("errmsg"), FabricConstants.STRING));
				processedResult.addParam(new Param("dbpErrCode", getResponseJSON.getString("dbpErrCode"), FabricConstants.STRING));
				processedResult.addParam(new Param("errmsg", getResponseJSON.getString("errmsg"), FabricConstants.STRING));
				if (getResponseJSON.has("dbpErrCode")) {
					processedResult.addParam(new Param("dbpErrCode", getResponseJSON.getString("dbpErrCode"), FabricConstants.STRING));
				}
				
            } else {
				Param statusParam = new Param("Status", "Edit successful", FabricConstants.STRING);
				processedResult.addParam(statusParam);
			}
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
		return processedResult;
	}

	public void deleteRisks(String AuthToken, String modifiedByName, JSONArray listOfRemovedRisks, String CustomerID,
			DataControllerRequest requestInstance) {

		for (int indexVar = 0; indexVar < listOfRemovedRisks.length(); indexVar++) {
			String riskID = listOfRemovedRisks.getString(indexVar);
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.clear();
			postParametersMap.put("Status_id", riskID);
			postParametersMap.put("Customer_id", CustomerID);

			Executor.invokeService(ServiceURLEnum.CUSTOMERFLAGSTATUS_DELETE, postParametersMap, null, requestInstance);
		}
	}

	public JSONObject createRisks(String AuthToken, String modifiedByName, JSONArray listOfAddedRisks,
			String CustomerID, DataControllerRequest requestInstance) {

		JSONObject createResponseJSON = null;
		for (int indexVar = 0; indexVar < listOfAddedRisks.length(); indexVar++) {
			String riskID = listOfAddedRisks.getString(indexVar);
			Map<String, String> postParametersMap = new HashMap<String, String>();
			postParametersMap.clear();
			postParametersMap.put("Status_id", riskID);
			postParametersMap.put("Customer_id", CustomerID);
			postParametersMap.put("createdby", modifiedByName);
			postParametersMap.put("modifiedby", modifiedByName);
			String addResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERFLAGSTATUS_CREATE, postParametersMap,
					null, requestInstance);
			createResponseJSON = CommonUtilities.getStringAsJSONObject(addResponse);
		}
		return createResponseJSON;
	}

	private  String stringifyForVelocityTemplate(String str) {
		if (StringUtils.isBlank(str)) {

			return "\"\"";
		}else if(str.contains("\\")) {
			str= str.replace("\\","\\\\" );
		}
		return "\"" + str.replace("\"", "\\\"") + "\"";
	}
}

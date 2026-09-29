package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CustomerInfoUpdateService implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String Customer_id = requestInstance.getParameter("Customer_id");
			String isCustomerEnrolled = requestInstance.getParameter("isCustomerEnrolled");
			result.addParam("Enrollment Status",isCustomerEnrolled);
			
			if(isCustomerEnrolled==null||isCustomerEnrolled.equalsIgnoreCase("true")) {
			// Get the access control for this customer for current logged-in internal user
            Boolean isCustomerAccessiable = CustomerHandler.doesCurrentLoggedinUserHasAccessToGivenCustomer(
                    null, Customer_id, requestInstance, result);
            
            if(!isCustomerAccessiable) {
            	if(null == result.getParamByName("dbpErrMsg")) {
            		ErrorCodeEnum.ERR_21591.setErrorCode(result);
            	}
            	return result;
            }}
			String Salutation = requestInstance.getParameter("Salutation");
			String EmployementStatus_id = requestInstance.getParameter("EmployementStatus_id");
			String eagreementStatus = requestInstance.getParameter("eagreementStatus");
			String MaritalStatus_id = requestInstance.getParameter("MaritalStatus_id");
			String preferredContactMethod = requestInstance.getParameter("PreferredContactMethod");
			String preferredContactTime = requestInstance.getParameter("PreferredContactTime");
			String deleteCommunicationID = requestInstance.getParameter("deleteCommunicationID");
			String deleteAddressID = requestInstance.getParameter("deleteAddressID");
			JSONArray PhoneNumbers = null;
			if (requestInstance.getParameter("PhoneNumbers") != null) {
				PhoneNumbers = new JSONArray(requestInstance.getParameter("PhoneNumbers"));

			}

			JSONArray EmailIds = null;
			if (requestInstance.getParameter("EmailIds") != null) {
				EmailIds = new JSONArray(requestInstance.getParameter("EmailIds"));

			}
			JSONArray Addresses = null;
			if (requestInstance.getParameter("Addresses") != null) {
				Addresses = new JSONArray(requestInstance.getParameter("Addresses"));
			}

			JSONArray listOfRemovedRisks = null;
			if (requestInstance.getParameter("listOfRemovedRisks") != null) {
				listOfRemovedRisks = new JSONArray(requestInstance.getParameter("listOfRemovedRisks"));
			}

			JSONArray listOfAddedRisks = null;
			if (requestInstance.getParameter("listOfAddedRisks") != null) {
				listOfAddedRisks = new JSONArray(requestInstance.getParameter("listOfAddedRisks"));
			}

			Map<String, String> postParametersMap = new HashMap<>();

			if (Customer_id != null) {
				postParametersMap.put("Customer_id", Customer_id);
			}
			if (Salutation != null) {
				postParametersMap.put("Salutation", Salutation);
			}
			if (EmployementStatus_id != null) {
				postParametersMap.put("EmployementStatus_id", EmployementStatus_id);
			}
			if (MaritalStatus_id != null) {
				postParametersMap.put("MaritalStatus_id", MaritalStatus_id);
			}
			if (preferredContactMethod != null) {
				JSONObject updatePreferredTimes = updatePreferredTimes(null, Customer_id, preferredContactMethod,
						preferredContactTime, requestInstance);
				if (updatePreferredTimes != null) {
					result.addParam("Status", "Success", FabricConstants.STRING);
					result.addParam("PreferredContact", updatePreferredTimes.toString());
					return result;

				}
			}
			if (deleteCommunicationID != null) {
				postParametersMap.put("deleteCommunicationID", deleteCommunicationID);
			}
			if (deleteAddressID != null) {
				postParametersMap.put("deleteCommunicationID", deleteCommunicationID);
			}
			if (eagreementStatus != null) {
				postParametersMap.put("eagreementStatus", eagreementStatus);
			}
			if (PhoneNumbers != null) {
				postParametersMap.put("PhoneNumbers", PhoneNumbers.toString());
			}
			if (EmailIds != null) {
				postParametersMap.put("EmailIds", EmailIds.toString());
			}
			if (Addresses != null) {
				postParametersMap.put("Addresses", Addresses.toString());
			}
			if (listOfRemovedRisks != null) {
				postParametersMap.put("listOfRemovedRisks", listOfRemovedRisks.toString());
			}
			if (listOfAddedRisks != null) {
				postParametersMap.put("listOfAddedRisks", listOfAddedRisks.toString());
			}
			Map<String, String> headerMap = new HashMap<>();
			String serviceResponse = Executor.invokeService(ServiceURLEnum.DBPSERVIEC_UPDATECUSTOMERINFO,
					postParametersMap, headerMap, requestInstance);
			JSONObject serviceResponseObject = CommonUtilities.getStringAsJSONObject(serviceResponse);

			if (serviceResponseObject != null && serviceResponseObject.has("success")) {
				result.addParam("Status", "Success", FabricConstants.STRING);
			} else {
				result.addParam("Status", "Edit failure", FabricConstants.STRING);
				ErrorCodeEnum.ERR_20719.setErrorCode(result);
			}
			return result;
		} catch (Exception e) {
			result.addParam("Error msg", e.toString(), FabricConstants.STRING);
			return result;
		}
	}

	private JSONObject updatePreferredTimes(String authToken, String customerID, String PreferredContactMethod,
			String PreferredContactTime, DataControllerRequest requestInstance) {
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.put("id", customerID);
		postParametersMap.put("PreferredContactMethod", PreferredContactMethod);
		postParametersMap.put("PreferredContactTime", PreferredContactTime);
		postParametersMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());

		String updateEndpointResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_UPDATE, postParametersMap, null,
				requestInstance);
		return CommonUtilities.getStringAsJSONObject(updateEndpointResponse);
	}

}
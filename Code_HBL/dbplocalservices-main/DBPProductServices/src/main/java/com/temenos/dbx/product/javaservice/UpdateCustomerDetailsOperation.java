package com.temenos.dbx.product.javaservice;

import java.util.ArrayList;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;

public class UpdateCustomerDetailsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
		ArrayList<Object> updatedInputArrDcReq = new ArrayList<Object>();

		Result result = new Result();
		String phoneNumber = null;
		try {
			ProfileManagementResource profileManagementResource = DBPAPIAbstractFactoryImpl
					.getResource(ProfileManagementResource.class);

			if (dcRequest.containsKeyInRequest("source")) {
				if (dcRequest.getParameter("source").toString().contentEquals("UserManagement")) {
				    phoneNumber = dcRequest.getParameter("phoneNumber");
				    if(StringUtils.isBlank(phoneNumber)) {
				        phoneNumber =HelperMethods.getInputParamMap(inputArray).get("phoneNumber");
				    }
					inputArray = constructInputArray(inputArray, dcRequest);
				}
			}

			result = profileManagementResource.updateProfile(methodID, inputArray, dcRequest, dcResponse);

		} catch (Exception e) {
			alert.prepareError("Caught exception while creating Customer: " + e).log();
		}
		if (dcRequest.containsKeyInRequest("source")) {
            if (dcRequest.getParameter("source").toString().contentEquals("UserManagement")) {
                dcRequest.addRequestParam_("phoneNumber", phoneNumber);
                HelperMethods.getInputParamMap(inputArray).put("phoneNumber", phoneNumber);
            }
        }
		return result;
	}

	public static Object[] constructInputArray(Object[] inputArray, DataControllerRequest dcRequest) {

		String operation = dcRequest.getParameter("operation") != null ? dcRequest.getParameter("operation") : "";
		String detailToBeUpdated = dcRequest.getParameter("detailToBeUpdated") != null
				? dcRequest.getParameter("detailToBeUpdated")
				: "";

		JSONArray customerDetailsArr = new JSONArray();
		JSONObject obj = new JSONObject();
		ArrayList<Object> updatedInputArrDcReq = new ArrayList<Object>();

		if (operation.contentEquals("Create")) {

			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("isAlertsRequired", dcRequest.getParameter("isAlertsRequired"));
			obj.put("Extension", dcRequest.getParameter("Extension"));

			if (detailToBeUpdated.contentEquals("phoneNumbers")) {
				obj.put("phoneNumber", dcRequest.getParameter("phoneNumber"));
				obj.put("phoneCountryCode", dcRequest.getParameter("phoneCountryCode"));

			} else if (detailToBeUpdated.contentEquals("EmailIds")) {
				obj.put("value", dcRequest.getParameter("value"));
			}
		}

		
		else if (operation.contentEquals("Update")) {
			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("isAlertsRequired", dcRequest.getParameter("isAlertsRequired"));
			obj.put("id", dcRequest.getParameter("communication_ID"));
			obj.put("isTypeBusiness", dcRequest.getParameter("isTypeBusiness"));

			if (detailToBeUpdated.contentEquals("phoneNumbers")) {
				obj.put("phoneNumber", dcRequest.getParameter("phoneNumber"));
				obj.put("phoneCountryCode", dcRequest.getParameter("phoneCountryCode"));
				obj.put("Extension", dcRequest.getParameter("Extension"));
			} else if (detailToBeUpdated.contentEquals("EmailIds")) {
				obj.put("value", dcRequest.getParameter("value"));
				if (dcRequest.getParameter("Extension") != null) {
					obj.put("Extension", dcRequest.getParameter("Extension"));
				}
			}

		}

		else if (detailToBeUpdated.contentEquals("Addresses")) {
			obj.put("Addr_type", dcRequest.getParameter("Addr_type"));
			obj.put("addrLine1", dcRequest.getParameter("addrLine1"));
			obj.put("addrLine2", dcRequest.getParameter("addrLine2"));
			obj.put("City_id", dcRequest.getParameter("City_id"));
			obj.put("ZipCode", dcRequest.getParameter("ZipCode"));
			obj.put("Region_id", dcRequest.getParameter("Region_id"));
			obj.put("isPrimary", dcRequest.getParameter("isPrimary"));
			obj.put("countryCode", dcRequest.getParameter("countryCode"));

			if (operation.contentEquals("UpdateAddress")) {
				obj.put("Addr_id", dcRequest.getParameter("Addr_id"));
			}

		}

		customerDetailsArr.put(obj);

		alert.prepareError("request from SRMS : "+obj).log();
		
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (detailToBeUpdated.contentEquals("phoneNumbers")) {

			if (inputParams.containsKey("phoneNumbers")) {
				inputParams.put("PhoneNumbers", customerDetailsArr.toString());
				inputParams.put("phoneNumber", null);
			}

			inputArray[1] = inputParams;

		} else if (detailToBeUpdated.contentEquals("EmailIds")) {
			if (inputParams.containsKey("emailIds")) {
				inputParams.put("emailIds", customerDetailsArr.toString());
			}

			inputArray[1] = inputParams;

		} else if (detailToBeUpdated.contentEquals("Addresses")) {
			if (inputParams.containsKey("addresses")) {
				inputParams.put("addresses", customerDetailsArr.toString());
			}

			inputArray[1] = inputParams;
		}

		dcRequest.addRequestParam_("phoneNumber", null);
        HelperMethods.getInputParamMap(inputArray).remove("phoneNumber");
        
		return inputArray;
	}
}

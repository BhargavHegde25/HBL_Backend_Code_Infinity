package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class UpdateUserDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			updateAddress(inputParams, dcRequest);
			updateCustomerCommication(inputParams, dcRequest);
			updatePreferences(inputParams, dcRequest);
			result = updateCustomer(inputParams, dcRequest);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess();
		}

		return result;
	}

	private void updatePreferences(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		String id = getPreferenceId(dcRequest, inputParams.get("id"));
		Map<String, String> input = getPreferencesMap(inputParams);
		HelperMethods.removeNullValues(input);
		if (input.isEmpty()) {
			return;
		}
		if (StringUtils.isNotBlank(id)) {
			input.put("id", id);
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCE_UPDATE);
		} else {
			input.put("id", "CPID" + HelperMethods.getNewId());
			input.put("Customer_id", HelperMethods.getCustomerIdFromSession(dcRequest));
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCE_CREATE);
		}
	}

	private Map<String, String> getPreferencesMap(Map<String, String> inputParams) {
		Map<String, String> input = new HashMap<>();
		addField(input, inputParams, "default_account_deposit", "DefaultAccountDeposit");
		addField(input, inputParams, "default_account_transfers", "DefaultAccountTransfers");
		addField(input, inputParams, "default_account_payments", "DefaultAccountPayments");
		addField(input, inputParams, "default_account_cardless", "DefaultAccountCardless");
		addField(input, inputParams, "default_account_billPay", "DefaultAccountBillPay");
		addField(input, inputParams, "default_to_account_p2p", "DefaultToAccountP2P");
		addField(input, inputParams, "default_from_account_p2p", "DefaultFromAccountP2P");
		addField(input, inputParams, "default_account_wire", "DefaultAccountWire");
		addField(input, inputParams, "showBillPayFromAccPopup", "ShowBillPayFromAccPopup");
		addField(input, inputParams, "default_from_account_qr", "DefaultFromAccountQR");
		
		addField(input, inputParams, "default_account_loanpayment", "DefaultAccountLoanPayment");
		addField(input, inputParams, "default_account_cardpayment", "DefaultAccountCardPayment");
		addField(input, inputParams, "default_account_checkmanagement", "DefaultAccountCheckManagement");
		
		addField(input, inputParams, "default_account_dashboard", "DefaultAccountDashboard");
		addField(input, inputParams, "default_account_qrpayment", "DefaultAccountQRPayment");
		addField(input, inputParams, "default_account_checkdeposit", "DefaultAccountCheckDeposit");
		addField(input, inputParams, "default_account_cashwithdrawl", "DefaultAccountCashWithdrawl");
		addField(input, inputParams, "default_account_esewa", "DefaultAccounteSewa");
		return input;
	}

	private String getPreferenceId(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		String select = "id";
		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String legalEntityId = (String) customer.get("legalEntityId");
		String filter = StringUtils.isNotBlank(legalEntityId) ? "Customer_id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "companyLegalUnit" +  DBPUtilitiesConstants.EQUAL + legalEntityId : "Customer_id" + DBPUtilitiesConstants.EQUAL + userId ;
		input.put(DBPUtilitiesConstants.SELECT, select);
		input.put(DBPUtilitiesConstants.FILTER, filter);
		Result res = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERPREFERENCE_GET);
		if (HelperMethods.hasRecords(res)) {
			return HelperMethods.getFieldValue(res, "id");
		}
		return null;
	}

	private void updateAddress(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = getAddressMap(inputParams);
		HelperMethods.removeNullValues(input);
		String addressId = null;
		if (input.isEmpty()) {
			return;
		}
		addressId = getAddressId(dcRequest, inputParams.get("id"));
		if (StringUtils.isNotBlank(addressId)) {
			input.put("id", addressId);
			HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ADDRESS_UPDATE);
		} else {
			addressId = "ADD" + HelperMethods.getNewId();
			input.put("id", addressId);
			HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ADDRESS_CREATE);
			Map<String, String> custAddress = new HashMap<>();
			custAddress.put("Customer_id", inputParams.get("id"));
			custAddress.put("Address_id", addressId);
			custAddress.put("isPrimary", "1");
			custAddress.put("Type_id", "ADR_TYPE_HOME");
			HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_ADDRESS_CREATE);
		}
	}

	private Map<String, String> getAddressMap(Map<String, String> inputParams) {
		Map<String, String> input = new HashMap<>();
		addField(input, inputParams, "addressLine1", "addressLine1");
		addField(input, inputParams, "addressLine2", "addressLine2");
		addField(input, inputParams, "city", "cityName");
		addField(input, inputParams, "country", "country");
		addField(input, inputParams, "state", "state");
		addField(input, inputParams, "zipcode", "zipCode");
		return input;
	}

	private void addField(Map<String, String> to, Map<String, String> from, String fromf, String tof) {
		if (StringUtils.isNotBlank(from.get(fromf))) {
			to.put(tof, from.get(fromf));
		}
	}

	private String getAddressId(DataControllerRequest dcRequest, String userId) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		String select = "Address_id";
		String filter = "AddressType eq ADR_TYPE_HOME and isPrimary eq 1 and CustomerId eq " + userId;
		input.put(DBPUtilitiesConstants.SELECT, select);
		input.put(DBPUtilitiesConstants.FILTER, filter);
		Result res = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_ADDRESS_VIEW_GET);
		if (HelperMethods.hasRecords(res)) {
			return HelperMethods.getFieldValue(res, "Address_id");
		}
		return null;
	}

	private void updateCustomerCommication(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = getEmailMap(inputParams);
		HelperMethods.removeNullValues(input);
		if (input.isEmpty()) {
			return;
		}
		input.put("Customer_id", inputParams.get("id"));
		String primaryId = getPrimaryCommunicationId(inputParams.get("id"), dcRequest);
		if (StringUtils.isNotBlank(primaryId) && StringUtils.isNotBlank(inputParams.get("email"))) {
			input.put("id", primaryId);
			input.put("isPrimary", "1");
			input.put("Value", inputParams.get("email"));
			updateEmail(dcRequest, input);
		}
		input.put("id", null);
		if (StringUtils.isNotBlank(inputParams.get("secondaryemail"))) {
			input.put("isPrimary", "0");
			input.put("Value", inputParams.get("secondaryemail"));
			updateEmail(dcRequest, input);
		}
		if (StringUtils.isNotBlank(inputParams.get("secondaryemail2"))) {
			input.put("isPrimary", "0");
			input.put("Value", inputParams.get("secondaryemail2"));
			updateEmail(dcRequest, input);
		}
	}

	private void updateEmail(DataControllerRequest dcRequest, Map<String, String> input) throws HttpCallException {
		if (StringUtils.isNotBlank(input.get("id"))) {
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_COMMUNICATION_UPDATE);
		} else {
			input.put("id", "CID" + HelperMethods.getNewId());
			HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERCOMMUNICATION_CREATE);
		}
	}

	private String getPrimaryCommunicationId(String custId, DataControllerRequest dcRequest) throws HttpCallException {
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + custId + DBPUtilitiesConstants.AND
				+ "isPrimary eq 1";
		Result res = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_COMMUNICATION_GET);
		return HelperMethods.getFieldValue(res, "id");
	}

	private Map<String, String> getEmailMap(Map<String, String> inputParams) {
		Map<String, String> input = new HashMap<>();
		input.put("Type_id", "COMM_TYPE_EMAIL");
		input.put("Extension", "Personal");
		input.put("createdby", "self");
		return input;
	}

	private Result updateCustomer(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = getCustomerMap(inputParams);
		HelperMethods.removeNullValues(input);
		if (input.isEmpty()) {
			return new Result();
		}
		if (inputParams.containsKey("default_account_wire")) {
			input.remove("default_account_wire");
			input.put("isWireTransferActivated", "1");
		}
		input.put("id", inputParams.get("id"));
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.USER_UPDATE);
	}

	private Map<String, String> getCustomerMap(Map<String, String> inputParams) {
		Map<String, String> input = new HashMap<>();
		addField(input, inputParams, "areAccountStatementTermsAccepted", "areAccountStatementTermsAccepted");
		addField(input, inputParams, "areDepositTermsAccepted", "areDepositTermsAccepted");
		addField(input, inputParams, "pin", "Pin");
		addField(input, inputParams, "userImage", "UserImage");
		addField(input, inputParams, "areUserAlertsTurnedOn", "areUserAlertsTurnedOn");
		addField(input, inputParams, "isPinSet", "isPinSet");
		addField(input, inputParams, "Password", "Password");
		addField(input, inputParams, "default_account_wire", "default_account_wire");
		addField(input, inputParams, "IsPinSet", "IsPinSet");
		return input;
	}

	private Result postProcess() {
		Result retVal = new Result();
		Param p = new Param("success", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		retVal.addParam(p);
		return retVal;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		HelperMethods.removeOnlyNullValues(inputParams);
		String id = HelperMethods.getUserIdFromSession(dcRequest);
		inputParams.put("id", id);
		String value = null;
		value = inputParams.get(DBPUtilitiesConstants.PIN);
		if (StringUtils.isNotBlank(value)) {
			inputParams.put(DBPUtilitiesConstants.IS_PIN_SET, "true");
		}
		value = inputParams.get(DBPUtilitiesConstants.USR_ALERTS_ON);
		if (StringUtils.isNotBlank(value)) {
			inputParams.put(DBPUtilitiesConstants.ARE_USR_ALERTS_ON, value);
		}

		value = inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
		if (StringUtils.isNotBlank(value)) {
			String oldvalue = inputParams.get(DBPUtilitiesConstants.OLD_PWD_FIELD);
			if (StringUtils.isNotBlank(oldvalue) && !oldvalue.equals(value)) {
				status = false;
				HelperMethods.setValidationMsg("Incorrect old password", dcRequest, result);
			}
			String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
			String hashedPassword = BCrypt.hashpw(value, salt);
			inputParams.put("Password", hashedPassword);
		}
		/*
		 * if(StringUtils.isNotBlank(userName)){ String oldUsername =
		 * getOldUserName(dcRequest, id); if(!updateUserNameAtAdminConsole(oldUsername,
		 * userName,dcRequest)){ status = false;
		 * HelperMethods.setValidationMsg("failed to update username in admin console",
		 * dcRequest, result); } }
		 */
		return status;
	}

	/*
	 * private String getOldUserName(DataControllerRequest dcRequest, String id)
	 * throws HttpCallException { String filter = "Id" + DBPUtilitiesConstants.EQUAL
	 * + id; Result result = HelperMethods.callGetApi(dcRequest, filter,
	 * HelperMethods.getHeaders(dcRequest), URLConstants.USER_GET);
	 * if(HelperMethods.hasRecords(result)){ return
	 * HelperMethods.getFieldValue(result, "userName"); } return null; }
	 */

	public boolean updateUserNameAtAdminConsole(String username, String newUserName, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		input.put("userName", username);
		input.put("newUserName", newUserName);
		JsonObject response = AdminUtil.invokeAPIAndGetJson(input, URLConstants.ADMIN_CUSTOMER_EDIT_BASIC_INO,
				dcRequest);
		return "0".equals(response.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString());
	}

}
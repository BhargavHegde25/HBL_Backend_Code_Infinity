package com.temenos.infinity.api.cardless;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.CreateTransferHelper;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class CreateTransfer implements JavaService2 {
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
	public static LoggerUtil logger = new LoggerUtil(CreateTransfer.class);

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		logger.debug("Inside CreateTransfer:::");
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		logger.debug("Inside CreateTransfer inputParams:::" + inputParams);
		boolean status = true;
		status = preProcess(inputParams, dcRequest, result);
		logger.debug("Inside CreateTransfer status:::" + status);
		if (inputParams.containsKey(DBPUtilitiesConstants.FILTER)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.TRANSACTION_GET);
			postProcess(dcRequest, inputParams, result);
		}
		if (status) {
			logger.debug("Inside CreateTransfer status:::" + QueryFormer.getDBType(dcRequest));
			if ("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest))) {
				Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
				String jdbcUrl = QueryFormer.getDBType(dcRequest);
				String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
				inputParams.put("Customer_id", user.get("customer_id"));
				inputParams.put("legalEntityId", legalEntityId);
				result = TransOperations.createDataSetInsert(dcRequest, inputParams);
				logger.debug("Inside CreateTransfer MSSQL result:::" + ResultToJSON.convert(result));
			} else
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.ACCOUNT_TRANSACTION_CREATE);
			/*
			 * result = HelperMethods.callApi(dcRequest, inputParams,
			 * HelperMethods.getHeaders(dcRequest),TransactionUtils.
			 * getCreateTransactionDBService());
			 */
			status = !HelperMethods.hasError(result);
			logger.debug("Inside CreateTransfer status 76:::" + status);
			if (status) {
				result = postProcess(dcRequest, result);
				logger.debug("Inside CreateTransfer status result:::" + ResultToJSON.convert(result));
				// Trigger email to customer
				sendEmailToCustomer(dcRequest, inputParams, result);
			}
		}
		logger.debug("Inside CreateTransfer result:::" + ResultToJSON.convert(result));

		return result;
	}

	private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
			throws HttpCallException {
		if (HelperMethods.hasRecords(result)) {
			List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
			for (Record transaction : transactions) {
				updateFromAccountDetails(dcRequest, transaction);
				updateToAccountDetails(dcRequest, transaction);
				updatePayPersonDetails(dcRequest, transaction);
				updateBillDetails(dcRequest, transaction);
				updatePayeeDetails(dcRequest, transaction);
				updateDateFormat(transaction);
			}
		}
	}

	private Result postProcess(DataControllerRequest request, Result result) {
		Result retResult = new Result();
		updateValidDate(retResult, result);
		retResult.addParam(new Param("cashlessPersonName", HelperMethods.getFieldValue(result, "cashlessPersonName"),
				DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("cashlessPhone", HelperMethods.getFieldValue(result, "cashlessPhone"),
				DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("cashlessEmail", HelperMethods.getFieldValue(result, "cashlessEmail"),
				DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("cashlessMode", HelperMethods.getFieldValue(result, "cashlessMode"),
				DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("otp", HelperMethods.getFieldValue(result, "cashlessOTP"),
				DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(
				new Param("referenceId", HelperMethods.getFieldValue(result, "Id"), DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
		retResult.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));
		return retResult;
	}

	private void updateValidDate(Result retResult, Result ipResult) {
		String otpValidDate = HelperMethods.getFieldValue(ipResult, "cashlessOTPValidDate");
		if (StringUtils.isNotBlank(otpValidDate)) {
			long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
			timeDiff = timeDiff / 1000;
			long m = (timeDiff / 60) % 60;
			long h = (timeDiff / (60 * 60)) % 24;
			retResult.addParam(new Param("validDate", (String.valueOf(h) + "h:" + String.valueOf(m) + "m"),
					DBPUtilitiesConstants.STRING_TYPE));
		}
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException, ParseException {
		boolean status = false;
		boolean isSchedulingEngine = false;
		String transactionType = inputParams.get(DBPUtilitiesConstants.TRANSACTION_TYPE);
		logger.debug("Inside CreateTransfer transactionType:::" + transactionType);
		Map<String, String> map = HelperMethods.getCustomerFromIdentityService(dcRequest);
		logger.debug("Inside CreateTransfer map:::" + map);
		if (map.containsKey("isSchedulingEngine") && map.get("isSchedulingEngine") != null
				&& map.get("isSchedulingEngine").equals("true")) {
			isSchedulingEngine = true;
		}

		if (!isSchedulingEngine) {
			logger.debug("Inside CreateTransfer inside if :::" + isSchedulingEngine);
			status = validations(inputParams, dcRequest, result);

		}

		if (status || isSchedulingEngine) {
			CreateTransferHelper helper = new CreateTransferHelper();
			inputParams.put(DBPUtilitiesConstants.CREATED_DATE, application.getServerTimeStamp());

			if (DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT.equalsIgnoreCase(transactionType)) {
				status = helper.createNewDeposit(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
				status = helper.createNewInternalTransfer(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
				status = helper.createNewExternalTransfer(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_PAY_BILL.equalsIgnoreCase(transactionType)) {
				status = helper.payNewBill(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_P2P.equalsIgnoreCase(transactionType)) {
				status = helper.payPerson(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN.equalsIgnoreCase(transactionType)) {
				status = helper.payLoan(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_CARDLESS.equalsIgnoreCase(transactionType)) {
				status = helper.cardlessCash(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_REQUEST.equalsIgnoreCase(transactionType)) {
				status = helper.createP2PRequestMoney(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_WIRE.equalsIgnoreCase(transactionType)) {
				status = helper.wireTransfer(inputParams, dcRequest, result);
			} else if (DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST
					.equalsIgnoreCase(transactionType)) {
				status = helper.stopCheckPaymentRequest(inputParams, dcRequest, result);
			}
		}
		return status;
	}

	private boolean validations(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String transactionType = inputParams.get(DBPUtilitiesConstants.TRANSACTION_TYPE);

		if (!StringUtils.isNotBlank(transactionType)) {
			logger.debug("Inside CreateTransfer validations inside if:::");
			HelperMethods.setValidationMsg("Edit operation not permitted for this Transaction type", dcRequest, result);
			status = false;
		}
		return status;
	}

	private boolean accountValidation(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {

		Result userResult = new Result();
		Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String fromAccountNum = inputParams.get("fromAccountNumber");
		String userId = user.get("customer_id");
		String userType = user.get("customerType");
		if (StringUtils.isNotBlank(fromAccountNum) && StringUtils.isNotBlank(userId)) {
			if (HelperMethods.isBusinessUserType(userType)) {
				String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
						+ "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
				userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMERACCOUNTS_GET);
			} else {
				String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
						+ DBPUtilitiesConstants.USER_ID + DBPUtilitiesConstants.EQUAL + userId;
				userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
						URLConstants.ACCOUNTS_GET);
			}
			return HelperMethods.hasRecords(userResult);
		}
		return true;
	}

	private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
			throws HttpCallException {
		String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
		if (StringUtils.isNotBlank(frmAccountNum)) {
			String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
			Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNTS_GET);
			String type = HelperMethods.getFieldValue(frmAccount, "typeDescription");
			transaction.addParam(new Param("fromAccountType", type, MWConstants.STRING));
			String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
			transaction.addParam(new Param("fromAccountName", accountName, MWConstants.STRING));
			String nickName = HelperMethods.getFieldValue(frmAccount, "nickName");
			if (StringUtils.isBlank(nickName)) {
				nickName = accountName;
			}
			transaction.addParam(new Param("fromAccountNickName", nickName, MWConstants.STRING));
		}
	}

	private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
		String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
		if (StringUtils.isNotBlank(toAccountNum)) {
			String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
			Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNTS_GET);
			String type = HelperMethods.getFieldValue(toAccount, "typeDescription");
			transaction.addParam(new Param("toAccountType", type, MWConstants.STRING));
			String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
			transaction.addParam(new Param("toAccountName", accountName, MWConstants.STRING));
		}
	}

	private void updatePayPersonDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
		String payPersonId = HelperMethods.getFieldValue(transaction, "Person_Id");
		if (StringUtils.isNotBlank(payPersonId)) {
			String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
			Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.PAYPERSON_GET);
			if (HelperMethods.hasRecords(payPerson)) {
				Record person = payPerson.getAllDatasets().get(0).getRecord(0);
				transaction
						.addParam(new Param("phone", HelperMethods.getFieldValue(person, "phone"), MWConstants.STRING));
				transaction
						.addParam(new Param("email", HelperMethods.getFieldValue(person, "email"), MWConstants.STRING));
				transaction
						.addParam(new Param("name", HelperMethods.getFieldValue(person, "name"), MWConstants.STRING));
			}
		}
	}

	private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
		String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
		if (StringUtils.isNotBlank(billId)) {
			String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
			Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.BILL_GET);
			if (HelperMethods.hasRecords(biller)) {
				Record bill = biller.getAllDatasets().get(0).getRecord(0);
				String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
				fetchAndUpdatePayee(dcRequest, payeeId, transaction);
			}
		}
	}

	private void fetchAndUpdatePayee(DataControllerRequest dcRequest, String payeeId, Record transaction)
			throws HttpCallException {
		if (StringUtils.isNotBlank(payeeId)) {
			String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
			Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.PAYEE_GET);
			if (HelperMethods.hasRecords(payees)) {
				Record payee = payees.getAllDatasets().get(0).getRecord(0);
				transaction.addParam(
						new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"), MWConstants.STRING));
			}
		}
	}

	private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
		String payeeId = HelperMethods.getFieldValue(transaction, "Payee_Id");
		if (StringUtils.isNotBlank(payeeId)) {
			String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
			Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.PAYEE_GET);
			if (HelperMethods.hasRecords(payees)) {
				Record payee = payees.getAllDatasets().get(0).getRecord(0);
				transaction.addParam(
						new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"), MWConstants.STRING));
			}
		}
	}

	private void updateDateFormat(Record transaction) {
		String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
		String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
		try {
			if (StringUtils.isNotBlank(scheduledDate)) {
				transaction.addParam(new Param("scheduledDate",
						HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
			if (StringUtils.isNotBlank(transactionDate)) {
				transaction.addParam(new Param("transactionDate",
						HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
			String frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
			if (StringUtils.isNotBlank(frequencyDate)) {
				transaction.addParam(new Param("frequencyEndDate",
						HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
			frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
			if (StringUtils.isNotBlank(frequencyDate)) {
				transaction.addParam(new Param("frequencyStartDate",
						HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
			}
		} catch (Exception e) {
		}
	}

	private static void sendEmailToCustomer(DataControllerRequest request, Map<String, String> inputParams,
			Result result) throws HttpCallException {
		logger.debug("Inside sendEmailToCustomer:::result:::" + ResultToJSON.convert(result));
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		String customerResultfromSession = (String) temenosUtils.retreiveFromSession("customer", request);
		Result enrollResult = ConvertJsonToResult.convert(customerResultfromSession);
		logger.debug("user session details ### :" + ResultToJSON.convert(enrollResult));
		String email = enrollResult.getParamValueByName("email");
		logger.debug("Inside sendEmailToCustomer email from session:::" + email);
		String firstName = enrollResult.getParamValueByName("firstName");
		String lastName = enrollResult.getParamValueByName("lastName");
		logger.debug("Inside sendEmailToCustomer email from FirstName:::" + firstName);
		logger.debug("Inside sendEmailToCustomer email from LastName:::" + lastName);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String transactionDate = sdf.format(new Date());
		logger.debug("transactionDate:" + transactionDate);
		String transactionId = result.getParamValueByName("referenceId");
		String withdrawalCode = result.getParamValueByName("otp");
		logger.debug("Inside sendEmailToCustomer transactionId:::" + transactionId);
		logger.debug("Inside sendEmailToCustomer email from withdrawalCode:::" + withdrawalCode);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", "Cardless_Cash_Transaction");

		JSONObject addContext = new JSONObject();
		addContext.put("firstName", firstName);
		addContext.put("lastName", lastName);
		addContext.put("transactionId", transactionId);
		addContext.put("transactionDate", transactionDate);
		addContext.put("withdrawalCode", withdrawalCode);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		String customerid = enrollResult.getParamValueByName("userId");
		logger.debug("customerid value:" + customerid.toString());
		JSONObject customerEmail = getContactDetails(customerid, request);
		logger.debug("getContactDetails value:" + customerEmail.toString());
		// String email1 = customerEmail.optString("email");
		String phone = customerEmail.optString("phone");
		String phone1 = phone.split("-")[1];
		logger.debug("Customer phone from Transact ### :" + phone1);
		String cashlessMode = (String) inputParams.get("cashlessMode");
		logger.debug("cashlessMode ### :" + cashlessMode);
		if ("others".equalsIgnoreCase(cashlessMode)) {
			String amount = (String) inputParams.get("amount");
			String receiverName = (String) inputParams.get("cashlessPersonName");
			String receiverPhoneNum = (String) inputParams.get("cashlessPhone");
			String receiverEmail = (String) inputParams.get("cashlessEmail");
			logger.debug("amount ### :" + amount);
			if (StringUtils.isNotBlank(receiverPhoneNum)) {
				String smsBody = "Dear " + receiverName + ", You have received NPR " + amount + " from " + firstName
						+ " " + lastName + ". To withdraw cash from ATM, please enter the Withdrawal Code – "
						+ withdrawalCode + " and 4-digit Secure Code shared by sender.";
				sendSMS(smsBody, receiverPhoneNum);
			} else {
				// trigger mail to receiver
				Map<String, String> inputMap = new HashMap<>();
				inputMap.put("Subscribe", "true");
				inputMap.put("EmailType", "Cardless_Cash_Transaction_Others");

				JSONObject addContext1 = new JSONObject();
				addContext1.put("receiverName", receiverName);
				addContext1.put("senderFirstName", firstName);
				addContext1.put("senderLastName", lastName);
				addContext1.put("amount", amount);
				addContext1.put("withdrawalCode", withdrawalCode);

				inputMap.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext1));
				inputMap.put("Email", receiverEmail);
				logger.debug("inputMap ### :" + inputMap);
				Map<String, String> headers1 = HelperMethods.getHeaders(request);
				headers1.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
				HelperMethods.callApi(request, inputMap, headers1, URLConstants.DBX_SEND_EMAIL_ORCH);
			}

		} else {
			String smsBody = "Dear " + firstName + " " + lastName + ", Your Cardless Cash Transaction No is "
					+ transactionId + ", dt " + transactionDate
					+ ". To withdraw cash from ATM, please enter the Withdrawal Code – " + withdrawalCode
					+ " and self-generated 4-digit Secure Code. Please don’t share the code with anyone.";
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
			sendSMS(smsBody, phone1);
		}
	}

	private static void sendSMS(String body, String phonenumber) {
		try {
			logger.debug("sendSms.phonenumber:" + phonenumber);
			logger.debug("sendSms.body:" + body);
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("contact", phonenumber);
			inputParams.put("body", body);
			String smsResponse = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams).withRequestHeaders(headerParams)
					.withDataControllerRequest(null).build().getResponse();
			logger.debug("sendSms.response:" + smsResponse);
		} catch (Exception e) {
			logger.debug("Error occured while sending SMS." + e);
		}
	}

	private static JSONObject getContactDetails(String customerId, DataControllerRequest request) {
		CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CommunicationBackendDelegate.class);
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(customerId);
		DBXResult communicationResponse = communicationBackendDelegate
				.getPrimaryMFACommunicationDetails(customerCommunicationDTO, request.getHeaderMap());
		JsonObject customerCommunication = ((JsonObject) communicationResponse.getResponse());
		JSONObject communicationObj = new JSONObject();
		if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
			JsonArray communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					.getAsJsonArray();
			for (JsonElement jsonelement : communicationArray) {
				JsonObject object = jsonelement.getAsJsonObject();
				if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("email", JSONUtil.getString(object, "Value"));
				if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("phone", JSONUtil.getString(object, "Value"));
			}
		}
		logger.debug("getContactDetails:" + communicationObj);
		return communicationObj;
	}
}
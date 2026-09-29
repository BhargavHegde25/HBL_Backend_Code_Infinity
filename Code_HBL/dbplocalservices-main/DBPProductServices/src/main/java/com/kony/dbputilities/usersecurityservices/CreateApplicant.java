package com.kony.dbputilities.usersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateApplicant implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if(dcRequest.getSource() != null) {
			for (Entry<String, String[]> entry : dcRequest.getSource().entrySet()) {
				String[] value = entry.getValue();
				inputParams.put(entry.getKey(), value[0]);
			}
		}

		if (preProcess(inputParams, dcRequest, result)) {
			result = (Result) new com.kony.dbputilities.customersecurityservices.CreateApplicant().invoke(methodID,
					inputArray, dcRequest, dcResponse);
			if (result.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null && result
					.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().equals(ErrorCodes.RECORD_CREATED)) {
				HashMap<String, String> input = new HashMap<>();
				input.put("bundle_name", "NUO");
				Result fmResult = AdminUtil.invokeAPI(input, URLConstants.FUNDING_AMOUNT, dcRequest);
				Dataset records = HelperMethods.getDataSet(fmResult);
				for (Record record : records.getAllRecords()) {
					if (StringUtils.isNotBlank(HelperMethods.getFieldValue(record, "key"))) {
						if (HelperMethods.getFieldValue(record, "key").equals("AUTO_FUNDING_AMOUNT")) {
							inputParams.put("AvailableBalance", HelperMethods.getParamValue(record.getParam("value")));
						} else if (HelperMethods.getFieldValue(record, "key").equals("DEFAULT_ACC_TYPE")) {
							inputParams.put("Type_id", HelperMethods.getParamValue(record.getParam("value")));
						}
					}
				}

				Result productDetails = getProductDetails(dcRequest, inputParams.get("Type_id"));
				if (HelperMethods.hasRecords(productDetails)) {
					String productType = HelperMethods.getFieldValue(productDetails, "productType");
					String productName = HelperMethods.getFieldValue(productDetails, "productName");
					inputParams.put("Type_id", HelperMethods.getAccountsTypes().get(productType));
					inputParams.put("AccountName", productName);
				}
				inputParams.put("Customer_id", HelperMethods.getParamValue(result.getParamByName("applicantID")));
			}
		}

		result = postProcess(inputParams, dcRequest);

		if ((!result.getNameOfAllParams().contains("applicantID")|| StringUtils.isBlank(result.getParamByName("applicantID").getValue()))
				&& (result.getNameOfAllParams().contains("Customer_id") || StringUtils.isBlank(result.getParamByName("Customer_id").getValue()))) {
			result = AdminUtil.invokeAPI(inputParams, URLConstants.ADMIN_CREATE_APPLICANT, dcRequest);
			ErrorCodeEnum.ERR_11019.setErrorCode(result);
		}

		return result;
	}

	private Result getProductDetails(DataControllerRequest dcRequest, String typeId) throws HttpCallException {
		if (StringUtils.isBlank(typeId)) {
			typeId = "2";
		}
		StringBuilder filter = new StringBuilder();
		filter.append("productTypeId").append(DBPUtilitiesConstants.EQUAL).append(typeId);
		filter.append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
		.append("SID_PRO_ACTIVE");
		filter.append(DBPUtilitiesConstants.AND).append("softdeleteflag").append(DBPUtilitiesConstants.EQUAL)
		.append("0");
		String select = "productName, productType";
		Map<String, String> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.SELECT, select);
		input.put(DBPUtilitiesConstants.FILTER, filter.toString());
		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.PRODUCT_VIEW_GET);

	}

	private Result postProcess(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Result retResult = new Result();
		Result result = new Result();
		if(inputParams.get("coreMembershipId") != null) {
			inputParams.put("id", inputParams.get("coreMembershipId"));
		}
		else {
			inputParams.put("id", HelperMethods.getNumericId() + "");
		}
		result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.MEMBERSHIP_CREATE);

		if (HelperMethods.hasRecords(result)) {

			inputParams.put("Membership_id", HelperMethods.getFieldValue(result, "id"));

			createMembershipAccount(inputParams, dcRequest);
			createMembershipOwner(inputParams, dcRequest);

			retResult.addParam(new Param("applicantID", inputParams.get("Membership_id"), "String"));
			retResult.addParam(new Param("Customer_id", inputParams.get("Customer_id"), "String"));
			retResult.addParam(new Param("AccountType", inputParams.get("AccountName"), "String"));
			retResult.addParam(new Param("FundingAmount", inputParams.get("AvailableBalance"), "String"));
		} else if (!HelperMethods.hasRecords(result) && HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_11017.setErrorCode(result);
			return result;
		} else {
			ErrorCodeEnum.ERR_11018.setErrorCode(retResult);
		}

		return retResult;
	}

	private void createMembershipOwner(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Map<String, String> input = new HashMap<>();
		SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		input.put("id", idformatter.format(new Date()));
		input.put("Customer_id", inputParams.get("Customer_id"));
		input.put("Membership_id", inputParams.get("Membership_id"));
		input.put("Membership_Type", dcRequest.getParameter("criteriaID"));
		HashMap<String, String> hashMap = HelperMethods.getRecordMap(dcRequest.getParameter("identityInformation"));
		input.put("IDType_id", hashMap.get("idType"));
		input.put("IDValue", hashMap.get("idValue"));
		HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.MEMBERSHIPOWNER_CREATE);
	}

	private void createMembershipAccount(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Result result = createCustomerAccount(inputParams, dcRequest);
		if (HelperMethods.hasRecords(result)) {
			Map<String, String> input = new HashMap<>();
			SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
			input.put("id", idformatter.format(new Date()));
			input.put("Membership_id", inputParams.get("Membership_id"));
			input.put("Account_id", HelperMethods.getFieldValue(result, "Account_id"));
			input.put("accountName", inputParams.get("AccountName"));
			input.put("IsOrganizationAccount", "false");
			HelperMethods.callApiAsync(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.MEMBERSHIP_ACCOUNTS_CREATE);
		}

	}

	private Result createCustomerAccount(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {

		Result result = createAccount(inputParams, dcRequest);
		if (HelperMethods.hasRecords(result)) {
			Map<String, String> input = new HashMap<>();
			input.put("id", HelperMethods.getNumericId() + "");
			input.put("Customer_id", inputParams.get("Customer_id"));
			input.put("Account_id", HelperMethods.getFieldValue(result, "Account_id"));
			input.put("IsOrganizationAccount", "true");
			input.put("IsOrgAccountUnLinked", "true");
			return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERACCOUNTS_CREATE);
		}

		return new Result();
	}

	private Result createAccount(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();

		input.put(DBPUtilitiesConstants.PI_USR_ID, inputParams.get("Customer_id"));
		input.put("Type_id", inputParams.get("Type_id"));
		input.put("Account_id", HelperMethods.getNumericId() + "");
		input.put("AccountName", inputParams.get("AccountName"));
		input.put("Name", inputParams.get("AccountName"));
		input.put("AvailableBalance", inputParams.get("AvailableBalance"));
		input.put("IsOrganizationAccount", "false");
		input.put("Bank_id", "1");
		input.put("StatusDesc", "active");
		input.put("SwiftCode", "123456789");
		input.put("AccountPreference", "1");
		input.put("IsPFM", "1");
		input.put("PrincipalValue", "0");
		input.put("AvailableCredit", "0");
		input.put("CurrentBalance", "0");
		input.put("MinimumDue", "0");
		input.put("SupportCardlessCash", "1");
		input.put("SupportDeposit", "1");
		input.put("SupportBillPay", "1");
		input.put("SupportTransferFrom", "1");
		input.put("SupportTransferTo", "1");
		input.put("OutstandingBalance", "0");
		input.put("ClosingDate", HelperMethods.getCurrentDate());
		input.put("DueDate", HelperMethods.getCurrentDate());
		input.put("MaturityDate", HelperMethods.getCurrentDate());
		input.put("PaymentTerm", "");
		input.put("AccountHolder", "{\"fullname\":\"" + inputParams.get("FirstName") + " " + inputParams.get("LastName")
		+ "\",\"username\":\"" + inputParams.get("UserName") + "\"}");
		input.put("InterestRate", "0");
		input.put("OpeningDate", HelperMethods.getCurrentDate());
		input.put("ShowTransactions", "1");
		input.put("TransactionLimit", "0");
		input.put("TransferLimit", "0");
		input.put("FavouriteStatus", "0");
		input.put("MaturityOption", "0");
		input.put("RoutingNumber", "0");
		input.put("JointHolders", "[{\"fullname\":\"" + inputParams.get("FirstName") + " " + inputParams.get("LastName")
		+ "\",\"username\":\"" + inputParams.get("UserName") + "\"}]");
		input.put("DividendRate", "0");
		input.put("DividendYTD", "0");
		input.put("LastDividendPaidAmount", "0");
		input.put("LastDividendPaidDate", HelperMethods.getCurrentDate());
		input.put("PreviousYearDividend", "0");
		input.put("BondInterest", "0");
		input.put("BondInterestLastYear", "0");
		input.put("TotalCreditMonths", "0");
		input.put("TotalDebitsMonth", "0");
		input.put("CurrentAmountDue", "0");
		input.put("PaymentDue", "0");
		input.put("LastPaymentAmount", "0");
		input.put("LateFeesDue", "0");
		input.put("CreditLimit", "0");
		input.put("InterestPaidYTD", "0");
		input.put("InterestPaidPreviousYTD", "0");
		input.put("UnpaidInterest", "0");
		input.put("RegularPaymentAmount", "0");
		input.put("DividendPaidYTD", "0");
		input.put("DividendLastPaidAmount", "0");
		input.put("DividendLastPaidDate", HelperMethods.getCurrentDate());
		input.put("PreviousYearsDividends", "0");
		input.put("PendingDeposit", "0");
		input.put("PendingWithdrawal", "0");
		input.put("InterestEarned", "0");
		input.put("maturityAmount", "0");
		input.put("principalBalance", "0");
		input.put("OriginalAmount", "0");
		input.put("payoffAmount", "0");
		input.put("BsbNum", "000000000");
		input.put("PayOffCharge", "0");
		input.put("InterestPaidLastYear", "0");
		input.put("EStatementmentEnable", "0");

		return HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.ACCOUNTS_CREATE);
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = true;
		getParamMap(inputParams, dcRequest);
		if (StringUtils.isBlank(inputParams.get("id"))) {
			inputParams.put("id", getID());
			inputParams.put("Status_id", "SID_APP_PENDING");
		}

		if (Calendar.getInstance().get(Calendar.MINUTE) % 59 == 0) {
			status = false;
			inputParams.put("criteriaID", dcRequest.getParameter("criteriaID"));
			inputParams.put("reason", "seconds is not dvivisible by 5");
			inputParams.put("status", "SID_APP_FAILED");
		}

		return status;
	}

	private void getParamMap(Map<String, String> inputParams, DataControllerRequest dcRequest) {

		String key;
		Iterator<String> iterator = dcRequest.getParameterNames();
		while (iterator.hasNext()) {
			key = iterator.next();
			inputParams.put(key, dcRequest.getParameter(key));
		}
	}

	private static String getID() {
		SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
		return idformatter.format(new Date());
	}

}
package com.infinity.dbx.temenos.accounts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.transactions.GetAccountTransactionByType;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.transactions.TransactionUtils;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;

public class GetAccountDetails implements JavaService2, AccountsConstants, TemenosConstants {
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			TransactionConstants.TRANSACTIONS_DATE_BACKEND_FORMAT);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
		HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
		String accountId = CommonUtils.getParamValue(params, ACCOUNTID);
		TemenosUtils temenosUtils = TemenosUtils.getInstance();

		HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
		Account account = accounts != null ? accounts.get(accountId) : null;

		request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.POST_LOGIN_FLOW);
		
		String userId = CommonUtils.getBackendIdFromCache(request, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID, "1");
		/*if("".equalsIgnoreCase(userId)) {
			userId = (String) temenosUtils.retreiveFromSession(MEMBERSHIPID, request);
		}*/
		params.put(USER_ID, userId);
		String authToken = TokenUtils.getT24AuthToken(request);
		if (/*StringUtils.isBlank(authToken) ||*/ account == null) {
			return null;
		}

		Result result = new Result();

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		headerParams.put(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		inputParams.put(ACCOUNTID, accountId);

		switch (account.getAccountType()) {
		case Constants.ACCOUNT_TYPE_CHECKING:
			result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_ACCOUNT_DETAILS, false);
			break;
		case Constants.ACCOUNT_TYPE_SAVINGS:
			result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_ACCOUNT_DETAILS, false);
			break;
		case Constants.ACCOUNT_TYPE_LOAN:
			try {
				TransactDate transactDate = TransactionUtils.getTransactDate(request);
				Calendar calender = Calendar.getInstance();
				calender.setTime(transactDate.getNextWorkingDate());
				Date getNextWorkingDate = calender.getTime();
				String nexDate = DATE_FORMATTER.format(getNextWorkingDate);
				inputParams.put(TemenosConstants.FROM_DATE, nexDate);
			} catch (Exception e) {
				inputParams.put(TemenosConstants.FROM_DATE,"");
				alert.prepareError("Error while fetching Transact Date").log();
			}
			result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_LOAN_DETAILS, false);
			//Mapping "interestPaidYTD" to "dividentPaidYtd"
			result.addParam(new Param(INTEREST_PAID_YTD, result.getParamValueByName(INTEREST_PAID), Constants.PARAM_DATATYPE_STRING));
			break;
		case Constants.ACCOUNT_TYPE_DEPOSIT:
			result = CommonUtils.callIntegrationService(request, inputParams, headerParams,
					TemenosConstants.SERVICE_T24IS_ACCOUNTS, TemenosConstants.OP_DEPOSIT_DETAILS, false);
			//Mapping "dividendLastPaidDate" to "LastPaymentDate"
			result.addParam(new Param(DIVIDEND_LAST_PAID_DATE, result.getParamValueByName(LAST_PAYMENT_DATE), Constants.PARAM_DATATYPE_STRING));
			//Setting dividendRate to InterestRate
			String interestRate = result.getParamValueByName(DIVIDEND_RATE);
			if(interestRate!=null) {
				interestRate = interestRate.replace("%","");
			}
			result.addParam(new Param(INTEREST_RATE, interestRate, Constants.PARAM_DATATYPE_STRING));
			break;
		}
		result.addParam(new Param(DB_ACCOUNTID,account.getAccountId(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(ARRANGEMENT_ID, account.getArrangementId(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param("product", account.getProductId(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(ACCOUNTID, account.getAccountId(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(COMPANY_ID,ServerConfigurations.BRANCH_ID_REFERENCE.getValueIfExists(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(Constants.PARAM_TYPE_DESCRIPTION, account.getAccountType(), Constants.PARAM_DATATYPE_STRING));
		// result.addParam(new Param(Constants.PARAM_ACCOUNT_HOLDER,
		// account.getAccountHolder(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PRINCIPAL_BALANCE, account.getPrincipalBalance(), Constants.PARAM_DATATYPE_STRING));
		//result.addParam(new Param(AVAILABLE_BALANCE, account.getAvailableBalance(), Constants.PARAM_DATATYPE_STRING));
		if(!(account.getAccountType().equalsIgnoreCase(Constants.ACCOUNT_TYPE_CHECKING)||account.getAccountType().equalsIgnoreCase(Constants.ACCOUNT_TYPE_SAVINGS))) {
			result.addParam(new Param(AVAILABLE_BALANCE, account.getAvailableBalance(), Constants.PARAM_DATATYPE_STRING));
		}
		result.addParam(new Param(CURRENT_BALANCE, account.getCurrentBalance(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(OPENING_DATE, account.getOpeningDate(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(
				new Param(Constants.PARAM_ACCOUNT_NAME, account.getAccountName(), Constants.PARAM_DATATYPE_STRING));
		
			if(StringUtils.isNotBlank(result.getParamValueByName(CURR_AMOUNT_DUE)))
		{
			result.addParam(new Param(PAYMENT_DUE, result.getParamValueByName(CURR_AMOUNT_DUE), Constants.PARAM_DATATYPE_STRING));
		}
		else
		{
			result.addParam(new Param(PAYMENT_DUE, account.getPaymentDue(), Constants.PARAM_DATATYPE_STRING));
		}
		
		if(StringUtils.isNotBlank(result.getParamValueByName(ORIGINAL_AMOUNT)))
		{
		result.addParam(new Param(ORIGINAL_AMOUNT, result.getParamValueByName(ORIGINAL_AMOUNT), Constants.PARAM_DATATYPE_STRING));
		}
		else
		{
		result.addParam(new Param(ORIGINAL_AMOUNT, account.getOriginalAmount(), Constants.PARAM_DATATYPE_STRING));
		}
		if(StringUtils.isNotBlank(result.getParamValueByName(START_DATE)))
		{
		result.addParam(new Param(OPENING_DATE, result.getParamValueByName(START_DATE), Constants.PARAM_DATATYPE_STRING));
		}
		else
		{
		result.addParam(new Param(OPENING_DATE, account.getOpeningDate(), Constants.PARAM_DATATYPE_STRING));		
		}
//		result.addParam(new Param(CURR_AMOUNT_DUE, account.getCurrentAmountDue(), Constants.PARAM_DATATYPE_STRING));
		//result.addParam(new Param(ORIGINAL_AMOUNT, account.getOriginalAmount(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(NICKNAME, account.getNickName(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(DISPLAY_NAME, account.getNickName(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(
				new Param(OUTSTANDING_BALANCE, account.getOutstandingBalance(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PRINCIPAL_VALUE, account.getPrincipalValue(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PAYOFF_AMOUNT, account.getPayoffAmount(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(CURRENCY_CODE, account.getCurrencyCode(), Constants.PARAM_DATATYPE_STRING));
		
		//getting from dbxdb
		result.addParam(new Param(DB_MEMBERSHIP_ID, account.getMembership_id(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(DB_MEMBERSHIP_NAME, account.getMembershipName(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(IS_BUSINESS_ACCOUNT, account.getIsBusinessAccount(), Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PARAM_SUPPORT_BILLPAY, YES, Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PARAM_SUPPORT_CHECKS, YES, Constants.PARAM_DATATYPE_STRING));	
		result.addParam(new Param(PARAM_DEPOSIT_DESTINATION_ACCOUNT, YES, Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PARAM_TRANSFER_SOURCE_ACCOUNT, YES, Constants.PARAM_DATATYPE_STRING));
		result.addParam(new Param(PARAM_TRANSFER_DESTINATION_ACCOUNT, YES, Constants.PARAM_DATATYPE_STRING));
		if (account.getAccountType().equalsIgnoreCase(Constants.ACCOUNT_TYPE_CHECKING)
				|| account.getAccountType().equalsIgnoreCase(Constants.ACCOUNT_TYPE_SAVINGS)) {
			
			String interestPropertyName="";
			if(account.getAccountType().equalsIgnoreCase(Constants.ACCOUNT_TYPE_CHECKING)) {
				interestPropertyName = AccountsConstants.PROPERTY_DEBIT_INTEREST;
			}else {
				interestPropertyName = AccountsConstants.PROPERTY_CREDIT_INTEREST;
			}
			
			if (result.getParamByName("interests") != null) {
				Object interests = result.getParamByName("interests").getObjectValue();

				JSONArray interestArr = null;
				JSONObject interestObj = null;
				
				if (interests instanceof JSONArray) {
					interestArr = (JSONArray) interests;
				}
				if (interestArr != null) {
					for (int i = 0; i < interestArr.length(); i++) {
						JSONObject obj = interestArr.getJSONObject(i);
						if (obj.has("interestPropertyName")) {
							if (obj.getString("interestPropertyName").equalsIgnoreCase(interestPropertyName)) {
								interestObj = obj;
								break;
							}
						}
					}
					if (interestObj != null) {
						if (interestObj.has(AccountsConstants.CONSOLIDATED_INTERESTS)) {
							JSONArray consolidatedInterestArr = interestObj.getJSONArray(AccountsConstants.CONSOLIDATED_INTERESTS);
								if(consolidatedInterestArr!=null && consolidatedInterestArr.length()>0) {
									JSONObject firstConsolidatedInterestObj = consolidatedInterestArr.getJSONObject(0);
									String firstConsolidatedInterest = firstConsolidatedInterestObj.getString(AccountsConstants.CONSOLIDATED_INTEREST);
							        String[] interestRate = firstConsolidatedInterest.toString().split("%");
							        result.getParamByName(DIVIDEND_RATE).setValue(interestRate[0]);
								}
						}
						
						if (interestObj.has(INTERESTPAIDYTD)) {
							result.addParam(new Param(DIVIDENDPAIDYTD, interestObj.get(INTERESTPAIDYTD).toString(),
									Constants.PARAM_DATATYPE_STRING));
						}
						
						if (interestObj.has(INTERESTPAIDYTD)) {
							result.addParam(new Param(DIVIDENDPAIDYTD, interestObj.get(INTERESTPAIDYTD).toString(),
									Constants.PARAM_DATATYPE_STRING));
						}
						if (interestObj.has(LASTPAIDINTERESTAMOUNT)) {
							result.addParam(new Param(DIVIDEND_LAST_PAID_AMOUNT,
									interestObj.get(LASTPAIDINTERESTAMOUNT).toString(),
									Constants.PARAM_DATATYPE_STRING));
						}
						if (interestObj.has(LASTPAYMENTDATE)) {
							result.addParam(new Param(LASTPAYMENTDATE, interestObj.getString("lastPaymentDate"),
									Constants.PARAM_DATATYPE_STRING));
						}
						if (interestObj.has(ACCRUALAMOUNT)) {
							result.addParam(new Param(ACCRUALAMOUNT, interestObj.get(ACCRUALAMOUNT).toString(),
									Constants.PARAM_DATATYPE_STRING));
							result.addParam(new Param("accruedInterest", interestObj.get(ACCRUALAMOUNT).toString(),
									Constants.PARAM_DATATYPE_STRING));
						}
					}
				}
			}
		}
		return result;
	}

}
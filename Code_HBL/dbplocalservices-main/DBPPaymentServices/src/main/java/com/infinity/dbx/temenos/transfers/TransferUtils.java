package com.infinity.dbx.temenos.transfers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.externalAccounts.ExternalAccount;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;

public class TransferUtils {
    
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final String ACOTHER_TRANSFER_PRODUCT_ID = "ACOTHER";
    private static final String PARKING_ACCOUNT_TRANSFER_PRODUCT_ID = "ACOTHER";
    private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";

	public static void convertFrequencyToT24Type(HashMap params) {
		String frequency = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_TYPE);
		String startDate = CommonUtils
				.convertDateToYYYYMMDD(CommonUtils.getParamValue(params, Constants.PARAM_SCHEDULED_DATE));
		params.put(Constants.PARAM_SCHEDULED_DATE, startDate);
		startDate = startDate.replaceAll("-", "");
		String currentFrequency = "";
		switch (frequency) {
		case Constants.FREQUENCY_DAILY:
			currentFrequency = startDate+ TransferConstants.DAILY_FREQUENCY_VALUE;
			break;
		case Constants.FREQUENCY_WEEKLY:
			currentFrequency = startDate + TransferConstants.WEEKLY_FREQUENCY_VALUE;
			break;
		case TransferConstants.FREQUENCY_BI_WEEKLY:
			currentFrequency = startDate + TransferConstants.BIWEEKLY_FREQUENCY_VALUE;
			frequency = TransferConstants.BI_WEEKLY_FREQUENCY;
			break;
		case Constants.FREQUENCY_MONTHLY:
			//currentFrequency = startDate + TransferConstants.MONTHLY_FREQUENCY_VALUE + startDate.substring(6);
			currentFrequency = startDate + TransferConstants.MONTHLY_FREQUENCY_VALUE;
			break;
		case Constants.FREQUENCY_QUARTERLY:
			//currentFrequency = startDate + TransferConstants.QUARTERLY_FREQUENCY_VALUE + startDate.substring(6);
			currentFrequency = startDate + TransferConstants.QUARTERLY_FREQUENCY_VALUE;
			break;
		case Constants.FREQUENCY_HALF_YEARLY:
			//currentFrequency = startDate + TransferConstants.SEMI_ANNUAL_FREQUENCY_VALUE + startDate.substring(6);
			currentFrequency = startDate + TransferConstants.SEMI_ANNUAL_FREQUENCY_VALUE;
			frequency = TransferConstants.HALF_YEARLY_FREQUENCY;
			break;
		case Constants.FREQUENCY_YEARLY:
			//currentFrequency = startDate + TransferConstants.ANNUAL_FREQUENCY_VALUE + startDate.substring(6);
			currentFrequency = startDate + TransferConstants.ANNUAL_FREQUENCY_VALUE;
			frequency = TransferConstants.YEARLY_FREQUENCY;
			break;
		}
		params.put(Constants.PARAM_FREQUENCY_TYPE, frequency);
		params.put(TransferConstants.PARAM_CURRENT_FREQUENCY, currentFrequency);
	}

	public static void setPaymentDetails(HashMap params, DataControllerRequest request) {

		String productId = null;
		String paymentMethod = null;
		String currencyId = null;

		String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
		String toAccountNumber = CommonUtils.getParamValue(params, Constants.PARAM_TO_ACCOUNT_NUMBER);
		if(StringUtils.isBlank(toAccountNumber))
		{
			toAccountNumber = request.getParameter(TransferConstants.EXTERNAL_ACCOUNT_NUMBER);
		}

		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		Gson gson = new Gson();
		if (PARKING_ACCOUNT_TRANSFER.equalsIgnoreCase(transactionType)) {
			productId = PARKING_ACCOUNT_TRANSFER_PRODUCT_ID;
			paymentMethod = TransferConstants.INTERNAL_TRANSFER_PAYMENT_METHOD;
		}
		if (TransferConstants.TRANSCTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
			productId = TransferConstants.INTERNAL_TRANSFER_PRODUCT_ID;
			paymentMethod = TransferConstants.INTERNAL_TRANSFER_PAYMENT_METHOD;
			if (isLoanAccount(toAccountNumber, request))
                productId = ACOTHER_TRANSFER_PRODUCT_ID;
		} else if (TransferConstants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {

			String serviceName = CommonUtils.getParamValue(params, TransferConstants.SERVICE_NAME);
			String paymentType = CommonUtils.getParamValue(params, TemenosConstants.PARAM_PAYMENT_TYPE);
			
			if ((StringUtils.isNotBlank(serviceName)
					&& TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID.equalsIgnoreCase(serviceName))) {
				if(StringUtils.isNotBlank(paymentType)){
				    productId = setDomesticProductId(paymentType);
				}else{
				    productId = TransferConstants.INTRA_TRANSFER_PRODUCT_ID;
				}
				request.addRequestParam_(TransferConstants.PARAM_OTHER_BANK_TRANSFER, "true");
		        params.put(TransferConstants.PARAM_OTHER_BANK_TRANSFER, "true");
				paymentMethod = TransferConstants.DOMESTIC_TRANSFER_PAYMENT_METHOD;
				if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_NAME))) {

					/**
					 * Checking whether to account is same bank account or not and setting
					 * respective values
					 */

					/*
					 * If account is not same bank account then checking whether is other bank
					 * account or not and setting respective values
					 */

					HashMap<String, ExternalAccount> otherBankAccounts = temenosUtils.getExternalAccountsMapFromCache(
							TemenosConstants.SESSION_ATTRIB_OTHERBANK_ACCOUNT, request);

					ExternalAccount otherBankAccount = otherBankAccounts != null	
							&& otherBankAccounts.containsKey(toAccountNumber) ? otherBankAccounts.get(toAccountNumber)
									: null;
					if (otherBankAccount != null) {
						request.addRequestParam_(TransferConstants.PARAM_BENEFICIARY_NAME,
								otherBankAccount.getNickName());
						params.put(TransferConstants.PARAM_BENEFICIARY_NAME, otherBankAccount.getNickName());
					}
				}

			} else if ((StringUtils.isNotBlank(serviceName)
					&& TransferConstants.INTRA_BANK_TRANSFER_SERVICE_ID.equalsIgnoreCase(serviceName))) {
				/**
				 * Getting domestic Accounts that are in session
				 */
				String frequency = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_TYPE);
				if(Constants.FREQUENCY_ONCE.equalsIgnoreCase(frequency))
				productId = TransferConstants.ACOTHER_TRANSFER_PRODUCT_ID;
				else {
					productId = TransferConstants.DOMESTIC_TRANSFER_PRODUCT_ID;
				}
				alert.prepareError("##TransferFrequency"+frequency).log();
				paymentMethod = TransferConstants.DOMESTIC_TRANSFER_PAYMENT_METHOD;
				HashMap<String, ExternalAccount> domesticAccounts = temenosUtils
						.getExternalAccountsMapFromCache(TemenosConstants.SESSION_ATTRIB_DOMESTIC_ACCOUNT, request);

				ExternalAccount domesticAccount = domesticAccounts != null
						&& domesticAccounts.containsKey(toAccountNumber) ? domesticAccounts.get(toAccountNumber) : null;
				if (domesticAccount != null) {
					request.addRequestParam_(TransferConstants.PARAM_BENEFICIARY_NAME, domesticAccount.getNickName());
					params.put(TransferConstants.PARAM_BENEFICIARY_NAME, domesticAccount.getNickName());
				}
			} else {
				productId = TransferConstants.INTERNATIONAL_TRANSFER_PRODUCT_ID;
				paymentMethod = TransferConstants.INTERNATIONAL_TRANSFER_PAYMENT_METHOD;

				if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransferConstants.PARAM_BENEFICIARY_NAME))
						|| StringUtils.isBlank(CommonUtils.getParamValue(params, TransferConstants.PARAM_SWIFT_CODE))) {

					HashMap<String, ExternalAccount> internationalAccounts = temenosUtils
							.getExternalAccountsMapFromCache(TemenosConstants.SESSION_ATTRIB_INTERNATIONAL_ACCOUNT,
									request);
					ExternalAccount internationalAccount = internationalAccounts != null
							&& internationalAccounts.containsKey(toAccountNumber)
									? internationalAccounts.get(toAccountNumber)
									: null;
					/**
					 * Checking whether to account is international or not and setting respective
					 * values
					 */
					if (internationalAccount != null) {
						request.addRequestParam_(TransferConstants.PARAM_BENEFICIARY_NAME,
								internationalAccount.getNickName());
						params.put(TransferConstants.PARAM_BENEFICIARY_NAME, internationalAccount.getNickName());
						request.addRequestParam_(TransferConstants.PARAM_SWIFT_CODE,
								internationalAccount.getSwiftCode());

						params.put(TransferConstants.PARAM_SWIFT_CODE, internationalAccount.getSwiftCode());
					}
				}
			}

		}

		/**
		 * setting currencyId
		 */

		String frequency = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_TYPE);
		if (!Constants.FREQUENCY_ONCE.equals(frequency)) {
			/* required for standing orders */
			request.addRequestParam_(TransferConstants.PAYMENTMETHOD, paymentMethod);
			params.put(TransferConstants.PAYMENTMETHOD, paymentMethod);
			request.addRequestParam_(TransferConstants.SUPRESSFT, TransferConstants.SUPRESSFT_VALUE);
			params.put(TransferConstants.SUPRESSFT, TransferConstants.SUPRESSFT_VALUE);
		}
		/* required for both standing and payment orders */
		request.addRequestParam_(Constants.PARAM_TRANSACTION_TYPE, productId);
		params.put(Constants.PARAM_TRANSACTION_TYPE, productId);
	}

	public static String getEndDateBasedOnFrequency(HashMap params, String noOfRecurrences) {

		int numberOfRecurrences = StringUtils.isNumeric(noOfRecurrences) ? Integer.parseInt(noOfRecurrences) : 0;
		int noofDays = 0;
		String frequency = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_TYPE);
		String startDate = CommonUtils
				.convertDateToYYYYMMDD(CommonUtils.getParamValue(params, Constants.PARAM_SCHEDULED_DATE));
		switch (frequency) {
		case Constants.FREQUENCY_DAILY:
			noofDays = numberOfRecurrences * 1;
			break;
		case Constants.FREQUENCY_WEEKLY:
			noofDays = numberOfRecurrences * 7;
			break;
		case TransferConstants.BI_WEEKLY_FREQUENCY:
			noofDays = numberOfRecurrences * 14;
			break;
		case Constants.FREQUENCY_MONTHLY:
			noofDays = numberOfRecurrences * 30;
			break;
		case Constants.FREQUENCY_QUARTERLY:
			noofDays = numberOfRecurrences * 91;
			break;
		case TransferConstants.HALF_YEARLY_FREQUENCY:
			noofDays = numberOfRecurrences * 182;
			break;
		case TransferConstants.YEARLY_FREQUENCY:
			noofDays = numberOfRecurrences * 365;
			break;
		}
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(Constants.DATE_YYYYMMDD);
		LocalDate localDate = LocalDate.parse(startDate);
		LocalDate plusDays = localDate.plusDays(noofDays);
		return plusDays.format(ofPattern);
	}

	public static Result setErrorDetails(Result result) {
		try {
			String errid = result.getParamValueByName(TransferConstants.PARAM_ERR_ID);
			String errCode = result.getParamValueByName("errcode");
			Result res = new Result();
			if (StringUtils.isNotBlank(errid)) {
				switch (errid) {
				case TransferConstants.ERR_NON_WORKING_DAY_CODE:
					CommonUtils.setErrCode(res, TransferConstants.ERR_NON_WORKING_DAY_CODE_DBX);
					CommonUtils.setErrMsg(res, TransferConstants.ERR_NON_WORKING_DAY_MSG);
					break;
				case TransferConstants.ERR_OVER_DRAFT_CODE:
					CommonUtils.setErrCode(res, TransferConstants.ERR_OVER_DRAFT_CODE_DBX);
					CommonUtils.setErrMsg(res, result.getParamValueByName(TransferConstants.PARAM_DESCRIPTION));
				default:
					CommonUtils.setErrCode(res, TransferConstants.ERR_DEFAULT_CODE_DBX);
					CommonUtils.setErrMsg(res, result.getParamValueByName(TransferConstants.PARAM_DESCRIPTION));
					break;
				}

			} else {
				CommonUtils.setErrCode(res, TransferConstants.ERR_DEFAULT_CODE_DBX);
				CommonUtils.setErrMsg(res, result.getParamValueByName(Constants.PARAM_ERR_MSG));
			}
			//Set dbpErrCode
			if (StringUtils.isNotBlank(errCode))
                res.addParam("dbpErrCode", errCode);
			else
			    res.addParam("dbpErrCode", TransferConstants.ERR_DEFAULT_CODE_DBX);
			CommonUtils.setOpStatusOk(res);
			res.addHttpStatusCodeParam(Constants.PARAM_HTTP_STATUS_OK);
			return res;
		} catch (Exception e) {
			Result errorResult = new Result();
			CommonUtils.setOpStatusError(errorResult);
			CommonUtils.setErrMsg(errorResult, e.getMessage());
			return errorResult;
		}

	}
	
	public static String setDomesticProductId(String paymentType){
	    switch (paymentType) {
        // Case statements
        case TransferConstants.INTRA_TRANSFER_PRODUCT_ID:
            return TransferConstants.INTRA_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.SEPA_TRANSFER_PRODUCT_ID:
            return TransferConstants.SEPA_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.FEDWIRE_TRANSFER_PRODUCT_ID:
            return TransferConstants.FEDWIRE_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.ACH_TRANSFER_PRODUCT_ID:
            return TransferConstants.ACH_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.CHAPS_TRANSFER_PRODUCT_ID:
            return TransferConstants.CHAPS_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.FASTER_TRANSFER_PRODUCT_ID:
            return TransferConstants.FASTER_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.RINGS_TRANSFER_PRODUCT_ID:
            return TransferConstants.RINGS_TRANSFER_PRODUCT_ID;
            
        case TransferConstants.BISERA_TRANSFER_PRODUCT_ID:
            return TransferConstants.BISERA_TRANSFER_PRODUCT_ID;
        // Default case statement
        default:
            return TransferConstants.DOMESTIC_TRANSFER_PRODUCT_ID;
        }
	}
	
	public static String getUniqueNumericString(int length) {
        SimpleDateFormat idFormatter = new SimpleDateFormat("ssSSS");
        String dateString = idFormatter.format(new Date());
        String randomString;
        SecureRandom secureRand = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int randStrLen;
        if (length > 10) {
            randStrLen = length - dateString.length();
        } else {
            randStrLen = 10 - dateString.length();
        }
        for (int i = 0; i < randStrLen; i++) {
            sb.append(Integer.toString(secureRand.nextInt(10)));
        }
        randomString = sb.toString();
        return randomString + dateString;

    }
	
	public static Object retrieve(String key) {
        ResultCache resultCache = null;
        try {
            resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
            if (resultCache != null && StringUtils.isNotBlank(key)) {
                String value = (String) resultCache.retrieveFromCache(key);
                SessionMap sessionData = new SessionMap();
                sessionData.setData(value);
                return sessionData;
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
        }
        return null;

    }

    public static boolean isLoanAccount(String toAccountNumber, DataControllerRequest request){
        String customerId = "";;
        try {
            customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                    .get("customer_id");
        } catch (Exception e) {
            alert.prepareError("Unable to retrieve customer id from session " +e).log();
        } 
        SessionMap internalAccntsMap = (SessionMap) retrieve("INTERNAL_BANK_ACCOUNTS" + customerId);
        if (StringUtils.isNotBlank(internalAccntsMap.toString())) {
            JSONObject accounts = new JSONObject(internalAccntsMap.toString());
            if (accounts.has(toAccountNumber)) {
                JSONObject account = (JSONObject) accounts.get(toAccountNumber);
                if (account.has("accountType")) {
                    if (account.get("accountType").toString().equalsIgnoreCase("Loan"))
                        return true;
                }
            }
        }
        return false;
    }
}

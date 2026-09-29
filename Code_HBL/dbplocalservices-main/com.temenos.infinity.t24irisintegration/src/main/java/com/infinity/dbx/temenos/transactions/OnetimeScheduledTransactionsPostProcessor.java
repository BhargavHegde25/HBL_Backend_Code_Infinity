package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class OnetimeScheduledTransactionsPostProcessor extends BasePostProcessor {

	private static final String CHARGE_BEARER = null;

    private static final String PAID_BY = null;


    private static Map<String, String> CHARGE_BEARER_MAP = new HashMap<>();
	
	static {
		CHARGE_BEARER_MAP.put(TransactionConstants.SHA_PAID_BY, TransactionConstants.PAID_BY_BOTH);
		CHARGE_BEARER_MAP.put(TransactionConstants.BEN_PAID_BY, TransactionConstants.PAID_BY_BENEFICIARY);
		CHARGE_BEARER_MAP.put(TransactionConstants.OUR_PAID_BY, TransactionConstants.PAID_BY_SELF);
	} 
	
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset transactionsDS = result.getDatasetById(TransactionConstants.TRANSACTION);

		String template_version = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
				TemenosConstants.PROP_PREFIX_TEMENOS, TemenosConstants.PROP_PREFIX_TRANSACTIONS,
				TemenosConstants.PROP_PREFIX_VERSION);
		String internalServiceName = "", otherBankServiceName = "", internationalServiceName = "",
				intrabankServiceName = "";

		/*
		 * getting service id based on version
		 */
		if (StringUtils.isNotBlank(template_version)
				&& TemenosConstants.VERSION_4_2_6.equalsIgnoreCase(template_version)) {

			internalServiceName = TransferConstants.INTERNAL_TRANSFER_SERVICE_ID_PREV;
			otherBankServiceName = TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID_PREV;
			internationalServiceName = TransferConstants.INTERNATIONAL_TRANSFER_SERVICE_ID;
			intrabankServiceName = TransferConstants.INTRA_BANK_TRANSFER_SERVICE_ID;
		} else {
			internalServiceName = TransferConstants.INTERNAL_TRANSFER_SERVICE_ID;
			otherBankServiceName = TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID;
			internationalServiceName = TransferConstants.INTERNATIONAL_TRANSFER_SERVICE_ID;
			intrabankServiceName = TransferConstants.INTRA_BANK_TRANSFER_SERVICE_ID;
		}

		List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
		if (transactionRecords == null || transactionRecords.isEmpty()) {
			return TemenosUtils.getEmptyResult(TransactionConstants.TRANSACTION);
		} else {
			if (transactionRecords.size() != 0) {
				/**
				 * Getting domestic Accounts that are in session
				 */

				// TemenosUtils temenosUtils = TemenosUtils.getInstance();
				/*
				 * HashMap<String, ExternalAccount> domesticAccounts = temenosUtils
				 * .getExternalAccountsMapFromCache(TemenosConstants.
				 * SESSION_ATTRIB_OTHERBANK_ACCOUNT, request);
				 * 
				 * HashMap<String, ExternalAccount> internationalAccounts =
				 * temenosUtils.getExternalAccountsMapFromCache(
				 * TemenosConstants.SESSION_ATTRIB_INTERNATIONAL_ACCOUNT, request);
				 */
				for (Record record : transactionRecords) {
					Dataset credits = record.getDatasetById(TransactionConstants.PAYMENT_ORDER_CREDITS_ARRAY_KEY);
					if (credits != null && !credits.getAllRecords().isEmpty()) {
						record.addStringParam(PARAM_TO_ACCOUNT_NUMBER,
								CommonUtils.getParamValue(credits.getRecord(0), PARAM_TO_ACCOUNT_NUMBER));
						record.addStringParam(PARAM_TO_ACCOUNT_NAME,
								CommonUtils.getParamValue(credits.getRecord(0), PARAM_TO_ACCOUNT_NAME));
					}
					Dataset beneficiaries = record
							.getDatasetById(TransactionConstants.PAYMENT_ORDER_BENEFICIARIES_ARRAY_KEY);
					if (beneficiaries != null && !beneficiaries.getAllRecords().isEmpty()) {
						record.addStringParam(PARAM_TO_ACCOUNT_NUMBER, CommonUtils.getParamValue(
								beneficiaries.getRecord(0), TransactionConstants.PARAM_COUNTER_PARTY_ID));

						String domesticIBAN = CommonUtils.getParamValue(beneficiaries.getRecord(0),
								TransactionConstants.BENEFICIARY_IBAN_KEY);
						if (!domesticIBAN.isEmpty()) {
							record.addStringParam(PARAM_TO_ACCOUNT_NUMBER, domesticIBAN);
						}

						record.addStringParam(PARAM_TO_ACCOUNT_NAME, CommonUtils
								.getParamValue(beneficiaries.getRecord(0), TransferConstants.PARAM_BENEFICIARY_NAME));
						record.addStringParam(TransferConstants.PARAM_BENEFICIARY_NAME, CommonUtils
								.getParamValue(beneficiaries.getRecord(0), TransferConstants.PARAM_BENEFICIARY_NAME));

					}
					String transactionType = CommonUtils.getParamValue(record, PARAM_TRANSACTION_TYPE);
					if (transactionType.equalsIgnoreCase(TransactionConstants.ACTRF_PRODUCT_ID)) {
						record.addParam(new Param(TransferConstants.SERVICE_NAME, internalServiceName,
								Constants.PARAM_DATATYPE_STRING));
						record.addParam(PARAM_TRANSACTION_TYPE, TransactionConstants.INTERNAL_TRANSFER);
					} else {
						record.addParam(PARAM_TRANSACTION_TYPE, TransactionConstants.EXTERNAL_TRANSFER);
						switch (transactionType) {
						case TransactionConstants.INATIONAL_PRODUCT_ID:
							record.addParam(new Param(TransferConstants.SERVICE_NAME, internationalServiceName,
									Constants.PARAM_DATATYPE_STRING));
							break;
						case TransactionConstants.DOMESTIC_PRODUCT_ID:
							record.addParam(new Param(TransferConstants.SERVICE_NAME, intrabankServiceName,
									Constants.PARAM_DATATYPE_STRING));
							break;
						case TransactionConstants.INSTA_PAY_PRODUCT_ID:
							record.addParam(new Param(TransferConstants.SERVICE_NAME, otherBankServiceName,
									Constants.PARAM_DATATYPE_STRING));
							break;
						case TransactionConstants.SEPA_PRODUCT_ID:
							record.addParam(new Param(TransferConstants.SERVICE_NAME, otherBankServiceName,
									Constants.PARAM_DATATYPE_STRING));
							break;
						}
					}
					if(record.getParamValueByName(CHARGE_BEARER) != null && record.getParamValueByName(CHARGE_BEARER) != StringUtils.EMPTY) {
						record.addParam(PAID_BY, CHARGE_BEARER_MAP.get(record.getParamValueByName(CHARGE_BEARER)));
					}
					record.addStringParam(PARAM_FREQUENCY_TYPE, Constants.FREQUENCY_ONCE);
					record.addParam(TransactionConstants.PARAM_IS_SCHEDULED, Constants.TRUE);
					String description = record.getParamValueByName(TransactionConstants.PARAM_VALUE_TO_ACCOUNT_NAME);

					description = TransactionConstants.TRANSFER_TO + description;
					record.addParam(TransactionConstants.PARAM_DESCRIPTION, description);
					record.addParam(PARAM_STATUS_DESCRIPTION, TransactionConstants.PARAM_VALUE_PENDING);
    			}  
             }   
		}
		return result;
	}
}
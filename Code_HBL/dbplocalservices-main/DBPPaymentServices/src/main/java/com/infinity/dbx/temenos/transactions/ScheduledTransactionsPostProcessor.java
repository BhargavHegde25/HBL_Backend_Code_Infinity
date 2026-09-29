package com.infinity.dbx.temenos.transactions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.utils.MultiValueUtil;
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
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ScheduledTransactionsPostProcessor extends BasePostProcessor implements TransactionConstants {
    private static Map<String, String> CHARGE_BEARER_MAP = new HashMap<>();

    static {
        CHARGE_BEARER_MAP.put(TransactionConstants.SHA_PAID_BY, TransactionConstants.PAID_BY_BOTH);
        CHARGE_BEARER_MAP.put(TransactionConstants.BEN_PAID_BY, TransactionConstants.PAID_BY_BENEFICIARY);
        CHARGE_BEARER_MAP.put(TransactionConstants.OUR_PAID_BY, TransactionConstants.PAID_BY_SELF);

    }

    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
	Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
        Map<String, ArrayList<Record>> paymentOrdersMap = new HashMap<>(); 
        try {
            Dataset transactionsDS = result.getDatasetById(TRANSACTION);

            List<Record> transactionRecords = transactionsDS != null ? transactionsDS.getAllRecords() : null;
            if (transactionRecords == null || transactionRecords.isEmpty()) {
                diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
                return TemenosUtils.getEmptyResult(TRANSACTION);
            }

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
                intrabankServiceName = TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID_PREV;
                internationalServiceName = TransferConstants.INTERNATIONAL_TRANSFER_SERVICE_ID_PREV;

            } else {
                internalServiceName = TransferConstants.INTERNAL_TRANSFER_SERVICE_ID;
                otherBankServiceName = TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID;
                intrabankServiceName = TransferConstants.INTRA_BANK_TRANSFER_SERVICE_ID;
                internationalServiceName = TransferConstants.INTERNATIONAL_TRANSFER_SERVICE_ID;
            }

            HashMap<String, String> transactions = new HashMap<String, String>();
            TransactDate transactDate = TransactionUtils.getTransactDate(request);
   
            if (transactionRecords.size() != 0) {
                for (Record record : transactionRecords) {
                    String id = record.getParamValueByName(PARAM_TRANSACTION_ID);
                    String fromAccountNumber = id.substring(0, id.indexOf("."));
                    String description = record.getParamValueByName(PARAM_VALUE_TO_ACCOUNT_NAME);

                    description = TRANSFER_TO + description;
                    record.addParam(PARAM_DESCRIPTION, description);
                    record.addParam(TransactionConstants.PARAM_STATUS_DESCRIPTION, PARAM_VALUE_PENDING);
                    String transactionType = record.getParamValueByName(STANDING_ORDER_PRODUCT_NAME_KEY);
                    if (transactionType != StringUtils.EMPTY) {
                        if (TransactionConstants.INATIONAL_PRODUCT_ID.equalsIgnoreCase(transactionType)) {
                            transactionType = EXTERNAL_TRANSFER;
                            record.addParam(new Param(TransferConstants.SERVICE_NAME, internationalServiceName,
                                    Constants.PARAM_DATATYPE_STRING));
                            record.addParam(TransactionConstants.PARAM_TO_EXTERNAL_ACCOUNT_NUMBER,
                                    record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));

                            record.addParam(TemenosConstants.PARAM_BENEFICARY_NAME,
                                    record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NAME));
                        } else if (TransactionConstants.DOMESTIC_PRODUCT_ID.equalsIgnoreCase(transactionType)
                                || TransactionConstants.INSTA_PAY_PRODUCT_ID.equalsIgnoreCase(transactionType)
                                || TransactionConstants.SEPA_PRODUCT_ID.equalsIgnoreCase(transactionType)|| TransactionConstants.DOMESTIC_PRODUCT_ID_ACOTHER.equalsIgnoreCase(transactionType)) {
                            record.addParam(TemenosConstants.PARAM_BENEFICARY_NAME,
                                    record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NAME));
                            if (TransactionConstants.DOMESTIC_PRODUCT_ID.equalsIgnoreCase(transactionType)
                                    && !StringUtils.isAlphanumeric(
                                            result.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER))) {
                                record.addParam(new Param(TransferConstants.SERVICE_NAME, intrabankServiceName,
                                        Constants.PARAM_DATATYPE_STRING));
                                record.addParam(TransactionConstants.PARAM_TO_EXTERNAL_ACCOUNT_NUMBER,
                                        record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));

                            } else {
                                record.addParam(TemenosConstants.PARAM_IBAN,
                                        record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));
                                record.addParam(new Param(TransferConstants.SERVICE_NAME, otherBankServiceName,
                                        Constants.PARAM_DATATYPE_STRING));
                            }
                            transactionType = EXTERNAL_TRANSFER;
                        } else if (transactionType != null
                                && transactionType.equalsIgnoreCase(TransactionConstants.ACTRF_PRODUCT_ID)) {
                            transactionType = INTERNAL_TRANSFER;
                            record.addParam(new Param(TransferConstants.SERVICE_NAME, internalServiceName,
                                    Constants.PARAM_DATATYPE_STRING));
                        }
                        // else {
                        // transactionType = EXTERNAL_TRANSFER;
                        // record.addParam(new
                        // Param(TransferConstants.SERVICE_NAME,
                        // intrabankServiceName,
                        // Constants.PARAM_DATATYPE_STRING));
                        // record.addParam(TransactionConstants.PARAM_TO_EXTERNAL_ACCOUNT_NUMBER,
                        // record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NUMBER));
                        //
                        // record.addParam(TemenosConstants.PARAM_BENEFICARY_NAME,
                        // record.getParamValueByName(Constants.PARAM_TO_ACCOUNT_NAME));
                        // }
                        Calendar calender = Calendar.getInstance();
                        calender.setTime(transactDate.getCurrentWorkingDate());
                        Date currentWorkingDate = calender.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("YYYYMMdd");
                        String strCurrentWorkingDate = dateFormat.format(currentWorkingDate);
                        Date endDate = StringUtils.isNotBlank(record.getParamValueByName("frequencyEndDate"))
                                ? new SimpleDateFormat(TransactionConstants.TRANSACTION_DATE_FORMAT).parse(record.getParamValueByName("frequencyEndDate"))
                                : null;
                        if (endDate == null || endDate.after(currentWorkingDate)) {
                            record.addParam(TransactionConstants.STATUS_DESCRIPTION, TransactionConstants.ACTIVE);
                        } else {
                            record.addParam(TransactionConstants.STATUS_DESCRIPTION, TransactionConstants.EXPIRED);
                        }
                        record.addParam(TransactionConstants.PARAM_TRANSACTION_TYPE, transactionType);
                        record.addParam(PARAM_IS_SCHEDULED, Constants.TRUE);
                        record.addParam(PARAM_FREQUENCY_TYPE, TransactionUtils.convertHBLT24FrequencyToDbxFrequencyType(
                                record.getParamValueByName(PARAM_FREQUENCY_TYPE)));
                    }
                    if (record.getParamValueByName(CHARGE_BEARER) != null
                            && record.getParamValueByName(CHARGE_BEARER) != StringUtils.EMPTY) {
                        record.addParam(PAID_BY, CHARGE_BEARER_MAP.get(record.getParamValueByName(CHARGE_BEARER)));
                    }
                    if (record.getParamValueByName("transactionCurrency") != null)
                    	record.addParam("paymentCurrencyId", record.getParamValueByName("transactionCurrency"));

                    record.addParam(
                            new Param(PARAM_FROM_ACCOUNT_NUMBER, fromAccountNumber, Constants.PARAM_DATATYPE_STRING));
                    transactions.put(id, record.getParamValueByName(TransferConstants.PARAM_VERSION_NUMBER));
                    
                    if (!paymentOrdersMap.containsKey(record.getParamValueByName(TransactionConstants.TRANSACTION_ID_KEY)))  {
			        paymentOrdersMap.put(record.getParamValueByName(TransactionConstants.TRANSACTION_ID_KEY), new ArrayList<Record>());
		    }	
		    paymentOrdersMap.get(record.getParamValueByName(TransactionConstants.TRANSACTION_ID_KEY)).add(record);
                    // Handle TransactionNotes
                    JSONObject stoRecord = ResultToJSON.convertRecord(record);
                    if (stoRecord.has(PARAM_DISPLAY_NAMES)) {
                        JSONArray displayArray = stoRecord.getJSONArray(PARAM_DISPLAY_NAMES);
                        String displayname = MultiValueUtil.mergeMultiValue(displayArray.toString(),
                                PARAM_DISPLAY_NAME);
                        if (StringUtils.isNotBlank(displayname))
                            record.addParam(PARAM_NOTES, displayname);
                    }
                }
            }
            Gson gson = new Gson();
            String transfersInSession = gson.toJson(transactions);
            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            temenosUtils.insertIntoSession(TransferConstants.SESSION_ATRIB_STANDING_ORDERS, transfersInSession,
                    request);
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            CommonUtils.setErrMsg(result, e.toString());
        }
        result.clearDatasets();
        Dataset mergedDataset = this.getMergedDataset(paymentOrdersMap);
        mergedDataset.setId(TransactionConstants.TRANSACTION);
        result.addDataset(mergedDataset);
        return result;
    }

	private Dataset getMergedDataset(Map<String, ArrayList<Record>> paymentOrdersMap) {
		Dataset mergedPaymentOrders = new Dataset();
		for (Map.Entry<String, ArrayList<Record>> entry : paymentOrdersMap.entrySet()) {
			ArrayList<Record> entryValue = entry.getValue();
			if (entryValue.size() > 1 ) {
				mergedPaymentOrders.addRecord(this.getMergedRecord(entryValue));
			}
			else {
				Record record = entryValue.get(0);
				String recordStatus = record.getParamValueByName(TransactionConstants.RECORD_STATUS);
				if (recordStatus == null) {
					record.addParam(TransactionConstants.PENDING_APPROVAL, "false",  "boolean");
				}
				else if (recordStatus.equalsIgnoreCase(TransactionConstants.IHLD)) {
					// skipping IHLD records
					continue;
				}
				else {
					record.addParam(TransactionConstants.PENDING_APPROVAL, String.valueOf(recordStatus.equals(TransactionConstants.INAU) || recordStatus.equals(TransactionConstants.RNAU)),  "boolean");
				}
				mergedPaymentOrders.addRecord(entryValue.get(0));
			}
		}
		return mergedPaymentOrders;		
	}
	
	private Record getMergedRecord(ArrayList<Record> entryValue) {
		Record mergedRecord = null;
		String recordStatus = "";
		for (Record entry: entryValue) {
			String currentRecordStatus = entry.getParamValueByName(TransactionConstants.RECORD_STATUS);
			if (currentRecordStatus == "" || currentRecordStatus == null) {
				mergedRecord = entry;
			}
			else  {
				recordStatus = currentRecordStatus;
			}
		}
		mergedRecord.addParam(TransactionConstants.PENDING_APPROVAL, String.valueOf(recordStatus.equals(TransactionConstants.INAU) || recordStatus.equals(TransactionConstants.RNAU)),  "boolean");
		return mergedRecord;
	}
}
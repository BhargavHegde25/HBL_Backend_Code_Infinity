package com.infinity.dbx.temenos.transactions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.constants.TransactionType;
import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class TransactionUtils {
    
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    public static String convertT24FrequencyToDbxFrequencyType(String frequency) {
        if (StringUtils.isNotBlank(frequency)) {
        	if(frequency.contains("Every working day"))
            {
            	frequency = Constants.FREQUENCY_DAILY;
            }
            else if(frequency.contains("Weekly"))
            {
            	frequency = Constants.FREQUENCY_WEEKLY;
            }
            else if(frequency.contains("Every 2 weeks"))
            {
            	  frequency = TransferConstants.FREQUENCY_BI_WEEKLY;            
            }
            else if(frequency.contains("Monthly on day"))
            {
            	 frequency = Constants.FREQUENCY_MONTHLY;          	
            }
            else if(frequency.contains("Every 3 months"))
            {
            	frequency = Constants.FREQUENCY_QUARTERLY;            
            }
            else if(frequency.contains("Every 6 months"))
            {
            	frequency = Constants.FREQUENCY_HALF_YEARLY;
            }
            else if(frequency.contains("Every 12 months"))
            {
            	 frequency = Constants.FREQUENCY_YEARLY;
            }
            else
            {
            	frequency="";
            }
        }
        return frequency;
    }

    public static String getMinusDays(String noOfDays) {
        String fromDate = "";
        if (StringUtils.isNotBlank(noOfDays) && StringUtils.isNumeric(noOfDays)) {
            int numberOfDays = Integer.parseInt(noOfDays);
            LocalDate currentDate = LocalDate.now();
            LocalDate minusDays = currentDate.minusDays(numberOfDays);
            fromDate = minusDays.toString();
        }
        return fromDate;
    }

    public static void getT24TransactionType(HashMap params, DataControllerRequest request) {
        String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
        if (StringUtils.equalsIgnoreCase(transactionType, Constants.TRANSCTION_TYPE_WITHDRAWAL)) {
            String minAmount = StringUtils
                    .isNotBlank(CommonUtils.getParamValue(params, Constants.PARAM_SEARCH_MINIMUM_AMOUNT))
                            ? "-" + CommonUtils.getParamValue(params, Constants.PARAM_SEARCH_MINIMUM_AMOUNT)
                            : "";
            String maxAmount = StringUtils
                    .isNotBlank(CommonUtils.getParamValue(params, Constants.PARAM_SEARCH_MIXIMUM_AMOUNT))
                            ? "-" + CommonUtils.getParamValue(params, Constants.PARAM_SEARCH_MIXIMUM_AMOUNT)
                            : "";

            params.put(Constants.PARAM_SEARCH_MINIMUM_AMOUNT, maxAmount);
            params.put(Constants.PARAM_SEARCH_MIXIMUM_AMOUNT, minAmount);
        }
    }

    /**
     * @param transactionType
     * @param request
     * @return
     */
    public static Collection<Integer> getTransactTransactionCodes(TransactionType transactionType,
            DataControllerRequest request) {

        Collection<Integer> transactionCodes = new ArrayList<>();
        TemenosUtils.getInstance().loadTransactionTypeProperties(request);
        Map<String, String> transactionTypesMap = TemenosUtils.getInstance().transactionTypesMap;

        String currentKey, currentValue;
        for (Entry<String, String> entry : transactionTypesMap.entrySet()) {
            currentKey = entry.getKey();
            currentValue = entry.getValue();
            if (currentKey != null && currentValue != null && currentValue.equals(transactionType.getValue())) {
                if (NumberUtils.isParsable(currentKey)) {
                    transactionCodes.add(Integer.parseInt(currentKey));
                }
            }
        }

        return transactionCodes;
    }

    public static String getTransactionType(String transactionTypeParam) {
        if (StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_DEPOSITS)) {
            transactionTypeParam = TransactionType.DEPOSIT.getValue();
        } else if (StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_WITHDRAWALS)) {
            transactionTypeParam = TransactionType.WITHDRAWAL.getValue();
        } else if (StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.P2P_DEBITS)) {
            transactionTypeParam = TransactionType.PERSON_TO_PERSON_DEBITS.getValue();
        } else if (StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.P2P_CREDITS)) {
            transactionTypeParam = TransactionType.PERSON_TO_PERSON_CREDITS.getValue();
        } else if (StringUtils.equalsIgnoreCase(transactionTypeParam, Constants.TRANSCTION_TYPE_CHECKS)) {
            transactionTypeParam = TransactionType.CHECKS.getValue();
        }
        return transactionTypeParam;
    }

    public static TransactDate getTransactDate(DataControllerRequest request) throws Exception {

        String CACHE_KEY_TRANSACT_DATE = "transactDate";
        final int CACHE_TIME = 10 * 60; // 10 minutes

        try {
            Object object = TemenosUtils.getInstance().getDataFromCache(request, CACHE_KEY_TRANSACT_DATE);
            if (null != object) {
                Gson gson = new Gson();
                TransactDate transactDate = gson.fromJson(object.toString() , TransactDate.class);
                return transactDate;
            }

            Result serviceResponseResult = CommonUtils.callIntegrationService(request, new HashMap<String, Object>(),
                    new HashMap<String, Object>(), TemenosConstants.T24_IS_PAYMENTS_VIEW,
                    TemenosConstants.GETBANKDATES_OPERATIONS_ID, true);

            String serviceResponseResultStr = ResultToJSON.convert(serviceResponseResult);
            JSONObject serviceResponseJSON = new JSONObject(serviceResponseResultStr);
            JSONObject transactDatesJSON = serviceResponseJSON.optJSONArray("body").optJSONObject(0);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(TransactionConstants.TRANSACTION_DATE_FORMAT);

            String companyId = transactDatesJSON.optString("companyId");
            Date lastWorkingDate = dateFormatter.parse(transactDatesJSON.optString("lastWorkingDate"));
            Date nextWorkingDate = dateFormatter.parse(transactDatesJSON.optString("nextWorkingDate"));
            Date currentWorkingDate = dateFormatter.parse(transactDatesJSON.optString("currentWorkingDate"));

            TransactDate transactDate = new TransactDate(companyId, lastWorkingDate, nextWorkingDate,
                    currentWorkingDate);
            TemenosUtils.getInstance().insertDataIntoCache(request, transactDate, CACHE_KEY_TRANSACT_DATE, CACHE_TIME);
            return transactDate;
        } catch (Exception e) {
            alert.prepareError("Unable to get transact date" + e).log();
        }
        return new TransactDate();

    }
}

package com.infinity.dbx.temenos.transfers;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.MultiValueUtil;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class Psd2UpdateStandingOrderPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request, response, result);
			String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
			if (StringUtils.isNotBlank(transactionType)
					&& !TransferConstants.TRANSCTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)
					&& !TransferConstants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
				CommonUtils.setOpStatusOk(result);
				result.addParam(new Param(Constants.PARAM_HTTP_STATUS_CODE, Constants.PARAM_HTTP_STATUS_OK,
						Constants.PARAM_DATATYPE_STRING));
				return false;
			}
			TransferUtils.convertFrequencyToT24Type(params);
			String frequencyEndDate;
			TransferUtils.setPaymentDetails(params, request);
			Gson gson = new Gson();
			Type transactionMapType = new TypeToken<HashMap<String, String>>() {
			}.getType();
			String noOfRecurrences = CommonUtils.getParamValue(params, TransferConstants.PARAM_NUMBER_OF_RECURRENCES);
			if (StringUtils.isNotBlank(noOfRecurrences)) {
				frequencyEndDate = TransferUtils.getEndDateBasedOnFrequency(params, noOfRecurrences);
				params.put(Constants.PARAM_FREQUENCY_END_DATE, CommonUtils.convertDateToYYYYMMDD(frequencyEndDate));
			} else {
				frequencyEndDate = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_END_DATE);
				params.put(Constants.PARAM_FREQUENCY_END_DATE, CommonUtils.convertDateToYYYYMMDD(frequencyEndDate));
			}
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			String standingOrdersInSession = (String) temenosUtils
					.retreiveFromSession(TransferConstants.SESSION_ATRIB_STANDING_ORDERS, request);
			if (!"".equalsIgnoreCase(standingOrdersInSession)) {
				HashMap<String, String> transactions = gson.fromJson(standingOrdersInSession, transactionMapType);
				params.put(TransferConstants.PARAM_VERSION_NUMBER, CommonUtils.getParamValue(transactions,
						CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_ID)));
			}
			String transactionNotes = CommonUtils.getParamValue(params, TransferConstants.PARAM_TRANSACTION_NOTES);
            if (StringUtils.isNotBlank(transactionNotes)) {
                transactionNotes = MultiValueUtil.splitMultiValue(TransferConstants.PARAM_STO_DISPLAYNAME,
                        TransferConstants.PARAM_STO_DISPLAYNAME_LENGTH, transactionNotes);
                params.remove(TransferConstants.PARAM_TRANSACTION_NOTES);
                params.put(TransferConstants.PARAM_TRANSACTION_NOTES, transactionNotes);
                alert.prepareError("transactionNotes : " + transactionNotes).log();
            }
		} catch (Exception e) {
			alert.prepareError("Exception Occured in Standing Order Pre Processor:" + e).log();
			return false;
		}
		return true;
	}
}

package com.infinity.dbx.temenos.transfers;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.MultiValueUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class Psd2StandingOrderPreProcessor extends TemenosBasePreProcessor {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			super.execute(params, request, response, result);
			String frequencyEndDate;
			TransferUtils.setPaymentDetails(params, request);
			TransferUtils.convertFrequencyToT24Type(params);
			String noOfRecurrences = CommonUtils.getParamValue(params, TransferConstants.PARAM_NUMBER_OF_RECURRENCES);
			if (StringUtils.isNotBlank(noOfRecurrences)) {
				frequencyEndDate = TransferUtils.getEndDateBasedOnFrequency(params, noOfRecurrences);
				params.put(Constants.PARAM_FREQUENCY_END_DATE, CommonUtils.convertDateToYYYYMMDD(frequencyEndDate));
			} else {
				frequencyEndDate = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_END_DATE);
				params.put(Constants.PARAM_FREQUENCY_END_DATE, CommonUtils.convertDateToYYYYMMDD(frequencyEndDate));
			}
			
			// Handle description multi value in T24
            String transactionNotes = CommonUtils.getParamValue(params, TransferConstants.PARAM_TRANSACTION_NOTES);
            if (StringUtils.isNotBlank(transactionNotes)) {
                transactionNotes = MultiValueUtil.splitMultiValue(TransferConstants.PARAM_STO_DISPLAYNAME,
                        TransferConstants.PARAM_STO_DISPLAYNAME_LENGTH, transactionNotes);
                params.remove(TransferConstants.PARAM_TRANSACTION_NOTES);
                params.put(TransferConstants.PARAM_TRANSACTION_NOTES, transactionNotes);
                alert.prepareError("transactionNotes : " + transactionNotes).log();
            }
		} catch (Exception e) {
			alert.prepareError("Exception Occured in Standing Order Pre Processor:"+e).log();
			return false;
		}
		return true;
	}
}

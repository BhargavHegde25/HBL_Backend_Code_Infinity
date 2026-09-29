package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateStopPaymentPreProcessor extends TemenosBasePreProcessor implements StopPaymentConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
    @Override
	public boolean execute(@SuppressWarnings("rawtypes") HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		super.execute(params, request, response, result);
		String transactionType = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_TRANSACTION_TYPE);
		String checkNumber =  CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_NUMBER);
        String amount =  CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_AMOUNT);  
		
		params.put(Constants.PARAM_TRANSACTION_TYPE, PARAM_TRANSACTIONTYPE_ONLINE); 
		transactionType = PARAM_TRANSACTIONTYPE_ONLINE;
        String validateRequest = CommonUtils.getParamValue(params, StopPaymentConstants.PARAM_CHEQUE_ISSUE_VALIDATE);
		if (StringUtils.isNotBlank(validateRequest) && validateRequest.equalsIgnoreCase("true")) {
            request.addRequestParam_(StopPaymentConstants.PARAM_VALIDATE_ONLY, validateRequest);
        } 

		
		if (transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_ONLINE)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_PHONE)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_BANK)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_MAIL)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_BATCH_FEED)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_LETTER)
				|| transactionType.equalsIgnoreCase(PARAM_TRANSACTIONTYPE_BRANCH)) {
			
			
			//Set Transaction Type Id
            if(checkNumber != null && !checkNumber.isEmpty()){
                if(amount !=null && !amount.isEmpty()){
                    params.put(StopPaymentConstants.PARAM_STOP_CONDITION_ID, StopPaymentConstants.PARAM_STOP_CHEQUE_AMOUNT);
                }else{
                    params.put(StopPaymentConstants.PARAM_STOP_CONDITION_ID, StopPaymentConstants.PARAM_STOP_CHEQUE_NUMBER);
                }
            }else{
                if(amount !=null && !amount.isEmpty()){
                    params.put(StopPaymentConstants.PARAM_STOP_CONDITION_ID, StopPaymentConstants.PARAM_STOP_AMOUNT); 
                }
            }
			
			return Boolean.TRUE;
		}
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		alert.prepareError("Error Occured while setting parameters in create stop payments preprocessor").log();
		return Boolean.FALSE; 
	}
}
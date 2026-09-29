package com.temenos.infinity.api.stoppayments;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.T24ErrorAndOverrideHandling;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class CreateChequeBookRequestsPostProcessor extends TemenosBasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        try {
            result = super.execute(result, request, response);
            
            T24ErrorAndOverrideHandling errorHandlingutil = T24ErrorAndOverrideHandling.getInstance();
            if (errorHandlingutil.isErrorResult(result))
                return result;

//            String status = result.getParamValueByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS);
//            String chequeIssueId = result.getParamValueByName(StopPaymentConstants.PARAM_CHEQUE_ISSUE_ID);
            String chequeStatus = result.getParamValueByName(StopPaymentConstants.PARAM_CHEQUE_STATUS);
//            Record error = result.getRecordById(StopPaymentConstants.PARAM_ERROR);
//            alert.prepareError("ChequeIssueId : " + chequeIssueId).log();
//            alert.prepareError("Status : " + status).log();
//            alert.prepareError("Body : " + ResultToJSON.convert(result)).log();
//            String errType = error.getParamValueByName(StopPaymentConstants.PARAM_TYPE);
//            if ((StringUtils.isNotEmpty(status) && StopPaymentConstants.STATUS_FAILED.equalsIgnoreCase(status))
//                    || StringUtils.isNotBlank(errType)) {
//                Dataset errorDetails = error.getDatasetById(StopPaymentConstants.PARAM_ERROR_DETAILS);
//                List<Record> errorRecord = errorDetails != null ? errorDetails.getAllRecords() : null;
//                for (Record iError : errorRecord) {
//                    alert.prepareError("T24 Error Code : " + CommonUtils.getParamValue(iError, StopPaymentConstants.PARAM_ERROR_CODE)).log();
//                    alert.prepareError("T24 Error Message : " + CommonUtils.getParamValue(iError, StopPaymentConstants.PARAM_ERR_MESSAGE)).log();
//                }
//                Result res = new Result();
//                CommonUtils.setErrCode(res, CommonUtils.getParamValue(errorDetails.getRecord(0), StopPaymentConstants.PARAM_ERROR_CODE));
//                CommonUtils.setErrMsg(res, CommonUtils.getParamValue(errorDetails.getRecord(0), StopPaymentConstants.PARAM_ERR_MESSAGE));
//                return res;
//            }            
            

            Dataset chargeAmountRecordDS = result.getDatasetById(StopPaymentConstants.PARAM_CHARGE_DETAILS);
            List<Record> chargeAmountList = chargeAmountRecordDS != null ? chargeAmountRecordDS.getAllRecords() : null;
            String charges = result.getParamValueByName(StopPaymentConstants.PARAM_CHARGES);
            Double Amount = 0.0;
            if (chargeAmountList != null) {
                for (Record ichargeAmount : chargeAmountList) {
                    String chargeAmount = CommonUtils.getParamValue(ichargeAmount,
                            StopPaymentConstants.PARAM_CHARGE_AMOUNT);
                    Amount = Amount + Double.parseDouble(chargeAmount);
                }
                if (StringUtils.isNotBlank(charges)) {
                    Amount = Amount + Double.parseDouble(charges);
                }
            }
            result.addStringParam(StopPaymentConstants.PARAM_FEES, Double.toString(Amount));
            //Removing status in result to add and overwrite with appropriate status in the switch case below
            result.removeParamByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS);
			switch (chequeStatus) {
			case "60": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_60);
				break;
			}
			case "61": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_61);
				break;
			}
			case "65": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_65);
				break;
			}
			case "69": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_69);
				break;
			}
			case "70": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_70);
				break;
			}
			case "71": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_71);
				break;
			}
			case "75": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_75);
				break;
			}
			case "80": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_80);
				break;
			}
			case "85": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_85);
				break;
			}
			case "90": {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_90);
				break;
			}
			default: {
				result.addStringParam(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS,
						StopPaymentConstants.CHEQUE_STATUS_FAILED);
				break;
			}
			}
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while creating cheque book request post processor:" + e).log();
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }

}

package com.infinity.dbx.temenos.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.temenos.TemenosBaseService;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class RevokeStopChequePayments extends TemenosBaseService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        try {
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];

            if (params == null) {
                CommonUtils.setErrMsg(result, "No input parameters provided");
                CommonUtils.setOpStatusError(result);
                return result;
            }

            String paymentModule = CommonUtils.getServerEnvironmentProperty(StopPaymentConstants.STOP_PAYMENT_MODULE,
                    request);
            String operationName = null;
            HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
            String serviceName = StopPaymentConstants.T24_SERVICE_NAME_STOP_PAYMENTS;
            if (StringUtils.isNotBlank(paymentModule)
                    && StopPaymentConstants.TRANSACTION_STOP_MODULE.equalsIgnoreCase(paymentModule)) {
            	Result errorResult = new Result();
                alert.prepareError("Revoke operation not available for TZ application.").log();
                CommonUtils.setOpStatusError(errorResult);
                CommonUtils.setErrMsg(errorResult, "Revoke operation not available for TZ application.");
                return errorResult;
            } else {
                if (StringUtils.isNotBlank(paymentModule)
                        && StopPaymentConstants.PAYMENT_STOP_MODULE.equalsIgnoreCase(paymentModule)) {
                    operationName = StopPaymentConstants.REVOKE_PAYMENT_STOP;
                    result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
                            operationName, true);
                }
                String status = result.getParamValueByName(StopPaymentConstants.PARAM_CHEQUE_SUPPLEMENT_STATUS);
                String errMessage = result.getParamValueByName(StopPaymentConstants.PARAM_ERROR_MESSAGE);
                String errCode = result.getParamValueByName(StopPaymentConstants.PARAM_ERR_CODE);

                // Process Error Message
                if ((StringUtils.isNotEmpty(status)
                        && StopPaymentConstants.STATUS_FAILED.equalsIgnoreCase(status))
                        || StringUtils.isNotBlank(errMessage)) {
                    alert.prepareError("T24 Error Message : " + errMessage).log();
                    Result res = new Result();
                    CommonUtils.setErrCode(res, errCode);
                    CommonUtils.setErrMsg(res, errMessage); 
                    res.addOpstatusParam(status);
                    return res;
                }
            }
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while revoking stop cheque payment:" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }
}
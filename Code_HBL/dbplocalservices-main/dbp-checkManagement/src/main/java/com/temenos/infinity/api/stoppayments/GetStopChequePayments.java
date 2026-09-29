package com.temenos.infinity.api.stoppayments;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

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
public class GetStopChequePayments extends TemenosBaseService {

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
                operationName = StopPaymentConstants.GET_TRANSACTION_STOP;
                result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
                        true); 
            } else {
                if (StringUtils.isNotBlank(paymentModule)
                        && StopPaymentConstants.PAYMENT_STOP_MODULE.equalsIgnoreCase(paymentModule)) {
                    request.addRequestParam_(StopPaymentConstants.PARAM_FILE_VERSION, StopPaymentConstants.PARAM_FILE_VERSION_VALUE);
                    operationName = StopPaymentConstants.GET_PAYMENT_STOP;
                    result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
                            operationName, true);
                } 
            }
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while creating stop cheque payment:" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }
}
package com.infinity.dbx.temenos.fileutil;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.transfers.TransferConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetDownloadPDFDetails extends TemenosBaseService {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        try {

            Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
            HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
            String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
            String transactionId = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_ID);

            if (params == null || transactionId == null) {
                CommonUtils.setErrMsg(localResult, "No input parameters provided");
                CommonUtils.setOpStatusError(localResult);
                return localResult;
            }

            HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
            String serviceName = TransferConstants.T24_SERVICE_NAME_TRANSACTION;
            String operationName = null;
            request.addRequestParam_(TransferConstants.PARAM_TRANSACTION_ID, transactionId);
            String srmsFlag = CommonUtils.getServerEnvironmentProperty(TransferConstants.PARAM_SRMS_TRANSACTIONS,
                    request);

            if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(transactionType)) {
                // If SRMS deployed get payment info from SRMS
                if (srmsFlag.equalsIgnoreCase("SRMS")) {
                    String srmsSserviceName = TransferConstants.SRMS_SERVICE_NAME;
                    String srmsOperationName = TransferConstants.SRMS_OPERATION_NAME;
                    result = CommonUtils.callIntegrationService(request, params, serviceHeaders, srmsSserviceName,
                            srmsOperationName, false);
                } else {
                operationName = TransferConstants.PAYMENTORDER_DETAILS_OPERATION_NAME;
                result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
                        false);
                }
            } else {
                operationName = TransferConstants.STANDINGORDER_DETAILS_OPERATION_NAME;
                result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
                        false);
            }
            
        } catch (Exception e) {
            Result errorResult = new Result();
            alert.prepareError("Exception Occured while DownloadPDF" + e).log();
            CommonUtils.setOpStatusError(result);
            CommonUtils.setErrMsg(errorResult, e.getMessage());
            return errorResult;
        }
        return result;
    }

}

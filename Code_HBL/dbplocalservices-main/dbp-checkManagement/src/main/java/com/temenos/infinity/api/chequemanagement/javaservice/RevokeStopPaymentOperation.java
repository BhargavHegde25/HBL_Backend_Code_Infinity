package com.temenos.infinity.api.chequemanagement.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.chequemanagement.resource.api.RevokeStopPaymentResource;

/**
 * 
 * @author smugesh
 * @version Java Service to create the stop payment request in order management
 *          micro services
 * 
 */
public class RevokeStopPaymentOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            RevokeStopPaymentResource revokeStopPaymentResource = DBPAPIAbstractFactoryImpl
                    .getResource(RevokeStopPaymentResource.class);
            StopPayment stopPaymentDTO = constructPayload(request);

            Result result = revokeStopPaymentResource.revokeStopPayment(stopPaymentDTO, request);
            return result;
        } catch (Exception e) {
            alert.prepareError("Unable to create order : " + e).log();
            return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
        }

    }

    public static StopPayment constructPayload(DataControllerRequest request) {

        StopPayment stopPaymentInput = new StopPayment();

        // Get input params from request object
        String fromAccountNumber =  request.getParameter("fromAccountNumber") != null ? request.getParameter("fromAccountNumber") : "";
        String validate = request.getParameter("validate") != null ? request.getParameter("validate") : "";
        String checkNumber1 = request.getParameter("checkNumber1") != null ? request.getParameter("checkNumber1") : "";
        String payeeName = request.getParameter("payeeName") != null ? request.getParameter("payeeName") : "";
        String revokeDate = request.getParameter("revokeDate") != null ? request.getParameter("revokeDate") : "";
        String revokeChequeTypeId = request.getParameter("revokeChequeTypeId") != null ? request.getParameter("revokeChequeTypeId") : "";
        String isRevoke = request.getParameter("isRevoke") != null ? request.getParameter("isRevoke") : "";
        String amount = request.getParameter("amount") != null ? request.getParameter("amount") : "";

        stopPaymentInput.setFromAccountNumber(fromAccountNumber);
        stopPaymentInput.setValidate(validate);
        stopPaymentInput.setCheckNumber1(checkNumber1);
        stopPaymentInput.setPayeeName(payeeName);
        stopPaymentInput.setRevokeDate(revokeDate);
        stopPaymentInput.setRevokeChequeTypeId(revokeChequeTypeId);
        stopPaymentInput.setIsRevoke(isRevoke);
        stopPaymentInput.setAmount(amount);
        return stopPaymentInput;
    }

}

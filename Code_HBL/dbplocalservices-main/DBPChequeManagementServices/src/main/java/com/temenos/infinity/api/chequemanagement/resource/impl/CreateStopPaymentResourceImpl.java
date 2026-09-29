package com.temenos.infinity.api.chequemanagement.resource.impl;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.CreateStopPaymentBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.chequemanagement.resource.api.CreateStopPaymentResource;
import com.temenos.infinity.api.chequemanagement.utils.AccountUtilities;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementUtilities;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class CreateStopPaymentResourceImpl implements CreateStopPaymentResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result createStopPayment(StopPayment stopPaymentDTO, DataControllerRequest request) {
        Result result = new Result();
        AccountUtilities ac = new AccountUtilities();
        StopPayment stopPaymentOrder = new StopPayment();

        String validate = stopPaymentDTO.getValidate();
        String accountID = stopPaymentDTO.getFromAccountNumber();
        // For validate request , send back the dummy response
        if (StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true")) {

            if (StringUtils.isNotBlank(accountID)) {
                
                String customerId = "";
                try {
                    customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                            .get(ChequeManagementConstants.PARAM_CUSTOMER_ID);
                } catch (Exception e) {
                    alert.prepareError("Unable to fetch the customer id from session" + e).log();
                }

                if (StringUtils.isBlank(customerId))
                    return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

                if (!ac.validateInternalAccount(customerId, accountID)) {
                    return ErrorCodeEnum.ERR_26008.setErrorCode(new Result());
                }
                
                if (accountID.contains("-")) {
                    accountID = ChequeManagementUtilities.RemoveCompanyId(accountID);
                    stopPaymentDTO.setFromAccountNumber(accountID); 
                }
                
                try {

                    CreateStopPaymentBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(CreateStopPaymentBusinessDelegate.class);
                    stopPaymentOrder = orderBusinessDelegate.validateStopPayment(stopPaymentDTO, request);
                    if (StringUtils.isBlank(stopPaymentOrder.getReferenceId())) {
                        String dbpErrMessage = stopPaymentOrder.getMessage();
                        if (StringUtils.isNotBlank(dbpErrMessage)) {
                            String msg = ErrorCodeEnum.ERR_26019.getMessage(dbpErrMessage);
                            return ErrorCodeEnum.ERR_26019.setErrorCode(new Result(), msg);
                        } 
                        return ErrorCodeEnum.ERR_26018.setErrorCode(new Result()); 
                    }
                    JSONObject stopPaymentOrderDTO = new JSONObject(stopPaymentOrder);
                    result = JSONToResult.convert(stopPaymentOrderDTO.toString());

                } catch (Exception e) {
                    alert.prepareError(e.toString()).log();
                    diagnostic.prepareDebug("Failed to fetch create stop payment request in OMS " + e).log();
                    return ErrorCodeEnum.ERR_26012.setErrorCode(new Result());
                }
                return result;
            }
        }

        try {

            CreateStopPaymentBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CreateStopPaymentBusinessDelegate.class);
            stopPaymentOrder = orderBusinessDelegate.createStopPayment(stopPaymentDTO, request);
            if (StringUtils.isBlank(stopPaymentOrder.getReferenceId())) {
            	if (StringUtils.isNotBlank(stopPaymentOrder.getMessage())) { 
					String msg = ErrorCodeEnum.ERR_26010.getMessage(stopPaymentOrder.getMessage());
					return ErrorCodeEnum.ERR_26010.setErrorCode(new Result(), msg);
				}
				String msg = ErrorCodeEnum.ERR_26010.getMessage("");
				return ErrorCodeEnum.ERR_26010.setErrorCode(new Result(),msg);
            }
            JSONObject stopPaymentOrderDTO = new JSONObject(stopPaymentOrder);
            result = JSONToResult.convert(stopPaymentOrderDTO.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch create stop payment request in OMS " + e).log();
            return ErrorCodeEnum.ERR_26012.setErrorCode(new Result());
        }
        return result; 
    }

}

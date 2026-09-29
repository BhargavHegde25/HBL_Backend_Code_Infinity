package com.kony.adminconsole.service.card;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * Service to get the Summary of the Requests and Notifications raised by a customer
 * 
 * @author Aditya Mankal
 * 
 * 
 */
public class CardSummaryGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {
        try {
            Result processedResult = new Result();
            String customerId = requestInstance.getParameter("Customer_id");
            String username = requestInstance.getParameter("customerUsername");

            if (StringUtils.isBlank(customerId)) {
                if (StringUtils.isBlank(username)) {
                    ErrorCodeEnum.ERR_20612.setErrorCode(processedResult);
                    return processedResult;
                }

                customerId = CustomerHandler.getCustomerId(username, requestInstance);
            }

            if (methodID.equalsIgnoreCase("getCustomerCardRequestNotificationSummary"))
                return CustomerHandler.getCustomerRequestNotificationCount(requestInstance, customerId,
                        processedResult);
            return null;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }

    }

}
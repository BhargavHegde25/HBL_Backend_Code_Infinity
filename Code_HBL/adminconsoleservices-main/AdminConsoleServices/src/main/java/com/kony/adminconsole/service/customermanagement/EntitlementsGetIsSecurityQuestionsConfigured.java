package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EntitlementsGetIsSecurityQuestionsConfigured implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String INPUT_CUSTOMER_ID = "customerId";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        String customerId = requestInstance.getParameter(INPUT_CUSTOMER_ID);
        String authToken = CommonUtilities.getAuthToken(requestInstance);
        CustomerHandler customerHandler = new CustomerHandler();

        if (StringUtils.isBlank(customerId)) {
            ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
            return processedResult;
        }

        try {
            // Fetch security questions information
            JSONObject securityQuestionsResponse = customerHandler.getSecurityQuestionsForGivenCustomer(customerId,
                    authToken, requestInstance);
            if (securityQuestionsResponse == null || !securityQuestionsResponse.has(FabricConstants.OPSTATUS)
                    || securityQuestionsResponse.getInt(FabricConstants.OPSTATUS) != 0
                    || !securityQuestionsResponse.has("customersecurityquestion_view")) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20246);
            }
            JSONArray securityQuestionsForCustomer = (JSONArray) securityQuestionsResponse
                    .get("customersecurityquestion_view");
            Boolean isSecurityQuestionConfigured = false;
            if (securityQuestionsForCustomer.length() != 0) {
                isSecurityQuestionConfigured = true;
            }
            processedResult.addParam(new Param("isSecurityQuestionConfigured",
                    String.valueOf(isSecurityQuestionConfigured), FabricConstants.STRING));
            return processedResult;

        } catch (ApplicationException applicationException) {
            applicationException.getErrorCodeEnum().setErrorCode(processedResult);
            alert.prepareError("Exception occurred while processing customer entitlements service ", applicationException).log();
        } catch (Exception e) {
            alert.prepareError("Exception occurred while processing customer entitlements service ", e).log();
            ErrorCodeEnum.ERR_20983.setErrorCode(processedResult);
        }
        return processedResult;
    }

}

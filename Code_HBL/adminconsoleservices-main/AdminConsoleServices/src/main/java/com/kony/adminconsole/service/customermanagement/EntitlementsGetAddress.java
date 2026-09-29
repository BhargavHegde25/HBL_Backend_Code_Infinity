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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class EntitlementsGetAddress implements JavaService2 {

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
            // Fetch address details
            JSONObject readCustomerAddr = customerHandler.readCustomerAddr(authToken, customerId, requestInstance);
            if (readCustomerAddr == null || !readCustomerAddr.has(FabricConstants.OPSTATUS)
                    || readCustomerAddr.getInt(FabricConstants.OPSTATUS) != 0
                    || !readCustomerAddr.has("customeraddress_view")) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20881);
            }
            JSONArray addressDetails = (JSONArray) readCustomerAddr.get("customeraddress_view");
            Dataset AddressDetailsArray = CommonUtilities.constructDatasetFromJSONArray(addressDetails);
            AddressDetailsArray.setId("Addresses");
            processedResult.addDataset(AddressDetailsArray);

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

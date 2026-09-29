package com.kony.adminconsole.service.customermanagement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * fetch all the entitlements assigned to the customer - directly or through groups and also their respective limits and
 * fees
 * 
 * @author Sowmya Mortha, Alahari Akhil
 *
 */
public class CustomerEntitlementsLimitsAndFees {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    public Result fetchCustomerEntitlements(String customerId, String systemUser, String authToken,
            DataControllerRequest requestInstance) {
        Result processedResult = new Result();
        try {
            JSONArray customerEntitlements = CustomerHandler.getCustomerEntitlements(customerId, requestInstance,
                    processedResult);
            if (customerEntitlements == null) {
                return processedResult;
            }
            Dataset customerEntitlementsDataset = CommonUtilities.constructDatasetFromJSONArray(customerEntitlements);
            customerEntitlementsDataset.setId("services");

            processedResult.addDataset(customerEntitlementsDataset);
        } catch (Exception e) {
            alert.prepareError("Error in fetching services entitled for customers", e).log();
            ErrorCodeEnum.ERR_20983.setErrorCode(processedResult);
        }
        return processedResult;
    }

}
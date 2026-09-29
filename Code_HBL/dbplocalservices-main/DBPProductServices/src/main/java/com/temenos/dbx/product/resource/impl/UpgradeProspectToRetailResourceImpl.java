package com.temenos.dbx.product.resource.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.UpgradeProspectToRetailCustomerBusinessDelegate;
import com.temenos.dbx.product.resource.api.UpgradeProspectToRetailResource;

public class UpgradeProspectToRetailResourceImpl implements UpgradeProspectToRetailResource {

    @Override
    public Result upgrade(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {

            UpgradeProspectToRetailCustomerBusinessDelegate upgradeProspect = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(UpgradeProspectToRetailCustomerBusinessDelegate.class);
            JsonObject object = upgradeProspect.upgrade(inputParams.get("Customer_id"), URLConstants.CUSTOMER_UPDATE,
                    HelperMethods.getHeaders(dcRequest));
            if (!JSONUtil.hasKey(object, DBPUtilitiesConstants.VALIDATION_ERROR) && JSONUtil.hasKey(object, "customer")
                    && object.get("customer").isJsonArray() && object.get("customer").getAsJsonArray().size() > 0) {
                result.addStringParam("success", "success");
            } else {
                ErrorCodeEnum.ERR_10629.setErrorCode(result);
            }
        }

        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {

        Map<String, String> userDetails = HelperMethods.getUserFromIdentityService(dcRequest);

        if (!(userDetails.containsKey("customerTypeId")
                && "DBP_API_USER".equalsIgnoreCase(userDetails.get("customerTypeId")))) {

            ErrorCodeEnum.ERR_10627.setErrorCode(result);
            return false;
        }

        String customerId = inputParams.get("Customer_id");

        if (StringUtils.isBlank(customerId)) {
            // Invalid input
            ErrorCodeEnum.ERR_10628.setErrorCode(result);
            return false;
        }

        return true;
    }

}

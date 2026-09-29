package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.DTOConstants;

public class CorePreProcessor implements DataPreProcessor2 {

    private Alert alert;

    @Override
    public boolean execute(HashMap inputParams, DataControllerRequest dcRequest, DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();

        String partyEventData = (String) inputParams.get("partyEventData");
        if(StringUtils.isBlank(partyEventData)) {
            partyEventData = dcRequest.getParameter("partyEventData");
        }
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        String partyID = null;

        if(StringUtils.isNotBlank(partyEventData)) {
            JsonObject partyEventDataJson =  new JsonParser().parse(partyEventData).getAsJsonObject();
            if(partyEventDataJson.has(DTOConstants.PARTY_ID) && !partyEventDataJson.get(DTOConstants.PARTY_ID).isJsonNull()) {
                partyID = partyEventDataJson.get(DTOConstants.PARTY_ID).getAsString();
            }
        }


        String filter = "BackendId" +DBPUtilitiesConstants.EQUAL + partyID + DBPUtilitiesConstants.AND + "BackendType" + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;

        try {
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
        } catch (HttpCallException e) {

            alert.prepareError("Caught exception while creating Party: ", e).log();
        }

        if(HelperMethods.hasRecords(result)) {

            partyID = HelperMethods.getFieldValue(result, "BackendId");


            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
            } catch (HttpCallException e) {

                alert.prepareError("Caught exception while creating Party: ", e).log();
            }

            if(HelperMethods.hasRecords(result)) {

                String customerID = HelperMethods.getFieldValue(result, "Customer_id");
                filter = "Customer_id" +DBPUtilitiesConstants.EQUAL + customerID + DBPUtilitiesConstants.AND + "BackendType" + DBPUtilitiesConstants.EQUAL + DTOConstants.PARTY;

                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.BACKENDIDENTIFIER_GET);
                } catch (HttpCallException e) {
                    alert.prepareError("Caught exception while creating Party: ", e).log();
                }

                if(HelperMethods.hasRecords(result)) {
                    inputParams.put("customerId", HelperMethods.getFieldValue(result, "BackendId"));
                    return true;
                }
            }
        }

        result = new Result();
        ErrorCodeEnum.ERR_10209.setErrorCode(result);
        
        return false;
    }


}

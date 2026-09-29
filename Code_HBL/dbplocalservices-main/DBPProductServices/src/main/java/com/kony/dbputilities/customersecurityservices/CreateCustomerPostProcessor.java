package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.DTOConstants;

public class CreateCustomerPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        Result retValue = new Result();
        Record dbxUsrAttr = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
        Record coreUsrAttr = result.getRecordById(DBPUtilitiesConstants.CORE_ATTR);
        if (HelperMethods.hasError(result)) {
            if(result.getNameOfAllParams().contains(ErrorCodeEnum.ERROR_MESSAGE_KEY)) {
                ErrorCodeEnum.ERR_10082.setErrorCode(retValue,
                        result.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY).getValue());
            }
            else {
                ErrorCodeEnum.ERR_10082.setErrorCode(retValue,
                        result.getParamByName(DBPUtilitiesConstants.VALIDATION_ERROR).getValue());
            }
        }else if (dbxUsrAttr != null && dbxUsrAttr.getAllParams().size() > 0) {
            for (Param param : dbxUsrAttr.getAllParams()) {
                retValue.addParam(param);
            }
        } else if(coreUsrAttr != null){
            for (Param param : coreUsrAttr.getAllParams()) {
                retValue.addParam(param);
            }
        }

        Map<String, String> input = new HashMap<String, String>();
        String customerId = result.getParamValueByName("id");
        if(StringUtils.isBlank(customerId)) {
            customerId = result.getParamValueByName("Customer_id");
        }

        if(StringUtils.isBlank(customerId) && dbxUsrAttr != null) {
            customerId =  dbxUsrAttr.getParamValueByName("id");
        }


        String coreCustomerId = result.getParamValueByName("partyID");

        if(StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(coreCustomerId)) {
            input.put("id", UUID.randomUUID().toString());
            input.put("Customer_id", customerId);
            input.put("BackendId", coreCustomerId);
            input.put("BackendType", DTOConstants.PARTY);

            try {
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.BACKENDIDENTIFIER_CREATE);
            } catch (HttpCallException e) {
                alert.prepareError(e.toString()).log();
            }
        }

        return retValue;
    }
}
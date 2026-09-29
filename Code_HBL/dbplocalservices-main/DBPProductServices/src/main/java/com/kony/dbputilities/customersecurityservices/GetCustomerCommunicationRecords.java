package com.kony.dbputilities.customersecurityservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerCommunicationRecords implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Result result = new Result();
        Result returnResult = new Result();

        if (preProcess(inputParams)) {
            try {
                result = HelperMethods.callGetApi(dcRequest, inputParams.get(DBPUtilitiesConstants.FILTER),
                        HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMER_COMMUNICATION_GET);

            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }

        if (HelperMethods.hasRecords(result)) {

            List<Record> list = result.getAllDatasets().get(0).getAllRecords();
            Dataset phoneRecords = new Dataset();
            Dataset emailRecords = new Dataset();

            phoneRecords.setId("ContactNumbers");
            emailRecords.setId("EmailIds");

            for (Record record : list) {
                if (record.getNameOfAllParams().contains(DBPUtilitiesConstants.TYPE_ID)
                        && record.getParamValueByName(DBPUtilitiesConstants.TYPE_ID)
                                .equals(DBPUtilitiesConstants.COMM_TYPE_EMAIL)) {
                    emailRecords.addRecord(record);
                } else if (record.getNameOfAllParams().contains(DBPUtilitiesConstants.TYPE_ID)
                        && record.getParamValueByName(DBPUtilitiesConstants.TYPE_ID)
                                .equals(DBPUtilitiesConstants.COMM_TYPE_PHONE)) {
                    processMobileNumber(record);
                    phoneRecords.addRecord(record);
                }
            }

            returnResult.addDataset(phoneRecords);
            returnResult.addDataset(emailRecords);
        }

        return returnResult;

    }

    private void processMobileNumber(Record record) {
        String phone = record.getParamValueByName("Value");
        if (StringUtils.isNotBlank(phone)) {
            String[] phoneArray = StringUtils.split(phone, "-");
            if (phoneArray != null && phoneArray.length >= 2) {
                String countryCode = phoneArray[0];
                record.addParam("phoneCountryCode", countryCode, DBPUtilitiesConstants.STRING_TYPE);
                String phoneNumber = phoneArray[1];
                record.addParam("phoneNumber", phoneNumber, DBPUtilitiesConstants.STRING_TYPE);
                if (phoneArray.length == 3) {
                    String extension = phoneArray[2];
                    record.addParam("phoneExtension", extension, DBPUtilitiesConstants.STRING_TYPE);
                }
            } else {
                record.addParam("phoneNumber", phone, DBPUtilitiesConstants.STRING_TYPE);
                record.addParam("phoneCountryCode", "", DBPUtilitiesConstants.STRING_TYPE);
            }
        }

    }

    private boolean preProcess(Map<String, String> inputParams) {
        String customerId = inputParams.get("Customer_id");

        if (StringUtils.isBlank(customerId)) {
            return false;
        }

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;

        String select = "id,Type_id,isPrimary,Value,Extension,isTypeBusiness,Description,phoneCountryCode";

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.SELECT, select);

        return true;
    }

}
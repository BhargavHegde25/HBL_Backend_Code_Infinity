package com.kony.dbputilities.productservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.accountservices.GetAccouts;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserProductList implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        HelperMethods.getInputParamMap(inputArray);
        Result accountResult = null;
        if (preProcess(methodID, inputArray, dcRequest, dcResponse, result)) {
            GetAccouts accountsService = new GetAccouts();
            accountResult = (Result) accountsService.invoke(methodID, inputArray, dcRequest, dcResponse);
            Map<String, String> input = new HashMap<>();
            result = AdminUtil.invokeAPI(input, URLConstants.ADMIN_PRODUCTLIST, dcRequest);
        }

        if (HelperMethods.hasRecords(result)) {
            return postProcess(dcRequest, accountResult, result);
        }

        return result;
    }

    private boolean preProcess(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse, Result result) throws Exception {
        boolean status = true;
        return status;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result accountResult, Result result)
            throws HttpCallException {
        Result retVal = new Result();
        Dataset dataset = new Dataset();

        Map<String, Record> accountMap = new HashMap<>();
        for (Record record : accountResult.getAllDatasets().get(0).getAllRecords()) {
            accountMap.put(HelperMethods.getFieldValue(record, "adminProductId"), record);
        }

        ArrayList<Record> records = new ArrayList<>();
        for (Record record : result.getAllDatasets().get(0).getAllRecords()) {
            if (accountMap.containsKey(HelperMethods.getFieldValue(record, "productId"))) {
                Record account = accountMap.get(HelperMethods.getFieldValue(record, "productId"));
                Param param = new Param("accountID", HelperMethods.getFieldValue(account, "Account_id"),
                        DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                record.addParam(param);
                records.add(record);
            }
        }

        dataset.setId("Product");
        dataset.addAllRecords(records);
        retVal.addDataset(dataset);

        return retVal;
    }
}

package com.kony.adminconsole.service.approvalworkflow.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.approvalworkflow.businessdelegate.api.MigrateDataBusinessDelegate;
import com.kony.adminconsole.service.approvalworkflow.resource.api.MigrateDataResource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class MigrateDataResourceImpl implements MigrateDataResource {

    MigrateDataBusinessDelegate migrateDataBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(MigrateDataBusinessDelegate.class);

    public Result migrateData(String methodId, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response) throws Exception {
        Result result = new Result();
        Map<String, Object> payloadMap = new HashMap<String, Object>();
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        payloadMap.put("requestId", inputParams.get("requestId").toString());
        payloadMap.put("recordId", inputParams.get("recordId"));
        payloadMap.put("status", inputParams.get("status").toString());
        payloadMap.put("expAPIOperationName", inputParams.get("expAPIOperationName").toString());
        payloadMap.put("response", inputParams.get("response").toString());
        JSONObject serviceResponse =
                migrateDataBusinessDelegate.moveDataRecords(payloadMap,request);
        return result;
    }
}
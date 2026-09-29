package com.kony.dbputilities.transservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class VerifyTrialDeposit implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        process(inputParams, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void process(Map inputParams, Result result) {
        String firstDeposit = (String) inputParams.get("firstDeposit");
        String secondDeposit = (String) inputParams.get("secondDeposit");
        Param output = new Param("success", "Failed", DBPUtilitiesConstants.STRING_TYPE);
        if (firstDeposit.split("\\.")[0].equals("0") && secondDeposit.split("\\.")[0].equals("0")) {
            output.setValue("Success");
        }
        result.addParam(output);
    }

    /*
     * @SuppressWarnings("rawtypes") private void postProcess(Map inputParams, Result result) {
     * if(result.getAllDatasets().get(0).getAllRecords().isEmpty()){ HelperMethods.setValidationMsg("Failed", null,
     * result); } String firstDeposit = (String) inputParams.get("firstDeposit"); String secondDeposit = (String)
     * inputParams.get("secondDeposit"); Record transaction1 = result.getAllDatasets().get(0).getRecord(0); Record
     * transaction2 = result.getAllDatasets().get(0).getRecord(1); String amount1 =
     * HelperMethods.getFieldValue(transaction1, "amount"); String amount2 = HelperMethods.getFieldValue(transaction2,
     * "amount"); if(!firstDeposit.equals(amount1) || !secondDeposit.equals(amount2)){
     * HelperMethods.setValidationMsg("Failed", null, result); } }
     * 
     * @SuppressWarnings({ "rawtypes", "unchecked" }) private boolean preProcess(Map inputParams, DataControllerRequest
     * dcRequest, Result result) { String accountNumber = (String) inputParams.get("accountNumber"); String filter =
     * "toExternalAccountNumber" + DBPUtilitiesConstants.EQUAL + accountNumber;
     * inputParams.put(DBPUtilitiesConstants.FILTER, filter); inputParams.put(DBPUtilitiesConstants.ORDERBY,
     * "transactionDate asc"); return true; }
     */
}

package com.kony.dbputilities.accountservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ShowDownloadStatements implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        process(inputParams, dcRequest, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void process(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String format = (String) inputParams.get("format");
        String statementLink = "https://retailbanking1.konycloud.com/dbimages/account-statement-pdf.pdf";
        if ("csv".equalsIgnoreCase(format)) {
            statementLink = "https://retailbanking1.konycloud.com/dbimages/account-statement-csv.csv";
        } else if ("xls".equalsIgnoreCase(format)) {
            statementLink = "https://retailbanking1.konycloud.com/dbimages/account-statement-xls.xls";
        }
        Dataset ds = new Dataset("accountstatement");
        Record accountStmt = new Record();
        Param p = new Param("statementLink", statementLink, DBPUtilitiesConstants.STRING_TYPE);
        ds.addRecord(accountStmt);
        accountStmt.addParam(p);
        result.addDataset(ds);
    }
}

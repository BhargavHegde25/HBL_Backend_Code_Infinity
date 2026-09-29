package com.kony.dbputilities.accountservices;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCreditCardAccounts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        
        Dataset ds = new Dataset();
        ds.setId("Accounts");
        /*
        Record record = new Record();
        record.addParam(new Param("accountID", "1901282"));
        record.addParam(new Param("accountName", "Freedom Credit Card"));
        record.addParam(new Param("accountType", "CreditCard"));
        record.addParam(new Param("typeDescription", "CreditCard"));
        record.addParam(new Param("nickName", "Freedom Credit"));
        record.addParam(new Param("currencyCode", "USD"));
        
        record.addParam(new Param("minimumDue", "276.0"));
        record.addParam(new Param("paymentDue", "3726.0"));
        record.addParam(new Param("outstandingBalance", "619.61"));
        record.addParam(new Param("principalBalance", "10000.00"));
        record.addParam(new Param("dueDate", "2020-11-10T00:00:00"));
        ds.addRecord(record);
       
        record = new Record();
        record.addParam(new Param("accountID", "2901282"));
        record.addParam(new Param("accountName", "HDFC Credit Card"));
        record.addParam(new Param("accountType", "CreditCard"));
        record.addParam(new Param("typeDescription", "CreditCard"));
        record.addParam(new Param("nickName", "HDFC Credit"));
        record.addParam(new Param("currencyCode", "USD"));
        
        record.addParam(new Param("minimumDue", "276.0"));
        record.addParam(new Param("paymentDue", "3726.0"));
        record.addParam(new Param("outstandingBalance", "629.61"));
        record.addParam(new Param("principalBalance", "20000.00"));
        record.addParam(new Param("dueDate", "2020-11-10T00:00:00"));
        ds.addRecord(record);

        record = new Record();
        record.addParam(new Param("accountID", "3901282"));
        record.addParam(new Param("accountName", "Citi Credit Card"));
        record.addParam(new Param("accountType", "CreditCard"));
        record.addParam(new Param("typeDescription", "CreditCard"));
        record.addParam(new Param("nickName", "Citi Credit"));
        record.addParam(new Param("currencyCode", "USD"));
        record.addParam(new Param("Account_id", "3901282"));
        
        record.addParam(new Param("minimumDue", "276.0"));
        record.addParam(new Param("paymentDue", "3726.0"));
        record.addParam(new Param("principalBalance", "10000.00"));
        record.addParam(new Param("outstandingBalance", "629.61"));
        record.addParam(new Param("dueDate", "2020-11-10T00:00:00"));
        ds.addRecord(record);
        */
        result.addDataset(ds);
        return result;
    }
}

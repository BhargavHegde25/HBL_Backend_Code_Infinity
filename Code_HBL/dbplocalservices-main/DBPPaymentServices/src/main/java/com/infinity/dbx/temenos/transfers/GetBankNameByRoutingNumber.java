package com.infinity.dbx.temenos.transfers;


import com.infinity.dbx.temenos.TemenosBaseService;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetBankNameByRoutingNumber extends TemenosBaseService {


    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result result = new Result();
        /*Dataset dataset = new Dataset();
        Record record = new Record();
        record.addStringParam("bankName", "Infinity");
        dataset.addRecord(record);
        dataset.setId("BankNames");
        result.addDataset(dataset);*/
        result.addStringParam("bankName", "Infinity");
        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        return result;
    }
}

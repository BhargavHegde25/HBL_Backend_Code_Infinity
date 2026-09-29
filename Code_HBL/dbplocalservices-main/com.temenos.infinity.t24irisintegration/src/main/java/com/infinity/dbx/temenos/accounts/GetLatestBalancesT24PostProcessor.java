package com.infinity.dbx.temenos.accounts;

import java.util.ArrayList;
import java.util.List;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GetLatestBalancesT24PostProcessor extends BasePostProcessor implements AccountsConstants{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
    	alert.prepareError("T24 complete response : "+response.getResponse()).log();
        Dataset accountTypeDS = result.getDatasetById(DS_ACCOUNTS);
        List<Record> accountTypeRecords = accountTypeDS != null ? accountTypeDS.getAllRecords() : null;
        List<Record> accountFinals = new ArrayList<Record>();
        Result finalResult = new Result();
        if (accountTypeRecords == null || accountTypeRecords.isEmpty()) {
            alert.prepareError("Accounts empty return result").log();
            finalResult.addDataset(new Dataset(DS_ACCOUNTS));
            finalResult.addOpstatusParam(0);
            finalResult.addHttpStatusCodeParam(200);
            return finalResult;
        }

        alert.prepareError("Result " + result.getAllParams().toString()).log();

        for (Record record : accountTypeRecords) {
            List<Record> products = record.getDatasetById(DS_PRODUCTS) != null
                    ? record.getDatasetById(DS_PRODUCTS).getAllRecords()
                    : null;
            for (Record product : products) {
            	accountFinals.add(product);
            }
        }

        result.removeDatasetById(DS_ACCOUNTS);
        Dataset ds = new Dataset(DS_ACCOUNTS);
        ds.addAllRecords(accountFinals);
        finalResult.addDataset(ds);
        finalResult.addOpstatusParam(0);
        finalResult.addHttpStatusCodeParam(200);
        return finalResult;
    }

}

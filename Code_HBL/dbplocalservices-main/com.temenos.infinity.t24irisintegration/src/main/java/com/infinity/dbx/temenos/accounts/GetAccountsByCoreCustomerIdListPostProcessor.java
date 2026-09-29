package com.infinity.dbx.temenos.accounts;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;

public class GetAccountsByCoreCustomerIdListPostProcessor extends BasePostProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        temenosUtils.loadAccountTypeProperties(request);
        if (result != null) {
            if (result.getAllDatasets().size() > 0 && result.getDatasetById("Accounts") != null &&
                    result.getDatasetById("Accounts").getAllRecords().size() > 0) {
                JsonArray jsonarray = new JsonArray();
                for (Record record : result.getDatasetById("Accounts").getAllRecords()) {
                    if (StringUtils.isNotBlank(record.getParamValueByName("accountId"))) {
                        String accountType = record.getParamValueByName("productId");
                        if (temenosUtils.accountTypesMap.containsKey(accountType)) {
                            accountType = temenosUtils.accountTypesMap.get(accountType);
                        } else {
                            accountType = null;
                        }
                        if (StringUtils.isBlank(accountType))
                            continue;
                        if (StringUtils.isBlank(record.getParamValueByName("accountId")))
                            continue;
                        if (StringUtils.isBlank(record.getParamValueByName("customerId")))
                            continue;
                        if (StringUtils.isBlank(record.getParamValueByName("productName")))
                            continue;
                        if (StringUtils.isBlank(record.getParamValueByName("arrangementId")))
                            continue;
                        if (StringUtils.isBlank(record.getParamValueByName("roleDisplayName")))
                            continue;
                        JsonObject json = new JsonObject();
                        json.addProperty("accountId", record.getParamValueByName("accountId"));
                        json.addProperty("customerId", record.getParamValueByName("customerId"));
                        json.addProperty("accountType", accountType);
                        json.addProperty("accountName", record.getParamValueByName("productName"));
                        json.addProperty("arrangementId", record.getParamValueByName("arrangementId"));
                        json.addProperty("roleDisplayName", record.getParamValueByName("roleDisplayName"));
                        jsonarray.add(json);
                    }
                }
                request.addRequestParam_("accounts", jsonarray.toString());
                alert.prepareError("Implicit cif enabled accounts list  :" + jsonarray.toString()).log();
            }
        }
        result = new Result();
        result.addOpstatusParam(0);
        result.addHttpStatusCodeParam(200);
        return result;
    }
}

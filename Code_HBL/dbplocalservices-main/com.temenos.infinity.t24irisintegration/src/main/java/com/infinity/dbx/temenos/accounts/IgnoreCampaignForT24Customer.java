package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class IgnoreCampaignForT24Customer implements JavaService2, AccountsConstants, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        // Headers and params
        HashMap<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        HashMap<String, Object> headerParams = new HashMap<String, Object>();

        Result result = new Result();
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        try {

            String customerId = (String) inputParams.get("customerId");
            String campaignId = (String) inputParams.get("campaignId");
            if (StringUtils.isBlank(customerId))
                customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                        .get("customer_id");

            if (preProcess(result, customerId, campaignId) == null
                    && !StringUtils.equalsAnyIgnoreCase(campaignId, AccountsConstants.DEFAULT_CAMPAIGN)) {

                HashMap<String, Object> input = new HashMap<String, Object>();

                input.put("customer_id", customerId);
                input.put("campaign_id", campaignId);
                input.put("createdby", customerId);

                JSONObject CampaignResponse = temenosUtils.invokeServiceAndGetJson(request, input, headerParams,
                        AccountsConstants.CAMPAIGN_SERVICE, AccountsConstants.CAMPAIGN_OPERATION, null);

                if (CampaignResponse.has("opstatus") && !CampaignResponse.get("opstatus").equals(0)) {
                    result.addParam("dbpErrCode", "12460");
                    result.addParam("dbpErrMsg", "Failed to ignore the campaign");
                    return result;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured in IgnoreCampaignForCustomer JAVA service. Error: ", e).log();
            result.addParam("dbpErrCode", "1200");
            result.addParam("dbpErrMsg", "Internal Service Error");
            return result;
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Result preProcess(Result result, String customerId, String campaignId) {
        if (!StringUtils.isNotBlank(customerId)) {
            result.addParam("dbpErrCode", "12458");
            result.addParam("dbpErrMsg", "Customer ID is a mandatory field");
            return result;
        }

        if (!StringUtils.isNotBlank(campaignId)) {
            result.addParam("dbpErrCode", "12459");
            result.addParam("dbpErrMsg", "Campaign ID is a mandatory field");
            return result;
        }
        return null;
    }
}

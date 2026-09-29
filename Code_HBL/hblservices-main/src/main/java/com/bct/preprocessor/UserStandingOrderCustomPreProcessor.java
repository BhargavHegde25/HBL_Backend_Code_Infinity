package com.bct.preprocessor;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class UserStandingOrderCustomPreProcessor extends TemenosBasePreProcessor{

    private static final Logger LOG = LogManager.getLogger(UserStandingOrderCustomPreProcessor.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean execute(  HashMap params, DataControllerRequest request,
            DataControllerResponse response,
            Result result) throws Exception {

        try {
            LOG.debug("In " + UserStandingOrderCustomPreProcessor.class.getName());

            super.execute(params, request, response, result);

            TemenosUtils temenosUtils = TemenosUtils.getInstance();
            String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
            String defaultAccountId = getUserDefaultAccount(CustomerId, request);
            		LOG.debug("defaultAccountId **"+ defaultAccountId);
			/*
			 * HashMap<String, Account> accounts =
			 * temenosUtils.getAccountsMapFromCache(request); Iterator<Entry<String,
			 * Account>> hmIterator = accounts.entrySet().iterator(); String
			 * spaceSeperatedAccounts = ""; while (hmIterator.hasNext()) {
			 * 
			 * @SuppressWarnings("rawtypes") Map.Entry mapElement =
			 * (Map.Entry)hmIterator.next(); spaceSeperatedAccounts =
			 * spaceSeperatedAccounts+" "+mapElement.getKey().toString(); }
			 */
            params.put(TransactionConstants.ACCID,defaultAccountId);
            params.remove(TransactionConstants.USERID);
            
            

        } catch (Exception e) {
            LOG.error("Exception in " + UserStandingOrderCustomPreProcessor.class.getName(), e);
        }

        return Boolean.TRUE;
    }
    
    public String getUserDefaultAccount(String customerID, DataControllerRequest request) {

		String accountId = "";
		Map<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerID
				+ DBPUtilitiesConstants.AND + "FavouriteStatus" + DBPUtilitiesConstants.EQUAL + "1");

		try {
//			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
//					.withObjectId(null).withOperationId(OperationName.DB_CUSTOMERACCOUNTS_GET)
//					.withRequestParameters(inputParams).build().getResponse();
			
			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					ServiceId.DBPRBLOCALSERVICEDB, OperationName.DB_CUSTOMERACCOUNTS_GET, false);
			LOG.debug("Result***" + ResultToJSON.convert(result));
			Dataset customerDataset = result.getDatasetById("customeraccounts");
			accountId = customerDataset.getRecord(0).getParamValueByName("Account_id");
			
			LOG.debug("Default Account"+ accountId);
		} catch (Exception e) {
			LOG.error("Exception caught while fetching user role", e);
		}
		return accountId;

	}

}

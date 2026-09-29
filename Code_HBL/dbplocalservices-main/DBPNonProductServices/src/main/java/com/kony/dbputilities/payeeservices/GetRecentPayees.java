package com.kony.dbputilities.payeeservices;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetRecentPayees implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
            result = postProcess(result);
        }

        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        StringBuffer filter = new StringBuffer();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayees_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.append(
			 * "select t1.Payee_id, p.name, p.nickName, p.eBillEnable, bm.ebillSupport,p.addressLine1,p.transitDays,p.addressLine2"
			 * + "from (" + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".payee p on (p.id = t1.Payee_id)) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".billermaster bm on (bm.id = p.billermaster_id)");
			 * filter.append(" where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("customer_id") + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("customer_id") + "') ");
			 * filter.append(" ) and t1.Type_id in (select id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype tt where tt.description = 'BillPay') order by t1.createdDate desc limit 50"
			 * );
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayees_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        } else {
			
			 * filter.append(
			 * "select t1.Payee_id, p.name, p.nickName, p.eBillEnable, bm.ebillSupport,p.addressLine1,p.transitDays,p.addressLine2 "
			 * + "from (" + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".payee p on (p.id = t1.Payee_id)) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".billermaster bm on (bm.id = p.billermaster_id)");
			 * filter.append(" where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("customer_id") + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("customer_id") + "') ");
			 * filter.append(" ) and t1.Type_id in (select id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype tt where tt.description = 'BillPay') order by t1.createdDate desc limit 50"
			 * );
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayees_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        }*/
        inputParams.put("transactions_query", filter.toString());
        return true;
    }

    private Result postProcess(Result result) {
        Result retResult = result;
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            Dataset resutds = new Dataset();
            resutds.setId(ds.getId());
            Set<String> payees = new HashSet<>();
            int recordNum = 0;
            int size = ds.getAllRecords().size();
            while (recordNum < size && resutds.getAllRecords().size() <= 5) {
                Record rec = ds.getRecord(recordNum);
                recordNum++;
                String payee = HelperMethods.getParamValue(rec.getParam(DBPUtilitiesConstants.P_PAYEE_ID));

                if (payees.contains(payee)) {
                    continue;
                }
                payees.add(payee);
                resutds.addRecord(rec);
            }
            // result.getAllDatasets().add(0, resutds);
            retResult = new Result();
            retResult.addDataset(resutds);
        }
        return retResult;
    }
}
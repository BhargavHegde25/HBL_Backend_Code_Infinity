package com.kony.dbputilities.paypersonservices;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
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

public class GetRecentPayPerson implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user)) {
            String url = URLConstants.ACCOUNT_TRANSACTION_PROC;
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
        }
        if (!HelperMethods.hasError(result)) {
            if (!HelperMethods.hasRecords(result)) {
                result.addDataset(new Dataset());
            }
            result.getAllDatasets().get(0).setId("accountransactionview");
        }
        if (HelperMethods.hasRecords(result)) {
            result = postProcess(dcRequest, result);
        }
        return result;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        Result retResult = new Result();
        Dataset resutds = new Dataset();
        Set<String> payPerson = new HashSet<>();
        int recordNum = 0;
        Dataset ds = result.getAllDatasets().get(0);
        int size = ds.getAllRecords().size();
        resutds.setId(ds.getId());
        int added = 0;
        String payPersonId = null;
        while (recordNum < size && added <= 5) {
            Record rec = ds.getRecord(recordNum);
            recordNum++;
            payPersonId = HelperMethods.getFieldValue(rec, "Person_Id");
            if (StringUtils.isBlank(payPersonId) || payPerson.contains(payPersonId)) {
                continue;
            }
            Record pp = getPayPerson(dcRequest, payPersonId);
            if (null != pp && isActive(pp)) {
                added++;
                payPerson.add(payPersonId);
                resutds.addRecord(pp);
            }
        }
        retResult.addDataset(resutds);
        return retResult;
    }

    private boolean isActive(Record pp) {
        return "0".equals(HelperMethods.getFieldValue(pp, "isSoftDelete"))
                || "false".equalsIgnoreCase(HelperMethods.getFieldValue(pp, "isSoftDelete"));
    }

    private Record getPayPerson(DataControllerRequest dcRequest, String payPersonId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
        Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYPERSON_GET);
        if (HelperMethods.hasRecords(payPerson)) {
            return payPerson.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String userId = user.get("user_id");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        StringBuffer queryBuf = new StringBuffer();
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayPerson_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * queryBuf.append("select transaction.Person_Id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".transaction");
			 * queryBuf.
			 * append("  where (transaction.FromAccountNumber in (select Account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = " + userId + ")");
			 * queryBuf.append(" or transaction.ToAccountNumber in (select Account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = " + userId + ") ");
			 * queryBuf.append(" ) and transaction.Type_id in (select id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype where description = 'P2P') and transaction.Person_Id is not null order by transaction.createdDate desc limit 50"
			 * );
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayPerson_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        } else {
			
			 * queryBuf.append("select transaction.Person_Id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".transaction");
			 * queryBuf.
			 * append("  where (transaction.FromAccountNumber in (select Account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = " + userId + ")");
			 * queryBuf.append(" or transaction.ToAccountNumber in (select Account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = " + userId + ") ");
			 * queryBuf.append(" ) and transaction.Type_id in (select id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype where description = 'P2P') and transaction.Person_Id is not null order by transaction.createdDate desc limit 50"
			 * );
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentPayPerson_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        }*/
        inputParams.put("transactions_query", queryBuf.toString());
        return true;
    }
}

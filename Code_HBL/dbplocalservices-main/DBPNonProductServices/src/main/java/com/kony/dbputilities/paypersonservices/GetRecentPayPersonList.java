package com.kony.dbputilities.paypersonservices;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetRecentPayPersonList implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            String url = URLConstants.ACCOUNT_TRANSACTION_PROC;
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
        }
        if (!HelperMethods.hasError(result)) {
            result.getAllDatasets().get(0).setId("paypersontransaction");
        } else {
            Dataset ds = new Dataset();
            ds.setId("paypersontransaction");
            result.addDataset(ds);
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
        while (recordNum < size && added <= 6) {
            Record rec = ds.getRecord(recordNum);
            recordNum++;
            payPersonId = HelperMethods.getFieldValue(rec, "Person_Id");
            if (StringUtils.isBlank(payPersonId) || payPerson.contains(payPersonId)) {
                continue;
            }

            if (null != rec) {
                added++;
                payPerson.add(payPersonId);
                resutds.addRecord(rec);
            }
        }
        retResult.addDataset(resutds);
        return retResult;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        if (!StringUtils.isNotBlank(userId)) {
            ErrorCodeEnum.ERR_10179.setErrorCode(result);
            return false;
        }
        String filterQuery = "id" + DBPUtilitiesConstants.EQUAL + userId;
        Result customer = new Result();
        try {
            customer = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_GET);
        } catch (HttpCallException e) {
            
            alert.prepareError(e.getMessage()).log();
        }
        String isP2PActivated = null;
        if (HelperMethods.hasRecords(customer)) {
            isP2PActivated = HelperMethods.getFieldValue(customer, "isP2PActivated");
        }
        if ("false".equalsIgnoreCase(isP2PActivated)) {
            ErrorCodeEnum.ERR_10180.setErrorCode(result);
            return false;
        }
        
        String jdbcUrl=QueryFormer.getDBType(dcRequest);

        // select distinct t.fromAccountNumber,t.Id,t.transactionDate,tt.description as
        // TransactionType,p.*
        // from payperson p inner join transaction t on t.Person_Id=p.id inner join
        // transactiontype tt on t.Type_id = tt.ID
        // where p.User_id='1002496540' and
        // tt.description in ('P2P') order by t.transactionDate desc;
        StringBuilder filter = new StringBuilder();
		/*
		 * filter.append(
		 * "select t.Id,t.fromAccountNumber,t.Person_Id,tt.description as TransactionType,t.transactionDate,p.*  from  "
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".payperson p "
		 * + " inner join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
		 * + ".transaction t " + " on t.Person_Id = p.id " + " inner join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transactiontype tt " + " on t.Type_id = tt.ID "); filter.append(" where ");
		 * filter.append(" p.User_id=" + "'" + userId + "'"); filter.append(" and ");
		 * filter.append(" p.isSoftDelete='0' "); filter.append(" and ");
		 * filter.append(" tt.description in ('P2P')  order by t.transactionDate desc");
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayeePersonTransactionDetails").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));

        inputParams.put("transactions_query", filter.toString());
        return true;
    }
}

package com.kony.dbputilities.paypersonservices;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.SortingUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPayPersonHistory implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        String sortBy = inputParams.get("sortBy");
        String order = inputParams.get("order");

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
            postProcess(dcRequest, inputParams, result);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            SortingUtil util = new SortingUtil(sortBy, order);
            return util.sort(result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Param amountSum = new Param("amountTransferedTillNow", "0", DBPUtilitiesConstants.STRING_TYPE);
        for (Record transaction : transactions) {
            addToTotalSum(transaction, amountSum);
            transaction.addParam(amountSum);
            updateAmount(transaction);
            updateFromAccountDetails(dcRequest, transaction);
            updateDateFormat(transaction);
        }
    }

    private void updateDateFormat(Record transaction) {
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");

        if (StringUtils.isNotBlank(transactionDate)) {
            try {
                HelperMethods.updateDateFormat(transaction, "transactionDate");
            } catch (ParseException e) {

            }
        }
    }

    private void addToTotalSum(Record transaction, Param amountSum) {
        BigDecimal amount = new BigDecimal(HelperMethods.getFieldValue(transaction, "amount"));
        BigDecimal totalAmt = new BigDecimal(amountSum.getValue());
        amountSum.setValue(amount.add(totalAmt).toPlainString());
    }

    private void updateAmount(Record transaction) {
        BigDecimal amount = new BigDecimal(HelperMethods.getFieldValue(transaction, "amount"));
        if (null != HelperMethods.getFieldValue(transaction, "fee") && StringUtils.isNotEmpty(HelperMethods.getFieldValue(transaction, "fee"))) {
            BigDecimal fee = new BigDecimal(HelperMethods.getFieldValue(transaction, "fee"));
            amount = amount.subtract(fee);
        }
        transaction.addParam(new Param("amount", amount.toPlainString(), DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result fromAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(fromAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            String nickName = HelperMethods.getFieldValue(fromAccount, "nickName");
            transaction.addParam(new Param("fromAccountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String userId = user.get("user_id");
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        String payPersonid = (String) inputParams.get("personId");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);

        /*
         * StringBuilder filter = new StringBuilder(); filter.append("Person_id").append(DBPUtilitiesConstants.EQUAL)
         * .append(payPersonid); filter.append(DBPUtilitiesConstants.AND); filter.append(DBPUtilitiesConstants.T_USR_ID)
         * .append(DBPUtilitiesConstants.EQUAL).append(userId); filter.append(DBPUtilitiesConstants.AND);
         * filter.append(DBPUtilitiesConstants.FRM_ACCONT) .append(DBPUtilitiesConstants.NOT_EQ).append(nullValue);
         * filter.append(DBPUtilitiesConstants.AND); filter.append("description").append(DBPUtilitiesConstants.EQUAL)
         * .append("P2P");
         */
        StringBuilder queryBuf = new StringBuilder();
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", payPersonid).replace("?3", userId));
        /*        
         * if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * queryBuf.append("select transaction.* from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left outer join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype on transaction.Type_id=transactiontype.id  where ");
			 * queryBuf.append("transaction.Person_id = " + payPersonid +
			 * " and transactiontype.description='P2P' and transaction.FromAccountNumber in (select Account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id= "); queryBuf.append(userId);
			 * queryBuf.append(")");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", payPersonid).replace("?3", userId));
        } else {
			
			 * queryBuf.append("select transaction.* from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left outer join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype on transaction.Type_id=transactiontype.id  where ");
			 * queryBuf.append("transaction.Person_id = " + payPersonid +
			 * " and transactiontype.description='P2P' and transaction.FromAccountNumber in (select Account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id= "); queryBuf.append(userId); queryBuf.append(")");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", payPersonid).replace("?3", userId));
        }
        */
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
            queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().replace("?1", offset).replace("?2", limit));
        }
        inputParams.put("transactions_query", queryBuf.toString());
        return true;
    }
}

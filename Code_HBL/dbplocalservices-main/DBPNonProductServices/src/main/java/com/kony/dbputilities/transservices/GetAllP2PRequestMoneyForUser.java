package com.kony.dbputilities.transservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAllP2PRequestMoneyForUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        if (!HelperMethods.hasError(result)) {
            if (!HelperMethods.hasRecords(result)) {
                result.addDataset(new Dataset());
            }
            result.getAllDatasets().get(0).setId("accountransactionview");
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updatePayPersonDetails(dcRequest, transaction);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String userId = user.get("user_id");
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);
        String jdbcUrl=QueryFormer.getDBType(dcRequest);

        if (StringUtils.isBlank(sortBy)) {
            sortBy = "createdDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        StringBuilder queryBuf = new StringBuilder();
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAllP2PRequestMoneyForUser_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * queryBuf.
			 * append("select transaction.*,transactiontype.description as transactionType from ("
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) +
			 * ".transactiontype on (transaction.Type_id = transactiontype.Id)) left join "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".payperson on (transaction.Person_Id=payperson.id) where ");
			 * queryBuf.append(
			 * "(transaction.isScheduled = 1 AND transactiontype.description = 'Request' AND transaction.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')) or ");
			 * queryBuf.append(
			 * "(transactiontype.description = 'ReceivedRequest' AND transaction.ToAccountNumber in (select account_id from customeraccounts where Customer_id = '"
			 * + userId + "'))");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAllP2PRequestMoneyForUser_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        } else {
			
			 * queryBuf.
			 * append("select transaction.*,transactiontype.description as transactionType from ("
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) +
			 * ".transactiontype on (transaction.Type_id = transactiontype.Id)) left join "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".payperson on (transaction.Person_Id=payperson.id) where ");
			 * queryBuf.append(
			 * "(transaction.isScheduled = 1 AND transactiontype.description = 'Request' AND transaction.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')) or "); queryBuf.append(
			 * "(transactiontype.description = 'ReceivedRequest' AND transaction.ToAccountNumber in (select account_id from accounts where User_id = '"
			 * + userId + "'))");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAllP2PRequestMoneyForUser_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        }*/
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            queryBuf.append(" order by " + sortBy + " " + order + " ");
        }
        inputParams.put("transactions_query", queryBuf.toString());
        return true;
    }

    private void updatePayPersonDetails(DataControllerRequest dcRequest, Record transaction) {
        try {
            String payPersonId = HelperMethods.getFieldValue(transaction, "Person_Id");
            String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
            Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_GET);
            if (HelperMethods.hasRecords(payPerson)) {
                Record person = payPerson.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("phone", HelperMethods.getFieldValue(person, "phone"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("email", HelperMethods.getFieldValue(person, "email"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("firstName", HelperMethods.getFieldValue(person, "name"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
    }
}

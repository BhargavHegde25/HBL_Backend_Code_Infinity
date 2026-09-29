package com.kony.dbputilities.transservices;

import java.util.HashMap;
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

public class GetReceivedP2PTransactions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
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
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        String accountId = inputParams.get("accountID");
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateAmount(transaction, accountId);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updatePayPersonDetails(dcRequest, transaction);
        }
    }

    private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction,
            Map<String, String> accountTypes) throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
        Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        String typeId = HelperMethods.getFieldValue(toAccount, "Type_id");
        updateAccountType(transaction, typeId, accountTypes);
        String accountName = HelperMethods.getFieldValue(toAccount, "AccountName");
        transaction.addParam(new Param("toAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
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
                transaction.addParam(new Param("secondaryEmail", HelperMethods.getFieldValue(person, "secondaryEmail"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("secondaryEmail2",
                        HelperMethods.getFieldValue(person, "secondaryEmail2"), DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(
                        new Param("secondoryPhoneNumber", HelperMethods.getFieldValue(person, "secondoryPhoneNumber"),
                                DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(
                        new Param("secondaryPhoneNumber2", HelperMethods.getFieldValue(person, "secondaryPhoneNumber2"),
                                DBPUtilitiesConstants.STRING_TYPE));
            }
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private void updateAccountType(Record transaction, String typeId, Map<String, String> accountType) {
        transaction.addParam(new Param("accountType", accountType.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        String filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
        Result accountTypes = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTTYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeID"),
                    HelperMethods.getFieldValue(type, "TypeDescription"));
        }
        return accountTypeMap;
    }

    private void updateAmount(Record transaction, String accountId) {
        String account1 = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(accountId) && accountId.equals(account1)) {
            String amount = HelperMethods.getFieldValue(transaction, "amount");
            transaction.addParam(new Param("amount", "-" + amount, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        inputParams.put("countryCode", user.get("countryCode"));
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "createdDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        StringBuilder filter = new StringBuilder();
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"GetReceivedP2PTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt ");
			 * filter.append("where t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "') ");
			 * filter.append(" and t.Type_id = tt.id and tt.description in ('ReceivedP2P')"
			 * );
			 

        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"GetReceivedP2PTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        } else {
			
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt ");d
			 * filter.append("where t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("user_id") + "') ");
			 * filter.append(" and t.Type_id = tt.id and tt.description in ('ReceivedP2P',)"
			 * );
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"GetReceivedP2PTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        }*/
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"GetReceivedP2PTransactions_ByCustomerId_ORDERBY").getQuery());
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
            filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().
            		replace("?1", offset).replace("?2", limit));
        }
        inputParams.put("transactions_query", filter.toString());
        return true;
    }
}

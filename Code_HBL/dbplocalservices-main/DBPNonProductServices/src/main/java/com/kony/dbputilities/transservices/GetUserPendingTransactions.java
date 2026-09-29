package com.kony.dbputilities.transservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserPendingTransactions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        return ErrorCodeEnum.ERR_99999.setErrorCode(new Result());
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        String searchType = inputParams.get("searchType");
        String accountId = inputParams.get("accountNumber");
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        for (Record transaction : transactions) {
            updateToAccountName(dcRequest, transaction, userId);
            updateFromAccountDetails(dcRequest, transaction, accountTypes);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updatePayeeDetails(dcRequest, transaction);
            updateBillDetails(dcRequest, transaction);
            updatePayPersonDetails(dcRequest, transaction);
            updateCashlessOTPValidDate(transaction);
            updateDateFormat(transaction);
            if ("Search".equalsIgnoreCase(searchType)) {
                updateAmount(accountId, transaction);
            }
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(new Param("scheduledDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyEndDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyStartDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private void updateAmount(String accountId, Record transaction) {
        String frmAccount = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        String description = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isNotBlank(frmAccount) && StringUtils.isNotBlank(accountId)
                && !"InterestCredit".equalsIgnoreCase(description) && accountId.equals(frmAccount)) {
            String amount = "-" + HelperMethods.getFieldValue(transaction, "amount");
            transaction.addParam(new Param("amount", amount, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        boolean status = true;
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        inputParams.put("countryCode", user.get("countryCode"));
        String searchType = (String) inputParams.get("searchType");
        if ("Search".equalsIgnoreCase(searchType)) {
            searchTransactionsForUser(inputParams, dcRequest, result, user);
        } else {
            getTransactionsForUser(inputParams, dcRequest, result, user);
        }

        return status;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void getTransactionsForUser(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);
        String userId = user.get("user_id");
        String isScheduled = (String) inputParams.get(DBPInputConstants.IS_SCHEDULED);
        String firstRecordNum = (String) inputParams.get(DBPUtilitiesConstants.FIRST_RECORD_NUMBER);
        String lastRecordNum = (String) inputParams.get(DBPUtilitiesConstants.LAST_RECORD_NUMBER);
        String statusDescription = DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING;

        if (!StringUtils.isNotBlank(sortBy)) {
            sortBy = "createdDate";
        }
        if (!StringUtils.isNotBlank(order)) {
            order = "desc";
        }
        if (!StringUtils.isNotBlank(isScheduled)) {
            isScheduled = "0";
        }
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        StringBuffer queryBuf = new StringBuffer();
        String query=SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_getTrans_ELSE").getQuery();
        if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
        	query=query.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", statusDescription).replace("?3", userId).replace("?4", sortBy).replace("?5", order);
        	queryBuf.append(query);
        } else {
        	query=query.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", statusDescription).replace("?3", userId).replace("?4", sortBy).replace("?5", order);
        	queryBuf.append(query);
			/*
			 * queryBuf.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * queryBuf.append(" where t1.isScheduled = false");
			 * queryBuf.append(" and t1.StatusDesc = '");
			 * queryBuf.append(statusDescription);
			 * queryBuf.append("' and (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * queryBuf.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "') "); queryBuf.append(
			 * " ) and tt.description not in ('Deposit','Cardless','CheckWithdrawal','Withdrawal','Interest','BillPay','P2P','Loan','Request'))  order by "
			 * + sortBy + " " + order + "");
			 */
        }
        if (StringUtils.isNotBlank(firstRecordNum) && StringUtils.isNotBlank(lastRecordNum)) {
        	
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_getTrans_limit").getQuery().replace("?1", firstRecordNum).replace("?2", lastRecordNum));
        }
        inputParams.put("transactions_query", queryBuf.toString());
    }

    @SuppressWarnings("rawtypes")
    private void searchTransactionsForUser(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String searchMinAmount = (String) inputParams.get("searchMinAmount");
        String searchEndDate = (String) inputParams.get("searchEndDate");
        String searchMaxAmount = (String) inputParams.get("searchMaxAmount");
        String searchStartDate = (String) inputParams.get("searchStartDate");
        String isScheduled2 = (String) inputParams.get("isScheduled");
        String fromCheckNumber = (String) inputParams.get("fromCheckNumber");
        String toCheckNumber = (String) inputParams.get("toCheckNumber");
        String searchType = (String) inputParams.get("searchType");
        String searchDescription = (String) inputParams.get("searchDescription");
        String accountNumber = (String) inputParams.get("accountNumber");
        String lastRecordNum = (String) inputParams.get("lastRecordNumber");
        String firstRecordNum = (String) inputParams.get("firstRecordNumber");
        String searchTransactionType = (String) inputParams.get("searchTransactionType");

        if (StringUtils.isBlank(searchMinAmount)) {
            searchMinAmount = "0";
        }

        if (StringUtils.isBlank(searchMaxAmount)) {
            searchMaxAmount = "2147483647";
        }

        if (StringUtils.isBlank(searchStartDate)) {
            searchStartDate = "1970-01-01";
        }

        if (StringUtils.isBlank(searchEndDate)) {
            searchEndDate = "2100-01-01";
        }

        if (StringUtils.isBlank(isScheduled2)) {
            isScheduled2 = "0";
        }
        if (StringUtils.isBlank(fromCheckNumber)) {
            fromCheckNumber = "-1";
        }
        if (StringUtils.isBlank(toCheckNumber)) {
            toCheckNumber = "2147483647";
        }

        if (StringUtils.isNotBlank(searchDescription)) {
            searchDescription = "'%" + searchDescription + "%'";
        } else {
            searchDescription = "'%'";
        }

        searchEndDate = searchEndDate + "T23:59:59";

        // security fix
        StringBuilder accountQuery = new StringBuilder();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        
        if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
        	String subQuery=SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_accountquery_IF").getQuery();
        	accountQuery.append(subQuery.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")).replace("?3", accountNumber));
        } else {
        	String subQuery=SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_accountquery_ELSE").getQuery();
        	accountQuery.append(subQuery.replace("?1",  URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")).replace("?3", accountNumber));
        }

        StringBuilder filter = new StringBuilder();

        String query=SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans").getQuery();
		filter.append(query.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", searchDescription).replace("?3", searchMinAmount).replace("?4", searchMaxAmount).replace("?5", searchStartDate).replace("?6", searchEndDate).replace("?7", fromCheckNumber).replace("?8", toCheckNumber));

        if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_BOTH.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
                  //  + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");

            
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_DEPOSIT.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.toAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
          
            //filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_WITHDRAWAL.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
          //  filter.append("( t.fromAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
           // filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_P2PCREDITS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.toAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            
            //filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_TRANSFERS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
              //      + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");

            
            filter.append(" and ");
           // filter.append("( tt.description = 'InternalTransfer' or tt.description = 'ExternalTransfer') ");
            filter.append("("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'InternalTransfer'"
            		+ " or " + SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery() + " ='ExternalTransfer')" );
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_CHECKS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
          //  filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
        //    filter.append(" and tt.description = 'CheckWithdrawal') ");
            filter.append("and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'CheckWithdrawal')");
        
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_P2PDEBITS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            
            //filter.append(" and tt.description = 'P2P') ");
            filter.append(" and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'P2P')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_BILLPAY.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            //filter.append(" and tt.description = 'BillPay') ");
            filter.append("and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'BillPay')");
        } else if ("Search".equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
             //       + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");

        } else if ("Search".equalsIgnoreCase(searchType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
             //       + accountQuery.toString() + ") ");
       
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");

        }
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        if (StringUtils.isBlank(sortBy)) {
          // sortBy = "transactionDate";
        	sortBy = SqlQueryEnum.valueOf(jdbcUrl+"_TRANSACTIONDATE").getQuery();
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            filter.append(" order by " + sortBy + " " + order + " ");
        }
        if (StringUtils.isNotBlank(firstRecordNum) && StringUtils.isNotBlank(lastRecordNum)) {
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_limit").getQuery().replace("?1", firstRecordNum).replace("?2", lastRecordNum));
        }
        inputParams.put("transactions_query", filter.toString());
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

    private void updateToAccountName(DataControllerRequest dcRequest, Record transaction, String userId)
            throws HttpCallException {
        String toExtAccountNum = HelperMethods.getFieldValue(transaction, "toExternalAccountNumber");

        String iBAN = HelperMethods.getFieldValue(transaction, DBPInputConstants.IBAN);

        String filter = "";
        if (StringUtils.isNotBlank(toExtAccountNum)) {
            filter = "accountNumber" + DBPUtilitiesConstants.EQUAL + toExtAccountNum;
        } else if (StringUtils.isNotBlank(iBAN)) {
            filter = DBPInputConstants.IBAN + DBPUtilitiesConstants.EQUAL + iBAN;

            if (StringUtils.isNotBlank(userId)) {
                filter += DBPUtilitiesConstants.AND;

                filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
            }
        }

        if (!filter.isEmpty()) {
            Result extAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_GET);
            String toAccountName = HelperMethods.getFieldValue(extAccount, "nickName");
            if (StringUtils.isBlank(toAccountName)) {
                toAccountName = HelperMethods.getFieldValue(extAccount, "beneficiaryName");
            }
            if (StringUtils.isBlank(toAccountName)) {
                toAccountName = HelperMethods.getFieldValue(transaction, "beneficiaryName");
            }
            transaction.addParam(new Param("toAccountName", toAccountName, DBPUtilitiesConstants.STRING_TYPE));
            String isInternationalAccount = HelperMethods.getFieldValue(extAccount, "isInternationalAccount");
            transaction.addParam(
                    new Param("isInternationalAccount", isInternationalAccount, DBPUtilitiesConstants.STRING_TYPE));
            String swiftCode = HelperMethods.getFieldValue(transaction, "swiftCode");
            if (StringUtils.isBlank(swiftCode)) {
                swiftCode = HelperMethods.getFieldValue(extAccount, "swiftCode");
                transaction.addParam(new Param("swiftCode", swiftCode, "String"));
            }

        }
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction,
            Map<String, String> accountTypes) throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String typeId = HelperMethods.getFieldValue(frmAccount, "Type_id");
            transaction.addParam(
                    new Param("fromAccountType", accountTypes.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            String nickName = HelperMethods.getFieldValue(frmAccount, "nickName");
            transaction.addParam(new Param("fromAccountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction,
            Map<String, String> accountTypes) throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
        if (StringUtils.isNotBlank(toAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
            Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String typeId = HelperMethods.getFieldValue(toAccount, "Type_id");
            transaction
                    .addParam(new Param("toAccountType", accountTypes.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
            transaction.addParam(new Param("toAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        transaction.addParam(
                new Param("payeeNickName", getPayeeNickName(dcRequest, payeeId), DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updatePayPersonDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payPersonId = HelperMethods.getFieldValue(transaction, "Person_Id");
        if (StringUtils.isNotBlank(payPersonId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
            Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_GET);
            if (HelperMethods.hasRecords(payPerson)) {
                Record person = payPerson.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("phone", HelperMethods.getFieldValue(person, "phone"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("email", HelperMethods.getFieldValue(person, "email"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("name", HelperMethods.getFieldValue(person, "name"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void updateCashlessOTPValidDate(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            transaction.addParam(
                    new Param("cashlessOTPValidDate", String.valueOf(timeDiff), DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private String getPayeeNickName(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                return HelperMethods.getFieldValue(payee, "nickName");
            }
        }
        return null;
    }

    private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "billid");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
            Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(biller)) {
                Record bill = biller.getAllDatasets().get(0).getRecord(0);
                String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
                String payeeNickName = getPayeeNickName(dcRequest, payeeId);
                transaction.addParam(new Param("payeeNickName", payeeNickName, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

   /* public Result invokeDB(Map<String, String> inputParams, DataControllerRequest dcRequest) throws Exception {
        Result result = null;

        try (Connection con = DBManager.getConnection(dcRequest);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(inputParams.get("query"))) {
            result = postProcess(rs);
        } catch (SQLException e) {

            alert.prepareError(e.getMessage()).log();
        }

        return result;
    }*/

    private Result postProcess(ResultSet rs) throws SQLException {
        Result result = new Result();
        Dataset transactions = new Dataset("accountransactionview");
        Record accnt = null;
        while (rs.next()) {
            accnt = getRecord(rs);
            transactions.addRecord(accnt);
        }
        result.addDataset(transactions);
        return result;
    }

    private Record getRecord(ResultSet rs) throws SQLException {
        Record rec = new Record();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int count = rsMetaData.getColumnCount();
        String col = null;
        for (int i = 1; i <= count; i++) {
            col = rsMetaData.getColumnLabel(i);
            rec.addParam(new Param(col, rs.getString(col), MWConstants.STRING));
        }
        return rec;
    }
}

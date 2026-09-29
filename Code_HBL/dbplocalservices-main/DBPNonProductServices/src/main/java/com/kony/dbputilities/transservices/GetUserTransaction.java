package com.kony.dbputilities.transservices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sortutil.SortRecordByParamValue;
import com.kony.dbputilities.util.DBPInputConstants;
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
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class GetUserTransaction implements JavaService2 {

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
        if (HelperMethods.hasRecords(result) && !HelperMethods.hasError(result)) {
            result.getAllDatasets().get(0).setId("Transactions");
            if (HelperMethods.hasRecords(result)) {
                postProcess(dcRequest, inputParams, result);
            }
        }else {
        	Dataset accountView = new Dataset(); 
        	accountView.setId("Transactions");
        	result.addDataset(accountView); 
        }

        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
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
        }
        if ("toAccountName".equalsIgnoreCase(inputParams.get("sortBy"))
                || "nickName".equalsIgnoreCase(inputParams.get("sortBy"))) {
            List<Record> sortedList = sortBy(transactions, "toAccountName", inputParams.get("order"));
            String id = result.getAllDatasets().get(0).getId();
            Dataset ds = new Dataset(id);
            ds.addAllRecords(sortedList);
            result.addDataset(ds);
        }
    }

    private List<Record> sortBy(List<Record> transactions, String fieldName, String order) {
        boolean asc = true;
        List<Record> mutable = new ArrayList<>();
        mutable.addAll(transactions);
        if (StringUtils.isNotBlank(order) && "desc".equals(order)) {
            asc = false;
        }
        Collections.sort(mutable, new SortRecordByParamValue(fieldName, asc));
        return mutable;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        boolean status = true;
        inputParams.put("countryCode", user.get("countryCode"));
        String searchType = (String) inputParams.get("searchType");

        String userId = user.get("user_id");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + userId);

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
        String statusDescription = DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
        String legalEntityId = (String) customer.get("legalEntityId");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        

        if (StringUtils.isBlank(sortBy)) {
            sortBy = "createdDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        if (!StringUtils.isNotBlank(isScheduled)) {
            isScheduled = "0";
        }
        
        if(isScheduled.equalsIgnoreCase("1"))
            statusDescription = DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING;

        StringBuilder queryBuf = new StringBuilder();
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_IdentifiedByTransactionType_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", statusDescription).replace("?3", userId).replace("?4", isScheduled));
        
		if (StringUtils.isNotBlank(legalEntityId)) {
			queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl + "_legalEntityId").getQuery().replace("?1", legalEntityId));
		}
        
       
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * queryBuf.
			 * append("select t1.*,tt.description as transactionType, IF(p.organizationId or e.organizationId or a.Organization_id, 1, 0) as isBusinessPayee from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".payee p on (p.Id = t1.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".externalaccount e on (e.Id = t1.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts a on (a.Account_id = t1.ToAccountNumber)" );
			 * queryBuf.append(" where "); queryBuf.append(" t1.StatusDesc = '");
			 * queryBuf.append(statusDescription);
			 * queryBuf.append("' and (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')");
			 * queryBuf.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "') "); queryBuf.
			 * append(" ) and tt.description in ('InternalTransfer','ExternalTransfer','P2P')"
			 * );
			 
        	
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_IdentifiedByTransactionType_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", statusDescription).replace("?3", userId));
        } else {
			
			 * queryBuf.
			 * append("select t1.*,tt.description as transactionType, IF(p.organizationId or e.organizationId or a.Organization_id, 1, 0) as isBusinessPayee from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".payee p on (p.Id = t1.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".externalaccount e on (e.Id = t1.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts a on (a.Account_id = t1.ToAccountNumber)" );
			 * queryBuf.append(" where "); queryBuf.append(" t1.StatusDesc = '");
			 * queryBuf.append(statusDescription);
			 * queryBuf.append("' and (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * queryBuf.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "') "); queryBuf.
			 * append(" ) and tt.description in ('InternalTransfer','ExternalTransfer','P2P')"
			 * );
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_IdentifiedByTransactionType_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", statusDescription).replace("?3", userId));
        }*/
        
        if (!"toAccountName".equalsIgnoreCase(sortBy) && !"nickName".equalsIgnoreCase(sortBy)) {
            //queryBuf.append(" order by " + sortBy + " " + order);
            queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_orderBy").getQuery().replace("?1", sortBy).replace("?2", order));
        }
        if (StringUtils.isNotBlank(firstRecordNum) && StringUtils.isNotBlank(lastRecordNum)) {
            queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_Limit").getQuery().replace("?1", firstRecordNum).replace("?2", lastRecordNum));
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
        String statusDescription = DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING;
        String jdbcUrl=QueryFormer.getDBType(dcRequest);

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

        if ("0".equals(isScheduled2)) {
            statusDescription = DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        }
        searchEndDate = searchEndDate + "T23:59:59";

        // security fix
        StringBuilder accountQuery = new StringBuilder();
        StringBuilder filter = new StringBuilder();
        
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_accountquery_IF").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")).replace("?3", accountNumber));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * accountQuery.
			 * append("(select account_id from customeraccounts where Customer_id = '" +
			 * user.get("user_id") + "'" + " and account_id='" + accountNumber + "')");
			 * toAccountQuery.append(URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".customeraccounts a on (a.Account_id = t1.ToAccountNumber),");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_accountquery_IF").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")).replace("?3", accountNumber));
        } else {
			
			 * accountQuery.append("(select account_id from accounts where User_id = '" +
			 * user.get("user_id") + "'" + " and account_id='" + accountNumber + "')");
			 * toAccountQuery.append(URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".accounts a on (a.Account_id = t1.ToAccountNumber),");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_searchTrans_accountquery_ELSE").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")).replace("?3", accountNumber));
        }*/

		/*
		 * filter.
		 * append("select t.*,tt.description as transactionType, IF(p.organizationId or e.organizationId or a.Organization_id, 1, 0) as isBusinessPayee from "
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction t left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".payee p on (p.Id = t.Payee_id) left join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".externalaccount e on (e.Id = t.Payee_id) left join " +
		 * toAccountQuery.toString() + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".transactiontype tt ");
		 * 
		 * filter.append("where (t.Type_id = tt.id) and (t.description LIKE " +
		 * searchDescription + " OR t.Amount LIKE " + searchDescription +
		 * " OR t.checkNumber LIKE " + searchDescription + ") AND Amount >= " +
		 * searchMinAmount + " AND Amount <= " + searchMaxAmount +
		 * " AND t.transactionDate >= '" + searchStartDate + "' ");
		 * filter.append("AND t.transactionDate <= '" + searchEndDate +
		 * "' AND ((t.checkNumber > " + fromCheckNumber + " AND t.checkNumber < " +
		 * toCheckNumber + ") OR t.checkNumber IS NULL) ");
		 * filter.append(" AND StatusDesc = '" + statusDescription + "'");
		 */

        if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_BOTH.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
             //       + accountQuery.toString() + ") ");
                     
            
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
            
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_DEPOSIT.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.toAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_WITHDRAWAL.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
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
            //filter.append("( tt.description = 'InternalTransfer' or tt.description = 'ExternalTransfer') ");
            filter.append("("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'InternalTransfer'"
            		+ " or " + SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery() + " ='ExternalTransfer')" );
            
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_CHECKS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
           // filter.append(" and tt.description = 'CheckWithdrawal') ");
            filter.append("and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'CheckWithdrawal')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_P2PDEBITS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            //filter.append(" and tt.description = 'P2P') ");
            filter.append(" and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'P2P')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_BILLPAY.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
           // filter.append(" and tt.description = 'BillPay') ");
            filter.append("and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'BillPay')");
        } else if ("Search".equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
            //        + accountQuery.toString() + ") ");
           filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
                    		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
                    
        } else if ("Search".equalsIgnoreCase(searchType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
               //     + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
            
        
        }
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        if (StringUtils.isBlank(sortBy)) {
            sortBy = SqlQueryEnum.valueOf(jdbcUrl+"_TRANSACTIONDATE").getQuery();
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order) && !"toAccountName".equalsIgnoreCase(sortBy)
                && !"nickName".equalsIgnoreCase(sortBy)) {
            //filter.append(" order by " + sortBy + " " + order + " ");
            filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_orderBy").getQuery().replace("?1", sortBy).replace("?2", order));
        }
        if (StringUtils.isNotBlank(firstRecordNum) && StringUtils.isNotBlank(lastRecordNum)) {
            //filter.append(" limit " + firstRecordNum + " , " + lastRecordNum + " ");
            filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_Limit").getQuery().replace("?1", firstRecordNum).replace("?2", lastRecordNum));
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
        }

        if (StringUtils.isNotBlank(userId)) {
            filter += DBPUtilitiesConstants.AND;

            filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        }

        if (StringUtils.isNotBlank(toExtAccountNum) || StringUtils.isNotBlank(iBAN)) {
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
        } else {
            String toExtAccountNum = HelperMethods.getFieldValue(transaction, "toExternalAccountNumber");
            String iBAN = HelperMethods.getFieldValue(transaction, DBPInputConstants.IBAN);
            if (StringUtils.isNotBlank(toExtAccountNum))
                transaction.addParam(new Param("toAccountNumber", toExtAccountNum, DBPUtilitiesConstants.STRING_TYPE));
            else
                transaction.addParam(new Param("toAccountNumber", iBAN, DBPUtilitiesConstants.STRING_TYPE));
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
                transaction.addParam(new Param("personId", payPersonId,
                        DBPUtilitiesConstants.STRING_TYPE));       
            }
            if (StringUtils.isBlank(transaction.getParamValueByName("toAccountNumber"))) {
                transaction.removeParamByName("toAccountNumber");
                transaction.addParam(new Param("toAccountNumber", payPersonId, DBPUtilitiesConstants.STRING_TYPE));
            }
            if (StringUtils.isBlank(transaction.getParamValueByName("toAccountName"))) {
                transaction.removeParamByName("toAccountName");
                transaction.addParam(new Param("toAccountName", HelperMethods.getFieldValue(transaction, "name"), DBPUtilitiesConstants.STRING_TYPE));
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
}
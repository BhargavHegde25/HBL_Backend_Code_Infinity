package com.kony.dbputilities.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountTransHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean constructQuery(Map inputParams, DataControllerRequest dcRequest, Result result) {

        boolean status = false;
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String accountId = (String) inputParams.get(DBPInputConstants.ACCOUNT_ID);
        String custId = (String) inputParams.get(DBPUtilitiesConstants.UA_USR_ID);
        String payeeId = (String) inputParams.get(DBPUtilitiesConstants.PAYEE_ID);
        String transType = (String) inputParams.get(DBPUtilitiesConstants.TRANSACTION_TYPE);
        String installmentType =(String)  inputParams.get(DBPUtilitiesConstants.INSTALLMENT_TYPE);
        String searchFetchType = (String) inputParams.get("searchType");
        String searchDesc = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_DESCRIPTION);
        String firstRecordNum = (String) inputParams.get(DBPUtilitiesConstants.FIRST_RECORD_NUMBER);
        String lastRecordNum = (String) inputParams.get(DBPUtilitiesConstants.LAST_RECORD_NUMBER);
        String searchMinAmt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_MIN_AMOUNT);
        String searchMaxAmt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_MAX_AMOUNT);
        String searchAmt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_AMOUNT);
        String searchStartDt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_START_DATE);
        String searchEndDt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_END_DATE);
        String searchDt = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_DATE);
        String chkNum = (String) inputParams.get(DBPUtilitiesConstants.CHK_NUMBER);
        String frmChkNum = (String) inputParams.get(DBPUtilitiesConstants.FRM_CHK_NUMBER);
        String toChkNum = (String) inputParams.get(DBPUtilitiesConstants.TO_CHK_NUMBER);
        String acctNum = (String) inputParams.get(DBPUtilitiesConstants.ACCT_NUMBER);
        String searchTransType = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_TRANS_TYPE);
        String transactionID = (String) inputParams.get("transactionId");
        String legalEntityId = (String) inputParams.get("legalEntityId");
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);

        String filter = "";
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        alert.prepareError("transaction id :"+transactionID).log();
        if (!StringUtils.isNotBlank(sortBy)) {
            if (StringUtils.isNotBlank(transactionID)) {
                filter = getByTransactionID(transactionID, dcRequest, legalEntityId);
			}else if (StringUtils.isNotBlank(acctNum) && !acctNum.contains("$") && StringUtils.isNotBlank(searchTransType)
					&& searchTransType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN_SCHEDULE)) {

				filter = getLoanScheduleforAccontId(installmentType, acctNum, dcRequest);
				//filter = filter + " order by Date desc";
				filter = filter + SqlQueryEnum.valueOf(jdbcUrl+"_ORDERBY_DATE").getQuery();
			}
            else if (StringUtils.isNotBlank(accountId) && !accountId.contains("$")) {
					filter = getAccountTransactions(accountId, dcRequest);
			}else if (StringUtils.isNotBlank(payeeId) && !payeeId.contains("$")) {
                filter = getPayeeTransactions(custId, payeeId, user.get("customerType"), dcRequest);
            } else if (StringUtils.isNotBlank(transType)
                    && transType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT)) {
                filter = findAllDepositsForUser(custId, user.get("customerType"), dcRequest);
            } else if (searchFetchType != null
                    && searchFetchType.equals(DBPUtilitiesConstants.TRANSACTION_FETCH_TYPE_SEARCH)) {
                filter = searchTransactionsForUser(user.get("customerType"), custId, searchDesc, searchMinAmt,
                        searchAmt, searchMaxAmt, searchStartDt, searchEndDt, searchDt, acctNum, chkNum, frmChkNum,
                        toChkNum, firstRecordNum, lastRecordNum, searchTransType, dcRequest);
                filter = filter + SqlQueryEnum.valueOf(jdbcUrl+"_ORDERBY_TRANSACTIONDATE").getQuery() ;
                if (StringUtils.isNotBlank(lastRecordNum) && StringUtils.isNotBlank(firstRecordNum)) {
                    //filter = filter + " limit " + firstRecordNum + "," + lastRecordNum;
                	String dbType=QueryFormer.getDBType(dcRequest);
                	filter=filter+SqlQueryEnum.valueOf(dbType+"_GetUserPendingTransactions_getTrans_limit").getQuery().replace("?1", firstRecordNum).replace("?2", lastRecordNum);
                	}
            } else {
                filter = getAllTransactionsForUser(custId, user.get("customerType"), dcRequest);
            }
            alert.prepareError("filter :"+filter).log();
            inputParams.put("transactions_query", filter);
        } else {
            if (!StringUtils.isNotBlank(order)) {
                order = "asc";
            }
			if (StringUtils.isNotBlank(acctNum) && !acctNum.contains("$") && StringUtils.isNotBlank(searchTransType)
					&& searchTransType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN_SCHEDULE)) {
				filter = getLoanScheduleforAccontId(installmentType, acctNum, dcRequest);
				if(sortBy.equalsIgnoreCase("transactionDate")) {
					sortBy= SqlQueryEnum.valueOf(jdbcUrl+"_DATE").getQuery();
				}
			} else if (StringUtils.isNotBlank(accountId) && !accountId.contains("$")) {
				filter = getSortedAccountTransactions(accountId, sortBy, order, firstRecordNum, lastRecordNum,
						dcRequest);
			} else if (StringUtils.isNotBlank(payeeId) && !payeeId.contains("$")) {
                filter = getPayeeTransactions(custId, payeeId, user.get("customerType"), dcRequest);
            } else if (StringUtils.isNotBlank(transType)
                    && transType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT)) {
                filter = findAllDepositsForUser(custId, user.get("customerType"), dcRequest);
            } else {
                filter = getAllTransactionsForUser(custId, user.get("customerType"), dcRequest);
            }
            if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
                filter = filter + " order by " + sortBy + " " + order;
            }
            inputParams.put("transactions_query", filter);
        }

        return status;
    }
    
	public String getLoanScheduleforAccontId(String installmentType, String acctNum, DataControllerRequest dcRequest) {
		StringBuilder filter = new StringBuilder();
		String condition = "";
		String jdbcUrl=QueryFormer.getDBType(dcRequest);
		if (installmentType.equalsIgnoreCase(DBPUtilitiesConstants.FUTURE)) {
		
			condition = ("("+SqlQueryEnum.valueOf(jdbcUrl+"_GetLoanSchedule_CONDITION").getQuery()+" = 'FUTURE')");
		}
		if (installmentType.equalsIgnoreCase(DBPUtilitiesConstants.DUE)) {
			//condition = " and InstallmentType='DUE'";
			condition = ("("+SqlQueryEnum.valueOf(jdbcUrl+"_GetLoanSchedule_CONDITION").getQuery()+" = 'DUE')");
		}
		if (installmentType.equalsIgnoreCase(DBPUtilitiesConstants.PAID)) {
			//condition = " and InstallmentType='PAID'";
			condition = ("("+SqlQueryEnum.valueOf(jdbcUrl+"_GetLoanSchedule_CONDITION").getQuery()+" = 'PAID')");
		}
    	
		filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetLoanSchedule").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2",acctNum));
		filter.append(condition);
		return filter.toString();
	}
	
    public String getAllTransactionsForUser(String userID, String userType, DataControllerRequest dcRequest) {
        StringBuilder filter = new StringBuilder();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if ("TYPE_ID_MICRO_BUSINESS".equals(userType) || "TYPE_ID_SMALL_BUSINESS".equals(userType)) {
			/*
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt "); filter.
			 * append("where t.isScheduled != 1 and (t.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userID + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userID + "') "); filter.
			 * append(" ) and t.Type_id = tt.id and tt.description not in ('Deposit', 'StopCheckPaymentRequest')"
			 * );
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAllTransactionsForUser_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userID));
        } else {
			/*
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt "); filter.
			 * append("where t.isScheduled != 1 and (t.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userID + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userID + "') "); filter.
			 * append(" ) and t.Type_id = tt.id and tt.description not in ('Deposit', 'StopCheckPaymentRequest')"
			 * );
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAllTransactionsForUser_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userID));
        }
        return filter.toString();
    }

    public String searchTransactionsForUser(String userType, String userID, String searchDescription,
            String searchMinAmount, String searchAmount, String searchMaxAmount, String searchStartDate,
            String searchEndDate, String searchDateRange, String accountNumber, String checkNumber,
            String fromCheckNumber, String toCheckNumber, String firstRecordNumber, String lastRecordNumber,
            String searchTransactionType, DataControllerRequest dcRequest) {
        StringBuilder filter = new StringBuilder();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);

        if (searchMinAmount == null || searchMinAmount.equals("")) {
            searchMinAmount = "0";
        }
        if (searchMaxAmount == null || searchMaxAmount.equals("")) {
            searchMaxAmount = "2147483647";
        }
        if (searchStartDate == null || searchStartDate.equals("")) {
            searchStartDate = "1970-01-01";
        }
        if (searchEndDate == null || searchEndDate.equals("")) {
            searchEndDate = "2100-01-01";
        }
        if (fromCheckNumber == null || fromCheckNumber.equals("")) {
            fromCheckNumber = "-1";
        }
        if (toCheckNumber == null || toCheckNumber.equals("")) {
            toCheckNumber = "2147483647";
        }
        if (StringUtils.isNotBlank(searchDescription)) {
        	searchDescription = "'%" + searchDescription + "%'";
        } else {
        	searchDescription = "'%'";
        }
        searchEndDate = searchEndDate + "T23:59:59";

        StringBuilder accountQuery = new StringBuilder();
        if ("TYPE_ID_MICRO_BUSINESS".equals(userType) || "TYPE_ID_SMALL_BUSINESS".equals(userType)) {
           /* accountQuery.append("(select account_id from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                    + ".customeraccounts where Customer_id = '" + userID + "'" + " and account_id='" + accountNumber
                    + "')");*/
          accountQuery.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactions").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userID).replace("?3", accountNumber));
            
        } else {
           // accountQuery.append("(select Account_id from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
             //       + ".customeraccounts where Customer_id = '" + userID + "'" + " and Account_id='" + accountNumber + "')");
        
        	 accountQuery.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactions").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userID).replace("?3", accountNumber));
        }

		/*
		 * filter.append("select t.*,tt.description as transactionType from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".transactiontype tt ");
		 * filter.append("where (t.Type_id = tt.id) and (t.description LIKE " +
		 * searchDescription + " OR t.Amount LIKE " + searchDescription +
		 * " OR t.checkNumber LIKE " + searchDescription + ") AND Amount >= '" +
		 * searchMinAmount + "' AND Amount <= '" + searchMaxAmount +
		 * "' AND t.transactionDate >= '" + searchStartDate + "' ");
		 * filter.append("AND t.transactionDate <= '" + searchEndDate +
		 * "' AND ((t.checkNumber > '" + fromCheckNumber + "' AND t.checkNumber < '" +
		 * toCheckNumber + "') OR t.checkNumber IS NULL) ");
		 * filter.append("AND t.isScheduled = '0' AND StatusDesc = 'Successful'");
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_SearchTransactionsForUser").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", searchDescription).replace("?2", searchDescription).replace("?3", searchMinAmount).replace("?4", searchMaxAmount).replace("?5", searchStartDate).replace("?6", searchEndDate).replace("?7", fromCheckNumber).replace("?8", toCheckNumber));

        if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_BOTH.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
              //      + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
           
        
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_DEPOSIT.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
          //  filter.append("( t.toAccountNumber = " + accountQuery.toString() + ") ");
            
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
           
            //filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_WITHDRAWAL.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            //filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_P2PCREDITS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.toAccountNumber = " + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            //filter.append(" and tt.description = '" + searchTransactionType + "'");
            filter.append(" and "+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = '" + searchTransactionType + "')");
        
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_TRANSFERS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
            //filter.append("( t.fromAccountNumber = " + accountQuery.toString() + " or t.toAccountNumber = "
                  //  + accountQuery.toString() + ") ");

            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
            
            filter.append(" and ");
            //filter.append("( tt.description = 'InternalTransfer' or tt.description = 'ExternalTransfer') ");
            filter.append("("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'InternalTransfer'"
            		+ " or " + SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery() + " ='ExternalTransfer')" );
            
            
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_CHECKS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
            //filter.append(" and tt.description = 'CheckWithdrawal') ");
            filter.append("and ("+SqlQueryEnum.valueOf(jdbcUrl+"_DESCRIPTION").getQuery()+" = 'CheckWithdrawal')");
        } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_SEARCH_P2PDEBITS.equalsIgnoreCase(searchTransactionType)) {
            filter.append(" and ");
           // filter.append("( t.fromAccountNumber = " + accountQuery.toString());
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery()+ " = " + accountQuery.toString() + ")");
            
           // filter.append(" and tt.description = 'P2P') ");
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
                //    + accountQuery.toString() + ") ");
            filter.append("(" +SqlQueryEnum.valueOf(jdbcUrl+"_FROMACCOUNTNUMBER").getQuery() + " = " + accountQuery.toString() + " or"
            		+SqlQueryEnum.valueOf(jdbcUrl+"_TOACCOUNTNUMBER").getQuery() + " = "+ accountQuery.toString() + "  )");
            
        }
        return filter.toString();
    }

    public String findAllDepositsForUser(String userId, String userType, DataControllerRequest dcRequest) {
        StringBuffer filter = new StringBuffer();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if ("TYPE_ID_MICRO_BUSINESS".equals(userType) || "TYPE_ID_SMALL_BUSINESS".equals(userType)) {
			/*
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append(" where t1.statusDesc = 'Pending' ");
			 * filter.append(" AND (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "'))");
			 * filter.append(" AND tt.description = 'Deposit' ");
			 * filter.append(" ORDER BY t.createdDate desc ");
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_FindAllDepositsForUser_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        } else {
			/*
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append(" where t1.statusDesc = 'Pending' ");
			 * filter.append(" AND (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "'))");
			 * filter.append(" AND tt.description = 'Deposit' ");
			 * filter.append(" ORDER BY t.createdDate desc ");
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_FindAllDepositsForUser_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        }
        return filter.toString();
    }

    public String getPayeeTransactions(String userId, String payeeId, String userType,
            DataControllerRequest dcRequest) {
        StringBuffer filter = new StringBuffer();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if ("TYPE_ID_MICRO_BUSINESS".equals(userType) || "TYPE_ID_SMALL_BUSINESS".equals(userType)) {
			/*
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "'))");
			 * filter.append(" and t1.Payee_id = '" + payeeId + "'");
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayeeTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", payeeId));
        } else {
			/*
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "'))");
			 * filter.append(" and t1.Payee_id = '" + payeeId + "'");
			 */
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayeeTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", payeeId));
        }
        return filter.toString();
    }

    public String getAccountTransactions(String accountId, DataControllerRequest dcRequest) {
        StringBuffer filter = new StringBuffer();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
		/*
		 * filter.append("select t1.*,tt.description as transactionType from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
		 * filter.append("where (t1.FromAccountNumber  = '" + accountId + "'");
		 * filter.append(" or t1.ToAccountNumber = '" + accountId + "') ");
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAccountTransactions").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", accountId));
        return filter.toString();
    }

    public String getByTransactionID(String transactionID, DataControllerRequest dcRequest, String legalEntityId) {
    	StringBuffer filter = new StringBuffer();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
    	/*
		 * String filter = "select t.*,tt.description as transactionType from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".transactiontype tt " + "where tt.id=t.Type_id and t.id='" +
		 * transactionID + "'";
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetByTransactionID").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", transactionID));
        if (StringUtils.isNotBlank(legalEntityId)) {
			filter.append(
					SqlQueryEnum.valueOf(jdbcUrl + "_BillPay_LegalEntityId").getQuery().replace("?1", legalEntityId));
		}
        return filter.toString();
    }

    public String getSortedAccountTransactions(String accountID, String sortBy, String order, String firstRecordNumber,
            String lastRecordNumber, DataControllerRequest dcRequest) {
        return getAccountTransactions(accountID, dcRequest);
    }
}
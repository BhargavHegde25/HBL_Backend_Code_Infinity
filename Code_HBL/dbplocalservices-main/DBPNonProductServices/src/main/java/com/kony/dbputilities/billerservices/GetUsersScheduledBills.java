package com.kony.dbputilities.billerservices;

import java.util.List;
import java.util.Map;

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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class GetUsersScheduledBills implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        postProcess(dcRequest, inputParams, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        if (HelperMethods.hasRecords(result)) {
            List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
            for (Record transaction : transactions) {
                updateFromAccountDetails(dcRequest, transaction);
                updateBillDetails(dcRequest, transaction);
                updateBillerMasterDetails(dcRequest, transaction);
                updateDateFormat(transaction);
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
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
            Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(biller)) {
                Record bill = biller.getAllDatasets().get(0).getRecord(0);
                String billDueDate = HelperMethods.getFieldValue(bill, "billDueDate");
                transaction.addParam(new Param("billDueDate", billDueDate, DBPUtilitiesConstants.STRING_TYPE));
                String paidDate = HelperMethods.getFieldValue(bill, "paidDate");
                transaction.addParam(new Param("paidDate", paidDate, DBPUtilitiesConstants.STRING_TYPE));
                String paidAmount = HelperMethods.getFieldValue(bill, "paidAmount");
                transaction.addParam(new Param("paidAmount", paidAmount, DBPUtilitiesConstants.STRING_TYPE));
                String billGeneratedDate = HelperMethods.getFieldValue(bill, "billGeneratedDate");
                transaction
                        .addParam(new Param("billGeneratedDate", billGeneratedDate, DBPUtilitiesConstants.STRING_TYPE));
                String dueAmount = HelperMethods.getFieldValue(bill, "dueAmount");
                transaction.addParam(new Param("billDueAmount", dueAmount, DBPUtilitiesConstants.STRING_TYPE));
                String ebillUrl = HelperMethods.getFieldValue(bill, "ebillURL");
                transaction.addParam(new Param("ebillURL", ebillUrl, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateBillerMasterDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String billMasterId = HelperMethods.getFieldValue(transaction, "billermaster_id");
        if (StringUtils.isNotBlank(billMasterId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billMasterId;
            Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_MASTER_GET);
            if (HelperMethods.hasRecords(billerMaster)) {
                Record billMaster = billerMaster.getAllDatasets().get(0).getRecord(0);
                String billerCategoryName = getCategoryName(dcRequest,
                        HelperMethods.getFieldValue(billMaster, "billerCategoryId"));
                transaction.addParam(
                        new Param("billerCategoryName", billerCategoryName, DBPUtilitiesConstants.STRING_TYPE));
                String ebillSupport = HelperMethods.getFieldValue(billMaster, "ebillSupport");
                transaction.addParam(new Param("ebillSupport", ebillSupport, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private String getCategoryName(DataControllerRequest dcRequest, String billerCategoryId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_CATEGORY_GET);
        return HelperMethods.getFieldValue(billerMaster, "cattegoryName");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String sortBy = (String) inputParams.get(DBPUtilitiesConstants.SORTBY);
        String order = (String) inputParams.get(DBPUtilitiesConstants.ORDER);
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String companyId = null;
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
        String legalEntityId = (String) customer.get("legalEntityId");
        StringBuilder filter = new StringBuilder();
        
		if (StringUtils.isBlank(legalEntityId))
			filter.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPendingTransactionDetails_ByCustomerId").getQuery()
					.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId)
					.replace("?3", sortBy));

		else
			filter.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPendingTransactionDetails_LegalEntityId").getQuery()
					.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId)
					.replace("?3", legalEntityId).replace("?4", sortBy));

        /*try {
			companyId = HelperMethods.getOrganizationIDForUser(userId, dcRequest);
		} catch (HttpCallException e) {
			companyId = null;
		}*/

        if (StringUtils.isBlank(sortBy)) {
            sortBy = "createdDate";
        }
        if (StringUtils.isNotBlank(order)) {
            order = "desc";
        }
		/*
		 * StringBuffer filter = new StringBuffer(
		 * " select t.*,p.name,p.nickName,p.eBillEnable,p.billermaster_id,tt.description as transactionType, IF(p.organizationId, 1, 0) as isBusinessPayee from ("
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction"); filter.append(" t left outer join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".payee p on t.payee_id=p.id) left join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transactiontype tt on tt.id=t.Type_id where ");
		 * filter.append("t.isScheduled = 1 AND t.statusDesc = 'Pending'");
		 * filter.append(" AND tt.description = 'BillPay' AND ");
		 */
        /*if( companyId != null && !"".equals(companyId)) {
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPendingTransactionDetails_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", sortBy));
        }
        else {
			
			 * filter.append(" t.fromAccountNumber in (select Account_id from "+
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
			 * +".accounts where User_id = '"+ userId +"') ");
			 * filter.append(" AND p.softDelete = 0 order by " + sortBy + " " + order);
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPendingTransactionDetails_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", sortBy));
        }*/
        
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
            filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().replace("?1", offset).replace("?2", limit));
        }
        inputParams.put("transactions_query", filter.toString());
        return true;
    }
}
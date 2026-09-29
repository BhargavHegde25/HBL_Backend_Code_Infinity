package com.kony.dbputilities.accountservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
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

public class GetRecentAccounts implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.ACCOUNT_TRANSACTION_PROC);
			result = postProcess(dcRequest, result);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
		String jdbcUrl=QueryFormer.getDBType(dcRequest);
		if (!StringUtils.isNotBlank(user.get("user_id"))) {
			HelperMethods.setValidationMsg("Please provied " + DBPInputConstants.USER_ID, dcRequest, result);
			return false;
		}
		StringBuilder filter = new StringBuilder();
		if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			/*
			 * filter.append("select t1.* from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append(" where ");
			 * filter.append(" (t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "')");
			 * filter.append(" OR t1.fromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "'))");
			 * filter.
			 * append(" AND tt.description in ('InternalTransfer','ExternalTransfer') ");
			 * filter.append(" ORDER BY t1.createdDate desc limit 0, 50");
			 */
			filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentAccounts").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
		} else {
			/*
			 * filter.append("select t1.toAccountNumber, t1.toExternalAccountNumber from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt, " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".accounts a ");
			 * filter.append(" where t1.Type_id = tt.id and a.User_id = '" +
			 * user.get("user_id") + "'"); filter.
			 * append(" (t1.ToAccountNumber = a.account_id or t1.fromAccountNumber = a.account_id"
			 * ); filter.
			 * append(" AND tt.description in ('InternalTransfer','ExternalTransfer') ");
			 * filter.append(" ORDER BY t1.createdDate desc limit 0, 50");
			 */
			filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentAccounts").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
		}
		inputParams.put("transactions_query", filter.toString());
		return true;
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		Result retResult = new Result();
		if (HelperMethods.hasRecords(result)) {
			Dataset ds = result.getAllDatasets().get(0);
			Dataset resutds = new Dataset();
			resutds.setId(ds.getId());
			Set<String> accounts = new HashSet<>();
			int recordNum = 0;
			int size = ds.getAllRecords().size();
			int added = 0;
			String userID = HelperMethods.getUserIdFromSession(dcRequest);
			while (recordNum < size && added <= 5) {
				Record rec = ds.getRecord(recordNum);
				recordNum++;
				String toAccount = HelperMethods.getParamValue(rec.getParam(DBPUtilitiesConstants.TO_ACCONT));
				String extAccount = HelperMethods.getParamValue(rec.getParam(DBPUtilitiesConstants.TO_EXT_ACCT_NUM));

				if (accounts.contains(extAccount) || accounts.contains(toAccount)) {
					continue;
				}
				if (StringUtils.isNotBlank(extAccount) && !"0".equals(extAccount)) {
					accounts.add(extAccount);
					addExternalAccount(resutds, dcRequest, extAccount, null, userID);
				} else {
					addToAccount(resutds, dcRequest, extAccount, userID);
					accounts.add(toAccount);
				}
				added++;
			}
			retResult.addDataset(resutds);
		} else {
			retResult = result;
		}
		return retResult;
	}

	private void addExternalAccount(Dataset ds, DataControllerRequest dcRequest, String extAccount, String iBAN,
			String userID) throws HttpCallException {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(extAccount)) {
			sb.append("accountNumber").append(DBPUtilitiesConstants.EQUAL).append(extAccount);
		}
		sb.append(DBPUtilitiesConstants.AND);
		sb.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");

		if (StringUtils.isNotBlank(userID)) {
			sb.append(DBPUtilitiesConstants.AND);

			sb.append("User_id" + DBPUtilitiesConstants.EQUAL + userID);
		}

		Map<String, String> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, sb.toString());
		Result account = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.EXT_ACCOUNTS_GET);
		if (HelperMethods.hasRecords(account)) {
			Record data = account.getAllDatasets().get(0).getRecord(0);
			Record rec = new Record();
			rec.addParam(new Param("AccountId", HelperMethods.getFieldValue(data, "accountNumber")));
			rec.addParam(new Param("AccountName", HelperMethods.getFieldValue(data, "nickName")));
			rec.addParam(new Param("NickName", HelperMethods.getFieldValue(data, "nickName")));
			rec.addParam(new Param("AccountType", HelperMethods.getFieldValue(data, "accountType")));
			rec.addParam(
					new Param("IsInternationalAccount", HelperMethods.getFieldValue(data, "isInternationalAccount")));
			ds.addRecord(rec);
		}
	}

	private void addToAccount(Dataset ds, DataControllerRequest dcRequest, String toAccount, String userID)
			throws HttpCallException {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(toAccount)) {
			sb.append("Account_id").append(DBPUtilitiesConstants.EQUAL).append(toAccount);
		}

		if (StringUtils.isNotBlank(userID)) {
			sb.append(DBPUtilitiesConstants.AND);

			sb.append("User_id" + DBPUtilitiesConstants.EQUAL + userID);
		}

		Map<String, String> input = new HashMap<>();
		input.put(DBPUtilitiesConstants.FILTER, sb.toString());
		Result account = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
				URLConstants.ACCOUNTS_GET);
		if (HelperMethods.hasRecords(account)) {
			Record data = account.getAllDatasets().get(0).getRecord(0);
			Record rec = new Record();
			rec.addParam(new Param("AccountId", HelperMethods.getFieldValue(data, "Account_id")));
			rec.addParam(new Param("AccountName", HelperMethods.getFieldValue(data, "accountName")));
			rec.addParam(new Param("NickName", HelperMethods.getFieldValue(data, "nickName")));
			rec.addParam(new Param("AvailableBalance", HelperMethods.getFieldValue(data, "availableBalance")));
			rec.addParam(new Param("CurrentBalance", HelperMethods.getFieldValue(data, "currentBalance")));
			rec.addParam(new Param("AccountType", HelperMethods.getFieldValue(data, "typeDescription")));
			rec.addParam(new Param("DisplayName", HelperMethods.getFieldValue(data, "displayName")));
			ds.addRecord(rec);
		}
	}
}

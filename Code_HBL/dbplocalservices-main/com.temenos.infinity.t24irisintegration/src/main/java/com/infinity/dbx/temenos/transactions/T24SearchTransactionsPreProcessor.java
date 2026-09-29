package com.infinity.dbx.temenos.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.constants.TransactionType;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.TransactionsCountProperties;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class T24SearchTransactionsPreProcessor extends TemenosBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			 super.execute(params, request, response, result);
			Authentication authentication = Authentication.getInstance();
			request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
			//String authToken = authentication.getAuthToken(request);
			//diagnostic.prepareInfo(authToken).log();
		//	request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
			String companyId = CommonUtils.getParamValue(params, TemenosConstants.COMPANY_ID);
			if (StringUtils.isBlank(companyId)) {
				companyId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
						.get("companyId");
			}
			params.put(TransactionConstants.PARAM_SEARCH_TYPE, TransactionConstants.PARAM_SEARCH_TYPE_VALUE);
			request.addRequestParam_(TemenosConstants.COMPANY_ID, companyId);
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
			String transactionId = CommonUtils.getParamValue(params, TransactionConstants.TRANSACTION_ID_KEY);
			if (StringUtils.isNotBlank(transactionId)) {
				params.put(TransactionConstants.PARAM_SEARCH_TYPE, TransactionConstants.PARAM_LIST_TYPE_INDIVIDUAL);
				return true;
			}

			if (accounts != null) {
				if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransactionConstants.ACCOUNTID))) {
					params.put(TransactionConstants.ACCOUNTID,
							CommonUtils.getParamValue(params, TransactionConstants.ACCOUNT_NUMBER));
				}

				String accountId = CommonUtils.getParamValue(params, TransactionConstants.ACCOUNTID);
				if (StringUtils.isNotBlank(accountId)) {
					Account account = accounts.containsKey(accountId) ? accounts.get(accountId) : null;
					if (account != null) {
						String accountType = account.getAccountType();
						if (TemenosConstants.ACCOUNT_TYPE_SPROUT.equalsIgnoreCase(accountType)) {
							result.addOpstatusParam(0);
							return Boolean.FALSE;
						}
					}
				}

				String searchDateRange = request.getParameter(Constants.PARAM_SEARCH_DATE_RANGE);

				if (searchDateRange == null || (searchDateRange != null && (searchDateRange.equalsIgnoreCase("")
						|| searchDateRange.equalsIgnoreCase("CustomDateRange")))) {

					String fromDate = request.getParameter(Constants.PARAM_SEARCH_START_DATE);
					if (StringUtils.isBlank(fromDate)) {
						String noOfDays = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
								TemenosConstants.PROP_PREFIX_TEMENOS, TransactionConstants.PROP_SECTION_TRANSACTIONS,
								TransactionConstants.PROP_NUMBER_OF_DAYS);
						fromDate = TransactionUtils.getMinusDays(noOfDays);
						params.put(Constants.PARAM_SEARCH_START_DATE, CommonUtils.convertDateToYYYYMMDD(fromDate));
					}
					String toDate = request.getParameter(Constants.PARAM_SEARCH_END_DATE);
					if (StringUtils.isNotBlank(toDate)) {
						params.put(Constants.PARAM_SEARCH_END_DATE, CommonUtils.convertDateToYYYYMMDD(toDate));
					}

				} else {
					String dateRange = getDateRangeString(searchDateRange);
					params.put(Constants.PARAM_SEARCH_START_DATE, dateRange);
					params.put(Constants.PARAM_SEARCH_END_DATE, "");
				}
				String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
				String entryReferenceFrom = "";
				String requestType = "";
				if (params.containsKey("requestType") && params.get("requestType") != null) {
					requestType = CommonUtils.getParamValue(params, "requestType");
				}
				alert.prepareError("Search Transaction Pre Processor request type is search:" + requestType).log();

				if (requestType.equalsIgnoreCase("search") || "adhoc".equalsIgnoreCase(requestType) || "combined".equalsIgnoreCase(requestType) ) {
					offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
					if (offset != null && !offset.equals("1") && !offset.equals("")) {
						entryReferenceFrom = "YES";

					} else {
						offset = "1";
						entryReferenceFrom = "";

					}
				}

				String limit = "";
				if (requestType != null && requestType.equals("search")) {
					int perPageTransactionsLimit = 10;
					try {
						TransactionsCountProperties transactionsCountProperties = new TransactionsCountProperties(
								request, "perPage");
						perPageTransactionsLimit = Integer
								.parseInt(TransactionsCountProperties.getValue("config_value"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						alert.prepareError(e.getMessage()).log();
						diagnostic.prepareDebug("Search Transaction Pre Processor Error while reading transactions per page").log();
					}
					alert.prepareError("Search Transaction Pre Processor setting limit from properties:"
							+ perPageTransactionsLimit).log();
					limit = Integer.toString(perPageTransactionsLimit);
				} else {
					alert.prepareError("Search Transaction Pre Processor setting limit from Params").log();
					limit = CommonUtils.getParamValue(params, TransactionConstants.PARAM_LIMIT);
				}

				alert.prepareError("Search Transaction Pre Processor Limit value:" + limit).log();
			
				if("adhoc".equalsIgnoreCase(requestType)) {  //This need to be verified
					params.put(TransactionConstants.PARAM_PAGE_START, offset);
					params.put(TransactionConstants.PARAM_LIMIT, DBPUtilitiesConstants.ADHOC_DOWNLOAD_PAGE_SIZE);
				}else if("combined".equalsIgnoreCase(requestType)) {
					params.put(TransactionConstants.PARAM_PAGE_START, offset);
					params.put(TransactionConstants.PARAM_LIMIT, DBPUtilitiesConstants.COMBINED_DOWNLOAD_PAGE_SIZE);
				}else if(StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)){
					if(requestType.equals("search")) {
						params.put(TransactionConstants.PARAM_PAGE_START, offset);
						params.put(TransactionConstants.PARAM_LIMIT, limit);
					}else {
						int firstRec = Integer.parseInt(offset);
						int lastRec = Integer.parseInt(limit);
						int noOfRecords = lastRec - firstRec + 1;
						firstRec = firstRec / noOfRecords + 1;
						params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(firstRec));
						params.put(TransactionConstants.PARAM_LIMIT, String.valueOf(noOfRecords));
						params.put("downloadSize", String.valueOf(noOfRecords));
					}
					
				}

	/*			if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {    //TODO: Duplicates conditions are there, refine the code
					if (requestType != null && requestType.equals("search")) {
						params.put(TransactionConstants.PARAM_PAGE_START, offset);
						params.put(TransactionConstants.PARAM_LIMIT, limit);
					} else if("adhoc".equalsIgnoreCase(requestType) || "combined".equalsIgnoreCase(requestType)){
						params.put(TransactionConstants.PARAM_PAGE_START, offset);
						params.put(TransactionConstants.PARAM_LIMIT, "500");	
					}else {
						int firstRec = Integer.parseInt(offset);
						int lastRec = Integer.parseInt(limit);
						int noOfRecords = lastRec - firstRec + 1;
						firstRec = firstRec / noOfRecords + 1;
						params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(firstRec));
						params.put(TransactionConstants.PARAM_LIMIT, String.valueOf(noOfRecords));
						params.put("downloadSize", String.valueOf(noOfRecords));
					}
				}else {
					if("adhoc".equalsIgnoreCase(requestType) || "combined".equalsIgnoreCase(requestType)){
						params.put(TransactionConstants.PARAM_PAGE_START, offset);
						params.put(TransactionConstants.PARAM_LIMIT, "500");	
					}
				}
*/
				params.put("entryReferenceFrom", entryReferenceFrom);
				// Transaction Code
				String transactionCodesParam;
				String transactionTypeParam = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
				if (StringUtils.isBlank(transactionTypeParam)
						|| StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_ALL)
						|| StringUtils.equalsIgnoreCase(transactionTypeParam, TransactionConstants.PARAM_VALUE_BOTH)) {
					transactionCodesParam = StringUtils.EMPTY;
				} else {
					transactionTypeParam = TransactionUtils.getTransactionType(transactionTypeParam);
					TransactionType transactionType = TransactionType.getTransactionType(transactionTypeParam);
					List<Integer> transactionCodes = new ArrayList<>(
							TransactionUtils.getTransactTransactionCodes(transactionType, request));
					if (transactionCodes.isEmpty()) {
						CommonUtils.setOpStatusOk(result);
						result.addHttpStatusCodeParam(0);
						result.addDataset(new Dataset("Transaction"));
						return Boolean.FALSE;
					}
					StringBuilder stringBuilder = new StringBuilder();
					for (Integer code : transactionCodes) {
						stringBuilder.append(code + "+");
					}

					transactionCodesParam = StringUtils.trim(stringBuilder.toString());

				}
				params.put(Constants.PARAM_TRANSACTION_TYPE, transactionCodesParam);

				alert.prepareError("Params obtained:" + params).log();
				TransactionUtils.getT24TransactionType(params, request);
			}
			if (params.containsKey("transactionType") && params.get("transactionType") != null
					&& StringUtils.isNotBlank(params.get("transactionType").toString())
					&& params.get("transactionType").toString().equals("Both")) {
				params.put("transactionType", "");
			}
			
			return Boolean.TRUE;

		} catch (Exception e) {
			return Boolean.FALSE;
		}

	}
	
	private String getDateRangeString(String searchDateRange) {
		String dateRangeString="";
		Map<String,String> dateStringMap = new HashMap<String,String>();
		dateStringMap.put("7days", "!TODAY-7W");
		dateStringMap.put("14days", "!TODAY-14W");
		dateStringMap.put("1month", "!TODAY-30W");
		dateStringMap.put("2month", "!TODAY-60W");
		dateStringMap.put("3month", "!TODAY-90W");
		dateStringMap.put("6month", "!TODAY-180W");
		dateStringMap.put("12month", "!TODAY-360W");
		return dateStringMap.get(searchDateRange);
	}
}

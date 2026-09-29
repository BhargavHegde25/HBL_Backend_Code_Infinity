package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class PendingTransactionsForAdminPreProcessor extends TemenosBasePreProcessor {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			
			if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransactionConstants.ACCOUNT_NUMBER))) {
				
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addErrMsgParam("Misssing input param "+TransactionConstants.ACCOUNT_NUMBER);
				return Boolean.FALSE;
			}
			super.execute(params, request);
			
			String fromDate = request.getParameter(Constants.PARAM_SEARCH_START_DATE);
			if (StringUtils.isBlank(fromDate)) {
				String noOfDays = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
						TemenosConstants.PROP_PREFIX_TEMENOS, TransactionConstants.PROP_SECTION_TRANSACTIONS,
						TransactionConstants.PROP_NUMBER_OF_DAYS);
				fromDate = TransactionUtils.getMinusDays(noOfDays);
			}
			params.put(Constants.PARAM_SEARCH_START_DATE, CommonUtils.convertDateToYYYYMMDD(fromDate));
			if (StringUtils.isEmpty(request.getParameter(TransactionConstants.PARAM_LIMIT))) {
				params.put(TransactionConstants.PARAM_LIMIT, TransactionConstants.PARAM_TRANSACTION_COUNT_DEF_VALUE);
			}
			
			if (StringUtils.isEmpty(request.getParameter(TransactionConstants.PARAM_PAGE_START))) {
				params.put(TransactionConstants.PARAM_PAGE_START, TransactionConstants.PARAM_PAGE_START_DEF_VALUE);
			}
			
			return Boolean.TRUE;
			

		} catch (Exception e) {
			return Boolean.FALSE;
		}

	}
}
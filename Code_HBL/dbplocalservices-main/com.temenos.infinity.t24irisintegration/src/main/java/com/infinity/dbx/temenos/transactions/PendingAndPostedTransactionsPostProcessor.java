package com.infinity.dbx.temenos.transactions;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class PendingAndPostedTransactionsPostProcessor extends BasePostProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			Dataset dataset = new Dataset();
			Record record = new Record();
			dataset.setId("Meta");
			int totalSize = 0;
			if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_PENDING)))
				totalSize +=  Integer.parseInt(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_PENDING));
			if (StringUtils.isNotBlank(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED)))
				totalSize +=  Integer.parseInt(result.getParamValueByName(TransactionConstants.PARAM_TOTAL_SIZE_COMPLETED));
			record.addParam(new Param(TransactionConstants.PARAM_TOTALSIZE, Integer.toString(totalSize)));
			if (StringUtils.isNotBlank(request.getParameter(TransactionConstants.PARAM_LIMIT)))
				record.addParam(new Param(TransactionConstants.PARAM_PAGESIZE, request.getParameter(TransactionConstants.PARAM_LIMIT)));
			if (StringUtils.isNotBlank(request.getParameter(TransactionConstants.PARAM_OFFSET)))
				record.addParam(new Param(TransactionConstants.PARAM_PAGESTART, request.getParameter(TransactionConstants.PARAM_OFFSET)));
			dataset.addRecord(record);
			result.addDataset(dataset);
			return result;
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
}
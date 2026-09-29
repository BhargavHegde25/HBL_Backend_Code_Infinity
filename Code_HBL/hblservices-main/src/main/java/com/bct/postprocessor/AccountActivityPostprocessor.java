package com.bct.postprocessor;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.kony.dbx.BasePostProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountActivityPostprocessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(AccountActivityPostprocessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		logger.debug("AccountActivityPostprocessor Result:###" + ResultToJSON.convert(result));

		Dataset graphArray = result.getDatasetById("graphArray");
		logger.debug("graphArray DS Result:###" + graphArray.toString());

		List<Record> transactionRecords = graphArray != null ? graphArray.getAllRecords() : null;
		logger.debug("transactionRecords size:" + transactionRecords.size() + "");
		if (transactionRecords.size() != 0) {
			for (Record record : transactionRecords) {
				String label = record.getParamValueByName("label");
				logger.debug("label:" + label);
				String newdate = label.substring(4, 6) + "/" + label.substring(6, 8) + "/" + label.substring(0, 4);
				record.removeParamByName("label");
				record.addParam("label", newdate);
			}
		}
		return result;

	}
}

package com.bct.postprocessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class disputeTransactionPostProcessor extends BasePostProcessor {
	Logger logger = LogManager.getLogger(disputeTransactionPostProcessor.class);

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("Result:###" + ResultToJSON.convert(result));
			Dataset Transactions = result.getDatasetById("accountransactionview");
			logger.debug("Transactions DS Result:###" + Transactions.toString());
			List<Record> transactionRecords = Transactions != null ? Transactions.getAllRecords() : null;
			logger.debug("transactionRecords size:" + transactionRecords.size() + "");
			if (transactionRecords.size() != 0) {
				
				for (Record record : transactionRecords) {
					String transactionId = record.getParamValueByName("transactionId");
					logger.debug("transactionId:" + transactionId);
					if (verifyDisputeFromDB(request, transactionId)) {
						// isDisputed true
						record.addParam("isDisputed", "true");
					} else {
						// isDisputed false
						record.addParam("isDisputed", "false");
					}
				}
				
				/*** Sorting the results to display latest ones first *** Start ***/
				
			    List<Record> sortableList = new ArrayList<>(transactionRecords);
			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			    sortableList.sort((r1, r2) -> {
			        String d1 = r1.getParamValueByName("transactionDate");
			        String d2 = r2.getParamValueByName("transactionDate");

			        if (d1 == null) return 1;
			        if (d2 == null) return -1;

			        LocalDate date1 = LocalDate.parse(d1, formatter);
			        LocalDate date2 = LocalDate.parse(d2, formatter);

			        // Descending (newest → oldest)
			        return date2.compareTo(date1);
			    });
			    Dataset sortedDataset = new Dataset();
			    sortedDataset.setId("accountransactionview");

			    // 2. Add all sorted records to the new dataset
			    for (Record rec : sortableList) {
			        sortedDataset.addRecord(rec);
			    }
			    result.removeDatasetById("accountransactionview");
			    result.addDataset(sortedDataset);
			    
			    /*** End ***/
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

	public boolean verifyDisputeFromDB(DataControllerRequest request, String TransactionId) {
		try {
			Result dispute = new Result();
			Dataset disputeDataset = new Dataset();
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			dispute = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "dbpRbLocalServicesdb",
					"dbxdb_disputeTransactions_get", false);
			logger.debug("DisputeTransactions result##" + ResultToJSON.convert(dispute));
			disputeDataset = dispute.getDatasetById("disputeTransactions");
			logger.debug("getDisputeTrans :" + disputeDataset.toString());
			logger.debug("TransactionId to check :" + TransactionId);
			for (int i = 0; i < disputeDataset.getAllRecords().size(); i++) {
				logger.debug("transactionId##" + disputeDataset.getRecord(i).getParamValueByName("transactionId"));
				if (TransactionId.equalsIgnoreCase(disputeDataset.getRecord(i).getParamValueByName("transactionId")))
					return true;
			}
		} catch (Exception e) {
			logger.debug("Error while retrieving CustomerType_id for Customer" + e.toString());
		}
		return false;
	}
}

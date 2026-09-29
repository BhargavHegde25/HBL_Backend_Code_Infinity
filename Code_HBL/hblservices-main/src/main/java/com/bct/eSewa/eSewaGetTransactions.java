package com.bct.eSewa;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class eSewaGetTransactions implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(eSewaGetTransactions.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		LOG.debug("customerId##:"+ CustomerId);
		Result result = getDisputeTrans(request, CustomerId);
		LOG.debug("Result::##"+ ResultToJSON.convert(result));
		return result;
	}

	private Result getDisputeTrans(DataControllerRequest request, String customer_id) {
		Dataset customerDataset = new Dataset();
		Result result = null;
		String isAdmin = "";
		String filter = "";
		try {
			
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			if(isAdmin == null || isAdmin == "null" || isAdmin == "") {
				filter = CommonUtils.buildOdataCondition("Customer_id", Constants.EQUAL,
						customer_id);

				svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			}
			
			result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "HBLMerchantCRUDService",
					"dbxdb_esewaTransactionLog_get", false);
			LOG.debug("esewaTransactionLog result :" + ResultToJSON.convert(result));
			customerDataset = result.getDatasetById("esewaTransactionLog");
			result = constructMockData(customerDataset);
			LOG.debug("esewaTransactionLog :" + customerDataset.toString());
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + customer_id);
		}
		return result;
	}

	private Result constructMockData(Dataset ds) {
		Result result = new Result();
		Dataset resDataset = new Dataset();
		int totalRecords = ds.getAllRecords().size();
		try {
			LOG.debug("Inside constructMockData");
			if(ds==null) {
				ds = new Dataset();
			}else {
				LOG.debug("dataset size:"+ totalRecords+"");
				LOG.debug("Amount: "+ ds.getRecord(0).getParamValueByName("amount"));;
				LOG.debug("fromAccountNumber: "+ ds.getRecord(0).getParamValueByName("fromAccountNumber"));;
			}
			Record record;
			resDataset.setId("Transactions");
			for (int i = 0; i < totalRecords; i++) {
				LOG.debug("Inside Loop i value:"+ i);
				LOG.debug("record size :"+ totalRecords);
				if(i == totalRecords)
					break;
				record = new Record();
				record.addStringParam("SourceAccountNo", ds.getRecord(i).getParamValueByName("SourceAccountNo"));
				record.addStringParam("Status", ds.getRecord(i).getParamValueByName("Status"));
				record.addStringParam("Amount", ds.getRecord(i).getParamValueByName("Amount"));
				record.addStringParam("EsewaId", ds.getRecord(i).getParamValueByName("EsewaId"));
				record.addStringParam("OriginatingUniqueId", ds.getRecord(i).getParamValueByName("OriginatingUniqueId"));
				record.addStringParam("Status", ds.getRecord(i).getParamValueByName("Status"));
				record.addStringParam("ResponseCode", ds.getRecord(i).getParamValueByName("ResponseCode"));
				record.addStringParam("SenderName", ds.getRecord(i).getParamValueByName("SenderName"));
				record.addStringParam("Purpose", ds.getRecord(i).getParamValueByName("TransactionPurpose"));
				record.addStringParam("Fee", ds.getRecord(i).getParamValueByName("Fee"));
				record.addStringParam("TransactionId", ds.getRecord(i).getParamValueByName("TransactionDetailOriginatingUniqueId"));
				record.addStringParam("Statuscheck", ds.getRecord(i).getParamValueByName("statuscheck"));
				record.addStringParam("Channel", ds.getRecord(i).getParamValueByName("channelName"));
				record.addStringParam("TransactionDate", ds.getRecord(i).getParamValueByName("TransactionDate"));
				resDataset.addRecord(record);
			}
			result.addDataset(resDataset);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer constructMockData:"+ e);
		}
		return result;
	}

}

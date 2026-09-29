package com.bct.javaservices;

import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class TransactionPINResetRequestGet implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(TransactionPINResetRequestGet.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			LOG.debug("HBL:ResetTransactionPINResetRequest :");
			getTransactionPinRestReqList(request);
			result = getTransactionPinRestReqList(request);
			LOG.debug("Result::##" + ResultToJSON.convert(result));
		} catch (Exception e) {
			LOG.error("Exception occured in TransactionPINResetRequestGet:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private Result getTransactionPinRestReqList(DataControllerRequest request) {
		Dataset requestsDataset = new Dataset();
		Result result = null;
		try {

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, Constants.DBX_DB_SERVICE_NAME,
					"dbxdb_transactionpinResetReq_get", false);
			LOG.debug("getDisputeTrans result :" + ResultToJSON.convert(result));
			requestsDataset = result.getDatasetById("transactionpinResetReq");
			result = constructMockData(requestsDataset);
			LOG.debug("getTransactionPinRestReqList :" + requestsDataset.toString());
		} catch (Exception e) {
			LOG.error("Error while retrieving getTransactionPinRestReqList");
		}
		return result;
	}

	private Result constructMockData(Dataset ds) {
		Result result = new Result();
		Dataset resDataset = new Dataset();
		int totalRecords = ds.getAllRecords().size();
		try {
			LOG.debug("Inside constructMockData");
			if (ds == null) {
				ds = new Dataset();
			} else {
				LOG.debug("dataset size:" + totalRecords + "");
			}
			Record record;
			resDataset.setId("serviceReqs");
			for (int i = 0; i < totalRecords; i++) {
				LOG.debug("Inside Loop i value:" + i);
				LOG.debug("record size :" + totalRecords);
				if (i == totalRecords)
					break;
				record = new Record();
				record.setId("resetPinRequests");
				record.addStringParam("RequestID", ds.getRecord(i).getParamValueByName("id"));
				record.addStringParam("RequestedBy", ds.getRecord(i).getParamValueByName("Customer_id"));
				record.addStringParam("RequestDateAndTime", ds.getRecord(i).getParamValueByName("requestDate"));

				record.addStringParam("Channel", ds.getRecord(i).getParamValueByName("channelName"));
				record.addStringParam("DeviceDetails", ds.getRecord(i).getParamValueByName("browser"));
				record.addStringParam("DeviceOSVersion", ds.getRecord(i).getParamValueByName("os"));
				record.addStringParam("Status", ds.getRecord(i).getParamValueByName("status"));
				record.addStringParam("customerName", ds.getRecord(i).getParamValueByName("customerName") + "-" + 
						ds.getRecord(i).getParamValueByName("coreIdentifier"));
				
				

				resDataset.addRecord(record);
			}
			result.addDataset(resDataset);
		} catch (Exception e) {
			LOG.error("Error while retrieving constructMockData:" + e);
		}
		return result;
	}
}

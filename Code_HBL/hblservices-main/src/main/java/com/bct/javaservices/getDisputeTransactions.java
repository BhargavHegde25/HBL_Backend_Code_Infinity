package com.bct.javaservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.constants.TemenosConstants;
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
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class getDisputeTransactions implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(getDisputeTransactions.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		// String UserName = request.getParameter("userName");
		// LOG.debug("UserName:"+ UserName);
		// String customerId = getCustomerIDFromUsername(request, UserName);
		Result result = new Result();
		try {
			String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
			LOG.debug("customerId##:" + CustomerId);
			result = getDisputeTrans(request, CustomerId);
			LOG.debug("Result::##" + ResultToJSON.convert(result));
		} catch (Exception e) {
			LOG.error("Exception occured in getDisputeTransactions:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private Result getDisputeTrans(DataControllerRequest request, String customer_id) {
		Dataset customerDataset = new Dataset();
		Result result = null;
		String isAdmin = "";
		String filter = "";
		try {
			
			isAdmin = request.getParameter("isAdmin");
			LOG.debug("isAdmin value:"+ isAdmin);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			if(isAdmin == null || isAdmin == "null" || isAdmin == "") {
				filter = CommonUtils.buildOdataCondition("Customer_id", Constants.EQUAL,
						customer_id);

				svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			}
			
			result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, Constants.DBX_DB_SERVICE_NAME,
					"dbxdb_disputeTransactions_get", false);
			LOG.debug("getDisputeTrans result :" + ResultToJSON.convert(result));
			customerDataset = result.getDatasetById("disputeTransactions");
			result = constructMockData(customerDataset);
			LOG.debug("getDisputeTrans :" + customerDataset.toString());
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + customer_id);
		}
		return result;
	}

	private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {

		String customerid = "";
		try {
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getCustomerIDFromUsername:");
			}
			LOG.debug("getCustomerIDFromUsername id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;
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
			Record recordSubSet;
			resDataset.setId("serviceReqs");
			for (int i = 0; i < totalRecords; i++) {
				LOG.debug("Inside Loop i value:"+ i);
				LOG.debug("record size :"+ totalRecords);
				if(i == totalRecords)
					break;
				record = new Record();
				recordSubSet = new Record();
				recordSubSet.setId("serviceReqRequestIn");
				recordSubSet.addStringParam("amount", ds.getRecord(i).getParamValueByName("amount"));
				recordSubSet.addStringParam("description", ds.getRecord(i).getParamValueByName("description"));
				recordSubSet.addStringParam("fromAccountName", ds.getRecord(i).getParamValueByName("fromAccountName"));
				recordSubSet.addStringParam("fromAccountNumber",
						ds.getRecord(i).getParamValueByName("fromAccountNumber"));
				recordSubSet.addStringParam("transactionDate", ds.getRecord(i).getParamValueByName("transactionDate"));
				recordSubSet.addStringParam("transactionId", ds.getRecord(i).getParamValueByName("transactionId"));
				recordSubSet.addStringParam("transactionsNotes",
						ds.getRecord(i).getParamValueByName("transactionsNotes"));
				recordSubSet.addStringParam("transactionType", ds.getRecord(i).getParamValueByName("transactionType"));
				recordSubSet.addStringParam("disputeReason", ds.getRecord(i).getParamValueByName("disputeReason"));
				recordSubSet.addStringParam("description", ds.getRecord(i).getParamValueByName("description"));
				recordSubSet.addStringParam("remarks", ds.getRecord(i).getParamValueByName("remarks"));
				if(ds.getRecord(i).getParamValueByName("toAccountName") != null && ds.getRecord(i).getParamValueByName("toAccountName") != "")
				recordSubSet.addStringParam("toAccountName", ds.getRecord(i).getParamValueByName("toAccountName"));
				if(ds.getRecord(i).getParamValueByName("toAccountNumber") != null && ds.getRecord(i).getParamValueByName("toAccountNumber") != "")
				recordSubSet.addStringParam("toAccountNumber", ds.getRecord(i).getParamValueByName("toAccountNumber"));
				record.addRecord(recordSubSet);
				record.addStringParam("serviceReqId", ds.getRecord(i).getParamValueByName("id"));
				record.addStringParam("serviceReqProcessedTime", formatedDate(ds.getRecord(i).getParamValueByName("serviceReqProcessedTime")));
				record.addStringParam("serviceReqStatus", ds.getRecord(i).getParamValueByName("disputeStatus"));
				resDataset.addRecord(record);
			}
			result.addDataset(resDataset);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer constructMockData:"+ e);
		}
		return result;
	}
	
	private String formatedDate(String inputDateString) {
		
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(inputDateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String outputDateString = dateTime.format(outputFormatter);

        LOG.debug("Original Date: " + inputDateString);
        LOG.debug("Formatted Date: " + outputDateString);
		return outputDateString;
	}

}

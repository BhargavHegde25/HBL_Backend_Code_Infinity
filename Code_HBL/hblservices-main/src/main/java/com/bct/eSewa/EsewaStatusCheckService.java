package com.bct.eSewa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLURLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class EsewaStatusCheckService implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(EsewaStatusCheckService.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		 ServicesManager sm = request.getServicesManager();
		 Dataset logDataset = new Dataset();
		 ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
	     String ESEWA_BASE_URL = paramHelper.getServerProperty("ESEWA_BASE_URL");
	     String ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY = paramHelper.getServerProperty("ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY");
	     String ESEWA_CLIENT_SIGNATURE_PRIVATEKEY = paramHelper.getServerProperty("ESEWA_CLIENT_SIGNATURE_PRIVATEKEY");
	     String ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY = paramHelper.getServerProperty("ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY");
	     String ESEWA_SERVER_SIGNATURE_PUBLICKEY = paramHelper.getServerProperty("ESEWA_SERVER_SIGNATURE_PUBLICKEY");
	     String ESEWA_CLINET_ID = paramHelper.getServerProperty("ESEWA_CLINET_ID");
	     String ESEWA_SWIFT_CODE = paramHelper.getServerProperty("ESEWA_SWIFT_CODE");
	     
		 String STATUS_CHECK_URL = ESEWA_BASE_URL + "/api/auth/load/status";
	     
		Result result = new Result();

		Result transResult = getLastFiveMinteSewaPendingTrans(request);
		String res = "";
		
		logger.debug("EsewaStatusCheckService result :" + ResultToJSON.convert(transResult));
		
		logDataset = transResult.getDatasetById("esewaTransactionPendingLog");
		int totalrecords = logDataset.getAllRecords().size();

		logger.debug("EsewaStatusCheckService result length:" + totalrecords);
		
		if (null != logDataset && totalrecords == 0) {
			result.setParam(new Param("message",
					"No pending esewa transactions to recheck the status"));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

			return result;
		}else {
			for(int i = 0; i < totalrecords; i++) {
				String OriginatingUniqueId = logDataset.getRecord(i).getParamValueByName("OriginatingUniqueId");
				String core_identifier = logDataset.getRecord(i).getParamValueByName("core_identifier");
				String username = logDataset.getRecord(i).getParamValueByName("username");
				String Customer_id = logDataset.getRecord(i).getParamValueByName("Customer_id");
				
				logger.debug("OriginatingUniqueId ## :" + OriginatingUniqueId);
				
				String[] pendingTransactions = { OriginatingUniqueId };
				
				res = EsewaValidationClient.checkTransactionStatus(request, pendingTransactions,
						 ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY,
						 ESEWA_CLIENT_SIGNATURE_PRIVATEKEY,
						 ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY,
						 ESEWA_SERVER_SIGNATURE_PUBLICKEY,
						 ESEWA_CLINET_ID,
						 ESEWA_SWIFT_CODE,
						 STATUS_CHECK_URL,core_identifier,Customer_id,username
						 );
				
				logger.debug("checkTransactionStatus result ## :"+OriginatingUniqueId +"  "+ res);
				
			}
		}
		
		if (res == null || res.isEmpty()) {
			result.setParam(new Param("message",
					"We’re experiencing issues with eSewa services. Please try again later."));
			result.addParam("errmsg", "We’re experiencing issues with eSewa services. Please try again later.");
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

			return result;
		}

		/*ObjectMapper mapper = new ObjectMapper();
		ESewaStatusCheckResponse resData = mapper.readValue(res, ESewaStatusCheckResponse.class);
		String transactionStatus = resData.getEsewa_load_status_responses().get(0).getTransactionStatus();
		String originatingUniqueId = resData.getEsewa_load_status_responses().get(0).getOriginatingUniqueId();
		String transId = resData.getEsewa_load_status_responses().get(0).getTransactionId();
		String responseCode = resData.getEsewa_load_status_responses().get(0).getResponse_code();

		logger.debug("Transaction Status: " + transactionStatus);
		logger.debug("originatingUniqueId: " + originatingUniqueId);
		logger.debug("transactionId: " + transId);
		logger.debug("responseCode: " + responseCode);

		result.setParam(new Param("transactionStatus", transactionStatus));
		result.setParam(new Param("originatingUniqueId", originatingUniqueId));
		result.setParam(new Param("transId", transId));
		result.setParam(new Param("responseCode", responseCode));
		result.setParam(new Param("responseData", resData.toString()));*/
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));

		return result;
	}

	
	private Result getLastFiveMinteSewaPendingTrans(DataControllerRequest request) {
		Dataset eSewaPendingTransData = new Dataset();
		Result result = null;
		String filter = "";
		try {
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			
			LocalDateTime time = LocalDateTime.now().minusMinutes(5);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			String formattedTime = time.format(formatter);
			filter = "createdts ge datetime'" + formattedTime + "'" +
				    " and (status eq 'PENDING' or status eq 'AMBIGUOUS' or status eq 'PARTIAL_COMPLETE')";

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE,
					HBLURLConstants.ESEWA_TRANSACTION_PENDING_LOG_GET, false);
			logger.debug("getLastFiveMinteSewaPendingTrans result :" + ResultToJSON.convert(result));
			eSewaPendingTransData = result.getDatasetById("esewaTransactionPendingLog");
			logger.debug("getLastFiveMinteSewaPendingTrans :" + eSewaPendingTransData.toString());
		} catch (Exception e) {
			logger.error("Error while retrieving CustomerType_id for Customer " + e);
		}
		return result;
	}
	
    public static String getTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}
	
}

package com.temenos.infinity.api.holdings.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.holdings.businessdelegate.api.DisputeTransactionBusinessDelegate;
import com.temenos.infinity.api.holdings.resource.api.DisputeTransactionResource;

public class DisputeTransactionResourceImpl implements DisputeTransactionResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result crearteDisputeCoreTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		// Initialization of business Delegate Class
		DisputeTransactionBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DisputeTransactionBusinessDelegate.class);

		if (DBPUtilitiesConstants.TRANSACTION_TYPE_P2P.equalsIgnoreCase(request.getParameter("transactionType"))
				|| DBPUtilitiesConstants.TRANSACTION_TYPE_PAY_BILL
						.equalsIgnoreCase(request.getParameter("transactionType"))
				|| DBPUtilitiesConstants.TRANSACTION_TYPE_CARDPAYMENT
						.equalsIgnoreCase(request.getParameter("transactionType"))) {
			return result;
		} else {
			try {

				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap = getMap(postParametersMap,request);
				result = businessDelegate.createDisputeCoreTransaction(postParametersMap, request.getHeaderMap());
				result.addStringParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
				return result;
			} catch (Exception e) {
				alert.prepareError("Caught exception at invoke : " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		}

	}

	/**
	 * used to set parameters
	 * 
	 * @param postParametersMap
	 * @return
	 */
	private Map<String, Object> getMap(Map<String, Object> postParametersMap,DataControllerRequest request) {
		postParametersMap.put("transactionId", request.getParameter("transactionId"));
		postParametersMap.put("disputeDescription", request.getParameter("disputeDescription"));
		postParametersMap.put("disputeReason", request.getParameter("disputeReason"));
		postParametersMap.put("transactionType", request.getParameter("transactionType"));
		return postParametersMap;
	}


	@Override
	public Result crearteDisputeBillPayTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		// Initialization of business Delegate Class
		DisputeTransactionBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DisputeTransactionBusinessDelegate.class);

		if (DBPUtilitiesConstants.TRANSACTION_TYPE_PAY_BILL.equalsIgnoreCase(request.getParameter("transactionType"))) {
			try {
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap = getMap(postParametersMap,request);
				result = businessDelegate.createDisputeBillPayTransaction(postParametersMap, request.getHeaderMap());
				result.addStringParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
				return result;
			} catch (Exception e) {
				alert.prepareError("Caught exception at invoke : " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		} else {
			return result;
		}

	}

	@Override
	public Result crearteDisputePayAPersonTransaction(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Result result = new Result();
		// Initialization of business Delegate Class
		DisputeTransactionBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DisputeTransactionBusinessDelegate.class);

		if (DBPUtilitiesConstants.TRANSACTION_TYPE_P2P.equalsIgnoreCase(request.getParameter("transactionType"))) {
			try {
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap = getMap(postParametersMap,request);
				result = businessDelegate.crearteDisputePayAPersonTransaction(postParametersMap, request.getHeaderMap());
				result.addStringParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
				return result;
			} catch (Exception e) {
				alert.prepareError("Caught exception at invoke : " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		} else {
			return result;
		}
	}

	@Override
	public Result crearteDisputeCardTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		// Initialization of business Delegate Class
		DisputeTransactionBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DisputeTransactionBusinessDelegate.class);

		if (DBPUtilitiesConstants.TRANSACTION_TYPE_CARDPAYMENT.equalsIgnoreCase(request.getParameter("transactionType"))) {
			try {
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap = getMap(postParametersMap,request);
				result = businessDelegate.crearteDisputeCardTransaction(postParametersMap, request.getHeaderMap());
				result.addStringParam(DBPUtilitiesConstants.MSG_STATUS, DBPUtilitiesConstants.SUCCESS);
				return result;
			} catch (Exception e) {
				alert.prepareError("Caught exception at invoke : " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		} else {
			return result;
		}
	}

	@Override
	public Result createDisputeTransaction(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
			String disputeTranscationsBackend = EnvironmentConfigurationsHandler.getValue("DISPUTETXNS_BACKEND");
			diagnostic.debug("createDisputeTransaction##");
			if ("STUB".equalsIgnoreCase(disputeTranscationsBackend)) {
				result.addStringParam("orderId", String.format("%06d", new java.util.Date().getTime()%1000000));
		        result.addStringParam("message", "Service Request created successfully");
		        return result;
			}
			else {
				try {
					HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
					HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
					diagnostic.debug("createDisputeTransaction else##");
					String transactionId = request.getParameter("transactionId");
					String disputeReason = request.getParameter("disputeReason");
					String disputeDescription = request.getParameter("disputeDescription");
					String transactionType = request.getParameter("transactionType");
					String transactionsNotes = request.getParameter("transactionsNotes");
					String amount = request.getParameter("amount");
					String description = request.getParameter("description");
					String fromAccountName = request.getParameter("fromAccountName");
					String fromAccountNumber = request.getParameter("fromAccountNumber");
					String toAccountName = request.getParameter("toAccountName");
					String toAccountNumber = request.getParameter("toAccountNumber");
					String transactionDate = request.getParameter("transactionDate");
					String secureMessageId = request.getParameter("secureMessageId");
					String merchantCity = request.getParameter("merchantCity");
					String merchantAddressName = request.getParameter("merchantAddressName");
					
					
					params.put("transactionId", transactionId);
					params.put("disputeReason", disputeReason);
					params.put("disputeDescription", disputeDescription);
					params.put("transactionType", transactionType);
					params.put("transactionsNotes", transactionsNotes);
					params.put("amount", amount);
					params.put("description", description);
					params.put("fromAccountName", fromAccountName);
					params.put("fromAccountNumber", fromAccountNumber);
					params.put("toAccountName", toAccountName);
					params.put("toAccountNumber", toAccountNumber);
					params.put("transactionDate", transactionDate);
					params.put("transactionDate", transactionDate);
					params.put("merchantCity", merchantCity);
					params.put("merchantAddressName", merchantAddressName);
					
					

					String serviceName = "DisputeTransactions";
					String operationName = "createDisputeTransaction";

					String resultStr = DBPServiceExecutorBuilder.builder()
			                    .withServiceId(serviceName)
			                    .withOperationId(operationName)
			                    .withRequestParameters(params).withRequestHeaders(serviceHeaders)
			                    .withDataControllerRequest(request).build().getResponse();
					result = JSONToResult.convert(new JSONObject(resultStr).toString());
					diagnostic.debug("createDisputeTransaction result:##"+ ResultToJSON.convert(result));
				} catch (Exception e) {
					Result errorResult = new Result();
					return errorResult;
				}
				return result;
				
			}
	}

	@Override
	public Result getDisputeTransactionRequests(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		diagnostic.debug("getDisputeTransactionRequests##");
		String disputeTranscationsBackend = EnvironmentConfigurationsHandler.getValue("DISPUTETXNS_BACKEND");
		if ("STUB".equalsIgnoreCase(disputeTranscationsBackend)) {
			return constructMockData(result);
		}
		else {
			try {
				HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
				HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
				diagnostic.debug("getDisputeTransactionRequests else##");
				String serviceName = "DisputeTransactions";
				String operationName = "getDisputeTransactions";

				String resultStr = DBPServiceExecutorBuilder.builder()
		                    .withServiceId(serviceName)
		                    .withOperationId(operationName)
		                    .withRequestParameters(params).withRequestHeaders(serviceHeaders)
		                    .withDataControllerRequest(request).build().getResponse();
				result = JSONToResult.convert(new JSONObject(resultStr).toString());
				diagnostic.debug("getDisputeTransactionRequests result##"+ ResultToJSON.convert(result));
			} catch (Exception e) {
				Result errorResult = new Result();
				alert.prepareError("Exception Occured while fetching order details:" + e).log();
				return errorResult;
			}
			return result;
			
		}
	}
	
	private Result constructMockData(Result result) {
		
		Dataset ds = new Dataset();
		ds.setId("serviceReqs");
		Record record = new Record();
		Record recordSubSet = new Record();
		recordSubSet.setId("serviceReqRequestIn");
		recordSubSet.addStringParam("amount", "-7");
		recordSubSet.addStringParam("description", "Cust Debit Book with charges");
		recordSubSet.addStringParam("fromAccountName", "Rex");
		recordSubSet.addStringParam("fromAccountNumber", "105929");
		recordSubSet.addStringParam("transactionDate", "2022-12-08");
		recordSubSet.addStringParam("transactionId", "PI2310802PMJ0W41");
		recordSubSet.addStringParam("transactionsNotes", "BNK23108HFMK0FKK");
		recordSubSet.addStringParam("transactionType", "Others");
		recordSubSet.addStringParam("disputeReason", "I don't recognize this transaction");
		record.addRecord(recordSubSet);
		record.addStringParam("serviceReqId", "CDT232948NKVY");
		record.addStringParam("serviceReqProcessedTime", "2023-10-23T09:21:50.975712Z");
		record.addStringParam("serviceReqStatus", "Request Failed");
		ds.addRecord(record);

		record = new Record();
	    recordSubSet = new Record();
		recordSubSet.setId("serviceReqRequestIn");
		recordSubSet.addStringParam("amount", "-400");
		recordSubSet.addStringParam("description", "Cust Debit Book with charges");
		recordSubSet.addStringParam("fromAccountName", "Rex");
		recordSubSet.addStringParam("fromAccountNumber", "105929");
		recordSubSet.addStringParam("transactionDate", "2023-04-18");
		recordSubSet.addStringParam("transactionId", "FT23108TWR28");
		recordSubSet.addStringParam("transactionsNotes", "FT23108TWR28");
		recordSubSet.addStringParam("transactionType", "Transfers");
		recordSubSet.addStringParam("disputeReason", "Recurring debit which was cancelled");
		record.addRecord(recordSubSet);
		record.addStringParam("serviceReqId", "CDT23294D607W");
		record.addStringParam("serviceReqProcessedTime", "2023-10-23T09:21:50.975712Z");
		record.addStringParam("serviceReqStatus", "Request Failed");
		ds.addRecord(record);

		record = new Record();
	    recordSubSet = new Record();
		recordSubSet.setId("serviceReqRequestIn");
		recordSubSet.addStringParam("amount", "1500");
		recordSubSet.addStringParam("description", "Online Banking Transfer");
		recordSubSet.addStringParam("fromAccountName", "John");
		recordSubSet.addStringParam("fromAccountNumber", "fake_demobank_xf-1083530004848974209");
		recordSubSet.addStringParam("transactionDate", "2022-12-08");
		recordSubSet.addStringParam("transactionId", "1083530007189395859");
		recordSubSet.addStringParam("transactionType", "Others");
		recordSubSet.addStringParam("disputeReason", "Billing error");
		record.addRecord(recordSubSet);
		record.addStringParam("serviceReqId", "CDT23296RWWVE");
		record.addStringParam("serviceReqProcessedTime", "2023-10-23T09:21:50.975712Z");
		record.addStringParam("serviceReqStatus", "Request Failed");
		ds.addRecord(record);

		result.addDataset(ds);

		return result;
	}

}

package com.temenos.dbx.transaction.resource.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.businessdelegate.api.DisputeTransactionBusinessDelegate;
import com.temenos.dbx.transaction.resource.api.DisputeTransactionResource;

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

}

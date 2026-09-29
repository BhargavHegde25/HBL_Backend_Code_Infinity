package com.bct.javaservices;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateDisputeTransaction implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(UpdateDisputeTransaction.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			Integer flagUpdate = updateDisputeTransaction(request);
			if (flagUpdate == 1) {
				result.setParam(new Param("status", "Dispute transaction status updated successfully."));
			} else {
				result.addParam(new Param("status", "Failed to update dispute status.", MWConstants.STRING));
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in UpdateDisputeTransaction:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private int updateDisputeTransaction(DataControllerRequest request) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		LOG.debug("disputeStatus:" + request.getParameter("disputeStatus"));
		LOG.debug("disputeId:" + request.getParameter("id"));
		LOG.debug("remarks:" + request.getParameter("remarks"));
		inputParams.put("disputeStatus", request.getParameter("disputeStatus"));
		inputParams.put("id", request.getParameter("id"));
		inputParams.put("remarks", request.getParameter("remarks"));
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_disputeTransactions_update";
		int isSuccess = 0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update updateDisputeTransaction");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't update dispute status due to  : " + errMessage);
			isSuccess = 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					isSuccess = 1;
				}
			} catch (Exception e) {
				LOG.debug("Couldn't update dispute status");
				isSuccess = 0;
			}
		}
		return isSuccess;

	}

}

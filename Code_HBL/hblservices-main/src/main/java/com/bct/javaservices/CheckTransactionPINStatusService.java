package com.bct.javaservices;

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
import com.konylabs.middleware.dataobject.Result;

public class CheckTransactionPINStatusService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CheckTransactionPINStatusService.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result retVal = new Result();
		try {
			String UserName = request.getParameter("userName");
			String PinStatus = getTransactionPINStatus(request, UserName);
			LOG.debug("CheckTransactionPINStatusService :" + PinStatus);
			try {
				if (PinStatus != null && !PinStatus.trim().isEmpty()) {
					retVal.addParam(new Param("isTransactionPinSet", "" + "true", "String"));
				} else {
					LOG.debug("Updated flag as 1");
					retVal.addParam(new Param("isTransactionPinSet", "" + "false", "String"));
				}
				retVal.setParam(new Param("opstatus", "0"));
				retVal.setParam(new Param("httpStatusCode", "200"));
			} catch (Exception e) {
				retVal.addParam(new Param("ErrMsg", "" + "Failed", "String"));
				retVal.setParam(new Param("opstatus", "0"));
				retVal.setParam(new Param("httpStatusCode", "200"));
				LOG.debug("Exception in CheckTransactionPINStatusService:" + e);
			}
		} catch (Exception e) {
			LOG.error("Exception occured in CheckTransactionPINStatusService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(retVal);
			retVal.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			retVal.addParam(new Param("success", "false"));
		}

		return retVal;
	}

	private String getTransactionPINStatus(DataControllerRequest request, String UserName) {

		String Pin = "";
		try {

			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {

				Pin = customerDataset.getRecord(0).getParamValueByName("Pin");
			} else {
				LOG.debug("Else getTransactionPINStatus:");
			}

			LOG.debug("getTransactionPINStatus Pin:" + Pin);

		} catch (Exception e) {

			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return Pin;

	}

}

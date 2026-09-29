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

public class ThirdParthAuthCheckWithAccNo implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(ThirdParthAuthCheckWithAccNo.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result retVal = new Result();
		try {
			String UserName = request.getParameter("userName");
			String AccNumber = request.getParameter("AccountNumber");
			String flag = getThirdPartyAuthStatus(request, UserName);
			String Customer_Id = getCustomerIDFromUsername(request, UserName);
			boolean isAccNoExists = checkAccNumberValidation(request, Customer_Id, AccNumber);
			LOG.debug("isThirdpartyAuthEnable ##:" + flag);
			LOG.debug("isAccNoExists ##:" + isAccNoExists);
			if (flag.equalsIgnoreCase("true")) {
				retVal.addParam(new Param("isThirdpartyAuthEnable", "" + "true", "String"));
			} else {
				LOG.debug("Updated flag as NULL");
				retVal.addParam(new Param("isThirdpartyAuthEnable", "" + "false", "String"));
			}
			retVal.setParam(new Param("opstatus", "0"));
			retVal.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in ThirdParthAuthCheckWithAccNo:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(retVal);
			retVal.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			retVal.addParam(new Param("success", "false"));
		}
		return retVal;
	}

	private String getThirdPartyAuthStatus(DataControllerRequest request, String UserName) {

		String IsOlbAllowed = "";
		try {
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				IsOlbAllowed = customerDataset.getRecord(0).getParamValueByName("IsOlbAllowed");
			} else {
				LOG.debug("Else getResetThirdPartyStatus:");
			}
			LOG.debug("getResetThirdPartyStatus:" + IsOlbAllowed);
		} catch (Exception e) {

			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return IsOlbAllowed;
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
				LOG.debug("Else getThirdpartyAuthFlag:");
			}
			LOG.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;
	}

	private boolean checkAccNumberValidation(DataControllerRequest request, String CutomerId, String AccNo) {

		boolean IsrecordExists = false;
		try {
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			svcParams.put("$filter", "Customer_id eq " + CutomerId + " and Account_id eq " + AccNo);
			request.addRequestParam_("$filter", "Customer_id eq " + CutomerId + " and Account_id eq " + AccNo);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OPERATION_CUSTOMER_ACCOUNTS_GET, false);
			Dataset customerDataset = result.getDatasetById("customeraccounts");
			if (null != customerDataset && customerDataset.getAllRecords().size() >0) {
				IsrecordExists = true;
			} else {
				LOG.debug("Else checkAccNumberValidation:");
				IsrecordExists = false;
			}
			LOG.debug("checkAccNumberValidation:" + IsrecordExists);
			LOG.debug("customerDataset ##"+ customerDataset.toString());
		} catch (Exception e) {
			LOG.error("Error while checkAccNumberValidation ");
		}
		return IsrecordExists;

	}
}

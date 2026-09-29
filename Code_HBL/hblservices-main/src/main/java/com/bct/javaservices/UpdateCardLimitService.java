package com.bct.javaservices;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCardLimitService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(UpdateCardLimitService.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		LOG.debug("HBL:UpdateCardLimitService :");
		Result result = new Result();
		try {

			Integer flagUpdate = updateCardLimits(request);
			if (flagUpdate == 1) {
				result.setParam(new Param("Message", "Card limit updated successfully!"));
				result.setParam(new Param("Status", "Success"));
			} else {
				result.setParam(new Param("Message", "Limit update failed!"));
				result.setParam(new Param("Status", "Failed"));
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in UpdateCardLimitService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public int updateCardLimits(DataControllerRequest request) throws Exception {
		
		
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String cardType = request.getParameter("cardType");
		String cardCategory = request.getParameter("cardCategory");
		String cardDescription = request.getParameter("cardDescription");
		String cardImage = request.getParameter("cardImage");
		String limitJson = HBLCommonUtility.createLimitJSON(request);
		
		LOG.debug("ID in update limit : "+ id);
		LOG.debug("limitJson : "+ limitJson);
		String serviceName = "CRUDLayer";
		String operationName = HBLURLConstants.CARD_LIMIT_UPDATE;
		
		inputParams.put("cardType", cardType);
		inputParams.put("cardCategory", cardCategory);
		inputParams.put("cardDescription", cardDescription);
		inputParams.put("cardLimits", limitJson);
		inputParams.put("id", id);
		inputParams.put("cardImage", cardImage);
		
		int isSuccess = 0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update updateCardLimits");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't update updateCardLimits  status due to  : " + errMessage);
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

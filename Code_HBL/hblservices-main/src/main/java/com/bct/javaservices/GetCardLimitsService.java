package com.bct.javaservices;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLURLConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetCardLimitsService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetCardLimitsService.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		LOG.debug("HBL:GetCardLimits :");
		Result result = new Result();
		try {
			Dataset limitRes = getCardLimits(request);
			if (null != limitRes) {
				result.addDataset(limitRes);
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			} else {
				result.setParam(new Param("ErrMsg", "Backend service failed!"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			}
		} catch (Exception e) {
			LOG.error("Exception occured in GetCardLimitsService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private Dataset getCardLimits(DataControllerRequest request) {
		Dataset limitDataset = null;
		try {
			String filter = "";
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					"CRUDLayer", HBLURLConstants.CARD_LIMIT_GET, false);
			limitDataset = result.getDatasetById("cardConfigLimit");
			if (null != limitDataset) {
				return limitDataset;
			} else {
				LOG.debug("Else getCardLimits:");
				return limitDataset;
			}
		} catch (Exception e) {
			LOG.error("Error in getCardLimits" );
		}
		return limitDataset;
	}
	
}

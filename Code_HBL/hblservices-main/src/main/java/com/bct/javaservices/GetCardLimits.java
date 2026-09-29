package com.bct.javaservices;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCardLimits implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetCardLimits.class);

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
			LOG.error("Exception occured in GetCardLimits:::" + e.getMessage(), e);
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
			//Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
			//		Constants.DBX_DB_SERVICE_NAME, "dbxdb_cardLimit_get", false);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					"CRUDLayer", "dbxdb_cardConfigLimit_get", false);
			//limitDataset = result.getDatasetById("cardLimit");
			limitDataset = result.getDatasetById("cardConfigLimit");
			
			if (null != limitDataset) {
				ServicesManager sm = request.getServicesManager();
				ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
				String HBL_IMAGES_APP_URL = paramHelper.getServerProperty("HBL_IMAGES_APP_URL");
				List<Record> cardRecords = limitDataset != null ? limitDataset.getAllRecords() : null;
				if (cardRecords.size() != 0) {
					for (Record record : cardRecords) {

							String cardType = record.getParamValueByName("cardType");
							String cardCategory = record.getParamValueByName("cardCategory");
							String cardImage = record.getParamValueByName("cardImage");
							String cardImageFinalUrl = HBL_IMAGES_APP_URL+""+cardImage;
							LOG.debug("cardType:" + cardType);
							LOG.debug("cardCategory:" + cardCategory);
							LOG.debug("cardImageFinalUrl:" + cardImageFinalUrl);

							LOG.debug("cardImage:" + cardImage);
							record.addParam("productName", cardCategory + " " + cardType);
							record.removeParamByName("cardImage");
							record.addParam("cardImage", cardImageFinalUrl);
					}
				}
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

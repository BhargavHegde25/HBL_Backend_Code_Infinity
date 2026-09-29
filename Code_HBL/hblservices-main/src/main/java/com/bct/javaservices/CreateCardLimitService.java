package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateCardLimitService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreateCardLimitService.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			if (checkLimitAvailibility(request)) {
				result.setParam(new Param("Message", "Selected category limit is already available!"));
				result.setParam(new Param("Status", "Success"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			} else if (createCardLimitInDB(request)) {
				result.setParam(new Param("Message", "Create card limit is failed!"));
				result.setParam(new Param("Status", "Failed"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			} else {
				result.setParam(new Param("Message", "Success"));
				result.setParam(new Param("Status", "Success"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			}
		} catch (Exception e) {
			LOG.error("Exception occured in CheckThirdpartyAuthStatus:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static boolean createCardLimitInDB(DataControllerRequest request)
			throws HttpCallException {
		boolean isInsertFailed = false;
		try {
			String id = UUID.randomUUID().toString();
			Map<String, Object> inputParams = new HashMap<>();

			String cardType = request.getParameter("cardType");
			String cardCategory = request.getParameter("cardCategory");
			String cardDescription = request.getParameter("cardDescription");
			String shortDescription = request.getParameter("shortDescription");
			String cardImage = request.getParameter("cardImage");
			String limitJson = HBLCommonUtility.createLimitJSON(request);
			//String cardLimits = request.getParameter("cardLimits");
			LOG.debug("limitJson : "+ limitJson);

			inputParams.put("id", id);
			inputParams.put("cardType", cardType);
			inputParams.put("cardCategory", cardCategory);
			inputParams.put("cardDescription", cardDescription);
			inputParams.put("cardLimits", limitJson);
			inputParams.put("cardImage", cardImage);
			inputParams.put("shortDescription", shortDescription);

			LOG.debug("BCT::createCardLimitInDB: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.CARD_LIMIT_CREATE)
					.withRequestParameters(inputParams).withServiceId("CRUDLayer")
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			LOG.debug("BCT::createCardLimitInDB: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isInsertFailed = true;
			} else {
				isInsertFailed = false;
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create CreateCardLimitService");
			return false;
		}

		return isInsertFailed;
	}
	
	
	
	public static boolean checkLimitAvailibility(DataControllerRequest request) {
		try {
			String cardType = request.getParameter("cardType");
			String cardCategory = request.getParameter("cardCategory");
			//cardType = cardType.replaceAll("[^a-zA-Z]+", "%");
			//cardCategory = cardCategory.replaceAll("[^a-zA-Z]+", "%");
			LOG.debug("cardType ##:"+ cardType);
			LOG.debug("cardCategory ##:"+ cardCategory);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "cardType eq" +"'" +cardType + "'"+" and " + "cardCategory eq" +"'" +cardCategory+"'");
			//request.addRequestParam_("$filter", "cardType eq" + " " + "'" +cardType + "'"+" AND " + "cardCategory eq" + " " +"'" +cardCategory+"'");

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					"CRUDLayer", HBLURLConstants.CARD_LIMIT_GET, false);
			Dataset limitDataset = result.getDatasetById("cardConfigLimit");
			if (null != limitDataset && limitDataset.getAllRecords().size()> 0) {
				return true;
			} else {
				LOG.debug("Else checkLimitAvailibility:");
				return false;
			}
		}catch(Exception e) {
			LOG.debug("Couldn't create checkLimitAvailibility"+ e);
			return false;
		}
		
	}

}

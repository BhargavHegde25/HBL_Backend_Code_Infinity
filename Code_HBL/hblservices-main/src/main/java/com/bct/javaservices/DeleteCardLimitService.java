package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class DeleteCardLimitService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(DeleteCardLimitService.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		LOG.debug("HBL:GetCardLimits :");
		String id = request.getParameter("id");
		Result limitRes = deleteCardLimits(request, id);
		
		return limitRes;
	}

	public Result deleteCardLimits(DataControllerRequest request, String id) throws ApplicationException {
		Result response = new Result();
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("id", id);
		LOG.debug("BCT::deleteCardLimits" + inputParams.toString());
		try {
			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.CARD_LIMIT_DELETE)
					.withRequestParameters(inputParams).withServiceId("CRUDLayer")
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			LOG.debug("BCT::deleteCardLimits: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			
			if (responseJSON.has("errmsg")) {
				response.setParam(new Param("Message", responseJSON.get("errmsg").toString()));
				response.setParam(new Param("Status", "Failed"));
			} else if (responseJSON.has("deletedRecords")) {
				//String count = responseJSON.getString("deletedRecords");
				response.setParam(new Param("Message", "Card limit deleted successfully!"));
				response.setParam(new Param("Status", "Success"));
			}
			response.setParam(new Param("opstatus", "0"));
			response.setParam(new Param("httpStatusCode", responseJSON.get("httpStatusCode").toString()));
			
		} catch (Exception e) {
			LOG.error("Exception caught while deleteCardLimits:" + e.toString());

		}
		return response;
	}

}

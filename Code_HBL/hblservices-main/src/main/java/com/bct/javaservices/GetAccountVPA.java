package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLCrossBorderConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountVPA implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetAccountVPA.class);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		String customerId = request.getParameter("customerId");
		String customerAccount = request.getParameter("customerAccount");
		
		String vpaDetails = getVAPDetails(customerAccount, customerId, request);
		LOG.debug("vpaDetails:"+ vpaDetails);
		result = convertToResult(vpaDetails,customerAccount,request);
		
		return result;
	}
	
	public static String getVAPDetails(String customerAccount, String customerId,DataControllerRequest request) {
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("customerId",customerId);
			requestParameters.put("customerAccount",customerAccount);
			LOG.debug("INPUT to BACKEND: "+new JSONObject(requestParameters).toString());
			return DBPServiceExecutorBuilder.builder().
					withServiceId(HBLCrossBorderConstants.VPA_SEVICE_ID).
					withObjectId(null).
					withOperationId(HBLCrossBorderConstants.GET_VPA_OPERATION).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
		}
		catch (Exception e) {
			LOG.debug("Caught exception at  getVAPDetails: ", e);
			return "{\"errormsg\":\""+e.getMessage()+"\"}";
		}
	}
	
	public static String createVPA(String customerAccount,DataControllerRequest request) {
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("customerAccount",customerAccount);
			LOG.debug("INPUT to BACKEND: "+new JSONObject(requestParameters).toString());
			return DBPServiceExecutorBuilder.builder().
					withServiceId(HBLCrossBorderConstants.VPA_SEVICE_ID).
					withObjectId(null).
					withOperationId(HBLCrossBorderConstants.CREATE_VPA_OPERATION).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
		}
		catch (Exception e) {
			LOG.debug("Caught exception at  createVPA: ", e);
			return "{\"errormsg\":\""+e.getMessage()+"\"}";
		}
	}
	
	public static Result convertToResult(String vpaDetails, String customerAccount, DataControllerRequest request) {
		Result result = new Result();

		JSONObject jsonRsponse = new JSONObject(vpaDetails);
		String status = jsonRsponse.getString("status");
		if (status.equalsIgnoreCase("success")) {
			result.addParam("vpaId", jsonRsponse.getString("vpaId"));
			result.addParam("customerAccount", jsonRsponse.getString("customerAccount"));
			result.addParam("status", jsonRsponse.getString("status"));
			result.addParam("internationalInwardConsent", jsonRsponse.getString("internationalInwardConsent"));
			result.addParam("domesticInwardConsent", jsonRsponse.getString("domesticInwardConsent"));
			result.addParam("internationalOutwardConsent", jsonRsponse.getString("internationalOutwardConsent"));
			result.addParam("domesticOutwardConsent", jsonRsponse.getString("domesticOutwardConsent"));
		} else {
			/** Creating VPA for the account ***/
			String createVPAResult = createVPA(customerAccount, request);

			JSONObject createRes = new JSONObject(createVPAResult);
			String createVPAStatus = createRes.getString("status");
			// String createVPAopstatus = createRes.getString("opstatus");
			if (createVPAStatus.equalsIgnoreCase("success")) {
				/** Created VPA for the account and return to the INFINITY **/
				result.addParam("vpaId", createRes.getString("vpaId"));
				result.addParam("customerAccount", createRes.getString("id"));
				result.addParam("status", createRes.getString("status"));
				result.addParam("internationalInwardConsent", "PENDING");
				result.addParam("domesticInwardConsent", "PENDING");
				result.addParam("internationalOutwardConsent", "PENDING");
				result.addParam("domesticOutwardConsent", "PENDING");
			} else {
				/** No VPA for the selected account and creation of VPA also failing **/
				result.addParam("errcode", createRes.getString("errcode"));
				//result.addParam("errmsg", createRes.getString("message"));
				result.addParam("errmsg", "Unable to fetch VPA for the selected account");
				result.addParam("status", createRes.getString("status"));
				result.addParam("type", createRes.getString("type"));
			}
		}
		// "message": "No records matched the selection criteria",
		return result;
	}
}

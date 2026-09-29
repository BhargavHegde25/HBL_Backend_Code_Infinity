package com.bct.postprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.CardConstants;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.google.common.net.HttpHeaders;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class cardEMIConvertPostprocessor extends BasePostProcessor {
	static Logger logger = LogManager.getLogger(cardEMIConvertPostprocessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("cardEMIConvertPostprocessor Result:###" + ResultToJSON.convert(result));
			
			String respLabel = result.getParamValueByName("respLabel");
			String opstatus = result.getParamValueByName("opstatus");
			String httpStatusCode = result.getParamValueByName("httpStatusCode");
			
			String errmsg = result.getParamValueByName("respLabel");
			logger.debug("errmsg :###" + errmsg);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(errmsg);

			// Find and display matches
			while (matcher.find()) {
				result.addParam("errcode", result.getParamValueByName("respLabel"));
				result.addParam("errmsg", CardConstants.errMessage(errmsg));
			}
			
			logger.debug("cardEMI respLabel:###" + respLabel);
			logger.debug("cardEMI opstatus:###" + opstatus);
			logger.debug("cardEMI httpStatusCode:###" + httpStatusCode);
			
			if(errmsg.equalsIgnoreCase("000")) {
				logger.debug("Sending email to customer");
				triggerCardTopupEmail(request, result);
			}
			
			if(respLabel != null && respLabel != "") {
				result.addParam("responseMsg", CardConstants.errMessage(respLabel));
			}	
			result.addParam("opstatus", opstatus);
			result.addParam("respLabel", respLabel);
			result.addParam("httpStatusCode", httpStatusCode);
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

	public static void triggerCardTopupEmail(DataControllerRequest request, Result result)
			throws HttpCallException, AppRegistryException {
		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		logger.debug("customerId :###" + customerId);
		String email = Utils.customerEmailFromSession(request);
		logger.debug("email :###" + email);
		 String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request,
				 "FirstName");
		 String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request,
				 "LastName");
		 String customerName = FirstName + " " + LastName;
		
		String transactionId = request.getParameter("referenceNumber");
		String transactionAmount = request.getParameter("billingAmount");
		String currency = request.getParameter("transactionCurrency");
		String transactionDate = request.getParameter("transactionDate");
		String cardNumber = request.getParameter("pan");
		cardNumber = HBLCommonUtility.maskAccountNumber(cardNumber, 0, cardNumber.length() - 4, "X");
		String cardAccNumber = request.getParameter("bankActNum");
		cardAccNumber = HBLCommonUtility.maskAccountNumber(cardAccNumber, 0, cardAccNumber.length() - 4, "X");
		String instNumb = paramHelper.getServerProperty("CARD_EMI_INSTA_NUMBER"); //bank will change later based on no of EMI
		
		String emailTemplate = "CARD_EMI_REQ_EMAIL_TEMPLATE";

		logger.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("transactionId", transactionId);
		addContext.put("transactionAmount", transactionAmount);
		addContext.put("currency", currency);
		addContext.put("transactionDate", transactionDate);
		addContext.put("tenure", instNumb);
		addContext.put("cardNumber", cardNumber);
		addContext.put("cardAccNumber", cardAccNumber);
		addContext.put("customerName", customerName);
		addContext.put("customerId", customerId);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
	}
}

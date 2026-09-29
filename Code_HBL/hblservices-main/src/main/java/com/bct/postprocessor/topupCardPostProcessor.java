package com.bct.postprocessor;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class topupCardPostProcessor extends BasePostProcessor {
	private static final Logger logger = LogManager.getLogger(topupCardPostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {

			logger.debug("topupCardPostProcessor Result:###" + ResultToJSON.convert(result));
			String paymentReferenceId = request.getParameter("paymentReferenceId");
			String referenceId = request.getParameter("referenceId");
			String transactionId = request.getParameter("transactionId");
			
			logger.debug("paymentReferenceId :###" + paymentReferenceId);
			logger.debug("referenceId :###" + referenceId);
			logger.debug("transactionId :###" + transactionId);
			
			String errmsg = result.getParamValueByName("respCode_out");
			logger.debug("errmsg :###" + errmsg);
			
			String regex = "^(?!0{2,3}$)\\d+$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(errmsg);
			
			if(errmsg.equalsIgnoreCase("000")) {
				logger.debug("Sending email to customer");
				triggerCardTopupEmail(request, result);
			}

			// Find and display matches
			String reversalMsg = ". Sorry, Your Top-Up could not be processed at this time. If the amount has been debited, it will be refunded back to your account within 5 business days. We apologize for the inconvenience.";
			while (matcher.find()) {
				//result.addParam("errcode", result.getParamValueByName("respCode_out"));
				//result.addParam("errmsg", CardConstants.TopupErrMessage(errmsg) + reversalMsg);
				
				/** S2M Failure scenario, so calling reversal service ***/
				
				try {
					String REVERSE_TRANSACTION_JAVA_SERVICE = "HBLCreateExternalTransfers";
					String REVERSE_TRANSACTION_JAVA_OPEARATION = "reverseTransaction";
					Result result1 = new Result();
					HashMap<String, Object> inputParams = new HashMap<>();
					inputParams.put("paymentReferenceId", paymentReferenceId);
					inputParams.put("referenceId", referenceId);
					inputParams.put("transactionId", transactionId);
					request.addRequestParam_("paymentReferenceId", paymentReferenceId);
					request.addRequestParam_("referenceId", referenceId);
					request.addRequestParam_("transactionId", transactionId);
					result1 = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
							REVERSE_TRANSACTION_JAVA_SERVICE, REVERSE_TRANSACTION_JAVA_OPEARATION, true);

					JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result1));
					logger.debug("reverseTxResponse java :###" + reverseTxResponse);
					if (result1.getParamValueByName("dbpErrCode") != null || result1.getParamValueByName("dbpErrMsg") != null) {
						logger.debug("reverseTxResponse failed :###" );
						result1.getParamValueByName("status");
						logger.debug("reverseTxResponse failed status:###"+ result1.getParamValueByName("status"));
					}else {
						logger.debug("reverseTxResponse success :###" );
						logger.debug("reverseTxResponse success status:###"+ result1.getParamValueByName("status"));
						logger.debug("reverseTxResponse success message:###"+ result1.getParamValueByName("message"));
					}
					result.addParam("errcode", result.getParamValueByName("respCode_out"));
					if (CardConstants.TopupErrMessage(errmsg).equalsIgnoreCase("Topup Limit Error"))
						result.addParam("errmsg", "You have exceeded your remaining top up limit. Please enter lesser amount and try again" + reversalMsg);
					else
						result.addParam("errmsg", CardConstants.TopupErrMessage(errmsg) + reversalMsg);
				} catch (Exception e) {
					result.addParam("errcode", result.getParamValueByName("respCode_out"));
					result.addParam("errmsg", CardConstants.TopupErrMessage(errmsg) + reversalMsg);
				}
			}	
		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}
	
	public static void triggerCardTopupEmail(DataControllerRequest request, Result result)
			throws HttpCallException, AppRegistryException {
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		logger.debug("customerId :###" + customerId);
		String email = Utils.customerEmailFromSession(request);
		logger.debug("email :###" + email);
		String date =  new SimpleDateFormat("yyyyMMdd").format(new Date());
		logger.debug("date :###" + date);
		String topupCurrency = request.getParameter("topupCurrency");
		String currencyVal = (topupCurrency.equalsIgnoreCase("NPR")) ?"NPR" : "USD";
		String topupAmou = request.getParameter("topupAmou");

		String transactionId = request.getParameter("transactionId");
		String status = result.getParamValueByName("respLabel_out");
		String transactionDate = date;
		String transCurrency = currencyVal;
		String transactionAmount = topupAmou;
		String cardProduct = request.getParameter("cardProduct");//cardType
		String cardType = request.getParameter("cardType");//Card_Label
		String cardNumber = request.getParameter("cCardNumber");
		cardNumber = HBLCommonUtility.maskAccountNumber(cardNumber, 0, cardNumber.length() - 4, "X");
		String cardHolderName = request.getParameter("cardHolderName");
		String accountNumber = request.getParameter("frmAccNumber");
		accountNumber = HBLCommonUtility.maskAccountNumber(accountNumber, 0, accountNumber.length() - 4, "X");
		String debtorName = request.getParameter("frmAccHolderName");
		String currency = "NPR";
		String debitedAmount = request.getParameter("convertedAmount");
		
		String emailTemplate = "CARD_TOPUP_TEMPLATE";

		logger.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("transactionId", transactionId);
		addContext.put("status", status);
		addContext.put("transactionDate", transactionDate);
		addContext.put("transCurrency", transCurrency);
		addContext.put("transactionAmount", transactionAmount);
		addContext.put("cardProduct", cardProduct);
		addContext.put("cardType", cardType);
		addContext.put("cardNumber", cardNumber);
		addContext.put("cardHolderName", cardHolderName);
		addContext.put("accountNumber", accountNumber);
		addContext.put("debtorName", debtorName);
		addContext.put("currency", currency);
		addContext.put("debitedAmount", debitedAmount);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
	}
}

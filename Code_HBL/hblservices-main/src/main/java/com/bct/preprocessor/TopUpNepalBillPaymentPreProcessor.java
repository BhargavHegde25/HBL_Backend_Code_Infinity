package com.bct.preprocessor;

import java.net.URLEncoder;
import java.util.HashMap;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TopUpNepalBillPaymentPreProcessor implements DataPreProcessor2 {
	public static LoggerUtil logger = new LoggerUtil(TopUpNepalBillPaymentPreProcessor.class);

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
			   String userId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_USERID");
		       String password = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PWD");
		       String partnerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PARTNERID");
		       String resellerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_RESELLERID");
		       String amount=inputMap.get("amount")!=null?inputMap.get("amount").toString():"";
		       String txnId=inputMap.get("txnid")!=null?inputMap.get("txnid").toString():"";
		       String action=inputMap.get("Action")!=null?inputMap.get("Action").toString():"";
		       String mobileno=inputMap.get("mobileno")!=null?inputMap.get("mobileno").toString():"";
		       txnId= URLEncoder.encode(txnId);
		       action= URLEncoder.encode(action);
		       mobileno= URLEncoder.encode(mobileno);
		       amount= URLEncoder.encode(amount);
		       userId= URLEncoder.encode(userId);
		       password= URLEncoder.encode(password);
		       partnerId= URLEncoder.encode(partnerId);
		       resellerId= URLEncoder.encode(resellerId);
		       inputMap.put("txnId", txnId);
		       inputMap.put("action", action);
		       inputMap.put("mobileNo", mobileno);
		       inputMap.put("amount", amount);
		       inputMap.put("userid", userId);
		       inputMap.put("pwd", password);
		       inputMap.put("partnerid", partnerId);
		       inputMap.put("resellerid", resellerId);
		       logger.debug("HBL:TopupNepalConfirmBillpayPreProcessor:payload:"+inputMap);
		      // request.getHeaderMap().put(TemenosConstants.PARAM_AUTHORIZATION, Authorization);
			return true;
		}
	}

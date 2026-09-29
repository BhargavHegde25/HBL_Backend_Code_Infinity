package com.bct.javaservices;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.bct.postprocessor.NEABillpaymentPostprocessor;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetNPSBillersIntegrationURL implements JavaService2{
	final public static String PARTICIPANT_CODE="himalayan";
	final public static String LANDING_PAGE="biller-form";
	final public static String ANDROID="android";
	final public static String IOS="ios";
	final public static String LANG="en";
	final public static String DISPLAY_MODE="light";
	LoggerUtil logger = new LoggerUtil(GetNPSBillersIntegrationURL.class);
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String channel = inputParams.get("channelName");
		String platform = inputParams.get("platform");
		logger.debug("HBL::GetNPSBillersIntegrationURL:inputParams:"+inputParams);
		String baseUrl= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLERS_BASE_URL");
		String sessionId=generateSessionId();
		String web_type="iframe";
		logger.debug("HBL::GetNPSBillersIntegrationURL:sessionId:"+sessionId);
		Result result = new Result();
		if(StringUtils.isBlank(channel)) {
			result.addParam(new Param("dbpErrCode", "20001"));
			result.addParam(new Param("dbpErrMsg", "Invalid ChannelName"));
			return result;
		}
		if(channel.equalsIgnoreCase("web")) {
			channel="web";
		}
		else if(channel.equalsIgnoreCase("mobile")) {
			if(StringUtils.isBlank(platform)){
				result.addParam(new Param("dbpErrCode", "20002"));
				result.addParam(new Param("dbpErrMsg", "Invalid Platform"));
				return result;
			}
			else if(platform.equalsIgnoreCase("Android")) {
				platform=ANDROID;
			}
			else if(platform.equalsIgnoreCase("iOS")) {
				platform=IOS;
			} 
			channel="mob";
		}
		try {
		String token= generateToken(sessionId, channel);
		logger.debug("HBL::GetNPSBillersIntegrationURL:inputParams:"+token);
		String suffixUrl="";
		if(channel.equalsIgnoreCase("web")) {
			suffixUrl="/billers?participant_code="+PARTICIPANT_CODE+"&landing_page="+LANDING_PAGE+"&channel="+channel+"&web_type="+web_type+"&session_id="+sessionId+"&token="+token+"&biller_code=";
		}else if(channel.equalsIgnoreCase("mob")) {
			suffixUrl="/billers?participant_code="+PARTICIPANT_CODE+"&landing_page="+LANDING_PAGE+"&channel="+channel+"&os="+platform+"&session_id="+sessionId+"&token="+token+"&biller_code=";
		}
		String NpsBillersURL=baseUrl+suffixUrl;
		result.setParam(new Param("formFieldsURL", NpsBillersURL));
		result.setParam(new Param("opstatus", "0"));
		result.setParam(new Param("httpStatusCode", "200"));
		}catch (Exception e) {
			logger.error("Exception Occured at:HBL::GetNPSBillersIntegrationURL:Token generation"+e.getLocalizedMessage());
			result.addParam(new Param("dbpErrCode", "20003"));
			result.addParam(new Param("dbpErrMsg",e.getMessage()));
			result.setParam(new Param("httpStatusCode", "500"));
		}
		return result;
	}
	public String generateSessionId(){
		String sessionId = PARTICIPANT_CODE + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return sessionId;
	}
	public String generateToken(String sessionId, String channel) throws Exception {
		JSONObject payload = new JSONObject();
		payload.put("participant_code", PARTICIPANT_CODE);
		payload.put("session_id", sessionId);
		payload.put("channel", channel);
		String senderCertPath= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_PFX_FILE_PATH");
		String senderCertPass= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_PFX_FILE_PASSWORD");
		String receiverCertPath= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_RECEIVER_PUBLIC_KEY");
		String receiverCertPass="";
		 //receiverCertPass= EnvironmentConfigurationsHandler.getServerProperty("NPI_BILLER_RECEIVER_FILE_PASSWORD");
		JSONObject certDetails = new JSONObject();
		certDetails.put("senderCertPath", senderCertPath);
		certDetails.put("senderCertPass", senderCertPass);
		certDetails.put("receiverCertPath", receiverCertPath);
		certDetails.put("receiverCertPass", receiverCertPass);
		return JoseUtils.signAndEncryptPayload(payload.toString(), certDetails);//HBLTxnToken(payload.toString());	
	}

}

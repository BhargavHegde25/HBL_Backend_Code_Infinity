package com.bct.preprocessor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.CIPSTokenCacheManager;
import com.bct.utilities.CIPSTokenGenerator;
import com.bct.utilities.IntegrationType;
import com.bct.utilities.TokenCacheManager;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbx.BasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ConnectIPSAuthorizationPreprocessor extends TemenosBasePreProcessor {
	private static final Logger LOG = LogManager.getLogger(ConnectIPSAuthorizationPreprocessor.class);
	private static final String contentType="application/x-www-form-urlencoded";
	private static final String connectIPSserviceId=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_BASE_URL");
	private static final String operationId="oauth/token?";
	private String cipsAuthorization="";
	private String cips_apiuser="";
	private String cips_apipw="";
	 @Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
	            Result result)
	            throws Exception {
	/* String grantType = getKey(request, params, "grant_type");
	 StringBuilder sb = new StringBuilder();
	 String username = getKey(request, params, "username");
     String password = getKey(request, params, "password");
     String refreshToken = getKey(request, params, "refresh_token");
     String access_token = getKey(request, params, "refresh_token");
     */
     cipsAuthorization =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_AUTHORIZATION");
       cips_apiuser =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");
       cips_apipw =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_PASSWORD");
       String errorMsg;
       if (StringUtils.isBlank(cipsAuthorization) || StringUtils.isBlank(cips_apiuser) || StringUtils.isBlank(cips_apipw)) {
    	   errorMsg="Invalid CIPS details";
    	   result.addParam("dbpErrMsg", errorMsg);
    	   result.addParam("dbpErrCode", "30012");	
    	   result.addParam("errMsg", errorMsg);
    	   return false;
       }
       //String accessToken=getAccessToken(request);
       Map<String, Object> inputmap = new HashMap<String, Object>();
       Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("ContentType", contentType);
		headermap.put("Authorization", cipsAuthorization);
       StringBuilder sb = new StringBuilder();
		 sb.append("username").append("=").append(cips_apiuser);
		 sb.append("&");
		 sb.append("password").append("=").append(cips_apipw);
		 sb.append("&");
		sb.append("grant_type").append("=").append("password");
		inputmap.put("username", cips_apiuser);
		inputmap.put("password", cips_apipw);
		inputmap.put("grant_type", "password");
       //String accessToken = CIPSTokenCacheManager.getAccessToken(IntegrationType.INTERBANK_TRANSFER, connectIPSserviceId, operationId, inputmap, headermap,request);
		//String accessToken =CIPSTokenGenerator.getAccessToken(IntegrationType.INTERBANK_TRANSFER, connectIPSserviceId, operationId, inputmap, headermap, request);
		String accessToken = (String) MemoryManager.getDataFromCache(request, "CIPS_ACCESS_TOKEN");
		LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor:: access_token:" + accessToken);
       if (StringUtils.isBlank(accessToken)) {
    	   errorMsg="failed to generate CIPS accessToken";
    	   result.addParam("errMsg", errorMsg);
    	   result.addParam("dbpErrMsg", errorMsg);
    	   result.addParam("dbpErrCode", "30011");	
    	   return false;
       }
       LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor:: access_token1:" + accessToken);
       request.getHeaderMap().put("Content-Type", "application/json");
       request.getHeaderMap().put(TemenosConstants.PARAM_AUTHORIZATION, accessToken);
       request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, accessToken);
       //params.put(TemenosConstants.PARAM_AUTHORIZATION, accessToken);
     return true;
	}
	private String getKey(DataControllerRequest request, HashMap params, String key) {
        if (params.containsKey(key) && params.get(key) != null && params.get(key) != "")
            return params.get(key).toString();
        else if (request.getParameter(key) != null && request.getParameter(key) != "")
            return request.getParameter(key);
        return "";
    }
	public String getAccessToken(DataControllerRequest request){
		Map<String, Object> inputmap = new HashMap<String, Object>();
		Map<String, Object> inputmap1 = new HashMap<String, Object>();
		String access_token="";
		 StringBuilder sb = new StringBuilder();
		 sb.append("username").append("=").append(cips_apiuser);
		 sb.append("&");
		 sb.append("password").append("=").append(cips_apipw);
		 sb.append("&");
		sb.append("grant_type").append("=").append("password");
		inputmap.put("filter", sb.toString());
		Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("Content-Type", contentType);
		headermap.put("Authorization", cipsAuthorization);
		try {
			Result res = callInternalServiceAndGetResult(connectIPSserviceId, "getAccessToken", inputmap, headermap);
			if(res.getHttpStatusCodeParamValue().equals("200")){
				String refreshToken = res.getParamValueByName("refresh_token");
				LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: refreshToken:" + refreshToken);
				if(StringUtils.isNotBlank(refreshToken)) {
					 sb = new StringBuilder();
					 sb.append("grant_type").append("=").append("refresh_token");
					 sb.append("&");
					 sb.append("refresh_token").append("=").append(refreshToken);
					 inputmap1.put("filter", sb.toString());
					 Result response = callInternalServiceAndGetResult(connectIPSserviceId, "getAccessToken", inputmap1, headermap);
					 if(response.getHttpStatusCodeParamValue().equals("200")){
							 access_token = response.getParamValueByName("access_token");
							 LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: access_token:" + access_token);
			     }
			}
			}
		} catch (Exception e) {
			LOG.debug("Error occured in getRefreshToken", e.toString());
		}
	 return access_token;
	}
	private static Result callInternalServiceAndGetResult(String serviceid, String operationid,
			Map<String, Object> inputmap, Map<String, Object> headers) throws DBPApplicationException {
		LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: inputmap:" + inputmap.toString());
		LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: headers:" + headers.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		LOG.debug("HBL::ConnectIPSAuthorizationPreprocessor: callInternalServiceAndGetString: response:" + res.getHttpStatusCodeParamValue());

		return res;
	}

}

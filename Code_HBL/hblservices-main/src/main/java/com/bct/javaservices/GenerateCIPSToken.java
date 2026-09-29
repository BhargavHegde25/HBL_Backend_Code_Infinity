package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateCIPSToken implements JavaService2{
	private static final Logger LOG = LogManager.getLogger(GenerateCIPSToken.class);
	private static final Gson gson = new Gson();
	private static final int maxRetries=10;
	private static final String CIPS_ACCESS_TOKEN = "CIPS_ACCESS_TOKEN";
	private static final String CIPS_REFRESH_TOKEN_CACHE_KEY = "CIPS_REFRESH_TOKEN";
	private static final int REFRESH_TOKEN_EXPIRY_S = 12 * 59 * 60; // 11 hours 48 minutes 0 Sec
	private static final int CIPS_ACCESS_TOKEN_EXPIRY_S = 20 * 60; // 20 Minutes
	private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS).build();
	private static final String contentType="application/x-www-form-urlencoded";
	private String serviceURL=null;
	private int currentRetryCount=0;
	@Override
	public Object invoke(String method, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
		throws Exception {
		Result result = new Result();
		String serviceName=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_BASE_URL");
		String cipsAuthorization =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_AUTHORIZATION");
		String cips_apiuser =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");
		String cips_apipw =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_PASSWORD");
		String operationName ="oauth/token?";
		serviceURL = serviceName + operationName; 
		Map<String, Object> inputmap = new HashMap<String, Object>();
		inputmap.put("username", cips_apiuser);
		inputmap.put("password", cips_apipw);
		inputmap.put("Authorization", cipsAuthorization);
		currentRetryCount=0;
		try {
		String accessToken=generateAccessToken(inputmap, dcRequest);
		if(StringUtils.isNotBlank(accessToken))
		result.addParam(new Param("accessToken", accessToken));
		else
		result.addParam(new Param("errorMessage", "Failed to generate access token, please debug for more information."));
		}catch (Exception e) {
		result.addParam(new Param("dbpErrCode", "20002"));
		result.addParam(new Param("dbpErrMsg", e.getMessage()));
		}
		return result;
	}
	public String generateAccessToken( Map<String, Object> inputmap, DataControllerRequest dcRequest) throws Exception {
        if (currentRetryCount >= maxRetries) {
            LOG.error("Max retries reached.");
            return null;
        }
        String refreshToken =  (String) MemoryManager.getDataFromCache(dcRequest, CIPS_REFRESH_TOKEN_CACHE_KEY);
        if (StringUtils.isBlank(refreshToken)) {
        	LOG.debug("No refresh token in cache — fetching new one.");
            String newToken = getRefreshTokenFromCIPS(inputmap, dcRequest);
            if (newToken != null) {
                Thread.sleep(5000);
                currentRetryCount++;
                return generateAccessToken(inputmap, dcRequest);
            }
            return null;
        }
        else{LOG.debug("Refresh token available in cache."+refreshToken);}
        String accessToken=getAccessTokenUsingRefreshToken(inputmap, refreshToken, dcRequest);
        return accessToken;
    }

	private String getRefreshTokenFromCIPS(Map<String, Object> inputMap, DataControllerRequest dcRequest) {
		String refreshToken = null;
		MediaType mediaType = MediaType.parse(contentType);
		String requestBody = "username=" + inputMap.get("username") + "&password=" + inputMap.get("password")+ "&grant_type=password";
		RequestBody body = RequestBody.create(requestBody, mediaType);
		Request request = new Request.Builder().url(serviceURL).method("POST", body)
				.addHeader("Content-Type", contentType)
				.addHeader("Authorization", inputMap.get("Authorization").toString()).build();
		Response httpResponse;
		try {
			httpResponse = client.newCall(request).execute();
			String responseBody = httpResponse.body().string();
			LOG.debug("GenerateCIPSToken:getRefreshTokenToCache:NCHL Service refreshTokenResponse:" + responseBody);
			JSONObject refreshTokenResponse = new JSONObject(responseBody);
			if (httpResponse.isSuccessful() && httpResponse.code()==200) {
				refreshToken = refreshTokenResponse.getString("refresh_token");
				if (StringUtils.isNotBlank(refreshToken)) {
					MemoryManager.saveIntoCache(CIPS_REFRESH_TOKEN_CACHE_KEY, refreshToken, REFRESH_TOKEN_EXPIRY_S);
					LOG.debug("GenerateCIPSToken:getRefreshTokenToCache:refreshToken stored into cache:" + refreshToken);
				}
			}
		} catch (Exception e) {
			LOG.error("Exception Occured in calling refreshToken service", e);
		}
		return refreshToken;
	}
	 private String getAccessTokenUsingRefreshToken(Map<String, Object> inputMap, String refreshToken, DataControllerRequest dcRequest) throws Exception {
		 	String accessToken = null;
	        MediaType mediaType = MediaType.parse(contentType);
	        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
	        RequestBody body = RequestBody.create(requestBody, mediaType);
	        
	        Request request = new Request.Builder().url(serviceURL).method("POST", body)
	                .addHeader("Content-Type", contentType)
	                .addHeader("Authorization", inputMap.get("Authorization").toString())
	                .build();

	        Response httpResponse = client.newCall(request).execute();
	        String responseBody = httpResponse.body().string();
	        LOG.debug("GenerateCIPSToken:getAccessTokenUsingRefreshToken:NCHL Service accessTokenResponse:" + responseBody);
	        
	        if (httpResponse.code() != 200) {
	        	LOG.debug("GenerateCIPSToken:Refresh token got expired, and getRefreshTokenFromCIPS:");
	        	String newToken = getRefreshTokenFromCIPS(inputMap, dcRequest);
	            if (newToken != null) {
	                Thread.sleep(15000);
	                currentRetryCount++;
	                return generateAccessToken(inputMap,dcRequest);
	            }
	            return null;
	        }
	        
	        JSONObject accessTokenResponse = new JSONObject(responseBody);
	        if(accessTokenResponse.has("access_token")) {
	        accessToken="Bearer "+accessTokenResponse.getString("access_token");
			MemoryManager.saveIntoCache(CIPS_ACCESS_TOKEN, accessToken, CIPS_ACCESS_TOKEN_EXPIRY_S);
			LOG.debug("GenerateCIPSToken:getRefreshTokenToCache:accessToken stored into cache:" + accessToken);
	        }
	        return accessToken;
	    }
}

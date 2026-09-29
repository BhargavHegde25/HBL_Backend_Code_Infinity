package com.bct.utilities;

import com.bct.utilities.NCHLNpixTransactionModel.TokenResponse;
import com.google.gson.Gson;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class CIPSTokenGenerator {
	  private static final Gson gson = new Gson();
	    private static final Logger LOG = LogManager.getLogger(CIPSTokenCacheManager.class);
	    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS).build();
	 // Constants for buffer times
	    private static final long ACCESS_TOKEN_RENEWAL_BUFFER_MS = 20 * 1000; // 20 seconds buffer
	    private static final long REFRESH_TOKEN_MAX_LIFE_MS = 12 * 60 * 60 * 1000; // 12 hours
	    public static synchronized String getAccessToken(IntegrationType type, String serviceName,
	            String operationName, Map<String, Object> inputmap, Map<String, Object> headerMap, DataControllerRequest request) throws Exception {
	        try {
	            ServicesManager sm = request.getServicesManager();
	            ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
	            long RENEWAL_BUFFER_MS = Long.parseLong(paramHelper.getServerProperty("CIPS_RENEWAL_BUFFER_MS"));
	            LOG.debug("RENEWAL_BUFFER_MS: ##" + RENEWAL_BUFFER_MS);
	            
	            String backendApiUri = serviceName + operationName;
	            LOG.debug("CIPSTokenCacheManager:backendApiUri##" + backendApiUri);
	            
	            // Get from distributed cache using MemoryManager
	            String cacheKey = generateCacheKey(type);
	            CIPSTokenCache cache = getFromCache(cacheKey, request);
	            
	            long now = System.currentTimeMillis();
	            LOG.debug("CIPSTokenCacheManager:cache##" + cache);
	            LOG.debug("CIPSTokenCacheManager:Current time ##" + now);

	            /**
	             * Check if valid access token exists in cache and hasn't reached renewal buffer
	             */
	            if (cache != null && cache.accessToken != null && (cache.accessTokenExpiry - ACCESS_TOKEN_RENEWAL_BUFFER_MS) > now) {
	                LOG.debug("Access Token from Cache: ##" + cache.accessToken);
	                return "Bearer " + cache.accessToken;
	            }

	            /**
	             * Check if refresh token is valid and use it to get new access token
	             */
	            if (cache != null && cache.refreshToken != null && (cache.refreshTokenExpiry - RENEWAL_BUFFER_MS) > now) {
	                try {
	                    CIPSTokenCache refreshed = getAccessTokenUsingRefreshToken(backendApiUri, inputmap, headerMap, cache.refreshToken);
	                    // Preserve the original refresh token expiry and issued time
	                    refreshed.refreshTokenExpiry = cache.refreshTokenExpiry;
	                    refreshed.refreshTokenIssuedAt = cache.refreshTokenIssuedAt;
	                    
	                    // Store in distributed cache with proper expiry
	                    saveToCache(cacheKey, refreshed, calculateCacheExpiry(refreshed, now), request);
	                    LOG.debug("New Access Token using refresh token: ##" + refreshed.accessToken);
	                    return "Bearer " + refreshed.accessToken;
	                } catch (TokenRefreshException e) {
	                    if (e.isUnauthorized()) {
	                        LOG.debug("Refresh token expired (401), getting new refresh token");
	                        // Refresh token is invalid, get completely new tokens
	                        return getNewRefreshTokenAndAccessToken(type, backendApiUri, inputmap, headerMap, request, cacheKey, now);
	                    } else {
	                        throw e;
	                    }
	                }
	            }

	            /**
	             * No valid tokens in cache, get completely new tokens
	             */
	            return getNewRefreshTokenAndAccessToken(type, backendApiUri, inputmap, headerMap, request, cacheKey, now);
	            
	        } catch (Exception e) {
	            LOG.error("Exception occurred in CIPSTokenCacheManager:getAccessToken ##" + e.getMessage(), e);
	            return null;
	        }
	    }

	    /**
	     * Get completely new refresh token and access token
	     */
	    private static String getNewRefreshTokenAndAccessToken(IntegrationType type, String backendApiUri, 
	            Map<String, Object> inputmap, Map<String, Object> headerMap, DataControllerRequest request, 
	            String cacheKey, long now) throws Exception {
	        
	        CIPSTokenCache newTokens = getRefreshTokenToCache(backendApiUri, inputmap, headerMap);
	        newTokens.refreshTokenExpiry = now + REFRESH_TOKEN_MAX_LIFE_MS; // 12 hours from now
	        newTokens.refreshTokenIssuedAt = now;
	        
	        // Store in distributed cache with proper expiry
	        saveToCache(cacheKey, newTokens, calculateCacheExpiry(newTokens, now), request);
	        LOG.debug("New Refresh Token: ##" + newTokens.refreshToken);
	        LOG.debug("New Access Token with new refresh token: ##" + newTokens.accessToken);
	        return "Bearer " + newTokens.accessToken;
	    }

	    /**
	     * Get access token using refresh token (with 401 handling)
	     */
	    private static CIPSTokenCache getAccessTokenUsingRefreshToken(String backendApiUri, Map<String, Object> inputMap, 
	            Map<String, Object> headerMap, String refreshToken) throws Exception {
	        
	        Map<String, String> response = new HashMap<String, String>();
	        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
	        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
	        RequestBody body = RequestBody.create(requestBody, mediaType);
	        
	        LOG.debug("CIPSTokenCacheManager:getAccessTokenUsingRefreshToken:NCHL Service accessTokenRequest:" + requestBody);
	        Request request = new Request.Builder().url(backendApiUri).method("POST", body)
	                .addHeader("Content-Type", headerMap.get("ContentType").toString())
	                .addHeader("Authorization", headerMap.get("Authorization").toString())
	                .build();

	        Response httpResponse = client.newCall(request).execute();
	        String responseBody = httpResponse.body().string();
	        LOG.debug("CIPSTokenCacheManager:getAccessTokenUsingRefreshToken:NCHL Service accessTokenResponse:" + responseBody);
	        
	        // Handle 401 Unauthorized - refresh token is invalid
	        if (httpResponse.code() == 401) {
	            LOG.debug("Received 401 Unauthorized for refresh token call");
	            throw new TokenRefreshException("Refresh token invalid or expired", true);
	        }
	        
	        TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
	        
	        if (httpResponse.isSuccessful()) {
	            String rToken = tokenResponse.getRefreshToken();
	            String aToken = tokenResponse.getAccessToken();
	            String expiresIn = tokenResponse.getTokenExpiry(); // This should be 203 seconds from API

	            response.put("access_token", aToken);
	            response.put("refresh_token", rToken != null ? rToken : refreshToken); // Use new refresh token if provided, else keep old one
	            response.put("expires_in", expiresIn);

	        } else {
	            String responseCode = tokenResponse.getError();
	            String responseMessage = tokenResponse.getErrorDescription();
	            int statusCode = httpResponse.code();
	            response.put("responseCode", responseCode);
	            response.put("responseMessage", responseMessage);
	            response.put("statusCode", statusCode + "");
	            
	            // If we get 401 in error response, throw specific exception
	            if (statusCode == 401 || statusCode == 400) {
	                throw new TokenRefreshException("Refresh token invalid or expired: " + responseMessage, true);
	            }
	        }

	        return buildTokenCacheFromResponse(response);
	    }

	    /**
	     * Get new refresh token and initial access token
	     */
	    private static CIPSTokenCache getRefreshTokenToCache(String backendApiUri, Map<String, Object> inputMap, 
	            Map<String, Object> headerMap) throws Exception {
	        
	        Map<String, String> response = new HashMap<String, String>();
	        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
	        String requestBody = "username=" + inputMap.get("username") + "&password=" + inputMap.get("password") + "&grant_type=password";
	        RequestBody body = RequestBody.create(requestBody, mediaType);
	        
	        LOG.debug("CIPSTokenCacheManager:getRefreshTokenToCache:NCHL Service refreshTokenRequest:" + requestBody);
	        Request request = new Request.Builder().url(backendApiUri).method("POST", body)
	                .addHeader("Content-Type", headerMap.get("ContentType").toString())
	                .addHeader("Authorization", headerMap.get("Authorization").toString())
	                .build();

	        Response httpResponse = client.newCall(request).execute();
	        String responseBody = httpResponse.body().string();
	        LOG.debug("CIPSTokenCacheManager:getRefreshTokenToCache:NCHL Service refreshTokenResponse:" + responseBody);
	        
	        TokenResponse refreshTokenResponse = gson.fromJson(responseBody, TokenResponse.class);
	        
	        if (httpResponse.isSuccessful()) {
	            String rToken = refreshTokenResponse.getRefreshToken();
	            String aToken = refreshTokenResponse.getAccessToken();
	            String expiresIn = refreshTokenResponse.getTokenExpiry(); // This should be 203 seconds from API

	            response.put("access_token", aToken);
	            response.put("refresh_token", rToken);
	            response.put("expires_in", expiresIn);

	        } else {
	            String responseCode = refreshTokenResponse.getError();
	            String responseMessage = refreshTokenResponse.getErrorDescription();
	            int statusCode = httpResponse.code();
	            response.put("responseCode", responseCode);
	            response.put("responseMessage", responseMessage);
	            response.put("statusCode", statusCode + "");
	        }
	        
	        return buildTokenCacheFromResponse(response);
	    }

	    /**
	     * Build token cache from API response with proper expiry calculation
	     */
	    private static CIPSTokenCache buildTokenCacheFromResponse(Map<String, String> response) {
	        long now = System.currentTimeMillis();
	        CIPSTokenCache cache = new CIPSTokenCache();
	        cache.accessToken = response.get("access_token");
	        cache.refreshToken = response.get("refresh_token");
	        
	        // Calculate access token expiry based on expires_in (203 seconds)
	        long expiresInMs = Long.parseLong(response.get("expires_in")) * 1000; // Convert to milliseconds
	        cache.accessTokenExpiry = now + expiresInMs;
	        
	        // Refresh token expiry is set by caller (12 hours from initial issue)
	        cache.responseCode = response.get("responseCode");
	        cache.responseMessage = response.get("responseMessage");
	        cache.statusCode = response.get("statusCode");
	        
	        LOG.debug("Built token cache - Access token expires at: " + cache.accessTokenExpiry + 
	                 " (in " + (expiresInMs/1000) + " seconds), Current time: " + now);
	        
	        return cache;
	    }

	    /**
	     * Cache helper methods using MemoryManager
	     */
	    private static String generateCacheKey(IntegrationType type) {
	        return "CIPS_TOKEN_" + type.name();
	    }

	    private static CIPSTokenCache getFromCache(String cacheKey, DataControllerRequest request) {
	        try {
	            Object cachedObject = MemoryManager.getDataFromCache(request, cacheKey);
	            LOG.debug("getFromCache:cachedObject::"+ cachedObject);
	            if(cachedObject!= null) {
	            	 JSONObject jsonObj = new JSONObject(cachedObject.toString());
	                return gson.fromJson(jsonObj.toString(), CIPSTokenCache.class);
	            }
	        } catch (Exception e) {
	            LOG.error("Error getting from cache for key: " + cacheKey, e);
	        }
	        return null;
	    }

	    private static void saveToCache(String cacheKey, CIPSTokenCache cache, int expiryInSeconds, DataControllerRequest request) {
	        try {
	           // String serializedCache = gson.toJson(cache);
	            MemoryManager.insertDataIntoCache(request, cache, cacheKey, expiryInSeconds);
	            LOG.debug("Successfully stored token in cache with key: " + cacheKey + ", expiry: " + expiryInSeconds + "s");
	        } catch (Exception e) {
	            LOG.error("Error storing in cache for key: " + cacheKey, e);
	        }
	    }

	    /**
	     * Calculate cache expiry - set to refresh token expiry + buffer
	     */
	    private static int calculateCacheExpiry(CIPSTokenCache cache, long currentTime) {
	        long timeUntilRefreshExpiry = (cache.refreshTokenExpiry - currentTime) / 1000;
	        // Add buffer of 5 minutes to ensure we don't lose tokens prematurely
	        return Math.max((int) timeUntilRefreshExpiry + 300, 3600); // At least 1 hour
	    }

	    /**
	     * Custom exception for token refresh failures
	     */
	    private static class TokenRefreshException extends Exception {
	        private final boolean unauthorized;
	        
	        public TokenRefreshException(String message, boolean unauthorized) {
	            super(message);
	            this.unauthorized = unauthorized;
	        }
	        
	        public boolean isUnauthorized() {
	            return unauthorized;
	        }
	    }

	    /**
	     * Method to clear cache if needed
	     */
	    public static void clearTokenCache(IntegrationType type, DataControllerRequest request) {
	        try {
	            String cacheKey = generateCacheKey(type);
	            MemoryManager.removeFromCache(cacheKey);
	            LOG.debug("Cleared token cache for type: " + type);
	        } catch (Exception e) {
	            LOG.error("Error clearing token cache for type: " + type, e);
	        }
	    }

}

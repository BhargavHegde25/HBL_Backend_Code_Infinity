package com.hbl.productservicesExtn.utills;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.google.gson.Gson;
import com.hbl.productservicesExtn.utills.AccessTokenTransactionModel.TokenResponse;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CIPSTokenCacheManager {
	private static final Map<IntegrationType, CIPSTokenCache> tokenMap = new ConcurrentHashMap<>();
	private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS).build();
	private static final Gson gson = new Gson();
	private static final Logger LOG = LogManager.getLogger(CIPSTokenCacheManager.class);

	/** Constants for token expiration times 
	private static final long ACCESS_TOKEN_EXPIRY_MS = 300 * 1000; // 5 minutes 
	private static final long REFRESH_TOKEN_INCREMENT_MS = 300 * 1000; // 5 minutes increment
	private static final long REFRESH_TOKEN_MAX_LIFE_MS = 12 * 60 * 60 * 1000; // 12 hours
	private static final long RENEWAL_BUFFER_MS = 5 * 1000; // 5 seconds buffer **/

	public static synchronized String getAccessToken(IntegrationType type, String serviceName,
			String operationName,  Map<String, Object> inputmap, Map<String, Object> headerMap, DataControllerRequest request) throws Exception {
		try {
		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		long ACCESS_TOKEN_EXPIRY_MS = Long.parseLong(paramHelper.getServerProperty("CIPS_ACCESS_TOKEN_EXPIRY_MS"));
		LOG.debug("ACCESS_TOKEN_EXPIRY_MS: ##"+ ACCESS_TOKEN_EXPIRY_MS);
		long REFRESH_TOKEN_INCREMENT_MS = Long.parseLong(paramHelper.getServerProperty("CIPS_REFRESH_TOKEN_INCREMENT_MS"));
		LOG.debug("REFRESH_TOKEN_INCREMENT_MS: ##"+ REFRESH_TOKEN_INCREMENT_MS);
		long REFRESH_TOKEN_MAX_LIFE_MS = Long.parseLong(paramHelper.getServerProperty("CIPS_REFRESH_TOKEN_MAX_LIFE_MS"));
		LOG.debug("REFRESH_TOKEN_MAX_LIFE_MS: ##"+ REFRESH_TOKEN_MAX_LIFE_MS);
		long RENEWAL_BUFFER_MS = Long.parseLong(paramHelper.getServerProperty("CIPS_RENEWAL_BUFFER_MS"));
		LOG.debug("RENEWAL_BUFFER_MS: ##"+ RENEWAL_BUFFER_MS);
		String backendApiUri= serviceName+operationName;
		
		CIPSTokenCache cache = tokenMap.get(type);
		long now = System.currentTimeMillis();

		/**
		 * This condition is checking whether accessToken is available in the cache. If
		 * it is, the function will return the token directly from the cache instead of
		 * generating or fetching a new one.
		 */
		LOG.debug("CIPSTokenCacheManager:cache##"+cache);
		LOG.debug("CIPSTokenCacheManager:Current time ##"+now);
		if (cache != null && cache.accessToken != null && (cache.accessTokenExpiry - RENEWAL_BUFFER_MS) > now) {
			LOG.debug("Access Token from Cache: ##"+ cache.accessToken);
			return "Bearer " +cache.accessToken;
		}

		/**
		 * This condition is checking whether refreshToken is available in the cache. If
		 * it is, the function will call the Access token API and storing accessToken &
		 * refreshToken in the cache and returning the access token. Also updating
		 * refresh token expire in the cache.
		 * 
		 */
		if (cache != null && cache.refreshToken != null && (cache.refreshTokenExpiry - RENEWAL_BUFFER_MS) > now) {
			CIPSTokenCache refreshed = getAccessTokenToCache(backendApiUri, inputmap, headerMap, cache.refreshToken, ACCESS_TOKEN_EXPIRY_MS);
			refreshed.refreshTokenExpiry = Math.min(now + REFRESH_TOKEN_INCREMENT_MS,
					cache.refreshTokenIssuedAt + REFRESH_TOKEN_MAX_LIFE_MS);
			tokenMap.put(type, refreshed);
			LOG.debug("New Access Token as refresh token available ##"+ refreshed.accessToken);
			return "Bearer " +refreshed.accessToken;
		}

		/**
		 * Below block will execute, if there is no valid access token and refresh token
		 * in the cache. Calling the Refresh token API and storing accessToken &
		 * refreshToken in the cache and returning the access token. Also updating
		 * refresh token expire in the cache.
		 * 
		 */
		CIPSTokenCache refreshed = getRefreshTokenToCache(backendApiUri, inputmap, headerMap, ACCESS_TOKEN_EXPIRY_MS);
		refreshed.refreshTokenExpiry = Math.min(now + REFRESH_TOKEN_INCREMENT_MS, now + REFRESH_TOKEN_MAX_LIFE_MS);
		refreshed.refreshTokenIssuedAt = now;
		tokenMap.put(type, refreshed);
		LOG.debug("New Refresh Token as refresh token expired ##"+ refreshed.refreshToken);
		LOG.debug("New Access Token with new refresh token ##"+ refreshed.accessToken);
		return "Bearer " +refreshed.accessToken;
		}catch (Exception e) {
			LOG.debug("Exception occured in CIPSTokenCacheManager:getAccessToken ##"+ e.getMessage());
			return null;
		}
	}

	/**
	 * Below method is using to call access token NCHL API and returns the
	 * accessToken and refreshToken and storing in the cache
	 **/

	private static CIPSTokenCache getAccessTokenToCache(String backendApiUri,  Map<String, Object> inputMap, Map<String, Object> headerMap, String refreshToken,
			 long ACCESS_TOKEN_EXPIRY_MS) throws Exception {
		Map<String, String> response = new HashMap<String, String>();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		//String requestBody = "username=" + inputMap.get("username") + "&password=" + inputMap.get("password") + "&grant_type=password";
		String requestBody = "grant_type=" + "refresh_token" + "&refresh_token=" + refreshToken;
		RequestBody body = RequestBody.create(requestBody, mediaType);
		LOG.debug("HBL:BillPayTransaction:getAccessTokenToCache:NCHL Service accessTokenRequest:" + body.toString());
		Request request = new Request.Builder().url(backendApiUri).method("POST", body)
				.addHeader("Content-Type", headerMap.get("ContentType").toString())
				.addHeader("Authorization", headerMap.get("Authorization").toString()).build();

		Response response1 = client.newCall(request).execute();
		String responseBody = response1.body().string();
		TokenResponse tokenResponse = gson.fromJson(responseBody, com.hbl.productservicesExtn.utills.AccessTokenTransactionModel.TokenResponse.class);
		LOG.debug("HBL:BillPayTransaction:getAccessTokenToCache:NCHL Service accessTokenResponse:" + responseBody);
		if (response1.isSuccessful()) {
			String rToken = tokenResponse.getRefreshToken();
			String aToken = tokenResponse.getAccessToken();

			response.put("access_token", aToken);
			response.put("refresh_token", rToken);

		} else {
			String responseCode = tokenResponse.getError();
			String responseMessage = tokenResponse.getErrorDescription();
			int statusCode = response1.code();
			response.put("responseCode", responseCode);
			response.put("responseMessage", responseMessage);
			response.put("statusCode", statusCode + "");
		}

		return buildTokenCacheFromResponse(response,ACCESS_TOKEN_EXPIRY_MS);
	}

	/**
	 * Below method is using to call refreshToken NCHL API and returns the
	 * accessToken, refreshToken and refresh token expiry to storing in the cache
	 **/

	public static CIPSTokenCache getRefreshTokenToCache(String backendApiUri, Map<String, Object> inputMap, Map<String, Object> headerMap, long ACCESS_TOKEN_EXPIRY_MS) throws Exception {
		Map<String, String> response = new HashMap<String, String>();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		String requestBody = "username=" + inputMap.get("username") + "&password=" + inputMap.get("password") + "&grant_type=password";
		RequestBody body = RequestBody.create(requestBody, mediaType);
		LOG.debug("HBL:BillPayTransaction:getRefreshTokenToCache:NCHL Service refreshTokenRequest:" + body.toString());
		Request request = new Request.Builder().url(backendApiUri).method("POST", body)
				.addHeader("Content-Type",  headerMap.get("ContentType").toString())
				.addHeader("Authorization", headerMap.get("Authorization").toString()).build();

		Response response1 = client.newCall(request).execute();
		String responseBody = response1.body().string();
		LOG.debug("HBL:BillPayTransaction:getRefreshTokenToCache:NCHL Service refreshTokenResponse:" + responseBody);
		TokenResponse refreshTokenResponse = gson.fromJson(responseBody, TokenResponse.class);
		if (response1.isSuccessful()) {

			String rToken = refreshTokenResponse.getRefreshToken();
			String refreshTokenExpiry = refreshTokenResponse.getTokenExpiry();
			//CIPSTokenCache accessTokenResponse = getAccessTokenToCache(backendApiUri, inputMap, headerMap, rToken, ACCESS_TOKEN_EXPIRY_MS);
			//String aToken = accessTokenResponse.accessToken;
			String aToken = refreshTokenResponse.getAccessToken();
			response.put("access_token", aToken);
			response.put("refresh_token", rToken);
			response.put("refreshTokenExpiry", refreshTokenExpiry);

		} else {
			String responseCode = refreshTokenResponse.getError();
			String responseMessage = refreshTokenResponse.getErrorDescription();
			int statusCode = response1.code();
			response.put("responseCode", responseCode);
			response.put("responseMessage", responseMessage);
			response.put("statusCode", statusCode + "");
		}
		return buildTokenCacheFromResponse(response, ACCESS_TOKEN_EXPIRY_MS);
	}

	private static CIPSTokenCache buildTokenCacheFromResponse(Map<String, String> response, long ACCESS_TOKEN_EXPIRY_MS) {
		long now = System.currentTimeMillis();
		CIPSTokenCache cache = new CIPSTokenCache();
		cache.accessToken = (String) response.get("access_token");
		cache.refreshToken = (String) response.get("refresh_token");
		cache.accessTokenExpiry = now + (ACCESS_TOKEN_EXPIRY_MS);
		cache.refreshTokenExpiry = now + (ACCESS_TOKEN_EXPIRY_MS);
		cache.responseCode = (String) response.get("responseCode");
		cache.responseMessage = (String) response.get("responseMessage");
		cache.statusCode = (String) response.get("statusCode");
		return cache;
	}

	private static String getTokenUrlForType(IntegrationType type) {
		switch (type) {
		case INTERBANK_TRANSFER:
			return "https://api.partner.com/interbank/token";
		case BILL_MERCHANT_PAYMENT:
			return "https://api.partner.com/payment/token";
		case QR_PAYMENT:
			return "https://api.partner.com/qr/token";
		case CROSS_BORDER_CONSENT:
			return "https://api.partner.com/crossborder/token";
		case DOMESTIC_TRANSFER:
			return "https://api.partner.com/domestic/token";
		default:
			throw new IllegalArgumentException("Unknown integration type: " + type);
		}
	}

	private static String getRefreshUrlForType(IntegrationType type) {
		switch (type) {
		case INTERBANK_TRANSFER:
			return "https://api.partner.com/interbank/refresh";
		case BILL_MERCHANT_PAYMENT:
			return "https://api.partner.com/payment/refresh";
		case QR_PAYMENT:
			return "https://api.partner.com/qr/refresh";
		case CROSS_BORDER_CONSENT:
			return "https://api.partner.com/crossborder/refresh";
		case DOMESTIC_TRANSFER:
			return "https://api.partner.com/domestic/refresh";
		default:
			throw new IllegalArgumentException("Unknown integration type: " + type);
		}
	}

	private static String getClientId(IntegrationType type) {
		switch (type) {
		case INTERBANK_TRANSFER:
			return "int-client-id";
		case BILL_MERCHANT_PAYMENT:
			return "pay-client-id";
		case QR_PAYMENT:
			return "qr-client-id";
		case CROSS_BORDER_CONSENT:
			return "cross-client-id";
		case DOMESTIC_TRANSFER:
			return "domestic-client-id";
		default:
			throw new IllegalArgumentException("Unknown integration type");
		}
	}

	private static String getClientSecret(IntegrationType type) {
		switch (type) {
		case INTERBANK_TRANSFER:
			return "int-secret";
		case BILL_MERCHANT_PAYMENT:
			return "pay-secret";
		case QR_PAYMENT:
			return "qr-secret";
		case CROSS_BORDER_CONSENT:
			return "cross-secret";
		case DOMESTIC_TRANSFER:
			return "domestic-secret";
		default:
			throw new IllegalArgumentException("Unknown integration type");
		}
	}

}

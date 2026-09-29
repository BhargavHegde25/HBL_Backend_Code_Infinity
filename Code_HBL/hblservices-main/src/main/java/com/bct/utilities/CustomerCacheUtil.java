package com.bct.utilities;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.infinity.dbx.temenos.user.UserConstants;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;

public class CustomerCacheUtil {

	private static final Logger logger = LogManager.getLogger(CustomerCacheUtil.class);

	/**
	 * Fetches customer details from cache. Returns empty object if not present.
	 */
	public static JSONObject getCustomerDetailsFromCache(String customerId) {
		logger.debug("Entering getCustomerDetailsFromCache for customerId:::", customerId);
		try {
			if (StringUtils.isBlank(customerId)) {
				throw new IllegalArgumentException("Customer ID cannot be blank");
			}

			String cacheKey = "customerDetails_" + customerId;
			logger.debug("Looking for cache with key:::", cacheKey);
			String cachedData = (String) MemoryManager.getFromCache(cacheKey);

			if (StringUtils.isNotBlank(cachedData)) {
				logger.info("Cache hit for customerId:::", customerId);
				return new JSONObject(cachedData);
			} else {
				logger.info("Cache miss for customerId:::", customerId);
			}
		} catch (Exception e) {
			logger.error("Error while retrieving customer details from cache", e);
		}

		return new JSONObject();
	}

	/**
	 * Fetches customer details from cache if available, else calls service and
	 * caches the result.
	 */
	public static JSONObject fetchAndCacheCustomerDetails(String customerId, DataControllerRequest request) {
		logger.debug("Entering fetchAndCacheCustomerDetails for customerId:::", customerId);
		try {
			JSONObject cachedData = getCustomerDetailsFromCache(customerId);
			if (cachedData != null && !cachedData.isEmpty()) {
				logger.debug("Returning cached data for customerId:::", customerId);
				return cachedData;
			}

			logger.info("Fetching customer details from service for customerId:::", customerId);
			JSONObject customerResponse = callCustomerService(customerId, request);

			if (customerResponse != null && !customerResponse.isEmpty()) {
				int cacheTime = 20 * 60; // default 20 mins
				try {
					cacheTime = Integer.parseInt(EnvironmentConfigurationsHandler
							.getServerAppPropertyValue("CUSTOMER_CACHE_EXPIRE_TIME", request));
					logger.debug("Cache time configured as: {} seconds", cacheTime);
				} catch (Exception e) {
					logger.warn("Failed to fetch cache expiration config, using default: {} seconds", cacheTime);
				}

				String cacheKey = "customerDetails_" + customerId;
				MemoryManager.saveIntoCache(cacheKey, customerResponse.toString(), cacheTime);
				logger.info("Customer details cached for customerId: {} with key: {}", customerId, cacheKey);
				return customerResponse;
			} else {
				logger.warn("Customer service returned empty response for customerId: {}", customerId);
			}
		} catch (Exception e) {
			logger.error("Error while fetching or caching customer details", e);
		}

		return new JSONObject();
	}

	/**
	 * Calls the customer service
	 */
	private static JSONObject callCustomerService(String customerId, DataControllerRequest request) {
		JSONObject jsonResponse = new JSONObject();
		logger.debug("Entering callCustomerService() for customerId: {}", customerId);
		try {
			Map<String, Object> inputMap = new HashMap<>();
			inputMap.put("userID", customerId);
			Result result = CommonUtils.callIntegrationService(request, inputMap, request.getHeaderMap(),
					UserConstants.SERVICE_ID_USER, UserConstants.OP_GET_USER_FOR_ADMIN, true);

			logger.debug("Customer service response for customerId {}: {}", customerId, ResultToJSON.convert(result));
			String response = ResultToJSON.convert(result);
			jsonResponse = new JSONObject(response);
			return jsonResponse;

		} catch (Exception e) {
			logger.error("Exception occurred while calling the customer service for customerId {}: {}", customerId,
					e.getMessage(), e);
		}
		return jsonResponse;
	}
}

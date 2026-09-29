package com.bct.javaservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;

public class FeeCacheUtil {
	private static final Logger LOGGER = LogManager.getLogger(FeeCacheUtil.class);
	private static final Map<String, List<QRFeeSlab>> FEE_CACHE = new HashMap<>();

	public static double getFee(String aggregatorType, double amount) throws Exception {
		LOGGER.debug("Fetching fee for aggregatorType: " + aggregatorType + ", amount: " + amount);

		synchronized (FEE_CACHE) {
			// If cache is empty, load the fees
			if (FEE_CACHE.isEmpty()) {
				LOGGER.info("Cache is empty. Fetching fees from API...");
				Result result = QRFeeDAO.callFeeAPI();
				if (result != null && !result.getAllDatasets().isEmpty()) {
					LOGGER.debug("API response received. Loading fees into cache.");
					loadFeesToCache(result);
				} else {
					LOGGER.warn("API response is empty. Unable to load fees.");
				}
			}
		}

		List<QRFeeSlab> feeSlabs = FEE_CACHE.get(aggregatorType);
		if (feeSlabs != null) {
			LOGGER.info("Cache hit for aggregatorType: " + aggregatorType);
			for (QRFeeSlab slab : feeSlabs) {
				LOGGER.debug("Checking slab: minAmount=" + slab.getMinAmount() + ", maxAmount=" + slab.getMaxAmount()
						+ ", fee=" + slab.getFee());
				if (amount >= slab.getMinAmount() && amount <= slab.getMaxAmount()) {
					LOGGER.info("Fee found: " + slab.getFee() + " for amount: " + amount);
					return slab.getFee();
				}
			}
			LOGGER.warn("No matching fee slab found for amount: " + amount);
		} else {
			LOGGER.warn("Cache miss for aggregatorType: " + aggregatorType);
		}
		return 0.00;
	}

	private static void loadFeesToCache(Result result) {
		try {
			JSONObject jsonResponse = new JSONObject(ResultToJSON.convert(result));
			LOGGER.debug("Parsed JSON response: " + jsonResponse);

			if (!jsonResponse.optString("success", "false").equalsIgnoreCase("true")) {
				LOGGER.error("Failed to fetch fees. API response: " + jsonResponse);
				return;
			}

			JSONArray qrpaymentCharges = jsonResponse.getJSONArray("qrpaymentCharges");
			LOGGER.debug("Total fee slabs received: " + qrpaymentCharges.length());

			// Iterate through each fee slab and load into the cache
			for (int i = 0; i < qrpaymentCharges.length(); i++) {
				JSONObject charge = qrpaymentCharges.getJSONObject(i);
				String aggregatorType = charge.getString("aggregatorType");
				double minAmount = charge.getDouble("minAmount");
				double maxAmount = charge.getDouble("maxAmount");
				double fee = charge.getDouble("fee");

				LOGGER.debug("Processing fee slab: aggregatorType=" + aggregatorType + ", minAmount=" + minAmount
						+ ", maxAmount=" + maxAmount + ", fee=" + fee);

				QRFeeSlab slab = new QRFeeSlab();
				slab.setAggregatorType(aggregatorType);
				slab.setMinAmount(minAmount);
				slab.setMaxAmount(maxAmount);
				slab.setFee(fee);

				// Log to ensure the aggregatorType is as expected
				LOGGER.debug("Adding slab with aggregatorType: " + aggregatorType);

				// Add fee slab to the appropriate aggregatorType's list
				FEE_CACHE.computeIfAbsent(aggregatorType, k -> new ArrayList<>()).add(slab);
			}

			// Log the size of the cache and the number of slabs per aggregatorType
			LOGGER.debug("Total fee slabs loaded into cache: " + FEE_CACHE.size());

			for (Map.Entry<String, List<QRFeeSlab>> entry : FEE_CACHE.entrySet()) {
				LOGGER.debug("AggregatorType: " + entry.getKey() + " has " + entry.getValue().size() + " slabs.");
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred while loading the fees into cache", e);
		}
	}

	// Invalidate Cache when fees are updated
	public static void invalidateCache() {
		synchronized (FEE_CACHE) {
			FEE_CACHE.clear();
			LOGGER.info("Fee cache invalidated.");
		}
	}
}

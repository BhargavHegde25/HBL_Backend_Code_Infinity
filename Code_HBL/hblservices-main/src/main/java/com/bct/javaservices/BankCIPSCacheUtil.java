package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public class BankCIPSCacheUtil {

	private static final Logger LOGGER = LogManager.getLogger(BankCIPSCacheUtil.class);
	private static final Map<String, String> BANK_CIPS_CACHE = new HashMap<>();
	private static final Map<String, JSONObject> BANK_LIST_CACHE = new HashMap<>();

	/**
	 * Fetches bankCIPSCode for given bankCode
	 */
	public String getBankCIPSCode(String bankCode, DataControllerRequest dcRequest) throws Exception {
		LOGGER.debug("Fetching bankCIPSCode for bankCode: " + bankCode);
		if (BANK_CIPS_CACHE.containsKey(bankCode)) {
			LOGGER.info("Cache hit for bankCode: " + bankCode);
			return BANK_CIPS_CACHE.get(bankCode);
		}
		LOGGER.info("Cache miss for bankCode: " + bankCode);
		synchronized (BANK_CIPS_CACHE) {
			if (BANK_CIPS_CACHE.isEmpty()) {
				LOGGER.info("BANK_CIPS_CACHE is empty. Loading from DB service...");
				loadBankDetailsToCache(dcRequest);
			}
		}
		String bankCIPSCode = BANK_CIPS_CACHE.get(bankCode);
		if (bankCIPSCode == null) {
			LOGGER.debug("No CIPS code found for bankCode: " + bankCode + ". Returning null.");
		} else {
			LOGGER.debug("Resolved bankCIPSCode: " + bankCIPSCode + " for bankCode: " + bankCode);
		}
		return bankCIPSCode;
	}

	/**
	 * Loads bank details from DB and fills cache
	 */
	private void loadBankDetailsToCache(DataControllerRequest dcRequest) {
		try {
			JSONArray result = getBranchDetails(dcRequest);
			if (result == null || result.length() == 0) {
				LOGGER.error("No bank details found in DB response!");
				return;
			}
			LOGGER.debug("Total bank records fetched from DB: " + result.length());
			for (int i = 0; i < result.length(); i++) {
				JSONObject bankObj = result.getJSONObject(i);
				String bankCode = bankObj.optString("bank_swift");
				String bankCIPSCode = bankObj.optString("bank_cd");
				if (bankCode == null || bankCode.isEmpty()) {
					LOGGER.warn("Skipping bank entry with missing bankCode at index: " + i);
					continue;
				}
				LOGGER.debug("Caching bankCode: " + bankCode + " -> bankCIPSCode: " + bankCIPSCode);
				BANK_CIPS_CACHE.put(bankCode, bankCIPSCode);
				BANK_LIST_CACHE.put(bankCIPSCode, bankObj);
			}
			LOGGER.info("BANK_CIPS_CACHE loaded successfully. Size: " + BANK_CIPS_CACHE.size());
			LOGGER.info("BANK_LIST_CACHE loaded successfully. Size: " + BANK_LIST_CACHE.size());
		} catch (Exception e) {
			LOGGER.error("Error while loading BANK_CIPS_CACHE", e);
		}
	}

	/**
	 * Clears cache (e.g., when DB updates)
	 */
	public static void invalidateCache() {
		synchronized (BANK_LIST_CACHE) {
			BANK_LIST_CACHE.clear();
			LOGGER.info("BANK_LIST_CACHE invalidated.");
		}
		synchronized (BANK_CIPS_CACHE) {
			BANK_CIPS_CACHE.clear();
			LOGGER.info("BANK_CIPS_CACHE invalidated.");
		}
	}

	public JSONArray getBranchDetails(DataControllerRequest dcRequest) throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "";
		filter = "Status  eq '" + 1 + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		LOGGER.debug("BankCIPSCacheUtil inputmap:::" + inputmap.toString());
		JSONArray response = new JSONArray();
		try {
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.BRANCH_DETAILS_GET_OPERATION).withRequestParameters(inputmap)
					.withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			LOGGER.debug("BankCIPSCacheUtil response:::" + dbresponse);
			JSONObject responseJSON = new JSONObject(dbresponse);
			response = responseJSON.getJSONArray("branchdetails");

		} catch (Exception e) {
			LOGGER.error("Exception caught while fetching branch details:::", e);

		}
		return response;
	}
	
	/**
	 * Fetches bank details for given bankCode
	 */
	public JSONObject getBankDetails(String bankCode, DataControllerRequest dcRequest) throws Exception {
		LOGGER.debug("Fetching bankCIPSCode for bankCode: " + bankCode);
		if (BANK_LIST_CACHE.containsKey(bankCode)) {
			LOGGER.info("Cache hit for bankCode: " + bankCode);
			return BANK_LIST_CACHE.get(bankCode);
		}
		LOGGER.info("Cache miss for bankCode: " + bankCode);
		synchronized (BANK_LIST_CACHE) {
			if (BANK_LIST_CACHE.isEmpty()) {
				LOGGER.info("BANK_LIST_CACHE is empty. Loading from DB service...");
				loadBankDetailsToCache(dcRequest);
			}
		}
		JSONObject bankDetails = BANK_LIST_CACHE.get(bankCode);
		if (bankDetails == null) {
			LOGGER.debug("No details found for bankCode: " + bankCode + ". Returning null.");
		} else {
			LOGGER.debug("Resolved bankDetails: " + bankDetails + " for bankCode: " + bankCode);
		}
		return bankDetails;
	}
}

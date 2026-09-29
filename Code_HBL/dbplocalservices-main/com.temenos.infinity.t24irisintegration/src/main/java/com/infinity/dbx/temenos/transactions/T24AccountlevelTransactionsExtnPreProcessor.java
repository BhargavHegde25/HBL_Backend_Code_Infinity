package com.infinity.dbx.temenos.transactions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
 
import org.apache.commons.lang3.StringUtils;
 
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class T24AccountlevelTransactionsExtnPreProcessor extends T24AccountlevelTransactionsPreProcessor{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 
	private static final String DATE_PATTERN = "yyyy-MM-dd";
 
	/** Same literal key the parent writes for the page size (see its limit handling). */
	private static final String PARAM_LIMIT_KEY = "limit";
 
	/** Fabric server properties driving the exactly-one-year scenario. */
	private static final String PROP_ONE_YEAR_RANGE_ENABLED = "HBL_TXN_ONE_YEAR_RANGE_ENABLED";
	private static final String PROP_ONE_YEAR_NO_OF_DAYS = "HBL_TXN_ONE_YEAR_NO_OF_DAYS";
	private static final String PROP_ONE_YEAR_LIMIT = "HBL_TXN_ONE_YEAR_LIMIT";
 
	private final ApplicationBusinessDelegate applicationDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);
 
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
 
		// 1. Decide on the ORIGINAL request values, before the parent writes anything into params.
		alert.prepareError("************ T24AccountlevelTransactionsExtnPreProcessor Begins ******").log();;
		boolean isOneYearDateRange = false;
		try {
			String requestStartDate = request.getParameter(Constants.PARAM_SEARCH_START_DATE);
			String requestEndDate = request.getParameter(Constants.PARAM_SEARCH_END_DATE);
			isOneYearDateRange = isOneYearRangeEnabled() && isExactlyOneYearRange(requestStartDate, requestEndDate);
		} catch (Exception e) {
			isOneYearDateRange = false;
			alert.prepareError("HBL## unable to evaluate the one year date range flag, "
					+ "continuing with the existing behaviour. error=" + e.getMessage()).log();
		}
 
		// 2. Parent runs exactly as it does today.
		boolean parentResult = super.execute(params, request, response, result);
 
		// 3. Nothing to add unless the parent succeeded and this is the one year scenario.
		if (!parentResult || !isOneYearDateRange) {
			return parentResult;
		}
 
		try {
			applyOneYearDateRange(params);
			applyOneYearLimit(params);
		} catch (Exception e) {
			// Never downgrade a successful parent run because of this extension.
			alert.prepareError("HBL## one year override skipped. error=" + e.getMessage()).log();
		}
		return parentResult;
	}
 
	/** searchEndDate = server date, searchStartDate = server date - configured number of days. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void applyOneYearDateRange(HashMap params) {
		String currentServerDate = CommonUtils.convertDateToYYYYMMDD(applicationDelegate.getServerTimeStamp());
		if (StringUtils.isBlank(currentServerDate)) {
			alert.prepareError("HBL## server date unavailable, one year date override skipped").log();
			return;
		}
		int numberOfDays = getServerPropertyAsInt(PROP_ONE_YEAR_NO_OF_DAYS);
		if (numberOfDays <= 0) {
			return;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
		LocalDate today = LocalDate.parse(StringUtils.trim(currentServerDate), formatter);
		String oneYearStartDate = today.minusDays(numberOfDays).format(formatter);
 
		params.put(Constants.PARAM_SEARCH_START_DATE, oneYearStartDate);
		params.put(Constants.PARAM_SEARCH_END_DATE, currentServerDate);
		alert.prepareError("HBL## one year range applied. searchStartDate=" + oneYearStartDate
				+ " searchEndDate=" + currentServerDate + " noOfDays=" + numberOfDays).log();
	}
 
	/** limit = configured page size; page_start recomputed with the parent's own formula. */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void applyOneYearLimit(HashMap params) {
		int oneYearLimit = getServerPropertyAsInt(PROP_ONE_YEAR_LIMIT);
		if (oneYearLimit <= 0) {
			return;
		}
		params.put(PARAM_LIMIT_KEY, String.valueOf(oneYearLimit));
		alert.prepareError("HBL## one year limit applied. limit=" + oneYearLimit).log();
 
		// Keep pagination consistent with the new page size - same calculation as the parent.
		try {
			String offset = CommonUtils.getParamValue(params, TransactionConstants.PARAM_OFFSET);
			if (StringUtils.isNotBlank(offset)) {
				int pageStart = (Integer.parseInt(StringUtils.trim(offset)) / oneYearLimit) + 1;
				params.put(TransactionConstants.PARAM_PAGE_START, String.valueOf(pageStart));
			}
		} catch (Exception e) {
			alert.prepareError("HBL## page_start not recomputed. error=" + e.getMessage()).log();
		}
	}
 
	/** Master flag. Off unless the server property is explicitly "true". */
	private boolean isOneYearRangeEnabled() {
		return Boolean.parseBoolean(
				StringUtils.trim(StringUtils.defaultString(getServerPropertyValue(PROP_ONE_YEAR_RANGE_ENABLED))));
	}
 
	/**
	 * Single point of access for Fabric server properties. Uses the accessor already used
	 * elsewhere in the product (EnvironmentConfigurationsHandler.getServerProperty). If this
	 * project standardises on ServerConfigurations, this is the ONLY method to change.
	 */
	private String getServerPropertyValue(String propertyName) {
		try {
			return EnvironmentConfigurationsHandler.getServerProperty(propertyName);
		} catch (Exception e) {
			alert.prepareError("HBL## unable to read server property " + propertyName
					+ " error=" + e.getMessage()).log();
			return null;
		}
	}
 
	/**
	 * Positive integer server property. Returns 0 when the property is missing, blank,
	 * non numeric or not positive, so the caller skips the override instead of falling back
	 * to a hardcoded value.
	 */
	private int getServerPropertyAsInt(String propertyName) {
		String configuredValue = getServerPropertyValue(propertyName);
		try {
			if (StringUtils.isNotBlank(configuredValue)) {
				int value = Integer.parseInt(StringUtils.trim(configuredValue));
				if (value > 0) {
					return value;
				}
			}
		} catch (Exception e) {
			alert.prepareError("HBL## invalid value for " + propertyName + "=" + configuredValue
					+ " error=" + e.getMessage()).log();
		}
		alert.prepareError("HBL## server property " + propertyName
				+ " is not configured with a positive value, override skipped").log();
		return 0;
	}
 
	/**
	 * True only when requestEndDate is exactly requestStartDate plus one calendar year.
	 * Calendar based, not a 365 day count, so leap years are correct:
	 * 2024-02-29 + 1 year = 2025-02-28.
	 */
	private boolean isExactlyOneYearRange(String requestStartDate, String requestEndDate) {
		LocalDate startDate = parseRequestDate(requestStartDate);
		LocalDate endDate = parseRequestDate(requestEndDate);
		if (startDate == null || endDate == null) {
			return false;
		}
		return startDate.plusYears(1).equals(endDate);
	}
 
	/** Tolerant parse of a request date - yyyy-MM-dd, yyyy-M-d, yyyy/MM/dd or yyyyMMdd. */
	private LocalDate parseRequestDate(String rawDate) {
		if (StringUtils.isBlank(rawDate)) {
			return null;
		}
		String value = StringUtils.trim(rawDate);
		try {
			if (value.matches("\\d{8}")) {
				return LocalDate.of(Integer.parseInt(value.substring(0, 4)),
						Integer.parseInt(value.substring(4, 6)), Integer.parseInt(value.substring(6, 8)));
			}
			String[] dateParts = value.split("[-/.]");
			if (dateParts.length != 3) {
				return null;
			}
			return LocalDate.of(Integer.parseInt(StringUtils.trim(dateParts[0])),
					Integer.parseInt(StringUtils.trim(dateParts[1])),
					Integer.parseInt(StringUtils.trim(dateParts[2])));
		} catch (Exception e) {
			return null;
		}
	}
}

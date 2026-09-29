/**
 * 
 */
package com.temenos.infinity.wealthorder.t24.processor.pre;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.sessionmanager.SessionScope;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealthorder.common.util.OrderServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTransactionsOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentTransactionsOrchPreProcessor T24 - Entered ").log();
		try {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetInstrumentTransactionsOrchPreProcessor T24 - No user permission ").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetInstrumentTransactionsOrchPreProcessor T24 - User has permission ").log();
				String dateformat = "YYYY-MM-dd";
				List sortValues = Arrays.asList("description", "orderType", "quantity", "limitPrice", "netAmount",
						"tradeDate", "exchangeRate", "instrumentAmount", "valueDate", "fees", "total");

				String[] fieldName = new String[] { "tradeDate", "description", "orderType", "quantity", "limitPrice",
						"netAmount", "exchangeRate", "instrumentAmount", "valueDate", "total" };
				String[] columnName = new String[] { "OPERATION_DATE", "INSTR_NAME", "OPERATION_NATURE", "QUANTITY",
						"PRICE", "Gross_amount_instr_ccy", "EXCH_RATE", "GROSS_AMOUNT_OPER_CURR", "VALUE_DATE",
						"NET_AMOUNT_OPER_CURR" };
				Object sortByObj = inputMap.get(TemenosConstants.SORTBY);
				Object startDateObj = inputMap.get(TemenosConstants.STARTDATE);
				Object endDateObj = inputMap.get(TemenosConstants.ENDDATE);
				Object sortTypeObj = inputMap.get(TemenosConstants.SORTORDER);
				String portfolioId = null, endDate = null, startDate = null, sortBy = null, sortType = null;
				int indexOfSort = 0;

				if (!sortValues.contains(sortByObj)) {
					return OrderServiceUtils.validateData(result, TemenosConstants.SORTBY);
				}

				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
				}

				if (startDateObj != null) {
					startDate = inputMap.get(TemenosConstants.STARTDATE).toString();
					inputMap.put(TemenosConstants.STARTDATE, startDate);
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
					if (!isTrue) {
						return OrderServiceUtils.validateFormat(result, TemenosConstants.STARTDATE);
					}
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.STARTDATE);
				}
				if (endDateObj != null) {
					endDate = inputMap.get(TemenosConstants.ENDDATE).toString();
					inputMap.put(TemenosConstants.ENDDATE, endDate);
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, endDate);
					if (!isTrue) {
						return OrderServiceUtils.validateFormat(result, TemenosConstants.ENDDATE);
					}
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.ENDDATE);
				}
				if (sortByObj != null) {
					sortBy = inputMap.get(TemenosConstants.SORTBY).toString();
					inputMap.put(TemenosConstants.SORTBY, sortBy);
					indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
					if (indexOfSort == -1) {
						indexOfSort = 0;
					}
				} else {
					return OrderServiceUtils.unauthAccess(result, TemenosConstants.SORTBY);
				}
				if (sortTypeObj != null) {
					sortType = inputMap.get(TemenosConstants.SORTORDER).toString();
					inputMap.put(TemenosConstants.SORTORDER, sortType);
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					request.addRequestParam_(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, sortType);
					request.addRequestParam_(TemenosConstants.SORTTYPE, sortType);
				} else {
					if (sortBy.equalsIgnoreCase("tradeDate") || sortBy.equalsIgnoreCase("valueDate")) {
						inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						request.addRequestParam_(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						inputMap.put(TemenosConstants.SORTTYPE, "desc");
						request.addRequestParam_(TemenosConstants.SORTTYPE, "desc");
					} else {
						inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						request.addRequestParam_(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						inputMap.put(TemenosConstants.SORTTYPE, "asc");
						request.addRequestParam_(TemenosConstants.SORTTYPE, "asc");
					}

				}
				List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
				if (allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========> GetInstrumentTransactionsOrchPreProcessor T24 - Entering into integration").log();
				} else {
					alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
					alert.prepareError("Invalid request").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					diagnostic.prepareDebug(
							"==========> GetInstrumentTransactionsOrchPreProcessor T24 - Exiting with invalid portfolio ").log();
					return false;

				}
				return true;
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetInstrumentTransactionsOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			return false;
		}
	}

}

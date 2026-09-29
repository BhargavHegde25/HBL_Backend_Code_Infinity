/**
 * 
 */
package com.temenos.infinity.wealth.t24.processor.pre;

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
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author himaja.sridhar
 *
 */
public class GetAccountActivityOrchPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetAccountActivityOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			if (!userPermissions.contains("WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW")) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetAccountActivityOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetAccountActivityOrchPreProcessor T24 - User has permission ").log();
				List sortValues = Arrays.asList("amount", "balance", "displayName", "shortName", "quantity",
						"bookingDate", "valueDate");
				String sortBy = null;
				int indexOfSort = 0;
				String dateformat = "YYYYMMdd";
				String[] fieldName = new String[] { "bookingDate", "amount", "balance", "displayName", "quantity",
						"valueDate", "shortName" };
				String[] columnName = new String[] { "OPERATION_DATE", "DEBIT_CREDIT", "ACCOUNT_BALANCE",
						"OPERATION_NATURE", "QUANTITY", "VALUE_DATE", "Instr_Name" };
				String accountId = null, dateFrom = null, dateTo = null, portfolioId = null,
						sortType = null;

				Object sortByObj = inputMap.get(TemenosConstants.SORTBY);
				if (!sortValues.contains(sortByObj)) {
					return PortfolioServiceUtils.validateData(result, TemenosConstants.SORTBY);
				}
				if (sortByObj != null) {
					sortBy = inputMap.get(TemenosConstants.SORTBY).toString();
					indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
					if (indexOfSort == -1) {
						indexOfSort = 0;
					}
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.SORTBY);
				}
				if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
						&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
					portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
				}

				Object accountIdObj = inputMap.get(TemenosConstants.ACCID);
				if (accountIdObj != null && !accountIdObj.equals("")) {
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.ACCID);
				}

				Object dateFromObj = inputMap.get(TemenosConstants.DATEFROM);
				if (dateFromObj != null) {
					dateFrom = inputMap.get(TemenosConstants.DATEFROM).toString();
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateFrom);
					if (!isTrue) {
						return PortfolioServiceUtils.validateFormat(result, TemenosConstants.DATEFROM);
					}
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.DATEFROM);
				}

				Object dateToObj = inputMap.get(TemenosConstants.DATETO);
				if (dateToObj != null) {
					dateTo = inputMap.get(TemenosConstants.DATETO).toString();
					boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, dateTo);
					if (!isTrue) {
						return PortfolioServiceUtils.validateFormat(result, TemenosConstants.DATETO);
					}
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.DATETO);
				}
				Object listTypeObj = inputMap.get(TemenosConstants.LISTTYPE);
				if (listTypeObj != null && !listTypeObj.equals("")) {
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.LISTTYPE);
				}

				Object sortTypeObj = inputMap.get(TemenosConstants.SORTORDER);
				if (sortTypeObj != null) {
					sortType = inputMap.get(TemenosConstants.SORTORDER).toString();
					inputMap.put(TemenosConstants.SORTORDER, sortType);
					inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					request.addRequestParam_(TemenosConstants.ORDERBY, columnName[indexOfSort]);
					inputMap.put(TemenosConstants.SORTTYPE, sortType);
					request.addRequestParam_(TemenosConstants.SORTTYPE, sortType);
				} else {
					if (sortBy.equalsIgnoreCase("bookingDate") || sortBy.equalsIgnoreCase("valueDate")) {
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
				List<String> allAccountList = PortfolioWealthUtils.getAllAccountsFromCache(request);
				if (allportfoliosList.contains(portfolioId)) {
					diagnostic.prepareDebug("==========> GetAccountActivityOrchPreProcessor T24 - Portfolio exists for the customer").log();
					if (allAccountList.contains(accountId)) {
						diagnostic.prepareDebug("==========> GetAccountActivityOrchPreProcessor T24 - Account exists for the portfolio").log();
					}
				} else {
					alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
					alert.prepareError("Invalid request").log();
					result.addParam("status", "Failure");
					result.addParam("error", "Unauthorized Access");
					return false;

				}
				return true;
			}

		}

		catch (Exception e) {
			alert.prepareError("==========> GetAccountActivityOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}

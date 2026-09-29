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
public class GetOrdersDetailsOrchPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetOrdersDetailsOrchPreProcessor T24 - Entered ").log();
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(request);
			String ordersViewType = inputMap.get(TemenosConstants.ORDERSVIEW_TYPE).toString();
			if ((!userPermissions.contains("WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW"))
					&& (!userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW"))) {
				result.addParam("opstatus", "1582");
				result.addParam("status", TemenosConstants.FAILURE);
				result.addParam("error", "Logged in user not authorized to perform this action");
				diagnostic.prepareDebug("==========> GetOrdersDetailsOrchPreProcessor T24 - No User permission").log();
				return false;
			} else {
				diagnostic.prepareDebug("==========> GetOrdersDetailsOrchPreProcessor T24 - User has permission ").log();
				if ((userPermissions.contains("WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW")
						&& ordersViewType.equalsIgnoreCase(TemenosConstants.ORDERS_TYPE_OPEN))
						|| (userPermissions.contains("WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW")
								&& ordersViewType.equalsIgnoreCase(TemenosConstants.ORDERS_TYPE_HISTORY))) {
					int indexOfSort = 0;
					String portfolioId = null, startDate = null, endDate = null, sortBy = null, search = null,
							orderviewType = null, cancelorderID = null, sortType = null, limit = null, offset = null,
							instrumentId = null, dateformat = "YYYY-MM-dd";

					List sortValues = Arrays.asList("description", "orderType", "quantity", "limitPrice", "tradeDate",
							"price", "status", "orderReference", "stopPrice", "orderExecutionPrice");

					String[] fieldName = new String[] { "tradeDate", "quantity", "limitPrice", "description",
							"orderReference", "stopPrice" };
					String[] columnName = new String[] { "ORDER_ENTRY_DATE", "QUANTITY", "LIMIT_PRICE", "INSTR_NAME",
							"ORDER_CODE", "STOP" };
					Object sortByObj = inputMap.get(TemenosConstants.SORTBY);
					if (!sortValues.contains(sortByObj)) {
						return PortfolioServiceUtils.validateData(result, TemenosConstants.SORTBY);
					}
					if (inputMap.get(TemenosConstants.PORTFOLIOID) != null
							&& inputMap.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0) {
						portfolioId = inputMap.get(TemenosConstants.PORTFOLIOID).toString();
					} else {
						return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.PORTFOLIOID);
					}
					if (inputMap.get(TemenosConstants.ORDERSVIEW_TYPE) != null
							&& (TemenosConstants.ORDERSVIEW_TYPE.length() > 0)) {
						orderviewType = inputMap.get(TemenosConstants.ORDERSVIEW_TYPE).toString();
						inputMap.put(TemenosConstants.ORDERSVIEW_TYPE, orderviewType);
						if (orderviewType.trim().equalsIgnoreCase("OPEN")
								|| orderviewType.trim().equalsIgnoreCase("HISTORY")) {

							if (inputMap.get(TemenosConstants.STARTDATE) != null) {
								startDate = inputMap.get(TemenosConstants.STARTDATE).toString();
								inputMap.put(TemenosConstants.STARTDATE, startDate);
								inputMap.put("fromDate", startDate);
								boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, startDate);
								if (!isTrue) {
									return PortfolioServiceUtils.validateFormat(result, TemenosConstants.STARTDATE);
								}
							} else if (orderviewType.trim().equalsIgnoreCase("HISTORY")) {
								return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.STARTDATE);
							}

							if (inputMap.get(TemenosConstants.ENDDATE) != null) {
								endDate = inputMap.get(TemenosConstants.ENDDATE).toString();
								inputMap.put(TemenosConstants.ENDDATE, endDate);
								inputMap.put("toDate", endDate);
								boolean isTrue = PortfolioWealthUtils.validateDateFormat(dateformat, endDate);
								if (!isTrue) {
									return PortfolioServiceUtils.validateFormat(result, TemenosConstants.ENDDATE);
								}
							} else if (orderviewType.trim().equalsIgnoreCase("HISTORY")) {
								return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.ENDDATE);
							}

						} else {
							return PortfolioServiceUtils.unauthAccess(result, "Type should be Open/History");
						}
					} else {
						return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.ORDERSVIEW_TYPE);
					}

					if (inputMap.get(TemenosConstants.SORTBY) != null
							&& sortValues.contains(inputMap.get(TemenosConstants.SORTBY))) {
						sortBy = inputMap.get(TemenosConstants.SORTBY).toString();
						inputMap.put(TemenosConstants.SORTBY, sortBy);
						indexOfSort = ArrayUtils.indexOf(fieldName, sortBy);
						if (indexOfSort == -1) {
							indexOfSort = 0;
						}
					} else {
						inputMap.put(TemenosConstants.SORTBY, "description");
						indexOfSort = ArrayUtils.indexOf(fieldName, "description");
						if (indexOfSort == -1) {
							indexOfSort = 0;
						}
					}

					if (inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME) != null) {
						search = inputMap.get(TemenosConstants.SEARCHBYINSTRUMENTNAME).toString();
						inputMap.put(TemenosConstants.SEARCHBYINSTRUMENTNAME, search);
					}

					if (inputMap.get(TemenosConstants.ORDER_ID) != null) {
						cancelorderID = inputMap.get(TemenosConstants.ORDER_ID).toString();
						inputMap.put(TemenosConstants.ORDER_ID, cancelorderID);
					}

					if (inputMap.get(TemenosConstants.SORTORDER) != null) {
						sortType = inputMap.get(TemenosConstants.SORTORDER).toString();
						inputMap.put(TemenosConstants.SORTORDER, sortType);
						inputMap.put(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						request.addRequestParam_(TemenosConstants.ORDERBY, columnName[indexOfSort]);
						inputMap.put(TemenosConstants.SORTTYPE, sortType);
						request.addRequestParam_(TemenosConstants.SORTTYPE, sortType);
					} else {
						if (sortBy.equalsIgnoreCase("tradeDate")) {
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

					if (inputMap.get(TemenosConstants.PAGESIZE) != null) {
						limit = inputMap.get(TemenosConstants.PAGESIZE).toString();
						inputMap.put(TemenosConstants.PAGESIZE, limit);
					}

					if (inputMap.get(TemenosConstants.PAGEOFFSET) != null) {
						offset = inputMap.get(TemenosConstants.PAGEOFFSET).toString();
						inputMap.put(TemenosConstants.PAGEOFFSET, offset);
					}

					if (inputMap.get(TemenosConstants.INSTRUMENTID) != null) {
						instrumentId = inputMap.get(TemenosConstants.INSTRUMENTID).toString();
						inputMap.put("instrumentId", instrumentId);
					}
					List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
					if (allportfoliosList.contains(portfolioId)) {
						diagnostic.prepareDebug("==========> GetOrdersDetailsOrchPreProcessor T24 - Portfolio exists for the customer").log();
						return true;
					} else {
						alert.prepareError("Portfolio ID " + portfolioId + " does not exist for the Customer").log();
						alert.prepareError("Invalid request").log();
						result.addParam("status", "Failure");
						result.addParam("error", "Unauthorized Access");
						return false;

					}
				} else {
					return PortfolioServiceUtils.unauthAccess(result, TemenosConstants.ORDERSVIEW_TYPE);
				}
			}
		} catch (Exception e) {
			alert.prepareError("==========> GetOrdersDetailsOrchPreProcessor T24 - Error: " + e.getMessage()).log();
			e.getMessage();
			return false;
		}
	}

}

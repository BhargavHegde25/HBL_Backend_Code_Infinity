package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.impl.OrdersListBackendDelegateImpl;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors.GetOrderDetailsOrchPostProcessor;


/**
 * (INFO) If status is set as a part of the request , the operation is exited
 * else operation is executed.
 * 
 * @author balaji.krishnan
 *
 */

public class GetOrderDetailsOrchPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private HashSet<String> allorderIds = new HashSet<String>();

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			allorderIds = new HashSet<String>();
			if (request.getParameter(TemenosConstants.ORDERSVIEW_TYPE) != null
					&& request.getParameter(TemenosConstants.ORDERSVIEW_TYPE).equalsIgnoreCase("history")
					|| (request.getParameter("pendingOrderID_Authentication") != null)) {
				OrdersListBackendDelegateImpl ordersListBackendDelegateImpl = new OrdersListBackendDelegateImpl();
				JSONArray sortedJSON = new JSONArray();
				JSONObject responseJSON = new JSONObject();
				JSONArray finalArr = new JSONArray();
				boolean cancel_OR_modify_order = false;
				String[] portfolioIdArr = null, searchArr = null, sortByArr = null, sortOrderArr = null,
						pageSizeArr = null, pageOffsetArr = null, startDateArr = null, endDateArr = null;
				String portfolioId = "", startDate = "", endDate = "", search = "", sortBy = "", sortOrder = "",
						pageSize = "", pageOffset = "", orderId = "";
				int totalCount = 0, pageSizeVal = 0, pageOffsetVal = 0;

				portfolioIdArr = request.getParameterValues(TemenosConstants.PORTFOLIOID);
				startDateArr = request.getParameterValues(TemenosConstants.STARTDATE);
				endDateArr = request.getParameterValues(TemenosConstants.ENDDATE);
				searchArr = request.getParameterValues(TemenosConstants.SEARCHBYINSTRUMENTNAME);
				sortByArr = request.getParameterValues(TemenosConstants.SORTBY);
				sortOrderArr = request.getParameterValues(TemenosConstants.SORTORDER);
				pageSizeArr = request.getParameterValues(TemenosConstants.PAGESIZE);
				pageOffsetArr = request.getParameterValues(TemenosConstants.PAGEOFFSET);

				if (request.getParameterValues("cancelormodify_order") != null
						&& request.getParameterValues("cancelormodify_order")[0].toString().equalsIgnoreCase("true")) {
					cancel_OR_modify_order = true;
					if (StringUtils.isNotBlank(request.getParameterValues(TemenosConstants.ORDER_ID)[0])) {
						orderId = request.getParameterValues(TemenosConstants.ORDER_ID)[0];
					}
				}

				if (startDateArr != null && startDateArr.length > 0) {
					startDate = startDateArr[0].trim();
				}
				if (endDateArr != null && endDateArr.length > 0) {
					endDate = endDateArr[0].trim();
				}
				if (searchArr != null && searchArr.length > 0) {
					search = searchArr[0].trim();
				}
				if (sortByArr != null && sortByArr.length > 0) {
					sortBy = sortByArr[0].trim();
				}
				if (sortOrderArr != null && sortOrderArr.length > 0) {
					sortOrder = sortOrderArr[0].trim();
				}
				if (pageSizeArr != null && pageSizeArr.length > 0) {
					pageSize = pageSizeArr[0].trim();
				}
				if (pageOffsetArr != null && pageOffsetArr.length > 0) {
					pageOffset = pageOffsetArr[0].trim();
				}
				if (portfolioIdArr != null && portfolioIdArr.length > 0) {
					portfolioId = portfolioIdArr[0].trim();
				}

				pageSizeVal = (pageSize != null && pageSize.trim().length() > 0) ? Integer.parseInt(pageSize) : 0;
				pageOffsetVal = (pageOffset != null && pageOffset.trim().length() > 0) ? Integer.parseInt(pageOffset)
						: 0;

				Dataset hist_ds = result.getDatasetById("recentoperationDetails");
				if (hist_ds != null) {
					JSONObject historyorderObj = new JSONObject();
					historyorderObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(hist_ds).toString()));
					JSONArray historyordersArray = historyorderObj.getJSONArray("Field");
					if (historyordersArray != null && historyordersArray.length() > 0) {
						for (int i = 0; i < historyordersArray.length(); i++) {
							JSONObject ordersObj = historyordersArray.getJSONObject(i);
							finalArr.put(ordersObj);
						}
					}
				} else {
					sortedJSON = new JSONArray();
				}

				Dataset open_ds = result.getDatasetById("pendingordersDetails");
				if (open_ds != null) {
					JSONObject openorderObj = new JSONObject();
					openorderObj.put("Field",
							Utilities.convertStringToJSONArray(ResultToJSON.convertDataset(open_ds).toString()));
					JSONArray openordersArray = openorderObj.getJSONArray("Field");
					if (openordersArray != null && openordersArray.length() > 0) {
						for (int i = 0; i < openordersArray.length(); i++) {
							JSONObject openordersObj = openordersArray.getJSONObject(i);
							finalArr.put(openordersObj);
						}
					}
				} else {
					sortedJSON = new JSONArray();
				}

				if (startDate == null || endDate == null || startDate.equals("") || endDate.equals("")) {
					sortedJSON = finalArr;
				} else {
					sortedJSON = ordersListBackendDelegateImpl.filterDate(finalArr, startDate, endDate);
				}

				if (search != null && search.length() > 0) {
					GetHistoryOrderDetailsPostProcessor obj = new GetHistoryOrderDetailsPostProcessor();
					sortedJSON = obj.returnSearch(sortedJSON, search);
				}

				// other sorts are performed in backendImpl itself
				if (sortBy != null && sortBy.equalsIgnoreCase("tradeDate")) {
					GetHistoryOrderDetailsPostProcessor obj = new GetHistoryOrderDetailsPostProcessor();
					sortedJSON = obj.sortdatetimeArray(sortedJSON, sortBy, sortOrder);
				} else if (sortBy != null) {
					sortedJSON = ordersListBackendDelegateImpl.sortArray(sortedJSON, sortBy, sortOrder);
				}

				totalCount = sortedJSON.length();

				if (pageSizeVal > 0 && pageOffsetVal >= 0) {
					sortedJSON = ordersListBackendDelegateImpl.pagination(sortedJSON, pageSizeVal, pageOffsetVal);
				}

				if (cancel_OR_modify_order) {
					HashSet<String> pendingorderIds = new HashSet<String>();
					HashSet<String> recentoperationorderIds = new HashSet<String>();
					String pendingorderIdlist = result.getParamValueByName("pendingorderIds");
					if (pendingorderIdlist != null && pendingorderIdlist.trim().length() > 0) {
						pendingorderIds = stringintohashset(pendingorderIdlist);
					}
					String recentoperationorderIdlist = result.getParamValueByName("recentoperationorderIds");
					if (recentoperationorderIdlist != null && recentoperationorderIdlist.trim().length() > 0) {
						recentoperationorderIds = stringintohashset(recentoperationorderIdlist);
					}
					allorderIds.addAll(pendingorderIds);
					allorderIds.addAll(recentoperationorderIds);
					result.clearDatasets();
					result.clearRecords();
					result.clearParams();
					responseJSON.put("OrderID_Authentication", allorderIds.contains(orderId));
					if (!allorderIds.contains(orderId)) {
						allorderIds = new HashSet<String>();
						return PortfolioWealthUtils.UnauthorizedAccess();
					}

				} else {

					responseJSON.put("portfolioID", portfolioId);
					responseJSON.put("ordersDetails", sortedJSON);
					responseJSON.put(TemenosConstants.STARTDATE, startDate);
					responseJSON.put(TemenosConstants.ENDDATE, endDate);
					responseJSON.put(TemenosConstants.SORTBY, sortBy);
					responseJSON.put(TemenosConstants.SORTORDER, sortOrder);
					responseJSON.put("totalCount", totalCount);
				}
				responseJSON.put("opstatus", "0");
				responseJSON.put("httpStatusCode", "200");
				responseJSON.put(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				return Utilities.constructResultFromJSONObject(responseJSON);
			} else {
				return result;
			}
		} catch (Exception e) {

			alert.prepareError("Error while invoking GetOrderDetailsOrchPostProcessor - "
					+ WealthAPIServices.WEALTHSERVICESORCHESTRATION.getOperationName() + "  : " + e).log();
		}

		return result;
	}

	private HashSet<String> stringintohashset(String orderIdlist) {

		orderIdlist = orderIdlist.substring(1, orderIdlist.length() - 1).trim();
		String[] strParts = orderIdlist.split(",\\s");
		List<String> listParts = Arrays.asList(strParts);
		HashSet<String> uniqueIDs = new HashSet<String>(listParts);

		return uniqueIDs;

	}

}

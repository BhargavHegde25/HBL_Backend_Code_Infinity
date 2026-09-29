package com.temenos.infinity.api.wealth.backenddelegate.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.backenddelegate.api.WealthDashboardBackendDelegate;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealth.constants.OperationName;
import com.temenos.infinity.api.wealth.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealth.util.WealthMockUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class WealthDashboardBackendDelegateImpl implements WealthDashboardBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	WealthMockUtil wealthMockUtil = new WealthMockUtil();
	PortfolioDetailsBackendDelegateImpl portfolioDetailsBackendDelegateImpl = new PortfolioDetailsBackendDelegateImpl();

	@SuppressWarnings("unchecked")
	@Override
	public Result getPortfolioList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Map<String, Object> inputMap = new HashMap<>();
		/* Checking whether coreCustomerId is present in request or not */
		String backendId = null;
		if (request.getParameter(TemenosConstants.CORECUSTOMERID) != null
				&& request.getParameter(TemenosConstants.CORECUSTOMERID).toString().trim().length() > 0) {
			/*CorecustomerId is present in request so entering Multi customer flow*/
			backendId=request.getParameter(TemenosConstants.CORECUSTOMERID).toString();
			inputMap.put(TemenosConstants.CUSTOMERID, backendId);
			request.addRequestParam_(TemenosConstants.CUSTOMERID, backendId);
		} else {
			/*CorecustomerId is not present in request so entering Single customer flow*/
			backendId = PortfolioWealthUtils.getCustomerFromCache(request);
			inputMap.put(TemenosConstants.CUSTOMERID, backendId);
			request.addRequestParam_(TemenosConstants.CUSTOMERID, backendId);
		}
		
		if(backendId!= null && !backendId.isEmpty())
		{
			PortfolioWealthUtils.saveCoreCustomerIdIntoSession(backendId);
		}
//		String backendId = PortfolioWealthUtils.getCustomerFromCache(request);
//		inputMap.put(TemenosConstants.CUSTOMERID, backendId);
		try {

				String createResponse = null;
				String serviceName = ServiceId.WEALTHORCHESTRATION;
				String operationName = OperationName.GETPORTFOLIOLIST;

				try {
					createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
							.withOperationId(operationName).withRequestParameters(inputMap)
							.withRequestHeaders(headersMap).withDataControllerRequest(request).build().getResponse();
					JSONObject customerJSON = new JSONObject(createResponse);
					JSONObject portfolioJSON = customerJSON.getJSONObject("PortfolioList");
					JSONArray portfolioArr = portfolioJSON.getJSONArray("portfolioList");
					PortfolioWealthUtils.saveWealthCoreIntoSession(request);

					if (portfolioArr.length() == 0) {
						return Utilities.constructResultFromJSONObject(customerJSON);
					} else {
						// Check if it is mock
						if (customerJSON.has(TemenosConstants.ISMOCKINTEGRATION)
								&& customerJSON.get(TemenosConstants.ISMOCKINTEGRATION) != null && customerJSON
										.get(TemenosConstants.ISMOCKINTEGRATION).toString().equalsIgnoreCase("true")) {
							return Utilities.constructResultFromJSONObject(customerJSON);
						}
					}

					JSONArray jointPortfolioArr = new JSONArray();
					String[] jointHolders = getJointHolders(portfolioArr);
					String jointHolderIds = jointHolders[0];
					String allPortfolios = jointHolders[3];

					if (customerJSON.has(TemenosConstants.ISTAPINTEGRATION)
							&& customerJSON.get(TemenosConstants.ISTAPINTEGRATION) != null && customerJSON
									.get(TemenosConstants.ISTAPINTEGRATION).toString().equalsIgnoreCase("false")) {

						if (!jointHolderIds.isEmpty()) {
							inputMap.put(TemenosConstants.CUSTOMERID, jointHolderIds);
							String jointResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
									.withObjectId(null).withOperationId(operationName).withRequestParameters(inputMap)
									.withRequestHeaders(headersMap).withDataControllerRequest(request).build()
									.getResponse();
							JSONObject jointJSON = new JSONObject(jointResponse);
							JSONObject jointPortfolioJSON = jointJSON.getJSONObject("PortfolioList");
							jointPortfolioArr = jointPortfolioJSON.getJSONArray("portfolioList");
							if (jointPortfolioArr != null && jointPortfolioArr.length() > 0) {
								String[] portfolios = getAllPortfolios(allPortfolios, jointPortfolioArr);
								allPortfolios = portfolios[0];
							}
						}
						JSONArray resultArr = allportfoliolist(jointPortfolioArr, portfolioArr, jointHolders);

						JSONObject resultJSON = new JSONObject();
						JSONObject listObject = new JSONObject();
						listObject.put("portfolioList", resultArr);
						resultJSON.put("PortfolioList", listObject);

						PortfolioWealthUtils.savePortfoliosIntoSession(allPortfolios, backendId);

						return Utilities.constructResultFromJSONObject(resultJSON);
					} else {
						PortfolioWealthUtils.savePortfoliosIntoSession(allPortfolios, backendId);
						return Utilities.constructResultFromJSONObject(customerJSON);
					}
				} catch (Exception e) {
					alert.prepareError("Error while invoking Service - "
							+ PortfolioWealthAPIServices.WEALTH_GETPORTFOLIOLIST.getOperationName() + "  : " + e).log();
				}

				return null;

		} catch (Exception e) {
			alert.prepareError("Error while invoking  - " + OperationName.GETPORTFOLIOLIST + "  : " + e).log();
			return null;

		}
	}

	private JSONArray allportfoliolist(JSONArray jointPortfolioArr, JSONArray portfolioArr, String[] jointHolders) {

		String customerId = "", customerName = "";
		JSONArray allportList = new JSONArray();

		try {
			for (int i = 0; i < portfolioArr.length(); i++) {
				JSONObject portObj = portfolioArr.getJSONObject(i);
				portObj.put(TemenosConstants.JOINTHOLDERID, jointHolders[1]);
				portObj.put(TemenosConstants.JOINTHOLDERS, jointHolders[2]);
				portObj.put(TemenosConstants.ISJOINTACCOUNT, "false");
				allportList.put(portObj);
				if (i == 0) {
					customerId = portObj.getString(TemenosConstants.CUSTOMERID);
					customerName = portObj.getString(TemenosConstants.PRIMARYHOLDER);
				}
			}
			for (int i = 0; i < jointPortfolioArr.length(); i++) {
				JSONObject jointportObj = jointPortfolioArr.getJSONObject(i);
				jointportObj.put(TemenosConstants.JOINTHOLDERID, customerId);
				jointportObj.put(TemenosConstants.JOINTHOLDERS, customerName);
				jointportObj.put(TemenosConstants.ISJOINTACCOUNT, "true");
				allportList.put(jointportObj);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return allportList;
	}

	private String[] getJointHolders(JSONArray list) {
		String[] jointArr = new String[5];
		String jointHolderIds = "";
		String jointHolderNames = "";
		String jointHolderId = "";
		String jointHolderName = "";
		String custPortfolios = "";
		String portfolioId = "";
		int count = 0;
		for (int i = 0; i < list.length(); i++) {
			JSONObject portfolioObj = list.getJSONObject(i);
			portfolioId = portfolioObj.getString(TemenosConstants.PORTFOLIOID);
			jointHolderId = portfolioObj.getString(TemenosConstants.JOINTHOLDERID);
			jointHolderName = portfolioObj.getString(TemenosConstants.JOINTHOLDERS);
			if (!jointHolderId.isEmpty()) {
				if (!jointHolderIds.contains(jointHolderId)) {
					if (jointHolderIds.isEmpty()) {
						jointHolderIds = jointHolderId;
						jointHolderNames = jointHolderName;
					} else {
						jointHolderIds = jointHolderIds + "," + jointHolderId;
						jointHolderNames = jointHolderNames + "," + jointHolderName;
					}
				}
			}
			if (portfolioId != null && !portfolioId.equals("")) {
				if (custPortfolios.equals("")) {
					custPortfolios = portfolioId;
				} else {
					custPortfolios = custPortfolios + "," + portfolioId;
				}
				count++;
			}
		}
		jointArr[0] = jointHolderIds.replace(",", " ");
		;
		jointArr[1] = jointHolderIds;
		jointArr[2] = jointHolderNames;
		jointArr[3] = custPortfolios;
		jointArr[4] = String.valueOf(count);
		return jointArr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result getAssetList(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Map<String, Object> inputMap = new HashMap<>();
		
		/* Checking whether coreCustomerId is present in request or not */
		String backendId = null;
		if (request.getParameter(TemenosConstants.CORECUSTOMERID) != null
				&& request.getParameter(TemenosConstants.CORECUSTOMERID).toString().trim().length() > 0) {
			/*CorecustomerId is present in request so entering Multi customer flow*/
			backendId=request.getParameter(TemenosConstants.CORECUSTOMERID).toString();
			inputMap.put(TemenosConstants.CUSTOMERID, backendId);
			request.addRequestParam_(TemenosConstants.CUSTOMERID, backendId);
		} else {
			/*CorecustomerId is not present in request so entering Single customer flow*/
			backendId = PortfolioWealthUtils.getCustomerFromCache(request);
			inputMap.put(TemenosConstants.CUSTOMERID, backendId);
			request.addRequestParam_(TemenosConstants.CUSTOMERID, backendId);
		}
		
		if(backendId!= null && !backendId.isEmpty())
		{
			PortfolioWealthUtils.saveCoreCustomerIdIntoSession(backendId);
		}
//		String customerId = PortfolioWealthUtils.getCustomerFromCache(request);
//		inputMap.put(TemenosConstants.CUSTOMERID, customerId);

			String createResponse = null;
			String serviceName = ServiceId.WEALTHORCHESTRATION;
			String operationName = OperationName.GETASSETLIST;

			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(createResponse);

				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETASSETLIST.getOperationName() + "  : " + e).log();
				e.getMessage();
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result getDashboardRecentActivity(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		

		String backendId = PortfolioWealthUtils.getCustomerFromCache(request);
		inputMap.put(TemenosConstants.CUSTOMERID, backendId);

			String createResponse = null;
			String serviceName = ServiceId.WEALTHORCHESTRATION;
			String operationName = OperationName.GETDASHBOARDRECENTACTIVITY;

			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(createResponse);

				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETDASHBOARDRECENTACTIVITY.getOperationName() + "  : " + e).log();
				e.getMessage();
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result getDashboardGraphData(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object graphDurationObj = inputParams.get(TemenosConstants.GRAPHDURATION);
		String customerId = PortfolioWealthUtils.getCustomerFromCache(request);
		
		String graphDuration = null;
		if (graphDurationObj == null || graphDurationObj.equals("") || customerId == null
				|| customerId.equals("")) {
			alert.prepareError("Invalid request").log();
			JSONObject result = new JSONObject();
			result.put("status", "Failure");
			result.put("error", "Invalid Input! Graph duration and CustomerId cannot be null or empty");
			return Utilities.constructResultFromJSONObject(result);

		} else {
			if (graphDurationObj != null) {
				graphDuration = inputParams.get(TemenosConstants.GRAPHDURATION).toString();
				inputMap.put(TemenosConstants.GRAPHDURATION, graphDuration);
			}
				inputMap.put(TemenosConstants.CUSTOMERID, customerId);
		}

			String createResponse = null;
			String serviceName = ServiceId.WEALTHORCHESTRATION;
			String operationName = OperationName.GETDASHBOARDGRAPHDATA;

			try {
				createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
						.withDataControllerRequest(request).build().getResponse();
				JSONObject resultJSON = new JSONObject(createResponse);

				return Utilities.constructResultFromJSONObject(resultJSON);

			} catch (Exception e) {
				alert.prepareError("Error while invoking Service - "
						+ PortfolioWealthAPIServices.WEALTH_GETDASHBOARDGRAPHDATA.getOperationName() + "  : " + e).log();
				e.getMessage();
			}

		
		return null;
	}

	public JSONArray getSortedJsonArray(JSONArray jsonArr) {

		JSONArray sortedJsonArr = new JSONArray();

		String sortValue = TemenosConstants.TRADEDATE;

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		Collections.sort(jsonValues, new Comparator<JSONObject>() {

			private final String KEY_NAME = sortValue;

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String str1 = new String();
				String str2 = new String();
				str1 = a.has(KEY_NAME) ? (String) a.get(KEY_NAME) : "";
				str2 = b.has(KEY_NAME) ? (String) b.get(KEY_NAME) : "";
				return str1.compareToIgnoreCase(str2);
			}

		});

		for (int i = jsonArr.length() - 1; i >= 0; i--) {
			sortedJsonArr.put(jsonValues.get(i));
		}

		return sortedJsonArr;
	}

	public JSONArray returnSearch(JSONArray array) {
		JSONArray filtedArray = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = null;
			try {
				obj = array.getJSONObject(i);
				String type = obj.getString("orderType");
				if (type.equalsIgnoreCase("Buy") || type.equalsIgnoreCase("Sell")) {
					filtedArray.put(obj);
				}
			} catch (Exception e) {

				alert.prepareError("Error while invoking TAP - "
						+ PortfolioWealthAPIServices.WEALTH_GETDASHBOARDRECENTACTIVITY.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		return filtedArray;
	}

	private String[] getAllPortfolios(String jointPortfolios, JSONArray jArr) {
		String portfolioId = "";
		String[] portfolios = new String[1];
		for (int i = 0; i < jArr.length(); i++) {
			JSONObject portfolioObj = jArr.getJSONObject(i);
			portfolioId = portfolioObj.getString(TemenosConstants.PORTFOLIOID);
			if (portfolioId != null && !portfolioId.equals("")) {
				if (jointPortfolios.equals("")) {
					jointPortfolios = portfolioId;
				} else {
					jointPortfolios = jointPortfolios + "," + portfolioId;
				}
			}
		}
		portfolios[0] = jointPortfolios;
		return portfolios;
	}
}

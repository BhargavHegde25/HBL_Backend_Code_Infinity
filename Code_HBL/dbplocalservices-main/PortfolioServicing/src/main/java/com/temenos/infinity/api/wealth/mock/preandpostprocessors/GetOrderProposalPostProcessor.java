package com.temenos.infinity.api.wealth.mock.preandpostprocessors;

/**
 * @author shreya.singh
 **/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.stream.IntStream;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealth.config.PortfolioWealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetOrderProposalPostProcessor implements DataPostProcessor2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		JSONObject responseN = new JSONObject();
		try {
			String portfolioId = request.getParameterValues(TemenosConstants.PORTFOLIOID)[0];
			String type = request.getParameterValues(TemenosConstants.ORDERSVIEW_TYPE)[0];
			String pastProposalId = request.getParameterValues(TemenosConstants.PASTPROPOSALID)[0];

			if ((type != null && type.equalsIgnoreCase("pastProposal") && portfolioId.equalsIgnoreCase("100777-4")
					|| portfolioId.equalsIgnoreCase("100777-5"))
					|| ((type == null || !type.equalsIgnoreCase("pastProposal"))
							&& (portfolioId.equalsIgnoreCase("100777-5")))) {
				
				String sortBy = request.getParameterValues(TemenosConstants.SORTBY)[0];
				String sortType = request.getParameterValues(TemenosConstants.SORTORDER)[0];
				String search = request.getParameterValues(TemenosConstants.SEARCHBYINSTRUMENTNAME)[0];
				String limitVal = request.getParameterValues(TemenosConstants.PAGESIZE)[0];
				String offsetVal = request.getParameterValues(TemenosConstants.PAGEOFFSET)[0];
				int limit = (limitVal != null && limitVal.trim().length() > 0) ? Integer.parseInt(limitVal) : 0;
				int offset = (offsetVal != null && offsetVal.trim().length() > 0) ? Integer.parseInt(offsetVal) : 0;
				ArrayList<HashMap<String, String>> orderProposalList = new ArrayList<HashMap<String, String>>();

				ArrayList<String> keyList = new ArrayList<String>(
						Arrays.asList("order", "instrumentName", "isinExchange", "ISIN", "exchange", "currentWeight",
								"targetWeight", "orderQty", "orderAmount", "investorDocuments"));
				ArrayList<String> orderList = new ArrayList<String>(
						Arrays.asList("order", "instrumentName",
								 "orderQty", "orderAmount"));
				
				String[][] mockvalues;
				if (type != null && type.equalsIgnoreCase("pastProposal")) {
					String[][] mockvalues_pastProposal = new String[][] {
							{ "Buy", "Apple", "US1912161007 | NYSE", "US1912161007", "NYSE", "1.01", "2.00", "5",
									"492.9", "" },
							{ "Buy", "Google LLC", "US0258161092 | NYSE", "US0258161092", "NYSE", "3.56", "5.80", "20",
									"1160", "" },
							{ "Sell", "Amazon", "US0231351067 | NYSE", "US0231351067", "NYSE", "26.00", "20.00", "50",
									"3203", "" } };
					mockvalues = mockvalues_pastProposal;
					IntStream.range(0, mockvalues.length).forEach((x_count -> {
						HashMap<String, String> hMap = new HashMap<String, String>();
						IntStream.range(0, keyList.size()).forEach((index -> {
							hMap.put(keyList.get(index), mockvalues[x_count][index]);
							if (index == keyList.size() - 1) {
								orderProposalList.add(hMap);
							}
						}));
					}));
				} else {
					String[][] mockvalues_orderProposal = new String[][] {
									
									{ "Buy", "Coca Cola Co", "220", "10843.8"},
									{ "Buy", "American Express Company",  "240", "27840"},
									{ "Sell", "Amazon", "25","3203" }	};	
									
									
									
					mockvalues = mockvalues_orderProposal;
					IntStream.range(0, mockvalues.length).forEach((x_count -> {
						HashMap<String, String> hMap = new HashMap<String, String>();
						IntStream.range(0, orderList.size()).forEach((index -> {
							hMap.put(orderList.get(index), mockvalues[x_count][index]);
							if (index == orderList.size() - 1) {
								orderProposalList.add(hMap);
							}
						}));
					}));
				}


				ArrayList<HashMap<String, String>> finalList = orderProposalList;
				PortfolioWealthUtils obj = new PortfolioWealthUtils();
				if (sortBy != null) {
					finalList = obj.sortArray(finalList, sortBy, sortType);
				}

				if (search != null) {
					finalList = obj.returnSearch(finalList, search, "instrumentName");
				}

				int totalCount = finalList.size();

				if (limit > 0 && offset >= 0) {
					finalList = obj.pagination(offset, limit, finalList);
				}

				if (type != null && type.equalsIgnoreCase("pastProposal")) {
					responseN.put("pastProposal", finalList);
					if (pastProposalId != null && pastProposalId.equalsIgnoreCase("1")) {
						responseN.put("proposaldateTime", "25/12/2021, 00:30 GMT");
					} else if (pastProposalId != null && pastProposalId.equalsIgnoreCase("2")) {
						responseN.put("proposaldateTime", "28/01/2022, 00:30 GMT");
					} else if (pastProposalId != null && pastProposalId.equalsIgnoreCase("3")) {
						responseN.put("proposaldateTime", "24/05/2022, 00:30 GMT");
					}
					responseN.put("totalFeesTaxes", "837.736");
				}else {
					responseN.put("orderProposal", finalList);
				}
				responseN.put("totalOrderAmount", "41886.8");
				responseN.put("orderAmountCurrency", "USD");
				responseN.put("totalCount", totalCount);
			} else {
				if (type != null && type.equalsIgnoreCase("pastProposal")) {
					responseN.put("pastProposal", new ArrayList<String>());
				} else {
					responseN.put("orderProposal", new ArrayList<String>());
					responseN.put("proposaldateTime", "");
				}
				responseN.put("totalOrderAmount", "");
				responseN.put("totalFeesTaxes", "");
				responseN.put("orderAmountCurrency", "");
				responseN.put("totalCount", "");
			}

			responseN.put("portfolioID", portfolioId);
			responseN.put("status", "success");
			responseN.put("opstatus", "0");
			responseN.put("httpStatusCode", "200");
			Result final_result = Utilities.constructResultFromJSONObject(responseN);
			result.appendResult(final_result);
			
		} catch (Exception e) {
			e.getMessage();
			alert.prepareError("Error while invoking GetOrderProposalPostProcessor MOCK - " + e).log();
		}
		return result;
	}

}

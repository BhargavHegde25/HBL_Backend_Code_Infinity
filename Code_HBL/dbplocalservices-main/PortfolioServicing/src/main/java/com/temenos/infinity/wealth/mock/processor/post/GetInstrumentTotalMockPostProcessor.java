/**
 * 
 */
package com.temenos.infinity.wealth.mock.processor.post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTotalMockPostProcessor implements DataPostProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetInstrumentTotalMockPostProcessor Mock - Entered ").log();
		JSONObject responseVal = new JSONObject();
		JSONArray responseArr = new JSONArray();
		JSONObject responseObj = new JSONObject();
		String portfolioId = (String) request.getParameter(TemenosConstants.PORTFOLIOID);
		String graphDuration = (String) request.getParameter(TemenosConstants.GRAPHDURATION);
		String navPage = (String) request.getParameter("navPage");
		if (portfolioId.equalsIgnoreCase("100777-1")) {
			responseVal.put("portfolioID", portfolioId);
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "49572.43");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "5023.50");
			responseVal.put("unRealizedPLPercentage", "11.33");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "510.12");
			responseVal.put("accountName", "John Bailey Portfolio 1.");
			responseVal.put("accountNumber", "100777-1");
			responseVal.put("todayPLPercentage", "1.15");
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "16852.13");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "2174.55");
			responseVal.put("unRealizedPLPercentage", "15.13");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "22.11");
			responseVal.put("todayPLPercentage", "0.15");
			responseVal.put("accountName", "John Bailey Portfolio 2.");
			responseVal.put("accountNumber", "100777-2");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "34215.94");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "4065.93");
			responseVal.put("unRealizedPLPercentage", "20.00");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "46.00");
			responseVal.put("todayPLPercentage", "1.15");
			responseVal.put("accountName", "John Bailey Portfolio 3.");
			responseVal.put("accountNumber", "100777-3");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "48881.31");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "4532.38");
			responseVal.put("unRealizedPLPercentage", "9.22");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "510.12");
			responseVal.put("todayPLPercentage", "1.04");
			responseVal.put("accountName", "John Bailey Portfolio 4.");
			responseVal.put("accountNumber", "100777-4");
			responseVal.put("portfolioID", portfolioId);
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "18191.27");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "2331.69");
			responseVal.put("unRealizedPLPercentage", "14.99");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "22.11");
			responseVal.put("todayPLPercentage", "0.14");
			responseVal.put("accountName", "John Bailey Portfolio 5.");
			responseVal.put("accountNumber", "100777-5");
			responseVal.put("portfolioID", portfolioId);
		} else {
			responseVal.put("portfolioID", portfolioId);
			responseVal.put("referenceCurrency", "USD");
			responseVal.put("marketValue", "49572.43");
			responseVal.put("unRealizedPL", "P");
			responseVal.put("unRealizedPLAmount", "5023.50");
			responseVal.put("unRealizedPLPercentage", "11.33");
			responseVal.put("todayPL", "P");
			responseVal.put("todayPLAmount", "510.12");
			responseVal.put("accountName", "John Bailey Portfolio 1.");
			responseVal.put("accountNumber", "100777-1");
			responseVal.put("todayPLPercentage", "1.15");

		}
		responseVal.put("httpStatusCode", "200");
		responseVal.put("opstatus", "0");
		if (navPage.equalsIgnoreCase("Portfolio")) {
			HashMap inputMap = new HashMap();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
			inputMap.put(TemenosConstants.GRAPHDURATION, graphDuration);
			JSONArray objGraph = getGraphData(inputMap);
			responseVal.put(graphDuration, objGraph);
			responseVal.put("graphDuration", graphDuration);
			responseArr.put(response);
			responseObj.put("instrumentTotal", responseArr);
		}
		Result final_result = Utilities.constructResultFromJSONObject(responseObj);
		final_result.addOpstatusParam("0");
		final_result.addHttpStatusCodeParam("200");
		final_result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
		diagnostic.prepareDebug("==========> GetInstrumentTotalMockPostProcessor Mock - Exited ").log();
		return final_result;
	}

	/**
	 * (INFO) Fetches the array of objects containing the x-axis labels(timestamp)
	 * and the corresponding data point to be plotted for the Portfolio details tab.
	 * The series of timestamp is created based on the input filter values such as
	 * "1Year", "1Day", "1Month" etc. Alter the list value for the corresponding
	 * symbol.
	 * 
	 * @param inputMap
	 * @author 22952
	 */
	@SuppressWarnings({ "unused", "static-access" })
	public JSONArray getGraphData(Map<String, Object> inputMap) {
		diagnostic.prepareDebug("==========> getGraphData Mock - Entered ").log();
		String portfolioId = (String) inputMap.get(TemenosConstants.PORTFOLIOID);
		String graphDuration = (String) inputMap.get(TemenosConstants.GRAPHDURATION);
		String[] XaxisArray = new String[30];
		Double[] XaxisArrayPerc, XaxisArrayVal;

		if (portfolioId.equalsIgnoreCase("100777-1")) {
			XaxisArrayVal = new Double[] { 47600.12, 48400.22, 47500.82, 48800.33, 49200.22, 49000.65, 48700.22,
					47800.12, 48700.33, 49000.01, 49000.88, 47600.68, 47800.72, 48200.33, 47600.12, 48200.43, 48700.77,
					48000.2, 49900.33, 47700.00, 47200.00, 47200.5, 47700.3, 47800.0, 48500.0, 47200.0, 48200.11,
					47700.12, 49500.23, 48700.3, 49572.43 };
		} else if (portfolioId.equalsIgnoreCase("100777-2")) {
			XaxisArrayVal = new Double[] { 14600.12, 15100.22, 15400.82, 15300.33, 16000.22, 15800.65, 15600.22,
					15300.12, 15600.33, 15800.01, 15800.88, 14600.68, 14800.72, 15000.33, 14600.12, 15000.43, 16600.77,
					15800.2, 16900.33, 15200.00, 15000.00, 15000.5, 15200.3, 15300.0, 15400.0, 15000.0, 15000.11,
					14800.12, 15400.23, 15200.3, 16852.13 };
		} else if (portfolioId.equalsIgnoreCase("100777-3")) {
			XaxisArrayVal = new Double[] { 32600.12, 33100.22, 33400.82, 33300.33, 34000.22, 33800.65, 33600.22,
					33300.12, 33600.33, 33800.01, 33800.88, 32600.68, 32800.72, 33000.33, 32600.12, 33000.43, 34600.77,
					33800.2, 34900.33, 33200.00, 33000.00, 33000.5, 33200.3, 33300.0, 33400.0, 32800.0, 33000.11,
					32800.12, 33400.23, 33200.3, 34215.94 };
		} else if (portfolioId.equalsIgnoreCase("100777-4")) {
			XaxisArrayVal = new Double[] { 40765.12, 43768.22, 45621.82, 48120.33, 42679.22, 43567.65, 42456.22,
					41234.12, 44567.33, 44445.01, 43215.88, 45345.68, 45879.72, 46578.33, 46789.12, 47324.43, 47908.77,
					48104.2, 41789.33, 41234.00, 45679.00, 46752.5, 46444.3, 45454.0, 46578.0, 44213.0, 44567.11,
					45678.12, 46091.23, 48881.31 };
		} else if (portfolioId.equalsIgnoreCase("100777-5")) {
			XaxisArrayVal = new Double[] { 16546.12, 17590.22, 16103.82, 15898.33, 15030.22, 14987.65, 14352.22,
					13100.12, 13433.33, 12097.01, 11111.88, 13987.68, 13824.72, 13731.33, 13567.12, 13999.43, 14154.77,
					14765.2, 15670.33, 15742.00, 16089.00, 16951.5, 17221.3, 17459.0, 15346.0, 14098.0, 14999.11,
					16678.12, 17728.23, 17933.3, 18191.27 };
		} else {
			XaxisArrayVal = new Double[] { 47600.12, 48400.22, 47500.82, 48800.33, 49200.22, 49000.65, 48700.22,
					47800.12, 48700.33, 49000.01, 49000.88, 47600.68, 47800.72, 48200.33, 47600.12, 48200.43, 48700.77,
					48000.2, 49900.33, 47700.00, 47200.00, 47200.5, 47700.3, 47800.0, 48500.0, 47200.0, 48200.11,
					47700.12, 49500.23, 48700.3, 49572.43 };
		}

		JSONArray assetArray = new JSONArray();
		if (graphDuration.equalsIgnoreCase("OneM")) {
			Date today = new Date();
			Calendar dates = Calendar.getInstance();
			dates.add(Calendar.DAY_OF_MONTH, -30);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			for (int i = 0; i < 29; i++) {
				dates.add(Calendar.DATE, 1);
				XaxisArray[i] = df.format(dates.getTime());

			}
			XaxisArray[29] = df.format(today.getTime());
			XaxisArrayPerc = new Double[] {};
			for (int i = 0; i < XaxisArray.length; i++) {
				JSONObject response = new JSONObject();
				response.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
				response.put("AMOUNT", XaxisArrayVal[i]);
				response.put("PERCENTAGE", i + 0.25);
				assetArray.put(response);
			}
		}
		if (graphDuration.equalsIgnoreCase("OneY")) {
			if (portfolioId.equalsIgnoreCase("100777-1")) {
				XaxisArrayVal = new Double[] { 46600.12, 46500.12, 47400.22, 48500.82, 47800.33, 49200.22, 49000.65,
						48700.22, 47800.12, 48100.33, 48200.33, 48300.01, 48400.01, 48300.88, 48400.88, 40600.68,
						46700.72, 47100.33, 47200.33, 40600.12, 40700.12, 47200.43, 48700.77, 49000.2, 49600.2,
						49700.33, 47500.00, 40600.00, 47700.0, 47200.0, 47200.5, 47400.5, 47700.3, 47800.0, 48000.0,
						47200.0, 47100.0, 47200.11, 40700.12, 40700.12, 47400.23, 48000.23, 47700.3, 47300.3, 47200.21,
						49572.43 };
			} else if (portfolioId.equalsIgnoreCase("100777-2")) {
				XaxisArrayVal = new Double[] { 14600.12, 14500.12, 15100.22, 15400.82, 15300.33, 16000.22, 15800.65,
						15600.22, 15300.12, 15500.33, 15600.33, 15700.01, 15800.01, 15700.88, 15800.88, 14600.68,
						16800.72, 14900.33, 15000.33, 14600.12, 14800.12, 15000.43, 15600.77, 15800.2, 16100.2,
						16900.33, 15200.00, 15000.00, 15200.0, 15000.0, 15000.5, 15100.5, 15200.3, 15300.0, 15400.0,
						16000.0, 14900.0, 15000.11, 14800.12, 14700.12, 15100.23, 15400.23, 15200.3, 15100.3, 15000.21,
						16852.13 };
			} else if (portfolioId.equalsIgnoreCase("100777-3")) {
				XaxisArrayVal = new Double[] { 32600.12, 32500.12, 32100.22, 33400.82, 33300.33, 34000.22, 33800.65,
						33600.22, 33300.12, 33500.33, 33600.33, 33700.01, 33800.01, 33700.88, 33800.88, 32600.68,
						34800.72, 32900.33, 33000.33, 32600.12, 32800.12, 33000.43, 33600.77, 33800.2, 34100.2,
						34900.33, 33200.00, 33000.00, 33200.0, 33000.0, 33000.5, 33100.5, 33200.3, 33300.0, 33400.0,
						34000.0, 32900.0, 33000.11, 32800.12, 32700.12, 33100.23, 33400.23, 33200.3, 33100.3, 33000.21,
						34215.94 };
			} else if (portfolioId.equalsIgnoreCase("100777-4")) {
				XaxisArrayVal = new Double[] { 44362.12, 40987.12, 41781.22, 42000.82, 42355.33, 42906.22, 43157.65,
						47890.22, 48123.12, 48599.33, 47100.33, 46868.01, 46533.01, 46111.88, 45899.88, 45189.68,
						44607.72, 44257.33, 43988.33, 43245.12, 42978.12, 43789.43, 43107.77, 42675.2, 42270.2,
						43567.33, 44678.00, 45536.00, 46908.0, 47000.0, 47017.5, 48170.5, 46289.3, 46789.0, 46782.0,
						45672.0, 44728.0, 44123.11, 43210.12, 45678.12, 46789.23, 47123.23, 47356.3, 47657.3, 48645.21,
						48881.31 };
			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				XaxisArrayVal = new Double[] { 16546.12, 17590.22, 16103.82, 15898.33, 15030.22, 14987.65, 14352.22,
						13100.12, 13433.33, 12097.01, 11111.88, 13987.68, 13824.72, 13731.33, 13567.12, 13999.43,
						14154.77, 13100.12, 13433.33, 12097.01, 11111.88, 13987.68, 14765.2, 15670.33, 15742.00,
						16089.00, 16546.12, 17590.22, 16103.82, 15898.33, 15030.22, 11111.88, 13987.68, 13824.72,
						13731.33, 13567.12, 16951.5, 17221.3, 17459.0, 15346.0, 14098.0, 14999.11, 16678.12, 17728.23,
						17933.3, 18191.27 };
			} else {
				XaxisArrayVal = new Double[] { 46600.12, 46500.12, 47400.22, 48500.82, 47800.33, 49200.22, 49000.65,
						48700.22, 47800.12, 48100.33, 48200.33, 48300.01, 48400.01, 48300.88, 48400.88, 40600.68,
						46700.72, 47100.33, 47200.33, 40600.12, 40700.12, 47200.43, 48700.77, 49000.2, 49600.2,
						49700.33, 47500.00, 40600.00, 47700.0, 47200.0, 47200.5, 47400.5, 47700.3, 47800.0, 48000.0,
						47200.0, 47100.0, 47200.11, 40700.12, 40700.12, 47400.23, 48000.23, 47700.3, 47300.3, 47200.21,
						49572.43 };
			}
			String[] months = PortfolioServiceUtils.nextMonths();
			XaxisArray = PortfolioServiceUtils.getMonths(months);
			for (int i = 0; i < XaxisArray.length; i++) {
				JSONObject response = new JSONObject();
				response.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
				response.put("AMOUNT", XaxisArrayVal[i]);
				response.put("PERCENTAGE", i + 0.25);
				assetArray.put(response);
			}
		}
		if (graphDuration.equalsIgnoreCase("FiveY")) {
			Calendar c = Calendar.getInstance();
			Calendar backDate = Calendar.getInstance();
			int firstYear = c.getInstance().get(Calendar.YEAR) - 4;
			backDate.set(firstYear, Calendar.JANUARY, 06, 00, 00, 00);

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			XaxisArray[0] = df.format(backDate.getTime());
			int index_count = 1;
			while (backDate.before(Calendar.getInstance())) {
				backDate.add(Calendar.MONTH, 3);
				XaxisArray[index_count] = df.format(backDate.getTime());
				index_count++;
			}

			XaxisArray[index_count - 1] = df.format(c.getTime());
			for (int i = 0; i < index_count; i++) {
				JSONObject response = new JSONObject();
				response.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
				response.put("AMOUNT", XaxisArrayVal[i]);
				response.put("PERCENTAGE", i + 0.25);
				assetArray.put(response);
			}
		}
		if (graphDuration.equalsIgnoreCase("YTD")) {
			if (portfolioId.equalsIgnoreCase("100777-1")) {
				XaxisArrayVal = new Double[] { 46600.12, 46500.12, 48300.22, 48000.82, 48900.33, 49200.22, 49000.65,
						47800.22, 48500.12, 48500.33, 48700.33, 49100.01, 49300.01, 48800.88, 47700.88, 46600.68,
						47800.72, 46700.33, 48500.33, 46600.12, 46800.12, 48200.43, 47400.77, 48500.2, 49700.2,
						50000.33, 49800.12, 48200.43, 48700.00, 48200.00, 48700.0, 48500.0, 48500.5, 48600.5, 48950.3,
						48100.0, 48500.0, 48600.0, 48500.0, 48650.11, 48700.12, 46600.12, 48600.23, 48000.50, 48700.3,
						47600.3, 48200.21, 49572.43 };
			} else if (portfolioId.equalsIgnoreCase("100777-2")) {
				XaxisArrayVal = new Double[] { 14600.12, 14500.12, 15100.22, 15400.82, 15300.33, 16000.22, 16500.65,
						14600.22, 15300.12, 15500.33, 15600.33, 15700.01, 16000.01, 15700.88, 15800.88, 14600.68,
						14000.72, 14900.33, 15000.33, 14600.12, 14800.12, 15000.43, 15600.77, 15800.2, 16700.2,
						16900.33, 16700.12, 15000.43, 15200.00, 15000.00, 15200.0, 15000.0, 15000.5, 15100.5, 15200.3,
						15300.0, 15400.0, 15000.0, 14900.0, 15000.11, 14800.12, 14700.12, 15100.23, 15400.23, 15200.3,
						15100.3, 15000.21, 16852.13 };
			} else if (portfolioId.equalsIgnoreCase("100777-3")) {
				XaxisArrayVal = new Double[] { 32600.12, 32500.12, 32100.22, 33400.82, 33300.33, 34000.22, 33800.65,
						33600.22, 33300.12, 33500.33, 33600.33, 33700.01, 33800.01, 33700.88, 33800.88, 32600.68,
						34800.72, 32900.33, 33000.33, 32600.12, 32800.12, 33000.43, 33600.77, 33800.2, 34100.2,
						34900.33, 33200.00, 33000.00, 33200.0, 33000.0, 33000.5, 33100.5, 33200.3, 33300.0, 33400.0,
						34000.0, 32900.0, 33000.11, 32800.12, 32700.12, 33100.23, 33400.23, 33200.3, 33100.3, 33000.21,
						33100.3, 33000.21, 34215.94 };
			} else if (portfolioId.equalsIgnoreCase("100777-4")) {
				XaxisArrayVal = new Double[] { 44362.12, 44100.46, 44145.30, 40987.12, 41781.22, 42000.82, 42355.33,
						42906.22, 43157.65, 47890.22, 48123.12, 48444.44, 48599.33, 47100.33, 46868.01, 46533.01,
						46111.88, 45899.88, 45189.68, 44607.72, 44257.33, 43988.33, 43245.12, 43100.67, 42978.12,
						43789.43, 43107.77, 42675.2, 42270.2, 43567.33, 44678.00, 45536.00, 45678.00, 46536.00, 46908.0,
						47000.0, 47017.5, 48170.5, 46289.3, 46789.0, 46782.0, 45672.0, 44728.0, 44123.11, 43210.12,
						45678.12, 45999.99, 46789.23, 47123.23, 47356.3, 47657.3, 48645.21, 48881.31 };
			} else if (portfolioId.equalsIgnoreCase("100777-5")) {
				XaxisArrayVal = new Double[] { 16546.12, 17590.22, 17891.56, 16103.82, 15898.33, 15030.22, 14987.65,
						14352.22, 13100.12, 13433.33, 12097.01, 11111.88, 12873.89, 13156.68, 13824.72, 13731.33,
						13567.12, 13999.43, 14154.77, 13300.12, 13838.33, 12067.01, 11517.88, 13987.68, 14765.2,
						15670.33, 15742.00, 16089.00, 16546.12, 17590.22, 16103.82, 15898.33, 15030.22, 11111.88,
						12057.22, 14141.88, 13387.68, 13824.72, 13731.33, 13567.12, 16951.5, 17221.3, 17459.0, 15346.0,
						14098.0, 14999.11, 16678.12, 16902.49, 17119.29, 17728.23, 17933.3, 18191.27 };
			} else {
				XaxisArrayVal = new Double[] { 46600.12, 46500.12, 48300.22, 48000.82, 48900.33, 49200.22, 49000.65,
						47800.22, 48500.12, 48500.33, 48700.33, 49100.01, 49300.01, 48800.88, 47700.88, 46600.68,
						47800.72, 46700.33, 48500.33, 46600.12, 46800.12, 48200.43, 47400.77, 48500.2, 49700.2,
						50000.33, 49800.12, 48200.43, 48700.00, 48200.00, 48700.0, 48500.0, 48500.5, 48600.5, 48950.3,
						48100.0, 48500.0, 48600.0, 48500.0, 48650.11, 48700.12, 46600.12, 48600.23, 48000.50, 48700.3,
						47600.3, 48200.21, 49572.43 };
			}

			String[] months = PortfolioServiceUtils.prevMonths();
			XaxisArray = PortfolioServiceUtils.getPrevMonths(months);
			for (int i = 0; i < XaxisArray.length; i++) {
				JSONObject response = new JSONObject();
				response.put("TIMESTAMP", XaxisArray[i].concat("+05:30"));
				response.put("AMOUNT", XaxisArrayVal[i]);
				response.put("PERCENTAGE", i + 0.25);
				assetArray.put(response);
			}
		}

		diagnostic.prepareDebug("==========> getGraphData Mock - Exited ").log();
		return assetArray;

	}

}

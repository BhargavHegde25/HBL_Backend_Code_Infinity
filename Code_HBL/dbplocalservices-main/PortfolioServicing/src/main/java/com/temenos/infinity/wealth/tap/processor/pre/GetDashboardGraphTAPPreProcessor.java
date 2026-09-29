/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author muthukumarv
 *
 */
public class GetDashboardGraphTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			diagnostic.prepareDebug("==========>  GetDashboardGraphTAPPreProcessor TAP - Token Generation Succeeded").log();
			diagnostic.prepareDebug("==========>  GetDashboardGraphTAPPreProcessor TAP - Begin:Manipulation of input parameters").log();
			String graphDuration = "";
			boolean isFirstTime = false;
			String perfMetric = (EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_PRFM_METRIC,request));
			if (request.getParameter(TemenosConstants.GRAPHDURATION) == null || request.getParameter(TemenosConstants.GRAPHDURATION).isEmpty()) {
				if (perfMetric.equalsIgnoreCase(TemenosConstants.OneY)) {
					graphDuration = TemenosConstants.ONEYEAR;
				}else
				{
					graphDuration=perfMetric;
				}
				isFirstTime = true;
			} else {
				graphDuration = request.getParameter(TemenosConstants.GRAPHDURATION).toString();
			}
			String startDate = null,endDate;
			Calendar edt = Calendar.getInstance();
			SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
			endDate = sdformat.format(edt.getTime());
			if(graphDuration.equalsIgnoreCase("OneM"))
			{
				Calendar sdt = Calendar.getInstance();
				sdt.add(Calendar.MONTH, -1);
				startDate = sdformat.format(sdt.getTime());
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - date calculated for OneM ").log();
			}
			else if(graphDuration.equalsIgnoreCase("OneY"))
			{
				Calendar sdt = Calendar.getInstance();
				sdt.add(Calendar.YEAR, -1);
				startDate = sdformat.format(sdt.getTime());
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - date calculated for OneY ").log();
			}
			else if(graphDuration.equalsIgnoreCase("FiveY"))
			{
				Calendar sdt = Calendar.getInstance();
				sdt.add(Calendar.YEAR, -5);
				startDate = sdformat.format(sdt.getTime());
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - date calculated for FiveY ").log();
			}
			else if(graphDuration.equalsIgnoreCase("YTD")) {
				Calendar sdt = Calendar.getInstance();
				int Year = sdt.get(Calendar.YEAR)-1;
				startDate = String.valueOf(Year).concat("-12-31");
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - date calculated for YTD ").log();
			}
			else {
				diagnostic.prepareDebug("==========> GetDashboardGraphTAPPreProcessor TAP - date calculated else case").log();
			}
	        inputMap.put(TemenosConstants.DATEFROM, startDate);
	        inputMap.put(TemenosConstants.DATETO, endDate);
	        inputMap.put("orderBy","PERIOD_FINAL_DATE%20asc");
			//inputMap.put(TemenosConstants.DATEFROM, "2021-05-01");
	        //inputMap.put(TemenosConstants.DATETO, "2021-05-31");
	        diagnostic.prepareDebug("==========>  GetDashboardGraphTAPPreProcessor TAP - End:Manipulation of input parameters").log();
			return true;
			}
			else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetDashboardGraphTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  GetDashboardGraphTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}

/**
 * 
 */
package com.temenos.infinity.wealthorder.tap.processor.pre;

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
 * 
 * (INFO) Prepares the input for the TAP service in the desired format.
 * 
 * @author padmasris
 *
 */
public class getCurrencyGraphTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========>  getCurrencyGraphTAPPreProcessor TAP - Entered").log();
		try {
			String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
			if (wealthCore != null
					&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("TAP"))) {
				String graphDuration = request.getParameter(TemenosConstants.DATEORPERIOD).toString();
				String startDate = null, endDate;
				Calendar edt = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				endDate = sdformat.format(edt.getTime());
				if (graphDuration.equalsIgnoreCase("1M")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.MONTH, -1);
					startDate = sdformat.format(sdt.getTime());
				} else if (graphDuration.equalsIgnoreCase("1Y")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.YEAR, -1);
					startDate = sdformat.format(sdt.getTime());
				} else if (graphDuration.equalsIgnoreCase("1D")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.DATE, -1);
					startDate = sdformat.format(sdt.getTime());
				} else if (graphDuration.equalsIgnoreCase("YTD")) {
					Calendar sdt = Calendar.getInstance();
					int Year = sdt.get(Calendar.YEAR);
					startDate = String.valueOf(Year).concat("-01-01");
				} else {

				}
				inputMap.put(TemenosConstants.DATEFROM, startDate);
				inputMap.put(TemenosConstants.DATETO, endDate);
				inputMap.put(TemenosConstants.INSTRUMENTCODE, request.getParameter("instrumentId"));
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  getCurrencyGraphTAPPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				result.addParam("flagHis", "false");
				diagnostic.prepareDebug("==========>  getCurrencyGraphTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  getCurrencyGraphTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}

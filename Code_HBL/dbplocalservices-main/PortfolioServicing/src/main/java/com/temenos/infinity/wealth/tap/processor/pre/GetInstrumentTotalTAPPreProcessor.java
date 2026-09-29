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

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTotalTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
				diagnostic.prepareDebug("==========>  GetInstrumentTotalTAPPreProcessor TAP - Begin:Manipulation of input parameters").log();
				String graphDuration = "";
				if (request.getParameter(TemenosConstants.GRAPHDURATION) == null) {
					graphDuration = "OneM";
				} else {
					graphDuration = request.getParameter(TemenosConstants.GRAPHDURATION).toString();
				}
				String startDate = null, endDate;
				Calendar edt = Calendar.getInstance();
				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
				endDate = sdformat.format(edt.getTime());
				if (graphDuration.equalsIgnoreCase("OneM")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.MONTH, -1);
					startDate = sdformat.format(sdt.getTime());
					diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPreProcessor TAP - date calculated for OneM ").log();
				} else if (graphDuration.equalsIgnoreCase("OneY")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.YEAR, -1);
					startDate = sdformat.format(sdt.getTime());
					diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPreProcessor TAP - date calculated for OneY ").log();
				} else if (graphDuration.equalsIgnoreCase("FiveY")) {
					Calendar sdt = Calendar.getInstance();
					sdt.add(Calendar.YEAR, -5);
					startDate = sdformat.format(sdt.getTime());
					diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPreProcessor TAP - date calculated for FiveY ").log();
				} else if (graphDuration.equalsIgnoreCase("YTD")) {
					Calendar sdt = Calendar.getInstance();
					int Year = sdt.get(Calendar.YEAR);
					startDate = String.valueOf(Year).concat("-01-01");
					diagnostic.prepareDebug("==========> GetInstrumentTotalTAPPreProcessor TAP - date calculated for YTD ").log();
				} else {

				}
				inputMap.put(TemenosConstants.DATEFROM, startDate);
				inputMap.put(TemenosConstants.DATETO, endDate);
				diagnostic.prepareDebug("==========>  GetInstrumentTotalTAPPreProcessor TAP - End:Manipulation of input parameters").log();
				TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
				obj.execute(inputMap, request, response, result);
				diagnostic.prepareDebug("==========>  GetInstrumentTotalTAPPreProcessor TAP - Token Generation Succeeded").log();
				return true;
			} else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  GetInstrumentTotalTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  GetInstrumentTotalTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}

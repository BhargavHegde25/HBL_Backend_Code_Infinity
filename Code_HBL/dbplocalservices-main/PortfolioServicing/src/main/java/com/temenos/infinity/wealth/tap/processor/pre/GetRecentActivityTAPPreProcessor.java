package com.temenos.infinity.wealth.tap.processor.pre;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class GetRecentActivityTAPPreProcessor implements DataPreProcessor2 {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		diagnostic.prepareDebug("==========> GetRecentActivityTAPPreProcessor TAP - Entered ").log();
		String wealthCore = EnvironmentConfigurationsHandler.getValue(TemenosConstants.WEALTH_CORE, request);
		if (wealthCore != null
				&& (wealthCore.equalsIgnoreCase("TAP,Refinitiv") || wealthCore.equalsIgnoreCase("TAP"))) {
			int no_of_days = Integer.parseInt(EnvironmentConfigurationsHandler
					.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS, request).toString().trim() == null ? "7"
							: EnvironmentConfigurationsHandler
									.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS, request).toString().trim());
			// int no_of_days = 1000;
			diagnostic.prepareDebug("==========>  GetRecentActivityTAPPreProcessor TAP - Environment variables fetched and set").log();
			Calendar sdt = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String toDate = sdf.format(sdt.getTime());
			Calendar edt = Calendar.getInstance();
			edt.add(Calendar.DATE, -no_of_days);
			String fromDate = sdf.format(edt.getTime());
			inputMap.put(TemenosConstants.DATEFROM, fromDate);
			inputMap.put(TemenosConstants.DATETO, toDate);
			diagnostic.prepareDebug("==========>  GetRecentActivityTAPPreProcessor TAP - End:Manipulation of input parameters").log();
			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			diagnostic.prepareDebug("==========>  GetRecentActivityTAPPreProcessor TAP - Token Generation Succeeded").log();
			return true;
		} else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
			diagnostic.prepareDebug("==========>  GetRecentActivityTAPPreProcessor TAP - Exiting without Token Generation").log();
			return false;
		}
	}

}

/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.tap.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * 
 * (INFO) Prepares the input for the TAP service in the desired format.
 * 
 * @author himaja.sridhar
 *
 */
public class GetInstrumentTotalPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
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
		} else if (graphDuration.equalsIgnoreCase("OneY")) {
			Calendar sdt = Calendar.getInstance();
			sdt.add(Calendar.YEAR, -1);
			startDate = sdformat.format(sdt.getTime());
		} else if (graphDuration.equalsIgnoreCase("FiveY")) {
			Calendar sdt = Calendar.getInstance();
			sdt.add(Calendar.YEAR, -5);
			startDate = sdformat.format(sdt.getTime());
		} else if (graphDuration.equalsIgnoreCase("YTD")) {
			Calendar sdt = Calendar.getInstance();
			int Year = sdt.get(Calendar.YEAR);
			startDate = String.valueOf(Year).concat("-01-01");
		} else {

		}
		inputMap.put(TemenosConstants.DATEFROM, startDate);
		inputMap.put(TemenosConstants.DATETO, endDate);

		return true;
	}

}

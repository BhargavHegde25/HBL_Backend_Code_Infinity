/**
 * 
 */
package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

/**
 * @author himaja.sridhar
 *
 */
public class GetPortfolioPerformancePreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String graphDuration = request.getParameter(TemenosConstants.DURATION).toString();
		String startDate , endDate = null ;
		Calendar edt = Calendar.getInstance();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		endDate = sdformat.format(edt.getTime());
		if (graphDuration.equalsIgnoreCase("OneY")) {
			Calendar sdt = Calendar.getInstance();
			sdt.add(Calendar.YEAR, -1);
			startDate = sdformat.format(sdt.getTime());
		} else if (graphDuration.equalsIgnoreCase("YTD")) {
			Calendar sdt = Calendar.getInstance();
			int Year = sdt.get(Calendar.YEAR);
			startDate = String.valueOf(Year).concat("-01-01");
		} else {
			String st =inputMap.get(TemenosConstants.DATEFROM).toString();
			String syear = st.substring(0, 4);
			String smonth =st.substring(4, 6);
			String sdate =st.substring(6, 8);
			startDate = syear.concat("-").concat(smonth).concat("-").concat(sdate);
			
			String et = inputMap.get(TemenosConstants.DATETO).toString();
			String eyear = et.substring(0, 4);
			String emonth =et.substring(4, 6);
			String edate =et.substring(6, 8);
			endDate = eyear.concat("-").concat(emonth).concat("-").concat(edate);
		}
		inputMap.put(TemenosConstants.DATEFROM, startDate);
		inputMap.put(TemenosConstants.DATETO, endDate);
		return true;
	}

}

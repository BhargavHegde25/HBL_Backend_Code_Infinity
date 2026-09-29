/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.preandpostprocessors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * (INFO) Prepares the input to the Refinitiv service in the desired format. 
 * 
 * @author himaja.sridhar
 *
 */
public class GetHistoricalDataInputPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		inputMap.put("EndTime", df.format(Calendar.getInstance().getTime()));
		String ric = request.getParameter("ric") == null ? request.getParameter("RICCode") : request.getParameter("ric");
		inputMap.put("Symbol", ric);
		Calendar startDate = Calendar.getInstance();
		if (request.getParameter("dateOrPeriod").equalsIgnoreCase("1D")) {
			inputMap.put("Interval", "30MINUTES");
			ZonedDateTime zonedTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
			if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
				zonedTime = zonedTime.minusDays(1);
			} else if (zonedTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
				zonedTime = zonedTime.minusDays(2);
			} else {
				if (zonedTime.getHour() < 9 || (zonedTime.getHour() == 9 && zonedTime.getMinute() < 30)) {
					System.out.println("inside if");
					zonedTime = zonedTime.minusDays(1);

					if (zonedTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
						zonedTime = zonedTime.minusDays(1);
					} else if (zonedTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
						zonedTime = zonedTime.minusDays(2);
					}
				}
			}

			zonedTime = zonedTime.withHour(0);
			zonedTime = zonedTime.withMinute(0);
			startDate = GregorianCalendar.from(zonedTime);
		} else if (request.getParameter("dateOrPeriod").equalsIgnoreCase("1Y")) {
			inputMap.put("Interval", "DAILY");
			startDate.add(Calendar.DATE, -365);
		} else if (request.getParameter("dateOrPeriod").equalsIgnoreCase("1M")) {
			inputMap.put("Interval", "DAILY");
			startDate.add(Calendar.DATE, -30);
		} else if (request.getParameter("dateOrPeriod").equalsIgnoreCase("5Y")) {
			inputMap.put("Interval", "DAILY");
			int firstYear = Calendar.getInstance().get(Calendar.YEAR) - 4;
			startDate.set(firstYear, Calendar.JANUARY, 01, 00, 00, 00);
		} else if (request.getParameter("dateOrPeriod").equalsIgnoreCase("YTD")) {
			inputMap.put("Interval", "DAILY");
			int year = Calendar.getInstance().get(Calendar.YEAR);
			startDate.set(year, Calendar.JANUARY, 01, 00, 00, 00);
		}
		inputMap.put("StartTime", df.format(startDate.getTime()));
		return true;
	}

}

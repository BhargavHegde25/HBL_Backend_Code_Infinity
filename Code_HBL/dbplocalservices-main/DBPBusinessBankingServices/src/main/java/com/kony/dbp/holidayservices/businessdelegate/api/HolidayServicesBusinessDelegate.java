package com.kony.dbp.holidayservices.businessdelegate.api;
import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.holidayservices.dto.HolidaysDTO;
/**
 * 
 * @author KH2394
 * @version 1.0
 * Interface for HolidaysBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface HolidayServicesBusinessDelegate extends BusinessDelegate {
	/**
	 *  method to get the Holidays 
	 *  return list of {@link HolidaysDTO}
	 */
	public List<HolidaysDTO> getHolidays();
}

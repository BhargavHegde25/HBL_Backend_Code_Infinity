package com.temenos.infinity.api.savingspot.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.savingspot.constants.TemenosConstants;

public class SavingsPotUtilites {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	/**
	 * Returns the remaining balances
	 * 
	 * @param key
	 * @return
	 */
	public static String getRemainingBalances(String targetAmount, String availableBalance) {
		if (StringUtils.isNotBlank(targetAmount) && StringUtils.isNotBlank(availableBalance)) {
			float remainingBal=0;
			float target = Float.parseFloat(targetAmount);
			float avail = Float.parseFloat(availableBalance);
			if(target>=avail)
			{
				remainingBal=target - avail;
			}
			return String.valueOf(remainingBal);
			
		}
		return null;
	}

	/**
	 * Get frequencyDay from the startDate
	 * 
	 * @param startDate
	 * @param frequency
	 * @param potType
	 * @return frequencyDay
	 * @author 19459
	 */
	@SuppressWarnings("deprecation")
	public static String getFrequencyDay(String startDate, String frequency) {
		String frequencyDay = null;
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(frequency)) {
			return frequencyDay;
		}
		if (frequency.equalsIgnoreCase(TemenosConstants.MONTHLY)) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			try {
				date = format.parse(startDate);
			} catch (ParseException e) {
				alert.prepareError(e.toString()).log();
			}
			int dateInt = date.getDate();
			if(dateInt<10)
			{
				frequencyDay = "0"+dateInt+ " of Every Month";
			}else
			{
				frequencyDay = dateInt + " of Every Month";
			}
	

		} else if (frequency.equalsIgnoreCase(TemenosConstants.BIWEEKLY)) {
			DayOfWeek day = LocalDate.parse(startDate).getDayOfWeek();
			frequencyDay = day.getDisplayName(TextStyle.FULL, Locale.US);
		}
		return frequencyDay;
	}

	/**
	 * Get potCurrentStatus
	 * 
	 * @param startDate
	 * @param frequency
	 * @param potType
	 * @return frequencyDay
	 * @author 19459
	 */
	public static String getPotCurrentStatus(String targetAmount, String availableBalance, String potType,
			String startDate, String contribution, String frequency) {
		String potCurrentStatus = null;
		if (StringUtils.isBlank(targetAmount) || StringUtils.isBlank(availableBalance)
				|| StringUtils.isBlank(potType)) {
			return potCurrentStatus;
		}
		int target = Math.round(Float.parseFloat(targetAmount));
		int avail = Math.round(Float.parseFloat(availableBalance));
		
		if (target <= avail) {
			potCurrentStatus = TemenosConstants.COMPLETED;
		} else if (potType.equalsIgnoreCase(TemenosConstants.GOAL)) {
			if (StringUtils.isBlank(frequency) || StringUtils.isBlank(startDate)
					|| StringUtils.isBlank(contribution)) {
				return potCurrentStatus;
			}
			float periodicContribution = Float.parseFloat(contribution);
			LocalDate dateStart = LocalDate.parse(startDate);
			LocalDate dateEnd = LocalDate.now();
			if (frequency.equalsIgnoreCase(TemenosConstants.MONTHLY)) {
				dateEnd = dateEnd.minusDays(1);
				long monthsDiff = ChronoUnit.MONTHS.between(dateStart, dateEnd);
				int monthlySchedules = (int) monthsDiff;
				if (avail >= (monthlySchedules * periodicContribution)) {
					potCurrentStatus = TemenosConstants.ONTRACK;
				} else {
					potCurrentStatus = TemenosConstants.NOT_ONTRACK;
				}
			} else if ((frequency.equalsIgnoreCase(TemenosConstants.BI_WEEKLY)) || (frequency.equalsIgnoreCase(TemenosConstants.BIWEEKLY)))  {
				long daysBetween = ChronoUnit.DAYS.between(dateStart, dateEnd);
				int daySchedules = (int) (daysBetween / 14);
				float biweeklycontribution = periodicContribution / 2;
				if (avail >= (daySchedules * biweeklycontribution)) {
					potCurrentStatus = TemenosConstants.ONTRACK;
				} else {
					potCurrentStatus = TemenosConstants.NOT_ONTRACK;
				}
			}
		}else if (Float.parseFloat(availableBalance)==0) {
			potCurrentStatus = TemenosConstants.YETTOFUND;
		}else if (avail<target) {
			potCurrentStatus = TemenosConstants.PARTIALLYFUNDED;
		}
		return potCurrentStatus;

	}

	/**
	 * Returns the potAmountPercentage
	 * 
	 * @param targetAmount
	 * @param availableBalance
	 * @return potAmountPercentage
	 */
	public static String getPotAmountPercentage(String targetAmount, String availableBalance) {
		if (StringUtils.isNotBlank(targetAmount) && StringUtils.isNotBlank(availableBalance)) {
			double target = Float.parseFloat(targetAmount);
			double avail = Float.parseFloat(availableBalance);
			int percentage = (int) ((avail / target) * 100);
			if(percentage > 100)
				percentage = 100;
			return String.valueOf(percentage);
		}
		return null;
	}

	/**
	 * Returns the MonthsLeftForCompletion
	 *
	 * @param dateEnd
	 * @return months
	 */
	public static long getMonthsLeft(String dateEnd) {
		if (StringUtils.isNotBlank(dateEnd)) {
			LocalDate currentDate = LocalDate.now();
			LocalDate endDate = LocalDate.parse(dateEnd);
			long months = ChronoUnit.MONTHS.between(currentDate, endDate);
			return months;
		}
		return 0;
	}

	/**
	 * Returns the date in another format
	 * 
	 * @input Date in YYYY-MM-DD format
	 * @return Date in MM/DD/YYYY format
	 */
	public static String changeDateToMMDDYYYY(String date) {
		if (StringUtils.isNotBlank(date)) {
			String day = date.substring(8);
			String month = date.substring(5, 7);
			String year = date.substring(0, 4);
			String convertedDate = month + "/" + day + "/" + year;
			return convertedDate;
		}
		return null;
	}

	/**
	 * Returns the date in another format
	 * 
	 * @input Date in MM/DD/YYYY format
	 * @return Date in YYYY-MM-DD format
	 */
	public static String changeDateToYYYYMMDD(String date) {
		if (StringUtils.isNotBlank(date)) {
			String day = date.substring(3, 5);
			String month = date.substring(0, 2);
			String year = date.substring(6);
			String convertedDate = year + "-" + month + "-" + day;
			return convertedDate;
		}
		return null;
	}
	
	public static void setCompanyIdToRequest(DataControllerRequest request) {
		String companyId = null;
		try {
			companyId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
			if(StringUtils.isEmpty(companyId)) {
				IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				if(userAttributes != null && userAttributes.size() >0) {
					companyId = (String)userAttributes.get("legalEntityId");
					if(StringUtils.isBlank(companyId)) {
						companyId = (String)userAttributes.get("companyId");
					}				
				}
			}
			request.addRequestParam_("companyid", companyId);
			request.getHeaderMap().put("companyid", companyId);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

	}
}

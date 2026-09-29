package com.hbl.productservicesExtn.impl;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Test {
	private static final Logger logger = LogManager.getLogger(Test.class);
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static void main(String[] args) {
		String schdeuledDate ="2024-12-31";
		String transactionstartdate = schdeuledDate + " 00:00:00";
		LocalDateTime transday = parseDate(transactionstartdate);
		String filter=getFilter(transday);
		System.out.println("filter:"+filter);
		
	}
	static String getFilter(LocalDateTime transday){
		LocalDate today = transday.toLocalDate();
		LocalDate currentransactionmonthstartday = today.withDayOfMonth(1);
		LocalDate currentransactionmonthendday = today.withDayOfMonth(today.lengthOfMonth());
		currentransactionmonthendday=currentransactionmonthendday.plusDays(1);
		System.out.println("currentransactionmonthendday"+currentransactionmonthendday);
		String filter = generateFilter("GB0010001", currentransactionmonthstartday.toString(),
				currentransactionmonthendday.toString(), "Default", "122671", "190382", "TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE");
		return filter;
	}
	private static String generateFilter(String companyid, String transactionstartdate, String transactionendday,
			String roleid, String accountid, String customerid, String featureactionid) {

		String filter = "featureActionId eq '" + featureactionid
				+ "' and (status eq 'Executed' or status eq 'Sent' or status eq 'Pending') and (scheduledDate ge '" + transactionstartdate
				+ "' and scheduledDate lt '" + transactionendday + "')";

		if (companyid == null) {
			if (accountid != null && customerid != null) {
				filter = filter + " and fromAccountNumber eq '" + accountid + "'";
				filter = filter + " and createdby eq '" + customerid + "'";
			} else if (customerid != null) {
				filter = filter + " and createdby eq '" + customerid + "'";
			} else {
				return null;
			}
		} else {
			filter = filter + " and companyId eq '" + companyid + "'";
			if (roleid != null) {
				filter = filter + " and roleId eq '" + roleid + "'";
			}
			if (customerid != null) {
				filter = filter + " and createdby eq '" + customerid + "'";
			}
			if (accountid != null) {
				filter = filter + " and fromAccountNumber eq '" + accountid + "'";
			}
		}

		return filter;
	}

	private static int noofDaysDifferenceToWeekStartDay(DayOfWeek transday, DayOfWeek weekstartday) {

		int diff = transday.getValue() - weekstartday.getValue();

		if (diff == 0)
			return 0;
		else if (diff < 0) {
			return -(7 + diff);
		} else {
			return -diff;
		}
	}

	private static int noofDaysDifferenceToWeekEndDay(DayOfWeek transday, DayOfWeek weekstartday) {
		int diff = transday.getValue() - weekstartday.getValue();
		if (diff == 0)
			return 7;
		else if (diff < 0) {
			return -diff;
		} else {
			return 7 - diff;
		}
	}

	private static LocalDateTime getNdaysAfterDate(LocalDateTime date, int noofdays) {
		if (date == null)
			return null;
		LocalDateTime t1 = null;
		t1 = date.plusDays(noofdays);
		return t1;
	}

	private static LocalDateTime parseDate(String date) {
		LocalDateTime t = null;
		try {
			t = LocalDateTime.parse(date, dtf);
		} catch (Exception e) {
			logger.debug("Parsing error", e);
		}
		return t;
	}

	private static DayOfWeek getStartDayOfWeek() {
		String day = "MONDAY";//HelperMethods.getConfigProperty("TRANSACTIONLIMITENGINE_STARTDAY");
		if (day == null)
			return DayOfWeek.MONDAY;
		for (DayOfWeek dayname : DayOfWeek.values()) {
			if (day.equalsIgnoreCase(dayname.toString())) {
				return dayname;
			}
		}
		return DayOfWeek.MONDAY;
	}
}

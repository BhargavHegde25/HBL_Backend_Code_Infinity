package com.kony.alertslogservices.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public class Helpermethods {
	private Helpermethods() {
	}
	

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static Result returnResult(boolean flag, String errmsg) {

		Result res = new Result();
		if (flag) {
			res.addParam(new Param(ArchivalConstants.SUCCESS, ArchivalConstants.TRUE, ArchivalConstants.STRING));
		} else {
			res.addParam(new Param(ArchivalConstants.SUCCESS, ArchivalConstants.FALSE, ArchivalConstants.STRING));
			res.addParam(new Param(ArchivalConstants.DBPERRMSG, errmsg, ArchivalConstants.STRING));
		}
		return res;
	}

	public static String calculateArchivingDate(Integer noofmonths) {

		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, -noofmonths);
				Date date=cal.getTime();
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date1 = format1.format(date);
				return date1;


	}

}
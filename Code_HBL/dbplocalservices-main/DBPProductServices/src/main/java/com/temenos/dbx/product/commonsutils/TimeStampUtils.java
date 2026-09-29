package com.temenos.dbx.product.commonsutils;

import java.text.ParseException;

import com.temenos.dbx.product.constants.Constants;
import com.kony.dbputilities.util.HelperMethods;

public final class TimeStampUtils {
	
	public static String convertToDBTimeStamp(String dateString) {
		try {
			return HelperMethods.convertDateFormat(dateString, Constants.TIMESTAMP_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}

}

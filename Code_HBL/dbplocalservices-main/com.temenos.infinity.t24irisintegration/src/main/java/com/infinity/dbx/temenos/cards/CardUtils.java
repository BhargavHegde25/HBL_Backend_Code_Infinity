package com.infinity.dbx.temenos.cards;

public class CardUtils {
	public static String mask(String number) {
		// TODO Auto-generated method stub
		if (number == null || number.equals(""))
			return "";
		int end = number.length() - 4;
		if (end > number.length())
			end = number.length();

		int maskLength = end - 0;

		if (maskLength == 0)
			return number;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append('X');
		}

		return number.substring(0, 0) + sbMaskString.toString() + number.substring(0 + maskLength);

	}
}

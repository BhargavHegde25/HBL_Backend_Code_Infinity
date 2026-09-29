package com.bct.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.javaservices.QRValidationService;

public class QRStringMatchUtil {
	private static final Logger logger = LogManager.getLogger(QRValidationService.class);

	public static int computeWeightedLevenshteinDistance(String s1, String s2) {
		int charChangeCost = 2; // Cost for replacing a character
		int spaceCost = 1; // Cost for insertion or deletion

		int[][] dp = new int[s1.length() + 1][s2.length() + 1];

		// Initialize first row and column
		for (int i = 0; i <= s1.length(); i++) {
			dp[i][0] = i * spaceCost;
		}

		for (int j = 0; j <= s2.length(); j++) {
			dp[0][j] = j * spaceCost;
		}

		// Fill DP table
		for (int i = 1; i <= s1.length(); i++) {
			for (int j = 1; j <= s2.length(); j++) {

				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1]; // No cost
				} else {
					int insert = dp[i][j - 1] + spaceCost;
					int delete = dp[i - 1][j] + spaceCost;
					int replace = dp[i - 1][j - 1] + charChangeCost;

					dp[i][j] = Math.min(Math.min(insert, delete), replace);
				}
			}
		}

		return dp[s1.length()][s2.length()];
	}

	public static int getAccountMatchPercentage(String string1, String string2) {

		String s1 = string1.toLowerCase();
		String s2 = string2.toLowerCase();

		int maxLength = s1.length() + s2.length();
		logger.debug("maxLength:::" + maxLength);

		int distance = computeWeightedLevenshteinDistance(s1, s2);
		logger.debug("distance:::" + distance);

		double matchPercentage = 1 - ((double) distance / maxLength);
		logger.debug("matchPercentage:::" + matchPercentage);

		return (int) Math.ceil(matchPercentage * 100);
	}

}

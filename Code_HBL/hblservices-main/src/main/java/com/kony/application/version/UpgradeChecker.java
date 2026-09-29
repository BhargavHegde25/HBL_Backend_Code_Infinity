package com.kony.application.version;

public class UpgradeChecker {
	// Sample method to check upgrade status
	public static UpgradeType checkUpgrade(String platform, String currentVersion, String minRequiredVersion,
			String latestVersion) {

		if (compareVersions(currentVersion, minRequiredVersion) < 0) {
			return UpgradeType.MANDATORY;
		}

		if (compareVersions(currentVersion, latestVersion) < 0) {
			return UpgradeType.OPTIONAL;
		}

		return UpgradeType.NONE;
	}

	// Version comparator: returns -1 if v1 < v2, 0 if equal, 1 if v1 > v2
	public static int compareVersions(String v1, String v2) {
		String[] v1Parts = v1.split("\\.");
		String[] v2Parts = v2.split("\\.");

		int maxLen = Math.max(v1Parts.length, v2Parts.length);

		for (int i = 0; i < maxLen; i++) {
			int v1Segment = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
			int v2Segment = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

			if (v1Segment < v2Segment)
				return -1;
			if (v1Segment > v2Segment)
				return 1;
		}

		return 0;
	}
}
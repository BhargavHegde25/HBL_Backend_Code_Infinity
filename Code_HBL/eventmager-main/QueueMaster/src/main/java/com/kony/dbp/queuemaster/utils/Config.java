
package com.kony.dbp.queuemaster.utils;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public abstract class Config {

	private Config() {
		
	}

 
  public static boolean getBooleanValue(String key) {
    return getBooleanValue(key, false, true);
  }

  public static boolean getBooleanValue(String key, boolean defaultValue) {
    return getBooleanValue(key, defaultValue, false);
  }

  private static boolean getBooleanValue(String key, boolean defaultValue, boolean required) {
    String value = getValue(key, null, required);
    boolean booleanValue = defaultValue;
    if (value != null) {
      booleanValue = Boolean.valueOf(value);
    }
    return booleanValue;
  }

  public static int getIntValue(String key) {
    return getIntValue(key, 0, true);
  }

  public static int getIntValue(String key, int defaultValue) {
    return getIntValue(key, defaultValue, false);
  }

  private static int getIntValue(String key, int defaultValue, boolean required) {
    String value = getValue(key, null, required);
    int intValue = defaultValue;
    if (value != null) {
      try {
        intValue = Integer.parseInt(value);
      }
      catch (Exception ex) { }
    }
    return intValue;
  }

  public static String getValue(String key){
    return getValue(key, null, true);
  }

  public static String getValue(String key, String defaultValue) {
    return getValue(key, defaultValue, false);
  }

  private static String getValue(String key, String defaultValue, boolean required) {
    String value = null;
    try {
    value = EnvironmentConfigurationsHandler.getServerAppProperty(key);
    }catch (Exception e) {
    	return defaultValue;
	}
    if (value == null) {
      if (required) {
        throw new RuntimeException("Required configurable parameter " + key + " has not been set!");
      }
      return defaultValue;
    } else {
      return value;
    }
  }
}

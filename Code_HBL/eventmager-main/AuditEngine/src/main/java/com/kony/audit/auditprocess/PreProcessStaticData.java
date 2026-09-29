package com.kony.audit.auditprocess;

import com.kony.audit.auditutils.AuditConstants;
import com.kony.audit.dbconnectionutils.DataBasePreprocessingEvents;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class PreProcessStaticData {
	private PreProcessStaticData() {
		
	}
  private static Map<String, String> mfinfoappleveldata = null;
  
  private static List<String> currencydata = null;
  
  private static Map<String, String> transactiontypedata = null;
  
  public static Map<String, String> getMfInfoAppData() {
    return mfinfoappleveldata;
  }
  
  public static Map<String, String> getTransactionTypeData() {
    return transactiontypedata;
  }
  
  public static List<String> getCurrencyData() {
    return currencydata;
  }
  
  public static synchronized void preProcessStaticData() {
    try {
      if (mfinfoappleveldata == null) {
        mfinfoappleveldata = new HashMap<>();
        fetchBaseData(AuditConstants.APPPROCESS, mfinfoappleveldata);
      } 
      if (transactiontypedata == null) {
        transactiontypedata = new HashMap<>();
        fetchBaseData(AuditConstants.TRANSPROCESS, transactiontypedata);
      } 
      if (currencydata == null) {
        currencydata = new ArrayList<>();
        fetchBaseCurrencyData(currencydata);
      } 
    } catch (Exception e) {
      alert.prepareError("Error Occured", e).log();
    } 
  }
  
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
  
  public static void fetchBaseData(String type, Map<String, String> map) {
    if (type.equals(AuditConstants.APPPROCESS))
      try {
        DataBasePreprocessingEvents.mfInfoFetchAppLevelData(map);
      } catch (Exception e) {
        alert.prepareError("Error occured ", e).log();
      }  
    if (type.equals(AuditConstants.TRANSPROCESS))
      try {
        DataBasePreprocessingEvents.transactiontypefetch(map);
      } catch (Exception e) {
        alert.prepareError("Error occured", e).log();
      }  
  }
  
  public static void fetchBaseCurrencyData(List<String> currencycodes) {
    try {
      DataBasePreprocessingEvents.fetchCurrencyData(currencycodes);
    } catch (Exception e) {
      alert.prepareError("Error occured", e).log();
    } 
  }
}

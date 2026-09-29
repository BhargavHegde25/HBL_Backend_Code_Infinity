package com.kony.memorymgmt;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.SavingsPotHelper;
import com.kony.utilities.Constants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SavingsPotManager {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;
    private static final String SAVINGS_POTS = "SAVINGS_POTS";
    
    public SavingsPotManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SavingsPotManager(FabricRequestManager fabricRequestManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }
    
    public SavingsPotManager(String customerId) {
        this.customerId = customerId;
    }

    public SessionMap getSavingsPotFromSession(String customerId) {
        SessionMap savingsPotMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
        		SavingsPotManager.SAVINGS_POTS + this.customerId);
        if (null == savingsPotMap || savingsPotMap.isEmpty()) {
            diagnostic.prepareDebug("savingsPotMap is null. Reload function will be triggered.").log();
            SavingsPotHelper.reloadSavingsPotsOfUserIntoSession(fabricRequestManager);
            savingsPotMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
            		SavingsPotManager.SAVINGS_POTS + this.customerId);
        }

        return savingsPotMap;
    }
    
    public void saveSavingsPotIntoSession(SessionMap savingsPotMap) {
        if (null != savingsPotMap) {
            MemoryManager.save(this.fabricRequestManager, SavingsPotManager.SAVINGS_POTS + this.customerId,
            		savingsPotMap);
        }
    }

	public boolean validateSavingsPot(String customerId, String savingsPotId) {
		   	if(StringUtils.isBlank(savingsPotId)) {
		    		return false;
		    	}
		        SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);

		        if (null == savingsPotMap || savingsPotMap.isEmpty()) {
		            diagnostic.prepareDebug("validateSavingsPot - savingsPotMap Null / Empty").log();
		            return false;
		        }

		        diagnostic.prepareDebug("validateSavingsPot: " + savingsPotMap.toString()).log();
		        return savingsPotMap.hasKey(savingsPotId);
		        
    }
    
   public SessionMap getSavingsPotMapFromSession(String customerId) {
    SessionMap savingsPotMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
    		SavingsPotManager.SAVINGS_POTS + this.customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("savingsPotMap is null. Reload function will be triggered.").log();
        SavingsPotHelper.reloadSavingsPotsOfUserIntoSession(fabricRequestManager);
        savingsPotMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
        		SavingsPotManager.SAVINGS_POTS + this.customerId);
    }
     return savingsPotMap;
    }
  
   public boolean isSavingsPotClosed(String customerId, String savingsPotId) {
	   	if(StringUtils.isBlank(savingsPotId)) {
	    		return false;
	    	}
	        SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);

	        if (null == savingsPotMap || savingsPotMap.isEmpty()) {
	            diagnostic.prepareDebug("isSavingsPotClosed - savingsPotMap Null / Empty").log();
	            return false;
	        }

	        diagnostic.prepareDebug("isSavingsPotClosed: " + savingsPotMap.toString()).log();
	        String potStatus =  savingsPotMap.getAttributeValueForKey(savingsPotId, Constants.STATUS);
	        if(potStatus.equalsIgnoreCase(Constants.CLOSED))
	        	return true;
	        else
	        	return false;
}

public String getFundingAccount(String customerId, String savingsPotId) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getFundingAccount - savingsPotMap Null / Empty").log();
        return "";
    }
    diagnostic.prepareDebug("getFundingAccount: " + savingsPotMap.toString()).log();
    String fundingAccountId =  savingsPotMap.getAttributeValueForKey(savingsPotId, Constants.FUNDINGACCOUNTID);
    return fundingAccountId;
}

public String getAvailableBalanceInPot(String customerId, String savingsPotId) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getAvailableBalanceInPot - savingsPotMap Null / Empty").log();
        return "";
    }
    diagnostic.prepareDebug("getAvailableBalanceInPot: " + savingsPotMap.toString()).log();
    String availableBalance =  savingsPotMap.getAttributeValueForKey(savingsPotId, Constants.AVAILABLEBALANCE);
    return availableBalance;
}
public void closeSavingsPotInSession(String customerId, JsonElement savingsPotId) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getFundingAccount - savingsPotMap Null / Empty").log();
    }
    diagnostic.prepareDebug("closeSavingsPotInSession: " + savingsPotMap.toString()).log();
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.STATUS,Constants.CLOSED);
    saveSavingsPotIntoSession(savingsPotMap);
}

public void addSavingsPotInSession(String customerId, JsonElement savingsPotId , JsonElement fundingAccountId, JsonElement fundingAccountHoldingsId, String potType) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getFundingAccount - savingsPotMap Null / Empty").log();
    }
    diagnostic.prepareDebug("addSavingsPotInSession: " + savingsPotMap.toString()).log();
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.FUNDINGACCOUNTID,fundingAccountId.getAsString());
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.AVAILABLEBALANCE,"0");
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.STATUS,Constants.ACTIVE);
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.FUNDINGACCOUNTHOLDINGSID,fundingAccountHoldingsId.getAsString());
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(),Constants.POTTYPE,potType);
    saveSavingsPotIntoSession(savingsPotMap);
}

public void setAvailableBalanceInPot(String customerId, JsonElement savingsPotId, String updatedAvailableBalance) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getAvailableBalanceInPot - savingsPotMap Null / Empty").log();
    }
    diagnostic.prepareDebug("setAvailableBalanceInPot: " + savingsPotMap.toString()).log();
    savingsPotMap.addAttributeForKey(savingsPotId.getAsString(), Constants.AVAILABLEBALANCE,updatedAvailableBalance);
    saveSavingsPotIntoSession(savingsPotMap);
}
public String getFundingAccountHoldingsId(String customerId, String savingsPotId) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getFundingAccountHoldingsId - savingsPotMap Null / Empty").log();
        return "";
    }
    diagnostic.prepareDebug("getFundingAccountHoldingsId: " + savingsPotMap.toString()).log();
    String fundingAccountHoldingsId =  savingsPotMap.getAttributeValueForKey(savingsPotId, Constants.FUNDINGACCOUNTHOLDINGSID);
    return fundingAccountHoldingsId;
}
public String getPotType(String customerId, String savingsPotId) {
    SessionMap savingsPotMap = getSavingsPotMapFromSession(customerId);
    if (null == savingsPotMap || savingsPotMap.isEmpty()) {
        diagnostic.prepareDebug("getPotType - savingsPotMap Null / Empty").log();
        return "";
    }
    diagnostic.prepareDebug("getPotType: " + savingsPotMap.toString()).log();
    String potType =  savingsPotMap.getAttributeValueForKey(savingsPotId, Constants.POTTYPE);
    return potType;
}
}

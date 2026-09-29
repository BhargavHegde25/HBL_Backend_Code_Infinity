package com.temenos.infinity.api.cards.utils;

import java.util.Iterator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
//import com.kony.memorymgmt.AccountsManager;
//import com.kony.memorymgmt.SessionMap;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;

public class AccountHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private AccountHelper() {
    }

    public static void reloadInternalBankAccountsIntoSession(FabricRequestManager fabricRequestManager) {
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.ACCOUNTS_OS_GETACCOUNTSPOSTLOGIN);
            diagnostic.prepareDebug("response in reloadInternalBankAccountsIntoSession: " + response.toString()).log();
            saveInternalBankAccountsIntoSession(response, fabricRequestManager);

        } catch (Exception e) {
            alert.prepareError("Error while reloading internal accounts:", e).log();
        }
    }

    public static void saveInternalBankAccountsIntoSession(JsonObject response,
            FabricRequestManager fabricRequestManager) {
        String AccountsObject = "Accounts";
        diagnostic.prepareDebug("Account Helper: Accounts Response: " + response.toString()).log();
        if (null != response && !response.isJsonNull()) {
            if (response.has(AccountsObject)) {
                JsonArray accounts = response.getAsJsonArray(AccountsObject);
                SessionMap accountsMap = getAccountsMap(accounts);
                diagnostic.prepareDebug("SessionMap of Accounts: " + accountsMap.toString()).log();
                AccountsManager acctManager = new AccountsManager(fabricRequestManager);
                acctManager.saveInternalBankAccountsIntoSession(accountsMap);
            }
        }
    }

    public static void reloadExternalBankAccountsIntoSession(FabricRequestManager fabricRequestManager) {
    	try {
    		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.EXTERNALACCOUNTS_OS_GET);
    		if (null != response && !response.isJsonNull()) {
                JsonArray extAccounts = response.getAsJsonArray("ExternalAccounts");
                SessionMap extAccountsMap = getExtAccountsMap(extAccounts);
                AccountsManager accntManager = new AccountsManager(fabricRequestManager, null);
                accntManager.saveExternalBankAccountsIntoSession(extAccountsMap);
    		}
    	} catch (Exception e) {
    		alert.prepareError("Error while reloading external accounts:", e).log();
    	}
    }
    
    public static SessionMap fetchExternalBankAccounts(FabricRequestManager fabricRequestManager) {
    	SessionMap extAccountsMap = new SessionMap();
    	try {
			 JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
	                    URLConstants.EXTERNALACCOUNTS_OS_GET);
			 if (null != response && !response.isJsonNull()) {
	                JsonArray extAccounts = response.getAsJsonArray("ExternalAccounts");
	                extAccountsMap = getExtAccountsMap(extAccounts);
			 }
		} catch (Exception e) {
            alert.prepareError("Error while reloading external accounts:", e).log();
        }
    	
    	return extAccountsMap;
		
	}
    
    public static SessionMap reloadSameBankAccountsIntoSession(FabricRequestManager fabricRequestManager) {
    	SessionMap extAccountsMap = new SessionMap();
    	try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.SAME_BANK_ACCOUNTS_OS_GET);
            if (null != response && !response.isJsonNull()) {
                JsonArray extAccounts = response.getAsJsonArray("ExternalAccounts");
                extAccountsMap = getExtAccountsMap(extAccounts);
                AccountsManager accntManager = new AccountsManager(fabricRequestManager, null);
                accntManager.saveSameBankAccountsIntoSession(extAccountsMap);
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading external accounts:", e).log();
        }
    	return extAccountsMap;
    }
    
    public static SessionMap reloadDoemsticBankAccountsIntoSession(FabricRequestManager fabricRequestManager) {
    	SessionMap extAccountsMap = new SessionMap();
    	try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.DOMESTIC_BANK_ACCOUNTS_OS_GET);
            if (null != response && !response.isJsonNull()) {
                JsonArray extAccounts = response.getAsJsonArray("ExternalAccounts");
                extAccountsMap = getExtAccountsMap(extAccounts);
                AccountsManager accntManager = new AccountsManager(fabricRequestManager, null);
                accntManager.saveDomesticBankAccountsIntoSession(extAccountsMap);
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading external accounts:", e).log();
        }
		return extAccountsMap;
	}
    
    public static SessionMap reloadInternationalBankAccountsIntoSession(FabricRequestManager fabricRequestManager) {
    	SessionMap extAccountsMap = new SessionMap();
    	try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(fabricRequestManager, null, null,
                    URLConstants.INTERNATIONAL_BANK_ACCOUNTS_OS_GET);
            if (null != response && !response.isJsonNull()) {
                JsonArray extAccounts = response.getAsJsonArray("ExternalAccounts");
                extAccountsMap = getExtAccountsMap(extAccounts);
                AccountsManager accntManager = new AccountsManager(fabricRequestManager, null);
                accntManager.saveInternationalBankAccountsIntoSession(extAccountsMap);
            }
        } catch (Exception e) {
            alert.prepareError("Error while reloading external accounts:", e).log();
        }
    	return extAccountsMap;
	}

    private static SessionMap getExtAccountsMap(JsonArray extAccounts) {
        SessionMap extAccountsMap = new SessionMap();
        if (null != extAccounts && !extAccounts.isJsonNull() && extAccounts.size() > 0) {
            Iterator<JsonElement> itr = extAccounts.iterator();
            while (itr.hasNext()) {
                JsonObject extAccount = itr.next().getAsJsonObject();
                if (HelperMethods.isJsonNotNull(extAccount.get("accountNumber"))) {
                    if (HelperMethods.isJsonNotNull(extAccount.get("isSameBankAccount"))) {
                        extAccountsMap.addAttributeForKey(extAccount.get("accountNumber").getAsString(),
                                "isSameBankAccount", extAccount.get("isSameBankAccount").getAsString());
                    }
                    if (HelperMethods.isJsonNotNull(extAccount.get("isInternationalAccount"))) {
                        extAccountsMap.addAttributeForKey(extAccount.get("accountNumber").getAsString(),
                                "isInternationalAccount", extAccount.get("isInternationalAccount").getAsString());
                    }
                }
            }
        }
        return extAccountsMap;
    }

    private static SessionMap getAccountsMap(JsonArray accounts) {
        SessionMap accountsMap = new SessionMap();
        JsonElement isBusinessAccount;
        JsonElement availableBalance;
        JsonElement accountType;
        JsonElement IBAN;
        JsonElement accountIBAN;
        JsonElement accountName;
        if (null != accounts && !accounts.isJsonNull() && accounts.size() > 0) {
            Iterator<JsonElement> itr = accounts.iterator();
            while (itr.hasNext()) {
                JsonElement ele = itr.next();
                isBusinessAccount = ele.getAsJsonObject().get("isBusinessAccount");
                availableBalance = ele.getAsJsonObject().get("availableBalance");
                accountType=ele.getAsJsonObject().get("accountType");
                IBAN=ele.getAsJsonObject().get("IBAN");
                accountIBAN=ele.getAsJsonObject().get("accountIBAN");
                accountName=ele.getAsJsonObject().get("accountName");
                if( isBusinessAccount == null && availableBalance == null)
                	accountsMap.addKey(ele.getAsJsonObject().get("accountID").getAsString());
                else
                {
                	if(isBusinessAccount != null)
                		accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"isBusinessAccount",isBusinessAccount.getAsString());
                    if(availableBalance != null)
                    	accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"availableBalance",availableBalance.getAsString());
                    if(accountType != null)
                	accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"accountType",accountType.getAsString()); 
                    if(IBAN != null)
                    	accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"IBAN",IBAN.getAsString()); 
                    if(accountIBAN != null)
                    	accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"accountIBAN",accountIBAN.getAsString()); 
                    if(accountName != null)
                        accountsMap.addAttributeForKey(ele.getAsJsonObject().get("accountID").getAsString(),"accountName",accountName.getAsString()); 
           
                }       
            }
        }
        return accountsMap;
    }

}

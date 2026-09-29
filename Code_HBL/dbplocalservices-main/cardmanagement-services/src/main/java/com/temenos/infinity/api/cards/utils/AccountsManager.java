package com.temenos.infinity.api.cards.utils;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.HelperMethods;
//import com.kony.model.AccountHelper;
import com.kony.utilities.Constants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class AccountsManager {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;
    private static final String EXTERNAL_BANK_ACCOUNTS = "EXTERNAL_BANK_ACCOUNTS";
    private static final String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";
    private static final String SAME_BANK_ACCOUNTS = "SAME_BANK_ACCOUNTS";
    private static final String OTHER_BANK_ACCOUNTS = "OTHER_BANK_ACCOUNTS";
    private static final String DOMESTIC_BANK_ACCOUNTS = "DOMESTIC_BANK_ACCOUNTS";
    private static final String INTERNATIONAL_BANK_ACCOUNTS = "INTERNATIONAL_BANK_ACCOUNTS";
    private static final String ACCOUNT_STATEMENTS = "ACCOUNT_STATEMENTS";

    public AccountsManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public AccountsManager(FabricRequestManager fabricRequestManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SessionMap getInternalBankAccountsFromSession(String customerId) {
        SessionMap internalAccntsMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                AccountsManager.INTERNAL_BANK_ACCOUNTS + this.customerId);
        if (null == internalAccntsMap || internalAccntsMap.isEmpty()) {
            diagnostic.prepareDebug("internalAccntsMap is null. Reload function will be triggered.").log();
            AccountHelper.reloadInternalBankAccountsIntoSession(fabricRequestManager);
            internalAccntsMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                    AccountsManager.INTERNAL_BANK_ACCOUNTS + this.customerId);
        }

        return internalAccntsMap;
    }

    public SessionMap getExternalBankAccountsFromSession(String customerId) {
        SessionMap extsAccntStore = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                AccountsManager.EXTERNAL_BANK_ACCOUNTS + this.customerId);
        if (null == extsAccntStore || extsAccntStore.isEmpty()) {
            AccountHelper.reloadExternalBankAccountsIntoSession(fabricRequestManager);
            extsAccntStore = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                    AccountsManager.EXTERNAL_BANK_ACCOUNTS + this.customerId);
        }
        return extsAccntStore;
    }
    
    public SessionMap fetchExternalBankAccounts() {
    	SessionMap extsAccntStore = new SessionMap();
    	extsAccntStore = AccountHelper.fetchExternalBankAccounts(fabricRequestManager);
    	
        if (null == extsAccntStore || extsAccntStore.isEmpty()) {
        	AccountsManager accntManager = new AccountsManager(fabricRequestManager, null);
    		accntManager.saveExternalBankAccountsIntoSession(extsAccntStore);
        }
        return extsAccntStore;
    }

    public SessionMap getSameBankAccountsFromSession() {
        SessionMap accntStore =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                        AccountsManager.SAME_BANK_ACCOUNTS + this.customerId);
        if (null == accntStore || accntStore.isEmpty()) {
        	accntStore = AccountHelper.reloadSameBankAccountsIntoSession(fabricRequestManager);
        }
        return accntStore;
    }
    
    //Not used anywhere. Refer getDoemsticBankAccountsFromSession
    public SessionMap getOtherBankAccountsFromSession(String customerId) {
        SessionMap accntStore =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                        AccountsManager.OTHER_BANK_ACCOUNTS + this.customerId);
        if (null == accntStore || accntStore.isEmpty()) {
            // AccountHelper.reloadExternalBankAccountsIntoSession(fabricRequestManager);
            accntStore =
                    (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                            AccountsManager.OTHER_BANK_ACCOUNTS + this.customerId);
        }
        return accntStore;
    }

    public SessionMap getDoemsticBankAccountsFromSession() {
        SessionMap accntStore =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                        AccountsManager.DOMESTIC_BANK_ACCOUNTS + this.customerId);
        if (null == accntStore || accntStore.isEmpty()) {
        	accntStore = AccountHelper.reloadDoemsticBankAccountsIntoSession(fabricRequestManager);
            
        }
        return accntStore;
    }

    public SessionMap getInternationalBankAccountsFromSession() {
        SessionMap accntStore = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                AccountsManager.INTERNATIONAL_BANK_ACCOUNTS + this.customerId);
        if (null == accntStore || accntStore.isEmpty()) {
        	 accntStore = AccountHelper.reloadInternationalBankAccountsIntoSession(fabricRequestManager);
            
        }
        return accntStore;
    }

    public void saveInternalBankAccountsIntoSession(SessionMap internalAccountsMap) {
        if (null != internalAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.INTERNAL_BANK_ACCOUNTS + this.customerId,
                    internalAccountsMap);
        }
    }

    public void saveExternalBankAccountsIntoSession(SessionMap externalAccountsMap) {
        if (null != externalAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.EXTERNAL_BANK_ACCOUNTS + this.customerId,
                    externalAccountsMap);
        }
    }

    public void saveInternationalBankAccountsIntoSession(SessionMap internationalAccountsMap) {
        if (null != internationalAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.INTERNATIONAL_BANK_ACCOUNTS + this.customerId,
                    internationalAccountsMap);
        }
    }

    public void saveDomesticBankAccountsIntoSession(SessionMap domesticAccountsMap) {
        if (null != domesticAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.DOMESTIC_BANK_ACCOUNTS + this.customerId,
                    domesticAccountsMap);
        }
    }

    public void saveOtherBankAccountsIntoSession(SessionMap otherAccountsMap) {
        if (null != otherAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.OTHER_BANK_ACCOUNTS + this.customerId,
                    otherAccountsMap);
        }
    }

    public void saveSameBankAccountsIntoSession(SessionMap sameAccountsMap) {
        if (null != sameAccountsMap) {
            MemoryManager.save(this.fabricRequestManager, AccountsManager.SAME_BANK_ACCOUNTS + this.customerId,
                    sameAccountsMap);
        }
    }

    public boolean validateInternalAccount(String customerId, String accountNumber) {
    	if(StringUtils.isBlank(accountNumber)) {
    		return false;
    	}
        SessionMap internalAccountsMap = getInternalBankAccountsFromSession(customerId);

        if (null == internalAccountsMap || internalAccountsMap.isEmpty()) {
            diagnostic.prepareDebug("validateInternalAccount - internalAccountsMap Null / Empty").log();
            return false;
        }
        diagnostic.prepareDebug("validateInternalAccount: " + internalAccountsMap.toString()).log();
        return internalAccountsMap.hasKey(accountNumber);
    }

    public boolean validateInternalAccount(String accountNumber) {
        // Logged-in User ID will be retrieved from Identity scope by underlying method.
        // As underlying method is demanding user_id, passing empty string here.
        String loggedInUserID = "";
        return validateInternalAccount(loggedInUserID, accountNumber);
    }

    public boolean validateExternalBankToAccount(String customerId, String accontNumber, boolean isSameBank,
            boolean isInternationalBank) {
    	boolean status = false;
    	
    	SessionMap accountsMap = null;
    	
    	boolean isEuropianGeography = com.kony.dbputilities.util.HelperMethods.isEuropieanGeography();
    	// this is for EU region
		if (isEuropianGeography) {
			accountsMap = getExternalBankAccountsFromSession(customerId);
			status = (accountsMap != null && accountsMap.hasKey(accontNumber)
					&& Boolean.toString(isSameBank)
							.equalsIgnoreCase(accountsMap.getAttributeValueForKey(accontNumber, "isSameBankAccount"))
					&& Boolean.toString(isInternationalBank).equalsIgnoreCase(
							accountsMap.getAttributeValueForKey(accontNumber, "isInternationalAccount")));
			return status;
    	}
    		
    	
    	if(isSameBank == true && isInternationalBank == false) {
    		accountsMap = getSameBankAccountsFromSession();
    	} else if(isSameBank == false && isInternationalBank == false) {
    		accountsMap = getDoemsticBankAccountsFromSession();
    	} else if(isSameBank == false && isInternationalBank == true) {
    		accountsMap = getInternationalBankAccountsFromSession();
    	}
    	
    	status = (accountsMap != null) && accountsMap.hasKey(accontNumber);
    	
    	if(!status) {
    		accountsMap = fetchExternalBankAccounts();
            status = accountsMap != null && accountsMap.hasKey(accontNumber)
                    && Boolean.toString(isSameBank)
                            .equalsIgnoreCase(accountsMap.getAttributeValueForKey(accontNumber, "isSameBankAccount"))
                    && Boolean.toString(isInternationalBank)
                            .equalsIgnoreCase(accountsMap.getAttributeValueForKey(accontNumber, "isInternationalAccount"));
    	}
    	
        return status;
    }
    
    public String getBalanceOfInternalAccount(String customerId, String accountNumber) {
        SessionMap internalAccountsMap = getInternalBankAccountsFromSession(customerId);
        if (null == internalAccountsMap || internalAccountsMap.isEmpty()) {
            diagnostic.prepareDebug("getBalanceOfInternalAccount - internalAccountsMap Null / Empty").log();
            return "";
        }

        diagnostic.prepareDebug("getBalanceOfInternalAccount: " + internalAccountsMap.toString()).log();
        if(internalAccountsMap.hasKey(accountNumber)) {
        	String balance =  internalAccountsMap.getAttributeValueForKey(accountNumber, Constants.ACCOUNTBALANCE);
        	return balance;
        }
		return "";
    }
    

        @SuppressWarnings("unused")
		public boolean isBusinessInternalAccount(String customerId, String accountNumber) {
            if(StringUtils.isBlank(accountNumber)) {
                return false;
            }
            SessionMap internalAccountsMap = getInternalBankAccountsFromSession(customerId);
            diagnostic.prepareDebug("validateInternalAccount: " + internalAccountsMap.toString()).log();
            String isBusinessAccouString =  internalAccountsMap.getAttributeValueForKey(accountNumber, Constants.ISBUSINESSACCOUNT);
            if(isBusinessAccouString != null || isBusinessAccouString != ""){
                return Boolean.parseBoolean(isBusinessAccouString);
            }else
            return false;
        }
        
        public void saveStatementsIntoSession(SessionMap statementsMap) {
        	if (null != statementsMap) {
                MemoryManager.save(this.fabricRequestManager, AccountsManager.ACCOUNT_STATEMENTS + this.customerId,
                		statementsMap);
            }
        }
        
        public SessionMap getStatementsFromSession(String customerId)
        {
        	SessionMap statementsMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                    AccountsManager.ACCOUNT_STATEMENTS + this.customerId);
            return statementsMap;
        }
        
        public boolean isValidDocumentStatements(String documentId) {
        	SessionMap statementsMap = getStatementsFromSession(this.customerId);
            if (null == statementsMap) {
                return false;
            }
            return statementsMap.hasKey(documentId);
        }
}
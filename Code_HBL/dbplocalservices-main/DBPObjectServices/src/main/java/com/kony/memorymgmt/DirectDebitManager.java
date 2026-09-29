package com.kony.memorymgmt;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.PayeeHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class DirectDebitManager {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;
    private static final String DIRECT_DEBIT_ID = "DIRECT_DEBIT_ID";
    
    public DirectDebitManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public DirectDebitManager(FabricRequestManager fabricRequestManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }
	
	public void saveDirectDebitIntoSession(SessionMap externalAccountsMap) {
	        if (null != externalAccountsMap) {
	            MemoryManager.save(this.fabricRequestManager, DirectDebitManager.DIRECT_DEBIT_ID + this.customerId,
	                    externalAccountsMap);
	        }
	    }
	
	public SessionMap getDirectDebitFromSession(String customerId) {
        SessionMap consentsMap =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, DirectDebitManager.DIRECT_DEBIT_ID + this.customerId);
        return consentsMap;
    }

}

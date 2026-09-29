package com.temenos.infinity.api.chequemanagement.utils;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.temenos.infinity.api.chequemanagement.dto.SessionMap;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class AccountUtilities {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;
    private static final String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";

    public boolean validateInternalAccount(String customerId, String accountNumber) {
        if (StringUtils.isBlank(accountNumber)) {
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

    public SessionMap getInternalBankAccountsFromSession(String customerId) { 
        SessionMap internalAccntsMap = (SessionMap) MemoryManagerUtils
                .retrieve(AccountUtilities.INTERNAL_BANK_ACCOUNTS + customerId);
        return internalAccntsMap;
    }

}

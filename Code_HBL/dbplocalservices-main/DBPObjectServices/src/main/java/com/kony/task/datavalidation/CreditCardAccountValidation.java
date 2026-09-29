package com.kony.task.datavalidation;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.SessionMap;
import com.kony.memorymgmt.TransactionManager;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class CreditCardAccountValidation implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    final String INTERNAL_BANK_ACCOUNTS = "INTERNAL_BANK_ACCOUNTS";

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {

        String customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
        JsonObject requestPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        TransactionManager transactionManager = new TransactionManager(fabricRequestManager, fabricResponseManager);
        AccountsManager accountsManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
        String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "fromAccountNumber");
        String toAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, "toAccountNumber");

        SessionMap creditcards = transactionManager.getCreditCardsFromSession(customerId);
        SessionMap internalAccntsMap = accountsManager.getInternalBankAccountsFromSession(customerId);
        diagnostic.prepareDebug("Credit Cards : " + creditcards.toString(), "Internal Accounts : " + internalAccntsMap.toString()).log();
        
        // Validating From and to Account Number
        if ((internalAccntsMap == null || !internalAccntsMap.hasKey(fromAccountNumber))
                || (creditcards == null || !creditcards.hasKey(toAccountNumber))) {
            JsonObject resPayload = null;
            resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
            return false;
        }
        return true;
    }

}
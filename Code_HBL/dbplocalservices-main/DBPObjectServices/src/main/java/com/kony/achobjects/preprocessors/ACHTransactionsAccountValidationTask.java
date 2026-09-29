package com.kony.achobjects.preprocessors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.AccountsManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class ACHTransactionsAccountValidationTask implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        Log4j2Configurator.getInstance();
        JsonObject reqPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        String debitAccount, template_id;
        String template_id_key = "Template_id";
        String debit_account_key = "DebitAccount";

        diagnostic.prepareDebug("Entered into ACHTransactionsAccountValidationTask").log();

        if (HelperMethods.isJsonNotNull(reqPayload)) {
            diagnostic.prepareDebug("request payload is not null").log();

            debitAccount = reqPayload.has(debit_account_key) ? reqPayload.get(debit_account_key).getAsString() : null;

            template_id = reqPayload.has(template_id_key) ? reqPayload.get(template_id_key).getAsString() : null;

            diagnostic.prepareDebug("Debit Account: " + debitAccount).log();

            if (!(debitAccount == null || debitAccount.isEmpty())) {
                AccountsManager accManager = new AccountsManager(fabricRequestManager);
                boolean accountValidationStatus = accManager.validateInternalAccount(debitAccount);
                diagnostic.prepareDebug("Account Validation status: " + accountValidationStatus).log();
                if (!accountValidationStatus) {
                    JsonObject resultJson = new JsonObject();
                    ErrorCodeEnum.ERR_13501.setErrorCode(resultJson);
                    fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
                }
                return accountValidationStatus;
            } else if (template_id == null || template_id.isEmpty()) {
                diagnostic.prepareDebug("Debit Account and Template Id is Null. "
                        + "Passing the Task, considering this is MFA Request.").log();
                return true;
            } else {
                diagnostic.prepareDebug("This is template execution. "
                        + "Company Validation task will take care of Account Validation.").log();
                return true;
            }
        } else {
            return false;
        }
    }

}

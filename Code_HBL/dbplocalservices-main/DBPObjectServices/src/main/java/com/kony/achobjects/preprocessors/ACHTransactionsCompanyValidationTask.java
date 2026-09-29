package com.kony.achobjects.preprocessors;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.memorymgmt.AccountsManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.dataobject.Result;

public class ACHTransactionsCompanyValidationTask implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
            throws Exception {
        String template_id_key = "Template_id";
        String organization_id_key = "Organization_Id";
        Log4j2Configurator.getInstance();
        diagnostic.prepareDebug("Entered into ACHTransactionsCompanyValidationTask").log();

        String company_id_from_identity =
                HelperMethods.getCustomerFromIdentityService(fabricRequestManager).get(organization_id_key);
        diagnostic.prepareDebug("ACHTransactionsCompanyValidationTask Company ID from identity" + company_id_from_identity).log();

        if (company_id_from_identity == null || company_id_from_identity.isEmpty()) {
            setCompanyValidationFailureError(fabricResponseManager);
            return false;
        }

        JsonObject requestPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();

        String template_id =
                (requestPayload.has(template_id_key)) ? requestPayload.get(template_id_key).getAsString() : null;

        if (template_id == null || template_id.isEmpty()) {
            diagnostic.prepareDebug("Not a Template execution. Hence skipping the validation.").log();
            return true;
        } else {
            Result templateDetails = getTemplate(template_id, fabricRequestManager, fabricResponseManager);
            if (HelperMethods.hasRecords(templateDetails)) {
                return templateCompanyAndAccountValidation(templateDetails, company_id_from_identity,
                        fabricRequestManager, fabricResponseManager);
            } else {
                setTemplateFetchFailureError(fabricResponseManager);
                return false;
            }
        }
    }

    private void setCompanyValidationFailureError(FabricResponseManager fabricResponseManager) {
        JsonObject resultJson = new JsonObject();
        ErrorCodeEnum.ERR_13500.setErrorCode(resultJson);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
    }

    private void setTemplateFetchFailureError(FabricResponseManager fabricResponseManager) {
        JsonObject resultJson = new JsonObject();
        ErrorCodeEnum.ERR_13502.setErrorCode(resultJson);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resultJson);
    }

    private Result getTemplate(String template_id, FabricRequestManager fabricRequestManager,
            FabricResponseManager fabricResponseManager) {
        HashMap<String, String> fetchTemplateMap = new HashMap<>();
        fetchTemplateMap.put(DBPUtilitiesConstants.FILTER,
                "Template_id" + DBPUtilitiesConstants.EQUAL + template_id);

        diagnostic.prepareDebug("Fetching template with id: " + template_id).log();

        Result templateDetails = HelperMethods.callApi(fabricRequestManager, fetchTemplateMap,
                HelperMethods.getHeaders(fabricRequestManager), URLConstants.BB_TEMPLATE_GET);

        return templateDetails;
    }

    private boolean templateCompanyAndAccountValidation(Result templateDetails, String company_id_from_identity,
            FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        String company_id_key = "Company_id";
        String debit_account_key = "DebitAccount";

        String company_id = HelperMethods.getFieldValue(templateDetails, company_id_key);
        String debit_account = HelperMethods.getFieldValue(templateDetails, debit_account_key);

        diagnostic.prepareDebug(" Company_id fetched from template is: " + company_id).log();

        boolean companyValidationStatus = company_id.equals(company_id_from_identity);

        AccountsManager accManager = new AccountsManager(fabricRequestManager);
        boolean accountValidationStatus = accManager.validateInternalAccount(debit_account);

        if (companyValidationStatus && accountValidationStatus) {
            return true;
        } else {
            setCompanyValidationFailureError(fabricResponseManager);
            return false;
        }
    }

}

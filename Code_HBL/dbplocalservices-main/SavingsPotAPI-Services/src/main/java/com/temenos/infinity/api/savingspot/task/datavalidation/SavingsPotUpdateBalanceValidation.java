package com.temenos.infinity.api.savingspot.task.datavalidation;


import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.AccountsManager;
import com.kony.memorymgmt.SavingsPotManager;
import com.kony.task.datavalidation.AccountsValidationTask;
import com.kony.utilities.Constants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class SavingsPotUpdateBalanceValidation implements ObjectProcessorTask {

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
    throws Exception {
        SavingsPotManager savingsPotManager = new SavingsPotManager(fabricRequestManager, fabricResponseManager);
        JsonElement reqPayloadJEle = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
        if (HelperMethods.isJsonNotNull(reqPayloadJEle)) {
        	
            JsonObject requestPayload = reqPayloadJEle.getAsJsonObject();
            String potId = requestPayload.get(Constants.SAVINGSPOTID).toString();
            String savingsPotId =potId.replaceAll("\"", "");
            String customerId = HelperMethods.getAPIUserIdFromSession(fabricRequestManager).toString();
            String fundingAccId = savingsPotManager.getFundingAccount(customerId, savingsPotId);
            String fundingAccHoldingsId = savingsPotManager.getFundingAccountHoldingsId(customerId, savingsPotId);
            AccountsManager accountManager = new AccountsManager(fabricRequestManager, fabricResponseManager);
            Float accountBalance =  Float.parseFloat(accountManager.getBalanceOfInternalAccount(customerId, fundingAccHoldingsId));
            String amountToFund = requestPayload.get(Constants.AMOUNT).toString();
            
            String isCreditDebit = requestPayload.get(Constants.ISCREDITDEBIT).toString();
            String isCredDeb = isCreditDebit.replaceAll("\"", "");
            if (isCredDeb.equalsIgnoreCase(Constants.CREDIT)) {
                Float amountToFundInt = Float.parseFloat(amountToFund.replaceAll("\"", ""));
                if (amountToFundInt <= accountBalance) {
                    return true;
                } else {
                	JsonObject resPayload = null;
                	resPayload = ErrorCodeEnum.ERR_20045.setErrorCode(resPayload);
                    fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    return false;
                }
            } else if(isCredDeb.equalsIgnoreCase(Constants.DEBIT)){
                Float potBalance = Float.parseFloat(savingsPotManager.getAvailableBalanceInPot(customerId, savingsPotId));
                String amountToWithdraw = requestPayload.get(Constants.AMOUNT).toString();
                Float amountToWithdrawInt = Float.parseFloat(amountToWithdraw.replaceAll("\"", ""));
                if (amountToWithdrawInt <= potBalance) {
                    return true;
                } else {
                	JsonObject resPayload = null;
                	resPayload = ErrorCodeEnum.ERR_20046.setErrorCode(resPayload);
                    fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    return false;
                }
            }
        }
        return true;
    }
}
package com.temenos.dbx.product.accounts.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.accounts.resource.api.AccountsResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * 
 * @author sowmya.vandanapu
 *
 */
public class AccountInformationGetOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            AccountsResource accountsResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(AccountsResource.class);
            result = accountsResource.getAccountInformation(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            request.addRequestParam_("canProceed", "false");
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while fetching account details :" + e.getMessage(), e).log();
        } catch (Exception e) {
            request.addRequestParam_("canProceed", "false");
            alert.prepareError("Exception occured while fetching the account details :" + e.getMessage(), e).log();
        }

        return result;
    }
}

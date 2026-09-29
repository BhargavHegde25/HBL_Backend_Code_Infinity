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

public class AccountCheckIfAvailableOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            AccountsResource accountsResource = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(ResourceFactory.class).getResource(AccountsResource.class);
            result = accountsResource.getCustomerAssociatedToAccounts(methodID, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while fetching the customers associated to accounts" + e.getMessage(), e).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the customers associated to accounts" + e.getMessage(), e).log();
        }

        return result;
    }

}

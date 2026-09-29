package com.temenos.dbx.product.contract.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.contract.resource.api.ContractResource;
import com.kony.dbputilities.util.Log4j2Configurator;

/**
 * Fetches the contract feature action limits
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 * @since 2021.01
 */
public class ContractFeatureActionLimitsGetOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            ContractResource resource = DBPAPIAbstractFactoryImpl.getResource(ContractResource.class);
            result = resource.getContractFeatureActionLimits(methodId, inputArray, request, response);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("Exception occured while fetching contract feature and action limits" + e.getStackTrace()).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching contract feature and action limits" + e.getStackTrace()).log();
            ErrorCodeEnum.ERR_10775.setErrorCode(result);
        }

        return result;
    }

}

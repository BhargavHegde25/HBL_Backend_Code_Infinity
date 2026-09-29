package com.kony.adminconsole.multientity.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchMultiEntityOperation implements JavaService2  {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	MultiEntityResource companyLegalUnitResource = DBPAPIAbstractFactoryImpl.getInstance().
            getFactoryInstance(ResourceFactory.class).getResource(MultiEntityResource.class);
    @Override
    public Object invoke(String method, Object[] inputArray, DataControllerRequest dataControllerRequest,
    		DataControllerResponse dataControllerResponse) throws Exception{
		Log4j2Configurator.getInstance();
    	alert.prepareError("TEST").log();
        try {
            Result companyLegalUnits = companyLegalUnitResource.getAllCompanyLegalUnits(dataControllerRequest);
            return companyLegalUnits;
        } catch (Exception e) {
            alert.prepareError("Exception occurred in invoking FetchComapnyLegalUnitsInformation: " + e).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            
        }
    }
}

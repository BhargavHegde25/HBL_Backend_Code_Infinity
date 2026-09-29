package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.utilities.DBPServices;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class PassthroughServicesPreProcessor implements DataPreProcessor2 {

    @Override
    public boolean execute(@SuppressWarnings("rawtypes") HashMap inputMap, DataControllerRequest request,
            DataControllerResponse response, Result result) throws Exception {
		Log4j2Configurator.getInstance();
        String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
        request.getSession().setAttribute("backendToken", dbpServicesClaimsToken);
        return true;
    }

}
